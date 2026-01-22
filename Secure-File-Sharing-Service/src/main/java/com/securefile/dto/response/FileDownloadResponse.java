package com.securefile.dto.response;

import java.time.LocalDateTime;

public record FileDownloadResponse(
        String downloadLink,
        LocalDateTime expiaryTime
) {
}
