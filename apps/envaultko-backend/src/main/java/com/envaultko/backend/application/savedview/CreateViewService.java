package com.envaultko.backend.application.savedview;

import com.envaultko.backend.domain.savedview.*;
import com.envaultko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CreateViewService {
    private final SavedViewRepository viewRepo;

    public CreateViewService(SavedViewRepository viewRepo) {
        this.viewRepo = viewRepo;
    }

    @Transactional
    public void execute(Name name, String description, String nameFilter,
                         List<Id> tagIds, Id userId) {
        if (viewRepo.existsByName(name, userId)) {
            throw new SavedViewNameConflictError(name.value());
        }
        var view = SavedView.create(name, userId, description, nameFilter, tagIds);
        viewRepo.save(view);
    }
}
