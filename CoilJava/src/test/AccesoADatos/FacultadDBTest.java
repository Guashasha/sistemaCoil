package test.AccesoADatos;

import AccesoADatos.FacultadDB;
import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;
import static test.AsercionListas.assertEqualListFacultad;
import static org.junit.jupiter.api.Assertions.*;

class FacultadDBTest {
    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ejecutarInstruccionSQL("INSERT INTO region (idRegion,nombre) VALUES (1,'Xalapa'), (2,'Veracruz'), (3,'Orizaba-Córdoba');");
        ejecutarInstruccionSQL("INSERT INTO facultad (idFacultad,nombre, region) VALUES (1,'Facultad de Estadística e Informática',1),(2,'Derecho',1),(3,'Arquitectura',3);");

    }

    @AfterAll
    static void afterAll () {
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
    }

    @Test
    void pruebaGetFacultadPorNombreExitosa () {
        System.out.println("pruebaGetFacultadPorNombreExitosa");
        Facultad esperada = new Facultad(2,"Derecho",1);
        Facultad obtenida = new Facultad();

        try {
            obtenida = FacultadDB.getFacultadPorNombre(esperada.getNombre());
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }

        assertEquals(esperada.getId(),obtenida.getId());
        assertEquals(esperada.getNombre(),obtenida.getNombre());
        assertEquals(esperada.getIdRegion(),obtenida.getIdRegion());
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        System.out.println("pruebaGetFacultadPorNombreInexistente");
        Facultad obtenida = new Facultad();

        try {
            obtenida = FacultadDB.getFacultadPorNombre("FEI");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }

        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetFacultadPorNombreNulo () {
        System.out.println("pruebaGetFacultadPorNombreInexistente");
        Facultad obtenida = new Facultad();

        try {
            obtenida = FacultadDB.getFacultadPorNombre("FEI");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }

        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetFacultadPorRegionExitosa () {
        System.out.println("pruebaGetFacultadPorRegionExitosa");
        List<Facultad> listaEsperada = new ArrayList<>();
        List<Facultad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Facultad(1,"Facultad de Estadística e Informática",1));
        listaEsperada.add(new Facultad(2,"Derecho",1));

        try {
            listaObtenida = FacultadDB.getFacultadPorRegion("Xalapa");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorRegionExitosa");
        }

        assertEqualListFacultad(listaEsperada,listaObtenida);
    }

    @Test
    void pruebaGetFacultadPorRegionInexistente () {
        System.out.println("pruebaGetFacultadPorRegionInexistente");
        List<Facultad> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = FacultadDB.getFacultadPorRegion("Coatepec");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorRegionInexistente");
        }
        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetFacultadPorRegionNula () {
        System.out.println("pruebaGetFacultadPorRegionNula");
        List<Facultad> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = FacultadDB.getFacultadPorRegion(null);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetFacultadPorRegionNula");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("pruebaGetTodasAlfabeticamenteExitosa");
        List<Facultad> listaEsperada = new ArrayList<>();
        List<Facultad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Facultad(3,"Arquitectura",3));
        listaEsperada.add(new Facultad(2,"Derecho",1));
        listaEsperada.add(new Facultad(1,"Facultad de Estadística e Informática",1));

        try {
            listaObtenida = FacultadDB.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("pruebaGetTodasAlfabeticamenteExitosa");
        }
        assertEqualListFacultad(listaEsperada,listaObtenida);
    }
}