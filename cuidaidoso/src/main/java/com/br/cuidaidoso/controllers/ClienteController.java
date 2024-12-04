package com.br.cuidaidoso.controllers;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;
import com.br.cuidaidoso.model.Cliente;
import com.br.cuidaidoso.model.Endereco;
import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.repository.ClienteRepository;
import com.br.cuidaidoso.service.ClienteLogService;
import com.br.cuidaidoso.service.ClienteService;
import com.br.cuidaidoso.service.EnderecoService;
import com.br.cuidaidoso.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteLogService clienteLogService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ClienteService clienteService;

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
            clienteService.save(cliente); // Use o serviço para salvar o cliente
            System.out.println("Cliente salvo com sucesso: " + cliente.getNome() + " " + cliente.getImagem());

            // Registrar ação
            clienteLogService.registrarAcao(cliente, "Cadastro do Cliente");

            // Redirecionar para a página de cadastro de endereço
            ModelAndView mvEndereco = new ModelAndView("cliente/endereco/cadastro");
            mvEndereco.addObject("clienteId", cliente.getId());
            mvEndereco.addObject("endereco", new EnderecoRequest());
            return mvEndereco;
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o cliente: " + e.getMessage());
            return mv;
        }
    }

    @PostMapping("/cadastro-endereco")
    public ModelAndView cadastrarEndereco(@ModelAttribute EnderecoRequest enderecoRequest,
            @RequestParam("clienteId") Long clienteId, Model model) {
        ModelAndView mv = new ModelAndView("cliente/endereco/cadastro");
        mv.addObject("endereco", enderecoRequest);

        try {
            Endereco endereco = enderecoService.executa(enderecoRequest);
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            cliente.setEndereco(endereco);
            clienteRepository.save(cliente);
            model.addAttribute("endereco", endereco);
            System.out.println("Endereço salvo com sucesso: " + enderecoRequest.getCep());
            return new ModelAndView("redirect:/cliente/inicio");
        } catch (Exception e) {
            mv.addObject("msgErro", e.getMessage());
            System.out.println("Erro ao salvar o endereço: " + e.getMessage());
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
        return "redirect:/admin/list-clientes";
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
    public String editarCliente(@ModelAttribute Cliente cliente, @RequestParam("file") MultipartFile imagem) {
        Cliente existingCliente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid cliente Id:" + cliente.getId()));

        // Manter a senha existente se o campo senha estiver vazio
        if (cliente.getSenha() == null || cliente.getSenha().isEmpty()) {
            cliente.setSenha(existingCliente.getSenha());
        } else {
            cliente.setSenha(clienteService.encodePassword(cliente.getSenha()));
        }

        // Preservar o caminho da imagem se não for alterado
        if (!imagem.isEmpty()) {
            if (UploadUtil.fazerUploadImagem(imagem)) {
                // Remover a imagem antiga se não for a imagem padrão
                if (!existingCliente.getImagem().equals("static/images/imagem-uploads/images.png")) {
                    String caminhoImagemAntiga = "C:\\Users\\Luizb\\OneDrive\\Documentos\\WorkspaceVsCode\\ProjetoThymeleafSpring\\cuidaidoso\\src\\main\\resources\\"
                            + existingCliente.getImagem();
                    UploadUtil.removerImagem(caminhoImagemAntiga);
                }
                cliente.setImagem("static/images/imagem-uploads/" + imagem.getOriginalFilename());
            } else {
                cliente.setImagem(existingCliente.getImagem());
            }
        } else {
            cliente.setImagem(existingCliente.getImagem());
        }
        // Preservar o endereço existente
        cliente.setEndereco(existingCliente.getEndereco());

        cliente.setPerfil(Perfil.CLIENTE);
        clienteRepository.save(cliente);

        return "redirect:/admin/list-clientes";
    }
}