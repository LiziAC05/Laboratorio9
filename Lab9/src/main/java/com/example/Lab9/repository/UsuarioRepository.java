package com.example.Lab9.repository;

import com.example.Lab9.dto.AreaDto;
import com.example.Lab9.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    @Query(value = "select u.* from usuarios u inner join areas a on u.idarea=a.idarea where a.idarea=?1",nativeQuery = true)
    List<Usuario> usuariosArea(int id);
}
