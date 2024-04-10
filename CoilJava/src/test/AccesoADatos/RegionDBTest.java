package test.AccesoADatos;

import AccesoADatos.RegionDB;
import Logica.Dominio.Region;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;
import static test.AsercionListas.assertEqualListRegion;

class RegionDBTest {
    @BeforeAll
    static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ejecutarInstruccionSQL("INSERT INTO region (idRegion,nombre) VALUES (1,'Xalapa'), (2,'Veracruz'), (3,'Orizaba-Córdoba');");
    }

    @AfterAll
    static void afterAll () {
        ConfiguracionPrueba.borrarDatosTablaRegion();
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("pruebaGetTodasAlfabeticamenteExitosa");
        List<Region> listaEsperada = new ArrayList<>();
        List<Region> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Region(3,"Orizaba-Córdoba"));
        listaEsperada.add(new Region(2,"Veracruz"));
        listaEsperada.add(new Region(1,"Xalapa"));

        try {
            listaObtenida = RegionDB.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetTodasAlfabeticamenteExitosa");
        }
        assertEqualListRegion(listaEsperada,listaObtenida);
    }
}