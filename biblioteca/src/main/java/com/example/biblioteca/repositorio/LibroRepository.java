package com.example.biblioteca.repositorio;

import com.example.biblioteca.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interfaz vacía: todos los métodos vienen heredados de JpaRepository.
 * <Libro, Integer> = entidad que gestiona + tipo de su @Id.
 */
public interface LibroRepository extends JpaRepository<Libro, Integer> {
        List<Libro> findByDisponibleTrue();

        List<Libro> findTop3ByOrderByPrecioAsc ();

        List<Libro> findByAutor_Pais (String pais);

        @Query("SELECT l FROM Libro l WHERE l.autor.pais IN :paises AND l.precio < :precioMax ORDER BY l.precio ")
        List<Libro> buscarPorPaisesYPrecioMaximo (@Param("paises") List<String> paises,@Param("precioMax") double precioMax);


}
