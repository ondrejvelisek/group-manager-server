package cz.ondrejvelisek.oauth.client.perun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.Member;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import cz.ondrejvelisek.oauth.client.model.PerunPrincipal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class GroupsManager extends Manager {

    public GroupsManager(String token) {
        super(token);
    }

    public List<Group> getGroups(Integer vo) throws PerunException {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vo", vo);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getGroups", params), mapper.getTypeFactory().constructCollectionType(List.class, Group.class));
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    public List<Member> getGroupMembers(Integer group) throws PerunException {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("group", group);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getGroupMembers", params), mapper.getTypeFactory().constructCollectionType(List.class, Member.class));
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    public Group getGroupByName(Integer vo, String name) throws PerunException {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vo", vo);
            params.put("name", name);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(get("getGroupByName", params), Group.class);
        } catch (IOException e) {
            throw new PerunException(e);
        }

    }

    public void addMember(Integer group, Integer member) throws PerunException {

        Map<String, Object> params = new HashMap<>();
        params.put("group", group);
        params.put("member", member);
        post("addMember", params);

    }

    public void removeMember(Integer group, Integer member) throws PerunException {

        Map<String, Object> params = new HashMap<>();
        params.put("group", group);
        params.put("member", member);
        post("removeMember", params);

    }

    @Override
    protected String getManager() {
        return "groupsManager";
    }
}
