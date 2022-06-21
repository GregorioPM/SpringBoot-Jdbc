package com.prueba.tutorial.controller;

import com.prueba.tutorial.model.dto.TutorialDto;
import com.prueba.tutorial.model.entity.Tutorial;
import com.prueba.tutorial.model.mapper.TutorialMapper;
import com.prueba.tutorial.repository.ITutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    private ITutorialRepository tutorialRepository;

    @Autowired
    private TutorialMapper tutorialMapper;

    @GetMapping("/tutorials")
    public ResponseEntity<?> getAll(@RequestParam(required = false) String title){
        HashMap<String, String> mapResponse = new HashMap<>();
        try {
            List<TutorialDto> tutorialDtos = new ArrayList<TutorialDto>();
            if(title == null){
                tutorialRepository.findAll().forEach(tutorialDtos::add);
            }else{
                tutorialRepository.findByTitleContaining(title).forEach(tutorialDtos::add);
            }
            if(tutorialDtos.isEmpty()){
                mapResponse.put("message", title +" No coincide con ningun titulo");
                mapResponse.put("status_code", "204");
                return new ResponseEntity<>(mapResponse,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorialDtos,HttpStatus.OK);
        }catch (Exception e){
            mapResponse.put("message","Ha ocurrido un error interno");
            mapResponse.put("status_code", "500");
            return new ResponseEntity<>(mapResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<?> save(@RequestBody Tutorial tutorial){
        try{
            int a = tutorialRepository.save(new Tutorial(tutorial.getTitle(),tutorial.getDescription(),false));
            Long f = (long) tutorialRepository.ultimoId();
            TutorialDto tutorialDto = tutorialRepository.findById(f);
            return new ResponseEntity<>(tutorialDto, HttpStatus.CREATED);
        }catch (Exception e){
            HashMap<String, String> mapResponse = new HashMap<>();
            mapResponse.put("message","Se a generado un error interno al guardar un tutorial");
            mapResponse.put("status_code", "500");
            mapResponse.put("error", String.valueOf(e));
            return new ResponseEntity<>(mapResponse,HttpStatus.INTERNAL_SERVER_ERROR);
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
            HashMap<String, String> mapResponse = new HashMap<>();
            mapResponse.put("message","No se encontro el tutorial");
            mapResponse.put("status_code", "404");
            return  new ResponseEntity<>(mapResponse,HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") long id){
        TutorialDto tutorialDto = tutorialRepository.findById(id);
        if(tutorialDto!=null){
            return  new ResponseEntity<>(tutorialDto,HttpStatus.OK);
        }else {
            HashMap<String, String> mapResponse = new HashMap<>();
            mapResponse.put("message","No se encontro el tutorial");
            mapResponse.put("status_code", "404");
            return new ResponseEntity<>(mapResponse,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        HashMap<String, String> mapResponse = new HashMap<>();
        TutorialDto tutorialDto = tutorialRepository.findById(id);
        if(tutorialDto!=null){
            tutorialRepository.deleteById(id);
            mapResponse.put("message","Tutorial "+ tutorialDto.getTitle() +" ha sido borrado correctamete");
            mapResponse.put("status_code", "200");
            return new ResponseEntity<>(mapResponse , HttpStatus.OK);
        }else {
            mapResponse.put("message","Tutorial no se encuentra");
            mapResponse.put("status_code", "404");
            return new ResponseEntity<>(mapResponse,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<?> deleteAll(){
        HashMap<String, String> mapResponse = new HashMap<>();
        try {
            int rows = tutorialRepository.deleteAll();
            mapResponse.put("message","Se eliminaron "+ rows+" Tutoriales correctamente");
            mapResponse.put("status_code", "200");
            return new ResponseEntity<>(mapResponse,HttpStatus.OK);
        }catch (Exception e){
            mapResponse.put("message","No se logro eliminar los tutoriales por un error interno");
            mapResponse.put("status_code", "500");
            return new ResponseEntity<>(mapResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<?> getAllTutorialsPublished(){
        HashMap<String, String> mapResponse = new HashMap<>();
        try {
            List<TutorialDto> tutorialDtos = tutorialRepository.findByIdPublished(true);
            if(!tutorialDtos.isEmpty()){
                return new ResponseEntity<>(tutorialDtos,HttpStatus.OK);
            }else {
                mapResponse.put("message","No se encontraron tutoriales publicados");
                mapResponse.put("status_code", "204");
                return new ResponseEntity<>(mapResponse,HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            mapResponse.put("message", "Se genero un error interno");
            mapResponse.put("status_code", "500");
            return new ResponseEntity<>(mapResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
