package test.DAO;

import static org.junit.jupiter.api.Assertions.*;

import DAO.ActividadAuxiliar;
import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
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

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
    }

    @Test
    void pruebaAgregarRetroalimentacion () {
        int resultadoConsulta = -1;

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setComentario("hola mundo");

        try {
            resultadoConsulta = dao.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultadoConsulta);
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
        }
        catch (ErrorDAO error) {
            assert(true);
        }
    }

    @Test
    void pruebaGetPorId () {
        RetroalimentacionActividadDTO esperado = new RetroalimentacionActividadDTO();
        esperado.setIdActividad(1);
        esperado.setIdUsuario(1);
        esperado.setInteres(4);
        esperado.setInteraccionConPar(4);
        esperado.setDificultad(4);

        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorId(1);
        } catch (ErrorDAO e) {
            fail();
        }

        if (retroalimentacionObtenida.isEmpty()) {
            fail();
        }

        RetroalimentacionActividadDTO retroalimentacion = retroalimentacionObtenida.get();

        assertEquals(esperado, retroalimentacion);
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
        RetroalimentacionActividadDTO esperado = new RetroalimentacionActividadDTO();
        esperado.setIdActividad(1);
        esperado.setIdUsuario(1);
        esperado.setInteres(4);
        esperado.setInteraccionConPar(4);
        esperado.setDificultad(4);

        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYActividad(1, 1);
        } catch (ErrorDAO e) {
            fail();
        }

        if (retroalimentacionObtenida.isEmpty()) {
            fail();
        }

        RetroalimentacionActividadDTO retroalimentacion = retroalimentacionObtenida.get();

        assertEquals(esperado, retroalimentacion);
    }

    @Test
    void pruebaGetPorPersonaSinActividad () {
        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYActividad(1, 9);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(retroalimentacionObtenida.isEmpty());
    }

    @Test
    void pruebaGetSinPersonaConActividad () {
        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYActividad(16, 1);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(retroalimentacionObtenida.isEmpty());
  }
}
