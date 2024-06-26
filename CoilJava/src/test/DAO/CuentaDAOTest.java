package test.DAO;
import DAO.CuentaDAO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasCuentaDB;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CuentaDAOTest {
    private final CuentaDAO CUENTA_DAO = new CuentaDAO();
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

        CuentaDTO cuentaEsperada = new CuentaDTO();
        cuentaEsperada.setIdCuenta(1);
        cuentaEsperada.setIdPersona(4);
        cuentaEsperada.setEstado(CuentaDTO.EstadoCuenta.aceptada);
        cuentaEsperada.setTipo(CuentaDTO.TipoUsuario.estudiante);

        try {
            Optional<CuentaDTO> cuentaObtenida = CUENTA_DAO.getCuentaPorUsuario("EduVillegas");
            assertEquals(cuentaEsperada.getIdCuenta(), cuentaObtenida.get().getIdCuenta());
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorUsuarioExitosa " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetCuentaPorUsuarioFallida () {
        System.out.print("pruebaGetCuentaPorUsuarioFallida");


        Optional<CuentaDTO> cuentaobtenida;
        try {
            cuentaobtenida = CUENTA_DAO.getCuentaPorUsuario("Maryek");
            assertFalse(cuentaobtenida.isPresent());

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorUsuarioFallida " + errorDAO.getMessage());
        }

    }
    @Test
    void pruebaActualizarNombreUsuarioExitoso () {
        System.out.println("pruebaActualizarNombreUsuarioExitoso");

        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setIdCuenta(1);
        cuenta.setNombreUsuario("MarLom");

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = 0;

        try {
            filasAfectadasObtenido = CUENTA_DAO.actualizarNombreUsuario(cuenta);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaActualizarNombreUsuarioExitoso " + errorDAO.getMessage());
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);

    }


    @Test
    void pruebaVerificarCredencialesExitoso () {
        System.out.println("pruebaVerificarCredencialesExitoso");

        String nombreUsuario = "EduVillegas";
        String contrasena = "eduVillegas2000";

        boolean credencialesValidas = false;

        try {
            credencialesValidas = CUENTA_DAO.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesExitoso " + errorDAO.getMessage());
        }

        assertTrue(credencialesValidas);

    }

    @Test
    void pruebaVerificarCredencialesUsuarioDistintoFallida () {
        System.out.println("pruebaVerificarCredencialesUsuarioDistintoFallida");

        String nombreUsuario = "EduVIllegas";
        String contrasena = "eduVillegas2001";

        boolean credencialesValidas = false;

        try {
            credencialesValidas = CUENTA_DAO.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesUsuarioDistintoFallida " + errorDAO.getMessage());
        }

        assertFalse(credencialesValidas);
    }

    @Test
    void pruebaVerificarCredencialesContrasenaDistintaFallida () {

        String nombreUsuario = "EduVillegas";
        String contrasena = "eduVillega2000";

        boolean credencialesValidas = false;

        try {
            credencialesValidas = CUENTA_DAO.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesContrasenaDistintaFallida " + errorDAO.getMessage());
        }

        assertFalse(credencialesValidas);

    }

    @Test
    void pruebaVerificarCredencialesVaciosFallida () {
        System.out.println("pruebaVerificarCredencialesVaciosFallida");

        String nombreUsuario = "";
        String contrasena = "";

        boolean credencialesValidas = false;

        try {
            credencialesValidas = CUENTA_DAO.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesVaciosFallida " + errorDAO.getMessage());
        }

        assertFalse(credencialesValidas);

    }

    @Test
    void pruebaActualizarContrasenaExitosa () {
        System.out.println("pruebaActualizarContrasenaExitosa");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        cuentaDTOPrueba.setIdCuenta(1);
        cuentaDTOPrueba.setNombreUsuario("EduVillegas");

        String contrasenaNueva = "FomePo" ;
        String contrasenaAntigua = "eduVillegas2000";

        int filasAfectadasEsperado = 2;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CUENTA_DAO.actualizarContrasena(cuentaDTOPrueba, contrasenaAntigua, contrasenaNueva);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaActualizarContrasenaExitosa " + errorDAO.getMessage());
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);

    }

    @Test
    void pruebaActualizarContrasenaContrasenaAntiguaFallida () {

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        cuentaDTOPrueba.setIdCuenta(1);
        cuentaDTOPrueba.setNombreUsuario("EduVillegas");

        String contrasenaNueva = "FomePo" ;
        String contrasenaAntigua = "eduVillegas200";

        assertThrows(ErrorDAO.class, () -> CUENTA_DAO.actualizarContrasena(cuentaDTOPrueba, contrasenaAntigua, contrasenaNueva), "pruebaActualizarContrasenaExitosa");
    }

    @Test
    void pruebaCambiarEstadoCuentaExitoso () {

        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setIdCuenta(1);

        String nuevoEstado = "pendiente";

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = 0;

        try {
            filasAfectadasObtenido = CUENTA_DAO.cambiarEstadoCuenta(cuenta, nuevoEstado);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaCambiarEstadoCuentaExitoso" + errorDAO.getMessage());

        }
        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido, "pruebaCambiarEstadoCuentaExitoso");
    }

    @Test
    void pruebaCambiarEstadoCuentaEstadoNoValidoFalido () {
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setIdCuenta(1);

        String nuevoEstado = "Auxiliar";

        assertThrows(ErrorDAO.class, () -> CUENTA_DAO.cambiarEstadoCuenta(cuenta, nuevoEstado), "pruebaCambiarEstadoCuentaNoValidoFalido");

    }
    @Test
    void pruebCambiarEstadoCuentaIdInexistenteFallido () {
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setIdCuenta(99);

        String nuevoEstado = "pendiente";

        int filasAfectadasEsperado = 0;
        int filasAfectadasObtenido = 0;

        try {
            filasAfectadasObtenido = CUENTA_DAO.cambiarEstadoCuenta(cuenta, nuevoEstado);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaCambiarEstadoCuentaExitoso" + errorDAO.getMessage());

        }
        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido, "pruebaCambiarEstadoCuentaNoValidoFalido");
    }


    @Test
    void pruebaGetCuentaPorTipoExitoso () {
        System.out.println("pruebaGetCuentaPorTipoExitoso");

        int tamanoListaEsperado = 1;

        List<CuentaDTO> listaObtenida = null;

        try {
            listaObtenida = CUENTA_DAO.getCuentasPorTipo("estudiante");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorTipoExitoso " + errorDAO.getMessage());

        }
        assertEquals(tamanoListaEsperado, listaObtenida.size(), "pruebaGetCuentaPorTipoExitoso");
    }

    @Test
    void pruebaGetCuentaPorTipoFallido () {

        List<CuentaDTO> listaCuentasObtenidas = null;
        int tamanoEsperado = 0;

        try {
            listaCuentasObtenidas = CUENTA_DAO.getCuentasPorTipo("administrador");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorTipoExitoso " + errorDAO.getMessage());

        }
        assertEquals(tamanoEsperado, listaCuentasObtenidas.size(), "pruebaGetCuentaPorTipoFallido");
    }

    @Test
    void pruebaGetPorEstadoExitoso () {
        int tamanoListaCuentaEsperado = 1;

        List<CuentaDTO> listaCuentaObtenido = null;

        try {
            listaCuentaObtenido = CUENTA_DAO.getCuentasPorEstado("aceptada");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorEstadoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoListaCuentaEsperado, listaCuentaObtenido.size(), "pruebaGetPorEstadoExitoso");

    }

    @Test
    void pruebaGetPorEstadoFallido () {
        System.out.println("pruebaGetPorEstadoFallido");

        int tamanoEsperado = 0;

        List<CuentaDTO> listaCuentaObtenido = null;

        try {
            listaCuentaObtenido = CUENTA_DAO.getCuentasPorEstado("rechazada");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorEstadoExitoso " + errorDAO.getMessage());
        }
        assertEquals(tamanoEsperado, listaCuentaObtenido.size(), "pruebaGetPorEstadoFallido");
    }


    @Test
    void pruebaAgregarCuentaExitoso () {
        System.out.println("pruebaAgregarCuentaExitoso");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();

        cuentaDTOPrueba.setIdPersona(1);
        cuentaDTOPrueba.setNombreUsuario("JoseLo");
        cuentaDTOPrueba.setContrasena("joseLoInge");
        cuentaDTOPrueba.setTipo(CuentaDTO.TipoUsuario.academico);
        cuentaDTOPrueba.setEstado(CuentaDTO.EstadoCuenta.pendiente);

        int filasAfectadaEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CUENTA_DAO.agregar(cuentaDTOPrueba);
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaAgregarCuentaExitoso " + errorDAO.getMessage());

        }
        assertEquals(filasAfectadaEsperado, filasAfectadasObtenido);
    }

    @Test
    void pruebaAgregarCuentaNombreDuplicadoFallida () {
        System.out.println("pruebaAgregarCuentaNombreDuplicadoFallida");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();


        cuentaDTOPrueba.setIdPersona(5);
        cuentaDTOPrueba.setNombreUsuario("EduVillegas");
        cuentaDTOPrueba.setContrasena("drew2000");
        cuentaDTOPrueba.setTipo(CuentaDTO.TipoUsuario.academico);
        cuentaDTOPrueba.setEstado(CuentaDTO.EstadoCuenta.pendiente);

        assertThrows(ErrorDAO.class, () -> CUENTA_DAO.agregar(cuentaDTOPrueba));
    }

    @Test
    void pruebaAgregarCuentaVaciaFallida () {
        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        assertThrows(NullPointerException.class, () -> CUENTA_DAO.agregar(cuentaDTOPrueba), "pruebaAgregarCuentaNombreExtensoFallida");
    }


    @Test
    void pruebaGetPorIdExitosa () {
        System.out.println("pruebaGetPorIdExitosa");
        CuentaDTO cuentaEsperada = new CuentaDTO();
        cuentaEsperada.setIdCuenta(1);
        cuentaEsperada.setIdPersona(4);
        cuentaEsperada.setNombreUsuario("EduVillegas");
        cuentaEsperada.setContrasena("eduVillegas2000");
        cuentaEsperada.setTipo(CuentaDTO.TipoUsuario.estudiante);
        cuentaEsperada.setEstado(CuentaDTO.EstadoCuenta.aceptada);

        try {
            Optional<CuentaDTO> cuentaObtenida = CUENTA_DAO.getPorId(1);
            assertEquals(cuentaEsperada.getIdCuenta(), cuentaObtenida.get().getIdCuenta(), "pruebaGetPorIdExitosa");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }
    }

    @Test
    void pruebaGetPorIdFallida () {
        try {
            Optional<CuentaDTO> cuentaoObtenida = CUENTA_DAO.getPorId(20);
            assertFalse(cuentaoObtenida.isPresent(), "pruebaGetPorIdFallida");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetTodosExitoso () {
        System.out.println("pruebaGetPorIdExitosa");
        int tamanoEsperado = 1;
        List<CuentaDTO> listaCuentasObtenida = null;

        try {
            listaCuentasObtenida = CUENTA_DAO.getTodos();
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }
        assertEquals(tamanoEsperado, listaCuentasObtenida.size(), "pruebaGetPorIdExitosa");
    }

    @Test
    void pruebaGetModificarNoImplementada () {
        CuentaDTO cuentaDTO = new CuentaDTO();
        assertThrows(ExecutionControl.NotImplementedException.class, () -> CUENTA_DAO.modificar(cuentaDTO));
    }

    @Test
    void pruebaActualizarContrasenaFallidaPorContrasenaAntiguaIncorrecta() {
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setIdCuenta(1);
        cuenta.setNombreUsuario("EduVillegas");

        String contrasenaNueva = "NuevaContrasena123";
        String contrasenaAntigua = "ContrasenaIncorrecta";

        assertThrows(ErrorDAO.class, () -> CUENTA_DAO.actualizarContrasena(cuenta, contrasenaAntigua, contrasenaNueva), "pruebaActualizarContrasenaFallidaPorContrasenaAntiguaIncorrecta");
    }

    @Test
    void pruebaGetCuentaPorEstadoConEstadoInvalido() {
        List<CuentaDTO> listaCuentaObtenida = null;
        int tamanoEsperado = 0;

        try {
            listaCuentaObtenida = CUENTA_DAO.getCuentasPorEstado("estadoInvalido");
        } catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorEstadoConEstadoInvalido " + errorDAO.getMessage());
        }
        assertEquals(tamanoEsperado, listaCuentaObtenida.size(), "pruebaGetCuentaPorEstadoConEstadoInvalido");
    }

    @Test
    void pruebaGetCuentaPorTipoConTipoInvalido() {
        List<CuentaDTO> listaCuentaObtenido = null;
        int tamanoEsperado = 0;

        try {
            listaCuentaObtenido = CUENTA_DAO.getCuentasPorTipo("tipoInvalido");
        } catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorTipoConTipoInvalido " + errorDAO.getMessage());
        }

        assertEquals(tamanoEsperado, listaCuentaObtenido.size(), "pruebaGetCuentaPorTipoConTipoInvalido");
    }
}