package cz.ondrejvelisek.oauth.client.perun;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.Member;
import cz.ondrejvelisek.oauth.client.model.PerunException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class MembersManager extends Manager {

    public MembersManager(String token) {
        super(token);
    }

    public List<Member> getMembers(Integer vo) throws PerunException {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vo", vo);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getMembers", params), mapper.getTypeFactory().constructCollectionType(List.class, Member.class));
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    @Override
    protected String getManager() {
        return "membersManager";
    }
}
