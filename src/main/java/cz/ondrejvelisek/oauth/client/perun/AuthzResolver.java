package cz.ondrejvelisek.oauth.client.perun;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import cz.ondrejvelisek.oauth.client.model.PerunPrincipal;
import cz.ondrejvelisek.oauth.client.perun.Manager;

import java.io.IOException;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class AuthzResolver extends Manager {

    public AuthzResolver(String token) {
        super(token);
    }

    public PerunPrincipal getPerunPrincipal() throws PerunException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getPerunPrincipal"), PerunPrincipal.class);
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    @Override
    protected String getManager() {
        return "authzResolver";
    }
}
