package cz.ondrejvelisek.oauth.client.perun;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import cz.ondrejvelisek.oauth.client.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class UsersManager extends Manager {

    public UsersManager(String token) {
        super(token);
    }

    public User getUserById(Integer id) throws PerunException {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getUserById", params), User.class);
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    public User getUserByMember(Integer memberId) throws PerunException {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("member", memberId);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getUserByMember", params), User.class);
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    @Override
    protected String getManager() {
        return "usersManager";
    }
}
