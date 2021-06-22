package com.example.Lab9.controller;

import com.example.Lab9.entity.Actividad;
import com.example.Lab9.entity.Area;
import com.example.Lab9.repository.ActividadRepository;
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
public class ActividadWS {
  @Autowired
  ActividadRepository actividadRepository;
  @GetMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity listarActividades() {
    return new ResponseEntity(actividadRepository.findAll(), HttpStatus.OK);
  }

  @PostMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity guardarActividades(@RequestBody Actividad actividad, @RequestParam(value = "fetchId", required = false) boolean fetchId) {
    HashMap<String, Object> responseMap = new HashMap<>();
    actividadRepository.save(actividad);
    if (fetchId) {
      responseMap.put("id", actividad.getIdactividad());
    }
    responseMap.put("estado", "creado");
    return new ResponseEntity(responseMap, HttpStatus.CREATED);
  }

  @PutMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity actualizarActividades(@RequestBody Actividad actividad) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if (actividad.getIdactividad() > 0) {
      Optional<Actividad> optionalActividad = actividadRepository.findById(actividad.getIdactividad());
      if (optionalActividad.isPresent()) {
        actividadRepository.save(actividad);
        responseMap.put("estado", "actualizado");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "El id de la actividad a actualizar no existe");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      }
    } else {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un ID");
      return new ResponseEntity(responseMap, HttpStatus.OK);
    }
  }

  @DeleteMapping(value = "/actividad/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity borrarActividades(@PathVariable("id") String idact) {
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idact);
      if (actividadRepository.existsById(id)) {
        actividadRepository.deleteById(id);
        responseMap.put("estado", "borrado exitoso");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontro la actividad con id" + id);
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
      responseMap.put("msg", "Debe enviar una actividad");
    }
    return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
  }

}
