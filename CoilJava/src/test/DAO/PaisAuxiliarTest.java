package test.DAO;

import DAO.PaisAuxiliar;
import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static test.ConfiguracionPrueba.borrarDatosTablaPais;
import static org.junit.jupiter.api.Assertions.*;

class PaisAuxiliarTest {
    private static final PaisAuxiliar PAIS_AUXILIAR = new PaisAuxiliar();

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
    void pruebaGetNombresPaisesAlfabeticamenteExitosa () {
        System.out.println("pruebaGetNombresPaisesAlfabeticamenteExitosa");
        List<String> listaEsperada = new ArrayList<>();
        List<String> listaObtenida = new ArrayList<>();
        listaEsperada.add("Brasil");
        listaEsperada.add("Estados Unidos");
        listaEsperada.add("México");

        try {
            listaObtenida = PAIS_AUXILIAR.getNombresPaisesAlfabeticamente();
        }
        catch (Utilidades.ErrorDAO error) {
            fail("Fallida: pruebaGetNombresPaisesAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        for (String nombre : listaEsperada) {
            assertEquals(nombre,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        Optional<PaisDTO> obtenido = Optional.empty();
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        try {
            obtenido = PAIS_AUXILIAR.getPaisPorNombre("Estados Unidos");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
        assertTrue(obtenido.isPresent());
        assertEquals(esperado,obtenido.get(),"pruebaGetPaisPorNombreExitosa");
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        try {
            Optional<PaisDTO> resultado = PAIS_AUXILIAR.getPaisPorNombre("Veracruz");
            assertTrue(resultado.isEmpty(),"pruebaGetPaisPorNombreInexistente");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreInexistente");
        }
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        try {
            Optional<PaisDTO> resultado = PAIS_AUXILIAR.getPaisPorNombre(null);
            assertTrue(resultado.isEmpty(),"pruebaGetPaisPorNombreNulo");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }
    }

    @Test
    void pruebaGetPaisPorNombreVacio () {
        try {
            Optional<PaisDTO> resultado = PAIS_AUXILIAR.getPaisPorNombre("  ");
            assertTrue(resultado.isEmpty(),"pruebaGetPaisPorNombreVacio");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreVacio");
        }
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        PaisDTO esperado = new PaisDTO(3,"BR","Brasil");
        Optional<PaisDTO> obtenido = Optional.empty();
        try {
            obtenido = PAIS_AUXILIAR.getPaisPorId(3);
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorIdExitosa");
        }
        assertTrue(obtenido.isPresent());
        assertEquals(esperado,obtenido.get(),"pruebaGetPaisPorIdExitosa");
    }

    @Test
    void pruebaGetPaisPorIdNegativo () {
        try {
            Optional<PaisDTO> resultado = PAIS_AUXILIAR.getPaisPorId(-3);
            assertTrue(resultado.isEmpty(),"pruebaGetPaisPorIdNegativo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorIdNegativo");
        }
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        Optional<PaisDTO> resultado;
        try {
            resultado = PAIS_AUXILIAR.getPaisPorId(23);
            assertTrue(resultado.isEmpty(),"pruebaGetPaisPorIdInexistente");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorIdInexistente");
        }
    }
}