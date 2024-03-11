package test.Logica;

import Logica.DAO.DAOAcademicoExterno;
import Logica.Dominio.AcademicoExterno;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DAOAcademicoExternoTest {
    private DAOAcademicoExterno instancia;
    private int nuevaCedula = 9862;

    @BeforeEach
    void setUP () {
        this.instancia = new DAOAcademicoExterno();
    }

    @Test
    void pruebaGetAcademicoExternoPorCedulaExitosa () {
        System.out.println("getAcademicoExternoPorCedula");
        AcademicoExterno academico = new AcademicoExterno();
        academico.setCedulaProfesional(0);
        int cedulaEsperada = 1234;
        int cedulaObtenida;

        try {
            academico = this.instancia.getAcademicoExternoPorCedula(1234);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        cedulaObtenida = academico.getCedulaProfesional();
        assertEquals(cedulaEsperada,cedulaObtenida);
    }

    @Test
    void pruebaGetAcademicoExternoPorCedulaNoEsta () {
        System.out.println("getAcademicoExternoPorCedula");
        AcademicoExterno academico = null;

        try {
            academico = this.instancia.getAcademicoExternoPorCedula(123);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertNull(academico);
    }

    @Test
    void pruebaGetAcademicoExternoPorCedulaNoNulo () {
        System.out.println("getAcademicoExternoPorCedula");
        AcademicoExterno academico = null;

        try {
            academico = this.instancia.getAcademicoExternoPorCedula(1234);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertNotNull(academico);
    }

    @Test
    void pruebaAgregarAcademicoExternoExitosa () {
        System.out.println("pruebaAgregarAcademicoExternoExitosa()");
        AcademicoExterno academico = new AcademicoExterno();
        academico.setNombre("Emmanuel");
        academico.setApellidoPaterno("Pale");
        academico.setApellidoMaterno("Molina");
        academico.setCedulaProfesional(this.nuevaCedula);
        Universidad institucion = new Universidad();
        institucion.setNombre("Harvard");
        institucion.setPaisOrigen("USA");
        academico.setInstitucion(institucion);
        academico.setAreaEstudios("Administracion");
        academico.setCorreoElectronico("epalemolina@hotmail.com");
        academico.setNumeroTelefono("2282353522");
        int esperado = 4;
        int obtenido = 0;

        try {
            obtenido = this.instancia.agregarAcademicoExterno(academico);
        } catch (ErrorDAO errorDAO) {
            fail(errorDAO.getMensaje());
        }

        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaAgregarAcademicoExternoFallida1Fila () {

    }

    @Test
    void pruebaAgregarAcademicoExternoFallida2Filas () {

    }

    @Test
    void pruebaAgregarAcademicoExternoFallida3Filas () {

    }

    @Test
    void pruebaAgregarAcademicoExternoFallida4Filas () {

    }

    @Test
    void pruebaGetAcademicosPorUniversidadExitosa () {
    }

    @Test
    void pruebaGetAcademicosPorUniversidadNoVacio () {
    }

    @Test
    void pruebaGetAcademicosPorUniversidadVacio () {
    }

    @Test
    void convertirListaAcademicos () {
    }

    @Test
    void convertirAcademicoExterno () {
    }
}