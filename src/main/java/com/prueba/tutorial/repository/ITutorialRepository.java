package com.prueba.tutorial.repository;

import com.prueba.tutorial.model.dto.TutorialDto;
import com.prueba.tutorial.model.entity.Tutorial;

import java.util.List;

public interface ITutorialRepository {
    int save(Tutorial tutorial);
    TutorialDto update(Tutorial tutorial);
    TutorialDto findById(Long id);
    void deleteById(Long id);
    List<TutorialDto> findAll();
    List<TutorialDto> findByIdPublished(boolean published);
    List<TutorialDto> findByTitleContaining(String title);
    int ultimoId();
    void deleteAll();
}
