package test;

import DAO.CuentaDAO;
import DAO.AcademicoAuxiliar;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class AyudantePruebasCuentaDB {
    private static final Logger BITACORA = Logger.getLogger(AcademicoAuxiliar.class);


    public static void agregarPrecondiciones () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('UniversidadDTO Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

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
        CuentaDTO cuentaDTO = new CuentaDTO();

        int filasAfectadas = 0;

        cuentaDTO.setIdCuenta(1);
        cuentaDTO.setIdPersona(4);
        cuentaDTO.setNombreUsuario("EduVillegas");
        cuentaDTO.setContrasena("eduVillegas2000");
        cuentaDTO.setTipo(CuentaDTO.TipoUsuario.estudiante);
        cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.aceptada);
        try {
            filasAfectadas = CuentaDAO.agregarCuenta(cuentaDTO);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error.getMessage());
        }


        assertEquals(1, filasAfectadas);
    }
}
