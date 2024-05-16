package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import DAO.RetroalimentacionActividadDAO;
import DAO.ActividadAuxiliar;
import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionDTOActividadDAOTestDTO {

    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();
        ConfiguracionPrueba.borrarDatosTablaActividad();

        ActividadAuxiliar act = new ActividadAuxiliar();

        ActividadDTO actividadDTO = new ActividadDTO();
        actividadDTO.setTitulo("act prueba");
        actividadDTO.setDescripcion("prueba para base de datos");
        actividadDTO.setTipo(ActividadDTO.TipoActividad.disciplinar);
        act.agregar(actividadDTO);

        actividadDTO = new ActividadDTO();
        actividadDTO.setTitulo("act 2 prueba");
        actividadDTO.setDescripcion("segunda prueba para base de datos");
        actividadDTO.setTipo(ActividadDTO.TipoActividad.intercultural);
        act.agregar(actividadDTO);

        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(4);
        retroalimentacion.setInteraccionConPar(4);
        retroalimentacion.setDificultad(4);
        ret.agregar(retroalimentacion);
    }

    @Test
    void pruebaAgregarRetroalimentacion () {
        int resultado = -1;

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setComentario("hola mundo");

        try {
            resultado = RetroalimentacionActividadDAO.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
    }

    @Test
    void pruebaAgregarRetroalimentacionSinActividad () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(5);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);

        try {
            RetroalimentacionActividadDAO.agregarRetroalimentacion(retroalimentacion);
            fail();
        }
        catch (SQLException error) {
            assert(true);
        }
    }

    @Test
    void pruebaAgregarRetroalimentacionSinUsuario () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setIdUsuario(9);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);

        try {
            RetroalimentacionActividadDAO.agregarRetroalimentacion(retroalimentacion);
            fail();
        }
        catch (SQLException error) {
            assertEquals(1452, error.getErrorCode());
        }
    }

    @Test
    void pruebaGetPorId () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionActividadDAO.getPorId(1);
        } catch (SQLException e) {
            fail();
        }

        try {
            if (resultado.next()) {
                assertEquals(1, resultado.getInt(1));
                assertEquals(4, resultado.getInt(2));
                assertEquals(4, resultado.getInt(4));
                assertEquals(4, resultado.getInt(5));
            }
            else {
                fail();
            }
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    void pruebaGetPorIdInexistente () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionActividadDAO.getPorId(9);
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(!resultado.next());
        }
        catch (SQLException error) {
            fail();
        }
    }

    @Test
    void pruebaGetPorPersonaYActividad () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionActividadDAO.getPorPersonaYActividad(1, 1);
        } catch (SQLException e) {
            fail();
        }

        try {
            if (resultado.next()) {
                assertEquals(4, resultado.getInt(2));
                assertEquals(4, resultado.getInt(4));
                assertEquals(4, resultado.getInt(5));
            }
            else {
                fail();
            }
        }
        catch (SQLException error) {
            fail();
        }
    }

    @Test
    void pruebaGetPorPersonaSinActividad () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionActividadDAO.getPorPersonaYActividad(1, 9);
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(!resultado.next());
        }
        catch (Exception _e) {
            fail();
        }
    }

    @Test
    void pruebaGetSinPersonaConActividad () {
        ResultSet resultado = null;

        try {
            resultado = RetroalimentacionActividadDAO.getPorPersonaYActividad(16, 1);
        } catch (SQLException e) {
            fail();
        }

        try {
            assert(!resultado.next());
        }
        catch (Exception _e) {
            fail();
        }
    }
}
