package test.AccesoADatos;

import DAO.RegionDAO;
import DTO.RegionDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;

class RegionDAOTest {
    private final RegionDAO REGION_DAO = new RegionDAO();
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
        List<RegionDTO> listaEsperada = new ArrayList<>();
        List<RegionDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new RegionDTO(3,"Orizaba-Córdoba"));
        listaEsperada.add(new RegionDTO(2,"Veracruz"));
        listaEsperada.add(new RegionDTO(1,"Xalapa"));

        try {
            listaObtenida = REGION_DAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetTodasAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        for (RegionDTO region : listaEsperada) {
            assertEquals(region,listaObtenida.get(0));
            listaEsperada.remove(0);
        }
    }
}