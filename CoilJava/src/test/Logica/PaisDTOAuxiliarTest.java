package test.Logica;

import DAO.PaisAuxiliar;
import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;

import java.util.Optional;
import static test.ConfiguracionPrueba.borrarDatosTablaPais;
import static org.junit.jupiter.api.Assertions.*;

class PaisDTOAuxiliarTest {
    private static final PaisAuxiliar INSTANCIA = new PaisAuxiliar();

    @BeforeAll
    static void setUp () {
        borrarDatosTablaPais();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void afterAll () {
        borrarDatosTablaPais();
    }
/*
    @Test
    void pruebaPaisesAlfabeticamenteExitosa () {
        System.out.println("pruebaPaisesAlfabeticamenteExitosa");
        List<PaisDTO> listaEsperada = new ArrayList<>();
        List<PaisDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new PaisDTO(3,"BR","Brasil"));
        listaEsperada.add(new PaisDTO(2,"US","Estados Unidos"));
        listaEsperada.add(new PaisDTO(1,"MX","México"));

        try {
            listaObtenida = INSTANCIA.getNombresPaisesAlfabeticamente();
        }
        catch (Utilidades.ErrorDAO error) {
            fail("Fallida: pruebaPaisesAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()){
            PaisDTO esperado = listaEsperada.get(0);
            assertTrue(esperado.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }*/

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        System.out.println("pruebaGetPaisPorNombreExitosa");
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        PaisDTO obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorNombre("Estados Unidos");
            obtenido = (PaisDTO) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
        assertTrue(esperado.equals(obtenido));
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        System.out.println("pruebaGetPaisPorNombreInexistente");
        PaisDTO obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorNombre("Veracruz");
            obtenido = (PaisDTO) resultado.get();
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
        PaisDTO esperado = new PaisDTO(3,"BR","Brasil");
        PaisDTO obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorId(3);
            obtenido = (PaisDTO) resultado.get();
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
        PaisDTO obtenido = null;
        try {
            Optional resultado = INSTANCIA.getPaisPorId(23);
            obtenido = (PaisDTO) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorIdInexistente");
        }
        assertEquals(0,obtenido.getId());
    }
}