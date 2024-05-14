package test.AccesoADatos;

import DAO.AcademicoDAO;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AcademicoDTODAOTest {
    private final AcademicoDTO ACADEMICO_DTO_FILOSOFIA = new AcademicoDTO(2,"Esther","Herrara","Martinez",1,"200011",
                                                                "4564","Filosofia","esther@gmail.com","522288536230","Dramaturgo",1);
    private final AcademicoDTO ACADEMICO_DTO_COMPUTACION = new AcademicoDTO(1,"Jose","Lopez","Perez",1,"123",
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
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('UniversidadDTO Veracruzana',1);");
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
        assertThrows(ErrorDAO.class, ()-> AcademicoDAO.getListaAcademicoPorCampos("TipoContratacion", "Fijo"),"pruebaGetListaAcademicoPorCamposFallida");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoNulo () {
        assertThrows(ErrorDAO.class,()-> AcademicoDAO.getListaAcademicoPorCampos(null,"Investigador"),"pruebaGetListaAcademicoPorCampoNulo");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoValorNulo () {
        try {
            List<AcademicoDTO> resultado = AcademicoDAO.getListaAcademicoPorCampos("categoria",null);
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoNulo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoNulo");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoFacultadExitosa () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_COMPUTACION);
        listaEsperada.add(this.ACADEMICO_DTO_FILOSOFIA);

        try {
            listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("facultad", "Economia");
        }
        catch (ErrorDAO error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadExitosa");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetListaAcademicoPorCampoFacultadExitosa");
        for (AcademicoDTO academicoDTO : listaEsperada) {
            assertEquals(academicoDTO,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoFacultadVacia () {
        try {
            List<AcademicoDTO> listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("facultad", "FEI");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoUniversidadExitoso () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_COMPUTACION);
        listaEsperada.add(this.ACADEMICO_DTO_FILOSOFIA);
        try {
            listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("universidad", "UniversidadDTO Veracruzana");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoUniversidadExitoso");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetListaAcademicoPorCampoUniversidadExitoso");
        for (AcademicoDTO academicoDTO : listaEsperada) {
            assertEquals(academicoDTO,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoUniversidadVacia () {
        try {
            List<AcademicoDTO> resultado = AcademicoDAO.getListaAcademicoPorCampos("universidad","UNAM");
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaExitosa () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_FILOSOFIA);

        try {
            listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("area", "Filosofia");
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
            List<AcademicoDTO> listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("area", "F");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoAreaVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoCategoriaContratacionExitosa () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_COMPUTACION);

        try {
            listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("categoria", "Investigador");
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
            List<AcademicoDTO> resultado = AcademicoDAO.getListaAcademicoPorCampos("categoria", "Profe");
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia");
        }
    }

    @Test
    void pruebaGetAcademicoPorRegionExitoso () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_COMPUTACION);
        listaEsperada.add(this.ACADEMICO_DTO_FILOSOFIA);
        try {
            listaObtenida = AcademicoDAO.getListaAcademicoPorCampos("region", "Xalapa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorRegionExitoso");
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetAcademicoPorRegionExitoso");
        for (AcademicoDTO academicoDTO : listaEsperada) {
            assertEquals(academicoDTO,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetAcademicoPorRegionVacia () {
        try {
            List<AcademicoDTO> resultado = AcademicoDAO.getListaAcademicoPorCampos("region","Veracruz");
            assertTrue(resultado.isEmpty(),"pruebaGetAcademicoPorRegionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorRegionVacia");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitosa () {
        try {
            AcademicoDTO obtenido = AcademicoDAO.getAcademicoPorCedula("200011");
            assertEquals(this.ACADEMICO_DTO_FILOSOFIA,obtenido,"pruebaGetAcademicoPorCedulaExitosa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitosa");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaInexistente () {
        try {
            AcademicoDTO resultado = AcademicoDAO.getAcademicoPorCedula("123456");
            assertNull(resultado,"pruebaGetAcademicoPorCedulaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaInexistente");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaNula () {
        try {
            AcademicoDTO resultado = AcademicoDAO.getAcademicoPorCedula(null);
            assertNull(resultado,"pruebaGetAcademicoPorCedulaNula");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaNula");
        }
    }

    @Test
    void pruebaAgregarAcademicoUVExitoso () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("Economia");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        academicoDTO.setCategoriaContratacion("Por Horas");
        academicoDTO.setIdFacultad(1);
        int esperado = 2;
        int obtenido = 0;
        try {
            obtenido = AcademicoDAO.agregarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoUVExitoso\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoUVExitoso");
    }

    @Test
    void pruebaAgregarAcademicoUVSinDatos () {
        assertThrows(ErrorDAO.class,() -> AcademicoDAO.agregarAcademico(new AcademicoDTO()),"pruebaAgregarAcademicoUVSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoUVUniversidadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Jose");
        academicoDTO.setApellidoPaterno("Andrei");
        academicoDTO.setApellidoMaterno("De la paz");
        academicoDTO.setIdUniversidad(5);
        academicoDTO.setCedulaProfesional("453432523");
        academicoDTO.setNumeroPersonal("2341");
        academicoDTO.setAreaEstudios("Humanidades");
        academicoDTO.setCorreoElectronico("Andrei@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523351256655");
        academicoDTO.setIdFacultad(1);
        assertThrows(ErrorDAO.class,() -> AcademicoDAO.agregarAcademico(academicoDTO),"pruebaAgregarAcademicoUVUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVFacultadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Jose");
        academicoDTO.setApellidoPaterno("Andrei");
        academicoDTO.setApellidoMaterno("De la paz");
        academicoDTO.setIdUniversidad(5);
        academicoDTO.setCedulaProfesional("98765");
        academicoDTO.setNumeroPersonal("4564");
        academicoDTO.setAreaEstudios("Humanidades");
        academicoDTO.setCorreoElectronico("Andrei@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523351256655");
        academicoDTO.setIdFacultad(10);
        assertThrows(ErrorDAO.class,() -> AcademicoDAO.agregarAcademico(academicoDTO),"pruebaAgregarAcademicoUVFacultadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVExcesoCaracteres () {
        try {
            AcademicoDTO academicoDTO = new AcademicoDTO();
            academicoDTO.setNombre("Ivan");
            academicoDTO.setApellidoPaterno("Ingram");
            academicoDTO.setApellidoMaterno("Lopez");
            academicoDTO.setIdUniversidad(1);
            academicoDTO.setCedulaProfesional("564635356");
            academicoDTO.setNumeroPersonal("2341");
            academicoDTO.setAreaEstudios("Ingenieria");
            academicoDTO.setCorreoElectronico("Ivan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("523351256655567");
            academicoDTO.setIdFacultad(1);
            AcademicoDAO.agregarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarAcademcioUVExcesoCaracteres");
        }
    }

    @Test
    void pruebaAgregarAcademicoExternoExitosa () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
        academicoDTO.setIdUniversidad(2);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("Economia");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        int esperado = 2;
        int obtenido = 0;
        try {
            obtenido = AcademicoDAO.agregarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExternoExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoExternoExitosa");
    }

    @Test
    void pruebaAgregarAcademicoExternoSinDatos () {
        assertThrows(ErrorDAO.class,() -> AcademicoDAO.agregarAcademico(new AcademicoDTO()),"pruebaAgregarAcademicoExternoSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoExternoUniversidadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
        academicoDTO.setIdUniversidad(20);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("Economia");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        assertThrows(ErrorDAO.class,() -> AcademicoDAO.agregarAcademico(academicoDTO),"pruebaAgregarAcademicoExternoUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoExternoCedulaRepetida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
        academicoDTO.setIdUniversidad(2);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_FILOSOFIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("Economia");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        assertThrows(ErrorDAO.class, () -> AcademicoDAO.agregarAcademico(academicoDTO), "pruebaAgregarAcademicoExternoCedulaRepetida");
    }

    @Test
    void pruebaAgregarAcademicoExternoExcesoCaracteres () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        try {
            academicoDTO.setNombre("Hernan");
            academicoDTO.setApellidoPaterno("Llamas");
            academicoDTO.setApellidoMaterno("Villa Señor");
            academicoDTO.setIdUniversidad(2);
            academicoDTO.setCedulaProfesional("9877985");
            academicoDTO.setNumeroPersonal("34563");
            academicoDTO.setAreaEstudios("Economia");
            academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("5233117566750123");
            AcademicoDAO.agregarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarAcademicoExternoExcesoCaracteres");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdExitoso () {
        try {
            AcademicoDTO resultado = AcademicoDAO.getAcademicoPorId(1);
            assertTrue(resultado.equals(ACADEMICO_DTO_COMPUTACION),"pruebaGetAcademicoPorIdExitoso");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdExitoso");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdInexistente () {
        try {
            AcademicoDTO resultado = AcademicoDAO.getAcademicoPorId(99);
            assertNull(resultado);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdInexistente");
        }
    }

    @Test
    void pruebaGetTodosExitosa() {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        listaEsperada.add(ACADEMICO_DTO_COMPUTACION);
        listaEsperada.add(ACADEMICO_DTO_FILOSOFIA);
        List<AcademicoDTO> listaObtenida = null;
        try {
            listaObtenida = AcademicoDAO.getTodos();
            assertTrue(listaObtenida.size() == listaEsperada.size(), "pruebaGetTodosExitosa");
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
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Fernando");
        academicoDTO.setApellidoPaterno("Hernandez");
        academicoDTO.setApellidoMaterno("Lopez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_FILOSOFIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_FILOSOFIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("Informatica");
        academicoDTO.setCorreoElectronico("fer@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756676");
        academicoDTO.setIdFacultad(1);

        try {
            obtenido = AcademicoDAO.editarAcademico(academicoDTO);
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
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Fernando");
        academicoDTO.setApellidoPaterno("Martinez");
        academicoDTO.setApellidoMaterno("Ramirez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("1");
        academicoDTO.setNumeroPersonal("1");
        academicoDTO.setAreaEstudios("Informatica");
        academicoDTO.setCorreoElectronico("fer@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756676");
        academicoDTO.setIdFacultad(1);
        try {
            obtenido = AcademicoDAO.editarAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarAcademicoCedulaInexistente");
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaEditarAcademicoUniversidadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Esther");
        academicoDTO.setApellidoPaterno("Ramirez");
        academicoDTO.setApellidoMaterno("Escobar");
        academicoDTO.setIdUniversidad(10);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_FILOSOFIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_FILOSOFIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("Humanidades");
        academicoDTO.setCorreoElectronico("Esther@Institucion.mx");
        academicoDTO.setNumeroTelefonico("522288536230");
        academicoDTO.setCategoriaContratacion("Fijo");
        academicoDTO.setIdFacultad(1);
        assertThrows(ErrorDAO.class, () -> AcademicoDAO.editarAcademico(academicoDTO),"pruebaEditarAcademicoUniversidadInexistente");
    }
    @Test
    void pruebaEditarAcademicoFacultadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Esther");
        academicoDTO.setApellidoPaterno("Ramirez");
        academicoDTO.setApellidoMaterno("Escobar");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_FILOSOFIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_FILOSOFIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("Dramaturgo");
        academicoDTO.setCorreoElectronico("esther@gmail.com");
        academicoDTO.setNumeroTelefonico("522288536230");
        academicoDTO.setIdFacultad(10);
        assertThrows(ErrorDAO.class, () -> AcademicoDAO.editarAcademico(academicoDTO),"pruebaEditarAcademicoFacultadInexistente");
    }

    @Test
    void pruebaEditarAcademicoCategoriaFacultadNuloExitosa () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Esther");
        academicoDTO.setApellidoPaterno("Ramirez");
        academicoDTO.setApellidoMaterno("Escobar");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_FILOSOFIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_FILOSOFIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("Dramaturgo");
        academicoDTO.setCorreoElectronico("esther@gmail.com");
        academicoDTO.setNumeroTelefonico("522288536230");
        academicoDTO.setIdFacultad(1);
        int esperado = 3;
        int real = 0;
        try {
            real = AcademicoDAO.editarAcademico(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("Error en la prueba " + errorDAO.getMessage());
        }
        assertEquals(esperado,real);
    }

    @Test
    void pruebaAgregarAcademicoConCuentaExitoso () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("Economia");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");

        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNombreUsuario("HernanVilla");
        cuentaDTO.setContrasena("MyZillTippens");
        cuentaDTO.setTipo(CuentaDTO.TipoUsuario.academico);
        cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.pendiente);
        int esperado = 3;
        int obtenido = 0;
        try {
            obtenido = AcademicoDAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExternoExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoExternoExitosa");
    }
}