package com.securefile.dto.response;

import java.time.LocalDateTime;

public record FileUploadResponse(
        Integer otp,
        LocalDateTime expiaryTime
) {
}
