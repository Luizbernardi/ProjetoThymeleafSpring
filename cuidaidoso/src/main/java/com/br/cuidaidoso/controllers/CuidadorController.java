package com.br.cuidaidoso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;
import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.repository.CuidadorRepository;
import com.br.cuidaidoso.util.UploadUtil;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/cuidador")
public class CuidadorController {

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @GetMapping("/cadastro")
    public ModelAndView cadastro(Cuidador cuidador) {
        ModelAndView mv = new ModelAndView("cuidador/cadastro");
        mv.addObject("usuario", new Cuidador());
        Perfil[] perfilCuidador = { Perfil.CUIDADOR };
        mv.addObject("perfils", perfilCuidador);
        Genero[] genero = { Genero.MASCULINO, Genero.FEMININO, Genero.OUTRO };
        mv.addObject("generos", genero);
        return mv;
    }

    @PostMapping("/cadastro-cuidador")
    public ModelAndView cadastro(@ModelAttribute Cuidador cuidador, @RequestParam("file") MultipartFile imagem) {
        ModelAndView mv = new ModelAndView("cuidador/cadastro");
        mv.addObject("usuario", cuidador);

        try {
            if (UploadUtil.fazerUploadImagem(imagem)) {
                cuidador.setImagem("static/images/imagem-uploads/" + imagem.getOriginalFilename());
            } else {
                cuidador.setImagem("static/images/imagem-uploads/images.png");
            }
            cuidador.setPerfil(Perfil.CUIDADOR);
            cuidadorRepository.save(cuidador);
            System.out.println("Cuidador salvo com sucesso" + cuidador.getNome() + "" + cuidador.getImagem());
            return home();
        } catch (Exception e) {
            mv.addObject("msgERRo", e.getMessage());
            System.out.println("Erro ao salvar o cuidador" + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/inicio")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home/index");
        return mv;
    }

}
