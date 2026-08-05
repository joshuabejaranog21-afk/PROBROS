package com.probros.admin.config;

import com.probros.admin.model.ImageSlotEntity;
import com.probros.admin.repository.ImageSlotRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ImageSlotRepository repo;

    public DataInitializer(ImageSlotRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Slots for the single-page "Pro Bros Restoration" design (index.html).
        // The last argument is the exact data-label of the .ph placeholder in the HTML.
        List<ImageSlotEntity> seeds = List.of(
            slot("hero-crew",        "Inicio", "index.html", "Hero — cuadrilla con equipo de secado",   "PHOTO — crew with drying equipment on-site"),
            slot("why-team-truck",   "Inicio", "index.html", "Por qué — equipo frente a la camioneta",  "PHOTO — uniformed team in front of company truck"),
            slot("ba-before-room",   "Inicio", "index.html", "Antes — habitación con daño por agua",    "BEFORE — water-damaged room"),
            slot("ba-after-room",    "Inicio", "index.html", "Después — habitación restaurada",          "AFTER — fully restored room"),
            slot("insurance-review", "Inicio", "index.html", "Seguro — técnico revisando con cliente",  "PHOTO — technician reviewing claim with homeowner"),
            slot("area-map",         "Inicio", "index.html", "Área — mapa de cobertura DFW",            "MAP — Dallas–Fort Worth coverage area")
        );

        for (ImageSlotEntity s : seeds) {
            if (!repo.existsById(s.getId())) {
                repo.save(s);
            }
        }
    }

    private ImageSlotEntity slot(String id, String page, String file, String label, String phLabel) {
        return new ImageSlotEntity(id, page, file, label, phLabel);
    }
}
