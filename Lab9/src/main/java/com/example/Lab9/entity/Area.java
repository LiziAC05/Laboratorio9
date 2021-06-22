package com.example.Lab9.entity;

import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;

@Entity
@Table(name = "areas")
public class Area {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int idarea;

  @Column(name = "nombreArea")
  private String nombrearea;

  public int getIdarea() {
    return idarea;
  }

  public void setIdarea(int idarea) {
    this.idarea = idarea;
  }

  public String getNombrearea() {
    return nombrearea;
  }

  public void setNombrearea(String nombrearea) {
    this.nombrearea = nombrearea;
  }
}
