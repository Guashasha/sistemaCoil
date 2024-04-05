package test.AccesoADatos;

import AccesoADatos.CuentaAcademicoDB;
import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CuentaAcademicoDBTest {
    // toDo getTodos
    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('ABC123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO cuenta (idCuenta ,idAcademico, nombreUsuario, contrasena, estado) VALUES (1, 'ABC123', 'joseLo', 'contrasena123', 'pendiente');");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'Filosofia', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");

    }

    @AfterEach
    void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaGetCuentaPorCedulaProfesionalExitosa () {
        CuentaAcademico cuentaAcademicoEsperado = new CuentaAcademico();
        cuentaAcademicoEsperado.setIdCuenta(1);
        cuentaAcademicoEsperado.setIdAcademico("ABC123");
        cuentaAcademicoEsperado.setNombreUsuario("joseLo");
        cuentaAcademicoEsperado.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        CuentaAcademico cuentaAcademicoReal = null;

        try {
            cuentaAcademicoReal = CuentaAcademicoDB.getCuentaPorCedulaProfesional("ABC123");

        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: getCuentaPorUsuarioExitosa" + errorDAO.getMessage());
        }
        assertEquals(cuentaAcademicoEsperado.getIdCuenta(), cuentaAcademicoReal.getIdCuenta());
        assertEquals(cuentaAcademicoEsperado.getIdAcademico(), cuentaAcademicoReal.getIdAcademico());
        assertEquals(cuentaAcademicoEsperado.getNombreUsuario(), cuentaAcademicoReal.getNombreUsuario());

    }

    @Test
    void pruebaGetCuentaPorUsuarioExitosa () {
        CuentaAcademico cuentaAcademicoEsperado = new CuentaAcademico();
        cuentaAcademicoEsperado.setIdCuenta(1);
        cuentaAcademicoEsperado.setIdAcademico("ABC123");
        cuentaAcademicoEsperado.setNombreUsuario("joseLo");
        cuentaAcademicoEsperado.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        CuentaAcademico cuentaAcademicoReal = null;

        try {
            cuentaAcademicoReal = CuentaAcademicoDB.getCuentaPorUsuario("joseLo");

        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: getCuentaPorUsuarioExitosa" + errorDAO.getMessage());
        }
        assertEquals(cuentaAcademicoEsperado.getIdCuenta(), cuentaAcademicoReal.getIdCuenta());
        assertEquals(cuentaAcademicoEsperado.getIdAcademico(), cuentaAcademicoReal.getIdAcademico());
        assertEquals(cuentaAcademicoEsperado.getNombreUsuario(), cuentaAcademicoReal.getNombreUsuario());
    }

    @Test
    void pruebaActualizarNombreUsuarioExitosa () {
        int esperado = 1;
        int obtenido = -1;

        CuentaAcademico cuentaAcademico = new CuentaAcademico();
        cuentaAcademico.setIdCuenta(1);
        cuentaAcademico.setIdAcademico("ABC123");
        cuentaAcademico.setNombreUsuario("joseLo");
        cuentaAcademico.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        try {
            obtenido = CuentaAcademicoDB.actualizarNombreUsuario(cuentaAcademico, "lopezJose");

        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: actualizarNombreUsuarioExitosa" + errorDAO.getMessage());
        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaVerificarCredencialesExitosa () {
        String nombreUsuarioPrueba = "joseLo";
        String contrasenaPrueba = "contrasena123";

        boolean resultado = false;

        try {
            resultado = CuentaAcademicoDB.verificarCredenciales(nombreUsuarioPrueba, contrasenaPrueba);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: verificarCredencialesExitosa" + errorDAO.getMessage());
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaActualizarContrasenaExitoso () {
        CuentaAcademico cuentaAcademico = new CuentaAcademico();
        cuentaAcademico.setIdCuenta(1);
        cuentaAcademico.setIdAcademico("ABC123");
        cuentaAcademico.setNombreUsuario("joseLo");
        cuentaAcademico.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        String contrasenaAntigua = "contrasena123";
        String contrasenaNueva = "pepe2000";

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = CuentaAcademicoDB.actualizarContrasena(cuentaAcademico, contrasenaAntigua, contrasenaNueva);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: actualizarContrasenaExitoso" + errorDAO.getMessage());
        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaCambiarEstadoCuentaExitoso () {
        CuentaAcademico cuentaAcademico = new CuentaAcademico();
        cuentaAcademico.setIdCuenta(1);
        cuentaAcademico.setIdAcademico("ABC123");
        cuentaAcademico.setNombreUsuario("joseLo");
        cuentaAcademico.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = CuentaAcademicoDB.cambiarEstadoCuenta(cuentaAcademico, "rechazada");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaCambiarEstadoCuentaExitoso" + errorDAO.getMessage());
        }

        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaAgregarCuentaAcademicoExitoso () {
        CuentaAcademico cuentaAcademico = new CuentaAcademico();
        cuentaAcademico.setIdCuenta(2);
        cuentaAcademico.setIdAcademico("200011");
        cuentaAcademico.setNombreUsuario("estherRa");
        cuentaAcademico.setContrasena("esther2000");
        cuentaAcademico.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        int esperado = 1;
        int obtenido = -1;

        try {
            CuentaAcademicoDB.agregaCuentaAcademico(cuentaAcademico);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaAgregarCuentaAcademicoExitoso" + errorDAO.getMessage());
        }

        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaGetPorIdExitoso () {
        CuentaAcademico cuentaAcademicoEsperado = new CuentaAcademico();
        cuentaAcademicoEsperado.setIdCuenta(1);
        cuentaAcademicoEsperado.setIdAcademico("ABC123");
        cuentaAcademicoEsperado.setNombreUsuario("joseLo");
        cuentaAcademicoEsperado.setEstado(CuentaAcademico.EstadoCuenta.valueOf("pendiente"));

        CuentaAcademico cuentaAcademicoReal = null;

        try {
            cuentaAcademicoReal = CuentaAcademicoDB.getPorId(1);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetPorIdExitoso" + errorDAO.getMessage());
        }

        assertEquals(cuentaAcademicoEsperado.getIdCuenta(), cuentaAcademicoReal.getIdCuenta());
        assertEquals(cuentaAcademicoEsperado.getIdAcademico(), cuentaAcademicoReal.getIdAcademico());
        assertEquals(cuentaAcademicoEsperado.getNombreUsuario(), cuentaAcademicoReal.getNombreUsuario());

    }



}