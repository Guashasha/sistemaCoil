package test.AccesoADatos;

import AccesoADatos.PaisDB;
import Logica.Dominio.Pais;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.AsercionListas.assertEqualListPais;

class PaisDBTest {
    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.ejecutarInstruccionSQL("DELETE FROM pais;");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void afterAll () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("DELETE FROM pais;");
    }

    @Test
    void pruebaPaisesAlfabeticamenteExitosa () {
        System.out.println("pruebaPaisesAlfabeticamenteExitosa");
        List<Pais> listaEsperada = new ArrayList<>();
        List<Pais> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Pais(3,"BR","Brasil"));
        listaEsperada.add(new Pais(2,"US","Estados Unidos"));
        listaEsperada.add(new Pais(1,"MX","México"));

        try {
            listaObtenida = PaisDB.paisesAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaPaisesAlfabeticamenteExitosa");
        }

        assertEqualListPais(listaEsperada,listaObtenida);
    }

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        System.out.println("pruebaGetPaisPorNombreExitosa");
        Pais esperado = new Pais(2,"US","Estados Unidos");
        Pais obtenido = new Pais();

        try {
            obtenido = PaisDB.getPaisPorNombre(esperado.getNombre());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }

        assertEquals(esperado.getId(),obtenido.getId());
        assertEquals(esperado.getIso(),obtenido.getIso());
        assertEquals(esperado.getNombre(),obtenido.getNombre());
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        System.out.println("pruebaGetPaisPorNombreInexistente");
        Pais obtenido = new Pais();

        try {
            obtenido = PaisDB.getPaisPorNombre("Argentina");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }

        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        System.out.println("pruebaGetPaisPorNombreNulo");
        Pais obtenido = new Pais();

        try {
            obtenido = PaisDB.getPaisPorNombre(null);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }

        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        System.out.println("pruebaGetPaisPorIdExitosa");
        Pais esperado = new Pais(2,"US","Estados Unidos");
        Pais obtenido = new Pais();

        try {
            obtenido = PaisDB.getPaisPorId(esperado.getId());
        }
        catch (SQLException error) {
            fail("Fallisa: pruebaGetPaisPorIdExitosa");
        }
        assertEquals(esperado.getId(),obtenido.getId());
        assertEquals(esperado.getIso(),obtenido.getIso());
        assertEquals(esperado.getNombre(),obtenido.getNombre());
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        System.out.println("pruebaGetPaisPorIdInexistente");
        Pais obtenido = new Pais();

        try {
            obtenido = PaisDB.getPaisPorId(0);
        }
        catch (SQLException error) {
            fail("Fallisa: pruebaGetPaisPorIdInexistente");
        }

        assertEquals(0,obtenido.getId());
    }
}