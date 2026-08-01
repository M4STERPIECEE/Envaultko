package com.envaultko.backend.application.user;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateUserNameService {
    private final UserRepository userRepo;

    public UpdateUserNameService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public void execute(Id userId, Name name) {
        userRepo.updateName(userId, name);
    }
}
