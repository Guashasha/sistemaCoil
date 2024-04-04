package test.Logica.DAO;

import Logica.DAO.DAOUniversidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


class DAOUniversidadTest {
    private final DAOUniversidad INSTANCIA = new DAOUniversidad();

    @BeforeEach
    void setUp() {
        ejecutarInstruccionSQL("DELETE FROM universidad;");
        ejecutarInstruccionSQL("DELETE FROM paises;");
        ejecutarInstruccionSQL("INSERT INTO paises (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    private static void ejecutarInstruccionSQL (String instruccionSQL) {
        try {
            String urlBaseDatos = "jdbc:mariadb://localhost:3307/coil";
            String usuario = "root";
            String contrasena = "040704";
            Connection conexion = DriverManager.getConnection(urlBaseDatos, usuario, contrasena);

            PreparedStatement declaracionSQL = conexion.prepareStatement(instruccionSQL);

            declaracionSQL.execute();
            declaracionSQL.close();
            conexion.close();
        }
        catch (SQLException error) {
            System.out.println("Error al ejecutar la instrucción SQL: " + error.getMessage());
        }
    }

    @Test
    void pruebaRegistrarUniversidadExitosa () {
        System.out.println("pruebaRegistrarUniversidadExitosa");

    }



    @Test
    void editarUniversidad () {
    }

    @Test
    void getUniversidadPorNombre () {
    }

    @Test
    void getUniversidadesPorPaisOrigen () {
    }

    @Test
    void getTodasAlfabeticamente () {
    }

    @Test
    void pruebaEsNuloVerdadero () {
        System.out.println("pruebaEsNuloVerdadero");
        boolean resultado = this.INSTANCIA.esNulo(null);
        assertTrue(resultado);
    }

    @Test
    void pruebaEsNuloFalso () {
        System.out.println("pruebaEsNuloFalso");
        boolean resultado = this.INSTANCIA.esNulo(new Object());
        assertFalse(resultado);
    }

    @Test
    void pruebaCadenaValidaExitosa () {
        System.out.println("pruebaCadenaValidaExitosa");
        boolean resultado = this.INSTANCIA.cadenaValida("México");
        assertTrue(resultado);
    }

    @Test
    void pruebaCadenaValidaNula () {
        System.out.println("pruebaCadenaValidaNula");
        boolean resultado = this.INSTANCIA.cadenaValida(null);
        assertFalse(resultado);
    }

    @Test
    void pruebaCadenaValidaVacia () {
        System.out.println("pruebaCadenaValidaVacia");
        boolean resultado = this.INSTANCIA.cadenaValida(" ");
        assertFalse(resultado);
    }

    @Test
    void pruebaUniversidadExisteExitosa () {
        //Uni y pais igual
    }

    @Test
    void pruebaUniversidadExisteUniversidadNula () {

    }

    @Test
    void pruebaUniversidadExistePaisNulo () {

    }

    @Test
    void pruebaUniversidadExisteUniversidadDiferente () {
        //uni diferente
    }

    @Test
    void pruebaUniversidadExistePaisDiferente () {
        //Uni igual pero pais diferente
    }
}