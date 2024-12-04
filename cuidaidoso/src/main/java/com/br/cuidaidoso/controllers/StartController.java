package com.br.cuidaidoso.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StartController {

    @GetMapping("home")
    public ModelAndView start() {
        ModelAndView mv = new ModelAndView("home/index");
        return mv;
    }

    @GetMapping("login")
    public ModelAndView login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_CUIDADOR")) {
                return new ModelAndView("redirect:/cuidador/home");
            } else if (role.equals("ROLE_CLIENTE")) {
                return new ModelAndView("redirect:/cliente/home");
            }
        }
        return new ModelAndView("login/login");
    }
}