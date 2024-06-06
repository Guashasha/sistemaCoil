DROP DATABASE IF EXISTS COIL;

SELECT "creando base de datos...";
CREATE DATABASE IF NOT EXISTS COIL;

USE COIL;

CREATE TABLE `persona` (
  `idPersona` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `apellidoPaterno` varchar(20) NOT NULL,
  `apellidoMaterno` varchar(20) NOT NULL,
  `universidad` int NOT NULL
);
ALTER TABLE persona AUTO_INCREMENT=1;

CREATE TABLE `estudiante` (
  `idEstudiante` int AUTO_INCREMENT,
  `idPersona` int,
  `matricula` char(10) NOT NULL,
  PRIMARY KEY (`idEstudiante`, `idPersona`)
);
ALTER TABLE estudiante AUTO_INCREMENT=1;

CREATE TABLE `universidad` (
  `idUniversidad` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `paisOrigen` int NOT NULL
);
ALTER TABLE universidad AUTO_INCREMENT=1;

CREATE TABLE `region` (
  `idRegion` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30)
);
ALTER TABLE region AUTO_INCREMENT=1;

CREATE TABLE `facultad` (
  `idFacultad` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `region` int NOT NULL
);
ALTER TABLE facultad AUTO_INCREMENT=1;

CREATE TABLE `academico` (
  `cedulaProfesional` varchar(30) NOT NULL,
  `numeroDePersonal` varchar(40) NULL,
  `idPersona` int NOT NULL,
  `areaEstudios` ENUM ('economico-administrativo', 'humanidades', 'tecnica', 'ciencias de la salud', 'biologia-agropecuarias', 'dgri') NULL,
  `correoElectronico` varchar(30) NOT NULL,
  `numeroTelefonico` char(13) NULL,
  `categoriaContratacion` varchar(40) NULL,
  `facultad` int NULL,
  PRIMARY KEY (`cedulaProfesional`)
);
ALTER TABLE academico AUTO_INCREMENT=1;

CREATE TABLE `colaboracion` (
  `idColaboracion` int PRIMARY KEY AUTO_INCREMENT,
  `estado` ENUM ('propuesta', 'aceptada', 'rechazada', 'disponible', 'vinculada', 'activa', 'enRevision', 'finalizada') NOT NULL,
  `tipo` ENUM ('claseEspejo', 'COIL') NULL,
  `temaInteres` varchar(80) NULL,
  `idioma` varchar(30) NULL,
  `objetivo` varchar(80) NULL,
  `fechaInicio` date NULL,
  `fechaFin` date NULL,
  `perfilEstudiante` varchar(50) NULL
);
ALTER TABLE colaboracion AUTO_INCREMENT=1;

CREATE TABLE `estudiantesColaboracion` (
  `idColaboracion` int,
  `idEstudiante` int
);
ALTER TABLE estudiantesColaboracion AUTO_INCREMENT=1;

CREATE TABLE `academicoDesarrolla` (
  `idColaboracion` int,
  `idAcademico` varchar(30),
  estado ENUM ('anfitrion', 'pendiente', 'aceptado', 'rechazado')
);
ALTER TABLE academicoDesarrolla AUTO_INCREMENT=1;

CREATE TABLE `cuenta` (
  idCuenta int PRIMARY KEY AUTO_INCREMENT,
  `idPersona` int NOT NULL,
  `nombreUsuario` varchar(50) NOT NULL UNIQUE,
  `contrasena` varchar(300) NOT NULL,
  `tipo` ENUM ('academico', 'estudiante', 'administrador') NOT NULL,
  `estado` ENUM ('pendiente', 'aceptada', 'rechazada', 'eliminada') NOT NULL
);
ALTER TABLE cuenta AUTO_INCREMENT=1;

CREATE TABLE `retroalimentacion` (
  `idRetroalimentacion` int PRIMARY KEY AUTO_INCREMENT,
  `interaccionPar` int NOT NULL,
  `comentario` varchar(200),
  `usuario` int NOT NULL
);
ALTER TABLE retroalimentacion AUTO_INCREMENT=1;

