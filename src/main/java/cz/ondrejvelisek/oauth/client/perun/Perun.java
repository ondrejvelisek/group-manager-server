package cz.ondrejvelisek.oauth.client.perun;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Perun {

    private String token;

    private AuthzResolver authzResolver;
    private GroupsManager groupsManager;
    private VosManager vosManager;
    private MembersManager membersManager;
    private UsersManager usersManager;

    public Perun(String token) {
        this.token = token;
    }

    public AuthzResolver authzResolver() {
        if (authzResolver == null) {
            authzResolver = new AuthzResolver(token);
            return authzResolver;
        } else {
            return authzResolver;
        }
    }

    public GroupsManager groupsManager() {
        if (groupsManager == null) {
            groupsManager = new GroupsManager(token);
            return groupsManager;
        } else {
            return groupsManager;
        }
    }

    public MembersManager membersManager() {
        if (membersManager == null) {
            membersManager = new MembersManager(token);
            return membersManager;
        } else {
            return membersManager;
        }
    }

    public UsersManager usersManager() {
        if (usersManager == null) {
            usersManager = new UsersManager(token);
            return usersManager;
        } else {
            return usersManager;
        }
    }

    public VosManager vosManager() {
        if (vosManager == null) {
            vosManager = new VosManager(token);
            return vosManager;
        } else {
            return vosManager;
        }
    }


}
