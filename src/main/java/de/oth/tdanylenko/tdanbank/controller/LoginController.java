package de.oth.tdanylenko.tdanbank.controller;

import de.oth.tdanylenko.tdanbank.entity.*;
import de.oth.tdanylenko.tdanbank.repository.RolesRepository;
import de.oth.tdanylenko.tdanbank.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SessionAttributes({"currentUser"})
@Controller
public class LoginController {
    private static final Logger log = LogManager.getLogger(LoginController.class);
@Autowired
private UserRepository userRepo;

@Autowired
private RolesRepository rolesRepo;
     public static boolean isAdmin = false;
    public static boolean isAdmin2 = false;
private ArrayList <RoleTypes> test;

    public static final String STATUS_MESSAGE = "STATUS_MESSAGE";
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @RequestMapping(value = "/login-error", method = RequestMethod.GET)
    public String loginError(Model model) {
        log.info("Login attempt failed");
        model.addAttribute("error", "true");
        return "error";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "redirect:/login";
    }
    @RequestMapping(value = "/postLogin", method = RequestMethod.POST)
    public String postLogin(Model model, HttpSession session) {
        log.info("postLogin()");

        // read principal out of security context and set it to session
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        User loggedInUser = ((User) authentication.getPrincipal()).getUserDetails();

        model.addAttribute("currentUser", loggedInUser.getUsername());
        session.setAttribute("userId", loggedInUser.getUserID());
        List<String> collect = loggedInUser.getAuthorities().stream().map(x -> ((GrantedAuthority) x).toString()).collect(Collectors.toList());
        log.info(collect.contains(RoleTypes.parseRoleValue(RoleTypes.ROLE_MANAGER)));
 if(collect.contains(RoleTypes.parseRoleValue(RoleTypes.ROLE_MANAGER))) {
     return "redirect:/dashboard";
 } else if (collect.contains(RoleTypes.parseRoleValue(RoleTypes.ROLE_USER))) {
     return "redirect:/account";
 } else
     return "redirect:/account";
    }

    private void validatePrinciple(Object principal) {
        if (!(principal instanceof User)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }
}