CREATE TABLE `retroalimentacionActividad` (
  `idRetroalimentacion` int PRIMARY KEY,
  `dificultad` int NOT NULL,
  `interes` int NOT NULL,
  `actividad` int NOT NULL
);
ALTER TABLE retroalimentacionActividad AUTO_INCREMENT=1;

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
ALTER TABLE retroalimentacionColaboracion AUTO_INCREMENT=1;

CREATE TABLE calendarioActividades (
  idActividad int NOT NULL,
  idColaboracion int NOT NULL,
  fechaInicio date NOT NULL,
  fechaFin date NOT NULL
);
ALTER TABLE calendarioActividades AUTO_INCREMENT=1;

CREATE TABLE `actividad` (
  `idActividad` int PRIMARY KEY AUTO_INCREMENT,
  `titulo` varchar(50) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `tipo` ENUM ('rompeHielo', 'intercultural', 'disciplinar', 'cierre') NOT NULL
);
ALTER TABLE actividad AUTO_INCREMENT=1;

CREATE TABLE pais (
idPais int NOT NULL AUTO_INCREMENT,
iso char(2) DEFAULT NULL,
nombre varchar(80) NOT NULL,
PRIMARY KEY (idPais)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
ALTER TABLE pais AUTO_INCREMENT=1;

ALTER TABLE `persona` ADD FOREIGN KEY (`universidad`) REFERENCES `universidad` (`idUniversidad`);

ALTER TABLE `estudiante` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `facultad` ADD FOREIGN KEY (`region`) REFERENCES `region` (`idRegion`);

ALTER TABLE `academico` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `academico` ADD FOREIGN KEY (`facultad`) REFERENCES `facultad` (`idFacultad`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `estudiantesColaboracion` ADD FOREIGN KEY (`idEstudiante`) REFERENCES `estudiante` (`idEstudiante`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `academicoDesarrolla` ADD FOREIGN KEY (`idAcademico`) REFERENCES `academico` (`cedulaProfesional`);

ALTER TABLE `cuenta` ADD FOREIGN KEY (`idPersona`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `retroalimentacion` ADD FOREIGN KEY (`usuario`) REFERENCES `persona` (`idPersona`);

ALTER TABLE `retroalimentacionActividad` ADD FOREIGN KEY (`idRetroalimentacion`) REFERENCES `retroalimentacion` (`idRetroalimentacion`);

ALTER TABLE `retroalimentacionActividad` ADD FOREIGN KEY (`actividad`) REFERENCES `actividad` (`idActividad`);

ALTER TABLE `retroalimentacionColaboracion` ADD FOREIGN KEY (`idRetroalimentacion`) REFERENCES `retroalimentacion` (`idRetroalimentacion`);

ALTER TABLE `retroalimentacionColaboracion` ADD FOREIGN KEY (`colaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `calendarioActividades` ADD FOREIGN KEY (`idColaboracion`) REFERENCES `colaboracion` (`idColaboracion`);

ALTER TABLE `calendarioActividades` ADD FOREIGN KEY (`idActividad`) REFERENCES `actividad` (`idActividad`);

ALTER TABLE `universidad` ADD FOREIGN KEY (`paisOrigen`) REFERENCES `pais` (`idPais`);

SELECT "creando usuarios...";
DROP USER IF EXISTS "admin_COIL"@"localhost";
DROP USER IF EXISTS "admin_COIL"@"%";

CREATE USER IF NOT EXISTS "admin_COIL"@"localhost" IDENTIFIED BY "habitacionDeVuelo";

GRANT INSERT, SELECT, EXECUTE, UPDATE, DELETE ON COIL.* TO "admin_COIL"@"localhost";

DROP USER IF EXISTS "CarrionMartinezPale"@"localhost";

CREATE USER IF NOT EXISTS "CarrionMartinezPale"@"localhost" IDENTIFIED BY "cremaxx";

GRANT ALL ON COIL.* TO "CarrionMartinezPale"@"localhost";

SELECT "cargando vistas...";
SOURCE vistasCoil.sql;

SELECT "cargando procedimientos...";
SOURCE procedimientosCoil.sql;

SELECT "ingresando datos...";
SOURCE datosCoil.sql;

SELECT "base de datos creada correctamente";

CALL registrar_cuenta_administrador("david", "carrion", "romero", 1, "admin", "contrasena");
