package com.securefile.service.impl;

import com.securefile.dto.response.ApiResponse;
import com.securefile.dto.response.FileDownloadResponse;
import com.securefile.dto.response.FileUploadResponse;
import com.securefile.entity.FileRecord;
import com.securefile.exception.FileExpiredException;
import com.securefile.repository.FileRecordRepository;
import com.securefile.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final FileRecordRepository fileRecordRepository;
    private final Path uploadDir;

    public FileServiceImpl(FileRecordRepository fileRecordRepository, @Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.fileRecordRepository = fileRecordRepository;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }


    @Override
    public ApiResponse<FileUploadResponse> storeFile(MultipartFile file) throws IOException {
        if(file.getSize() > 50 * 1024 * 1024){
            throw new IllegalArgumentException("File larger tha 50 MB");
        }

        String filename = System.currentTimeMillis() + "_"+file.getOriginalFilename();
        Path targetPath = this.uploadDir.resolve(filename);
        Files.copy(file.getInputStream(),targetPath, StandardCopyOption.REPLACE_EXISTING);

        int opt = new Random().nextInt(900000) + 100000;

        FileRecord fileRecord = FileRecord.builder()
                .filename(filename)
                .filePath(targetPath.toString())
                .otp(opt)
                .otpUsed(false)
                .uploadTime(LocalDateTime.now())
                .expiaryTime(LocalDateTime.now().plusMinutes(10))
                .downloadToken(UUID.randomUUID().toString())
                .downloadTokenUsed(false)
                .build();
        fileRecordRepository.save(fileRecord);

        FileUploadResponse response = new FileUploadResponse(fileRecord.getOtp(),fileRecord.getExpiaryTime());
        ApiResponse<FileUploadResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "File uploaded successfully.",
                false,
                response
        );
        return apiResponse;
    }

    @Override
    public ApiResponse<FileDownloadResponse> validatorOtp(Integer otp) {
        Optional<FileRecord> checkOtp = fileRecordRepository.findByOtp(otp);
        if(checkOtp.isEmpty()){
            throw new EntityNotFoundException("Otp does not fouond.");
        }

        FileRecord fileRecord = checkOtp.get();

        if(fileRecord.getOtpUsed() || fileRecord.getExpiaryTime().isBefore(LocalDateTime.now())){
            throw new FileExpiredException("File already expired.");
        }

        fileRecord.setOtpUsed(true);
        fileRecordRepository.save(fileRecord);
        FileDownloadResponse response = new FileDownloadResponse(fileRecord.getDownloadToken(),fileRecord.getExpiaryTime());
        ApiResponse<FileDownloadResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.toString(),
                200,
                "Download created successfully.",
                false,
                response
        );
        return apiResponse;
    }

    @Override
    public File getFileForDownload(String token) throws FileNotFoundException {
        FileRecord record = fileRecordRepository.findByDownloadToken(token)
                .orElseThrow(() -> new FileNotFoundException("Invalid download token"));

        if(record.getDownloadTokenUsed()){
            throw new FileExpiredException("This download token already used.");
        }
        if (record.getExpiaryTime().isBefore(LocalDateTime.now())) {
            throw new FileExpiredException("File download time expired");
        }
        record.setDownloadTokenUsed(true);
        fileRecordRepository.save(record);

        File file = new File(record.getFilePath());
        if (!file.exists()) {
            throw new FileNotFoundException("File not found on disk");
        }

        return file;
    }


    public void cleanupExpiredFiles() {
        fileRecordRepository.findAll().forEach(record -> {
            if (record.getExpiaryTime().isBefore(LocalDateTime.now())) {
                try { Files.deleteIfExists(Paths.get(record.getFilePath())); }
                catch (IOException e) { e.printStackTrace(); }
                fileRecordRepository.delete(record);
            }
        });
    }
}
