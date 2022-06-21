package com.prueba.tutorial.controller;

import com.prueba.tutorial.model.dto.TutorialDto;
import com.prueba.tutorial.model.entity.Tutorial;
import com.prueba.tutorial.model.mapper.TutorialMapper;
import com.prueba.tutorial.repository.ITutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    private ITutorialRepository tutorialRepository;

    @Autowired
    private TutorialMapper tutorialMapper;

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

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Tutorial tutorial){
        Tutorial tutorialFind = tutorialMapper.toEntity(tutorialRepository.findById(id));
        if(tutorialFind!=null){
            tutorialFind.setId(id);
            tutorialFind.setTitle(tutorial.getTitle());
            tutorialFind.setDescription(tutorial.getDescription());
            tutorialFind.setPublished(tutorial.isPublished());
            return  new ResponseEntity<>(tutorialRepository.update(tutorialFind),HttpStatus.OK);
        }else{
            return  new ResponseEntity<>("No se encontro el tutorial",HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") long id){
        TutorialDto tutorialDto = tutorialRepository.findById(id);
        if(tutorialDto!=null){
            return  new ResponseEntity<>(tutorialDto,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("No se encontro el tutorial",HttpStatus.NOT_FOUND);
        }
    }
}
