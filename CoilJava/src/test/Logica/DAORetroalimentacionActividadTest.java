//package test.Logica;
//
//import Logica.DAO.DAORetroalimentacionActividad;
//import Logica.Dominio.RetroalimentacionActividad;
//import Logica.ErrorDAO;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import test.ConfiguracionPrueba;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//
//public class DAORetroalimentacionActividadTest {
//    @BeforeAll
//    static void setUp () {
//        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
//        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
//        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();
//
//        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
//        retroalimentacion.setDificultad(5);
//        retroalimentacion.setInteres(4);
//        retroalimentacion.setIdUsuario(1);
//        retroalimentacion.setIdActividad(1);
//        retroalimentacion.setInteraccionConPar(4);
//
//        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
//        ret.agregar(retroalimentacion);
//    }
//    @Test
//    void testAgregarRetroalimentacionActividad () {
//        int resultado = -1;
//        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
//
//        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
//        retroalimentacion.setComentario("hola mundo");
//        retroalimentacion.setInteres(5);
//        retroalimentacion.setDificultad(2);
//        retroalimentacion.setInteraccionConPar(5);
//        retroalimentacion.setIdUsuario(2);
//        retroalimentacion.setIdActividad(1);
//
//        try {
//            resultado = ret.agregar(retroalimentacion);
//        }
//        catch (ErrorDAO error) {
//            fail(error.getMessage());
//        }
//
//        assertEquals(2, resultado);
//    }
//
//    @Test
//    public void testAgregarRetroalimentacionInvalida () {
//        RetroalimentacionActivadad retroalimentacion = new RetroalimentacionActividad();
//        retroalimentacion.set
//
//            // TODO
//
//        RetroalimentacionActividadDAO ret = new RetroalimentacionActividadDAO();
//        try {
//            ret.agregarRetroalimentacion(retroalimentacion);
//        }
//        catch (ErrorDAO error) {
//            assertEquals(Tipo.VALIDACION, error.getTipo())
//        }
//
//    }
//
//    @Test
//    void testGetRetroalimentacionPorId () {
//        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
//        Optional<RetroalimentacionActividad> retroalimentacion = Optional.empty();
//
//        try {
//            retroalimentacion = ret.getPorId(1);
//        }
//        catch (ErrorDAO error) {
//            fail(error.getMessage());
//        }
//
//        RetroalimentacionActividad objRetroalimentacion = null;
//
//        if (retroalimentacion.isPresent()) {
//            objRetroalimentacion = retroalimentacion.get();
//
//            assertEquals(4, objRetroalimentacion.getInteraccionConPar());
//            assertEquals(1, objRetroalimentacion.getIdRetroalimentacion());
//            assertEquals(5, objRetroalimentacion.getDificultad());
//            assert(objRetroalimentacion.getComentario().isEmpty());
//        }
//        else {
//            fail("no existe la retroalimentacion");
//        }
//    }
//
//    @Test
//    void testValidarRetroalimentacion () {
//        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
//        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
//
//        retroalimentacion.setComentario("hola mundo");
//        retroalimentacion.setInteres(5);
//        retroalimentacion.setDificultad(2);
//        retroalimentacion.setInteraccionConPar(5);
//        retroalimentacion.setIdUsuario(1);
//        retroalimentacion.setIdActividad(1);
//
//        assert(retroalimentacion.esCorrecto());
//    }
//
//    @Test
//    void testGetTodos () {
//        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
//
//        List<RetroalimentacionActividad> retroalimentaciones = null;
//
//        try {
//            retroalimentaciones = ret.getTodos();
//        }
//        catch (ErrorDAO error) {
//            fail(error.getMessage());
//        }
//
//        assertEquals(1, retroalimentaciones.size());
//
//        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
//        retroalimentacion.setDificultad(5);
//        retroalimentacion.setInteres(4);
//        retroalimentacion.setIdUsuario(1);
//        retroalimentacion.setIdActividad(1);
//        retroalimentacion.setInteraccionConPar(4);
//
//        assert(retroalimentacion.equals(retroalimentaciones.get(0)));
//    }
//
//    @Test
//    void testGetPorPersonaYActividad () {
//        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
//
//        Optional<RetroalimentacionActividad> retroalimentacion = Optional.empty();
//
//        try {
//            retroalimentacion = ret.getPorPersonaYActividad(1, 1);
//        }
//        catch (ErrorDAO error) {
//            fail(error.getMessage());
//        }
//
//        RetroalimentacionActividad resultadoEsperado = new RetroalimentacionActividad();
//        resultadoEsperado.setDificultad(5);
//        resultadoEsperado.setInteres(4);
//        resultadoEsperado.setIdUsuario(1);
//        resultadoEsperado.setIdActividad(1);
//        resultadoEsperado.setInteraccionConPar(4);
//
//        if (retroalimentacion.isPresent()) {
//            assert(resultadoEsperado.equals(retroalimentacion.get()));
//        }
//        else {
//            fail("no existe la retroalimentacion");
//        }
//    }
//}
