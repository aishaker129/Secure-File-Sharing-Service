package com.securefile.repository;

import com.securefile.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord,Long> {
    Optional<FileRecord> findByOtp(Integer otp);

    Optional<FileRecord> findByDownloadToken(String token);
}
