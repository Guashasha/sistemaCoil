DELETE FROM estudiantesColaboracion;
ALTER TABLE estudiantesColaboracion AUTO_INCREMENT = 1;

DELETE FROM academicoDesarrolla;
ALTER TABLE academicoDesarrolla AUTO_INCREMENT = 1;

DELETE FROM cuenta;
ALTER TABLE cuenta AUTO_INCREMENT = 1;

DELETE FROM estudiante;
ALTER TABLE estudiante AUTO_INCREMENT = 1;

DELETE FROM academico;
ALTER TABLE academico AUTO_INCREMENT = 1;

DELETE FROM persona;
ALTER TABLE persona AUTO_INCREMENT = 1;

DELETE FROM facultad;
ALTER TABLE facultad AUTO_INCREMENT = 1;

DELETE FROM region;
ALTER TABLE region AUTO_INCREMENT = 1;

DELETE FROM universidad;
ALTER TABLE universidad AUTO_INCREMENT = 1;

DELETE FROM colaboracion;
ALTER TABLE colaboracion AUTO_INCREMENT = 1;

DELETE FROM retroalimentacionColaboracion;
ALTER TABLE retroalimentacionColaboracion AUTO_INCREMENT = 1;

DELETE FROM calendarioActividades;
ALTER TABLE calendarioActividades AUTO_INCREMENT = 1;

DELETE FROM actividad;
ALTER TABLE actividad AUTO_INCREMENT = 1;

DELETE FROM retroalimentacionActividad;
ALTER TABLE retroalimentacionActividad AUTO_INCREMENT = 1;


INSERT INTO universidad (nombre, paisOrigen) VALUES ("universidad veracruzana", 146);
INSERT INTO universidad (nombre, paisOrigen) VALUES ("ANAHUAC", 35);

INSERT INTO region (nombre) VALUES ("xalapa");
INSERT INTO region (nombre) VALUES ("veracruz");

INSERT INTO facultad (nombre, region) VALUES ("estadistica e informatica", 1);
INSERT INTO facultad (nombre, region) VALUES ("psicología", 2);

INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);
INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('ABC123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);

INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);
INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'Filosofia', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);

INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (3, 'Johan', 'Wallstrom', 'Figurason', 2);
INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('102939', '02', 3, 'Filosofia', 'johan@ptron.com', '522238536430', 'Sociologia', 1);

INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (4, 'Eduardo', 'Villegas', 'Hurtado', 1);
INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 4, 'zs22013693');

INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (5, 'John', 'Smith', 'Onell', 2);
INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 5, 'zs2201356');

INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (1, 'propuesta', 'claseEspejo', 'Inteligencia Artificial', 'Español', 'Mejorar habilidades en IA', '2024-05-01', '2024-06-30', 'Estudiantes de informática');
INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (2, 'aceptada', 'COIl', 'Desarrollo web', 'Inglés', 'Crear un proyecto conjunto', '2024-04-15', '2024-07-15', 'Estudiantes de ingeniería de software');

INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (1, 'ABC123', 'anfitrion');
INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (1, '200011', 'pendiente');

INSERT INTO estudiantesColaboracion (idColaboracion, idEstudiante) VALUES (1, 1);

INSERT INTO actividad (titulo, descripcion, tipo) VALUES ("actividad prueba 1", "hola mundo", "disciplinar");
INSERT INTO actividad (titulo, descripcion, tipo) VALUES ("actividad prueba 2", "adios mundo", "intercultural");
