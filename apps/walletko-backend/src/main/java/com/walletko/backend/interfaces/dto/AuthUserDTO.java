package com.walletko.backend.interfaces.dto;

public record AuthUserDTO(String id, String name, String email, boolean emailVerified) {}
