package test.DAO;

import DAO.AcademicoAuxiliar;
import DTO.AcademicoDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AcademicoAuxiliarTest {
    private static final AcademicoAuxiliar ACADEMICO_AUXILIAR = new AcademicoAuxiliar();

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (1, 'Jose Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico) VALUES ('123', '123456', 1, 'tecnica', 'jose@gmail.com', '522288536230');");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (2, 'Esther', 'Herrara Martinez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad) VALUES ('200011', '4564', 2, 'economico-administrativo', 'esther@gmail.com', '522288536230', 'Dramaturgo', 1);");


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
    void pruebaGetAcademicosPorFacultadExitosa () {
        String nombreFacultad = "Economia";
        List<AcademicoDTO> listaAcademicoObtenido = null;
        try {
            listaAcademicoObtenido = ACADEMICO_AUXILIAR.getAcademicosPorFacultad(nombreFacultad);
        }
        catch (ErrorDAO error) {
            fail("Fallida : pruebaGetAcademicosPorFacultadExitosa");
        }
        int tamanoEsperado = 1;
        assertEquals(tamanoEsperado, listaAcademicoObtenido.size(), "pruebaGetAcademicosPorFacultadExitosa");
    }

    @Test
    void pruebaGetAcademicosPorFacultadCadenaInvalida () {
        String nombreFacultad = null;
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorFacultad(nombreFacultad), "pruebaGetAcademicosPorFacultadCadenaInvalida");
    }

    @Test
    void pruebaGetAcademicosPorFacultadCadenaEspacios () {
        String nombreFacultad = "";
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorFacultad(nombreFacultad),"pruebaGetAcademicosPorFacultadCadenaEspacios");
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitoso () {
        String cedula = "123";
        try {
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_AUXILIAR.getAcademicoPorCedula(cedula);
            assertTrue(academicoObtenido.isPresent(), "pruebaGetAcademicoPorCedulaExitoso");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitoso");
        }
    }

   @Test
   void pruebaGetAcademicoPorCedulaCadenaInvalida () {
        String cedula = null;
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicoPorCedula(cedula), "pruebaGetAcademicoPorCedulaCadanaInvalida");
   }

   @Test
   void pruebaGetAcademicoPorCedulaCadenaPorEspacios () {
        String cedula = "";
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicoPorCedula(cedula), "pruebaGetAcademicoPorCedulaCadenaPorEspacios");
   }

    @Test
    void pruebaGetAcademicosPorUniversidadExitosa () {
        String nombreUniversidad = "Universidad Veracruzana";
        List<AcademicoDTO> listaAcademicosObtenidos = null;
        int tamanoEsperado = 2;

        try {
            listaAcademicosObtenidos = ACADEMICO_AUXILIAR.getAcademicosPorUniversidad(nombreUniversidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruenaGetAcademicosPorUniversidadExitosa");
        }
        assertEquals(tamanoEsperado, listaAcademicosObtenidos.size(), "pruebaGetAcademicosPorUniversidadExitosa");
    }

    @Test
    void pruebaGetAcademicosPorUniversidadCadenaNoValida () {
        String universidad = null;
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorUniversidad(universidad), "pruebaGetAcademicosPorUniversidadCadenaNoValida");
    }

    @Test
    void getAcademicosPorAreaEstudios () {
        String areaEstudios = "economico-administrativo";
        List<AcademicoDTO> listaAcademicosObtenidos = null;
        try {
            listaAcademicosObtenidos = ACADEMICO_AUXILIAR.getAcademicosPorAreaEstudios(areaEstudios);
        }
        catch (ErrorDAO error) {
            fail("Fallido getAcademicosPorAreaEstudios");
        }
        assertNotNull(listaAcademicosObtenidos, "getAcademicosPorAreaEstudios");
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionExitosa () {
        String categoria = "Dramaturgo";
        List<AcademicoDTO> listaAcademicosObtenidos = null;
        try {
            listaAcademicosObtenidos = ACADEMICO_AUXILIAR.getAcademicosPorCategoriaContratacion(categoria);
        }
        catch (ErrorDAO error) {
            fail("Fallido getAcademicosPorAreaEstudios");
        }
        assertNotNull(listaAcademicosObtenidos, "pruebaGetAcademicosPorCategoriaContratacionExitosa");
    }

    @Test
    void pruebaGetAcademicosPorRegionExitosa () {
        String region = "XALAPA";
        List<AcademicoDTO> listaAcademicosObtenidos = null;
        int tamanoEsperado = 1;
        try {
            listaAcademicosObtenidos = ACADEMICO_AUXILIAR.getAcademicosPorRegion(region);
        }
        catch (ErrorDAO error) {
            fail("Fallido pruebaGetAcademicosPorRegionExitosa");
        }
        assertEquals(tamanoEsperado, listaAcademicosObtenidos.size(), "pruebaGetAcademicosPorRegionExitosa");
    }

    @Test
    void pruebaGetAcademicosPorRegionCadenaNoValida () {
        String region = "";
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorRegion(region), "pruebaGetAcademicosPorRegionCadenaNoValida");
    }

    @Test
    void pruebaGetAcademicosPorRegionCadenaNula () {
        String region = null;
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorRegion(region), "pruebaGetAcademicosPorRegionCadenaNoValida");
    }
    @Test
    void pruebaGetAcademicoPorIdPersona () {
        int idpersona = 1;
        try {
            Optional<AcademicoDTO> academicoObtenido = ACADEMICO_AUXILIAR.getPorId(idpersona);
            assertTrue(academicoObtenido.isPresent(), "pruebaGetAcademicoPorIdPersona");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetAcademicoPorIdPersona");
        }
    }

    @Test
    void pruebaAgregarExitoso () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidos("Llamas");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("9877985");
        academicoDTO.setNumeroPersonal("34563");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        academicoDTO.setCategoriaContratacion("Por Horas");
        academicoDTO.setIdFacultad(1);

        int esperado = 2;
        int obtenido = 0;

        try {
            obtenido = ACADEMICO_AUXILIAR.agregar(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            System.out.println(errorDAO.getMessage());
            fail("Fallida agregarExitoso");
        }
        assertEquals(esperado,obtenido, "pruebaAgregarAcademicoExitoso");

    }

    @Test
    void pruebaAgregarCedulaRepetidaFallida () {
        System.out.println("pruebaAgregarCedulaRepetidaFallida");
        AcademicoDTO academico = new AcademicoDTO();
        academico.setNombre("Manuel");
        academico.setApellidos("Llamas");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("123");
        academico.setNumeroPersonal("453");
        academico.setAreaEstudios("economico-administrativo");
        academico.setCorreoElectronico("hernan@Institucion.mx");
        academico.setNumeroTelefonico("523311756675");
        academico.setCategoriaContratacion("Por Horas");
        academico.setIdFacultad(1);
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.agregar(academico));
    }

    @Test
    void pruebaModificarExitosa () {
        System.out.println("pruebaEditarAcademicoExitoso");
        AcademicoDTO academico = new AcademicoDTO();
        academico.setNombre("Fernando");
        academico.setApellidos("Hernandez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("200011");
        academico.setNumeroPersonal("4564");
        academico.setAreaEstudios("tecnica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);

        int esperado = 3;
        int obtenido = 0;
        try {
            obtenido = ACADEMICO_AUXILIAR.modificar(academico);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida modificarExitoso");
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaGetTodosExitoso () {
        List<AcademicoDTO> listaAcademicosObtenidos = null;
        int tamanoEsperado = 2;
        try {
            listaAcademicosObtenidos = ACADEMICO_AUXILIAR.getTodos();
        }
        catch (ErrorDAO errorDAO) {
            fail("Faliida pruebaGetTodosExitoso" );
        }
        assertEquals(tamanoEsperado, listaAcademicosObtenidos.size(), "pruebaGetTodosExitoso");
    }
}