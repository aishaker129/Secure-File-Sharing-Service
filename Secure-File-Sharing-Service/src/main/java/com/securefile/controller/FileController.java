package com.securefile.controller;

import com.securefile.dto.response.ApiResponse;
import com.securefile.dto.response.FileDownloadResponse;
import com.securefile.dto.response.FileUploadResponse;
import com.securefile.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;
    @Value("${app.download.base-path}")
    private String downloadPath;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUploadResponse>> upload(@RequestParam("file")MultipartFile file) throws IOException{
        return ResponseEntity.ok(fileService.storeFile(file));
    }

    @GetMapping("/otp/{otp}")
    public ResponseEntity<ApiResponse<FileDownloadResponse>> getDownLink(@PathVariable Integer otp){
        return ResponseEntity.ok(fileService.validatorOtp(otp));
    }



    @GetMapping("/download/{token}")
    public ResponseEntity<Resource> download(@PathVariable String token) throws IOException {

        File file = fileService.getFileForDownload(token);

        Resource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(file.getName(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
