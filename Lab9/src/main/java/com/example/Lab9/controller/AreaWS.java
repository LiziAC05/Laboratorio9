package com.example.Lab9.controller;

import com.example.Lab9.dto.AreaDto;
import com.example.Lab9.entity.Area;
import com.example.Lab9.repository.AreaRepository;
import com.example.Lab9.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

@RestController
@CrossOrigin
public class AreaWS {
  @Autowired
  AreaRepository areaRepository;
  @Autowired
  UsuarioRepository usuarioRepository;

  @GetMapping(value = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity listarAreas() {
    return new ResponseEntity(areaRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping(value = "/area/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity obtenerArea(@PathVariable("id") String idArea) {
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idArea);
      Optional<Area> optionalArea = areaRepository.findById(id);
      if (optionalArea.isPresent()) {
        responseMap.put("estado", "ok");
        responseMap.put("area", optionalArea.get());
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontró el area con id: " + id);
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    } catch (NumberFormatException e) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "El ID debe ser un número");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/areaConUsuarios/{id}")
  public ResponseEntity areaConUsuarios(@PathVariable("id") String idArea){
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idArea);
      Optional<Area> optionalArea = areaRepository.findById(id);
      if (optionalArea.isPresent()) {
        AreaDto areaUsuarios = new AreaDto();
        areaUsuarios.setIdarea(optionalArea.get().getIdarea());
        areaUsuarios.setNombrearea(optionalArea.get().getNombrearea());
        areaUsuarios.setListaUsuarios(usuarioRepository.usuariosArea(id));
        responseMap.put("estado", "ok");
        responseMap.put("area", areaUsuarios);
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "no se encontró el area con id: " + id);
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    } catch (NumberFormatException e) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "El ID debe ser un número");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity guardarArea(@RequestBody Area area, @RequestParam(value = "fetchId", required = false) boolean fetchId) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if(area.getNombrearea()!=null) {
      areaRepository.save(area);
      if (fetchId) {
        responseMap.put("id", area.getIdarea());
      }
      responseMap.put("estado", "creado");
      return new ResponseEntity(responseMap, HttpStatus.CREATED);
    }
    else{
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un area");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity actualizarArea(@RequestBody Area area) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if (area.getIdarea() > 0) {
      Optional<Area> optionalArea = areaRepository.findById(area.getIdarea());
      if (optionalArea.isPresent()) {
        areaRepository.save(area);
        responseMap.put("estado", "actualizado");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "El área a actualizar no existe");
        return new ResponseEntity(responseMap, HttpStatus.OK);
      }
    } else {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un ID");
      return new ResponseEntity(responseMap, HttpStatus.OK);
    }
  }

  @DeleteMapping(value = "/area/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity borrarArea(@PathVariable("id") String idarea) {
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
        int id = Integer.parseInt(idarea);
        if (areaRepository.existsById(id)) {
          areaRepository.deleteById(id);
          responseMap.put("estado", "borrado exitoso");
          return new ResponseEntity(responseMap, HttpStatus.OK);
        } else {
          responseMap.put("estado", "error");
          responseMap.put("msg", "no se encontro el area con id " + id);
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
      responseMap.put("msg", "Debe enviar un area");
    }
    return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
  }

}


