DROP DATABASE IF EXISTS COIL;

CREATE DATABASE IF NOT EXISTS COIL;

USE COIL;

CREATE USER IF NOT EXISTS "admin_COIL"@"localhost" IDENTIFIED BY "habitacionDeVuelo";

GRANT INSERT, SELECT, UPDATE, DELETE ON COIL.* TO "admin_COIL"@"localhost";

CREATE TABLE `persona` (
  `idPersona` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `apellidoPaterno` varchar(20) NOT NULL,
  `apellidoMaterno` varchar(20) NOT NULL
);

CREATE TABLE `estudiante` (
  `idEstudiante` int AUTO_INCREMENT,
  `idPersona` int,
  `universidad` varchar(30) NOT NULL,
  `paisOrigen` varchar(30) NOT NULL,
  PRIMARY KEY (`idEstudiante`, `idPersona`)
);

CREATE TABLE `universidad` (
  `idUniversidad` int PRIMARY KEY NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `paisOrigen` varchar(40) NOT NULL
);

CREATE TABLE `academico` (
  `cedulaProfesional` int AUTO_INCREMENT,
  `idPersona` int,
  `institucion` int NOT NULL,
  `areaEstudios` varchar(40) NOT NULL,
  `correoElectronico` varchar(30) NOT NULL,
  `numeroTelefonico` char(12) NOT NULL,
  PRIMARY KEY (`cedulaProfesional`, `idPersona`)
);

CREATE TABLE `academicoExterno` (
  `cedulaProfesional` int PRIMARY KEY
);

CREATE TABLE `academicoUV` (
  `cedulaProfesional` int PRIMARY KEY,
  `categoriaContratacion` varchar(30) NOT NULL,
  `region` varchar(30) NOT NULL
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

ALTER TABLE `academico` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `academico` ADD FOREIGN KEY (`institucion`) REFERENCES `universidad` (`idUniversidad`);

ALTER TABLE `academicoExterno` ADD FOREIGN KEY (`cedulaProfesional`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoUV` ADD FOREIGN KEY (`cedulaProfesional`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idEstudiante`) REFERENCES `estudiante` (`idEstudiante`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracionCOIL` (`idColaboracion`);

ALTER TABLE `academicoInscrito` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoInscrito` ADD FOREIGN KEY (`idCurso`) REFERENCES `cursoTaller` (`idCursoTaller`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracionCOIL` (`idColaboracion`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);
