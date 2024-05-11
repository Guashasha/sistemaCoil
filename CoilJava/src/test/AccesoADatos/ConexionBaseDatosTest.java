package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import AccesoADatos.ConexionBaseDatos;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

class ConexionBaseDatosTest {

    @Test
    void getInstancia () {
        System.out.println("getInstancia");
        Connection resultado = null;
        try {
            resultado = ConexionBaseDatos.getInstancia();
        } catch (ErrorDAO errorDAO) {
            fail("Prueba fallida: " + errorDAO.getMessage());
        }
        assertNotNull(resultado);
    }

    @Test
    void desconectar () {
        System.out.println("desconectar");
        boolean resultado = false;

        try {
            resultado = ConexionBaseDatos.desconectar();
        } catch (ErrorDAO errorDAO) {
            fail("Prueba fallida: " + errorDAO.getMessage());
        }
        assertTrue(resultado);
    }

    @Test
    void rollaback () {
        boolean resultado = false;
        try {
            resultado = ConexionBaseDatos.rollback();
        }
        catch (ErrorDAO errorDAO) {
            fail("Prueba fallida: " + errorDAO.getMessage());
        }
        assertTrue(resultado);
    }


}