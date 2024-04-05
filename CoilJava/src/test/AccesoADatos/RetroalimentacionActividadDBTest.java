package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import AccesoADatos.ConexionBaseDatos;
import AccesoADatos.RetroalimentacionActividadDB;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionActividadDBTest {
    private ConexionBaseDatos conector;

    @BeforeAll
    static void setUp() {
        ConexionBaseDatos conector = new ConexionBaseDatos();

        try {
            CallableStatement consulta = conector.getConexion().prepareCall("delete from retroalimentacion, retroalimentacionActividad, retroalimentacionColaboracion");
            consulta.execute();
        }
        catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }

    @Test
    void testAgregarRetroalimentacion () {
        int resultado = -1;

        CallableStatement consulta = null;
        try {
            consulta = conector.getConexion()
                               .prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            conector.desconectar();

            consulta.setInt(1, 5);
            consulta.setInt(2, 5);
            consulta.setInt(3, 4);
            consulta.setInt(4, 1);
            consulta.setString(5, null);
            consulta.setInt(6, 1);

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        catch (SQLException error) {
            System.err.println("error durante el test \"agregar retroalimentacion\" " + error.getMessage());
        }

        assert (resultado == 1);
    }

    @Test
    void testAgregarRetroalimentacionFallido () {
        int resultado = -1;

        CallableStatement consulta = null;

        try {
            consulta = conector.getConexion().prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            conector.desconectar();

            consulta.setInt(1, 5);
            consulta.setString(2, "adios");
            consulta.setInt(3, 4);
            consulta.setInt(4, 1);
            consulta.setString(5, "hola");
            consulta.setInt(6, 1);

            resultado = consulta.executeUpdate();
        }
        catch (SQLException error) {
            assert(true);
        }

        assert(resultado == -1);
    }

    @Test
    void testModificarRetroalimentacion () {
        int resultado = -1;

        try {
            CallableStatement consulta;
            consulta = conector.getConexion().prepareCall("update retroalimentacionActividad set dificultad=3 where idRetroalimentacion=1");

            conector.desconectar();

            resultado = consulta.executeUpdate();

            consulta.close();
        }
        catch (SQLException error) {
            System.err.println("error durante el test \"agregar retroalimentacion\" " + error.getMessage());
        }

        assert(resultado == 1);

        ResultSet set = RetroalimentacionActividadDB.getPorId(1);

        try {
            if (set.next()) {
                assert(set.getInt(4) == 3);
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
