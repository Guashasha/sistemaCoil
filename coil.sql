DROP DATABASE IF EXISTS COIL;

CREATE DATABASE IF NOT EXISTS COIL;

USE COIL;

CREATE USER IF NOT EXISTS "admin_COIL"@"192.168.100.216" IDENTIFIED BY "habitacionDeVuelo";

GRANT INSERT, SELECT, UPDATE, DELETE ON COIL.* TO "admin_COIL"@"192.168.100.216";

CREATE TABLE `persona` (
  `idPersona` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `apellidoPaterno` varchar(20) NOT NULL,
  `apellidoMaterno` varchar(20) NOT NULL
);

CREATE TABLE `estudiante` (
  `idEstudiante` int AUTO_INCREMENT,
  `idPersona` int,
  PRIMARY KEY (`idEstudiante`, `idPersona`)
);

CREATE TABLE `estudianteExterno` (
  `matricula` varchar(15) PRIMARY KEY,
  `idEstudiante` int,
  `universidad` int
);

CREATE TABLE `estudianteUV` (
  `matricula` char(9) PRIMARY KEY NOT NULL,
  `idEstudiante` int,
  `facultad` int
);

CREATE TABLE `universidad` (
  `idUniversidad` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `paisOrigen` varchar(40) NOT NULL
);

CREATE TABLE `facultad` (
  `idFacultad` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) NOT NULL,
  `region` int
);

CREATE TABLE `region` (
  `idRegion` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30)
);

CREATE TABLE `academico` (
  `cedulaProfesional` int AUTO_INCREMENT,
  `idPersona` int,
  `areaEstudios` varchar(40) NOT NULL,
  `correoElectronico` varchar(30) NOT NULL,
  `numeroTelefonico` char(12) NOT NULL,
  PRIMARY KEY (`cedulaProfesional`, `idPersona`)
);

CREATE TABLE `academicoExterno` (
  `cedulaProfesional` int PRIMARY KEY,
  `universidad` int
);

CREATE TABLE `academicoUV` (
  `cedulaProfesional` int PRIMARY KEY,
  `categoriaContratacion` varchar(30) NOT NULL,
  `facultad` int
);

CREATE TABLE `cursoTaller` (
  `idCursoTaller` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(30) NOT NULL,
  `expositor` varchar(50) NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `modalidad` ENUM ('virtual', 'presencial', 'mixto') NOT NULL
);

CREATE TABLE `colaboracionCOIL` (
  `idColaboracion` int PRIMARY KEY AUTO_INCREMENT,
  `tipo` ENUM ('claseEspejo', 'COIl') NOT NULL,
  `temaInteres` varchar(80) NOT NULL,
  `idioma` varchar(30) NOT NULL,
  `objetivo` varchar(80),
  `fechaInicio` date,
  `fechaFin` date NOT NULL,
  `perfilEstudiante` varchar(50) NOT NULL
);

CREATE TABLE `estudiantesColaboracion` (
  `idEstudiante` int,
  `idColaboracion` int
);

CREATE TABLE `academicoInscrito` (
  `idAcademico` int,
  `idCurso` int,
  `estadoAcreditacion` ENUM ('aprobado', 'reprobado') NOT NULL
);

CREATE TABLE `academicoDesarrolla` (
  `idAcademico` int,
  `idColaboracion` int
);

CREATE TABLE `cuenta` (
  `idAcademico` int,
  `contrasena` varchar(30)
);

ALTER TABLE `estudiante` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `estudianteExterno` ADD FOREIGN KEY (`idEstudiante`) REFERENCES `estudiante` (`idEstudiante`);

ALTER TABLE `estudianteExterno` ADD FOREIGN KEY (`universidad`) REFERENCES `universidad` (`idUniversidad`);

ALTER TABLE `estudianteUV` ADD FOREIGN KEY (`idEstudiante`) REFERENCES `estudiante` (`idEstudiante`);

ALTER TABLE `estudianteUV` ADD FOREIGN KEY (`facultad`) REFERENCES `facultad` (`idFacultad`);

ALTER TABLE `facultad` ADD FOREIGN KEY (`region`) REFERENCES `region` (`idRegion`);

