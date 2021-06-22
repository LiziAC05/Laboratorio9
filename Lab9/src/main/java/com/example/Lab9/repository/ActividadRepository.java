package com.example.Lab9.repository;

import com.example.Lab9.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    @Query(value = "select distinct a.*from actividades a inner join proyectos p join actividades a2 on p.idproyecto = a2.idproyecto\n" +
            "where p.idproyecto=?1",nativeQuery = true)
    List<Actividad> actividadesProyecto(int id);
    @Query(value="select idactividad from actividades\n" +
            "where idproyecto=?1",nativeQuery = true)
    List<Integer> idActividadProyecto(int id);
}
