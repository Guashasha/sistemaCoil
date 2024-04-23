package test.Logica;

import AccesoADatos.UniversidadDB;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Pais;
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
        int filasAfectadas = 0;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad(new Universidad("UNAM"),new Pais("México"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRegistrarUniversidadExitosa");
        }
        assertEquals(1,filasAfectadas,"pruebaRegistrarUniversidadExitosa");
    }

    @Test
    void pruebaRegistrarUniversidadCadenasInvalida () {
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.registrarUniversidad(new Universidad("   "),new Pais("")),"pruebaRegistrarUniversidadCadenasInvalida");
    }

    @Test
    void pruebaRegistrarUniversidadExistente () {
        assertThrows(ErrorDAO.class,() -> this.INSTANCIA.registrarUniversidad(new Universidad("Universidad Veracruzana"),new Pais("México")),"pruebaRegistrarUniversidadExistente");
    }

    @Test
    void pruebaRegistrarUniversidadPaisInexistente () {
        assertThrows(ErrorDAO.class,()-> this.INSTANCIA.registrarUniversidad(new Universidad("UNAM"),new Pais("Argentina")),"pruebaRegistrarUniversidadPaisInexistente");
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = INSTANCIA.editarUniversidad(new Universidad("Universidad Veracruzana"),new Universidad("UV"),new Pais("México"));
        }
        catch (ErrorDAO error) {
            fail("pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido,"pruebaEditarUniversidadExitosa");
    }

    @Test
    void pruebaEditarUniversidadCadenasInvalidas () {
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.editarUniversidad(new Universidad("UV"),new Universidad(""), new Pais("   ")),"pruebaEditarUniversidadCadenasInvalidas");
    }

    @Test
    void pruebaEditarUniversidadConDatosExistente () {
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.editarUniversidad(new Universidad("Universidad Veracruzana"),new Universidad("Harvard"),new Pais("Estados Unidos")),"pruebaEditarUniversidadConDatosExistente");
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        int esperado = 0;
        int obtenido = 1;
        try {
            obtenido = INSTANCIA.editarUniversidad(new Universidad("UNAM"),new Universidad("Universidad Autonoma"),new Pais("México"));
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }
        assertEquals(esperado,obtenido,"pruebaEditarUniversidadInexistente");
    }

    @Test
    void pruebaEditarUniversidadConPaisInexistente () {
        assertThrows(ErrorDAO.class,()->INSTANCIA.editarUniversidad(new Universidad("Harvard"),new Universidad("Oxford"),new Pais("Inglaterra")),"pruebaEditarUniversidadConPaisInexistente");
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        Universidad esperada = new Universidad(3,"BUAP",1);
        Universidad obtenida = null;
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre(esperada.getNombre());
            obtenida = (Universidad) resultado.get();
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
        Universidad obtenido = null;
        try {
            Optional resultado = INSTANCIA.getUniversidadPorNombre("UNAM");
            obtenido = (Universidad) resultado.get();
        }
        catch (Exception error) {
            fail("Fallida: pruebaGetUniversidadPorNombreInexistente");
        }
        assertEquals(0,obtenido.getId(),"pruebaGetUniversidadPorNombreInexistente");
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
        try {
            List<Universidad> listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("  ");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
        catch (ErrorDAO error) {
            fail("pruebaGetUniversidadesPorPaisOrigenCadenaInvalida");
        }
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenInexistente () {
        try {
            List<Universidad> listaObtenida = INSTANCIA.getUniversidadesPorPaisOrigen("Veracruz");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetUniversidadesPorPaisOrigenInexistente");
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