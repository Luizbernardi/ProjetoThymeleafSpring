package com.br.cuidaidoso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;
import com.br.cuidaidoso.model.Admin;
import com.br.cuidaidoso.model.Endereco;
import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.repository.AdminRepository;
import com.br.cuidaidoso.service.EnderecoService;
import com.br.cuidaidoso.util.UploadUtil;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("/cadastro")
    public ModelAndView cadastro(Admin admin) {
        ModelAndView mv = new ModelAndView("admin/cadastro");
        mv.addObject("usuario", new Admin());
        Perfil[] perfilAdmin = { Perfil.ADMIN };
        mv.addObject("perfils", perfilAdmin);
        Genero[] genero = { Genero.MASCULINO, Genero.FEMININO, Genero.OUTRO };
        mv.addObject("generos", genero);
        return mv;
    }

    @PostMapping("/cadastro-admin")
    public ModelAndView cadastro(@ModelAttribute Admin admin, @RequestParam("file") MultipartFile imagem) {
        ModelAndView mv = new ModelAndView("admin/cadastro");
        mv.addObject("usuario", admin);

        try {
            if (UploadUtil.fazerUploadImagem(imagem)) {
                admin.setImagem("static/images/imagem-uploads/" + imagem.getOriginalFilename());
            } else {
                admin.setImagem("static/images/imagem-uploads/images.png");
            }
            admin.setPerfil(Perfil.ADMIN);
            Admin adminSalvo = adminRepository.save(admin);
            System.out.println("Admin salvo com sucesso: " + admin.getNome() + " " + admin.getImagem());

            // Redirecionar para a página de cadastro de endereço
            ModelAndView mvEndereco = new ModelAndView("admin/endereco/cadastro");
            mvEndereco.addObject("adminId", adminSalvo.getId());
            mvEndereco.addObject("endereco", new EnderecoRequest());
            return mvEndereco;
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o admin: " + e.getMessage());
            return mv;
        }
    }

    @PostMapping("/cadastro-endereco")
    public ModelAndView cadastrarEndereco(@ModelAttribute EnderecoRequest enderecoRequest,
            @RequestParam("adminId") Long adminId, Model model) {
        ModelAndView mv = new ModelAndView("admin/endereco/cadastro");
        mv.addObject("endereco", enderecoRequest);

        try {
            Endereco endereco = enderecoService.executa(enderecoRequest);
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new IllegalArgumentException("Admin não encontrado"));
            admin.setEndereco(endereco);
            adminRepository.save(admin);
            model.addAttribute("endereco", endereco);
            System.out.println("Endereço salvo com sucesso: " + enderecoRequest.getCep());
            return new ModelAndView("redirect:/admin/inicio");
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o endereço: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/inicio")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home/index");
        return mv;
    }
}