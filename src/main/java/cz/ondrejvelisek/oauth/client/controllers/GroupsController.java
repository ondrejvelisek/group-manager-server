package cz.ondrejvelisek.oauth.client.controllers;

import cz.ondrejvelisek.oauth.client.model.Group;
import cz.ondrejvelisek.oauth.client.model.Member;
import cz.ondrejvelisek.oauth.client.model.PerunException;
import cz.ondrejvelisek.oauth.client.model.User;
import cz.ondrejvelisek.oauth.client.model.Vo;
import cz.ondrejvelisek.oauth.client.perun.Perun;
import cz.ondrejvelisek.oauth.client.perun.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/**")
public class GroupsController {

    @RequestMapping("/{voShortName}/{groupName}")
    public String group(ServletRequest req, @PathVariable String voShortName, @PathVariable String groupName) {
        String token = (String) req.getAttribute(OauthFilter.TOKEN_ARG_NAME);
        Perun p = new Perun(token);

        try {
            req.setAttribute("perunPrincipal", p.authzResolver().getPerunPrincipal());



            Vo vo = p.vosManager().getVoByShortName(voShortName);
            List<Group> groups = p.groupsManager().getGroups(vo.getId());

            Group group = Utils.getGroupByName(groups, groupName);

            req.setAttribute("vo", vo);
            req.setAttribute("group", group);
            req.setAttribute("parentGroup", Utils.getParentGroup(groups, group));
            req.setAttribute("groups", Utils.getSubgroups(groups, group));
            req.setAttribute("breadcrumbs", Utils.getBreadcrumbs(groups, group));
            req.setAttribute("isVo", Utils.isVo(group));
            req.setAttribute("isMembers", Utils.isMembers(group));



            List<Member> members = new ArrayList<>();
            if (!Utils.isVo(group)) {
                members = p.groupsManager().getGroupMembers(group.getId());
            }
            Map<Member, User> memberUsers = new HashMap<>();
            for (Member member : members) {
                memberUsers.put(member, p.usersManager().getUserById(member.getUserId()));
            }

            req.setAttribute("memberUsers", memberUsers);



            List<Member> voMembers = Utils.diff(p.membersManager().getMembers(vo.getId()), Utils.directMembers(members));
            Map<Member, User> voMemberUsers = new HashMap<>();
            for (Member member : voMembers) {
                voMemberUsers.put(member, p.usersManager().getUserById(member.getUserId()));
            }

            req.setAttribute("voMemberUsers", voMemberUsers);



        } catch (PerunException e) {
            req.setAttribute("e", e);
            return "exception";
        }

        return "groups";
    }

    @RequestMapping("/{voShortName}")
    public String vo(ServletRequest req, @PathVariable String voShortName) {
        return group(req, voShortName, null);
    }

    @RequestMapping(value = "/{voShortName}/{groupName}/remove-member/{memberId}", method = RequestMethod.POST)
    public String removeMember(ServletRequest req, @PathVariable String voShortName, @PathVariable String groupName, @PathVariable int memberId) {
        String token = (String) req.getAttribute(OauthFilter.TOKEN_ARG_NAME);
        Perun p = new Perun(token);

        try {
            Vo vo = p.vosManager().getVoByShortName(voShortName);
            Group group = p.groupsManager().getGroupByName(vo.getId(), groupName);
            p.groupsManager().removeMember(group.getId(), memberId);
        } catch (PerunException e) {
            req.setAttribute("e", e);
            return "exception";
        }

        return "redirect:/"+voShortName+"/"+groupName;
    }

    @RequestMapping(value = "/{voShortName}/{groupName}/add-member/{memberId}", method = RequestMethod.POST)
    public String addMember(ServletRequest req, @PathVariable String voShortName, @PathVariable String groupName, @PathVariable int memberId) {
        String token = (String) req.getAttribute(OauthFilter.TOKEN_ARG_NAME);
        Perun p = new Perun(token);

        try {
            Vo vo = p.vosManager().getVoByShortName(voShortName);
            Group group = p.groupsManager().getGroupByName(vo.getId(), groupName);
            p.groupsManager().addMember(group.getId(), memberId);
        } catch (PerunException e) {
            req.setAttribute("e", e);
            return "exception";
        }

        return "redirect:/"+voShortName+"/"+groupName;
    }









}
