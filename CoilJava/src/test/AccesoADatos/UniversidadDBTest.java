package test.AccesoADatos;

import AccesoADatos.UniversidadDB;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.AsercionListas.assertEqualListUniversidad;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;

class UniversidadDBTest {
    private final UniversidadDB INSTANCIA = new UniversidadDB();

    @BeforeAll
    static void beforeAll () {
        ejecutarInstruccionSQL("DELETE FROM paises;");
        ejecutarInstruccionSQL("INSERT INTO paises (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos');");
    }

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'Harvard',2), (3,'BUAP',1);");
    }

    @AfterAll
    static void arterAll () {
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ejecutarInstruccionSQL("DELETE FROM paises;");
    }

    @Test
    void pruebaRegistrarUniversidadExitoso () {
        System.out.println("pruebaRegistrarUniversidadExitoso");
        Universidad universidad = new Universidad("UNAM",1);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = this.INSTANCIA.registrarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: registrarUniversidadExitoso");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaRegistrarUniversidadVaciaFallida () {
        System.out.println("pruebaRegistrarUniversidadVaciaFallida");
        Universidad universidad = new Universidad();
        assertThrows(ErrorDAO.class,() -> this.INSTANCIA.registrarUniversidad(universidad));
    }

    @Test
    void pruebaRegistrarUniversidadIncorrecta () {
        System.out.println("pruebaRegistrarUniversidadIncorrecta");
        Universidad universidad = new Universidad("Universidad Veracruzana",10);
        assertThrows(ErrorDAO.class,()->this.INSTANCIA.registrarUniversidad(universidad));
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        System.out.println("pruebaEditarUniversidadExitosa");
        Universidad universidad = new Universidad(3,"Benemerita Universidad de Puebla",2);
        int esperado = 1;
        int obtenido = 0;
        try {
            obtenido = this.INSTANCIA.editarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadExitosa");
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadInexistente () {
        System.out.println("pruebaEditarUniversidadInexistente");
        Universidad universidad = new Universidad(10,"UV",1);
        int esperado = 0;
        int obtenido = 1;

        try {
            obtenido = this.INSTANCIA.editarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente");
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaEditarUniversidadVacia () {
        System.out.println("pruebaEditarUniversidadInexistente");
        Universidad universidad = new Universidad();
        int filasAfectadas = 1;

        try {
            filasAfectadas = this.INSTANCIA.editarUniversidad(universidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarUniversidadInexistente. Filas afectadas = " + filasAfectadas);
        }

        assertEquals(0,filasAfectadas);
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        System.out.println("pruebaGetUniversidadPorNombreExitosa");
        Universidad esperada = new Universidad(1,"Universidad Veracruzana",1);
        Universidad obtenida = new Universidad();
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre("Universidad Veracruzana");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(esperada.getId(),obtenida.getId());
        assertEquals(esperada.getNombre(),obtenida.getNombre());
        assertEquals(esperada.getIdPais(),obtenida.getIdPais());
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        System.out.println("pruebaGetUniversidadPorNombreInexistente");
        Universidad obtenida = new Universidad();
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre("VU");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetUniversidadPorNombreNulo () {
        System.out.println("pruebaGetUniversidadPorNombreNulo");
        Universidad obtenida = new Universidad();
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(0,obtenida.getId());
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");
        List<Universidad> listaEsperada = new ArrayList<>();
        List<Universidad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Universidad(1,"Universidad Veracruzana",1));
        listaEsperada.add(new Universidad(3,"BUAP",1));

        try {
           listaObtenida = this.INSTANCIA.getUniversidadesPorPaisOrigen("México");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadesPorPaisOrigenExitosa");
        }

        assertEqualListUniversidad(listaEsperada,listaObtenida);
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenInexistente () {
        System.out.println("pruebaGetUniversidadPorPaisOrigenInexistente");
        List<Universidad> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = this.INSTANCIA.getUniversidadesPorPaisOrigen("");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenInexistente");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetUniversidadPorPaisOrigenNulo () {
        System.out.println("pruebaGetUniversidadPorPaisOrigenNulo");
        List<Universidad> listaObtenida = new ArrayList<>();

        try {
            listaObtenida = this.INSTANCIA.getUniversidadesPorPaisOrigen(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorPaisOrigenNulo");
        }

        assertTrue(listaObtenida.isEmpty());
    }

    @Test
    void pruebaGetTodasAlfabeticamenteExitosa () {
        System.out.println("getTodasAlfabeticamente");
        List<Universidad> listaEsperada = new ArrayList<>();
        List<Universidad> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Universidad(3,"BUAP",1));
        listaEsperada.add(new Universidad(2,"Harvard",2));
        listaEsperada.add(new Universidad(1,"Universidad Veracruzana",1));

        try {
            listaObtenida = this.INSTANCIA.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: getTodasAlfabeticamente");
        }

        assertEqualListUniversidad(listaEsperada,listaObtenida);
    }

}