package com.example.Lab9.entity;

import javax.persistence.*;

@Entity
@Table(name = "actividades")
public class Actividad {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int idactividad;
  private String nombreActividad;
  private String descripcion;

  @Column(nullable = false)
  private int idproyecto;

  @Column(nullable = false)
  private String usuario_owner;

  private float peso;
  private int estado;

  public int getIdactividad() {
    return idactividad;
  }

  public void setIdactividad(int idactividad) {
    this.idactividad = idactividad;
  }

  public String getNombreActividad() {
    return nombreActividad;
  }

  public void setNombreActividad(String nombreActividad) {
    this.nombreActividad = nombreActividad;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public int getIdproyecto() {
    return idproyecto;
  }

  public void setIdproyecto(int idproyecto) {
    this.idproyecto = idproyecto;
  }

  public String getUsuario_owner() {
    return usuario_owner;
  }

  public void setUsuario_owner(String usuario_owner) {
    this.usuario_owner = usuario_owner;
  }

  public float getPeso() {
    return peso;
  }

  public void setPeso(float peso) {
    this.peso = peso;
  }

  public int getEstado() {
    return estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }
}
