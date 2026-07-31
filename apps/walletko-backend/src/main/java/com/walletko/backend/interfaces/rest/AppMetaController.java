package com.walletko.backend.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/meta")
public class AppMetaController {

    @Value("${APP_VERSION:dev}")
    private String version;

    @Value("${APP_RELEASE_DATE:}")
    private String releaseDate;

    @GetMapping
    public ResponseEntity<Map<String, String>> getMeta() {
        return ResponseEntity.ok(Map.of(
            "version", version,
            "releaseDate", releaseDate != null && !releaseDate.isBlank() ? releaseDate : null
        ));
    }
}
