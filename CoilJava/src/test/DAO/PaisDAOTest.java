package test.DAO;

import DAO.PaisDAO;
import DTO.PaisDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PaisDAOTest {
    private final PaisDAO PAIS_DAO = new PaisDAO();
    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaPais();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @AfterAll
    static void afterAll () {
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaPaisesAlfabeticamenteExitosa () {
        System.out.println("pruebaPaisesAlfabeticamenteExitosa");
        List<PaisDTO> listaEsperada = new ArrayList<>();
        List<PaisDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new PaisDTO(3,"BR","Brasil"));
        listaEsperada.add(new PaisDTO(2,"US","Estados Unidos"));
        listaEsperada.add(new PaisDTO(1,"MX","México"));

        try {
            listaObtenida = PAIS_DAO.getPaisesAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaPaisesAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()){
            PaisDTO esperado = listaEsperada.get(0);
            assert(esperado.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        Optional<PaisDTO> paisObtenidoOptional = Optional.empty();
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        try {
            paisObtenidoOptional = PAIS_DAO.getPaisPorNombre(esperado.getNombre());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
        assertTrue(paisObtenidoOptional.isPresent());
        assertEquals(esperado,paisObtenidoOptional.get(),"pruebaGetPaisPorNombreExitosa");
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        try {
            Optional<PaisDTO> obtenido = PAIS_DAO.getPaisPorNombre("Argentina");
            assertTrue(obtenido.isEmpty(),"pruebaGetPaisPorNombreInexistente");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        try {
            Optional<PaisDTO> obtenido = PAIS_DAO.getPaisPorNombre(null);
            assertTrue(obtenido.isEmpty(),"pruebaGetPaisPorNombreNulo");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        Optional<PaisDTO> obtenidoOptional = Optional.empty();
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        try {
            obtenidoOptional = PAIS_DAO.getPaisPorId(esperado.getId());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorIdExitosa");
        }
        assertTrue(obtenidoOptional.isPresent());
        assertEquals(esperado,obtenidoOptional.get(),"pruebaGetPaisPorIdExitosa");
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        try {
            Optional<PaisDTO> obtenido = PAIS_DAO.getPaisPorId(0);
            assertTrue(obtenido.isEmpty(),"pruebaGetPaisPorIdInexistente");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorIdInexistente");
        }
    }
}