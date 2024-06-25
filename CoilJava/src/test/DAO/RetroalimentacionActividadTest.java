package test.DAO;

import DAO.ActividadAuxiliar;
import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionActividadTest {
    @BeforeAll
    static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaCalendarioActividades();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();
        ConfiguracionPrueba.borrarDatosTablaActividad();
        AyudantePruebasColaboracionDB.agregarPrecondiciones();

        ActividadDTO actividadDTO = new ActividadDTO("actividadDTO setup", "descripcion de actividadDTO setup", ActividadDTO.TipoActividad.disciplinar);
        ActividadAuxiliar act = new ActividadAuxiliar();
        act.agregar(actividadDTO);

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setInteraccionConPar(4);

        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();
        ret.agregar(retroalimentacion);
    }

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
    }

    @Test
    void pruebaAgregarRetroalimentacionActividad () {
        int resultadoConsulta = -1;
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(2);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setIdUsuario(2);
        retroalimentacion.setIdActividad(1);

        try {
            resultadoConsulta = ret.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultadoConsulta);
    }

    @Test
    public void pruebaAgregarRetroalimentacionInvalida () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(8);
        retroalimentacion.setInteres(9);
        retroalimentacion.setInteraccionConPar(5);

        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebaAgregarRetroalimentacionExistente () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setInteraccionConPar(5);

        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.DUPLICIDAD, error.getTipo());
        }
    }

    @Test
    public void pruebaAgregarConActividadInexistente () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(300);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setInteraccionConPar(5);

        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.CONSULTA, error.getTipo());
        }
    }

    @Test
    void pruebaGetRetroalimentacionPorId () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();
        RetroalimentacionActividadDTO esperado = new RetroalimentacionActividadDTO();
        esperado.setDificultad(5);
        esperado.setInteres(4);
        esperado.setIdUsuario(1);
        esperado.setIdActividad(1);
        esperado.setInteraccionConPar(4);

        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorId(1);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        RetroalimentacionActividadDTO objRetroalimentacion = null;

        if (retroalimentacionObtenida.isPresent()) {
            objRetroalimentacion = retroalimentacionObtenida.get();

            assertEquals(esperado, objRetroalimentacion);
        }
        else {
            fail("no existe la retroalimentacion");
        }
    }

    @Test
    public void pruebaGetPorIdInvalido () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.getPorId(-5);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorId(100);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(Optional.empty(), retroalimentacionObtenida);
    }

    @Test
    void pruebaPorPersonaIncorrecta () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.getPorPersonaYActividad(-10, 1);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    void pruebaPorActividadIncorrecta () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.getPorPersonaYActividad(1, -20);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    void getPorPersonaInexistente () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();
        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            dao.getPorPersonaYActividad(200, 1);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(Optional.empty(), retroalimentacionObtenida);
    }

    @Test
    void getPorActividadInexistente () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();
        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            dao.getPorPersonaYActividad(1, 1000);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(Optional.empty(), retroalimentacionObtenida);
    }

    @Test
    void pruebaValidarRetroalimentacion () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(2);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);

        assert(retroalimentacion.esCorrecto());
    }

    @Test
    void pruebaGetTodos () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        List<RetroalimentacionActividadDTO> retroalimentacionesObtenidas = null;

        try {
            retroalimentacionesObtenidas = dao.getTodos();
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, retroalimentacionesObtenidas.size());

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setInteraccionConPar(4);

        assert(retroalimentacion.equals(retroalimentacionesObtenidas.get(0)));
    }

    @Test
    void pruebaGetPorPersonaYActividad () {
        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        Optional<RetroalimentacionActividadDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYActividad(1, 1);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        RetroalimentacionActividadDTO resultadoEsperado = new RetroalimentacionActividadDTO();
        resultadoEsperado.setDificultad(5);
        resultadoEsperado.setInteres(4);
        resultadoEsperado.setIdUsuario(1);
        resultadoEsperado.setIdActividad(1);
        resultadoEsperado.setInteraccionConPar(4);

        if (retroalimentacionObtenida.isPresent()) {
            assert(resultadoEsperado.equals(retroalimentacionObtenida.get()));
        }
        else {
            fail("no existe la retroalimentacion");
        }
    }
}