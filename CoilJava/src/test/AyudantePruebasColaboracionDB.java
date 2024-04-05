package test;

public class AyudantePruebasColaboracionDB {

    public static void agregarPrecondiciones () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('ABC123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'Filosofia', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES ('propuesta', 'claseEspejo', 'Inteligencia Artificial', 'Español', 'Mejorar habilidades en IA', '2024-05-01', '2024-06-30', 'Estudiantes de informática');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO colaboracion (estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante) VALUES ('aceptada', 'COIl', 'Desarrollo web', 'Inglés', 'Crear un proyecto conjunto', '2024-04-15', '2024-07-15', 'Estudiantes de ingeniería de software');");


    }

    public static void borrarTodosDatosTabla () {
        ConfiguracionPrueba.borrarDatosTablaPais();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();

    }

}
