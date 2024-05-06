package test.AccesoADatos;

import AccesoADatos.ActividadDB;
import Logica.Dominio.Actividad;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;

public class ActividadDBTest {
    private final Actividad ACTIVIDAD1 = new Actividad(1,"kahoot prueba", "descripcion de la actividad prueba", Actividad.TipoActividad.cierre);
    private final Actividad ACTIVIDAD2 = new Actividad(2,"Presentacion","presentacion individual ante grupo", Actividad.TipoActividad.rompeHielo);
    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.borrarDatosTablaActividad();
        ejecutarInstruccionSQL("INSERT INTO actividad (titulo, descripcion, tipo) values ('kahoot prueba','descripcion de la actividad prueba','cierre');");
        ejecutarInstruccionSQL("INSERT INTO actividad (titulo, descripcion, tipo) values ('Presentacion','presentacion individual ante grupo','rompeHielo');");
    }

    @AfterAll
    static void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaActividad();
    }

    @Test
    void pruebaAgregarActividadExitosa () {
        try {
            int resultado = ActividadDB.agregarActividad(ACTIVIDAD1);
            assertEquals(1,resultado,"pruebaAgregarActividadExitosa");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaAgregarActividadExitosa");
        }
    }

    @Test
    void pruebaAgregarActividadVacia () {
        assertThrows(NullPointerException.class,() ->ActividadDB.agregarActividad(new Actividad()));
    }

    @Test
    public void pruebaGetPorIdExitosa () {
        System.out.println("pruebaGetPorIdExitosa");
        ResultSet resultado = null;
        try {
            resultado = ActividadDB.getPorId(1);
        } catch (SQLException e) {
            fail("Fallida: pruebaGetPorIdExitosa");
        }

        try {
            assertTrue(resultado.next());
            assertEquals(ACTIVIDAD1.getIdActividad(), resultado.getInt("idActividad"));
            assertEquals(ACTIVIDAD1.getTitulo(), resultado.getString("titulo"));
            assertEquals(ACTIVIDAD1.getDescripcion(), resultado.getString("descripcion"));
            assertEquals(ACTIVIDAD1.getTipo().toString(), resultado.getString("tipo"));
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPorIdExitosa" + error.getMessage());
        }
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        try {
            ResultSet resultado = ActividadDB.getPorId(10);
            assertFalse(resultado.next());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPorIdInexistente");
        }
    }

    @Test
    public void pruebaGetPorTituloExitosa () {
        ResultSet resultado = null;
        try {
            resultado = ActividadDB.getPorTitulo(ACTIVIDAD1.getTitulo());
        } catch (SQLException e) {
            fail("Fallida: pruebaGetPorTituloExitosa");
        }

        try {
            assertTrue(resultado.next());
            assertEquals(ACTIVIDAD1.getIdActividad(), resultado.getInt("idActividad"));
            assertEquals(ACTIVIDAD1.getTitulo(), resultado.getString("titulo"));
            assertEquals(ACTIVIDAD1.getDescripcion(), resultado.getString("descripcion"));
            assertEquals(ACTIVIDAD1.getTipo().toString(), resultado.getString("tipo"));
        } catch (SQLException error) {
            fail("Fallida: pruebaGetPorTituloExitosa");
        }
    }

    @Test
    public void pruebaGetPorTituloInexistente () {
        try {
            ResultSet resultado = ActividadDB.getPorTitulo("Actividad cuatro");
            assertFalse(resultado.next(),"pruebaGetPorTituloInexistente");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPorTituloInexistente");
        }
    }

    @Test
    public void pruebaGetPorTituloNulo () {
        try {
            ResultSet resultado = ActividadDB.getPorTitulo(null);
            assertFalse(resultado.next(),"pruebaGetPorTituloNulo");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPorTituloNulo");
        }
    }
    
    @Test
    void pruebaGetPorIdColaboracionExitosa () {
        System.out.println("pruebaGetPorIdColaboracionExitosa");
        ResultSet resultado = null;
        List<Actividad> esperado = new ArrayList<>();
        esperado.add(ACTIVIDAD1);
        esperado.add(ACTIVIDAD2);

        try {
            resultado = ActividadDB.getPorIdColaboracion(1);
        } catch (SQLException e) {
            fail("Fallida: pruebaGetPorIdColaboracionExitosa");
        }

        try {
            for (Actividad act : esperado) {
                assertTrue(resultado.next());
                assertEquals(act.getIdActividad(), resultado.getInt("idActividad"));
                assertEquals(act.getTitulo(), resultado.getString("titulo"));
                assertEquals(act.getDescripcion(), resultado.getString("descripcion"));
                assertEquals(act.getTipo().toString()
                        .toLowerCase(), resultado.getString("tipo"));
            }
        } catch (SQLException error) {
            fail("Fallida: pruebaGetPorIdColaboracionExitosa");
        }
    }

    @Test
    void pruebaGetPorIdColaboracionInexistente () {
        try {
            ResultSet resultado = ActividadDB.getPorIdColaboracion(10);
            assertFalse(resultado.next());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPorIdColaboracionInexistente");
        }
    }

    @Test
    void pruebaGetTodosExitosa () {
        System.out.println("pruebaGetTodosExitosa");
        ResultSet resultado = null;
        List<Actividad> esperado = new ArrayList<>();
        esperado.add(ACTIVIDAD1);
        esperado.add(ACTIVIDAD2);
        try {
            resultado = ActividadDB.getTodos();
        } catch (SQLException e) {
            fail("Fallida: pruebaGetTodosExitosa");
        }

        try {
            for (Actividad act : esperado) {
                assertTrue(resultado.next());
                assertEquals(act.getIdActividad(), resultado.getInt("idActividad"));
                assertEquals(act.getTitulo(), resultado.getString("titulo"));
                assertEquals(act.getDescripcion(), resultado.getString("descripcion"));
                assertEquals(act.getTipo().toString()
                        .toLowerCase(), resultado.getString("tipo"));
            }
        } catch (SQLException error) {
            fail("Fallida: pruebaGetTodosExitosa");
        }
    }
}
