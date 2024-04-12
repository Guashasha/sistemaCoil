package test.Logica;

import Logica.DAO.DAOAcademico;
import Logica.Dominio.Academico;
import Logica.Dominio.Persona;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DAOAcademicoTest {
    private static final DAOAcademico INSTANCIA = new DAOAcademico();

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
    void pruebaGetAcademicosPorFacultadExitosa () {
        System.out.println("pruebaGetAcademicosPorFacultadExitosa");
        String nombreFacultad = "Economia";
        List<Academico> listaAcademicos = null;
        try {
            listaAcademicos= INSTANCIA.getAcademicosPorFacultad(nombreFacultad);
        }
        catch (ErrorDAO error) {
            fail("Fallida : pruebaGetAcademicosPorFacultadExitosa");
        }
        int tamanoEsperado = 2;
        assertEquals(tamanoEsperado, listaAcademicos.size());
    }

    @Test
    void pruebaGetAcademicosPorFacultadCadenaInvalida () {
        System.out.println("pruebaGetAcademicosPorFacultadCadenaInvalida");
        String nombreFacultad = null;

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorFacultad(nombreFacultad));
    }

    @Test
    void pruebaGetAcademicosPorFacultadCadenaEspacios () {
        System.out.println("pruebaGetAcademicosPorFacultadCadenaEspacios");
        String nombreFacultad = "";

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorFacultad(nombreFacultad));
    }

    @Test
    void pruebaGetAcademicoPorCedulaExitoso () {
        System.out.println("pruebaFetAcademicoPorCedulaExitoso");

        Academico academicoObtenido = null;
        String cedula = "ABC123";
        try {
            Optional optionalAcademico = INSTANCIA.getAcademicoPorCedula(cedula);
            academicoObtenido = (Academico) optionalAcademico.get();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaExitoso");
        }
        assertNotNull(academicoObtenido);

    }

   @Test
   void pruebaGetAcademicoPorCedulaCadenaInvalida () {
        System.out.println("pruebaGetAcademicoPorCedulaCadanaInvalida");

        String cedula = null;

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicoPorCedula(cedula));
   }

   @Test
   void pruebaGetAcademicoPorCedulaCadenaPorEspacios () {
        System.out.println("pruebaGetAcademicoPorCedulaCadenaPorEspacios");
        String cedula = "";

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicoPorCedula(cedula));
   }

    @Test
    void pruebaGetAcademicosPorUniversidadExitosa () {
        System.out.println("pruebaGetAcademicosPorUniversidadExitosa");
        String nombreUniversidad = "Universidad Veracruzana";

        List<Academico> listaAcademicos = null;
        int tamanoEsperado = 2;

        try {
            listaAcademicos = INSTANCIA.getAcademicosPorUniversidad(nombreUniversidad);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruenaGetAcademicosPorUniversidadExitosa");
        }

        assertEquals(tamanoEsperado, listaAcademicos.size());
    }

    @Test
    void pruebaGetAcademicosPorUniversidadCadenaNoValida () {
        System.out.println("pruebaGetAcademicosPorUniversidadCadenaNoValida");
        String universidad = null;

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorUniversidad(universidad));
    }

    @Test
    void getAcademicosPorAreaEstudios () {
        System.out.println("getAcademicosPorAreaEstudios");
        String areaEstudios = "Filosofia";
        List<Academico> listaAcademicos = null;
        try {
            listaAcademicos = INSTANCIA.getAcademicosPorAreaEstudios(areaEstudios);
        }
        catch (ErrorDAO error) {
            fail("Fallido getAcademicosPorAreaEstudios");
        }
        assertNotNull(listaAcademicos);

    }

    @Test
    void getAcademicosPorAreaEstudiosCadenaNoValidaFallida () {
        System.out.println("getAcademicosPorAreaEstudiosCadenaNoValidaFallida");

        String areaAcademica = null;

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorAreaEstudios(areaAcademica));
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionExitosa () {
        System.out.println("pruebaGetAcademicosPorCategoriaContratacionExitosa");
        String categoria = "Dramaturgo";
        List<Academico> listaAcademicos = null;
        try {
            listaAcademicos = INSTANCIA.getAcademicosPorCategoriaContratacion(categoria);
        }
        catch (ErrorDAO error) {
            fail("Fallido getAcademicosPorAreaEstudios");
        }
        assertNotNull(listaAcademicos);
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionNulaFallida () {
        System.out.println("pruebaGetAcademicosPorCategoriaContratacionFallida");
        String categoria = null;

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorCategoriaContratacion(categoria));
    }

    @Test
    void pruebaGetAcademicosPorCategoriaContratacionEspacioFallida () {
        System.out.println("pruebaGetAcademicosPorCategoriaContratacionEspacioFallida");
        String categoria = "";

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorCategoriaContratacion(categoria));
    }

    @Test
    void pruebaGetAcademicosPorRegionExitosa () {
        System.out.println("pruebaGetAcademicosPorRegionExitosa");
        String region = "XALAPA";
        List<Academico> listaAcademicos = null;
        int tamanoEsperado = 2;
        try {
            listaAcademicos = INSTANCIA.getAcademicosPorRegion(region);
        }
        catch (ErrorDAO error) {
            fail("Fallido pruebaGetAcademicosPorRegionExitosa");
        }
        assertNotNull(listaAcademicos);
        assertEquals(tamanoEsperado, listaAcademicos.size());
    }

    @Test
    void pruebaGetAcademicosPorRegionCadenaNoValida () {
        System.out.println("pruebaGetAcademicosPorRegionCadenaNoValida");
        String region = "";

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorRegion(region));

    }

    @Test
    void pruebaGetAcademicosPorRegionCadenaNula () {
        System.out.println("pruebaGetAcademicosPorRegionCadenaNoValida");
        String region = null;

        assertThrows(ErrorDAO.class, ()-> INSTANCIA.getAcademicosPorRegion(region));
    }
    @Test
    void pruebaGetAcademicoPorIdPersona () {
        System.out.println("pruebaGetAcademicoPorIdPersona");
        int idpersona = 1;
        Academico academico = null;
        Optional optional = null;
        try {
            optional = INSTANCIA.getAcademicoPorIdPersona(idpersona);
            academico = (Academico) optional.get();
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetAcademicoPorIdPersona");
        }

        assertNotNull(academico);
    }

    @Test
    void pruebaAgregarExitoso () {
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
            obtenido = INSTANCIA.agregar(academico);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida agregarExitoso");
        }

        assertEquals(esperado,obtenido);

    }

    @Test
    void pruebaModificarExitosa () {
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

        int esperado = 3;
        int obtenido = 0;
        try {
            obtenido = INSTANCIA.modificar(academico);
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
        List<Academico> academicos = null;
        int esperado = 2;
        try {
            academicos = INSTANCIA.getTodos();
        }
        catch (ErrorDAO errorDAO) {
            fail("Faliida pruebaGetTodosExitoso" );
        }

        assertEquals(esperado, academicos.size());
    }

    @Test
    void resultSetAObjeto () {
    }
}