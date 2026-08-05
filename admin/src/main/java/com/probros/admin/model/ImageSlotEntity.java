package com.probros.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "image_slots")
public class ImageSlotEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String page;

    @Column(nullable = false)
    private String pageFile;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false, length = 512)
    private String phLabelText;

    private boolean filled = false;

    private String imageFilename;

    private LocalDateTime uploadedAt;

    protected ImageSlotEntity() {}

    public ImageSlotEntity(String id, String page, String pageFile, String label, String phLabelText) {
        this.id = id;
        this.page = page;
        this.pageFile = pageFile;
        this.label = label;
        this.phLabelText = phLabelText;
    }

    public String getId() { return id; }
    public String getPage() { return page; }
    public String getPageFile() { return pageFile; }
    public String getLabel() { return label; }
    public String getPhLabelText() { return phLabelText; }
    public boolean isFilled() { return filled; }
    public String getImageFilename() { return imageFilename; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void markUploaded(String filename) {
        this.imageFilename = filename;
        this.filled = true;
        this.uploadedAt = LocalDateTime.now();
    }

    public void markRemoved() {
        this.imageFilename = null;
        this.filled = false;
        this.uploadedAt = null;
    }
}
