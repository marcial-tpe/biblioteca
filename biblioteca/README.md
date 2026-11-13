# Biblioteca: Spring Data JPA + SQLite

Ejemplo de relación 1:N entre **Autor** y **Libro** con Spring Boot, Hibernate y SQLite.

## Cómo abrirlo en IntelliJ
1. *File → Open* y elige **esta carpeta**, la que contiene `pom.xml`, no la carpeta de arriba.
2. Comprueba el JDK en *File → Project Structure → SDK*: Java 25 o superior.
3. Ejecuta `BibliotecaApplication` con el botón ▶.

La base de datos `biblioteca.db` se crea en el directorio de trabajo, normalmente esta misma carpeta.
Si la borras, se vuelve a crear con las tablas vacías.

## Estructura
| Paquete | Contenido |
|---|---|
| `modelo` | Entidades JPA: `Autor` (lado inverso, `mappedBy`) y `Libro` (lado propietario, con la FK `autor_id`) |
| `repositorio` | Interfaces `JpaRepository` con consultas derivadas (`findByNombre`, `findByAutorNombre`...) |
| `BibliotecaApplication` | Arranque y demo en un `CommandLineRunner` |

## Convenciones de nombres en Java
- Clases e interfaces en **PascalCase**: `Autor`, `Libro`, `LibroRepository`. El fichero se llama igual que la clase: `Autor.java`.
- Atributos, métodos y variables en **camelCase**: `anioPublicacion`, `agregarLibro()`, `autorRepository`.
- Paquetes en **minúsculas**: `com.example.biblioteca.modelo`.
- Tablas y columnas en **snake_case**: `autores`, `anio_publicacion`, `autor_id`. Spring hace la conversión automáticamente.
