package test;

import static test.ConfiguracionPrueba.*;

public class AyudantePruebasColaboracionDB {

    public static void agregarPrecondiciones () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais, Iso,nombre) VALUES (2, 'EU','Estados Unidos');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Cambridge',2);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (1, 'Jose', 'Lopez Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('123', '123456', 1, 'tecnica', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (2, 'Esther', 'Herrara Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '264', 2, 'economico-administrativo', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos , universidad) VALUES (3, 'Johan', 'Wallstrom Figurason', 2);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('102939', '322', 3, 'economico-administrativo', 'johan@ptron.com', '522238536430', 'Sociologia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (4, 'Eduardo', 'Villegas Hurtado', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 4, 'zs22013693')");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (5, 'John', 'Smith Onell', 2);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 5, 'zs2201356')");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (1, 'activa', 'claseEspejo', 'Inteligencia Artificial', 'Español', 'Mejorar habilidades en IA', '2024-05-01', '2024-06-30', 'Estudiantes de informática');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (2, 'aceptada', 'COIl', 'Desarrollo web', 'Inglés', 'Crear un proyecto conjunto', '2024-04-15', '2024-07-15', 'Estudiantes de ingeniería de software');");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (3, 'propuesta', 'claseEspejo', 'Inteligencia Artificial', 'Español', 'Mejorar habilidades en IA', '2024-05-01', '2024-06-30', 'Estudiantes de informática');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (4, 'disponible', 'COIL', 'Desarrollo web', 'Inglés', 'Crear un proyecto conjunto', '2024-04-15', '2024-07-15', 'Estudiantes de ingeniería de software');");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (2, '102939', 'anfitrion')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (1, '123', 'anfitrion')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (1, '200011', 'pendiente')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (3, '200011', 'anfitrion')");


        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiantesColaboracion (idColaboracion, idEstudiante) VALUES (1, 1)");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiantesColaboracion (idColaboracion, idEstudiante) VALUES (1, 2)");


    }

    public static void agregarColaboracionesParaRetroalimentacion () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (3, 'enRevision', 'COIl', 'Desarrollo web', 'Inglés', 'Crear un proyecto conjunto', '2024-04-15', '2024-07-15', 'Estudiantes de ingeniería de software');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (4, 'enRevision', 'COIl', 'Desarrollo no web', 'español', 'Crear un proyecto conjunto', '2024-04-15', '2024-07-15', 'Estudiantes de ingeniería de software');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (3, '200011', 'anfitrion')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (4, '123', 'anfitrion')");
    }

    public static void vincularActividadConColaboracion() {
        borrarTablasActividadTest();
        agregarActividades();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (idColaboracion, estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES (1, 'propuesta', 'claseEspejo', 'Inteligencia Artificial', 'Español', 'Mejorar habilidades en IA', '2024-05-01', '2024-06-30', 'Estudiantes de informática');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO calendarioActividades (idActividad, idColaboracion, fechaFinalizacion) values (1,1,'2024-07-05');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO calendarioActividades (idActividad, idColaboracion, fechaFinalizacion) values (2,1,'2024-07-05');");
    }

    public static void borrarTablasActividadTest () {
        ConfiguracionPrueba.borrarDatosTablaCalendarioActividades();
        borrarDatosTablaAcademicoDesarrolla();
        borrarDatosTablaEstudiantesColaboracion();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaActividad();
    }

    public static void agregarActividades () {
        ejecutarInstruccionSQL("INSERT INTO actividad (titulo, descripcion, tipo) values ('kahoot prueba','descripcion de la actividad prueba','cierre');");
        ejecutarInstruccionSQL("INSERT INTO actividad (titulo, descripcion, tipo) values ('Presentacion','presentacion individual ante grupo','rompeHielo');");
    }

    public static void borrarTodosDatosTabla () {
        ConfiguracionPrueba.borrarDatosTablaEstudiantesColaboracion();
        ConfiguracionPrueba.borrarDatosTablaAcademicoDesarrolla();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();

    }
}