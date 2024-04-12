package test.AccesoADatos;

import AccesoADatos.ActividadDB;
import Logica.DAO.DAOActividad;
import Logica.Dominio.Actividad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ActividadDBTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaActividad();

        DAOActividad act = new DAOActividad();
        Actividad actividad = new Actividad("titulo", "descripcion", Actividad.TipoActividad.rompeHielo);

        act.agregar(actividad);
    }

    private static Actividad crearActividad () {
        Actividad actividad = new Actividad("kahoot prueba", "descripcion de la actividad prueba", Actividad.TipoActividad.cierre);

        return actividad;
    }

    @Test
    public void testAgregar () {
        int resultado = -1;

        try {
            resultado = ActividadDB.agregarActividad(crearActividad());
        }
        catch (SQLException error) {
            fail();
        }

        assertEquals(1, resultado);
    }

    @Test
    public void testGetPorId () {
        ResultSet resultado = null;

        try {
            resultado = ActividadDB.getPorId(1);
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(resultado.next());
            assertEquals(1, resultado.getInt(1));
            assertEquals("titulo", resultado.getString(2));
            assertEquals("descripcion", resultado.getString(3));
        }
        catch (SQLException error) {
            fail();
        }
    }

    @Test
    public void testGetPorTitulo () {
        ResultSet resultado = null;

        try {
            resultado = ActividadDB.getPorTitulo("titulo");
        } catch (SQLException e) {
            fail();
        }

        try {
            assert (resultado.next());
            assertEquals(1, resultado.getInt(1));
            assertEquals("titulo", resultado.getString(2));
            assertEquals("descripcion", resultado.getString(3));
        } catch (SQLException error) {
            fail();
        }
    }

    @Test
    public void testModificar () {
        int resultado = -1;

        Actividad actividad = new Actividad("titulo", "adios", Actividad.TipoActividad.rompeHielo);

        try {
            ActividadDB.modificarActividad(actividad);
        } catch (SQLException e) {
            fail();
        }

        assertEquals(1, resultado);
    }
}
