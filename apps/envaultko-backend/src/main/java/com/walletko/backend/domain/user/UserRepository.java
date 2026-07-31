package com.walletko.backend.domain.user;

import com.walletko.backend.domain.shared.vo.*;

public interface UserRepository {
    void updateName(Id userId, Name name);
}
