package test.Logica;

import AccesoADatos.ConexionBaseDatos;
import Logica.DAO.DAORetroalimentacionActividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DAORetroalimentacionActividadTest {
    @BeforeAll
    static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

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
    void testAgregarRetroalimentacionActividad () {
        int resultado = -1;
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(2);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);

        try {
            resultado = ret.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(2, resultado);
    }

    @Test
    void testGetRetroalimentacionPorId () {
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();
        Optional<RetroalimentacionActividad> retroalimentacion = Optional.empty();

        try {
            retroalimentacion = ret.getPorId(1);
        }
        catch (ErrorDAO error) {
            fail();
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
            fail();
        }
    }

    @Test
    void testValidarRetroalimentacion () {
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(2);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setIdActividad(1);

        boolean resultado = false;

        try {
            resultado = ret.validarRetroalimentacion(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assert(resultado);
    }

    @Test
    void testGetTodos () {
        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        List<RetroalimentacionActividad> retroalimentaciones = null;
        try {
            retroalimentaciones = ret.getTodos();
        }
        catch (ErrorDAO error) {
            fail();
        }
    }
}
