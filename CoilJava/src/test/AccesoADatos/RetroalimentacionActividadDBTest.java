package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import AccesoADatos.ConexionBaseDatos;
import Logica.ErrorDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class RetroalimentacionActividadDBTest {
    private ConexionBaseDatos conector;

    @Before
    void setUp() {
        this.conector = new ConexionBaseDatos();

        try {
            conector.conectar();
        }
        catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }

    @Test
    void testAgregarRetroalimentacion () {
        int resultado = -1;

        try {
            CallableStatement consulta;

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

        assert(resultado > 0);
    }

    @Test
    void testModificarRetroalimentacion () {
        int resultado = -1;

        assert(resultado > 0);
    }
}
