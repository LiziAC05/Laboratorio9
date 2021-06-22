package com.example.Lab9.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
  @Id
  private String correo;

  @Column(nullable = false)
  private int idarea;
  private String nombres;
  private String apellidos;

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public int getIdarea() {
    return idarea;
  }

  public void setIdarea(int idarea) {
    this.idarea = idarea;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }
}
