package com.br.cuidaidoso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;
import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.model.Endereco;
import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.repository.CuidadorRepository;
import com.br.cuidaidoso.service.EnderecoService;
import com.br.cuidaidoso.util.UploadUtil;

@Controller
@RequestMapping("/cuidador")
public class CuidadorController {

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @Autowired
    private EnderecoService enderecoService;

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
            Cuidador cuidadorSalvo = cuidadorRepository.save(cuidador);
            System.out.println("Cuidador salvo com sucesso: " + cuidador.getNome() + " " + cuidador.getImagem());

            // Redirecionar para a página de cadastro de endereço
            ModelAndView mvEndereco = new ModelAndView("cuidador/endereco/cadastro");
            mvEndereco.addObject("cuidadorId", cuidadorSalvo.getId());
            mvEndereco.addObject("endereco", new EnderecoRequest());
            return mvEndereco;
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o cuidador: " + e.getMessage());
            return mv;
        }
    }

    @PostMapping("/cadastro-endereco")
    public ModelAndView cadastrarEndereco(@ModelAttribute EnderecoRequest enderecoRequest,
            @RequestParam("cuidadorId") Long cuidadorId, Model model) {
        ModelAndView mv = new ModelAndView("cuidador/endereco/cadastro");
        mv.addObject("endereco", enderecoRequest);

        try {
            Endereco endereco = enderecoService.executa(enderecoRequest);
            Cuidador cuidador = cuidadorRepository.findById(cuidadorId)
                    .orElseThrow(() -> new IllegalArgumentException("Cuidador não encontrado"));
            cuidador.setEndereco(endereco);
            cuidadorRepository.save(cuidador);
            model.addAttribute("endereco", endereco);
            System.out.println("Endereço salvo com sucesso: " + enderecoRequest.getCep());
            return new ModelAndView("redirect:/cuidador/inicio");
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o endereço: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/list-cuidadores")
    public ModelAndView listCuidadores() {
        ModelAndView mv = new ModelAndView("cuidador/list-cuidadores");
        mv.addObject("cuidadores", cuidadorRepository.findAll());
        return mv;
    }

    @GetMapping("/inicio")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home/index");
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String excluirCuidador(@PathVariable("id") Long id) {
        cuidadorRepository.deleteById(id);
        return "redirect:/cuidador/list-cuidadores";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarCuidador(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("cuidador/editar");
        Perfil[] perfilCuidador = { Perfil.CUIDADOR };
        mv.addObject("perfils", perfilCuidador);
        Genero[] genero = { Genero.MASCULINO, Genero.FEMININO, Genero.OUTRO };
        mv.addObject("generos", genero);
        mv.addObject("usuario", cuidadorRepository.findById(id).orElse(null));
        return mv;
    }

    @PostMapping("/editar-cuidador")
    public String editarCuidador(@ModelAttribute Cuidador cuidador) {
        cuidador.setPerfil(Perfil.CUIDADOR); // Garantir que o perfil seja CUIDADOR
        cuidadorRepository.save(cuidador);
        return "redirect:/cuidador/list-cuidadores";
    }

}