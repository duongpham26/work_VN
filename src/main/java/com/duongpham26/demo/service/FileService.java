package com.duongpham26.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${duongpham26.upload-dir-uri}")
    private String baseURI;

    public void createFolder(String folder) {
        URI uri = URI.create(folder);
        Path path = Paths.get(uri);
        File file = new File(path.toString());
        if (!file.isDirectory()) {
            try {
                Files.createDirectories(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Folder is exist " + file.toPath());
        }
    }

    public String saveFile(MultipartFile file, String folder) throws URISyntaxException {
        // create unique file name
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
