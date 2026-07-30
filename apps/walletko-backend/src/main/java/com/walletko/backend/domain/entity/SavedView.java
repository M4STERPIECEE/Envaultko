package com.walletko.backend.domain.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "saved_views")
public class SavedView {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "name_filter")
    private String nameFilter;

    @ElementCollection
    @CollectionTable(name = "saved_view_tags", joinColumns = @JoinColumn(name = "saved_view_id"))
    @Column(name = "tag_id")
    private List<String> tagIds = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public SavedView() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNameFilter() { return nameFilter; }
    public void setNameFilter(String nameFilter) { this.nameFilter = nameFilter; }

    public List<String> getTagIds() { return tagIds; }
    public void setTagIds(List<String> tagIds) { this.tagIds = tagIds; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
