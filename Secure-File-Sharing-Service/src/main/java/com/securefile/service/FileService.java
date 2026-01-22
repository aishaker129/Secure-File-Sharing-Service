package com.securefile.service;

import com.securefile.dto.response.ApiResponse;
import com.securefile.dto.response.FileDownloadResponse;
import com.securefile.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    ApiResponse<FileUploadResponse> storeFile(MultipartFile file) throws IOException;

    ApiResponse<FileDownloadResponse> validatorOtp(Integer otp);

    File getFileForDownload(String token) throws FileNotFoundException;
}
