package com.walletko.backend.interfaces.rest;

import com.walletko.backend.interfaces.dto.AppMetaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meta")
public class AppMetaController {

    @Value("${APP_VERSION:dev}")
    private String version;

    @Value("${APP_RELEASE_DATE:}")
    private String releaseDate;

    @GetMapping
    public ResponseEntity<AppMetaDTO> getMeta() {
        String resolvedReleaseDate = releaseDate != null && !releaseDate.isBlank() ? releaseDate : null;
        return ResponseEntity.ok(new AppMetaDTO(version, resolvedReleaseDate));
    }
}
