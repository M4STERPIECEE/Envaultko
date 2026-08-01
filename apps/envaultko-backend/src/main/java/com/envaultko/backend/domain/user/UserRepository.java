package com.envaultko.backend.domain.user;

import com.envaultko.backend.domain.shared.vo.*;

public interface UserRepository {
    void updateName(Id userId, Name name);
}
