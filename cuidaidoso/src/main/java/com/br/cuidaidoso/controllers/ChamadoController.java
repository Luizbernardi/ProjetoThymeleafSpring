package com.br.cuidaidoso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/chamado")
public class ChamadoController {

    @GetMapping("/cadastro")
    public ModelAndView ticket() {
        ModelAndView mv = new ModelAndView("chamado/cadastro");
        return mv;
    }

}
