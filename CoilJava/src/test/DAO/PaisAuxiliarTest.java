package test.DAO;

import DAO.PaisAuxiliar;
import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class PaisAuxiliarTest {
    private static final PaisAuxiliar PAIS_AUXILIAR = new PaisAuxiliar();

    @BeforeAll
    static void prepararBaseDatos () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void limpiarBaseDatos () {
        ConfiguracionPrueba.borrarDatosTablaPais();
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
        Optional<PaisDTO> paisObtenido = Optional.empty();
        PaisDTO paisEsperado = new PaisDTO(2,"US","Estados Unidos");
        try {
            paisObtenido = PAIS_AUXILIAR.getPaisPorNombre("Estados Unidos");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
        assertEquals(paisEsperado,paisObtenido.get(),"pruebaGetPaisPorNombreExitosa");
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        try {
            Optional<PaisDTO> paisObtenido = PAIS_AUXILIAR.getPaisPorNombre("Veracruz");
            assertTrue(paisObtenido.isEmpty(),"pruebaGetPaisPorNombreInexistente");
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
}