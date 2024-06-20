package test.DAO;

import DAO.UniversidadDAO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.*;

class UniversidadDAOTest {
    private final UniversidadDAO UNIVERSIDAD_DAO = new UniversidadDAO();
    @BeforeAll
    static void prepararBaseDatos () {
        borrarDatosTodasLasTablas();
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'), (2,'US','Estados Unidos');");
    }

    @BeforeEach
    void reiniciarBaseDatos () {
        borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void limpiarBaseDatos () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
    }

    @Test
    void pruebaRegistrarUniversidadExitoso () {
        UniversidadDTO universidadDTO = new UniversidadDTO("UNAM",1);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = UNIVERSIDAD_DAO.registrarUniversidad(universidadDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: registrarUniversidadExitoso");
        }
        assertEquals(esperado,obtenido,"pruebaRegistrarUniversidadExitoso");
    }

    @Test
    void pruebaRegistrarUniversidadVaciaFallida () {
        UniversidadDTO universidadDTO = new UniversidadDTO();
        assertThrows(ErrorDAO.class,() -> UNIVERSIDAD_DAO.registrarUniversidad(universidadDTO),"pruebaRegistrarUniversidadVaciaFallida");
    }

    @Test
    void pruebaRegistrarUniversidadIncorrecta () {
        UniversidadDTO universidadDTO = new UniversidadDTO("Universidad Veracruzana",10);
        assertThrows(ErrorDAO.class,()-> UNIVERSIDAD_DAO.registrarUniversidad(universidadDTO),"pruebaRegistrarUniversidadIncorrecta");
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        UniversidadDTO universidadDTO = new UniversidadDTO(3,"Benemérita Universidad de Puebla",2);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = UNIVERSIDAD_DAO.editarUniversidad(universidadDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido,"pruebaEditarUniversidadExitosa");
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        UniversidadDTO universidadDTO = new UniversidadDTO(10,"UV",1);
        int esperado = 0;
        int obtenido = 1;

        try {
            obtenido = UNIVERSIDAD_DAO.editarUniversidad(universidadDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }

        assertEquals(esperado,obtenido,"pruebaEditarUniversidadInexistente");
    }

    @Test
    void pruebaEditarUniversidadVacia () {
        UniversidadDTO universidadDTO = new UniversidadDTO();
        int filasAfectadas = 1;

        try {
            filasAfectadas = UNIVERSIDAD_DAO.editarUniversidad(universidadDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente. Filas afectadas = " + filasAfectadas);
        }

        assertEquals(0,filasAfectadas,"pruebaEditarUniversidadInexistente");
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        UniversidadDTO esperada = new UniversidadDTO(1,"Universidad Veracruzana",1);
        Optional<UniversidadDTO> obtenida = Optional.empty();
        try {
            obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombre("Universidad Veracruzana");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertTrue(obtenida.isPresent());
        assertEquals(esperada,obtenida.get(),"pruebaGetUniversidadPorNombreExitosa");
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombre("VU");
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorNombreInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreNulo () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombre(null);
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorNombreNulo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));

        try {
           listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen("México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEquals(listaEsperada,listaObtenida,"pruebaGetUniversidadesPorPaisOrigenExitosa");
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenInexistente () {
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen("Argentina");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenInexistente");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadPorPaisOrigenInexistente");
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenNulo () {
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenNulo");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadPorPaisOrigenNulo");
    }

    @Test
    void getUniversidadesPorNombreExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));

        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorNombre("U");
        }
        catch (ErrorDAO error) {
            fail("Fallida: getUniversidadesPorNombreExitosa");
        }

        assertEquals(listaEsperada,listaObtenida,"getUniversidadesPorNombreExitosa");
    }

    @Test
    void getUniversidadesPorNombreInexistente () {
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorNombre("X");
        }
        catch (ErrorDAO ErrorDAO) {
            fail("Fallida: getUniversidadesPorNombreInexistente");
        }
        assertTrue(listaObtenida.isEmpty(),"getUniversidadesPorNombreInexistente");
    }

    @Test
    void pruebaGetUniversidadesPorNombreNulo () {
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorNombre(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorNombreNulo");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorNombreNulo");
    }

    @Test
    void pruebaGetUniversidadPorNombreYPaisExitosa () {
        UniversidadDTO esperado = new UniversidadDTO(2,"Harvard",2);
        Optional<UniversidadDTO> obtenida = Optional.empty();
        try {
            obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais(esperado.getNombre(),"Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreYPaisExitosa\n" + error.getMessage());
        }
        assertTrue(obtenida.isPresent());
        assertEquals(esperado,obtenida.get(),"pruebaGetUniversidadPorNombreYPaisExitosa");
    }

    @Test
    void pruebaGetUniversidadPorNombreYPaisInexistente () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais("UNAM","Argentina");
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorNombreYPaisInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreYPaisInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreYPaisNombreNulo () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais(null,"México");
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorNombreYPaisInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreYPaisInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreYPaisNulo () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombreYPais("Universidad Veracruzana",null);
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorNombreYPaisInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreYPaisInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetUniversidadPorIdExitosa () {
        UniversidadDTO esperado = new UniversidadDTO(2,"Harvard",2);
        Optional<UniversidadDTO> obtenida = Optional.empty();
        try {
            obtenida = UNIVERSIDAD_DAO.getUniversidadPorId(esperado.getId());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorIdExitosa\n" + error.getMessage());
        }
        assertTrue(obtenida.isPresent());
        assertEquals(esperado,obtenida.get(),"pruebaGetUniversidadPorIdExitosa");
    }

    @Test
    void pruebaGetUniversidadPorIdInexistente () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorId(10);
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorIdInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorIdInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetUniversidadPorIdNegativo () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorId(-10);
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorIdNegativo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorIdNegativo\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));
        listaEsperada.add(new UniversidadDTO(2,"Harvard",2));
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));

        try {
            listaObtenida = UNIVERSIDAD_DAO.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: getTodasAlfabeticamente");
        }

        assertEquals(listaEsperada,listaObtenida,"getTodasAlfabeticamente");
    }

}