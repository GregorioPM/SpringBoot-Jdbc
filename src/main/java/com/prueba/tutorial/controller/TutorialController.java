package com.prueba.tutorial.controller;

import com.prueba.tutorial.model.dto.TutorialDto;
import com.prueba.tutorial.model.entity.Tutorial;
import com.prueba.tutorial.repository.ITutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    private ITutorialRepository tutorialRepository;

    @PostMapping("/tutorials")
    public ResponseEntity<?> save(@RequestBody Tutorial tutorial){
        try{
            int a = tutorialRepository.save(new Tutorial(tutorial.getTitle(),tutorial.getDescription(),false));
            Long f = (long) tutorialRepository.ultimoId();
            TutorialDto tutorialDto = tutorialRepository.findById(f);
            return new ResponseEntity<>(tutorialDto, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
