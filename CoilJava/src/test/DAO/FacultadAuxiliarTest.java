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
    static void prepararBaseDatos () {
        ejecutarInstruccionSQL("INSERT INTO region (idRegion,nombre) VALUES (1,'Xalapa'), (2,'Veracruz'), (3,'Orizaba-Córdoba');");
        ejecutarInstruccionSQL("INSERT INTO facultad (idFacultad,nombre, region) VALUES (1,'Facultad de Estadística e Informática',1),(2,'Derecho',1),(3,'Arquitectura',3);");
    }

    @AfterAll
    static void limpiarBaseDatos () {
        borrarDatosTablaFacultad();
        borrarDatosTablaRegion();
    }

    @Test
    void pruebaGetFacultadPorNombreExitosa () {
        FacultadDTO facultadEsperada = new FacultadDTO(1,"Facultad de Estadística e Informática",1);
        Optional<FacultadDTO> facultadObtenida = Optional.empty();
        try {
            facultadObtenida = FACULTAD_AUXILIAR.getFacultadPorNombre(facultadEsperada.getNombre());
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }
        assertEquals(facultadEsperada,facultadObtenida.get(),"pruebaGetFacultadPorNombreExitosa");
    }

    @Test
    void pruebaGetFacultadPorNombreCadenaInvalida () {
        try {
            Optional<FacultadDTO> facultadObtenida = FACULTAD_AUXILIAR.getFacultadPorNombre("   ");
            assertTrue(facultadObtenida.isEmpty(),"pruebaGetFacultadPorNombreCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreCadenaInvalida");
        }
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        try {
            Optional<FacultadDTO> facultadObtenida = FACULTAD_AUXILIAR.getFacultadPorNombre("Argentina");
            assertTrue(facultadObtenida.isEmpty(),"pruebaGetFacultadPorNombreInexistente");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
    }

    @Test
    void pruebaGetFacultadPorRegionExitosa () {
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(1,"Facultad de Estadística e Informática",1));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));
        try {
            listaObtenida = FACULTAD_AUXILIAR.getFacultadesPorRegion("Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionExitosa");
        }
        assertEquals(listaEsperada,listaObtenida,"pruebaGetFacultadPorRegionExitosa");
    }

    @Test
    void pruebaGetFacultadPorRegionCadenaInvalida () {
        try {
            List<FacultadDTO> facultadObtenida = FACULTAD_AUXILIAR.getFacultadesPorRegion(null);
            assertTrue(facultadObtenida.isEmpty(),"pruebaGetFacultadPorRegionCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("pruebaGetFacultadPorRegionCadenaInvalida");
        }
    }

    @Test
    void pruebaGetFacultadPorRegionInexistente () {
        try {
            List<FacultadDTO> facultadObtenida = FACULTAD_AUXILIAR.getFacultadesPorRegion("Sur");
            assertTrue(facultadObtenida.isEmpty(),"pruebaGetFacultadPorRegionInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionInexistente");
        }
    }
}