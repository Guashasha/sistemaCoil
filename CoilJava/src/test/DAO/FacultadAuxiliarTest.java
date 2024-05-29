package test.DAO;

import DAO.FacultadAuxiliar;
import DTO.FacultadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.borrarDatosTablaFacultad;
import static test.ConfiguracionPrueba.borrarDatosTablaRegion;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;

class FacultadAuxiliarTest {
    private final FacultadAuxiliar FACULTAD_AUXILIAR = new FacultadAuxiliar();

    @BeforeAll
    static void setUp() {
        borrarDatosTablaFacultad();
        borrarDatosTablaRegion();
        ejecutarInstruccionSQL("INSERT INTO region (idRegion,nombre) VALUES (1,'Xalapa'), (2,'Veracruz'), (3,'Orizaba-Córdoba');");
        ejecutarInstruccionSQL("INSERT INTO facultad (idFacultad,nombre, region) VALUES (1,'Facultad de Estadística e Informática',1),(2,'Derecho',1),(3,'Arquitectura',3);");

    }

    @AfterAll
    static void afterAll () {
        borrarDatosTablaFacultad();
        borrarDatosTablaRegion();
    }

    @Test
    void pruebaGetFacultadPorNombreExitosa () {
        FacultadDTO esperada = new FacultadDTO(1,"Facultad de Estadística e Informática",1);
        Optional<FacultadDTO> obtenido = Optional.empty();
        try {
            obtenido = FACULTAD_AUXILIAR.getFacultadPorNombre(esperada.getNombre());
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }
        assertTrue(obtenido.isPresent());
        assertEquals(esperada,obtenido.get(),"pruebaGetFacultadPorNombreExitosa");
    }

    @Test
    void pruebaGetFacultadPorNombreCadenaInvalida () {
        try {
            Optional<FacultadDTO> resultado = FACULTAD_AUXILIAR.getFacultadPorNombre("   ");
            assertTrue(resultado.isEmpty(),"pruebaGetFacultadPorNombreCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreCadenaInvalida");
        }
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        try {
            Optional<FacultadDTO> resultado = FACULTAD_AUXILIAR.getFacultadPorNombre("Argentina");
            assertTrue(resultado.isEmpty(),"pruebaGetFacultadPorNombreInexistente");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
    }

    @Test
    void pruebaGetFacultadPorRegionExitosa () {
        System.out.println("pruebaGetFacultadPorRegionExitosa");
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(1,"Facultad de Estadística e Informática",1));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));

        try {
            listaObtenida = FACULTAD_AUXILIAR.getFacultadPorRegion("Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        for (FacultadDTO facultad : listaEsperada) {
            assertEquals(facultad,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetFacultadPorRegionCadenaInvalida () {
        try {
            List<FacultadDTO> resultado = FACULTAD_AUXILIAR.getFacultadPorRegion(null);
            assertTrue(resultado.isEmpty(),"pruebaGetFacultadPorRegionCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("pruebaGetFacultadPorRegionCadenaInvalida");
        }
    }

    @Test
    void pruebaGetFacultadPorRegionInexistente () {
        try {
            List<FacultadDTO> resultado = FACULTAD_AUXILIAR.getFacultadPorRegion("Sur");
            assertTrue(resultado.isEmpty(),"pruebaGetFacultadPorRegionInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionInexistente");
        }
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa() {
        System.out.println("pruebaGetTodasAlfabeticamenteExitosa");
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(3,"Arquitectura",3));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));
        listaEsperada.add(new FacultadDTO(1,"Facultad de Estadística e Informática",1));

        try {
            listaObtenida = FACULTAD_AUXILIAR.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetTodasAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        for (FacultadDTO facultad : listaEsperada) {
            assertEquals(facultad,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }
}