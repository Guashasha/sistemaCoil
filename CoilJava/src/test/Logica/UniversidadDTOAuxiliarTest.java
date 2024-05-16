package test.Logica;

import DAO.UniversidadDAO;
import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.*;

class UniversidadDTOAuxiliarTest {
    private static final UniversidadAuxiliar INSTANCIA = new UniversidadAuxiliar();

    @BeforeEach
    void setUp() {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'UniversidadDTO Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void afterAll () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
    }

    @Test
    void pruebaRegistrarUniversidadExitosa () {
        int filasAfectadas = 0;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad(new UniversidadDTO("UNAM"),new PaisDTO("México"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRegistrarUniversidadExitosa");
        }
        assertEquals(1,filasAfectadas,"pruebaRegistrarUniversidadExitosa");
    }

    @Test
    void pruebaRegistrarUniversidadCadenasInvalida () {
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.registrarUniversidad(new UniversidadDTO("   "),new PaisDTO("")),"pruebaRegistrarUniversidadCadenasInvalida");
    }

    @Test
    void pruebaRegistrarUniversidadExistente () {
        assertThrows(ErrorDAO.class,() -> this.INSTANCIA.registrarUniversidad(new UniversidadDTO("UniversidadDTO Veracruzana"),new PaisDTO("México")),"pruebaRegistrarUniversidadExistente");
    }

    @Test
    void pruebaRegistrarUniversidadPaisInexistente () {
        assertThrows(ErrorDAO.class,()-> this.INSTANCIA.registrarUniversidad(new UniversidadDTO("UNAM"),new PaisDTO("Argentina")),"pruebaRegistrarUniversidadPaisInexistente");
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = INSTANCIA.editarUniversidad(new UniversidadDTO("UniversidadDTO Veracruzana"),new UniversidadDTO("UV"),new PaisDTO("México"));
        }
        catch (ErrorDAO error) {
            fail("pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido,"pruebaEditarUniversidadExitosa");
    }

    @Test
    void pruebaEditarUniversidadCadenasInvalidas () {
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.editarUniversidad(new UniversidadDTO("UV"),new UniversidadDTO(""), new PaisDTO("   ")),"pruebaEditarUniversidadCadenasInvalidas");
    }

