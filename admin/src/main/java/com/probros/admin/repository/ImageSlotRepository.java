package com.probros.admin.repository;

import com.probros.admin.model.ImageSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageSlotRepository extends JpaRepository<ImageSlotEntity, String> {
    List<ImageSlotEntity> findAllByOrderByPageAscIdAsc();
}
