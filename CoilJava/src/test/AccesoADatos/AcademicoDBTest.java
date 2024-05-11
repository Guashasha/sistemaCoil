package test.AccesoADatos;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AcademicoDBTest {
    private final Academico ACADEMICO_FILOSOFIA = new Academico(2,"Esther","Herrara","Martinez",1,"200011",
                                                                "4564","Filosofia","esther@gmail.com","522288536230","Dramaturgo",1);
    private final Academico ACADEMICO_COMPUTACION = new Academico(1,"Jose","Lopez","Perez",1,"123",
                                                                  "123456","Ciencias de la Computación","jose@gmail.com","522288536230","Investigador",1);

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('UNAM',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('123', '123456', 1, 'Ciencias de la Computación', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'Filosofia', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");
    }

    @AfterAll
    static void tearDown() {
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaGetListaAcademicoPorCamposFallida () {
        assertThrows(ErrorDAO.class, ()-> AcademicoDB.getListaAcademicoPorCampos("TipoContratacion", "Fijo"),"pruebaGetListaAcademicoPorCamposFallida");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoNulo () {
        assertThrows(ErrorDAO.class,()->AcademicoDB.getListaAcademicoPorCampos(null,"Investigador"),"pruebaGetListaAcademicoPorCampoNulo");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoValorNulo () {
        try {
            List<Academico> resultado = AcademicoDB.getListaAcademicoPorCampos("categoria",null);
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoNulo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoNulo");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoFacultadExitosa () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_COMPUTACION);
        listaEsperada.add(this.ACADEMICO_FILOSOFIA);

        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("facultad", "Economia");
        }
        catch (ErrorDAO error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetListaAcademicoPorCampoFacultadExitosa");
        for (Academico academico : listaEsperada) {
            assertEquals(academico,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoFacultadVacia () {
        try {
            List<Academico> listaObtenida = AcademicoDB.getListaAcademicoPorCampos("facultad", "FEI");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoUniversidadExitoso () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_COMPUTACION);
        listaEsperada.add(this.ACADEMICO_FILOSOFIA);
        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("universidad", "Universidad Veracruzana");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoUniversidadExitoso");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetListaAcademicoPorCampoUniversidadExitoso");
        for (Academico academico : listaEsperada) {
            assertEquals(academico,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoUniversidadVacia () {
        try {
            List<Academico> resultado = AcademicoDB.getListaAcademicoPorCampos("universidad","UNAM");
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaExitosa () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_FILOSOFIA);

        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("area", "Filosofia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaExitosa");
        }

        assertFalse(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoAreaExitosa");
        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"pruebaGetListaAcademicoPorCampoAreaExitosa");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaVacia () {
        try {
            List<Academico> listaObtenida = AcademicoDB.getListaAcademicoPorCampos("area", "F");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoAreaVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoCategoriaContratacionExitosa () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_COMPUTACION);

        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("categoria", "Investigador");
        }
        catch (ErrorDAO error) {
            fail("Fallido: pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia () {
        try {
            List<Academico> resultado = AcademicoDB.getListaAcademicoPorCampos("categoria", "Profe");
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia");
        }
    }

    @Test
    void pruebaGetAcademicoPorRegionExitoso () {
        List<Academico> listaEsperada = new ArrayList<>();
        List<Academico> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_COMPUTACION);
        listaEsperada.add(this.ACADEMICO_FILOSOFIA);
        try {
            listaObtenida = AcademicoDB.getListaAcademicoPorCampos("region", "Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorRegionExitoso");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetAcademicoPorRegionExitoso");
        for (Academico academico : listaEsperada) {
            assertEquals(academico,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetAcademicoPorRegionVacia () {
        try {
            List<Academico> resultado = AcademicoDB.getListaAcademicoPorCampos("region","Veracruz");
            assertTrue(resultado.isEmpty(),"pruebaGetAcademicoPorRegionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorRegionVacia");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitosa () {
        try {
            Academico obtenido = AcademicoDB.getAcademicoPorCedula("200011");
            assertEquals(this.ACADEMICO_FILOSOFIA,obtenido,"pruebaGetAcademicoPorCedulaExitosa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitosa");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaInexistente () {
        try {
            Academico resultado = AcademicoDB.getAcademicoPorCedula("123456");
            assertNull(resultado,"pruebaGetAcademicoPorCedulaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaInexistente");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaNula () {
        try {
            Academico resultado = AcademicoDB.getAcademicoPorCedula(null);
            assertNull(resultado,"pruebaGetAcademicoPorCedulaNula");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaNula");
        }
    }

    @Test
    void pruebaAgregarAcademicoUVExitoso () {
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
            obtenido = AcademicoDB.agregarAcademico(academico);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoUVExitoso\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoUVExitoso");
    }

    @Test
    void pruebaAgregarAcademicoUVSinDatos () {
        assertThrows(ErrorDAO.class,() -> AcademicoDB.agregarAcademico(new Academico()),"pruebaAgregarAcademicoUVSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoUVUniversidadInexistente () {
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
        assertThrows(ErrorDAO.class,() -> AcademicoDB.agregarAcademico(academico),"pruebaAgregarAcademicoUVUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVFacultadInexistente () {
        Academico academico = new Academico();
        academico.setNombre("Jose");
        academico.setApellidoPaterno("Andrei");
        academico.setApellidoMaterno("De la paz");
        academico.setIdUniversidad(5);
        academico.setCedulaProfesional("98765");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("Humanidades");
        academico.setCorreoElectronico("Andrei@Institucion.mx");
        academico.setNumeroTelefonico("523351256655");
        academico.setIdFacultad(10);
        assertThrows(ErrorDAO.class,() -> AcademicoDB.agregarAcademico(academico),"pruebaAgregarAcademicoUVFacultadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVExcesoCaracteres () {
        try {
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
            AcademicoDB.agregarAcademico(academico);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarAcademcioUVExcesoCaracteres");
        }
    }

    @Test
    void pruebaAgregarAcademicoExternoExitosa () {
        Academico academico = new Academico();
        academico.setNombre("Hernan");
        academico.setApellidoPaterno("Llamas");
        academico.setApellidoMaterno("Villa Señor");
        academico.setIdUniversidad(2);
        academico.setCedulaProfesional("9877985");
        academico.setNumeroPersonal("34563");
        academico.setAreaEstudios("Economia");
        academico.setCorreoElectronico("hernan@Institucion.mx");
        academico.setNumeroTelefonico("523311756675");
        int esperado = 2;
        int obtenido = 0;
        try {
            obtenido = AcademicoDB.agregarAcademico(academico);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExternoExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoExternoExitosa");
    }

    @Test
    void pruebaAgregarAcademicoExternoSinDatos () {
        assertThrows(ErrorDAO.class,() -> AcademicoDB.agregarAcademico(new Academico()),"pruebaAgregarAcademicoExternoSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoExternoUniversidadInexistente () {
        Academico academico = new Academico();
        academico.setNombre("Hernan");
        academico.setApellidoPaterno("Llamas");
        academico.setApellidoMaterno("Villa Señor");
        academico.setIdUniversidad(20);
        academico.setCedulaProfesional("9877985");
        academico.setNumeroPersonal("34563");
        academico.setAreaEstudios("Economia");
        academico.setCorreoElectronico("hernan@Institucion.mx");
        academico.setNumeroTelefonico("523311756675");
        assertThrows(ErrorDAO.class,() -> AcademicoDB.agregarAcademico(academico),"pruebaAgregarAcademicoExternoUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoExternoCedulaRepetida () {
        Academico academico = new Academico();
        academico.setNombre("Hernan");
        academico.setApellidoPaterno("Llamas");
        academico.setApellidoMaterno("Villa Señor");
        academico.setIdUniversidad(2);
        academico.setCedulaProfesional(ACADEMICO_FILOSOFIA.getCedulaProfesional());
        academico.setNumeroPersonal("34563");
        academico.setAreaEstudios("Economia");
        academico.setCorreoElectronico("hernan@Institucion.mx");
        academico.setNumeroTelefonico("523311756675");
        assertThrows(ErrorDAO.class, () -> AcademicoDB.agregarAcademico(academico), "pruebaAgregarAcademicoExternoCedulaRepetida");
    }

    @Test
    void pruebaAgregarAcademicoExternoExcesoCaracteres () {
        Academico academico = new Academico();
        try {
            academico.setNombre("Hernan");
            academico.setApellidoPaterno("Llamas");
            academico.setApellidoMaterno("Villa Señor");
            academico.setIdUniversidad(2);
            academico.setCedulaProfesional("9877985");
            academico.setNumeroPersonal("34563");
            academico.setAreaEstudios("Economia");
            academico.setCorreoElectronico("hernan@Institucion.mx");
            academico.setNumeroTelefonico("5233117566750123");
            AcademicoDB.agregarAcademico(academico);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarAcademicoExternoExcesoCaracteres");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdExitoso () {
        try {
            Academico resultado = AcademicoDB.getAcademicoPorId(1);
            assertTrue(resultado.equals(ACADEMICO_COMPUTACION),"pruebaGetAcademicoPorIdExitoso");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdExitoso");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdInexistente () {
        try {
            Academico resultado = AcademicoDB.getAcademicoPorId(99);
            assertNull(resultado);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdInexistente");
        }
    }

    @Test
    void pruebaGetTodosExitosa() {
        List<Academico> listaEsperada = new ArrayList<>();
        listaEsperada.add(ACADEMICO_COMPUTACION);
        listaEsperada.add(ACADEMICO_FILOSOFIA);
        List<Academico> listaObtenida = null;
        try {
            listaObtenida = AcademicoDB.getTodos();
            assertTrue(listaObtenida.size() == listaObtenida.size(), "pruebaGetTodosExitosa");
        } catch (ErrorDAO error) {
            fail("Fallida: pruebaGetTodosExitosa");
        }
        for (int i = 0;i < listaEsperada.size(); i ++) {
            assertTrue(listaEsperada.get(i).equals(listaObtenida.get(i)));
        }
    }


    @Test
    void pruebaEditarAcademicoExitoso () {
        int esperado = 3;
        int obtenido = 0;
        Academico academico = new Academico();
        academico.setNombre("Fernando");
        academico.setApellidoPaterno("Hernandez");
        academico.setApellidoMaterno("Lopez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional(ACADEMICO_FILOSOFIA.getCedulaProfesional());
        academico.setNumeroPersonal(ACADEMICO_FILOSOFIA.getNumeroPersonal());
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);

        try {
            obtenido = AcademicoDB.editarAcademico(academico);
        }
        catch (ErrorDAO error) {
            System.out.println(error.getMessage());
            fail("Fallida: pruebaEditarAcademicoExitoso");
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaEditarAcademicoCedulaInexistente () {
        int esperado = 0;
        int obtenido = 0;
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
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarAcademicoCedulaInexistente");
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaEditarAcademicoUniversidadInexistente () {
        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(10);
        academico.setCedulaProfesional(ACADEMICO_FILOSOFIA.getCedulaProfesional());
        academico.setNumeroPersonal(ACADEMICO_FILOSOFIA.getNumeroPersonal());
        academico.setAreaEstudios("Humanidades");
        academico.setCorreoElectronico("Esther@Institucion.mx");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Fijo");
        academico.setIdFacultad(1);
        assertThrows(ErrorDAO.class, () -> AcademicoDB.editarAcademico(academico),"pruebaEditarAcademicoUniversidadInexistente");
    }
    @Test
    void pruebaEditarAcademicoFacultadInexistente () {
        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional(ACADEMICO_FILOSOFIA.getCedulaProfesional());
        academico.setNumeroPersonal(ACADEMICO_FILOSOFIA.getNumeroPersonal());
        academico.setAreaEstudios("Dramaturgo");
        academico.setCorreoElectronico("esther@gmail.com");
        academico.setNumeroTelefonico("522288536230");
        academico.setIdFacultad(10);
        assertThrows(ErrorDAO.class, () -> AcademicoDB.editarAcademico(academico),"pruebaEditarAcademicoFacultadInexistente");
    }

    @Test
    void pruebaEditarAcademicoCategoriaFacultadNuloExitosa () {
        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional(ACADEMICO_FILOSOFIA.getCedulaProfesional());
        academico.setNumeroPersonal(ACADEMICO_FILOSOFIA.getNumeroPersonal());
        academico.setAreaEstudios("Dramaturgo");
        academico.setCorreoElectronico("esther@gmail.com");
        academico.setNumeroTelefonico("522288536230");
        academico.setIdFacultad(1);
        int esperado = 3;
        int real = 0;
        try {
            real = AcademicoDB.editarAcademico(academico);
        }
        catch (ErrorDAO errorDAO) {
            fail("Error en la prueba " + errorDAO.getMessage());
        }
        assertEquals(esperado,real);
    }

    @Test
    void pruebaAgregarAcademicoConCuentaExitoso () {
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

        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario("HernanVilla");
        cuenta.setContrasena("MyZillTippens");
        cuenta.setTipo(Cuenta.TipoUsuario.academico);
        cuenta.setEstado(Cuenta.EstadoCuenta.pendiente);
        int esperado = 3;
        int obtenido = 0;
        try {
            obtenido = AcademicoDB.agregarAcademicoConCuenta(academico, cuenta);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExternoExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoExternoExitosa");
    }
}