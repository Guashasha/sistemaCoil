package test.AccesoADatos;

import AccesoADatos.AcademicoDB;
import AccesoADatos.CuentaDB;
import Logica.Dominio.Cuenta;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasCuentaDB;
import test.ConfiguracionPrueba;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaDBTest {
    // toDo getTodos
    @BeforeEach
    void setUp () {
        AyudantePruebasCuentaDB.agregarPrecondiciones();
    }

    @AfterEach
    void tearDown () {
       AyudantePruebasCuentaDB.borrarTodosDatosTabla();
    }

    @Test
    void pruebaGetCuentaPorUsuarioExitosa () {
        System.out.print("pruebaGetCuentaPorUsuarioExitosa");

        Cuenta cuentaEsperada = new Cuenta();
        cuentaEsperada.setIdCuenta(1);
        cuentaEsperada.setIdPersona(4);
        cuentaEsperada.setEstado(Cuenta.EstadoCuenta.aceptada);
        cuentaEsperada.setTipo(Cuenta.TipoUsuario.estudiante);

        Cuenta cuentaObtenida = null;

        try {
            cuentaObtenida = CuentaDB.getCuentaPorUsuario("EduVillegas");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorUsuarioExitosa " + errorDAO.getMessage());
        }

        assertEquals(cuentaEsperada.getIdCuenta(), cuentaObtenida.getIdCuenta());
        assertEquals(cuentaEsperada.getIdPersona(), cuentaObtenida.getIdPersona());
        assertEquals(cuentaEsperada.getEstado(), cuentaObtenida.getEstado());
        assertEquals(cuentaEsperada.getTipo(), cuentaObtenida.getTipo());
    }

    @Test
    void pruebaActualizarNombreUsuarioExitoso () {
        System.out.println("pruebaActualizarNombreUsuarioExitoso");

        Cuenta cuentaPrueba = new Cuenta();
        cuentaPrueba.setIdCuenta(1);
        cuentaPrueba.setNombreUsuario("MarLom");

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDB.actualizarNombreUsuario(cuentaPrueba);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaActualizarNombreUsuarioExitoso " + errorDAO.getMensaje());
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);

    }

    @Test
    void pruebaVerificarCredencialesExitoso () {
        System.out.println("pruebaVerificarCredencialesExitoso");

        String nombreUsuario = "EduVillegas";
        String contrasena = "eduVillegas2000";

        boolean validacion = false;

        try {
            validacion = CuentaDB.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesExitoso " + errorDAO.getMessage());
        }

        assertTrue(validacion);

    }

    @Test
    void pruebaVerificarCredencialesUsuarioDistintoFallida () {
        System.out.println("pruebaVerificarCredencialesUsuarioDistintoFallida");

        String nombreUsuario = "EduVIllegas";
        String contrasena = "eduVillegas2000";

        boolean validacion = false;

        try {
            validacion = CuentaDB.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesUsuarioDistintoFallida " + errorDAO.getMessage());
        }

        assertFalse(validacion);
    }

    @Test
    void pruebaVerificarCredencialesContrasenaDistintaFallida () {
        System.out.println("pruebaVerificarCredencialesContrasenaDistintaFallida");

        String nombreUsuario = "EduVillegas";
        String contrasena = "eduVillega2000";

        boolean validacion = false;

        try {
            validacion = CuentaDB.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesContrasenaDistintaFallida " + errorDAO.getMessage());
        }

        assertFalse(validacion);

    }

    @Test
    void pruebaVerificarCredencialesVaciosFallida () {
        System.out.println("pruebaVerificarCredencialesVaciosFallida");

        String nombreUsuario = "";
        String contrasena = "";

        boolean validacion = false;

        try {
            validacion = CuentaDB.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesVaciosFallida " + errorDAO.getMessage());
        }

        assertFalse(validacion);

    }

    @Test
    void pruebaActualizarContrasenaExitosa () {
        System.out.println("pruebaActualizarContrasenaExitosa");

        Cuenta cuentaPrueba = new Cuenta();
        cuentaPrueba.setIdCuenta(1);
        cuentaPrueba.setNombreUsuario("EduVillegas");

        String contrasenaNueva = "FomePo" ;
        String contrasenaAntigua = "eduVillegas2000";

        int filasAfectadasEsperado = 2;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDB.actualizarContrasena(cuentaPrueba, contrasenaAntigua, contrasenaNueva);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaActualizarContrasenaExitosa " + errorDAO.getMessage());
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);

    }

    @Test
    void pruebaActualizarContrasenaContrasenaAntiguaFallida () {
        System.out.println("pruebaActualizarContrasenaExitosa");

        Cuenta cuentaPrueba = new Cuenta();
        cuentaPrueba.setIdCuenta(1);
        cuentaPrueba.setNombreUsuario("EduVillegas");

        String contrasenaNueva = "FomePo" ;
        String contrasenaAntigua = "eduVillegas200";

        assertThrows(ErrorDAO.class, () -> CuentaDB.actualizarContrasena(cuentaPrueba, contrasenaAntigua, contrasenaNueva));

    }

    @Test
    void pruebaCambiarEstadoCuentaExitoso () {
        System.out.println("pruebaCambiarEstadoCuentaExitoso");

        Cuenta cuentaPrueba = new Cuenta();
        cuentaPrueba.setIdCuenta(1);

        String estado = "pendiente";

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDB.cambiarEstadoCuenta(cuentaPrueba, estado);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaCambiarEstadoCuentaExitoso" + errorDAO.getMessage());

        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);
    }

    @Test
    void pruebaGetCuentaPorTipoExitoso () {
        System.out.println("pruebaGetCuentaPorTipoExitoso");

        int tamanoEsperado = 1;

        List<Cuenta> listaCuentas = null;

        try {
            listaCuentas = CuentaDB.getCuentaPorTipo("estudiante");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorTipoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentas.size());


    }

    @Test
    void pruebaGetPorEstadoExitoso () {
        System.out.println("pruebaGetPorEstadoExitoso");

        int tamanoEsperado = 1;

        List<Cuenta> listaCuentas = null;

        try {
            listaCuentas = CuentaDB.getCuentasPorEstado("aceptada");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorEstadoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentas.size());

    }


    @Test
    void pruebaAgregarCuentaExitoso () {
        System.out.println("pruebaAgregarCuentaExitoso");

        Cuenta cuentaPrueba = new Cuenta();


        cuentaPrueba.setIdPersona(1);
        cuentaPrueba.setNombreUsuario("JoseLo");
        cuentaPrueba.setContrasena("joseLoInge");
        cuentaPrueba.setTipo(Cuenta.TipoUsuario.academico);
        cuentaPrueba.setEstado(Cuenta.EstadoCuenta.pendiente);

        int filasAfectadaEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDB.agregarCuenta(cuentaPrueba);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaAgregarCuentaExitoso " + errorDAO.getMessage());

        }

        assertEquals(filasAfectadaEsperado, filasAfectadasObtenido);

    }

    @Test
    void pruebaGetPorIdExitosa () {
        System.out.println("pruebaGetPorIdExitosa");

        Cuenta cuentaEsperada = new Cuenta();

        cuentaEsperada.setIdCuenta(1);
        cuentaEsperada.setIdPersona(4);
        cuentaEsperada.setNombreUsuario("EduVillegas");
        cuentaEsperada.setContrasena("eduVillegas2000");
        cuentaEsperada.setTipo(Cuenta.TipoUsuario.estudiante);
        cuentaEsperada.setEstado(Cuenta.EstadoCuenta.aceptada);


        Cuenta cuentaPrueba = new Cuenta();

        try {
            cuentaPrueba = CuentaDB.getPorId(1);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }
        assertEquals(cuentaEsperada.getIdCuenta(), cuentaPrueba.getIdCuenta());
        assertEquals(cuentaEsperada.getIdPersona(), cuentaPrueba.getIdPersona());
        assertEquals(cuentaEsperada.getNombreUsuario(), cuentaPrueba.getNombreUsuario());
        assertEquals(cuentaEsperada.getTipo(), cuentaPrueba.getTipo());
        assertEquals(cuentaEsperada.getEstado(), cuentaPrueba.getEstado());

    }

    @Test
    void pruebaGetTodosExitoso () {
        System.out.println("pruebaGetPorIdExitosa");

        int tamanoEsperado = 1;

        List<Cuenta> listaCuentas = null;

        try {
            listaCuentas = CuentaDB.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentas.size());

    }


}