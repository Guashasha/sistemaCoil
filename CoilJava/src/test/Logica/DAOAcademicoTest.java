package test.Logica;

import Logica.DAO.DAOAcademico;
import Logica.Dominio.Academico;
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

        assertThrows()
    }

    @Test
    void getAcademicosPorAreaEstudios () {

    }

    @Test
    void getAcademicosPorCategoriaContratacion () {
    }

    @Test
    void getAcademicosPorRegion () {
    }

    @Test
    void getAcademicoPorIdPersona () {
    }

    @Test
    void agregar () {
    }

    @Test
    void modificar () {
    }

    @Test
    void getPorId () {
    }

    @Test
    void getTodos () {
    }

    @Test
    void resultSetAObjeto () {
    }
}