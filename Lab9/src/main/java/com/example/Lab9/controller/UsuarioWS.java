package com.example.Lab9.controller;

import com.example.Lab9.entity.Area;
import com.example.Lab9.entity.Usuario;
import com.example.Lab9.repository.AreaRepository;
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
public class UsuarioWS {
  @Autowired
  UsuarioRepository usuarioRepository;
  @Autowired
  AreaRepository areaRepository;
  @GetMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity listarUsuarios(){
    return new ResponseEntity(usuarioRepository.findAll(), HttpStatus.OK);
  }

  @PostMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity guardarUsuario(@RequestBody Usuario usuario, @RequestParam(value = "fetchCorreo", required = false) boolean fetchCorreo){
    HashMap<String, Object> responseMap = new HashMap<>();
    if (usuario.getCorreo()!=null) {
      if(areaRepository.existsById(usuario.getIdarea())) {
        usuarioRepository.save(usuario);
        if (fetchCorreo) {
          responseMap.put("correo", usuario.getCorreo());
        }
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);
      }
      else{
        responseMap.put("estado", "error");
        responseMap.put("msg", "No existe area con ese id");
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    }
    else{
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un Usuario ");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity actualizarUsuario(@RequestBody Usuario usuario) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if (usuario.getCorreo() != null) {
      Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getCorreo());
      if (optionalUsuario.isPresent()) {
        if(areaRepository.existsById(usuario.getIdarea())) {
          optionalUsuario.get().setIdarea(usuario.getIdarea());
          if(usuario.getNombres()!=null){
            optionalUsuario.get().setNombres(usuario.getNombres());
          }
          if(usuario.getApellidos()!=null){
            optionalUsuario.get().setApellidos(usuario.getApellidos());
          }
          usuarioRepository.save(optionalUsuario.get());
          responseMap.put("estado", "actualizado");
          return new ResponseEntity(responseMap, HttpStatus.OK);
        }
        else{
          responseMap.put("estado", "error");
          responseMap.put("msg", "Debe enviar un id area válido");
          return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
      } else {
        responseMap.put("estado", "error");
        responseMap.put("msg", "El correo del usuario a actualizar no existe");
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
      }
    } else {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un correo");
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(value = "/usuario/{correo}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity borrarUsuario(@PathVariable("correo") String correo) {
    HashMap<String, Object> responseMap = new HashMap<>();
    if (usuarioRepository.existsById(correo)) {
      usuarioRepository.deleteById(correo);
      responseMap.put("estado", "borrado exitoso");
      return new ResponseEntity(responseMap, HttpStatus.OK);
    } else {
      responseMap.put("estado", "error");
      responseMap.put("msg", "no se encontro el usuario con correo" + correo);
      return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity gestionExcepcion(HttpServletRequest request) {

    HashMap<String, Object> responseMap = new HashMap<>();
    if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
      responseMap.put("estado", "error");
      responseMap.put("msg", "Debe enviar un usuario");
    }
    return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
  }

}
