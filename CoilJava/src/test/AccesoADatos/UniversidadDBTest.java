package test.AccesoADatos;

import AccesoADatos.UniversidadDB;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.AsercionListas.compararPaises;
import static test.AsercionListas.assertEqualListUniversidad;

class UniversidadDBTest {

    public final UniversidadDB INSTANCIA = new UniversidadDB();

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
    void pruebaRegistrarUniversidadExitoso () {
        System.out.println("pruebaRegistrarUniversidadExitoso");
        Pais pais = new Pais(1,"MX","México");
        Universidad universidad = new Universidad("UNAM",pais);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = this.INSTANCIA.registrarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: registrarUniversidadExitoso");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaRegistrarUniversidadVaciaFallida () {
        System.out.println("pruebaRegistrarUniversidadVaciaFallida");
        Pais pais = new Pais(-1,null);
        Universidad universidad = new Universidad(-1,null,pais);
        int filasAfectadas;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad(universidad);
            fail("Fallida: pruebaRegistrarUniversidadVaciaFallida. Se afectaron " + filasAfectadas);
        }
        catch (ErrorDAO error) {
            assertNotNull(error);
        }
    }

    @Test
    void pruebaRegistrarUniversidadIncorrecta () {
        System.out.println("pruebaRegistrarUniversidadIncorrecta");
        Pais pais = new Pais(0,"México");
        Universidad universidad = new Universidad(1,"Universidad Veracruzana",pais);
        int filasAfectadas;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad(universidad);
            fail("Fallida: pruebaRegistrarUniversidadIncorrecta. Se afectaron " + filasAfectadas);
        }
        catch (ErrorDAO error) {
            assertNotNull(error);
        }
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        System.out.println("pruebaEditarUniversidadExitosa");
        Pais pais = new Pais(2,"Estados Unidos");
        Universidad universidad = new Universidad(3,"MIT",pais);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = this.INSTANCIA.editarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        System.out.println("pruebaEditarUniversidadInexistente");
        Pais pais = new Pais(1,"México");
        Universidad universidad = new Universidad(10,"UV",pais);
        int esperado = 0;
        int obtenido = 1;
        try {
            obtenido = this.INSTANCIA.editarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadVacia () {
        System.out.println("pruebaEditarUniversidadInexistente");
        Pais pais = new Pais(0,null);
        Universidad universidad = new Universidad(1,null,pais);
        int filasAfectadas;
        try {
            filasAfectadas = this.INSTANCIA.editarUniversidad(universidad);
            fail("Fallida: pruebaEditarUniversidadInexistente. Filas afectadas = " + filasAfectadas);
        }
        catch (ErrorDAO error) {
            assertNotNull(error);
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        System.out.println("pruebaGetUniversidadPorNombreExitosa");
        Universidad esperada = new Universidad(1,"Universidad Veracruzana",new Pais(1,"MX","México"));
        Universidad obtenida = new Universidad();
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre("Universidad Veracruzana");
        }
        catch (ErrorDAO erro) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(esperada.getId(),obtenida.getId());
        assertEquals(esperada.getNombre(),obtenida.getNombre());
        compararPaises(1,esperada.getPaisOrigen(),obtenida.getPaisOrigen());
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        System.out.println("pruebaGetUniversidadPorNombreInexistente");
        Universidad obtenida = null;
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre("VU");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertNull(obtenida);
    }

    @Test
    void pruebaGetUniversidadPorNombreNulo () {
        System.out.println("pruebaGetUniversidadPorNombreNulo");
        Universidad obtenida = null;
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertNull(obtenida);
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");
        Pais paisBuscado = new Pais(1,"MX","México");
        Universidad universidad1 = new Universidad(1,"Universidad Veracruzana",paisBuscado);
        Universidad universidad2 = new Universidad(3,"BUAP",paisBuscado);
        List<Universidad> listaEsperada = new ArrayList<>();
        List<Universidad> listaObtenida = new ArrayList<>();
        listaEsperada.add(universidad1);
        listaEsperada.add(universidad2);

        try {
           listaObtenida = this.INSTANCIA.getUniversidadesPorPaisOrigen(paisBuscado.getNombre());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEqualListUniversidad(listaEsperada,listaObtenida);
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenInexistente () {
        System.out.println("pruebaGetUniversidadPorPaisOrigenInexistente");
        List<Universidad> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = this.INSTANCIA.getUniversidadesPorPaisOrigen("");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenInexistente");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("getTodasAlfabeticamente");
        Pais pais1 = new Pais(1,"MX","México");
        Pais pais2 = new Pais(2,"US","Estados Unidos");
        Universidad universidad1 = new Universidad(3,"BUAP",pais1);
        Universidad universidad2 = new Universidad(2,"Harvard",pais2);
        Universidad universidad3 = new Universidad(1,"Universidad Veracruzana",pais1);
        List<Universidad> listaEsperada = new ArrayList<>();
        List<Universidad> listaObtenida = new ArrayList<>();
        listaEsperada.add(universidad1);
        listaEsperada.add(universidad2);
        listaEsperada.add(universidad3);

        try {
            listaObtenida = this.INSTANCIA.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: getTodasAlfabeticamente");
        }

        assertEqualListUniversidad(listaEsperada,listaObtenida);
    }

}