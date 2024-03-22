package test.AccesoADatos;

import AccesoADatos.UniversidadDB;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UniversidadDBTest {

    public final UniversidadDB INSTANCIA = new UniversidadDB();

    @BeforeEach
    void setUp() {

    }

    @Test
    void pruebaRegistrarUniversidadExitoso () {
        System.out.println("pruebaRegistrarUniversidadExitoso");
        Universidad universidad = new Universidad("MIT","USA");
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
        int filasAfectadas;
        try {
            filasAfectadas = this.INSTANCIA.registrarUniversidad(universidad);
            fail("Fallida: registrarUniversidadExitoso. Se afectaron " + filasAfectadas);
        }
        catch (ErrorDAO error) {
            assertNotNull(error);
        }
    }

    @Test
    void pruebaEditarUniversidadExitosa () {
        System.out.println("pruebaEditarUniversidadExitosa");
        Universidad universidad = new Universidad(1,"UV","México");
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
        Universidad universidad = new Universidad(0,"UV","México");
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
        int filasAfectadas;
        try {
            filasAfectadas = this.INSTANCIA.editarUniversidad(universidad);
            fail("Fallida: pruebaEditarUniversidadInexistente. Filas afectadas = " + filasAfectadas);
        }
        catch (ErrorDAO error) {
            assertNotNull(error);
        }
    }

    @Test
    void pruebaGetUniversidadPorNombreExitosa () {
        System.out.println("pruebaGetUniversidadPorNombreExitosa");
        Universidad esperada = new Universidad(2,"BUAP","México");
        Universidad obtenida = new Universidad();
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre("BUAP");
        }
        catch (ErrorDAO erro) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertEquals(esperada.getId(),obtenida.getId());
        assertEquals(esperada.getNombre(),obtenida.getNombre());
        assertEquals(esperada.getPaisOrigen(),obtenida.getPaisOrigen());
    }

    @Test
    void pruebaGetUniversidadPorNombreInexistente () {
        System.out.println("pruebaGetUniversidadPorNombreInexistente");
        Universidad obtenida = null;
        try {
            obtenida = this.INSTANCIA.getUniversidadPorNombre("VU");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetUniversidadPorNombreExitosa");
        }
        assertNull(obtenida);
    }

    @Test
    void pruebaGetUniversidadesPorPaisOrigenExitosa () {
        System.out.println("pruebaGetUniversidadesPorPaisOrigenExitosa");

        List<Universidad> esperada;

    }

    @Test
    void getTodasAlfabeticamente () {
        System.out.println();
    }
}