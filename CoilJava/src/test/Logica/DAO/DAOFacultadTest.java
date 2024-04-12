package test.Logica.DAO;

import AccesoADatos.FacultadDB;
import Logica.DAO.DAOFacultad;
import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.borrarDatosTablaFacultad;
import static test.ConfiguracionPrueba.borrarDatosTablaRegion;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;

class DAOFacultadTest {
    private static final DAOFacultad INSTANCIA = new DAOFacultad();

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
        System.out.println("pruebaGetFacultadPorNombreExitosa");
        Facultad esperada = new Facultad(1,"Facultad de Estadística e Informática",1);
        Facultad obtenida = new Facultad();
        try {
            Optional resultado = INSTANCIA.getFacultadPorNombre(esperada.getNombre());
            obtenida = (Facultad) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }
        assertTrue(esperada.equals(obtenida));
    }

    @Test
    void pruebaGetFacultadPorNombreCadenaInvalida () {
        System.out.println("pruebaGetFacultadPorNombreCadenaInvalida");
        try {
            Optional resultado = INSTANCIA.getFacultadPorNombre("   ");
            assertTrue(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreCadenaInvalida");
        }
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        System.out.println("pruebaGetFacultadPorNombreInexistente");
        try {
            Optional resultado = INSTANCIA.getFacultadPorNombre("Argentina");
            Facultad esperada = (Facultad) resultado.get();
            assertEquals(0,esperada.getId());
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
    }

    @Test
    void pruebaGetFacultadPorRegionExitosa () {
        System.out.println("pruebaGetFacultadPorRegionExitosa");
        List<Facultad> listaEsperada = new ArrayList<>();
        List<Facultad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Facultad(1,"Facultad de Estadística e Informática",1));
        listaEsperada.add(new Facultad(2,"Derecho",1));

        try {
            listaObtenida = INSTANCIA.getFacultadPorRegion("Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            Facultad esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetFacultadPorRegionCadenaInvalida () {
        System.out.println("pruebaGetFacultadPorRegionCadenaInvalida");
        try {
            List<Facultad> resultado = INSTANCIA.getFacultadPorRegion(null);
            assertTrue(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("pruebaGetFacultadPorRegionCadenaInvalida");
        }
    }

    @Test
    void pruebaGetFacultadPorRegionInexistente () {
        System.out.println("pruebaGetFacultadPorRegionInexistente");
        try {
            List<Facultad> resultado = INSTANCIA.getFacultadPorRegion("Sur");
            assertTrue(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("pruebaGetFacultadPorRegionInexistente");
        }
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa() {
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

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            Facultad esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }
}