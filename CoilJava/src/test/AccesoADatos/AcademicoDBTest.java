package test.AccesoADatos;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static test.ConfiguracionPrueba.*;

class AcademicoDBTest {
    private Academico academicoFilosofia;
    private Academico academicoComputacion;

    @BeforeAll
    static void beforeAll () {
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();

    }

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('ABC123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'Filosofia', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");
    }

    @AfterEach
    void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaGetListaAcademicoPorCamposFallida () {
        assertThrows(SQLException.class, () -> AcademicoDB.getListaAcademicoPorCampos("TipoContratacion", "Fijo"), "pruebaGetListaAcademicoPorCamposFallida");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoFacultadExitosa () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        Academico academico = new Academico();
        Academico academico2 = new Academico();
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setIdPersona(2);
        academico.setAreaEstudios("Filosofia");
        academico.setCorreoElectronico("esther@gmail.com");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Dramaturgo");
        academico.setIdFacultad(1);

        academico2.setCedulaProfesional("200011");
        academico2.setNumeroPersonal("4564");
        academico2.setIdPersona(2);
        academico2.setAreaEstudios("Filosofia");
        academico2.setCorreoElectronico("esther@gmail.com");
        academico2.setNumeroTelefonico("522288536230");
        academico2.setCategoriaContratacion("Dramaturgo");
        academico2.setIdFacultad(1);

        listaEsperada.add(academico2);
        listaEsperada.add(academico);
        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("facultad", "Economia");
        }
        catch (SQLException error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadExitosa");
        }

