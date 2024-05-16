package test.AccesoADatos;

import DAO.FacultadDAO;
import DTO.FacultadDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;
import static org.junit.jupiter.api.Assertions.*;

class FacultadDTODAOTest {
    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ejecutarInstruccionSQL("INSERT INTO region (idRegion,nombre) VALUES (1,'Xalapa'), (2,'Veracruz'), (3,'Orizaba-Córdoba');");
        ejecutarInstruccionSQL("INSERT INTO facultad (idFacultad,nombre, region) VALUES (1,'FacultadDTO de Estadística e Informática',1),(2,'Derecho',1),(3,'Arquitectura',3);");

    }

    @AfterAll
    static void afterAll () {
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
    }

    @Test
    void pruebaGetFacultadPorNombreExitosa () {
        System.out.println("pruebaGetFacultadPorNombreExitosa");
        FacultadDTO esperada = new FacultadDTO(2,"Derecho",1);
        FacultadDTO obtenida = new FacultadDTO();

        try {
            obtenida = FacultadDAO.getFacultadPorNombre(esperada.getNombre());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }

        assertTrue(esperada.equals(obtenida));
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        System.out.println("pruebaGetFacultadPorNombreInexistente");
        FacultadDTO obtenida = new FacultadDTO();

        try {
            obtenida = FacultadDAO.getFacultadPorNombre("FEI");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }

        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetFacultadPorNombreNulo () {
        System.out.println("pruebaGetFacultadPorNombreInexistente");
        FacultadDTO obtenida = new FacultadDTO();

        try {
            obtenida = FacultadDAO.getFacultadPorNombre("FEI");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }

        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetFacultadPorRegionExitosa () {
        System.out.println("pruebaGetFacultadPorRegionExitosa");
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(1,"FacultadDTO de Estadística e Informática",1));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));

        try {
            listaObtenida = FacultadDAO.getFacultadPorRegion("Xalapa");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorRegionExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            FacultadDTO esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetFacultadPorRegionInexistente () {
        System.out.println("pruebaGetFacultadPorRegionInexistente");
        List<FacultadDTO> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = FacultadDAO.getFacultadPorRegion("Coatepec");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorRegionInexistente");
        }
        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetFacultadPorRegionNula () {
        System.out.println("pruebaGetFacultadPorRegionNula");
        List<FacultadDTO> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = FacultadDAO.getFacultadPorRegion(null);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorRegionNula");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("pruebaGetTodasAlfabeticamenteExitosa");
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(3,"Arquitectura",3));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));
        listaEsperada.add(new FacultadDTO(1,"FacultadDTO de Estadística e Informática",1));

        try {
            listaObtenida = FacultadDAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("pruebaGetTodasAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            FacultadDTO esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }
}