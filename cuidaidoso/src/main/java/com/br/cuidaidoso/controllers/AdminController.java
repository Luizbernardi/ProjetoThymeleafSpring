package com.br.cuidaidoso.controllers;

import com.br.cuidaidoso.enums.Genero;
import com.br.cuidaidoso.enums.Perfil;
import com.br.cuidaidoso.model.Admin;
import com.br.cuidaidoso.model.Cliente;
import com.br.cuidaidoso.model.Cuidador;
import com.br.cuidaidoso.model.Endereco;
import com.br.cuidaidoso.model.dto.EnderecoRequest;
import com.br.cuidaidoso.repository.AdminRepository;
import com.br.cuidaidoso.repository.ClienteRepository;
import com.br.cuidaidoso.repository.CuidadorRepository;
import com.br.cuidaidoso.service.AdminLogService;
import com.br.cuidaidoso.service.AdminService;
import com.br.cuidaidoso.service.EnderecoService;
import com.br.cuidaidoso.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private CuidadorRepository cuidadorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminLogService adminLogService;

    @Autowired
    private AdminService adminService;

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
            adminService.save(admin); // Use o serviço para salvar o admin
            System.out.println("Admin salvo com sucesso: " + admin.getNome() + " " + admin.getImagem());

            // Registrar ação
            adminLogService.registrarAcao(admin, "Cadastro admin");

            // Redirecionar para a página de cadastro de endereço
            ModelAndView mvEndereco = new ModelAndView("admin/endereco/cadastro");
            mvEndereco.addObject("adminId", admin.getId());
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

    @GetMapping("list-admin")
    public ModelAndView adminList() {
        ModelAndView mv = new ModelAndView("admin/list-admin");
        mv.addObject("admins", adminRepository.findAll());
        return mv;
    }

    @GetMapping("/list-cuidadores")
    public ModelAndView listCuidadores() {
        ModelAndView mv = new ModelAndView("admin/list-cuidadores");
        mv.addObject("cuidadores", cuidadorRepository.findAll());
        return mv;
    }

    @GetMapping("/list-clientes")
    public ModelAndView listClientes() {
        ModelAndView mv = new ModelAndView("admin/list-clientes");
        mv.addObject("clientes", clienteRepository.findAll());
        return mv;
    }

    @GetMapping("/inicio")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home/index");
        return mv;
    }

    @GetMapping("/excluir-admin/{id}")
    public String excluirAdmin(@PathVariable("id") Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin Id:" + id));
        // Registrar ação
        adminLogService.registrarAcao(admin, "Excluiu um administrador");
        adminRepository.deleteById(id);

        return "redirect:/admin/list-admin";
    }

    @GetMapping("/excluir-cliente/{id}")
    public String excluirCliente(@PathVariable("id") Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cliente Id:" + id));

        // Registrar ação
        adminLogService.registrarAcao(cliente, "Excluiu um cliente");
        clienteRepository.deleteById(id);

        return "redirect:/admin/list-clientes";
    }

    @GetMapping("/excluir-cuidador/{id}")
    public String excluirCuidador(@PathVariable("id") Long id) {
        Cuidador cuidador = cuidadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cuidador Id:" + id));
        // Registrar ação
        adminLogService.registrarAcao(cuidador, "Excluiu um cuidador");
        cuidadorRepository.deleteById(id);

        return "redirect:/admin/list-cuidadores";
    }

    @GetMapping("/editar-admin/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("admin/editar-admin");
        Perfil[] perfilAdmin = { Perfil.ADMIN };
        mv.addObject("perfils", perfilAdmin);
        Genero[] genero = { Genero.MASCULINO, Genero.FEMININO, Genero.OUTRO };
        mv.addObject("generos", genero);
        mv.addObject("usuario", adminRepository.findById(id).orElse(null));
        return mv;
    }

    @PostMapping("/editar-admin")
    public String editar(@ModelAttribute Admin admin, @RequestParam("file") MultipartFile imagem) {
        Admin existingAdmin = adminRepository.findById(admin.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin Id:" + admin.getId()));

        // Manter a senha existente se o campo senha estiver vazio
        if (admin.getSenha() == null || admin.getSenha().isEmpty()) {
            admin.setSenha(existingAdmin.getSenha());
        } else {
            admin.setSenha(adminService.encodePassword(admin.getSenha()));
        }

        // Preservar o caminho da imagem se não for alterado
        if (!imagem.isEmpty()) {
            if (UploadUtil.fazerUploadImagem(imagem)) {
                // Remover a imagem antiga se não for a imagem padrão
                if (!existingAdmin.getImagem().equals("static/images/imagem-uploads/images.png")) {
                    String caminhoImagemAntiga = "C:\\Users\\Luizb\\OneDrive\\Documentos\\WorkspaceVsCode\\ProjetoThymeleafSpring\\cuidaidoso\\src\\main\\resources\\"
                            + existingAdmin.getImagem();
                    UploadUtil.removerImagem(caminhoImagemAntiga);
                }
                admin.setImagem("static/images/imagem-uploads/" + imagem.getOriginalFilename());
            } else {
                admin.setImagem(existingAdmin.getImagem());
            }
        } else {
            admin.setImagem(existingAdmin.getImagem());
        }
        // Preservar o endereço existente
        admin.setEndereco(existingAdmin.getEndereco());

        admin.setPerfil(Perfil.ADMIN);
        adminRepository.save(admin);

        // Registrar ação
        adminLogService.registrarAcao(admin, "Editou um administrador");

        return "redirect:/admin/list-admin";
    }

    @GetMapping("/editar-cliente/{id}")
    public ModelAndView editarCliente(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("admin/editar-cliente");
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
            cliente.setSenha(adminService.encodePassword(cliente.getSenha()));
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

        // Registrar ação
        adminLogService.registrarAcao(cliente, "Editou um cliente");

        return "redirect:/admin/list-clientes";
    }

    @GetMapping("/editar-cuidador/{id}")
    public ModelAndView editarCuidador(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("admin/editar-cuidador");
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
            cuidador.setSenha(adminService.encodePassword(cuidador.getSenha()));
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

        // Registrar ação
        adminLogService.registrarAcao(cuidador, "Editou um cuidador");
        return "redirect:/admin/list-cuidadores";
    }

}