package com.walletko.backend.shared;

import com.walletko.backend.application.auth.InvalidOtpError;
import com.walletko.backend.domain.income.CancelIncomeBlockedError;
import com.walletko.backend.domain.savedview.SavedViewNameConflictError;
import com.walletko.backend.domain.tag.TagNameConflictError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOtpError.class)
    public ResponseEntity<Map<String, String>> handleInvalidOtp(InvalidOtpError e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleConflict(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(TagNameConflictError.class)
    public ResponseEntity<Map<String, String>> handleTagNameConflict(TagNameConflictError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "name_conflict"));
    }

    @ExceptionHandler(SavedViewNameConflictError.class)
    public ResponseEntity<Map<String, String>> handleViewNameConflict(SavedViewNameConflictError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "name_conflict"));
    }

    @ExceptionHandler(CancelIncomeBlockedError.class)
    public ResponseEntity<Map<String, Object>> handleCancelBlocked(CancelIncomeBlockedError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "blocked", true, "code", "WOULD_CAUSE_NEGATIVE_BALANCE",
            "pots", e.pots().stream()
                .map(p -> Map.of("name", p.name(), "shortfall", p.shortfall()))
                .toList()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Internal server error"));
    }
}
