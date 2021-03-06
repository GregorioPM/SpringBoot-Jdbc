package com.prueba.tutorial.repository.impl;

import com.prueba.tutorial.model.dto.TutorialDto;
import com.prueba.tutorial.model.entity.Tutorial;
import com.prueba.tutorial.model.mapper.TutorialMapper;
import com.prueba.tutorial.repository.ITutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcTutorialRepository implements ITutorialRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TutorialMapper tutorialMapper;

    @Override
    public int save(Tutorial tutorial) {
        return jdbcTemplate.update("INSERT INTO tutorials (title, description, published) VALUES(?,?,?)",
                new Object[] { tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublished() });
    }

    @Override
    public TutorialDto update(Tutorial tutorial) {
        jdbcTemplate.update("UPDATE tutorials SET title=?, description=?, published=? WHERE id=?",
                new Object[]{tutorial.getTitle(),tutorial.getDescription(),tutorial.isPublished(),tutorial.getId()});

        return findById(tutorial.getId());
    }

    @Override
    public TutorialDto findById(Long id) {
        Tutorial tutorial = null;
        try {
            tutorial = jdbcTemplate.queryForObject("SELECT * FROM tutorials WHERE id=?", BeanPropertyRowMapper.newInstance(Tutorial.class), id);
        }catch (IncorrectResultSizeDataAccessException e){
            return null;
        }
        return tutorialMapper.toModel(tutorial);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM tutorials WHERE id=?",id);
    }

    @Override
    public List<TutorialDto> findAll() {
        List<Tutorial> tutoriales = jdbcTemplate.query("SELECT * FROM tutorials",BeanPropertyRowMapper.newInstance(Tutorial.class));

        return tutoriales.stream()
                .map(t-> tutorialMapper.toModel(t) )
                .collect(Collectors.toList());
    }

    @Override
    public List<TutorialDto> findByIdPublished(boolean published) {

        List<Tutorial> tutorials= jdbcTemplate.query("SELECT * FROM tutorials WHERE published=?",BeanPropertyRowMapper.newInstance(Tutorial.class),published);

        return tutorials.stream()
                .map(t->tutorialMapper.toModel(t))
                .collect(Collectors.toList());
    }

    @Override
    public List<TutorialDto> findByTitleContaining(String title) {

        List<Tutorial> tutorials= jdbcTemplate.query("SELECT * FROM tutorials WHERE title ILIKE '%" + title + "%'",BeanPropertyRowMapper.newInstance(Tutorial.class));
        String q = "SELECT * from tutorials WHERE title ILIKE '%" + title + "%'";
        //return jdbcTemplate.query(q, BeanPropertyRowMapper.newInstance(Tutorial.class));

        return tutorials.stream()
                .map(t-> tutorialMapper.toModel(t))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteAll() {
        return  jdbcTemplate.update("DELETE FROM tutorials");
    }

    //SELECT MAX(id_tabla) AS id FROM tabla

    @Override
    public int ultimoId(){
        return jdbcTemplate.queryForObject("SELECT MAX(id) AS id FROM tutorials",Integer.class);
    }
}
