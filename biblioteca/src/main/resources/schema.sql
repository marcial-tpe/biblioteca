CREATE TABLE IF NOT EXISTS autores (
                                       id     INTEGER PRIMARY KEY,
                                       nombre VARCHAR(255),
    pais   VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS libros (
                                      id               INTEGER PRIMARY KEY,
                                      titulo           VARCHAR(255),
    genero           VARCHAR(255),
    anio_publicacion INTEGER NOT NULL,
    precio           REAL    NOT NULL,
    disponible       BOOLEAN NOT NULL,
    autor_id         INTEGER REFERENCES autores(id)
    );