ALTER TABLE `academico` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `academicoExterno` ADD FOREIGN KEY (`cedulaProfesional`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoExterno` ADD FOREIGN KEY (`universidad`) REFERENCES `universidad` (`idUniversidad`);

ALTER TABLE `academicoUV` ADD FOREIGN KEY (`cedulaProfesional`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoUV` ADD FOREIGN KEY (`facultad`) REFERENCES `facultad` (`idFacultad`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idEstudiante`) REFERENCES `estudiante` (`idEstudiante`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracionCOIL` (`idColaboracion`);

ALTER TABLE `academicoInscrito` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoInscrito` ADD FOREIGN KEY (`idCurso`) REFERENCES `cursoTaller` (`idCursoTaller`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracionCOIL` (`idColaboracion`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

DELIMITER //

CREATE PROCEDURE `insertarEstudianteUV`(
    IN `matricula` CHAR(9),
    IN `nombre` VARCHAR(20),
    IN `apellidoPaterno` VARCHAR(20),
    IN `apellidoMaterno` VARCHAR(20),
    IN `facultad` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY INVOKER
COMMENT 'Inserta Estudiante: Persona -> Estudiante -> EstudianteUV'
BEGIN
    INSERT INTO Persona (nombre, apellidoPaterno, apellidoMaterno) VALUES (nombre, apellidoPaterno, apellidoMaterno);
    INSERT INTO Estudiante (idPersona) VALUES (LAST_INSERT_ID());
    INSERT INTO EstudianteUV (matricula, idEstudiante, facultad) VALUES (matricula, LAST_INSERT_ID(), facultad);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `insertarEstudianteExterno`(
    IN `matricula` CHAR(9),
    IN `nombre` VARCHAR(20),
    IN `apellidoPaterno` VARCHAR(20),
    IN `apellidoMaterno` VARCHAR(20),
    IN `universidad` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY INVOKER
COMMENT 'Inserta EstudianteExtranjero: Persona-> Estudiante -> EstudianteExtranjero'
BEGIN
    INSERT INTO Persona (nombre, apellidoPaterno, apellidoMaterno) VALUES (nombre, apellidoPaterno, apellidoMaterno);
    INSERT INTO Estudiante (idPersona) VALUES (LAST_INSERT_ID());
    INSERT INTO estudianteExterno(matricula, idEstudiante, universidad) VALUES (matricula, LAST_INSERT_ID(), universidad);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `insertarAcademicoUV`(
    IN `nombre` VARCHAR(20),
    IN `apellidoPaterno` VARCHAR(20),
    IN `apellidoMaterno` VARCHAR(20),
    IN `cedulaProfesional` INT,
    IN `areaEstudios` VARCHAR(40),
    IN `correoElectronico` VARCHAR(30),
    IN `numeroTelefonico` CHAR(12),
    IN `categoriaContratacion` VARCHAR(30),
    IN `facultad` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY INVOKER
COMMENT 'Inserta un AcademicoUV: Persona -> Academico -> AcademicoUV'
BEGIN
    INSERT INTO Persona (nombre, apellidoPaterno, apellidoMaterno) VALUES (nombre, apellidoPaterno, apellidoMaterno);
    INSERT INTO Academico (cedulaProfesional, idPersona, areaEstudios, correoElectronico, numeroTelefonico) VALUES (cedulaProfesional, LAST_INSERT_ID(), areaEstudios, correoElectronico, numeroTelefonico);
    INSERT INTO academicouv (cedulaProfesional, categoriaContratacion, facultad) VALUES (cedulaProfesional, categoriaContratacion, facultad);
END //

DELIMITER ;



CREATE VIEW vista_academico_uv AS
SELECT
  p.nombre,
  p.apellidoPaterno,
  p.apellidoMaterno,
  auv.cedulaProfesional AS cedulaProfesional,
  a.areaEstudios,
  a.correoElectronico,
  a.numeroTelefonico,
  auv.categoriaContratacion,
  auv.facultad AS idFacultad,
  f.nombre AS nombreFacultad,
  r.nombre AS nombreRegion
FROM
  academicoUV auv
  INNER JOIN academico a ON auv.cedulaProfesional = a.cedulaProfesional
  INNER JOIN persona p ON a.idPersona = p.idPersona
  INNER JOIN facultad f ON auv.facultad = f.idFacultad
  INNER JOIN region r ON f.region = r.idRegion;


