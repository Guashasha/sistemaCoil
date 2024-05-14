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
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.*;

class UniversidadDTODAOTest {
    @BeforeAll
    static void beforeAll () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
    }

    @BeforeEach
    void setUp () {
        borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'UniversidadDTO Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void arterAll () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
    }

    @Test
    void pruebaRegistrarUniversidadExitoso () {
        System.out.println("pruebaRegistrarUniversidadExitoso");
        UniversidadDTO universidadDTO = new UniversidadDTO("UNAM",1);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = UniversidadDAO.registrarUniversidad(universidadDTO);
        }
        catch (SQLException error) {
            fail("Fallida: registrarUniversidadExitoso");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaRegistrarUniversidadVaciaFallida () {
        System.out.println("pruebaRegistrarUniversidadVaciaFallida");
        UniversidadDTO universidadDTO = new UniversidadDTO();
        assertThrows(SQLException.class,() -> UniversidadDAO.registrarUniversidad(universidadDTO));
    }

    @Test
    void pruebaRegistrarUniversidadIncorrecta () {
        System.out.println("pruebaRegistrarUniversidadIncorrecta");
        UniversidadDTO universidadDTO = new UniversidadDTO("UniversidadDTO Veracruzana",10);
        assertThrows(SQLException.class,()-> UniversidadDAO.registrarUniversidad(universidadDTO));
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        System.out.println("pruebaEditarUniversidadExitosa");
        UniversidadDTO universidadDTO = new UniversidadDTO(3,"Benemerita UniversidadDTO de Puebla",2);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = UniversidadDAO.editarUniversidad(universidadDTO);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        System.out.println("pruebaEditarUniversidadInexistente");
        UniversidadDTO universidadDTO = new UniversidadDTO(10,"UV",1);
        int esperado = 0;
        int obtenido = 1;

        try {
            obtenido = UniversidadDAO.editarUniversidad(universidadDTO);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadVacia () {
        System.out.println("pruebaEditarUniversidadInexistente");
        UniversidadDTO universidadDTO = new UniversidadDTO();
        int filasAfectadas = 1;

        try {
            filasAfectadas = UniversidadDAO.editarUniversidad(universidadDTO);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaEditarUniversidadInexistente. Filas afectadas = " + filasAfectadas);
        }

        assertEquals(0,filasAfectadas);
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        System.out.println("pruebaGetUniversidadPorNombreExitosa");
        UniversidadDTO esperada = new UniversidadDTO(1,"UniversidadDTO Veracruzana",1);
        UniversidadDTO obtenida = new UniversidadDTO();
        try {
            obtenida = UniversidadDAO.getUniversidadPorNombre("UniversidadDTO Veracruzana");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertTrue(esperada.equals(obtenida));
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        System.out.println("pruebaGetUniversidadPorNombreInexistente");
        UniversidadDTO obtenida = new UniversidadDTO();
        try {
            obtenida = UniversidadDAO.getUniversidadPorNombre("VU");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetUniversidadPorNombreNulo () {
        System.out.println("pruebaGetUniversidadPorNombreNulo");
        UniversidadDTO obtenida = new UniversidadDTO();
        try {
            obtenida = UniversidadDAO.getUniversidadPorNombre(null);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"UniversidadDTO Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));

        try {
           listaObtenida = UniversidadDAO.getUniversidadesPorPaisOrigen("México");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            UniversidadDTO esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenInexistente () {
        System.out.println("pruebaGetUniversidadPorPaisOrigenInexistente");
        List<UniversidadDTO> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = UniversidadDAO.getUniversidadesPorPaisOrigen("");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenInexistente");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenNulo () {
        System.out.println("pruebaGetUniversidadPorPaisOrigenNulo");
        List<UniversidadDTO> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = UniversidadDAO.getUniversidadesPorPaisOrigen(null);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenNulo");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void getUniversidadesPorNombreExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"UniversidadDTO Veracruzana",1));

        try {
            listaObtenida = UniversidadDAO.getUniversidadesPorNombre("U");
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
            listaObtenida = UniversidadDAO.getUniversidadesPorNombre("X");
        }
        catch (SQLException error) {
            fail("Fallida: getUniversidadesPorNombreInexistente");
        }
        assertTrue(listaObtenida.isEmpty(),"getUniversidadesPorNombreInexistente");
    }

    @Test
    void pruebaGetUniversidadesPorNombreNulo () {
        System.out.println();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = UniversidadDAO.getUniversidadesPorNombre(null);
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
        listaEsperada.add(new UniversidadDTO(1,"UniversidadDTO Veracruzana",1));

        try {
            listaObtenida = UniversidadDAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: getTodasAlfabeticamente");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            UniversidadDTO esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

}