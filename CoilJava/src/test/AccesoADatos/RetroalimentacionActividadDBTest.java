package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import AccesoADatos.ConexionBaseDatos;
import AccesoADatos.RetroalimentacionActividadDB;
import Logica.DAO.DAOActividad;
import Logica.DAO.DAORetroalimentacionActividad;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionActividadDBTest {
    private ConexionBaseDatos conector = new ConexionBaseDatos();

    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();
        ConfiguracionPrueba.borrarDatosTablaActividad();

        DAOActividad act = new DAOActividad();

        Actividad actividad = new Actividad();
        actividad.setTitulo("act prueba");
        actividad.setDescripcion("prueba para base de datos");
        actividad.setTipo(Actividad.TipoActividad.disciplinar);
        act.agregar(actividad);

        actividad = new Actividad();
        actividad.setTitulo("act 2 prueba");
        actividad.setDescripcion("segunda prueba para base de datos");
        actividad.setTipo(Actividad.TipoActividad.intercultural);
        act.agregar(actividad);

        DAORetroalimentacionActividad ret = new DAORetroalimentacionActividad();

        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(4);
        retroalimentacion.setInteraccionConPar(4);
        retroalimentacion.setDificultad(4);
        ret.agregar(retroalimentacion);
    }

    @Test
    void testAgregarRetroalimentacion () {
        int resultado = -1;

        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setComentario("hola mundo");

        try {
            resultado = RetroalimentacionActividadDB.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
    }

    @Test
    void testAgregarRetroalimentacionSinActividad () {
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(5);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);

        try {
            RetroalimentacionActividadDB.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            assert(true);
        }

        fail();
    }

    @Test
    void testAgregarRetroalimentacionSinUsuario () {
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();
        retroalimentacion.setIdActividad(1);
        retroalimentacion.setIdUsuario(9);
        retroalimentacion.setInteres(5);
        retroalimentacion.setDificultad(5);
        retroalimentacion.setInteraccionConPar(5);

        try {
            RetroalimentacionActividadDB.agregarRetroalimentacion(retroalimentacion);
        }
        catch (SQLException error) {
            assert(true);
        }

        fail();
    }

    @Test
    void testGetPorId () {
        RetroalimentacionActividad esperado = new RetroalimentacionActividad();
    }
}
