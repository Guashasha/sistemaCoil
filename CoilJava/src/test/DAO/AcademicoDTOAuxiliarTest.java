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

class AcademicoDTOAuxiliarTest {
    private static final AcademicoAuxiliar ACADEMICO_AUXILIAR = new AcademicoAuxiliar();

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('UniversidadDTO Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico) VALUES ('123', '123456', 1, 'tecnica', 'jose@gmail.com', '522288536230');");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Esther', 'Herrara', 'Martinez', 1);");
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
        System.out.println("pruebaGetAcademicosPorFacultadExitosa");
        String nombreFacultad = "Economia";
        List<AcademicoDTO> listaAcademicoDTOS = null;
        try {
            listaAcademicoDTOS = ACADEMICO_AUXILIAR.getAcademicosPorFacultad(nombreFacultad);
        }
        catch (ErrorDAO error) {
            fail("Fallida : pruebaGetAcademicosPorFacultadExitosa");
        }
        int tamanoEsperado = 1;
        assertEquals(tamanoEsperado, listaAcademicoDTOS.size());
    }

    @Test
    void pruebaGetAcademicosPorFacultadCadenaInvalida () {
        System.out.println("pruebaGetAcademicosPorFacultadCadenaInvalida");
        String nombreFacultad = null;

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorFacultad(nombreFacultad));
    }

    @Test
    void pruebaGetAcademicosPorFacultadCadenaEspacios () {
        System.out.println("pruebaGetAcademicosPorFacultadCadenaEspacios");
        String nombreFacultad = "";

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorFacultad(nombreFacultad));
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitoso () {
        System.out.println("pruebaGetAcademicoPorCedulaExitoso");

        AcademicoDTO academicoDTOObtenido = null;
        String cedula = "123";
        try {
            Optional<AcademicoDTO> optionalAcademico = ACADEMICO_AUXILIAR.getAcademicoPorCedula(cedula);
            assertTrue(optionalAcademico.isPresent());
            academicoDTOObtenido = optionalAcademico.get();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitoso");
        }
        assertNotNull(academicoDTOObtenido);

    }

   @Test
   void pruebaGetAcademicoPorCedulaCadenaInvalida () {
        System.out.println("pruebaGetAcademicoPorCedulaCadanaInvalida");

        String cedula = null;

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicoPorCedula(cedula));
   }

   @Test
   void pruebaGetAcademicoPorCedulaCadenaPorEspacios () {
        System.out.println("pruebaGetAcademicoPorCedulaCadenaPorEspacios");
        String cedula = "";

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicoPorCedula(cedula));
   }

    @Test
    void pruebaGetAcademicosPorUniversidadExitosa () {
        System.out.println("pruebaGetAcademicosPorUniversidadExitosa");
        String nombreUniversidad = "UniversidadDTO Veracruzana";

        List<AcademicoDTO> listaAcademicoDTOS = null;
        int tamanoEsperado = 2;

        try {
            listaAcademicoDTOS = ACADEMICO_AUXILIAR.getAcademicosPorUniversidad(nombreUniversidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruenaGetAcademicosPorUniversidadExitosa");
        }

        assertEquals(tamanoEsperado, listaAcademicoDTOS.size());
    }

    @Test
    void pruebaGetAcademicosPorUniversidadCadenaNoValida () {
        System.out.println("pruebaGetAcademicosPorUniversidadCadenaNoValida");
        String universidad = null;

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorUniversidad(universidad));
    }

    @Test
    void getAcademicosPorAreaEstudios () {
        System.out.println("getAcademicosPorAreaEstudios");
        String areaEstudios = "economico-administrativo";
        List<AcademicoDTO> listaAcademicoDTOS = null;
        try {
            listaAcademicoDTOS = ACADEMICO_AUXILIAR.getAcademicosPorAreaEstudios(areaEstudios);
        }
        catch (ErrorDAO error) {
            fail("Fallido getAcademicosPorAreaEstudios");
        }
        assertNotNull(listaAcademicoDTOS);

    }

    @Test
    void getAcademicosPorAreaEstudiosCadenaNoValidaFallida () {
        System.out.println("getAcademicosPorAreaEstudiosCadenaNoValidaFallida");

        String areaAcademica = null;

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorAreaEstudios(areaAcademica));
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionExitosa () {
        System.out.println("pruebaGetAcademicosPorCategoriaContratacionExitosa");
        String categoria = "Dramaturgo";
        List<AcademicoDTO> listaAcademicoDTOS = null;
        try {
            listaAcademicoDTOS = ACADEMICO_AUXILIAR.getAcademicosPorCategoriaContratacion(categoria);
        }
        catch (ErrorDAO error) {
            fail("Fallido getAcademicosPorAreaEstudios");
        }
        assertNotNull(listaAcademicoDTOS);
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionNulaFallida () {
        System.out.println("pruebaGetAcademicosPorCategoriaContratacionFallida");
        String categoria = null;

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorCategoriaContratacion(categoria));
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionEspacioFallida () {
        System.out.println("pruebaGetAcademicosPorCategoriaContratacionEspacioFallida");
        String categoria = "";

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorCategoriaContratacion(categoria));
    }

    @Test
    void pruebaGetAcademicosPorRegionExitosa () {
        System.out.println("pruebaGetAcademicosPorRegionExitosa");
        String region = "XALAPA";
        List<AcademicoDTO> listaAcademicoDTOS = null;
        int tamanoEsperado = 1;
        try {
            listaAcademicoDTOS = ACADEMICO_AUXILIAR.getAcademicosPorRegion(region);
        }
        catch (ErrorDAO error) {
            fail("Fallido pruebaGetAcademicosPorRegionExitosa");
        }
        assertNotNull(listaAcademicoDTOS);
        assertEquals(tamanoEsperado, listaAcademicoDTOS.size());
    }

    @Test
    void pruebaGetAcademicosPorRegionCadenaNoValida () {
        System.out.println("pruebaGetAcademicosPorRegionCadenaNoValida");
        String region = "";

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorRegion(region));

    }

    @Test
    void pruebaGetAcademicosPorRegionCadenaNula () {
        System.out.println("pruebaGetAcademicosPorRegionCadenaNoValida");
        String region = null;

        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.getAcademicosPorRegion(region));
    }
    @Test
    void pruebaGetAcademicoPorIdPersona () {
        System.out.println("pruebaGetAcademicoPorIdPersona");
        int idpersona = 1;
        AcademicoDTO academicoDTO = null;

        try {
            Optional<AcademicoDTO> optionalAcademicoDTO = ACADEMICO_AUXILIAR.getPorId(idpersona);
            assertTrue(optionalAcademicoDTO.isPresent());
            academicoDTO = optionalAcademicoDTO.get();
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetAcademicoPorIdPersona");
        }

        assertNotNull(academicoDTO);
    }

    @Test
    void pruebaAgregarExitoso () {
        System.out.println("pruebaAgregarAcademicoExitoso");

        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Hernan");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
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

        assertEquals(esperado,obtenido);

    }

    @Test
    void pruebaAgregarCedulaRepetidaFallida () {
        System.out.println("pruebaAgregarCedulaRepetidaFallida");
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Manuel");
        academicoDTO.setApellidoPaterno("Llamas");
        academicoDTO.setApellidoMaterno("Villa Señor");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("123");
        academicoDTO.setNumeroPersonal("453");
        academicoDTO.setAreaEstudios("economico-administrativo");
        academicoDTO.setCorreoElectronico("hernan@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756675");
        academicoDTO.setCategoriaContratacion("Por Horas");
        academicoDTO.setIdFacultad(1);
        assertThrows(ErrorDAO.class, ()-> ACADEMICO_AUXILIAR.agregar(academicoDTO));

    }

    @Test
    void pruebaModificarExitosa () {
        System.out.println("pruebaEditarAcademicoExitoso");
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre("Fernando");
        academicoDTO.setApellidoPaterno("Hernandez");
        academicoDTO.setApellidoMaterno("Lopez");
        academicoDTO.setIdUniversidad(1);
        academicoDTO.setCedulaProfesional("200011");
        academicoDTO.setNumeroPersonal("4564");
        academicoDTO.setAreaEstudios("tecnica");
        academicoDTO.setCorreoElectronico("fer@Institucion.mx");
        academicoDTO.setNumeroTelefonico("523311756676");
        academicoDTO.setIdFacultad(1);

        int esperado = 3;
        int obtenido = 0;
        try {
            obtenido = ACADEMICO_AUXILIAR.modificar(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida modificarExitoso");
        }

        assertEquals(esperado, obtenido);
    }

    @Test
    void getPorId () {
    }

    @Test
    void pruebaGetTodosExitoso () {
        System.out.println("pruebaGetTodosExitoso");
        List<AcademicoDTO> academicoDTOS = null;
        int esperado = 2;
        try {
            academicoDTOS = ACADEMICO_AUXILIAR.getTodos();
        }
        catch (ErrorDAO errorDAO) {
            fail("Faliida pruebaGetTodosExitoso" );
        }

        assertEquals(esperado, academicoDTOS.size());
    }

    @Test
    void resultSetAObjeto () {
    }
}