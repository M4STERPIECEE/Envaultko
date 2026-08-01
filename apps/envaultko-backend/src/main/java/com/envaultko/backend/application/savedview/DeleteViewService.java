package com.envaultko.backend.application.savedview;

import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.domain.savedview.SavedViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteViewService {
    private final SavedViewRepository viewRepo;

    public DeleteViewService(SavedViewRepository viewRepo) {
        this.viewRepo = viewRepo;
    }

    @Transactional
    public void execute(Id viewId, Id userId) {
        viewRepo.remove(viewId, userId);
    }
}
