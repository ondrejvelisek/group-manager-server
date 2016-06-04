package cz.ondrejvelisek.oauth.client.model;

import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunPrincipal {

    private User user;
    private String actor;
    private String userId;
    private Map<String, String> additionalInformations;
    private String extSourceType;
    private String extSourceLoa;
    private String extSourceName;
    private Map<String, Object> roles;
    private boolean authzInitialized;

    public boolean isAuthzInitialized() {
        return authzInitialized;
    }

    public void setAuthzInitialized(boolean authzInitialized) {
        this.authzInitialized = authzInitialized;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getAdditionalInformations() {
        return additionalInformations;
    }

    public void setAdditionalInformations(Map<String, String> additionalInformations) {
        this.additionalInformations = additionalInformations;
    }

    public String getExtSourceType() {
        return extSourceType;
    }

    public void setExtSourceType(String extSourceType) {
        this.extSourceType = extSourceType;
    }

    public String getExtSourceLoa() {
        return extSourceLoa;
    }

    public void setExtSourceLoa(String extSourceLoa) {
        this.extSourceLoa = extSourceLoa;
    }

    public String getExtSourceName() {
        return extSourceName;
    }

    public void setExtSourceName(String extSourceName) {
        this.extSourceName = extSourceName;
    }

    public Map<String, Object> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Object> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "PerunPrincipal{" +
                "user=" + user +
                ", actor='" + actor + '\'' +
                ", userId='" + userId + '\'' +
                ", additionalInformations=" + additionalInformations +
                ", extSourceType='" + extSourceType + '\'' +
                ", extSourceLoa='" + extSourceLoa + '\'' +
                ", extSourceName='" + extSourceName + '\'' +
                ", roles=" + roles +
                ", authzInitialized=" + authzInitialized +
                '}';
    }
}
