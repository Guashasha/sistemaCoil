package test.DAO;

import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
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

class UniversidadAuxiliarTest {
    private final UniversidadAuxiliar UNIVERSIDAD_AUXILIAR = new UniversidadAuxiliar();

    @BeforeAll
    static void prepararBaseDatos () {
        borrarDatosTodasLasTablas();
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
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
    void pruebaRegistrarUniversidadExitosa () {
        int filasAfectadasObtnidas = 0;
        int filasAfectadasEsperadas = 1;
        try {
            filasAfectadasObtnidas = UNIVERSIDAD_AUXILIAR.registrarUniversidad(new UniversidadDTO("UNAM"),new PaisDTO("México"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRegistrarUniversidadExitosa");
        }
        assertEquals(filasAfectadasEsperadas,filasAfectadasObtnidas,"pruebaRegistrarUniversidadExitosa");
    }

    @Test
    void pruebaRegistrarUniversidadCadenasInvalida () {
        assertThrows(ErrorDAO.class,()->UNIVERSIDAD_AUXILIAR.registrarUniversidad(new UniversidadDTO("   "),new PaisDTO("")),"pruebaRegistrarUniversidadCadenasInvalida");
    }

    @Test
    void pruebaRegistrarUniversidadExistente () {
        assertThrows(ErrorDAO.class,() -> UNIVERSIDAD_AUXILIAR.registrarUniversidad(new UniversidadDTO("Universidad Veracruzana"),new PaisDTO("México")),"pruebaRegistrarUniversidadExistente");
    }

    @Test
    void pruebaRegistrarUniversidadPaisInexistente () {
        assertThrows(ErrorDAO.class,()-> UNIVERSIDAD_AUXILIAR.registrarUniversidad(new UniversidadDTO("UNAM"),new PaisDTO("Argentina")),"pruebaRegistrarUniversidadPaisInexistente");
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        int filasAfectadasEsperadas = 1;
        int filasAfectadasObtenidas = 0;
        try {
            filasAfectadasObtenidas = UNIVERSIDAD_AUXILIAR.editarUniversidad(new UniversidadDTO("Universidad Veracruzana"),new UniversidadDTO("UV"),new PaisDTO("México"));
        }
        catch (ErrorDAO error) {
            fail("pruebaEditarUniversidadExitosa");
        }
        assertEquals(filasAfectadasEsperadas,filasAfectadasObtenidas,"pruebaEditarUniversidadExitosa");
    }

    @Test
    void pruebaEditarUniversidadCadenasInvalidas () {
        assertThrows(ErrorDAO.class,()->UNIVERSIDAD_AUXILIAR.editarUniversidad(new UniversidadDTO("UV"),new UniversidadDTO(""), new PaisDTO("   ")),"pruebaEditarUniversidadCadenasInvalidas");
    }

    @Test
    void pruebaEditarUniversidadConDatosExistente () {
        assertThrows(ErrorDAO.class,()->UNIVERSIDAD_AUXILIAR.editarUniversidad(new UniversidadDTO("Universidad Veracruzana"),new UniversidadDTO("Harvard"),new PaisDTO("Estados Unidos")),"pruebaEditarUniversidadConDatosExistente");
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        assertThrows(ErrorDAO.class,()->UNIVERSIDAD_AUXILIAR.editarUniversidad(new UniversidadDTO("UNAM"),new UniversidadDTO("Universidad Autónoma"),new PaisDTO("México")),"pruebaEditarUniversidadInexistente");
    }

    @Test
    void pruebaEditarUniversidadConPaisInexistente () {
        assertThrows(ErrorDAO.class,()-> UNIVERSIDAD_AUXILIAR.editarUniversidad(new UniversidadDTO("Harvard"),new UniversidadDTO("Oxford"),new PaisDTO("Inglaterra")),"pruebaEditarUniversidadConPaisInexistente");
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        UniversidadDTO universidadEsperada = new UniversidadDTO(3,"BUAP",1);
        Optional<UniversidadDTO> universidadObtenida = Optional.empty();
        try {
            universidadObtenida = UNIVERSIDAD_AUXILIAR.getUniversidadPorNombre(universidadEsperada.getNombre());
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(universidadEsperada,universidadObtenida.get(),"pruebaGetUniversidadPorNombreExitosa");
    }

    @Test
    void pruebaGetUniversidadPorNombreCadenaVacia () {
        assertThrows(ErrorDAO.class,()->UNIVERSIDAD_AUXILIAR.getUniversidadPorNombre("  "),"pruebaGetUniversidadPorNombreCadenaVacia");
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        try {
            Optional<UniversidadDTO> universidadObtenida = UNIVERSIDAD_AUXILIAR.getUniversidadPorNombre("UNAM");
            assertTrue(universidadObtenida.isEmpty(),"pruebaGetUniversidadPorNombreInexistente");
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreInexistente");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));

        try {
            listaObtenida = UNIVERSIDAD_AUXILIAR.getUniversidadesPorPaisOrigen("México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEquals(listaEsperada,listaObtenida,"pruebaGetUniversidadesPorPaisOrigenExitosa");
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenCadenaInvalida () {
        try {
            List<UniversidadDTO> listaObtenida = UNIVERSIDAD_AUXILIAR.getUniversidadesPorPaisOrigen("  ");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenInexistente () {
        try {
            List<UniversidadDTO> listaObtenida = UNIVERSIDAD_AUXILIAR.getUniversidadesPorPaisOrigen("Veracruz");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorPaisOrigenInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenInexistente");
        }
    }

    @Test
    void getUniversidadesPorNombreExitosa () {
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"Universidad Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));
        try {
            listaObtenida = UNIVERSIDAD_AUXILIAR.getUniversidadesPorNombre(new UniversidadDTO("U"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: getUniversidadesPorNombreExitosa\n" + error.getMessage());
        }
        assertEquals(listaEsperada,listaObtenida,"getUniversidadesPorNombreExitosa");
    }

    @Test
    void getUniversidadesPorNombreCadenaVacia () {
        assertThrows(ErrorDAO.class,()-> UNIVERSIDAD_AUXILIAR.getUniversidadesPorNombre(new UniversidadDTO("   ")),"getUniversidadesPorNombreCadenaVacia");
    }

    @Test
    void getUniversidadesPorNombreCadenaNula () {
        assertThrows(ErrorDAO.class,()-> UNIVERSIDAD_AUXILIAR.getUniversidadesPorNombre(new UniversidadDTO(null)),"getUniversidadesPorNombreCadenaNula");
    }

    @Test
    void getUniversidadesPorNombreObjetoNulo () {
        assertThrows(ErrorDAO.class,()-> UNIVERSIDAD_AUXILIAR.getUniversidadesPorNombre(null),"getUniversidadesPorNombreObjetoNulo");
    }

    @Test
    void pruebaUniversidadExisteExitosa () {
        boolean existeUniversidad = false;
        try {
            existeUniversidad = UNIVERSIDAD_AUXILIAR.universidadExiste("Harvard","Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteExitosa");
        }
        assertTrue(existeUniversidad,"pruebaUniversidadExisteExitosa");
    }

    @Test
    void pruebaUniversidadExisteUniversidadNula () {
        try {
            assertFalse(UNIVERSIDAD_AUXILIAR.universidadExiste(null,"Estados Unidos"),"pruebaUniversidadExistePaisNulo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteUniversidadNula\n" + error.getMessage());
        }
    }

    @Test
    void pruebaUniversidadExistePaisNulo () {
        try {
            assertFalse(UNIVERSIDAD_AUXILIAR.universidadExiste("Harvard",null),"pruebaUniversidadExistePaisNulo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteUniversidadNula\n" + error.getMessage());
        }
    }

    @Test
    void pruebaUniversidadExisteUniversidadInexistente () {
        boolean existeUniversidad = true;
        try {
            existeUniversidad = UNIVERSIDAD_AUXILIAR.universidadExiste("UNAM","México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteUniversidadInexistente");
        }
        assertFalse(existeUniversidad,"pruebaUniversidadExisteUniversidadInexistente");
    }

    @Test
    void pruebaUniversidadExistePaisDiferente () {
        boolean existeUniversidad = true;
        try {
            existeUniversidad = UNIVERSIDAD_AUXILIAR.universidadExiste("Universidad Veracruzana","Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExistePaisDiferente");
        }
        assertFalse(existeUniversidad,"pruebaUniversidadExistePaisDiferente");
    }
}