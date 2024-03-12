package test.AccesoADatos;

import static org.junit.jupiter.api.Assertions.*;

import AccesoADatos.ConexionBaseDatos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.sql.Connection;
import java.sql.SQLException;

class ConexionBaseDatosTest {

    private ConexionBaseDatos conectorBaseDatos;

    @BeforeEach
    void setUp() {
        this.conectorBaseDatos = new ConexionBaseDatos();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void getConexion() {
        System.out.println("getConexion");
        Connection resultado = null;

        try {
            resultado = this.conectorBaseDatos.getConexion();
        } catch (SQLException excepcionSQL) {
            fail("Prueba fallida: SQLException");
        }

        assertNotNull(resultado);
    }


}