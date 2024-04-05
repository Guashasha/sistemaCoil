package test.AccesoADatos;

import AccesoADatos.PaisDB;
import Logica.Dominio.Pais;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static test.AsercionListas.assertEqualListPais;

class PaisDBTest {
    private final PaisDB INSTANCIA = new PaisDB();

    @BeforeAll
    static void setUp() {
        ConfiguracionPrueba.ejecutarInstruccionSQL("DELETE FROM paises;");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO paises (idPais,Iso,nombre) VALUES (1,'MX','México'),  (2,'US','Estados Unidos'), (3,'BR','Brasil');");
    }

    @Test
    void pruebaPaisesAlfabeticamenteExitosa () {
        System.out.println("pruebaPaisesAlfabeticamenteExitosa");
        List<Pais> listaEsperada = new ArrayList<>();
        List<Pais> listaObtenida = new ArrayList<>();
        listaEsperada.add(new Pais(3,"BR","Brasil"));
        listaEsperada.add(new Pais(2,"US","Estados Unidos"));
        listaEsperada.add(new Pais(1,"MX","México"));

        try {
            listaObtenida = this.INSTANCIA.paisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaPaisesAlfabeticamenteExitosa");
        }

        assertEqualListPais(listaEsperada,listaObtenida);
    }

    @Test
    void pruebaGetPaisPorNombreExitosa () {
        System.out.println("pruebaGetPaisPorNombreExitosa");
        Pais esperado = new Pais(2,"US","Estados Unidos");
        Pais obtenido = new Pais();

        try {
            obtenido = this.INSTANCIA.getPaisPorNombre(esperado.getNombre());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }

        assertEquals(esperado.getId(),obtenido.getId());
        assertEquals(esperado.getIso(),obtenido.getIso());
        assertEquals(esperado.getNombre(),obtenido.getNombre());
    }

    @Test
    void pruebaGetPaisPorNombreInexistente () {
        System.out.println("pruebaGetPaisPorNombreInexistente");
        Pais obtenido = new Pais();

        try {
            obtenido = this.INSTANCIA.getPaisPorNombre("Argentina");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorNombreExitosa");
        }

        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorNombreNulo () {
        System.out.println("pruebaGetPaisPorNombreNulo");
        Pais obtenido = new Pais();

        try {
            obtenido = this.INSTANCIA.getPaisPorNombre(null);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPaisPorNombreNulo");
        }

        assertEquals(0,obtenido.getId());
    }

    @Test
    void pruebaGetPaisPorIdExitosa () {
        System.out.println("pruebaGetPaisPorIdExitosa");
        Pais esperado = new Pais(2,"US","Estados Unidos");
        Pais obtenido = new Pais();

        try {
            obtenido = this.INSTANCIA.getPaisPorId(esperado.getId());
        }
        catch (ErrorDAO error) {
            fail("Fallisa: pruebaGetPaisPorIdExitosa");
        }
        assertEquals(esperado.getId(),obtenido.getId());
        assertEquals(esperado.getIso(),obtenido.getIso());
        assertEquals(esperado.getNombre(),obtenido.getNombre());
    }

    @Test
    void pruebaGetPaisPorIdInexistente () {
        System.out.println("pruebaGetPaisPorIdInexistente");
        Pais obtenido = new Pais();

        try {
            obtenido = this.INSTANCIA.getPaisPorId(0);
        }
        catch (ErrorDAO error) {
            fail("Fallisa: pruebaGetPaisPorIdInexistente");
        }

        assertEquals(0,obtenido.getId());
    }
}