package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.user.UpdateUserNameService;
import com.walletko.backend.domain.shared.vo.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UpdateUserNameService updateUserNameService;

    public UserController(UpdateUserNameService updateUserNameService) {
        this.updateUserNameService = updateUserNameService;
    }

    @PutMapping("/name")
    public ResponseEntity<Void> updateUserName(Authentication auth,
                                                @Valid @RequestBody UpdateNameRequest req) {
        updateUserNameService.execute(new Id((String) auth.getPrincipal()), new Name(req.name()));
        return ResponseEntity.ok().build();
    }

    public record UpdateNameRequest(@NotBlank String name) {}
}
