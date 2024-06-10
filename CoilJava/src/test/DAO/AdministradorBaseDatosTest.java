package test.DAO;

import static org.junit.jupiter.api.Assertions.*;

import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

class AdministradorBaseDatosTest {

    @Test
    void getInstancia () {
        System.out.println("getInstancia");
        Connection resultado = null;
        try {
            resultado = AdministradorBaseDatos.getInstancia();
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
            resultado = AdministradorBaseDatos.desconectar();
        } catch (ErrorDAO errorDAO) {
            fail("Prueba fallida: " + errorDAO.getMessage());
        }
        assertTrue(resultado);
    }

    @Test
    void rollaback () {
        boolean resultado = false;
        try {
            resultado = AdministradorBaseDatos.rollback();
        }
        catch (ErrorDAO errorDAO) {
            fail("Prueba fallida: " + errorDAO.getMessage());
        }
        assertTrue(resultado);
    }


}