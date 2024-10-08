package com.br.cuidaidoso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.service.EnderecoService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/endereco")
@Controller
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping("/cadastro")
    public ModelAndView mostrarFormulario() {
        ModelAndView mv = new ModelAndView("endereco/cadastro");
        mv.addObject("endereco", new EnderecoRequest());
        return mv;
    }

    @PostMapping("/cadastro-endereco")
    public ModelAndView consultaCep(@ModelAttribute EnderecoRequest enderecoRequest, Model model) {
        ModelAndView mv = new ModelAndView("endereco/cadastro");
        mv.addObject("endereco", enderecoRequest);

        try {
            model.addAttribute("endereco", enderecoService.executa(enderecoRequest));
            System.out.println("Endereço salvo com sucesso: " + enderecoRequest.getCep());
            return mv;
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o endereço: " + e.getMessage());
            return mv;
        }
    }

}
