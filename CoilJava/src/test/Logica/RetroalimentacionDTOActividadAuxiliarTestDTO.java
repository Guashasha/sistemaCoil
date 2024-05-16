package test.Logica;

import DAO.ActividadAuxiliar;
import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionDTOActividadAuxiliarTestDTO {
    @BeforeAll
    static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
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
    @Test
    void pruebaAgregarRetroalimentacionActividad () {
        int resultado = -1;
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(2);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setIdUsuario(2);
        retroalimentacion.setIdActividad(1);

        try {
            resultado = ret.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
    }

    @Test
    public void pruebaAgregarRetroalimentacionInvalida () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(8);
        retroalimentacion.setInteres(9);
        retroalimentacion.setInteraccionConPar(5);

        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        try {
            ret.agregar(retroalimentacion);
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

        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        try {
            ret.agregar(retroalimentacion);
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

        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        try {
            ret.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.CONSULTA, error.getTipo());
        }
    }

    @Test
    void pruebaGetRetroalimentacionPorId () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();
        Optional<RetroalimentacionActividadDTO> retroalimentacion = Optional.empty();

        try {
            retroalimentacion = ret.getPorId(1);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        RetroalimentacionActividadDTO objRetroalimentacion = null;

        if (retroalimentacion.isPresent()) {
            objRetroalimentacion = retroalimentacion.get();

            assertEquals(4, objRetroalimentacion.getInteraccionConPar());
            assertEquals(1, objRetroalimentacion.getIdRetroalimentacion());
            assertEquals(5, objRetroalimentacion.getDificultad());
            assert(objRetroalimentacion.getComentario().isEmpty());
        }
        else {
            fail("no existe la retroalimentacion");
        }
    }

    @Test
    public void pruebaGetPorIdInvalido () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        try {
            ret.getPorId(-5);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            resultado = ret.getPorId(100);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(Optional.empty(), resultado);
    }

    @Test
    void pruebaPorPersonaIncorrecta () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        try {
            ret.getPorPersonaYActividad(-10, 1);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    void pruebaPorActividadIncorrecta () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        try {
            ret.getPorPersonaYActividad(1, -20);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    void getPorPersonaInexistente () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            ret.getPorPersonaYActividad(200, 1);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(Optional.empty(), resultado);
    }

    @Test
    void getPorActividadInexistente () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();
        Optional<RetroalimentacionActividadDTO> resultado = Optional.empty();

        try {
            ret.getPorPersonaYActividad(1, 1000);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(Optional.empty(), resultado);
    }

    @Test
    void pruebaValidarRetroalimentacion () {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

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
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        List<RetroalimentacionActividadDTO> retroalimentaciones = null;

        try {
            retroalimentaciones = ret.getTodos();
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(1, retroalimentaciones.size());

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setInteraccionConPar(4);

        assert(retroalimentacion.equals(retroalimentaciones.get(0)));
    }

    @Test
    void pruebaGetPorPersonaYActividad () {
        RetroalimentacionActividadAuxiliar ret = new RetroalimentacionActividadAuxiliar();

        Optional<RetroalimentacionActividadDTO> retroalimentacion = Optional.empty();

        try {
            retroalimentacion = ret.getPorPersonaYActividad(1, 1);
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

        if (retroalimentacion.isPresent()) {
            assert(resultadoEsperado.equals(retroalimentacion.get()));
        }
        else {
            fail("no existe la retroalimentacion");
        }
    }
}