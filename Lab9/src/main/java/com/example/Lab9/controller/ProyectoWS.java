package com.example.Lab9.controller;

import com.example.Lab9.dto.ActividadesDto;
import com.example.Lab9.dto.AreaDto;
import com.example.Lab9.entity.Area;
import com.example.Lab9.entity.Proyecto;
import com.example.Lab9.repository.ActividadRepository;
import com.example.Lab9.repository.AreaRepository;
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
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ProyectoWS {
  @Autowired
  ProyectoRepository proyectoRepository;
  @Autowired
  ActividadRepository actividadRepository;
  @Autowired
  UsuarioRepository usuarioRepository;
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
    if(proyecto.getUsuario_owner()!=null) {
      proyectoRepository.save(proyecto);
      if (fetchId) {
        responseMap.put("id", proyecto.getIdproyecto());
      }
      responseMap.put("estado", "creado");
      return new ResponseEntity(responseMap, HttpStatus.CREATED);
    }
    else{
      responseMap.put("estado", "error");
      responseMap.put("msg", "El proyecto debe tener un usuario a cargo");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/proyectoConActividades/{id}")
  public ResponseEntity proyectoConActividades(@PathVariable("id") String idProyecto){
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idProyecto);
      Optional<Proyecto> optionalProyecto = proyectoRepository.findById(id);
      if (optionalProyecto.isPresent()) {
        ActividadesDto actividadesProyecto = new ActividadesDto();
        actividadesProyecto.setIdproyecto(optionalProyecto.get().getIdproyecto());
        actividadesProyecto.setNombreproyecto(optionalProyecto.get().getNombreProyecto());
        actividadesProyecto.setUsuario_owner(optionalProyecto.get().getUsuario_owner());
        actividadesProyecto.setListaActividades(actividadRepository.actividadesProyecto(id));
        responseMap.put("estado", "ok");
        responseMap.put("proyecto", actividadesProyecto);
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

  @PutMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity actualizarProyecto(@RequestBody Proyecto proyecto) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if (proyecto.getIdproyecto() > 0) {
      Optional<Proyecto> optionalProyecto = proyectoRepository.findById(proyecto.getIdproyecto());
      if (optionalProyecto.isPresent()) {
        if (proyecto.getUsuario_owner() != null) {
          if (usuarioRepository.existsById(proyecto.getUsuario_owner())) {
            if (proyecto.getNombreProyecto() != null) {
              optionalProyecto.get().setNombreProyecto(proyecto.getNombreProyecto());
            }
            optionalProyecto.get().setUsuario_owner(proyecto.getUsuario_owner());
            proyectoRepository.save(optionalProyecto.get());
            responseMap.put("estado", "actualizado");
            return new ResponseEntity(responseMap, HttpStatus.OK);
          } else {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un usuario existente");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
          }
        }
        else{
          if (proyecto.getNombreProyecto() != null) {
            optionalProyecto.get().setNombreProyecto(proyecto.getNombreProyecto());
          }
          responseMap.put("estado", "No hay nada para actualizar");
          return new ResponseEntity(responseMap, HttpStatus.OK);
        }
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "El id del proyecto a actualizar no existe");
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    } else {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un ID");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(value = "/proyecto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity borrarProyecto(@PathVariable("id") String idproyecto) {
    HashMap<String, Object> responseMap = new HashMap<>();
    try {
      int id = Integer.parseInt(idproyecto);
      if (proyectoRepository.existsById(id)) {
        List<Integer> list = actividadRepository.idActividadProyecto(id);
        for(int i=0;i<list.size();i++){
          actividadRepository.deleteById(list.get(i));
        }
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
