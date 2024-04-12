package test.Logica.DAO;

import Logica.DAO.DAORegion;
import Logica.Dominio.Region;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;

class DAORegionTest {
    private static final DAORegion INSTANCIA = new DAORegion();

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
            listaObtenida = INSTANCIA.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetTodasAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            Region esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }


}