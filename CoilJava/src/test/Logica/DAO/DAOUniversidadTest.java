package test.Logica.DAO;

import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;
import static org.junit.jupiter.api.Assertions.*;


class DAOUniversidadTest {
    private final DAOUniversidad INSTANCIA = new DAOUniversidad();

    @BeforeEach
    void setUp() {
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("DELETE FROM paises;");
        ejecutarInstruccionSQL("INSERT INTO paises (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void afterAll () {
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("DELETE FROM paises;");
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
    void editarUniversidad () {

    }

    @Test
    void getUniversidadPorNombre () {

    }

    @Test
    void getUniversidadesPorPaisOrigen () {

    }

    @Test
    void getTodasAlfabeticamente () {

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