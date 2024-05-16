package test.AccesoADatos;

import DAO.PaisDAO;
import DTO.PaisDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PaisDTODAOTest {
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
            listaObtenida = PaisDAO.paisesAlfabeticamente();
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
        System.out.println("pruebaGetPaisPorNombreExitosa");
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        PaisDTO obtenido = new PaisDTO();

        try {
            obtenido = PaisDAO.getPaisPorNombre(esperado.getNombre());
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
        PaisDTO obtenido = new PaisDTO();

        try {
            obtenido = PaisDAO.getPaisPorNombre("Argentina");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }

        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        System.out.println("pruebaGetPaisPorNombreNulo");
        PaisDTO obtenido = new PaisDTO();

        try {
            obtenido = PaisDAO.getPaisPorNombre(null);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }

        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        System.out.println("pruebaGetPaisPorIdExitosa");
        PaisDTO esperado = new PaisDTO(2,"US","Estados Unidos");
        PaisDTO obtenido = new PaisDTO();

        try {
            obtenido = PaisDAO.getPaisPorId(esperado.getId());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetPaisPorIdExitosa");
        }
        assertEquals(esperado.getId(),obtenido.getId());
        assertEquals(esperado.getIso(),obtenido.getIso());
        assertEquals(esperado.getNombre(),obtenido.getNombre());
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        System.out.println("pruebaGetPaisPorIdInexistente");
        PaisDTO obtenido = new PaisDTO();

        try {
            obtenido = PaisDAO.getPaisPorId(0);
        }
        catch (SQLException error) {
            fail("Fallisa: pruebaGetPaisPorIdInexistente");
        }

        assertEquals(0,obtenido.getId());
    }
}