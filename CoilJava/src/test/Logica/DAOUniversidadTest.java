package test.Logica;

import AccesoADatos.UniversidadDB;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.*;

class DAOUniversidadTest {
    private static final DAOUniversidad INSTANCIA = new DAOUniversidad();

    @BeforeEach
    void setUp() {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void afterAll () {
        borrarDatosTablaUniversidad();
        borrarDatosTablaPais();
    }

    @Test
    void pruebaRegistrarUniversidadExitosa () {
        System.out.println("pruebaRegistrarUniversidadExitosa");
        int filasAfectadas = 0;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad("UNAM","México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRegistrarUniversidadExitosa");
        }
        assertEquals(1,filasAfectadas);
    }

    @Test
    void pruebaRegistrarUniversidadCadenasInvalida () {
        System.out.println("pruebaRegistrarUniversidadCadenasInvalida");
        int filasAfectadas = 1;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad("  ",null);
        }
        catch (ErrorDAO error) {
            fail("pruebaRegistrarUniversidadCadenasInvalida");
        }
        assertEquals(0,filasAfectadas);
    }

    @Test
    void pruebaRegistrarUniversidadExistente () {
        System.out.println("pruebaRegistrarUniversidadExistente");
        int filasAfectadas = 0;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad("Universidad Veracruzana","México");
        }
        catch (ErrorDAO error) {
            fail("pruebaRegistrarUniversidadExistente");
        }
        assertEquals(-1,filasAfectadas);
    }

    @Test
    void pruebaRegistrarUniversidadPaisInexistente () {
        System.out.println("pruebaRegistrarUniversidadPaisInexistente");
        assertThrows(ErrorDAO.class,()-> this.INSTANCIA.registrarUniversidad("UNAM","Argentina"));
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        System.out.println("pruebaEditarUniversidadExitosa");
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = INSTANCIA.editarUniversidad("Universidad Veracruzana","UV","México");
        }
        catch (ErrorDAO error) {
            fail("pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadCadenasInvalidas () {
        System.out.println("pruebaEditarUniversidadCadenasInvalidas");
        int esperado = 0;
        int obtenido = 1;
        try {
            obtenido = INSTANCIA.editarUniversidad("UV",null, "  ");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadCadenasInvalidas");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadConDatosExistente () {
        System.out.println("pruebaEditarUniversidadConDatosExistente");
        int esperado = -1;
        int obtenido = 0;
        try {
            obtenido = INSTANCIA.editarUniversidad("Universidad Veracruzana","Harvard","Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("pruebaEditarUniversidadConDatosExistente");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        System.out.println("pruebaEditarUniversidadInexistente");
        int esperado = 0;
        int obtenido = 1;
        try {
            obtenido = INSTANCIA.editarUniversidad("UNAM","Universidad Autonoma","México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadConPaisInexistente () {
        System.out.println("pruebaEditarUniversidadConPaisInexistente");
        assertThrows(ErrorDAO.class,()->INSTANCIA.editarUniversidad("Harvard","Oxford","Inglaterra"));
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        System.out.println("pruebaGetUniversidadPorNombreExitosa");
        Universidad esperada = new Universidad(3,"BUAP",1);
        Universidad obtenida = null;
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre(esperada.getNombre());
            obtenida = (Universidad) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertTrue(esperada.equals(obtenida));
    }

    @Test
    void pruebaGetUniversidadPorNombreCadenaVacia () {
        System.out.println("pruebaGetUniversidadPorNombreCadenaVacia");
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre("  ");
            assertTrue(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreCadenaVacia");
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreCadenaNula () {
        System.out.println("pruebaGetUniversidadPorNombreCadenaNula");
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre(null);
            assertTrue(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreCadenaNula");
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente ()   {
        System.out.println("pruebaGetUniversidadPorNombreInexistente");
        Universidad obtenido = null;
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre("UNAM");
            obtenido = (Universidad) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreInexistente");
        }
        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");
        List<Universidad> listaEsperada = new ArrayList<>();
        List<Universidad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Universidad(1,"Universidad Veracruzana",1));
        listaEsperada.add(new Universidad(3,"BUAP",1));

        try {
            listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("México");
        }
        catch (ErrorDAO error) {
            fail("pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            Universidad esperada = listaEsperada.get(0);
            assertTrue(esperada.equals(listaObtenida.get(0)));
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenCadenaInvalida () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        try {
            List<Universidad> listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen(null);
            assertTrue(listaObtenida.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenInexistente () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenInexistente");
        try {
            List<Universidad> listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("Veracruz");
            assertTrue(listaObtenida.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("pruebaGetUniversidadesPorPaisOrigenInexistente");
        }
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("pruebaGetTodasAlfabeticamenteExitosa");
        List<Universidad> listaEsperada = new ArrayList<>();
        List<Universidad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Universidad(3,"BUAP",1));
        listaEsperada.add(new Universidad(2,"Harvard",2));
        listaEsperada.add(new Universidad(1,"Universidad Veracruzana",1));

        try {
            listaObtenida = UniversidadDB.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetTodasAlfabeticamenteExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size());
        while (!listaEsperada.isEmpty()) {
            Universidad esperada = listaEsperada.get(0);
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
            resultado = this.INSTANCIA.universidadExiste("Universidad Veracruzana","Estados Unidos");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaUniversidadExistePaisDiferente");
        }
        assertFalse(resultado);
    }

    @Test
    void pruebaValidarCadenasExitosa () {
        System.out.println("pruebaValidarCadenasExitosa");
        String[] cadenas = new String[]{"Harvard","BUAP","Universidad Veracruzana","México"};
        boolean resultado = this.INSTANCIA.validarCadenas(cadenas);
        assertTrue(resultado);
    }

    @Test
    void pruebaValidarCadenasNulas () {
        System.out.println("pruebaValidarCadenasNulas");
        String[] cadenas = new String[]{"Harvard","BUAP","Universidad Veracruzana",null};
        boolean resultado = this.INSTANCIA.validarCadenas(cadenas);
        assertFalse(resultado);
    }

    @Test
    void pruebaValidarCadenasVacias () {
        System.out.println("pruebaValidarCadenasVacias");
        String[] cadenas = new String[]{"Harvard","BUAP","Universidad Veracruzana","    "};
        boolean resultado = this.INSTANCIA.validarCadenas(cadenas);
        assertFalse(resultado);
    }
}