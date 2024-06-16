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
    static void prepararBaseDatos () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void limpiarBaseDatos () {
        borrarDatosTablaPais();
    }

    @Test
    void pruebaGetNombresPaisesAlfabeticamenteExitosa () {
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
        assertEquals(listaEsperada,listaObtenida,"pruebaGetNombresPaisesAlfabeticamenteExitosa");
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
        assertThrows(ErrorDAO.class,()->PAIS_AUXILIAR.getPaisPorNombre(null),"pruebaGetPaisPorNombreNulo");
    }

    @Test
    void pruebaGetPaisPorNombreVacio () {
        assertThrows(ErrorDAO.class,()->PAIS_AUXILIAR.getPaisPorNombre("  "),"pruebaGetPaisPorNombreVacio");
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
        assertThrows(ErrorDAO.class,()->PAIS_AUXILIAR.getPaisPorId(-3),"pruebaGetPaisPorIdNegativo");
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