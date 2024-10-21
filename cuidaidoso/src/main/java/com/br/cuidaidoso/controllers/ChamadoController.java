package com.br.cuidaidoso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.br.cuidaidoso.enums.Prioridade;
import com.br.cuidaidoso.enums.Status;
import com.br.cuidaidoso.model.Chamado;

@Controller
@RequestMapping("/chamado")
public class ChamadoController {

    @GetMapping("/cadastro")
    public ModelAndView cadastro(Chamado chamado) {
        ModelAndView mv = new ModelAndView("chamado/cadastro");
        Status[] St = { Status.ABERTO };
        mv.addObject("statusChamado", St);
        Prioridade[] Pr = { Prioridade.ALTA, Prioridade.MEDIA, Prioridade.BAIXA };
        mv.addObject("prioridade", Pr);
        return mv;
    }
}