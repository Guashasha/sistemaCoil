package test;

import AccesoADatos.CuentaDB;
import Logica.DAO.DAOAcademico;
import Logica.Dominio.Cuenta;
import org.apache.log4j.Logger;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AyudantePruebasCuentaDB {
    private static final Logger BITACORA = Logger.getLogger(DAOAcademico.class);


    public static void agregarPrecondiciones () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('ABC123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'Filosofia', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (4, 'Eduardo', 'Villegas', 'Hurtado', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 4, 'zs22013693')");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (5, 'John', 'Smith', 'Onell', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 5, 'zs2201356')");

        agregarCuentaTipoEstudiantePrueba();
    }

    public static void borrarTodosDatosTabla () {
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    private static void agregarCuentaTipoEstudiantePrueba () {
        Cuenta cuenta = new Cuenta();

        int filasAfectadas = 0;

        cuenta.setIdCuenta(1);
        cuenta.setIdPersona(4);
        cuenta.setNombreUsuario("EduVillegas");
        cuenta.setContrasena("eduVillegas2000");
        cuenta.setTipo(Cuenta.TipoUsuario.estudiante);
        cuenta.setEstado(Cuenta.EstadoCuenta.aceptada);
        try {
            filasAfectadas = CuentaDB.agregarCuenta(cuenta);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }


        assertEquals(1, filasAfectadas);
    }
}