        assertEquals(listaEsperada.size(), listaObtenida.size(), "pruebaGetListaAcademicoPorCampoFacultadExitosa");
        for (Academico acad : listaEsperada) {
            assertEquals(acad, listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoFacultadVacia () {
        try {
            List<Academico> listaObtenida = AcademicoDB.getListaAcademicoPorCampos("facultad", "FEI");
            assertTrue(listaObtenida.isEmpty(), "pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
        catch (SQLException error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaExitosa () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        Academico academico = new Academico();
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setIdPersona(2);
        academico.setAreaEstudios("Filosofia");
        academico.setCorreoElectronico("esther@gmail.com");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Dramaturgo");
        academico.setIdFacultad(1);
        listaEsperada.add(academico);

        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("area", "Filosofia");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaExitosa");
        }

        assertFalse(listaObtenida.isEmpty(), "pruebaGetListaAcademicoPorCampoAreaExitosa");
        assertEquals(listaEsperada.get(0), listaObtenida.get(0), "pruebaGetListaAcademicoPorCampoAreaExitosa");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaVacia () {
        try {
            List<Academico> listaObtenida = AcademicoDB.getListaAcademicoPorCampos("area", "F");
            assertTrue(listaObtenida.isEmpty(), "pruebaGetListaAcademicoPorCampoAreaVacia");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaVacia");
        }
    }

    //HERE

    @Test
    void pruebaGetListaAcademicoPorCampoCategoriaContratacionExitosa () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        Academico academico = new Academico();
        academico.setCedulaProfesional("ABC123");
        academico.setNumeroPersonal("123456");
        academico.setIdPersona(1);
        academico.setAreaEstudios("Ciencias de la Computación");
        academico.setCorreoElectronico("jose@gmail.com");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Investigador");
        academico.setIdFacultad(1);
        listaEsperada.add(academico);

        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("categoria", "Investigador");
        }
        catch (SQLException error) {
            fail("Fallido: pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
        }

        assertTrue(!listaObtenida.isEmpty(), "pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
        assertEquals(listaEsperada.get(0), listaObtenida.get(0), "pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
    }

    @Test
    void pruebaGetAcademicoPorCampoUniversidadExitoso () {
        System.out.println("pruebaGetAcademicoPorCampoFacultadExitosa");
        List<Academico> listaAcademicos = null;

        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("universidad", "Universidad Veracruzana");
        }
        catch (SQLException error) {
            fail("pruebaGetAcademicoPorCampoFacultadExitosa");
        }

        assertNotNull(listaAcademicos);

    }

    @Test
    void pruebaGetAcademicoPorRegionExitoso () {
        System.out.println("pruebaGetAcademicoPorRegionExitoso");
        List<Academico> listaAcademicos = null;


        try {
            listaAcademicos = AcademicoDB.getListaAcademicoPorCampos("region", "Xalapa");
        }
        catch (SQLException error) {
            fail("pruebaGetAcademicoPorRegionExitoso");
        }

        assertNotNull(listaAcademicos);
    }

    @Test
    void pruebaAgregarAcademicoExitoso () {
        System.out.println("pruebaAgregarAcademicoExitoso");

        Academico academico = new Academico();
        academico.setNombre("Hernan");
        academico.setApellidoPaterno("Llamas");
        academico.setApellidoMaterno("Villa Señor");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("9877985");
        academico.setNumeroPersonal("34563");
        academico.setAreaEstudios("Economia");
        academico.setCorreoElectronico("hernan@Institucion.mx");
        academico.setNumeroTelefonico("523311756675");
        academico.setCategoriaContratacion("Por Horas");
        academico.setIdFacultad(1);

        int esperado = 2;
        int obtenido = 0;

        try {
            obtenido = AcademicoDB.agregarAcademicoUV(academico);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaAgregarAcademicoExitoso " + error.getMessage());
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaAgregarAcademicoVacioFallida () {
        System.out.println("pruebaAgregarAcademicoVacioFallida");
        Academico academico = new Academico();

        assertThrows(SQLException.class,
                     () -> AcademicoDB.agregarAcademicoUV(academico),
                     "Se esperaba que lanzara una excepción Utilidades.ErrorDAO debido a un mal registro");

    }

    @Test
    void pruebaAgregarAcademicoUniversidadInexistente () {
        System.out.println("pruebaAgregarAcademicoUniversidadInexistente");
        Academico academico = new Academico();
        academico.setNombre("Jose");
        academico.setApellidoPaterno("Andrei");
        academico.setApellidoMaterno("De la paz");
        academico.setIdUniversidad(5);
        academico.setCedulaProfesional("453432523");
        academico.setNumeroPersonal("2341");
        academico.setAreaEstudios("Humanidades");
        academico.setCorreoElectronico("Andrei@Institucion.mx");
        academico.setNumeroTelefonico("523351256655");
        academico.setIdFacultad(1);

        assertThrows(SQLException.class,
                     () -> AcademicoDB.agregarAcademicoUV(academico),
                     "Se esperaba que lanzara una excepción Utilidades.ErrorDAO debido a una universidad inexistente");

    }

    @Test
    void pruebaAgregarAcademicoNumeroTelefonoExtensoFallida () {
        System.out.println("pruebaAgregarAcademicoNumeroTelefonoExtensoFallida");
        Academico academico = new Academico();
        academico.setNombre("Ivan");
        academico.setApellidoPaterno("Ingram");
        academico.setApellidoMaterno("Lopez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("564635356");
        academico.setNumeroPersonal("2341");
        academico.setAreaEstudios("Ingenieria");
        academico.setCorreoElectronico("Ivan@Institucion.mx");
        academico.setNumeroTelefonico("523351256655567");
        academico.setIdFacultad(1);

        assertThrows(SQLException.class, () -> AcademicoDB.agregarAcademicoUV(academico));
    }

    @Test
    void pruebaAgregarAcademicoCedulaExtensaFallida () {
        System.out.println("pruebaAgregarAcademicoCedulaExtensaFallida");

        Academico academico = new Academico();
        academico.setNombre("Andrea");
        academico.setApellidoPaterno("Hernandez");
        academico.setApellidoMaterno("Tronque");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("456476923456742064791240676039603950312965603953");
        academico.setNumeroPersonal("456");
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("Andrea@Institucion.mx");
        academico.setNumeroTelefonico("522288536230");
        academico.setIdFacultad(1);

        assertThrows(SQLException.class, () -> AcademicoDB.agregarAcademicoUV(academico));

    }


    @Test
    void pruebaAgregarAcademicoCedulaNoPersonalDuplicadaFallida () {
        System.out.println("pruebaAgregarAcademicoCedulaDuplicadaFallida");

        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("Humanidades");
        academico.setCorreoElectronico("Esther@Institucion.mx");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Fijo");
        academico.setIdFacultad(1);

        assertThrows(SQLException.class, () -> AcademicoDB.agregarAcademicoUV(academico));

    }

    @Test
    void pruebaGetAcademicoPorCedulaExitosa () {
        System.out.println("pruebaGetAcademicoPorCedulaExitosa");

        Academico academicoEsperado = new Academico();
        academicoEsperado.setIdPersona(2);
        academicoEsperado.setNombre("Esther");
        academicoEsperado.setApellidoPaterno("Ramirez");
        academicoEsperado.setApellidoMaterno("Escobar");
        academicoEsperado.setIdUniversidad(1);
        academicoEsperado.setCedulaProfesional("200011");
        academicoEsperado.setNumeroPersonal("4564");
        academicoEsperado.setAreaEstudios("Humanidades");
        academicoEsperado.setCorreoElectronico("Esther@Institucion.mx");
        academicoEsperado.setNumeroTelefonico("522288536230");
        academicoEsperado.setCategoriaContratacion("Fijo");
        academicoEsperado.setIdFacultad(1);

        Academico academicoObtenido = null;

        try {
            academicoObtenido = AcademicoDB.getAcademicoPorCedula("200011");

        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitosa");

        }
        assertEquals(academicoEsperado.getIdPersona(), academicoObtenido.getIdPersona());
        assertEquals(academicoEsperado.getIdFacultad(), academicoObtenido.getIdFacultad());

    }

    @Test
    void pruebaGetAcademicoPorCedulaInexistenteFallida () {
        System.out.println("pruebaGetAcademicoPorCedulaInexistenteFallida");

        Academico academicoObtenido = null;

        try {
            academicoObtenido = AcademicoDB.getAcademicoPorCedula("123456");
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaInexistenteFallida");
        }
        assertNull(academicoObtenido);

    }

    @Test
    void pruebaGetAcademicoPorIdExitoso () {
        System.out.println("pruebaGetAcademicoPorIdExitoso");
        Academico academicoObtenido = null;

        try {
            academicoObtenido = AcademicoDB.getAcademicoPorId(1);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetAcademicoPorIdExitoso");
        }
        assertNotNull(academicoObtenido);
    }

    @Test
    void pruebaGetAcademicoPorIdFallido () {
        System.out.println("pruebaGetAcademicoPorIdFallido");
        Academico academicoObtenido = null;

        try {
            academicoObtenido = AcademicoDB.getAcademicoPorId(99);
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetAcademicoPorIdFallido");

        }

        assertNull(academicoObtenido);
    }


    @Test
    void pruebaEditarAcademicoExitoso () {
        int esperado = 3;
        int obtenido = 0;

        System.out.println("pruebaEditarAcademicoExitoso");
        Academico academico = new Academico();
        academico.setNombre("Fernando");
        academico.setApellidoPaterno("Hernandez");
        academico.setApellidoMaterno("Lopez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);

        try {
            obtenido = AcademicoDB.editarAcademico(academico);

        }
        catch (SQLException error) {
            fail("Fallida: pruebaEditarAcademicoExitoso");
        }
        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaGetTodosExitosa () {
        int tamanoEsperado = 2;

        System.out.println("pruebaGetTodosExitosa");

        List<Academico> listaAcademico = null;

        try {
            listaAcademico = AcademicoDB.getTodos();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetTodosExitosa");
        }

        assertEquals(tamanoEsperado, listaAcademico.size());
    }

    @Test
    void pruebaGetTodosFallida () {
        int tamanoEsperado = 0;
        System.out.println("pruebaGetTodosFallida");

        List<Academico> listaAcademico = null;

        try {
            listaAcademico = AcademicoDB.getTodos();
        }
        catch (SQLException error) {
            fail("Fallida: pruebaGetTodosExitosa");
        }

        assertNotEquals(tamanoEsperado, listaAcademico.size());

    }

    @Test
    void pruebaEditarAcademicoCedulaInexistenteFallida () {
        int esperado = 0;
        int obtenido = 0;

        System.out.println("pruebaEditarAcademicoCedulaInexistenteFallida");

        Academico academico = new Academico();
        academico.setNombre("Fernando");
        academico.setApellidoPaterno("Martinez");
        academico.setApellidoMaterno("Ramirez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("1");
        academico.setNumeroPersonal("1");
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);

        try {
            obtenido = AcademicoDB.editarAcademico(academico);

        }
        catch (SQLException error) {
            fail("Fallida: pruebaEditarAcademicoExitoso");
        }
        assertEquals(esperado, obtenido);

    }


    @Test
    void pruebaEditarAcademicoUniversidadInexistenteFallida () {

        System.out.println("pruebaEditarAcademicoCedulaInexistenteFallida");

        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(-7);
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("Humanidades");
        academico.setCorreoElectronico("Esther@Institucion.mx");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Fijo");
        academico.setIdFacultad(1);

        assertThrows(SQLException.class, () -> AcademicoDB.editarAcademico(academico));

    }

    @Test
    void pruebaEditarAcademicoFacultadInexistenteFallida () {

        System.out.println("pruebaEditarAcademicoFacultadInexistenteFallida");

        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("Dramaturgo");
        academico.setCorreoElectronico("esther@gmail.com");
        academico.setNumeroTelefonico("522288536230");
        academico.setIdFacultad(-5);

        assertThrows(SQLException.class, () -> AcademicoDB.editarAcademico(academico));

    }
}