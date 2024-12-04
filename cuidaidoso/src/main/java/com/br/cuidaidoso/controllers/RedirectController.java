package com.br.cuidaidoso.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirectController {

    @GetMapping("/redirect")
    public ModelAndView redirect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_CUIDADOR")) {
                return new ModelAndView("redirect:/cuidador/list-cuidadores");
            } else if (role.equals("ROLE_CLIENTE")) {
                return new ModelAndView("redirect:/cliente/list-clientes");
            } else if (role.equals("ROLE_ADMIN")) {
                return new ModelAndView("redirect:/admin/list-admin");
            }
        }
        return new ModelAndView("redirect:/login");
    }
}