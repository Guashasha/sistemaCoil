package test.AccesoADatos;
import DAO.CuentaDAO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasCuentaDB;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaDTODAOTest {
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

        CuentaDTO cuentaDTOEsperada = new CuentaDTO();
        cuentaDTOEsperada.setIdCuenta(1);
        cuentaDTOEsperada.setIdPersona(4);
        cuentaDTOEsperada.setEstado(CuentaDTO.EstadoCuenta.aceptada);
        cuentaDTOEsperada.setTipo(CuentaDTO.TipoUsuario.estudiante);

        CuentaDTO cuentaDTOObtenida = null;

        try {
            cuentaDTOObtenida = CuentaDAO.getCuentaPorUsuario("EduVillegas");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorUsuarioExitosa " + errorDAO.getMessage());
        }

        assertEquals(cuentaDTOEsperada.getIdCuenta(), cuentaDTOObtenida.getIdCuenta());
        assertEquals(cuentaDTOEsperada.getIdPersona(), cuentaDTOObtenida.getIdPersona());
        assertEquals(cuentaDTOEsperada.getEstado(), cuentaDTOObtenida.getEstado());
        assertEquals(cuentaDTOEsperada.getTipo(), cuentaDTOObtenida.getTipo());
    }

    @Test
    void pruebaGetCuentaPorUsuarioFallida () {
        System.out.print("pruebaGetCuentaPorUsuarioFallida");

        CuentaDTO cuentaDTOObtenida = null;

        try {
            cuentaDTOObtenida = CuentaDAO.getCuentaPorUsuario("Maryek");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorUsuarioFallida " + errorDAO.getMessage());
        }

        assertNull(cuentaDTOObtenida);
    }
    @Test
    void pruebaActualizarNombreUsuarioExitoso () {
        System.out.println("pruebaActualizarNombreUsuarioExitoso");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        cuentaDTOPrueba.setIdCuenta(1);
        cuentaDTOPrueba.setNombreUsuario("MarLom");

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDAO.actualizarNombreUsuario(cuentaDTOPrueba);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaActualizarNombreUsuarioExitoso " + errorDAO.getMessage());
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);

    }

    @Test
    void pruebaActualizarNombreUsuarioExtensoFallido () {
        System.out.println("pruebaActualizarNombreUsuarioExtensoFallido");
        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        boolean resultado = false;
        try {
            cuentaDTOPrueba.setIdCuenta(1);
            cuentaDTOPrueba.setNombreUsuario("SSSGGHJSKKFKFKFKFKFKFKDKSKSKSKSKSKKSK477LSXLFLHL6LWKSKVLYLTLRKKDKFKRKEKXKFKFKKDKKDKDKDDKDKDKDKDKDKDK");
            CuentaDAO.actualizarNombreUsuario(cuentaDTOPrueba);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaVerificarCredencialesExitoso () {
        System.out.println("pruebaVerificarCredencialesExitoso");

        String nombreUsuario = "EduVillegas";
        String contrasena = "eduVillegas2000";

        boolean validacion = false;

        try {
            validacion = CuentaDAO.verificarCredenciales(nombreUsuario, contrasena);

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
        String contrasena = "eduVillegas2001";

        boolean validacion = false;

        try {
            validacion = CuentaDAO.verificarCredenciales(nombreUsuario, contrasena);

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
            validacion = CuentaDAO.verificarCredenciales(nombreUsuario, contrasena);

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
            validacion = CuentaDAO.verificarCredenciales(nombreUsuario, contrasena);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaVerificarCredencialesVaciosFallida " + errorDAO.getMessage());
        }

        assertFalse(validacion);

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
            filasAfectadasObtenido = CuentaDAO.actualizarContrasena(cuentaDTOPrueba, contrasenaAntigua, contrasenaNueva);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaActualizarContrasenaExitosa " + errorDAO.getMessage());
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);

    }

    @Test
    void pruebaActualizarContrasenaContrasenaAntiguaFallida () {
        System.out.println("pruebaActualizarContrasenaExitosa");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        cuentaDTOPrueba.setIdCuenta(1);
        cuentaDTOPrueba.setNombreUsuario("EduVillegas");

        String contrasenaNueva = "FomePo" ;
        String contrasenaAntigua = "eduVillegas200";

        assertThrows(ErrorDAO.class, () -> CuentaDAO.actualizarContrasena(cuentaDTOPrueba, contrasenaAntigua, contrasenaNueva));
    }

    @Test
    void pruebaCambiarEstadoCuentaExitoso () {
        System.out.println("pruebaCambiarEstadoCuentaExitoso");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        cuentaDTOPrueba.setIdCuenta(1);

        String estado = "pendiente";

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDAO.cambiarEstadoCuenta(cuentaDTOPrueba, estado);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaCambiarEstadoCuentaExitoso" + errorDAO.getMessage());

        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido);
    }

    @Test
    void pruebaCambiarEstadoCuentaNoValidoFalido () {
        System.out.println("pruebaCambiarEstadoCuentaNoValidoFalido");
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setIdCuenta(1);

        String estado = "Auxiliar";

        assertThrows(ErrorDAO.class, () -> CuentaDAO.cambiarEstadoCuenta(cuentaDTO, estado));

    }
    @Test
    void pruebCambiarEstadoCuentaIdInexistenteFallido () {
        System.out.println("pruebaCambiarEstadoCuentaNoValidoFalido");
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setIdCuenta(99);

        String estado = "pendiente";

        int filasAfectadasEsperado = 0;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = CuentaDAO.cambiarEstadoCuenta(cuentaDTO, estado);

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

        List<CuentaDTO> listaCuentaDTOS = null;

        try {
            listaCuentaDTOS = CuentaDAO.getCuentaPorTipo("estudiante");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorTipoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentaDTOS.size());


    }

    @Test
    void pruebaGetCuentaPorTipoFallido () {
        System.out.println("pruebaGetCuentaPorTipoFallido");

        List<CuentaDTO> listaCuentaDTOS = null;
        int tamanoEsperado = 0;

        try {
            listaCuentaDTOS = CuentaDAO.getCuentaPorTipo("administrador");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetCuentaPorTipoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentaDTOS.size());
    }

    @Test
    void pruebaGetPorEstadoExitoso () {
        System.out.println("pruebaGetPorEstadoExitoso");

        int tamanoEsperado = 1;

        List<CuentaDTO> listaCuentaDTOS = null;

        try {
            listaCuentaDTOS = CuentaDAO.getCuentasPorEstado("aceptada");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorEstadoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentaDTOS.size());

    }

    @Test
    void pruebaGetPorEstado () {
        System.out.println("pruebaGetPorEstadoFallido");

        int tamanoEsperado = 0;

        List<CuentaDTO> listaCuentaDTOS = null;

        try {
            listaCuentaDTOS = CuentaDAO.getCuentasPorEstado("rechazada");

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorEstadoExitoso " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentaDTOS.size());


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
            filasAfectadasObtenido = CuentaDAO.agregarCuenta(cuentaDTOPrueba);

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

        assertThrows(ErrorDAO.class, () -> CuentaDAO.agregarCuenta(cuentaDTOPrueba));
    }
    @Test
    void pruebaAgregarCuentaNombreExtensoFallida () {
        System.out.println("pruebaAgregarCuentaNombreExtensoFallida");
        CuentaDTO cuentaDTOPrueba = new CuentaDTO();
        boolean resultado = false;
        try {
            cuentaDTOPrueba.setIdPersona(5);
            cuentaDTOPrueba.setNombreUsuario("HernandoCarmenElizabethJuanitaDeCostabravaCortesIngDoctorYMaestroDramaturgoExperto");
            cuentaDTOPrueba.setContrasena("drew2000");
            cuentaDTOPrueba.setTipo(CuentaDTO.TipoUsuario.academico);
            cuentaDTOPrueba.setEstado(CuentaDTO.EstadoCuenta.pendiente);
            CuentaDAO.agregarCuenta(cuentaDTOPrueba);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado);
    }
    @Test
    void pruebaAgregarCuentaVaciaFallida () {
        System.out.println("pruebaAgregarCuentaNombreExtensoFallida");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();


        assertThrows(NullPointerException.class, () -> CuentaDAO.agregarCuenta(cuentaDTOPrueba));
    }


    @Test
    void pruebaGetPorIdExitosa () {
        System.out.println("pruebaGetPorIdExitosa");

        CuentaDTO cuentaDTOEsperada = new CuentaDTO();

        cuentaDTOEsperada.setIdCuenta(1);
        cuentaDTOEsperada.setIdPersona(4);
        cuentaDTOEsperada.setNombreUsuario("EduVillegas");
        cuentaDTOEsperada.setContrasena("eduVillegas2000");
        cuentaDTOEsperada.setTipo(CuentaDTO.TipoUsuario.estudiante);
        cuentaDTOEsperada.setEstado(CuentaDTO.EstadoCuenta.aceptada);


        CuentaDTO cuentaDTOPrueba = new CuentaDTO();

        try {
            cuentaDTOPrueba = CuentaDAO.getPorId(1);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }
        assertEquals(cuentaDTOEsperada.getIdCuenta(), cuentaDTOPrueba.getIdCuenta());
        assertEquals(cuentaDTOEsperada.getIdPersona(), cuentaDTOPrueba.getIdPersona());
        assertEquals(cuentaDTOEsperada.getNombreUsuario(), cuentaDTOPrueba.getNombreUsuario());
        assertEquals(cuentaDTOEsperada.getTipo(), cuentaDTOPrueba.getTipo());
        assertEquals(cuentaDTOEsperada.getEstado(), cuentaDTOPrueba.getEstado());

    }

    @Test
    void pruebaGetPorIdFallida () {
        System.out.println("pruebaGetPorIdFallida");

        CuentaDTO cuentaDTOPrueba = new CuentaDTO();

        try {
            cuentaDTOPrueba = CuentaDAO.getPorId(20);

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }

        assertNull(cuentaDTOPrueba);
    }

    @Test
    void pruebaGetTodosExitoso () {
        System.out.println("pruebaGetPorIdExitosa");

        int tamanoEsperado = 1;

        List<CuentaDTO> listaCuentaDTOS = null;

        try {
            listaCuentaDTOS = CuentaDAO.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaCuentaDTOS.size());

    }

    @Test
    void pruebaGetTodosFallida () {
        System.out.println("pruebaGetPorIdFallida");

        int tamanoEsperado = 0;

        List<CuentaDTO> listaCuentaDTOS = null;

        try {
            listaCuentaDTOS = CuentaDAO.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPorIdExitosa " + errorDAO.getMessage());

        }
        assertNotEquals(tamanoEsperado, listaCuentaDTOS.size());
    }
}