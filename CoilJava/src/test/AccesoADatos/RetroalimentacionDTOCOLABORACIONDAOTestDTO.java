package test.AccesoADatos;

import DAO.RetroalimentacionColaboracionDAO;
import DAO.RetroalimentacionColaboracionAuxiliar;
import DTO.RetroalimentacionColaboracionDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionDTOCOLABORACIONDAOTestDTO {
    @BeforeAll
    public static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        AyudantePruebasColaboracionDB.agregarPrecondiciones();

        RetroalimentacionColaboracionAuxiliar rt = new RetroalimentacionColaboracionAuxiliar();
        rt.agregar(crearRetroalimentacion2());
    }

    private static RetroalimentacionColaboracionDTO crearRetroalimentacion () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
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

    private static RetroalimentacionColaboracionDTO crearRetroalimentacion2 () {
    RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
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
    public void pruebaAgregarRetroalimentacion () {
        int resultado = -1;

        try {
            RetroalimentacionColaboracionDAO.agregarRetroalimentacion(crearRetroalimentacion());
        }
        catch (SQLException error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
    }

    @Test
    public void pruebaGetPorTitulo () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionColaboracionDAO.getPorId(1);
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
    public void pruebaGetPorPersonaYColaboracion () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionColaboracionDAO.getPorPersonaYColaboracion(1, 1);
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
    public void pruebaGetTodos () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionColaboracionDAO.getTodos();
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
