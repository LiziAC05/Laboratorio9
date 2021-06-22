package com.example.Lab9.entity;

import javax.persistence.*;

@Entity
@Table(name = "proyectos")
public class Proyecto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int idproyecto;

  private String nombreProyecto;

  @Column(nullable = false)
  private String usuario_owner;

  public int getIdproyecto() {
    return idproyecto;
  }

  public void setIdproyecto(int idproyecto) {
    this.idproyecto = idproyecto;
  }

  public String getNombreProyecto() {
    return nombreProyecto;
  }

  public void setNombreProyecto(String nombreProyecto) {
    this.nombreProyecto = nombreProyecto;
  }

  public String getUsuario_owner() {
    return usuario_owner;
  }

  public void setUsuario_owner(String usuario_owner) {
    this.usuario_owner = usuario_owner;
  }
}
