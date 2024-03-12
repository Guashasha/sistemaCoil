package test.Logica;

import Logica.DAO.DAOAcademico;
import Logica.Dominio.Academico;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOAcademicoTest {
    private Academico academico;
    private DAOAcademico instancia;

    @BeforeEach
    void setUP () {
        this.academico = new Academico();
        this.instancia = new DAOAcademico();
    }

    @Test
    void pruebaAcademicoRegistradoSiEsta () {
        System.out.println("academicoRegistrado");
        boolean resultado = false;

        try {
            resultado = this.instancia.academicoRegistrado(1234);
        } catch (ErrorDAO errorDAO){
            fail(errorDAO.getMensaje());
        }

        assertTrue(resultado);
    }

    @Test
    void pruebaAcademicoRegistradoNoEsta () {
        System.out.println("academicoRegistrado");
        boolean resultado = true;

        try {
            resultado = this.instancia.academicoRegistrado(1000);
        } catch (ErrorDAO errorDAO){
            fail(errorDAO.getMensaje());
        }

        assertFalse(resultado);
    }

    @Test
    void pruebGetAcademicosPorAreaEstudiosExitosa () {
        System.out.println("getAcademicosPorAreaEstudios");
        List<Academico> resultado = null;
        Academico academico1;
        int cedulaEsperada = 7893;
        int cedulaObtenida;

        try {
            resultado = this.instancia.getAcademicosPorAreaEstudios("Informática");
        } catch (ErrorDAO errorDAO){
            fail(errorDAO.getMensaje());
        }

        academico1 = resultado.get(0);
        cedulaObtenida = academico1.getCedulaProfesional();
        assertEquals(cedulaEsperada,cedulaObtenida);
    }

    @Test
    void pruebaGetAcademicosPorAreaEstudiosNoVacio () {
        System.out.println("getAcademicosPorAreaEstudios");
        ArrayList<Academico> resultado = null;

        try {
            resultado = this.instancia.getAcademicosPorAreaEstudios("Ingeniería");
        } catch (ErrorDAO errorDAO){
            fail(errorDAO.getMensaje());
        }

        assertTrue(!resultado.isEmpty());
    }

    @Test
    void pruebaGetAcademicosPorAreaEstudiosVacio () {
        System.out.println("getAcademicosPorAreaEstudios");
        List<Academico> resultado = null;

        try {
            resultado = this.instancia.getAcademicosPorAreaEstudios("Ciencias politicas");
        } catch (ErrorDAO errorDAO){
            fail(errorDAO.getMensaje());
        }

        assertTrue(resultado.isEmpty());
    }

    @Test
    void pruebaCambiarCorreoElectronicoExitosa () {
        System.out.println("cambiarCorreoElectronico");
        int esperado = 1;
        int obtenido = 0;

        try {
            obtenido = this.instancia.cambiarCorreoElectronico("zs22013688@estudiantes.uv.mx",1234);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaCambiarCorreoElectronicoFallida () {
        System.out.println("cambiarCorreoElectronico");
        int esperado = 0;
        int obtenido = 1;

        try {
            obtenido = this.instancia.cambiarCorreoElectronico("zs22013688@estudiantes.uv.mx",123);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaCambiarTelefonoExitosa () {
        System.out.println("cambiarTelefono");
        int esperado = 1;
        int obtenido = 0;

        try {
            obtenido = this.instancia.cambiarTelefono("2283664636",1234);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaCambiarTelefonoFallida () {
        System.out.println("cambiarTelefono");
        int esperado = 0;
        int obtenido = 1;

        try {
            obtenido = this.instancia.cambiarTelefono("2283664636",123);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void convertirListaAcademicos() {
    }
}