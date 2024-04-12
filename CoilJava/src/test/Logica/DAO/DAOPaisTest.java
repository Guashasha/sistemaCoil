package test.Logica.DAO;

import Logica.DAO.DAOPais;
import Logica.Dominio.Pais;
import Logica.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static test.ConfiguracionPrueba.borrarDatosTablaPais;
import static org.junit.jupiter.api.Assertions.*;

class DAOPaisTest {
    private static final DAOPais INSTANCIA = new DAOPais();

    @BeforeAll
    static void setUp () {
        borrarDatosTablaPais();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void afterAll () {
        borrarDatosTablaPais();
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
            listaObtenida = INSTANCIA.paisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaPaisesAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()){
            Pais esperado = listaEsperada.get(0);
            assertTrue(esperado.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        System.out.println("pruebaGetPaisPorNombreExitosa");
        Pais esperado = new Pais(2,"US","Estados Unidos");
        Pais obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorNombre("Estados Unidos");
            obtenido = (Pais) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
        assertTrue(esperado.equals(obtenido));
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        System.out.println("pruebaGetPaisPorNombreInexistente");
        Pais obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorNombre("Veracruz");
            obtenido = (Pais) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreInexistente");
        }
        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        System.out.println("pruebaGetPaisPorNombreNulo");
        try {
            Optional resultado = INSTANCIA.getPaisPorNombre(null);
            assertTrue(resultado.isEmpty());
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }
    }

    @Test
    void pruebaGetPaisPorNombreVacio () {
        System.out.println("pruebaGetPaisPorNombreVacio");
        try {
            Optional resultado = INSTANCIA.getPaisPorNombre("  ");
            assertTrue(resultado.isEmpty());
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreVacio");
        }
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        System.out.println("pruebaGetPaisPorIdExitosa");
        Pais esperado = new Pais(3,"BR","Brasil");
        Pais obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorId(3);
            obtenido = (Pais) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorIdExitosa");
        }
        assertTrue(esperado.equals(obtenido));
    }

    @Test
    void pruebaGetPaisPorIdNegativo () {
        System.out.println("pruebaGetPaisPorIdNegativo");
        try {
            Optional resultado = INSTANCIA.getPaisPorId(-3);
            assertTrue(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorIdNegativo");
        }
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        System.out.println("pruebaGetPaisPorIdInexistente");
        Pais obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorId(23);
            obtenido = (Pais) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorIdInexistente");
        }
        assertEquals(0,obtenido.getId());
    }
}