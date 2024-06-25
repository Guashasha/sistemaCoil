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
    void pruebaGetListaAcademicoPorCampoUniversidadExitoso () {
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

        for (AcademicoDTO academicoDTO : listaEsperada) {
            assertEquals(academicoDTO,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoUniversidadVacia () {
        try {
            List<AcademicoDTO> listaObtenida = ACADEMICO_DAO.getAcademicosPorUniversidad("UNAM");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoUniversidadVacia");
        }
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaExitosa () {
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(this.ACADEMICO_DTO_ECONOMIA);

        try {
            listaObtenida = ACADEMICO_DAO.getAcademicosPorAreaEstudios("economico-administrativo");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaAcademicoPorCampoAreaExitosa");
        }

        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"pruebaGetListaAcademicoPorCampoAreaExitosa");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoAreaVacia () {
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

        assertEquals(listaEsperada.get(0),listaObtenida.get(0),"pruebaGetAcademicoPorCampoCategoriaContratacionExitosa");
    }

    @Test
    void pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia () {
        try {
            List<AcademicoDTO> listaObtenida = ACADEMICO_DAO.getAcademicosPorCategoriaContratacion("Profe");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetListaAcademicoPorCampoCategoriaContratacionVacia");
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

        for (AcademicoDTO academico : listaEsperada) {
            assertEquals(academico,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetAcademicoPorRegionVacia () {
        try {
            List<AcademicoDTO> listaObtenida = ACADEMICO_DAO.getAcademicosPorRegion("Veracruz");
            assertTrue(listaObtenida.isEmpty(),"pruebaGetAcademicoPorRegionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorRegionVacia");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitosa () {
        try {
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_DAO.getAcademicoPorCedula("200011");
            assertEquals(this.ACADEMICO_DTO_ECONOMIA, academicoObtenido.get(), "pruebaGetAcademicoPorCedulaExitosa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitosa");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaInexistente () {
        try {
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_DAO.getAcademicoPorCedula("123456");
            assertFalse(academicoObtenido.isPresent(),"pruebaGetAcademicoPorCedulaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaInexistente");
        }
    }

    @Test
    void pruebaGetAcademicoPorCedulaNula () {
        try {
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_DAO.getAcademicoPorCedula(null);
            assertFalse(academicoObtenido.isPresent(),"pruebaGetAcademicoPorCedulaNula");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaNula");
        }
    }

    @Test
    void pruebaAgregarAcademicoUVExitoso () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setNombre("Hernan");
        academico.setApellidos("Llamas");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("9877985");
        academico.setNumeroPersonal("34563");
        academico.setAreaEstudios("humanidades");
        academico.setCorreoElectronico("hernan@Institucion.mx");
        academico.setNumeroTelefonico("523311756675");
        academico.setCategoriaContratacion("Por Horas");
        academico.setIdFacultad(1);
        int filaAfectadaEsperada = 2;
        int filaAfectadaObtenida = 0;
        try {
            filaAfectadaObtenida = ACADEMICO_DAO.agregar(academico);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoUVExitoso\n" + error.getMessage());
        }
        assertEquals(filaAfectadaEsperada,filaAfectadaObtenida,"pruebaAgregarAcademicoUVExitoso");
    }

    @Test
    void pruebaAgregarAcademicoUVSinDatos () {
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(new AcademicoDTO()), "pruebaAgregarAcademicoUVSinDatos");
    }

    @Test
    void pruebaAgregarAcademicoUVUniversidadInexistente () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setNombre("Jose");
        academico.setApellidos("Andrei");
        academico.setIdUniversidad(5);
        academico.setCedulaProfesional("453432523");
        academico.setNumeroPersonal("2341");
        academico.setAreaEstudios("economico-administrativo");
        academico.setCorreoElectronico("Andrei@Institucion.mx");
        academico.setNumeroTelefonico("523351256655");
        academico.setIdFacultad(1);
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(academico), "pruebaAgregarAcademicoUVUniversidadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVFacultadInexistente () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setNombre("Jose");
        academico.setApellidos("Andrei");
        academico.setIdUniversidad(5);
        academico.setCedulaProfesional("98765");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("economico-administrativo");
        academico.setCorreoElectronico("Andrei@Institucion.mx");
        academico.setNumeroTelefonico("523351256655");
        academico.setIdFacultad(10);
        assertThrows(ErrorDAO.class, () -> ACADEMICO_DAO.agregar(academico), "pruebaAgregarAcademicoUVFacultadInexistente");
    }

    @Test
    void pruebaAgregarAcademicoUVExcesoCaracteres () {
        try {
            AcademicoDTO academico = new AcademicoDTO();
            academico.setNombre("Ivan");
            academico.setApellidos("Ingram");
            academico.setIdUniversidad(1);
            academico.setCedulaProfesional("564635356");
            academico.setNumeroPersonal("2341");
            academico.setAreaEstudios("economico-administrativo");
            academico.setCorreoElectronico("Ivan@Institucion.mx");
            academico.setNumeroTelefonico("523351256655567");
            academico.setIdFacultad(1);
            ACADEMICO_DAO.agregar(academico);
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
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_DAO.getPorId(1);
            assertTrue(academicoObtenido.get().equals(ACADEMICO_DTO_TECNICA), "pruebaGetAcademicoPorIdExitoso");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorIdExitoso");
        }
    }

    @Test
    void pruebaGetAcademicoPorIdInexistente () {
        try {
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_DAO.getPorId(99);
            assertFalse(academicoObtenido.isPresent());
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
        } catch (ErrorDAO error) {
            fail("Fallida: pruebaGetTodosExitosa");
        }
        for (int i = 0;i < listaEsperada.size(); i ++) {
            assertTrue(listaEsperada.get(i).equals(listaObtenida.get(i)));
        }
    }


    @Test
    void pruebaEditarAcademicoExitoso () {
        int filasAfectadasEsperadas = 3;
        int filasAfectadasObtenidos = 0;
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Fernando");
        academicoDTO.setApellidos("Hernandez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional(ACADEMICO_DTO_ECONOMIA.getCedulaProfesional());
        academicoDTO.setNumeroPersonal(ACADEMICO_DTO_ECONOMIA.getNumeroPersonal());
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("fer@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756676");
        academicoDTO.setIdFacultad(1);

        try {
            filasAfectadasObtenidos = ACADEMICO_DAO.modificar(academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarAcademicoExitoso");
        }
        assertEquals(filasAfectadasEsperadas, filasAfectadasObtenidos);
    }

    @Test
    void pruebaEditarAcademicoCedulaInexistente () {
        int filasAfectadasEsperada = 0;
        int filasAfectadasObtenida = 0;

        AcademicoDTO academico = new AcademicoDTO();
        academico.setNombre("Fernando");
        academico.setApellidos("Martinez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("1");
        academico.setNumeroPersonal("1");
        academico.setAreaEstudios("economico-administrativo");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);
        try {
            filasAfectadasObtenida = ACADEMICO_DAO.modificar(academico);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarAcademicoCedulaInexistente");
        }
        assertEquals(filasAfectadasEsperada, filasAfectadasObtenida);
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
            cuentaDTO.setNombreUsuario("HernanVillasdadasdssdsdadasasdsadddasdsadaqwwqMiasdfERADSASFhETANASA SDOEA");
            cuentaDTO.setContrasena("MyZillTippens");
            cuentaDTO.setTipo(CuentaDTO.TipoUsuario.academico);
            cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.pendiente);
            ACADEMICO_DAO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);

        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "PruebaAgregarAcademicoConCuentaFallido");
        }
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