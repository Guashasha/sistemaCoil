package test.Logica;

import AccesoADatos.ConexionBaseDatos;
import Logica.DAO.DAORetroalimentacionActividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DAORetroalimentacionActividadTest {
    @BeforeAll
    static void setUp () {
        ConexionBaseDatos conector = new ConexionBaseDatos();

        try {
            CallableStatement consulta = conector.getConexion().prepareCall("delete from retroalimentacion, retroalimentacionActividad, retroalimentacionColaboracion");
            consulta.execute();
        }
        catch (SQLException error) {
            throw new RuntimeException(error);
        }
        finally {
            try {
                conector.desconectar();
            }
            catch (SQLException error) {
                throw new RuntimeException(error);
            }
        }
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

        assertEquals(1, resultado);
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

        assert(retroalimentacion.isPresent());

        RetroalimentacionActividad objRetroalimentacion = retroalimentacion.get();

        assertEquals(1, objRetroalimentacion.getIdActividad());
        assertEquals(5, objRetroalimentacion.getInteraccionConPar());
        assert(objRetroalimentacion.getComentario().get()
                                   .equals("hola mundo"));
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
}
