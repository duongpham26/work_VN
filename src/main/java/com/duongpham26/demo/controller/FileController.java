package com.duongpham26.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.duongpham26.demo.entity.dto.response.upload.ResUploadFileDTO;
import com.duongpham26.demo.service.FileService;
import com.duongpham26.demo.util.annotation.ApiMessage;
import com.duongpham26.demo.util.error.StorageException;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/files")
    @ApiMessage("Download file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, FileNotFoundException, URISyntaxException {
        if (fileName == null || folder == null) {
            throw new StorageException("Missing required params (fileName or folder)");
        }

        // check file exist
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("fileName with name = " + fileName + " not found");
        }

        // download a file
        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
