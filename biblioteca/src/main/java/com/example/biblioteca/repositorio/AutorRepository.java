package com.example.biblioteca.repositorio;

import com.example.biblioteca.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz vacía: todos los métodos (save, saveAll, findById, findAll, count, deleteById...)
 * vienen heredados de JpaRepository. Spring genera la implementación al arrancar.
 * <Autor, Integer> = entidad que gestiona + tipo de su @Id.
 */
public interface AutorRepository extends JpaRepository<Autor, Integer> {
        Optional<Autor> findByNombre (String nombre);

        List<Autor> findByNombreContainingIgnoreCase (String fragmento);

        long countByPais (String pais);
}
