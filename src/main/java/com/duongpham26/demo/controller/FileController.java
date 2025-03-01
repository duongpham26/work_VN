package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.duongpham26.demo.entity.dto.response.job.ResUpdateJobDTO;
import com.duongpham26.demo.entity.dto.response.upload.ResUploadFileDTO;
import com.duongpham26.demo.service.FileService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.StorageException;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("/api/v1")
@RestController
public class FileController {

    private final FileService fileService;

    @Value("${duongpham26.upload-dir-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload a file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "folder") String folder)
            throws URISyntaxException, StorageException {

        // skill validation
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "pdf", "doc", "docx");
        // boolean isValid = allowedExtensions.stream().anyMatch(fileName::endsWith);
        @SuppressWarnings("null")
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.endsWith(item));

        if (!isValid) {
            throw new StorageException("Invalid file extension, only support: " + allowedExtensions);
        }

        // create folder if not exist
        this.fileService.createFolder(baseURI + folder);

        // save file
        String fileNameUpload = this.fileService.saveFile(file, folder);
        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(fileNameUpload, Instant.now());

        return ResponseEntity.ok().body(resUploadFileDTO);
    }

}
