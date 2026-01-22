package com.securefile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_record")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String filePath;

    @Column(unique = true)
    private Integer otp;

    private Boolean otpUsed = false;

    private LocalDateTime uploadTime;
    private LocalDateTime expiaryTime;

    @Column(unique = true)
    private String downloadToken;

    private Boolean downloadTokenUsed = false;

}
