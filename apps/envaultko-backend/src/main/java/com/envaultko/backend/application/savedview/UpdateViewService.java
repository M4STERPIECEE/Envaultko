package com.envaultko.backend.application.savedview;

import com.envaultko.backend.domain.savedview.*;
import com.envaultko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UpdateViewService {
    private final SavedViewRepository viewRepo;

    public UpdateViewService(SavedViewRepository viewRepo) {
        this.viewRepo = viewRepo;
    }

    @Transactional
    public void execute(Id viewId, Name name, String description,
                         String nameFilter, List<Id> tagIds, Id userId) {
        var view = viewRepo.findById(viewId, userId)
            .orElseThrow(() -> new IllegalArgumentException("View not found"));
        if (!view.name().equals(name) && viewRepo.existsByName(name, userId)) {
            throw new SavedViewNameConflictError(name.value());
        }
        view.update(name, description, nameFilter, tagIds);
        viewRepo.save(view);
    }
}
