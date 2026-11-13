package com.example.biblioteca;

import com.example.biblioteca.modelo.Autor;
import com.example.biblioteca.modelo.Libro;
import com.example.biblioteca.repositorio.AutorRepository;
import com.example.biblioteca.repositorio.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

/**
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan.
 * El escaneo empieza en este paquete: "modelo" y "repositorio" deben estar DENTRO de él.
 */
@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        // Crea el contexto de Spring, configura Hibernate y los repositorios,
        // y al final ejecuta los CommandLineRunner.

        SpringApplication.run(BibliotecaApplication.class, args);
    }

    /**
     * Spring llama a este método al arrancar y le INYECTA los repositorios como parámetros.
     * Todos los métodos que se usan aquí vienen de serie en JpaRepository.
     */
    @Bean
    CommandLineRunner demostrarFuncionesJpa(AutorRepository autorRepository, LibroRepository libroRepository) {
        return args -> {

            if (autorRepository.count() >0 || libroRepository.count()>0) {
                autorRepository.deleteAllInBatch();
                libroRepository.deleteAllInBatch();
            }

            //SAVE
            Autor borges = new Autor("Jorge Luis Borges","Argentina");
            System.out.println("ID del autor antes de grabar: " + borges.getId());
            borges = autorRepository.save(borges);
            System.out.println("ID del autor despues de grabar: " + borges.getId());

            Autor allende = autorRepository.save(new Autor("Isabel Allende","Chile"));

            Autor cervantes = new Autor("Miguel de Cervantes","España");
            Autor vargas = new Autor ("Mario Vargas Llosa","Peru");

            List<Autor> listaAutores = List.of(cervantes,vargas);
            List<Autor> autoresGuardar = autorRepository.saveAll(listaAutores);

            //List<Autor> autoresGuardar2 = autorRepository.saveAll(List.of(cervantes,vargas));

            Autor gabo = autorRepository.save(new Autor("Gabriel Garcia Marquez", "Colombia"));
            Libro cien = new Libro("Cien años de soledad","Novela",1967,25.5,true);
            Libro amor = new Libro("El amor en los tiempos del colera", "Novela", 1985, 17, false);
            cien.setAutor(gabo);
            amor.setAutor(gabo);
            libroRepository.saveAll(List.of(cien,amor));

            Libro aleph = new Libro("El Aleph", "Cuentos", 1949, 13.90, true);
            Libro quijote = new Libro("Don Quijote de la Mancha", "Novela", 1615, 25, true);
            Libro casa = new Libro("La casa de los espíritus", "Novela", 1982, 15, true);

            aleph.setAutor(borges);
            quijote.setAutor(cervantes);
            casa.setAutor(allende);
            libroRepository.saveAll(List.of(aleph, quijote, casa));

            // FIND
            var busca_id=1;
            Optional<Libro> resultado = libroRepository.findById(busca_id);
            resultado.ifPresentOrElse(
                    libro -> System.out.println("Libro Encontrado.Titulo: "+libro.getTitulo()),
                    () -> System.out.println("No existe libro con ID "+ busca_id)
            );

            var busca_id2=100;
            Optional<Libro> resultado2 = libroRepository.findById(busca_id2);
            resultado2.ifPresentOrElse(
                    libro -> System.out.println("Libro Encontrado.Titulo: "+libro.getTitulo()),
                    () -> System.out.println("No existe libro con ID "+ busca_id2)
            );
        //LEER DATOS

            List<Libro> listaLibros = libroRepository.findAll();
            for (Libro libro: listaLibros) {
                System.out.println(
                        "ID:" + libro.getId()
                                + "Titulo: " + libro.getTitulo()
                                + "Autor" + libro.getAutor().getNombre()

                );
            }
            
            //BUSQUEDA POR LITERAL
            var inicioTitulo = "el";
            List<Libro> librosEncontrados = libroRepository.findAll().stream()
                    .filter(l -> l.getTitulo().toLowerCase().startsWith(inicioTitulo.toLowerCase()))
                    .toList();
            if (librosEncontrados.isEmpty()) {
                System.out.println("No existe coincidencia");
            }
            else {
                System.out.println("Libros que empiezan por: " + inicioTitulo);
                for (Libro libro: librosEncontrados) {
                    libro.setPrecio(libro.getPrecio()*0.9);
                    libroRepository.save(libro);
                    System.out.println(
                            "ID:" + libro.getId()
                                    + "Titulo: " + libro.getTitulo()
                                    + "Autor" + libro.getAutor().getNombre()
                    );
                }
            }
// CONSULTAS DINAMICAS
            // buscAR AUTOR POR NOMBRE
            var busqueda_autor = "Isabel Allende";
            autorRepository.findByNombre(busqueda_autor).ifPresentOrElse(
                    autor -> System.out.println("El autor existe: " + autor.getNombre()+ "-Oais:" + autor.getPais()),
                    () -> System.out.println("El autor no existe")
            );
// bucar por Fragmento
            var busqueda_fragmento = "m";
            autorRepository.findByNombreContainingIgnoreCase(busqueda_fragmento)
                    .forEach( autor -> System.out.println("Autor encontrado " + autor.getNombre()));
// CONTAR LIBROS POR PAIS
            var contar_pais = "España";
            System.out.println("Numero de libros en "+ contar_pais + autorRepository.countByPais(contar_pais) );

            //condiciones
            libroRepository.findByDisponibleTrue()
                    .forEach(libro -> System.out.println("Libro disponible: "+ libro.getTitulo()));

            libroRepository.findTop3ByOrderByPrecioAsc()
                    .forEach(libro -> System.out.println("Libro disponible: "+ libro.getTitulo() + "precio: "+ libro.getPrecio()));

            //buscar libros para un autor de un pais
            libroRepository.findByAutor_Pais(contar_pais)
                    .forEach(libro -> System.out.println("Libro encontrado de "+ contar_pais + "Titulo: " + libro.getTitulo() ));

            //Funciones Nuevas _ No dinamicas
            var listaPaises = List.of("Argentina","Chile","Colombia");
            var precioMax = 20;

            libroRepository.buscarPorPaisesYPrecioMaximo(listaPaises,precioMax)
                    .forEach(libro -> System.out.println("Titulo: "+libro.getTitulo() +" - precio: "+libro.getPrecio())
                    );

        };
    }
}
