package test.DAO;

import DAO.PaisDAO;
import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class PaisDAOTest {
    private final PaisDAO PAIS_DAO = new PaisDAO();
    @BeforeAll
    static void prepararBaseDatos() {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void limpiarBaseDatos () {
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaPaisesAlfabeticamenteExitosa () {
        List<PaisDTO> listaEsperada = new ArrayList<>();
        List<PaisDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new PaisDTO(3,"BR","Brasil"));
        listaEsperada.add(new PaisDTO(2,"US","Estados Unidos"));
        listaEsperada.add(new PaisDTO(1,"MX","México"));

        try {
            listaObtenida = PAIS_DAO.getPaisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaPaisesAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada,listaObtenida,"pruebaPaisesAlfabeticamenteExitosa");
    }

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        Optional<PaisDTO> paisObtenido = Optional.empty();
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        try {
            paisObtenido = PAIS_DAO.getPaisPorNombre(esperado.getNombre());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
        assertEquals(esperado,paisObtenido.get(),"pruebaGetPaisPorNombreExitosa");
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        try {
            Optional<PaisDTO> paisObtenido = PAIS_DAO.getPaisPorNombre("Argentina");
            assertTrue(paisObtenido.isEmpty(),"pruebaGetPaisPorNombreInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        try {
            Optional<PaisDTO> paisObtenido = PAIS_DAO.getPaisPorNombre(null);
            assertTrue(paisObtenido.isEmpty(),"pruebaGetPaisPorNombreNulo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        Optional<PaisDTO> paisObtenido = Optional.empty();
        PaisDTO paisEsperado = new PaisDTO(2,"US","Estados Unidos");
        try {
            paisObtenido = PAIS_DAO.getPaisPorId(paisEsperado.getId());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorIdExitosa");
        }
        assertEquals(paisEsperado,paisObtenido.get(),"pruebaGetPaisPorIdExitosa");
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        try {
            Optional<PaisDTO> paisObtenido = PAIS_DAO.getPaisPorId(0);
            assertTrue(paisObtenido.isEmpty(),"pruebaGetPaisPorIdInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorIdInexistente");
        }
    }
}