package com.prueba.tutorial.model.mapper;

import com.prueba.tutorial.model.dto.TutorialDto;
import com.prueba.tutorial.model.entity.Tutorial;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TutorialMapper {

    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "title",target = "title"),
            @Mapping(source = "description",target = "description"),
            @Mapping(source = "published",target = "published")
    })
    TutorialDto toModel(Tutorial tutorial);

    @InheritInverseConfiguration
    Tutorial toEntity(TutorialDto tutorialDto);

}
