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
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
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
        FacultadDTO esperada = new FacultadDTO(2,"Derecho",1);
        Optional<FacultadDTO> obtenido = Optional.empty();
        try {
            obtenido = FACULTAD_DAO.getFacultadPorNombre(esperada.getNombre());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreExitosa");
        }
        assertTrue(obtenido.isPresent());
        assertEquals(esperada,obtenido.get(),"pruebaGetFacultadPorNombreExitosa");
    }

    @Test
    void pruebaGetFacultadPorNombreInexistente () {
        Optional<FacultadDTO> resultado = Optional.empty();
        try {
            resultado = FACULTAD_DAO.getFacultadPorNombre("FEI");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
        assertTrue(resultado.isEmpty(),"pruebaGetFacultadPorNombreInexistente");
    }

    @Test
    void pruebaGetFacultadPorNombreNulo () {
        Optional<FacultadDTO> resultado = Optional.empty();
        try {
            resultado = FACULTAD_DAO.getFacultadPorNombre(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorNombreInexistente");
        }
        assertTrue(resultado.isEmpty(),"pruebaGetFacultadPorNombreInexistente");
    }

    @Test
    void pruebaGetFacultadPorRegionExitosa () {
        List<FacultadDTO> listaEsperada = new ArrayList<>();
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new FacultadDTO(1,"Facultad de Estadística e Informática",1));
        listaEsperada.add(new FacultadDTO(2,"Derecho",1));
        try {
            listaObtenida = FACULTAD_DAO.getFacultadPorRegion("Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionExitosa");
        }
        assertEquals(listaEsperada,listaObtenida,"pruebaGetFacultadPorRegionExitosa");
    }

    @Test
    void pruebaGetFacultadPorRegionInexistente () {
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = FACULTAD_DAO.getFacultadPorRegion("Coatepec");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionInexistente");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetFacultadPorRegionInexistente");
    }

    @Test
    void pruebaGetFacultadPorRegionNula () {
        List<FacultadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = FACULTAD_DAO.getFacultadPorRegion(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetFacultadPorRegionNula");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetFacultadPorRegionNula");
    }
}