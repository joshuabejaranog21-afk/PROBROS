package com.probros.admin.service;

import com.probros.admin.model.ImageSlotEntity;
import com.probros.admin.repository.ImageSlotRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageSlotService {

    @Value("${site.path}")
    private String sitePath;

    private final ImageSlotRepository repo;

    public ImageSlotService(ImageSlotRepository repo) {
        this.repo = repo;
    }

    public Map<String, List<ImageSlotEntity>> getSlotsByPage() {
        return repo.findAllByOrderByPageAscIdAsc().stream()
                .collect(Collectors.groupingBy(ImageSlotEntity::getPage,
                        LinkedHashMap::new, Collectors.toList()));
    }

    public void uploadImage(String slotId, MultipartFile file) throws IOException {
        ImageSlotEntity slot = repo.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot no encontrado: " + slotId));

        String ext = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        File imagesDir = new File(sitePath, "assets/images");
        imagesDir.mkdirs();

        // Elimina archivo anterior si existe
        if (slot.getImageFilename() != null) {
            new File(imagesDir, slot.getImageFilename()).delete();
        }

        String filename = slotId + "." + ext;
        file.transferTo(new File(imagesDir, filename));

        updateHtml(slot, "assets/images/" + filename);

        slot.markUploaded(filename);
        repo.save(slot);
    }

    public void removeImage(String slotId) throws IOException {
        ImageSlotEntity slot = repo.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot no encontrado: " + slotId));

        if (slot.getImageFilename() != null) {
            new File(sitePath + "/assets/images/" + slot.getImageFilename()).delete();
        }

        revertHtml(slot);

        slot.markRemoved();
        repo.save(slot);
    }

    private void updateHtml(ImageSlotEntity slot, String relativeImagePath) throws IOException {
        File htmlFile = new File(sitePath, slot.getPageFile());
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        doc.outputSettings().prettyPrint(false).charset("UTF-8");

        Element ph = findPhElement(doc, slot);
        if (ph == null) return;

        // Marca el .ph y coloca la imagen encima. La clase "filled" oculta el
        // rótulo ::after (definido en styles.css). Conservamos data-label para
        // poder revertir el placeholder tal cual estaba.
        ph.attr("data-slot", slot.getId());
        ph.addClass("filled");
        ph.select("img").remove();
        ph.prepend("<img src=\"" + relativeImagePath + "\" alt=\"" + slot.getLabel()
                + "\" style=\"position:absolute;inset:0;width:100%;height:100%;object-fit:cover;z-index:1;\">");

        Files.writeString(Path.of(htmlFile.toURI()), doc.outerHtml());
    }

    private void revertHtml(ImageSlotEntity slot) throws IOException {
        File htmlFile = new File(sitePath, slot.getPageFile());
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        doc.outputSettings().prettyPrint(false).charset("UTF-8");

        Element ph = doc.selectFirst("[data-slot=" + slot.getId() + "]");
        if (ph == null) return;

        ph.select("img").remove();
        ph.removeClass("filled");
        ph.removeAttr("data-slot");
        // data-label sigue presente, así que el rótulo ::after vuelve a mostrarse.

        Files.writeString(Path.of(htmlFile.toURI()), doc.outerHtml());
    }

    private Element findPhElement(Document doc, ImageSlotEntity slot) {
        // Primero intenta por data-slot (si ya fue subida antes)
        Element byAttr = doc.selectFirst("[data-slot=" + slot.getId() + "]");
        if (byAttr != null) return byAttr;

        // Si no, busca por el data-label del placeholder
        Elements phs = doc.select(".ph");
        for (Element ph : phs) {
            String dataLabel = ph.attr("data-label");
            if (!dataLabel.isEmpty() && dataLabel.trim().equalsIgnoreCase(slot.getPhLabelText().trim())) {
                return ph;
            }
        }
        return null;
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1).toLowerCase() : "jpg";
    }
}
