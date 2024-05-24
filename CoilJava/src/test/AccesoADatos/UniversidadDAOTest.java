package test.AccesoADatos;

import DAO.UniversidadDAO;
import DTO.UniversidadDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.*;

class UniversidadDAOTest {
    private final UniversidadDAO UNIVERSIDAD_DAO = new UniversidadDAO();
    @BeforeAll
    static void beforeAll () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
    }

    @BeforeEach
    void setUp () {
        borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void afterAll () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
    }

    @Test
    void pruebaRegistrarUniversidadExitoso () {
        System.out.println();
        UniversidadDTO universidadDTO = new UniversidadDTO("UNAM",1);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = UNIVERSIDAD_DAO.registrarUniversidad(universidadDTO);
        }
        catch (SQLException error) {
            fail("Fallida: registrarUniversidadExitoso");
        }
        assertEquals(esperado,obtenido,"pruebaRegistrarUniversidadExitoso");
    }

    @Test
    void pruebaRegistrarUniversidadVaciaFallida () {
        UniversidadDTO universidadDTO = new UniversidadDTO();
        assertThrows(SQLException.class,() -> UNIVERSIDAD_DAO.registrarUniversidad(universidadDTO),"pruebaRegistrarUniversidadVaciaFallida");
    }

    @Test
    void pruebaRegistrarUniversidadIncorrecta () {
        UniversidadDTO universidadDTO = new UniversidadDTO("Universidad Veracruzana",10);
        assertThrows(SQLException.class,()-> UNIVERSIDAD_DAO.registrarUniversidad(universidadDTO),"pruebaRegistrarUniversidadIncorrecta");
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        UniversidadDTO universidadDTO = new UniversidadDTO(3,"Benemérita Universidad de Puebla",2);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = UNIVERSIDAD_DAO.editarUniversidad(universidadDTO);
        }
        catch (SQLException error) {
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
        catch (SQLException error) {
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
        catch (SQLException error) {
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
        catch (SQLException error) {
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
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreNulo () {
        try {
            Optional<UniversidadDTO> obtenida = UNIVERSIDAD_DAO.getUniversidadPorNombre(null);
            assertTrue(obtenida.isEmpty(),"pruebaGetUniversidadPorNombreNulo");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));

        try {
           listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen("México");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        for (UniversidadDTO universidad : listaEsperada) {
            assertEquals(universidad,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenInexistente () {
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorPaisOrigen("");
        }
        catch (SQLException error) {
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
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenNulo");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadPorPaisOrigenNulo");
    }

    @Test
    void getUniversidadesPorNombreExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));

        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorNombre("U");
        }
        catch (SQLException error) {
            fail("Fallida: getUniversidadesPorNombreExitosa");
        }

        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"getUniversidadesPorNombreExitosa");
    }

    @Test
    void getUniversidadesPorNombreInexistente () {
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = UNIVERSIDAD_DAO.getUniversidadesPorNombre("X");
        }
        catch (SQLException error) {
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
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadesPorNombreNulo");
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorNombreNulo");
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("getTodasAlfabeticamente");
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));
        listaEsperada.add(new UniversidadDTO(2,"Harvard",2));
        listaEsperada.add(new UniversidadDTO(1,"UniversidadD Veracruzana",1));

        try {
            listaObtenida = UNIVERSIDAD_DAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: getTodasAlfabeticamente");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        for (UniversidadDTO universidad : listaEsperada) {
            assertEquals(universidad,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

}