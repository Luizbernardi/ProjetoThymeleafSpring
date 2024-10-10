package com.br.cuidaidoso.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

    public static boolean fazerUploadImagem(MultipartFile imagem) {

        boolean sucessoUpload = false;

        if (!imagem.isEmpty()) {
            String nomeArquivo = imagem.getOriginalFilename();
            try {

                String pastaUploadImagem = "C:\\Users\\Luizb\\OneDrive\\Documentos\\WorkspaceVsCode\\ProjetoThymeleafSpring\\cuidaidoso\\src\\main\\resources\\static\\images\\imagem-uploads";
                File dir = new File(pastaUploadImagem);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(dir.getAbsolutePath() + File.separator + nomeArquivo);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

                stream.write(imagem.getBytes());
                stream.close();

                System.out.println("Armazenado em: " + serverFile.getAbsolutePath());
                System.out.println("Você fez o upload do arquivo " + nomeArquivo + " com sucesso");

                sucessoUpload = true;

            } catch (Exception e) {
                System.out.println("Você falhou em carregar o arquivo: " + nomeArquivo + " => " + e.getMessage());
            }

        } else {
            System.out.println("Você falhou em carregar o arquivo por ele estar vazio");
        }

        return sucessoUpload;
    }

    public static void removerImagem(String caminhoImagem) {
        try {
            File file = new File(caminhoImagem);
            if (file.exists()) {
                file.delete();
                System.out.println("Imagem removida: " + caminhoImagem);
            } else {
                System.out.println("Imagem não encontrada: " + caminhoImagem);
            }
        } catch (Exception e) {
            System.out.println("Erro ao remover a imagem: " + caminhoImagem + " => " + e.getMessage());
        }
    }
}