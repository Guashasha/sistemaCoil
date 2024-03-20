DROP DATABASE IF EXISTS COIL;

CREATE DATABASE IF NOT EXISTS COIL;

USE COIL;

CREATE USER IF NOT EXISTS "admin_COIL"@"%" IDENTIFIED BY "habitacionDeVuelo";

GRANT INSERT, SELECT, UPDATE, DELETE ON COIL.* TO "admin_COIL"@"%";

CREATE TABLE `persona` (
  `idPersona` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `apellidoPaterno` varchar(20) NOT NULL,
  `apellidoMaterno` varchar(20) NOT NULL,
  `universidad` int NOT NULL
);

CREATE TABLE `estudiante` (
  `idEstudiante` int AUTO_INCREMENT,
  `idPersona` int,
  `matricula` char(10) NOT NULL,
  PRIMARY KEY (`idEstudiante`, `idPersona`)
);

CREATE TABLE `universidad` (
  `idUniversidad` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `paisOrigen` varchar(40) NOT NULL
);

CREATE TABLE `region` (
  `idRegion` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30)
);

CREATE TABLE `facultad` (
  `idFacultad` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `region` int NOT NULL
);

CREATE TABLE `academico` (
  `cedulaProfesional` varchar(30),
  `numeroDePersonal` varchar(40) NOT NULL,
  `idPersona` int NOT NULL,
  `areaEstudios` varchar(40) NOT NULL,
  `correoElectronico` varchar(30) NOT NULL,
  `numeroTelefonico` char(12) NOT NULL,
  `categoriaContratacion` varchar(40),
  `facultad` int NOT NULL,
  PRIMARY KEY (`cedulaProfesional`, `numeroDePersonal`)
);

CREATE TABLE `colaboracion` (
  `idColaboracion` int PRIMARY KEY AUTO_INCREMENT,
  `estado` ENUM ('propuesta', 'aceptada', 'rechazada', 'disponible', 'vinculada', 'activa', 'en revision', 'finalizada') NOT NULL,
  `tipo` ENUM ('claseEspejo', 'COIl') NOT NULL,
  `temaInteres` varchar(80) NOT NULL,
  `idioma` varchar(30) NOT NULL,
  `objetivo` varchar(80),
  `fechaInicio` date,
  `fechaFin` date,
  `perfilEstudiante` varchar(50) NOT NULL
);

CREATE TABLE `estudiantesColaboracion` (
  `idColaboracion` int,
  `idEstudiante` int
);

CREATE TABLE `academicoDesarrolla` (
  `idColaboracion` int,
  `idAcademico` varchar(30)
);

CREATE TABLE `solicitaParticiparColaboracion` (
  `idAcademico` varchar(30) NOT NULL,
  `idColaboracion` int NOT NULL
);

CREATE TABLE `cuenta` (
  idCuenta int PRIMARY KEY AUTO_INCREMENT,
  `idAcademico` varchar(30),
  `nombreUsuario` varchar(50) NOT NULL,
  `contrasena` varchar(30) NOT NULL,
  `estado` ENUM ('pendiente', 'aceptada', 'rechazada') NOT NULL
);

CREATE TABLE `retroalimentacion` (
  `idRetroalimentacion` int PRIMARY KEY AUTO_INCREMENT,
  `interaccionPar` int NOT NULL,
  `comentario` varchar(200),
  `usuario` int NOT NULL
);

CREATE TABLE `retroalimentacionActividad` (
  `idRetroalimentacion` int PRIMARY KEY,
  `dificultad` int NOT NULL,
  `interes` int NOT NULL,
  `actividad` int NOT NULL
);

CREATE TABLE `retroalimentacionColaboracion` (
  `idRetroalimentacion` int PRIMARY KEY,
  `habilidadesObtenidas` int NOT NULL,
  `calificacion` int NOT NULL,
  `intercambioCultural` int NOT NULL,
  `mejoraDelLenguaje` int NOT NULL,
  `trabajoColaborativo` int NOT NULL,
  `mejoraFormacionProfesional` int NOT NULL,
  `colaboracion` int NOT NULL
);

CREATE TABLE `actividad` (
  `idActividad` int PRIMARY KEY AUTO_INCREMENT,
  `titulo` varchar(50) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `tipo` ENUM ('rompehielo', 'intercultural', 'disciplinar', 'cierre') NOT NULL,
  `colaboracion` int NOT NULL
);

ALTER TABLE `persona` ADD FOREIGN KEY (`universidad`) REFERENCES `universidad` (`idUniversidad`);

ALTER TABLE `estudiante` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `facultad` ADD FOREIGN KEY (`region`) REFERENCES `region` (`idRegion`);

ALTER TABLE `academico` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `academico` ADD FOREIGN KEY (`facultad`) REFERENCES `facultad` (`idFacultad`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idEstudiante`) REFERENCES `estudiante` (`idEstudiante`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `solicitaCuenta` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `solicitaCuenta` ADD FOREIGN KEY (`idCuenta`) REFERENCES `cuenta` (`idCuenta`);

ALTER TABLE `solicitaParticiparColaboracion` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `solicitaParticiparColaboracion` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `retroalimentacion` ADD FOREIGN KEY (`usuario`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `retroalimentacionActividad` ADD FOREIGN KEY (`idRetroalimentacion`) REFERENCES `retroalimentacion` (`idRetroalimentacion`);

ALTER TABLE `retroalimentacionActividad` ADD FOREIGN KEY (`actividad`) REFERENCES `actividad` (`idActividad`);

ALTER TABLE `retroalimentacionColaboracion` ADD FOREIGN KEY (`idRetroalimentacion`) REFERENCES `retroalimentacion` (`idRetroalimentacion`);

ALTER TABLE `retroalimentacionColaboracion` ADD FOREIGN KEY (`colaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `actividad` ADD FOREIGN KEY (`colaboracion`) REFERENCES `colaboracion` (`idColaboracion`);
