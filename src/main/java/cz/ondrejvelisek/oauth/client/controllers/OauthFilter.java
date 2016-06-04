package cz.ondrejvelisek.oauth.client.controllers;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationErrorResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.AuthorizationResponse;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.http.HttpRequest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class OauthFilter implements Filter {

    public static String AUTHORIZATION_ENDPOINT = "https://perun-dev.meta.zcu.cz/krb/oauth2/authorize";
    public static String TOKEN_ENDPOINT = "https://perun-dev.meta.zcu.cz/oauth2/token";
    public static String RESOURCE_ENDPOINT = "https://perun-dev.meta.zcu.cz/oauth/rpc-xvelisek/json";

    public static String REDIRECT_PATH = "/oauth-callback";
    public static String REDIRECT_URI = "http://localhost:8080"+REDIRECT_PATH;

    public static String TOKEN_ARG_NAME = "token";
    public static String STATE_ARG_NAME = "state";

    public static String CLIENT_ID = "group-manager-server-side";
    public static String CLIENT_SECRET = "a4508288-3572-436f-b216-ce93f96b4684";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // we do not want to map /callback when obtaining code and token
        String path = ((HttpServletRequest) request).getRequestURI();
        if (path.startsWith(REDIRECT_PATH)) {
            callback(req, res);
            return;
        }

        // We do not want to authenticate to resources
        if (path.startsWith("/resources")) {
            chain.doFilter(req, res);
            return;
        }

        // If cookie with token exists we can continue
        Cookie token = getCookie(req, TOKEN_ARG_NAME);
        if (token != null && token.getValue() != null && token.getValue().length() > 0) {
            req.setAttribute(TOKEN_ARG_NAME, token.getValue());
            chain.doFilter(req, res);
            return;
        }

        // Token does not exist. We have to obtain it. Begin with code.
        obtainCode(req, res);

    }

    private Cookie getCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private void obtainCode(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        // Generate and save state to cookie
        State state = new State();
        Cookie stateCookie = new Cookie(STATE_ARG_NAME, state.toString());
        stateCookie.setPath("/");
        res.addCookie(stateCookie);

        try {
            // Prepare and data and pass control to authorization server.
            AuthorizationRequest authenticationRequest = new AuthorizationRequest.Builder(
                    new ResponseType(ResponseType.Value.CODE),
                    new ClientID(CLIENT_ID)).
                    redirectionURI(new URI(REDIRECT_URI+req.getRequestURI())).
                    endpointURI(new URI(AUTHORIZATION_ENDPOINT)).
                    state(state).
                    build();
            res.sendRedirect(authenticationRequest.toURI().toASCIIString());

        } catch (URISyntaxException | SerializeException e) {
            errorForward(req, res, e);
        }

    }

    public void callback(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            // Exchange code for access token
            AccessToken accessToken = tokenRequest(req, res, extractCode(req, res));

            // Save token to cookie
            Cookie tokenCookie = new Cookie(TOKEN_ARG_NAME, accessToken.toString());
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge((int) (accessToken.getLifetime() - 3));
            res.addCookie(tokenCookie);
            req.setAttribute(TOKEN_ARG_NAME, accessToken);

            // Redirect to original place
            res.sendRedirect(req.getRequestURI().replace(REDIRECT_PATH,""));

        } catch (URISyntaxException | ParseException | SerializeException e) {
            errorForward(req, res, e);
        }

    }



    private AuthorizationCode extractCode(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException, URISyntaxException, ParseException {

        URI callbackUri = new URI(req.getRequestURI()+"?"+req.getQueryString());

        AuthorizationResponse response = AuthorizationResponse.parse(callbackUri);

        if (!response.indicatesSuccess()) {
            AuthorizationErrorResponse errorRes = (AuthorizationErrorResponse) response;
            ErrorObject error = errorRes.getErrorObject();
            errorForward(req, res, new PerunException(error.getCode(), error.getDescription()));
        }

        AuthorizationSuccessResponse successRes = (AuthorizationSuccessResponse) response;

        // Protection against XCRF attack
        verifyState(req, res, successRes.getState());

        return successRes.getAuthorizationCode();

    }

    private void verifyState(HttpServletRequest req, HttpServletResponse res, State receivedState) throws IOException, ServletException {

        Cookie state = getCookie(req, STATE_ARG_NAME);

        if (state != null && !state.getValue().equals(receivedState.toString())) {
            errorForward(req, res, new PerunException("IllegalStateException", "States doesn't match. XCRF attack? " +
                    "Send state: " + state.getValue() + " Received state: " + receivedState));
        }

    }

    private AccessToken tokenRequest(HttpServletRequest req, HttpServletResponse res, AuthorizationCode authorizationCode)
            throws URISyntaxException, ParseException, SerializeException, IOException, ServletException {

        URI tokenEndpoint = new URI(TOKEN_ENDPOINT);

        URI redirectUri = new URI(REDIRECT_URI+req.getRequestURI().replace(REDIRECT_PATH, ""));
        AuthorizationGrant codeGrant = new AuthorizationCodeGrant(authorizationCode, redirectUri);

        ClientAuthentication clientCredentials = new ClientSecretBasic(new ClientID(CLIENT_ID), new Secret(CLIENT_SECRET));

        TokenRequest request = new TokenRequest(tokenEndpoint, clientCredentials, codeGrant);

        TokenResponse response = TokenResponse.parse(request.toHTTPRequest().send());

        if (!response.indicatesSuccess()) {
            TokenErrorResponse errorRes = (TokenErrorResponse) response;
            ErrorObject error = errorRes.getErrorObject();
            errorForward(req, res, new PerunException(error.getCode(), error.getDescription()));
        }

        AccessTokenResponse successResponse = (AccessTokenResponse) response;

        return successResponse.getAccessToken();
    }


    private void errorForward(ServletRequest req, ServletResponse res, Throwable e) throws IOException, ServletException {
        req.setAttribute("e", e);
        req.getRequestDispatcher("exception").forward(req, res);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
}
