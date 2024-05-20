//package test.AccesoADatos;
//
//import DAO.ActividadDAO;
//import DTO.ActividadDTO;
//import org.junit.jupiter.api.*;
//import test.AyudantePruebasColaboracionDB;
//import test.ConfiguracionPrueba;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ActividadDTODAOTest {
//    private final ActividadDTO ACTIVIDADDTO1 = new ActividadDTO(1,"kahoot prueba", "descripcion de la actividad prueba", ActividadDTO.TipoActividad.cierre);
//    private final ActividadDTO ACTIVIDADDTO2 = new ActividadDTO(2,"Presentacion","presentacion individual ante grupo", ActividadDTO.TipoActividad.rompeHielo);
//    @BeforeEach
//    void setUp () {
//        ConfiguracionPrueba.borrarDatosTablaActividad();
//        AyudantePruebasColaboracionDB.agregarActividades();
//    }
//
//    @AfterAll
//    static void tearDown () {
//        ConfiguracionPrueba.borrarDatosTablaActividad();
//    }
//
//    @Test
//    void pruebaAgregarActividadExitosa () {
//        try {
//            int resultado = ActividadDAO.agregarActividad(ACTIVIDADDTO1);
//            assertEquals(1,resultado,"pruebaAgregarActividadExitosa");
//        }
//        catch (SQLException error) {
//            fail("Fallida: pruebaAgregarActividadExitosa");
//        }
//    }
//
//    @Test
//    void pruebaAgregarActividadVacia () {
//        assertThrows(NullPointerException.class,() -> ActividadDAO.agregarActividad(new ActividadDTO()));
//    }
//
//    @Test
//    public void pruebaGetPorIdExitosa () {
//        System.out.println("pruebaGetPorIdExitosa");
//        ResultSet resultado = null;
//        try {
//            resultado = ActividadDAO.getPorId(1);
//        } catch (SQLException e) {
//            fail("Fallida: pruebaGetPorIdExitosa");
//        }
//
//        try {
//            assertTrue(resultado.next());
//            assertEquals(ACTIVIDADDTO1.getIdActividad(), resultado.getInt("idActividad"));
//            assertEquals(ACTIVIDADDTO1.getTitulo(), resultado.getString("titulo"));
//            assertEquals(ACTIVIDADDTO1.getDescripcion(), resultado.getString("descripcion"));
//            assertEquals(ACTIVIDADDTO1.getTipo().toString(), resultado.getString("tipo"));
//        }
//        catch (SQLException error) {
//            fail("Fallida: pruebaGetPorIdExitosa" + error.getMessage());
//        }
//    }
//
//    @Test
//    public void pruebaGetPorIdInexistente () {
//        try {
//            ResultSet resultado = ActividadDAO.getPorId(10);
//            assertFalse(resultado.next());
//        }
//        catch (SQLException error) {
//            fail("Fallida: pruebaGetPorIdInexistente");
//        }
//    }
//
//    @Test
//    public void pruebaGetPorTituloExitosa () {
//        ResultSet resultado = null;
//        try {
//            resultado = ActividadDAO.getPorTitulo(ACTIVIDADDTO1.getTitulo());
//        } catch (SQLException e) {
//            fail("Fallida: pruebaGetPorTituloExitosa");
//        }
//
//        try {
//            assertTrue(resultado.next());
//            assertEquals(ACTIVIDADDTO1.getIdActividad(), resultado.getInt("idActividad"));
//            assertEquals(ACTIVIDADDTO1.getTitulo(), resultado.getString("titulo"));
//            assertEquals(ACTIVIDADDTO1.getDescripcion(), resultado.getString("descripcion"));
//            assertEquals(ACTIVIDADDTO1.getTipo().toString(), resultado.getString("tipo"));
//        } catch (SQLException error) {
//            fail("Fallida: pruebaGetPorTituloExitosa");
//        }
//    }
//
//    @Test
//    public void pruebaGetPorTituloInexistente () {
//        try {
//            ResultSet resultado = ActividadDAO.getPorTitulo("ActividadDTO cuatro");
//            assertFalse(resultado.next(),"pruebaGetPorTituloInexistente");
//        }
//        catch (SQLException error) {
//            fail("Fallida: pruebaGetPorTituloInexistente");
//        }
//    }
//
//    @Test
//    public void pruebaGetPorTituloNulo () {
//        try {
//            ResultSet resultado = ActividadDAO.getPorTitulo(null);
//            assertFalse(resultado.next(),"pruebaGetPorTituloNulo");
//        }
//        catch (SQLException error) {
//            fail("Fallida: pruebaGetPorTituloNulo");
//        }
//    }
//
//    @Test
//    void pruebaGetPorIdColaboracionExitosa () {
//        System.out.println("pruebaGetPorIdColaboracionExitosa");
//        ResultSet resultado = null;
//        List<ActividadDTO> esperado = new ArrayList<>();
//        esperado.add(ACTIVIDADDTO1);
//        esperado.add(ACTIVIDADDTO2);
//
//        try {
//            AyudantePruebasColaboracionDB.vincularActividadConColaboracion();
//            resultado = ActividadDAO.getPorIdColaboracion(1);
//        } catch (SQLException e) {
//            fail("Fallida: pruebaGetPorIdColaboracionExitosa");
//        }
//
//        try {
//            for (ActividadDTO act : esperado) {
//                assertTrue(resultado.next());
//                assertEquals(act.getIdActividad(), resultado.getInt("idActividad"));
//                assertEquals(act.getTitulo(), resultado.getString("titulo"));
//                assertEquals(act.getDescripcion(), resultado.getString("descripcion"));
//                assertEquals(act.getTipo().toString()
//                        .toLowerCase(), resultado.getString("tipo"));
//            }
//            AyudantePruebasColaboracionDB.borrarTablasActividadTest();
//        } catch (SQLException error) {
//            fail("Fallida: pruebaGetPorIdColaboracionExitosa");
//        }
//    }
//
//    @Test
//    void pruebaGetPorIdColaboracionInexistente () {
//        try {
//            ResultSet resultado = ActividadDAO.getPorIdColaboracion(10);
//            assertFalse(resultado.next());
//        }
//        catch (SQLException error) {
//            fail("Fallida: pruebaGetPorIdColaboracionInexistente");
//        }
//    }
//
//    @Test
//    void pruebaGetTodosExitosa () {
//        System.out.println("pruebaGetTodosExitosa");
//        ResultSet resultado = null;
//        List<ActividadDTO> esperado = new ArrayList<>();
//        esperado.add(ACTIVIDADDTO1);
//        esperado.add(ACTIVIDADDTO2);
//        try {
//            resultado = ActividadDAO.getTodos();
//        } catch (SQLException e) {
//            fail("Fallida: pruebaGetTodosExitosa");
//        }
//
//        try {
//            for (ActividadDTO act : esperado) {
//                assertTrue(resultado.next());
//                assertEquals(act.getIdActividad(), resultado.getInt("idActividad"));
//                assertEquals(act.getTitulo(), resultado.getString("titulo"));
//                assertEquals(act.getDescripcion(), resultado.getString("descripcion"));
//                assertEquals(act.getTipo().toString()
//                        .toLowerCase(), resultado.getString("tipo"));
//            }
//        } catch (SQLException error) {
//            fail("Fallida: pruebaGetTodosExitosa");
//        }
//    }
//}
