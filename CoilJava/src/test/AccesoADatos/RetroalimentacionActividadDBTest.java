package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import AccesoADatos.ConexionBaseDatos;
import AccesoADatos.RetroalimentacionActividadDB;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    }

    @Test
    void testAgregarRetroalimentacion () {
        int resultado = -1;

        CallableStatement consulta = null;
        try {
            consulta = conector.getConexion()
                               .prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            consulta.setInt(1, 5);
            consulta.setInt(2, 5);
            consulta.setInt(3, 4);
            consulta.setInt(4, 1);
            consulta.setString(5, null);
            consulta.setInt(6, 1);

            resultado = consulta.executeUpdate();
            consulta.close();

            conector.desconectar();
        }
        catch (SQLException error) {
            System.err.println("error durante el test \"agregar retroalimentacion\" " + error.getMessage());
        }

        assertEquals(2, resultado);
    }

    @Test
    void testAgregarRetroalimentacionFallido () {
        int resultado = -1;

        CallableStatement consulta = null;

        try {
            consulta = conector.getConexion().prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            consulta.setInt(1, 5);
            consulta.setString(2, "adios");
            consulta.setInt(3, 4);
            consulta.setInt(4, 1);
            consulta.setString(5, "hola");
            consulta.setInt(6, 1);

            resultado = consulta.executeUpdate();

            conector.desconectar();
        }
        catch (SQLException error) {
            assert(true);
        }

        assertNotEquals(2, resultado);
    }

    @Test
    void testModificarRetroalimentacion () {
        int resultado = -1;

        try {
            PreparedStatement consulta;
            consulta = conector.getConexion().prepareStatement("update retroalimentacionActividad set dificultad=3 where idRetroalimentacion=1");

            resultado = consulta.executeUpdate();

            consulta.close();

            conector.desconectar();
        }
        catch (SQLException error) {
            System.err.println("error durante el test \"modificar retroalimentacion\" " + error.getMessage());
        }

        assert(resultado == 1);

        ResultSet set = RetroalimentacionActividadDB.getPorId(1);

        try {
            if (set.next()) {
                assertEquals(1, set.getInt(1));
            }
            else {
                fail();
            }
        }
        catch (SQLException e) {
            fail();
        }
    }
}
