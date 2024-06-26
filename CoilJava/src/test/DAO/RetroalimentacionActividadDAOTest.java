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
    static void prepararBaseDatos () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();

        ActividadAuxiliar actividadAuxiliar = new ActividadAuxiliar();
        ActividadDTO actividad = new ActividadDTO();
        actividad.setTitulo("act prueba");
        actividad.setDescripcion("prueba para base de datos");
        actividad.setTipo(ActividadDTO.TipoActividad.disciplinar);
        actividadAuxiliar.agregar(actividad);

        actividad = new ActividadDTO();
        actividad.setTitulo("act 2 prueba");
        actividad.setDescripcion("segunda prueba para base de datos");
        actividad.setTipo(ActividadDTO.TipoActividad.intercultural);
        actividadAuxiliar.agregar(actividad);

        RetroalimentacionActividadAuxiliar retroalimentacionActividadAuxiliar = new RetroalimentacionActividadAuxiliar();
        RetroalimentacionActividadDTO retroalimentacionActividad = new RetroalimentacionActividadDTO();
        retroalimentacionActividad.setIdActividad(1);
        retroalimentacionActividad.setInteraccionConPar(4);
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais, iso, nombre) VALUES (1,'MX','México')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'UV',1)");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona,nombre,apellidos,universidad) VALUES (1,'Emmanuel','Pale',1)");
        retroalimentacionActividad.setIdUsuario(1);
        retroalimentacionActividad.setInteres(4);
        retroalimentacionActividad.setDificultad(4);
        retroalimentacionActividadAuxiliar.agregar(retroalimentacionActividad);
    }

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
    }

    @Test
    void pruebaAgregarRetroalimentacion () {
        int filasAfectadasObtenidas = -1;

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setComentario("hola mundo");

        try {
            filasAfectadasObtenidas = dao.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, filasAfectadasObtenidas);
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
