package test.DAO;

import static org.junit.jupiter.api.Assertions.*;

import DAO.ActividadAuxiliar;
import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.Optional;

public class RetroalimentacionActividadDAOTest {
    RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

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
            resultado = dao.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
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
            dao.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assert(true);
        }
    }

    @Test
    void pruebaGetPorId () {
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorId(1);
        } catch (ErrorDAO e) {
            fail();
        }

        if (resultado.isEmpty()) {
            fail();
        }

        RetroalimentacionActividadDTO retroalimentacion = resultado.get();

        assertEquals(1, retroalimentacion.getIdActividad());
        assertEquals(1, retroalimentacion.getIdUsuario());
        assertEquals(4, retroalimentacion.getInteres());
        assertEquals(4, retroalimentacion.getInteraccionConPar());
        assertEquals(4, retroalimentacion.getDificultad());
    }

    @Test
    void pruebaGetPorIdInexistente () {
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorId(9);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    void pruebaGetPorPersonaYActividad () {
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYActividad(1, 1);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isPresent());

        RetroalimentacionActividadDTO retroalimentacion = resultado.get();

        assertEquals(4, retroalimentacion.getInteres());
        assertEquals(4, retroalimentacion.getInteraccionConPar());
        assertEquals(4, retroalimentacion.getDificultad());
    }

    @Test
    void pruebaGetPorPersonaSinActividad () {
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYActividad(1, 9);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    void pruebaGetSinPersonaConActividad () {
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYActividad(16, 1);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isEmpty());
  }
}
