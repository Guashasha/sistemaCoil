package test.DAO;

import DAO.FacultadDAO;
import DTO.FacultadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;
import static org.junit.jupiter.api.Assertions.*;

class FacultadDAOTest {
    private final FacultadDAO FACULTAD_DAO = new FacultadDAO();

    @BeforeAll
    static void prepararBaseDatos () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
        ejecutarInstruccionSQL("INSERT INTO region (idRegion,nombre) VALUES (1,'Xalapa'), (2,'Veracruz'), (3,'Orizaba-Córdoba');");
        ejecutarInstruccionSQL("INSERT INTO facultad (idFacultad,nombre, region) VALUES (1,'Facultad de Estadística e Informática',1),(2,'Derecho',1),(3,'Arquitectura',3);");

    }

    @AfterAll
    static void limpiarBaseDatos () {
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
    }

    @Test
    void pruebaGetFacultadPorNombreExitosa () {
        FacultadDTO facultadEsperada = new FacultadDTO(2,"Derecho",1);
        Optional<FacultadDTO> facultadObtenida = Optional.empty();
        try {
            facultadObtenida = FACULTAD_DAO.getFacultadPorNombre(facultadEsperada.getNombre());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }
        assertEquals(facultadEsperada,facultadObtenida.get(),"pruebaGetFacultadPorNombreExitosa");
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        Optional<FacultadDTO> facultadObtenida = Optional.empty();
        try {
            facultadObtenida = FACULTAD_DAO.getFacultadPorNombre("FEI");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
        assertTrue(facultadObtenida.isEmpty(),"pruebaGetFacultadPorNombreInexistente");
    }

    @Test
    void pruebaGetFacultadPorNombreNulo () {
        Optional<FacultadDTO> facultadObtenida = Optional.empty();
        try {
            facultadObtenida = FACULTAD_DAO.getFacultadPorNombre(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
        assertTrue(facultadObtenida.isEmpty(),"pruebaGetFacultadPorNombreInexistente");
    }

    @Test
    void pruebaGetFacultadesPorRegionExitosa () {
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(1,"Facultad de Estadística e Informática",1));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));
        try {
            listaObtenida = FACULTAD_DAO.getFacultadesPorRegion("Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadesPorRegionExitosa");
        }
        assertEquals(listaEsperada,listaObtenida,"pruebaGetFacultadesPorRegionExitosa");
    }

    @Test
    void pruebaGetFacultadesPorRegionInexistente () {
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = FACULTAD_DAO.getFacultadesPorRegion("Coatepec");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadesPorRegionInexistente");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetFacultadesPorRegionInexistente");
    }

    @Test
    void pruebaGetFacultadesPorRegionNula () {
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = FACULTAD_DAO.getFacultadesPorRegion(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadesPorRegionNula");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetFacultadesPorRegionNula");
    }
}