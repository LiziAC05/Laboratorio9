package com.example.Lab9.controller;

import com.example.Lab9.entity.Actividad;
import com.example.Lab9.entity.Area;
import com.example.Lab9.repository.ActividadRepository;
import com.example.Lab9.repository.ProyectoRepository;
import com.example.Lab9.repository.UsuarioRepository;
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
  @Autowired
  ProyectoRepository proyectoRepository;
  @Autowired
  UsuarioRepository usuarioRepository;
  @GetMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity listarActividades() {
    return new ResponseEntity(actividadRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping(value = "/actividad/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity obtenerArea(@PathVariable("id") String idActividad) {
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idActividad);
      Optional<Actividad> optionalActividad = actividadRepository.findById(id);
      if (optionalActividad.isPresent()) {
        responseMap.put("estado", "ok");
        responseMap.put("actividad", optionalActividad.get());
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontró la actividad con id: " + id);
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    } catch (NumberFormatException e) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "El ID debe ser un número");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity guardarActividades(@RequestBody Actividad actividad, @RequestParam(value = "fetchId", required = false) boolean fetchId) {
    HashMap<String, Object> responseMap = new HashMap<>();

    if(proyectoRepository.existsById(actividad.getIdproyecto())) {
      if(usuarioRepository.existsById(actividad.getUsuario_owner())){
        actividadRepository.save(actividad);
        if (fetchId) {
          responseMap.put("id", actividad.getIdactividad());
        }
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);
      }else{
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontró el usuario asociado: " + actividad.getUsuario_owner());
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    }else{
      responseMap.put("estado", "error");
      responseMap.put("msg", "no se encontró el proyecto con id: " + actividad.getIdproyecto());
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
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
