package test.Logica;

import Logica.DAO.DAOActividad;
import Logica.DAO.DAORetroalimentacionActividad;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DAORetroalimentacionActividadTest {
    @BeforeAll
    static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();
        ConfiguracionPrueba.borrarDatosTablaActividad();
        AyudantePruebasColaboracionDB.agregarPrecondiciones();

        Actividad actividad = new Actividad("actividad setup", "descripcion de actividad setup", Actividad.TipoActividad.disciplinar);
        DAOActividad act = new DAOActividad();
        act.agregar(actividad);

        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setInteraccionConPar(4);

        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
        ret.agregar(retroalimentacion);
    }
    @Test
    void pruebaAgregarRetroalimentacionActividad () {
        int resultado = -1;
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
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
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(8);
        retroalimentacion.setInteres(9);
        retroalimentacion.setInteraccionConPar(5);

        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setInteraccionConPar(5);

        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(300);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setInteraccionConPar(5);

        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
        Optional<RetroalimentacionActividad> retroalimentacion = Optional.empty();

        try {
            retroalimentacion = ret.getPorId(1);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        RetroalimentacionActividad objRetroalimentacion = null;

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        Optional<RetroalimentacionActividad> resultado = Optional.empty();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
        Optional<RetroalimentacionActividad> resultado = Optional.empty();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
        Optional<RetroalimentacionActividad> resultado = Optional.empty();

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
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

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
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        List<RetroalimentacionActividad> retroalimentaciones = null;

        try {
            retroalimentaciones = ret.getTodos();
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(1, retroalimentaciones.size());

        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteres(4);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setInteraccionConPar(4);

        assert(retroalimentacion.equals(retroalimentaciones.get(0)));
    }

    @Test
    void pruebaGetPorPersonaYActividad () {
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        Optional<RetroalimentacionActividad> retroalimentacion = Optional.empty();

        try {
            retroalimentacion = ret.getPorPersonaYActividad(1, 1);
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        RetroalimentacionActividad resultadoEsperado = new RetroalimentacionActividad();
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