package com.example.Lab9.dto;

import com.example.Lab9.entity.Area;
import com.example.Lab9.entity.Usuario;

import java.util.List;

public class AreaDto {
    private Integer idarea;
    private String nombrearea;
    private List<Usuario> listaUsuarios;

    public Integer getIdarea() {
        return idarea;
    }

    public void setIdarea(Integer idarea) {
        this.idarea = idarea;
    }

    public String getNombrearea() {
        return nombrearea;
    }

    public void setNombrearea(String nombrearea) {
        this.nombrearea = nombrearea;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
}
