package com.br.cuidaidoso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.br.cuidaidoso.enums.Prioridade;
import com.br.cuidaidoso.enums.Status;
import com.br.cuidaidoso.model.Chamado;
import com.br.cuidaidoso.repository.ChamadoRepository;

@Controller
@RequestMapping("/chamado")
public class ChamadoController {
    
    @Autowired
    private ChamadoRepository chamadoRepository;

    @GetMapping("/cadastro")
    public ModelAndView cadastro(Chamado chamado) {
        ModelAndView mv = new ModelAndView("chamado/cadastro");
        Status[] St = { Status.ABERTO, Status.ANDAMENTO, Status.FECHADO };
        mv.addObject("statusChamado", St);
        Prioridade[] Pr = { Prioridade.ALTA, Prioridade.MEDIA, Prioridade.BAIXA };
        mv.addObject("prioridade", Pr);
        return mv;
    }

    @PostMapping("/cadastro-chamado")
    public ModelAndView cadastroChamado(@ModelAttribute Chamado chamado) {
        chamadoRepository.save(chamado);
        ModelAndView mv = new ModelAndView("redirect:/home");
        return mv;
    }
    
    @GetMapping("/list-chamados")
    public ModelAndView lista() {
        ModelAndView mv = new ModelAndView("chamado/list-chamados");
        mv.addObject("chamados", chamadoRepository.findAll());
        return mv;
    }
}