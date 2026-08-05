package com.probros.admin.model;

public class ImageSlot {
    private String id;
    private String page;
    private String pageFile;
    private String label;
    private String phLabelText;
    private boolean filled;
    private String imageFilename;

    public ImageSlot(String id, String page, String pageFile, String label, String phLabelText) {
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
    public void setFilled(boolean filled) { this.filled = filled; }
    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }
}
