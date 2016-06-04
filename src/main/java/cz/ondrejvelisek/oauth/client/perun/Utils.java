package cz.ondrejvelisek.oauth.client.perun;

import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.Member;
import cz.ondrejvelisek.oauth.client.model.PerunBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Utils {



    public static <P extends PerunBean> List<P> diff(List<P> all, List<P> diff) {
        List<P> result = new ArrayList<>();
        result.addAll(all);
        result.removeAll(diff);
        return result;
    }

    public static List<Member> directMembers(List<Member> members) {
        List<Member> result = new ArrayList<> ();
        for (Member member : members) {
            if (member.getMembershipType().equals("DIRECT")) {
                result.add(member);
            }
        }
        return result;
    }

    public static Group getParentGroup(List<Group> groups, Group group) {
        if (group == null) {
            return null;
        }
        return getGroupById(groups, group.getParentGroupId());
    }

    public static List<Group> getBreadcrumbs(List<Group> groups, Group group) {
        return getBreadcrumbs(groups, group, new ArrayList<Group>());
    }

    public static List<Group> getBreadcrumbs(List<Group> groups, Group group, List<Group> result) {
        if (group == null) {
            return result;
        } else {
            result.add(0, group);
            return getBreadcrumbs(groups, getParentGroup(groups, group), result);
        }
    }

    public static boolean isVo(Group group) {
        return (group == null);
    }

    public static boolean isMembers(Group group) {
        return (group != null && group.getName().equals("members"));
    }

    public static Group getGroupByName(List<Group> groups, String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group;
            }
        }
        return null;
    }

    public static Group getGroupById(List<Group> groups, int groupId) {
        for (Group group : groups) {
            if (group.getId() == groupId) {
                return group;
            }
        }
        return null;
    }

    public static List<Group> getSubgroups(List<Group> groups, Group group) {
        List<Group> result = new ArrayList<>();

        if (group != null) {
            for (Group g : groups) {
                if (g.getParentGroupId() == group.getId()) {
                    result.add(g);
                }
            }
        } else {
            for (Group g : groups) {
                if (getGroupById(groups, g.getParentGroupId()) == null) {
                    result.add(g);
                }
            }
        }

        return result;
    }

    public static boolean isWrapperType(Class<?> clazz)
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret.contains(clazz);
    }

}
