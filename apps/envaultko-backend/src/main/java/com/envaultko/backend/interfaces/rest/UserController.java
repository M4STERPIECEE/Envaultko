package com.envaultko.backend.interfaces.rest;

import com.envaultko.backend.application.user.UpdateUserNameService;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.interfaces.dto.request.UpdateNameRequest;
import jakarta.validation.Valid;
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
}