    @Test
    void pruebaEditarUniversidadConDatosExistente () {
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.editarUniversidad(new UniversidadDTO("UniversidadDTO Veracruzana"),new UniversidadDTO("Harvard"),new PaisDTO("Estados Unidos")),"pruebaEditarUniversidadConDatosExistente");
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        int esperado = 0;
        int obtenido = 1;
        try {
            obtenido = INSTANCIA.editarUniversidad(new UniversidadDTO("UNAM"),new UniversidadDTO("UniversidadDTO Autonoma"),new PaisDTO("México"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }
        assertEquals(esperado,obtenido,"pruebaEditarUniversidadInexistente");
    }

    @Test
    void pruebaEditarUniversidadConPaisInexistente () {
        assertThrows(ErrorDAO.class,()->INSTANCIA.editarUniversidad(new UniversidadDTO("Harvard"),new UniversidadDTO("Oxford"),new PaisDTO("Inglaterra")),"pruebaEditarUniversidadConPaisInexistente");
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        UniversidadDTO esperada = new UniversidadDTO(3,"BUAP",1);
        UniversidadDTO obtenida = null;
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre(esperada.getNombre());
            obtenida = (UniversidadDTO) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertTrue(esperada.equals(obtenida),"pruebaGetUniversidadPorNombreExitosa");
    }

    @Test
    void pruebaGetUniversidadPorNombreCadenaVacia () {
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre("  ");
            assertTrue(resultado.isEmpty(),"pruebaGetUniversidadPorNombreCadenaVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreCadenaVacia");
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        UniversidadDTO obtenido = null;
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre("UNAM");
            obtenido = (UniversidadDTO) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreInexistente");
        }
        assertEquals(0,obtenido.getId(),"pruebaGetUniversidadPorNombreInexistente");
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(1,"UniversidadDTO Veracruzana",1));
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));

        try {
            listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("México");
        }
        catch (ErrorDAO error) {
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
    void pruebaGetUniversidadesPorPaisOrigenCadenaInvalida () {
        try {
            List<UniversidadDTO> listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("  ");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenInexistente () {
        try {
            List<UniversidadDTO> listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("Veracruz");
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
        listaEsperada.add(new UniversidadDTO(1,"UniversidadDTO Veracruzana",1));
        try {
            listaObtenida = INSTANCIA.getUniversidadesPorNombre(new UniversidadDTO("U"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: getUniversidadesPorNombreExitosa");
        }
        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"getUniversidadesPorNombreExitosa");
    }

    @Test
    void getUniversidadesPorNombreCadenaVacia () {
        assertThrows(ErrorDAO.class,()->INSTANCIA.getUniversidadesPorNombre(new UniversidadDTO("   ")));
    }

    @Test
    void getUniversidadesPorNombreCadenaNula () {
        assertThrows(ErrorDAO.class,()->INSTANCIA.getUniversidadesPorNombre(new UniversidadDTO(null)));
    }

    @Test
    void getUniversidadesPorNombreObjetoNulo () {
        assertThrows(ErrorDAO.class,()->INSTANCIA.getUniversidadesPorNombre(null));
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("pruebaGetTodasAlfabeticamenteExitosa");
        List<UniversidadDTO> listaEsperada = new ArrayList<>();
        List<UniversidadDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(new UniversidadDTO(3,"BUAP",1));
        listaEsperada.add(new UniversidadDTO(2,"Harvard",2));
        listaEsperada.add(new UniversidadDTO(1,"UniversidadDTO Veracruzana",1));

        try {
            listaObtenida = UniversidadDAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetTodasAlfabeticamenteExitosa");
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
    void pruebaEsNuloVerdadero () {
        System.out.println("pruebaEsNuloVerdadero");
        boolean resultado = this.INSTANCIA.esNulo(null);
        assertTrue(resultado);
    }

    @Test
    void pruebaEsNuloFalso () {
        System.out.println("pruebaEsNuloFalso");
        boolean resultado = this.INSTANCIA.esNulo(new Object());
        assertFalse(resultado);
    }

    @Test
    void pruebaCadenaValidaExitosa () {
        System.out.println("pruebaCadenaValidaExitosa");
        boolean resultado = this.INSTANCIA.cadenaValida("México");
        assertTrue(resultado);
    }

    @Test
    void pruebaCadenaValidaNula () {
        System.out.println("pruebaCadenaValidaNula");
        boolean resultado = this.INSTANCIA.cadenaValida(null);
        assertFalse(resultado);
    }

    @Test
    void pruebaCadenaValidaVacia () {
        System.out.println("pruebaCadenaValidaVacia");
        boolean resultado = this.INSTANCIA.cadenaValida(" ");
        assertFalse(resultado);
    }

    @Test
    void pruebaUniversidadExisteExitosa () {
        System.out.println("pruebaUniversidadExisteExitosa");
        boolean resultado = false;
        try {
            resultado = this.INSTANCIA.universidadExiste("Harvard","Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteExitosa");
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaUniversidadExisteUniversidadNula () {
        System.out.println("pruebaUniversidadExisteUniversidadNula");
        boolean resultado = true;
        try {
            resultado = this.INSTANCIA.universidadExiste(null,"Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteUniversidadNula");
        }
        assertFalse(resultado);
    }

    @Test
    void pruebaUniversidadExistePaisNulo () {
        System.out.println("pruebaUniversidadExistePaisNulo");
        boolean resultado = true;
        try {
            resultado = this.INSTANCIA.universidadExiste("Harvard",null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExistePaisNulo");
        }
        assertFalse(resultado);
    }

    @Test
    void pruebaUniversidadExisteUniversidadInexistente () {
        System.out.println("pruebaUniversidadExisteUniversidadInexistente");
        boolean resultado = true;
        try {
            resultado = this.INSTANCIA.universidadExiste("UNAM","México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExisteUniversidadInexistente");
        }
        assertFalse(resultado);
    }

    @Test
    void pruebaUniversidadExistePaisDiferente () {
        System.out.println("pruebaUniversidadExistePaisDiferente");
        boolean resultado = true;
        try {
            resultado = this.INSTANCIA.universidadExiste("UniversidadDTO Veracruzana","Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExistePaisDiferente");
        }
        assertFalse(resultado);
    }
}