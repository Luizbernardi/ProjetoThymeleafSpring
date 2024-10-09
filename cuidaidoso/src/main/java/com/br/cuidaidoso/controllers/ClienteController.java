package com.br.cuidaidoso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.br.cuidaidoso.model.Cliente;
import com.br.cuidaidoso.repository.ClienteRepository;
import com.br.cuidaidoso.service.ClienteLogService;
import com.br.cuidaidoso.util.UploadUtil;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteLogService clienteLogService;

    @GetMapping("/inicio")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home/index");
        return mv;
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro(Cliente cliente) {
        ModelAndView mv = new ModelAndView("cliente/cadastro");
        mv.addObject("usuario", new Cliente());
        Perfil[] perfilCliente = { Perfil.CLIENTE };
        mv.addObject("perfils", perfilCliente);
        Genero[] genero = { Genero.MASCULINO, Genero.FEMININO, Genero.OUTRO };
        mv.addObject("generos", genero);
        return mv;
    }

    @PostMapping("/cadastro-cliente")
    public ModelAndView cadastro(@ModelAttribute Cliente cliente, @RequestParam("file") MultipartFile imagem) {
        ModelAndView mv = new ModelAndView("cliente/cadastro");
        mv.addObject("usuario", cliente);

        try {
            if (UploadUtil.fazerUploadImagem(imagem)) {
                cliente.setImagem("static/images/imagem-uploads/" + imagem.getOriginalFilename());
            } else {
                cliente.setImagem("static/images/imagem-uploads/images.png");
            }
            cliente.setPerfil(Perfil.CLIENTE);
            clienteRepository.save(cliente);
            System.out.println("Cliente salvo com sucesso: " + cliente.getNome() + " " + cliente.getImagem());

            // Registrar ação
            clienteLogService.registrarAcao(cliente, "Cadastro do Cliente");

            ModelAndView mvHome = new ModelAndView("redirect:/cliente/inicio");
            return mvHome;
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o cliente: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/list-clientes")
    public ModelAndView listClientes() {
        ModelAndView mv = new ModelAndView("cliente/list-clientes");
        mv.addObject("clientes", clienteRepository.findAll());
        return mv;
    }

    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable("id") Long id) {
        clienteRepository.deleteById(id);
        return "redirect:/cliente/list-clientes";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarCliente(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("cliente/editar");
        Perfil[] perfilCliente = { Perfil.CLIENTE };
        mv.addObject("perfils", perfilCliente);
        Genero[] genero = { Genero.MASCULINO, Genero.FEMININO, Genero.OUTRO };
        mv.addObject("generos", genero);
        mv.addObject("usuario", clienteRepository.findById(id).orElse(null));
        return mv;
    }

    @PostMapping("/editar-cliente")
    public String editarCliente(@ModelAttribute Cliente cliente) {
        Cliente existingCliente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid cliente Id:" + cliente.getId()));

        // Manter a senha existente se o campo senha estiver vazio
        if (cliente.getSenha() == null || cliente.getSenha().isEmpty()) {
            cliente.setSenha(existingCliente.getSenha());
        }

        cliente.setPerfil(Perfil.CLIENTE);
        clienteRepository.save(cliente);

        return "redirect:/cliente/list-clientes";
    }

}
