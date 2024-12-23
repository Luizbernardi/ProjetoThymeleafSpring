package com.br.cuidaidoso.controllers;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;
import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.model.Endereco;
import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.repository.CuidadorRepository;
import com.br.cuidaidoso.service.CuidadorLogService;
import com.br.cuidaidoso.service.CuidadorService;
import com.br.cuidaidoso.service.EnderecoService;
import com.br.cuidaidoso.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cuidador")
public class CuidadorController {

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @Autowired
    private CuidadorLogService cuidadorLogService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private CuidadorService cuidadorService;

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
            cuidadorService.save(cuidador); // Use o serviço para salvar o cuidador
            System.out.println("Cuidador salvo com sucesso: " + cuidador.getNome() + " " + cuidador.getImagem());
            // Registrar ação
            cuidadorLogService.registrarAcao(cuidador, "Cadastro Cuidador");

            // Redirecionar para a página de cadastro de endereço
            ModelAndView mvEndereco = new ModelAndView("cuidador/endereco/cadastro");
            mvEndereco.addObject("cuidadorId", cuidador.getId());
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
        return "redirect:/admin/list-cuidadores";
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
    public String editarCuidador(@ModelAttribute Cuidador cuidador, @RequestParam("file") MultipartFile imagem) {
        Cuidador existingCuidador = cuidadorRepository.findById(cuidador.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid cuidador Id:" + cuidador.getId()));

        // Manter a senha existente se o campo senha estiver vazio
        if (cuidador.getSenha() == null || cuidador.getSenha().isEmpty()) {
            cuidador.setSenha(existingCuidador.getSenha());
        } else {
            cuidador.setSenha(cuidadorService.encodePassword(cuidador.getSenha()));
        }

        // Preservar o caminho da imagem se não for alterado
        if (!imagem.isEmpty()) {
            if (UploadUtil.fazerUploadImagem(imagem)) {
                // Remover a imagem antiga se não for a imagem padrão
                if (!existingCuidador.getImagem().equals("static/images/imagem-uploads/images.png")) {
                    String caminhoImagemAntiga = "C:\\Users\\Luizb\\OneDrive\\Documentos\\WorkspaceVsCode\\ProjetoThymeleafSpring\\cuidaidoso\\src\\main\\resources\\"
                            + existingCuidador.getImagem();
                    UploadUtil.removerImagem(caminhoImagemAntiga);
                }
                cuidador.setImagem("static/images/imagem-uploads/" + imagem.getOriginalFilename());
            } else {
                cuidador.setImagem(existingCuidador.getImagem());
            }
        } else {
            cuidador.setImagem(existingCuidador.getImagem());
        }

        // Preservar o endereço existente
        cuidador.setEndereco(existingCuidador.getEndereco());

        cuidador.setPerfil(Perfil.CUIDADOR); // Garantir que o perfil seja CUIDADOR
        cuidadorRepository.save(cuidador);
        return "redirect:/admin/list-cuidadores";
    }
}