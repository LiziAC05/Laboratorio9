package com.example.Lab9.controller;

import com.example.Lab9.entity.Area;
import com.example.Lab9.entity.Proyecto;
import com.example.Lab9.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

@RestController
@CrossOrigin
public class ProyectoWS {
  @Autowired
  ProyectoRepository proyectoRepository;
  @GetMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity listarProyectos(){
    return new ResponseEntity(proyectoRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping(value = "/proyecto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity obtenerProyecto(@PathVariable("id") String idProyecto){
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idProyecto);
      Optional<Proyecto> optionalProyecto = proyectoRepository.findById(id);
      if (optionalProyecto.isPresent()) {
        responseMap.put("estado", "ok");
        responseMap.put("proyecto", optionalProyecto.get());
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontró el proyecto con id: " + id);
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    }catch(NumberFormatException e) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "El ID debe ser un número");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity guardarProyecto(@RequestBody Proyecto proyecto, @RequestParam(value = "fetchId", required = false) boolean fetchId){
    HashMap<String, Object> responseMap = new HashMap<>();
    proyectoRepository.save(proyecto);
    if(fetchId){
      responseMap.put("id", proyecto.getIdproyecto());
    }
    responseMap.put("estado", "creado");
    return new ResponseEntity(responseMap, HttpStatus.CREATED);
  }

  @PutMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity actualizarProyecto(@RequestBody Proyecto proyecto) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if (proyecto.getIdproyecto() > 0) {
      Optional<Proyecto> optionalProyecto = proyectoRepository.findById(proyecto.getIdproyecto());
      if (optionalProyecto.isPresent()) {
        proyectoRepository.save(proyecto);
        responseMap.put("estado", "actualizado");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "El id del proyecto a actualizar no existe");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      }
    } else {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un ID");
      return new ResponseEntity(responseMap, HttpStatus.OK);
    }
  }

  @DeleteMapping(value = "/proyecto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity borrarProyecto(@PathVariable("id") String idproyecto) {
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idproyecto);
      if (proyectoRepository.existsById(id)) {
        proyectoRepository.deleteById(id);
        responseMap.put("estado", "borrado exitoso");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontro el proyecto con id" + id);
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    } catch (NumberFormatException e) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "El ID debe ser un número");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity gestionExcepcion(HttpServletRequest request) {

    HashMap<String, Object> responseMap = new HashMap<>();
    if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un proyecto");
    }
    return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
  }

}
