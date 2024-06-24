package test.DAO;

import DAO.AcademicoDAO;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AcademicoDAOTest {
    private final AcademicoDAO ACADEMICO_DAO = new AcademicoDAO();
    private final AcademicoDTO ACADEMICO_DTO_ECONOMIA = new AcademicoDTO(2, "Esther", "Herrara Martinez", 1, "200011",
                                                                         "4564", "economico-administrativo", "esther@gmail.com", "522288536230", "Dramaturgo", 1);
    private final AcademicoDTO ACADEMICO_DTO_TECNICA = new AcademicoDTO(1, "Jose", "Lopez Perez", 1, "123",
                                                                        "123456", "tecnica", "jose@gmail.com", "522288536230", "Investigador", 1);

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
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

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (1, 'Jose', 'Lopez Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('123', '123456', 1, 'tecnica', 'jose@gmail.com', '522288536230', 'Investigador', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (2, 'Esther', 'Herrara Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'economico-administrativo', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");
    }

    @AfterAll
    static void tearDown() {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }


    @Test
    void pruebaGetListaAcademicoPorCampoFacultadExitosa () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_TECNICA);
        listaEsperada.add(this.ACADEMICO_DTO_ECONOMIA);

        try {
            listaObtenida = ACADEMICO_DAO.getAcademicosPorFacultad("Economia");
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
            List<AcademicoDTO> listaObtenida = ACADEMICO_DAO.getAcademicosPorFacultad("FEI");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallido: pruebaGetListaAcademicoPorCampoFacultadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoUniversidadExitoso () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_TECNICA);
        listaEsperada.add(this.ACADEMICO_DTO_ECONOMIA);
        try {
            listaObtenida = ACADEMICO_DAO.getAcademicosPorUniversidad("Universidad Veracruzana");
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
    void pruebaGetListaAcademicoUniversidadVacia () {
        try {
            List<AcademicoDTO> resultado = ACADEMICO_DAO.getAcademicosPorUniversidad("UNAM");
            assertTrue(resultado.isEmpty(),"pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoAreaExitosa () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_ECONOMIA);

        try {
            listaObtenida = ACADEMICO_DAO.getAcademicosPorAreaEstudios("economico-administrativo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaExitosa");
        }

        assertFalse(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoAreaExitosa");
        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"pruebaGetListaAcademicoPorCampoAreaExitosa");
    }

    @Test
    void pruebaGetListaAcademicoAreaVacia () {
        try {
            List<AcademicoDTO> listaObtenida = ACADEMICO_DAO.getAcademicosPorAreaEstudios("F");
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
        listaEsperada.add(this.ACADEMICO_DTO_TECNICA);

        try {
            listaObtenida = ACADEMICO_DAO.getAcademicosPorCategoriaContratacion("Investigador");
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
            List<AcademicoDTO> resultado = ACADEMICO_DAO.getAcademicosPorCategoriaContratacion("Profe");
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
        listaEsperada.add(this.ACADEMICO_DTO_TECNICA);
        listaEsperada.add(this.ACADEMICO_DTO_ECONOMIA);
        try {
            listaObtenida = ACADEMICO_DAO.getAcademicosPorRegion("Xalapa");
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
            List<AcademicoDTO> resultado = ACADEMICO_DAO.getAcademicosPorRegion("Veracruz");
            assertTrue(resultado.isEmpty(),"pruebaGetAcademicoPorRegionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorRegionVacia");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitosa () {
        try {
            Optional<AcademicoDTO> academicoDTOOptional = ACADEMICO_DAO.getAcademicoPorCedula("200011");
            assertTrue(academicoDTOOptional.isPresent());
            AcademicoDTO obtenido = academicoDTOOptional.get();
            assertEquals(this.ACADEMICO_DTO_ECONOMIA, obtenido, "pruebaGetAcademicoPorCedulaExitosa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitosa");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaInexistente () {
        try {
            Optional<AcademicoDTO> resultado = ACADEMICO_DAO.getAcademicoPorCedula("123456");
            assertFalse(resultado.isPresent(),"pruebaGetAcademicoPorCedulaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaInexistente");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaNula () {
        try {
            Optional<AcademicoDTO> resultado = ACADEMICO_DAO.getAcademicoPorCedula(null);
            assertFalse(resultado.isPresent(),"pruebaGetAcademicoPorCedulaNula");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaNula");
        }
    }

    @Test
    void pruebaAgregarAcademicoUVExitoso () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidos("Llamas");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("humanidades");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        academicoDTO.setCategoriaContratacion("Por Horas");
        academicoDTO.setIdFacultad(1);
        int esperado = 2;
        int obtenido = 0;
        try {
            obtenido = ACADEMICO_DAO.agregar(academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoUVExitoso\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoUVExitoso");
    }

    @Test
    void pruebaAgregarAcademicoUVSinDatos () {
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(new AcademicoDTO()), "pruebaAgregarAcademicoUVSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoUVUniversidadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Jose");
        academicoDTO.setApellidos("Andrei");
        academicoDTO.setIdUniversidad(5);
        academicoDTO.setCedulaProfesional("453432523");
        academicoDTO.setNumeroPersonal("2341");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("Andrei@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523351256655");
        academicoDTO.setIdFacultad(1);
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(academicoDTO), "pruebaAgregarAcademicoUVUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVFacultadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Jose");
        academicoDTO.setApellidos("Andrei");
        academicoDTO.setIdUniversidad(5);
        academicoDTO.setCedulaProfesional("98765");
        academicoDTO.setNumeroPersonal("4564");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("Andrei@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523351256655");
        academicoDTO.setIdFacultad(10);
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(academicoDTO), "pruebaAgregarAcademicoUVFacultadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVExcesoCaracteres () {
        try {
            AcademicoDTO academicoDTO = new AcademicoDTO();
            academicoDTO.setNombre("Ivan");
            academicoDTO.setApellidos("Ingram");
            academicoDTO.setIdUniversidad(1);
            academicoDTO.setCedulaProfesional("564635356");
            academicoDTO.setNumeroPersonal("2341");
            academicoDTO.setAreaEstudios("economico-administrativo");
            academicoDTO.setCorreoElectronico("Ivan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("523351256655567");
            academicoDTO.setIdFacultad(1);
            ACADEMICO_DAO.agregar(academicoDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarAcademcioUVExcesoCaracteres");
        }
    }

    @Test
    void pruebaAgregarAcademicoExternoExitosa () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidos("Llamas");
        academicoDTO.setIdUniversidad(2);
        academicoDTO.setCedulaProfesional("20001232");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        int esperado = 2;
        int obtenido = 0;
        try {
            obtenido = ACADEMICO_DAO.agregar(academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExternoExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoExternoExitosa");
    }

    @Test
    void pruebaAgregarAcademicoExternoSinDatos () {
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(new AcademicoDTO()), "pruebaAgregarAcademicoExternoSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoExternoUniversidadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidos("Llamas");
        academicoDTO.setIdUniversidad(20);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(academicoDTO), "pruebaAgregarAcademicoExternoUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoExternoCedulaRepetida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidos("Llamas");
        academicoDTO.setIdUniversidad(2);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_ECONOMIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(academicoDTO), "pruebaAgregarAcademicoExternoCedulaRepetida");
    }

    @Test
    void pruebaAgregarAcademicoExternoExcesoCaracteres () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        try {
            academicoDTO.setNombre("Hernan");
            academicoDTO.setApellidos("Llamas");
            academicoDTO.setIdUniversidad(2);
            academicoDTO.setCedulaProfesional("9877985");
            academicoDTO.setNumeroPersonal("34563");
            academicoDTO.setAreaEstudios("economico-administrativo");
            academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("5233117566750123");
            ACADEMICO_DAO.agregar(academicoDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarAcademicoExternoExcesoCaracteres");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdExitoso () {
        try {
            Optional<AcademicoDTO> academicoDTOOptional = ACADEMICO_DAO.getPorId(1);
            assertTrue(academicoDTOOptional.isPresent());
            AcademicoDTO resultado = academicoDTOOptional.get();
            assertTrue(resultado.equals(ACADEMICO_DTO_TECNICA), "pruebaGetAcademicoPorIdExitoso");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdExitoso");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdInexistente () {
        try {
            Optional<AcademicoDTO> resultado = ACADEMICO_DAO.getPorId(99);
            assertFalse(resultado.isPresent());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdInexistente");
        }
    }

    @Test
    void pruebaGetTodosExitosa() {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        listaEsperada.add(ACADEMICO_DTO_TECNICA);
        listaEsperada.add(ACADEMICO_DTO_ECONOMIA);
        List<AcademicoDTO> listaObtenida = null;
        try {
            listaObtenida = ACADEMICO_DAO.getTodos();
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
        academicoDTO.setApellidos("Martinez Ramirez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_ECONOMIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_ECONOMIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("fer@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756676");
        academicoDTO.setIdFacultad(1);

        try {
            obtenido = ACADEMICO_DAO.modificar(academicoDTO);
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
        academicoDTO.setApellidos("Martinez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("1");
        academicoDTO.setNumeroPersonal("1");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("fer@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756676");
        academicoDTO.setIdFacultad(1);
        try {
            obtenido = ACADEMICO_DAO.modificar(academicoDTO);
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
        academicoDTO.setApellidos("Ramirez");
        academicoDTO.setIdUniversidad(10);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_ECONOMIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_ECONOMIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("Esther@Institucion.mx");
        academicoDTO.setNumeroTelefonico("522288536230");
        academicoDTO.setCategoriaContratacion("Fijo");
        academicoDTO.setIdFacultad(1);
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.modificar(academicoDTO), "pruebaEditarAcademicoUniversidadInexistente");
    }
    @Test
    void pruebaEditarAcademicoFacultadInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Esther");
        academicoDTO.setApellidos("Ramirez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_ECONOMIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_ECONOMIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("esther@gmail.com");
        academicoDTO.setNumeroTelefonico("522288536230");
        academicoDTO.setIdFacultad(10);
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.modificar(academicoDTO), "pruebaEditarAcademicoFacultadInexistente");
    }

    @Test
    void pruebaEditarAcademicoCategoriaFacultadNuloExitosa () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Esther");
        academicoDTO.setApellidos("Ramirez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_ECONOMIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_ECONOMIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("esther@gmail.com");
        academicoDTO.setNumeroTelefonico("522288536230");
        academicoDTO.setIdFacultad(1);
        int esperado = 3;
        int real = 0;
        try {
            real = ACADEMICO_DAO.modificar(academicoDTO);
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
        academicoDTO.setApellidos("Llamas");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("economico-administrativo");
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
            obtenido = ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExternoExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregarAcademicoExternoExitosa");
    }

    @Test
    void pruebaAgregarAcademicoConCuentaUsuarioExcesoCaracteresFallido () {
        AcademicoDTO academicoDTO = new AcademicoDTO();


            academicoDTO.setNombre("Hernan");
            academicoDTO.setApellidos("Llamas");
            academicoDTO.setIdUniversidad(1);
            academicoDTO.setCedulaProfesional("9877985");
            academicoDTO.setNumeroPersonal("34563");
            academicoDTO.setAreaEstudios("economico-administrativo");
            academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("523311756675");

            CuentaDTO cuentaDTO = new CuentaDTO();
            assertThrows(ErrorDAO.class, () -> {
                cuentaDTO.setNombreUsuario("HernanVillasdadasdssdsdadasasdsadddasdsadaqwwqMiasdfERADSASFhETANASA SDOEA");
                cuentaDTO.setContrasena("MyZillTippens");
                cuentaDTO.setTipo(CuentaDTO.TipoUsuario.academico);
                cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.pendiente);
                ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
            });
    }

    @Test
    void pruebaAgregarAcademicoConCuentaUsuarioEspaciosFallido () {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setNombre("Hernan");
            academicoDTO.setApellidos("Llamas");
            academicoDTO.setIdUniversidad(1);
            academicoDTO.setCedulaProfesional("9877985");
            academicoDTO.setNumeroPersonal("34563");
            academicoDTO.setAreaEstudios("economico-administrativo");
            academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("523311756675");

            CuentaDTO cuentaDTO = new CuentaDTO();
            cuentaDTO.setNombreUsuario("Herna   nVila");
            cuentaDTO.setContrasena("MyZillTippens");
            cuentaDTO.setTipo(CuentaDTO.TipoUsuario.academico);
            cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.pendiente);
            ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);

        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarAcademicoConCuentaUsuarioEspaciosFallido");
        }
    }

    @Test
    void pruebaAgregarAcademicoConCuentaContrasenaExcesoCaracteresFallida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setNombre("Hernan");
            academicoDTO.setApellidos("Llamas");
            academicoDTO.setIdUniversidad(1);
            academicoDTO.setCedulaProfesional("9877985");
            academicoDTO.setNumeroPersonal("34563");
            academicoDTO.setAreaEstudios("economico-administrativo");
            academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
            academicoDTO.setNumeroTelefonico("523311756675");

            CuentaDTO cuentaDTO = new CuentaDTO();
            cuentaDTO.setNombreUsuario("HernanVila");
            cuentaDTO.setContrasena("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                                            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                                            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                                            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                                            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                                            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
            cuentaDTO.setTipo(CuentaDTO.TipoUsuario.academico);
            cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.pendiente);
            ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);

        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarAcademicoConCuentaContrasenaExcesoCaracteresFallida");
        }
    }
}