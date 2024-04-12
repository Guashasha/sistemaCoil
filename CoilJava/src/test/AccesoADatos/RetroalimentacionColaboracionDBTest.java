package test.AccesoADatos;

import AccesoADatos.RetroalimentacionColaboracionDB;
import Logica.DAO.DAORetroalimentacionColaboracion;
import Logica.Dominio.RetroalimentacionColaboracion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionColaboracionDBTest {
    @BeforeAll
    public static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        AyudantePruebasColaboracionDB.agregarPrecondiciones();

        DAORetroalimentacionColaboracion rt = new DAORetroalimentacionColaboracion();
        rt.agregar(crearRetroalimentacion2());
    }

    private static RetroalimentacionColaboracion crearRetroalimentacion() {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(4);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(5);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        return retroalimentacion;
    }

    private static RetroalimentacionColaboracion crearRetroalimentacion2 () {
    RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setIdUsuario(2);
        retroalimentacion.setColaboracion(2);
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(5);
        retroalimentacion.setMejoraDelLenguaje(5);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(4);

        return retroalimentacion;
    }

    @Test
    public void testAgregarRetroalimentacion () {
        int resultado = -1;

        try {
            RetroalimentacionColaboracionDB.agregarRetroalimentacion(crearRetroalimentacion());
        }
        catch (SQLException error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
    }

    @Test
    public void testGetPorTitulo () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionColaboracionDB.getPorId(1);
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(resultado.next());
            assertEquals(2, resultado.getInt(1));
            assertEquals(1, resultado.getInt("usuario"));
            assertEquals(1, resultado.getInt("colaboracion"));
            assertEquals(5, resultado.getInt("interaccionPar"));
            assertEquals(4, resultado.getInt("intercambioCultural"));
            assertEquals(4, resultado.getInt("calificacion"));
        }
        catch (SQLException error) {
            fail();
        }
    }

    @Test
    public void testGetPorPersonaYColaboracion () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionColaboracionDB.getPorPersonaYColaboracion(1, 1);
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(resultado.next());
            assertEquals(2, resultado.getInt(1));
            assertEquals(1, resultado.getInt("usuario"));
            assertEquals(1, resultado.getInt("colaboracion"));
            assertEquals(5, resultado.getInt("interaccionPar"));
            assertEquals(4, resultado.getInt("intercambioCultural"));
            assertEquals(4, resultado.getInt("calificacion"));
        }
        catch (SQLException error) {
            fail();
        }
    }

    @Test
    public void testGetTodos () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionColaboracionDB.getTodos();
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(resultado.next());
            assertEquals(2, resultado.getInt(1));
            assertEquals(1, resultado.getInt("usuario"));
            assertEquals(1, resultado.getInt("colaboracion"));
            assertEquals(5, resultado.getInt("interaccionPar"));
            assertEquals(4, resultado.getInt("intercambioCultural"));

            assert(resultado.next());
            assertEquals(1, resultado.getInt(1));
            assertEquals(2, resultado.getInt("usuario"));
            assertEquals(2, resultado.getInt("colaboracion"));
            assertEquals(5, resultado.getInt("interaccionPar"));
            assertEquals(5, resultado.getInt("intercambioCultural"));
            assertEquals(4, resultado.getInt("mejoraFormacionProfesional"));
        }
        catch (SQLException error) {
            fail();
        }
    }
}
