package test.AccesoADatos;

import AccesoADatos.AcademicoDB;
import AccesoADatos.UniversidadDB;
import Logica.Dominio.Academico;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;

import static org.junit.jupiter.api.Assertions.*;

// TODO Agregar metodos de consultas.

class AcademicoDBTest {

    @BeforeEach
    void setUp () {
        registrarUniversidadMexicana();
        registrarAcademico();

    }

    @AfterEach
    void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();

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
            obtenido = AcademicoDB.agregarAcademico(academico);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarAcademicoExitoso");
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaAgregarAcademicoVacioFallida () {
        System.out.println("pruebaAgregarAcademicoVacioFallida");
        Academico academico = new Academico();

        assertThrows(ErrorDAO.class,
                     () -> AcademicoDB.agregarAcademico(academico),
                     "Se esperaba que lanzara una excepción ErrorDAO debido a un mal registro");

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

        assertThrows(ErrorDAO.class,
                     () -> AcademicoDB.agregarAcademico(academico),
                     "Se esperaba que lanzara una excepción ErrorDAO debido a una universidad inexistente");

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

        assertThrows(ErrorDAO.class, () -> AcademicoDB.agregarAcademico(academico));
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

        assertThrows(ErrorDAO.class, () -> AcademicoDB.agregarAcademico(academico));

    }


    @Test
    void pruebaAgregarAcademicoCedulaDuplicadaFallida () {
        System.out.println("pruebaAgregarAcademicoCedulaDuplicadaFallida");

        Academico academico = new Academico();
        academico.setNombre("Esther");
        academico.setApellidoPaterno("Ramirez");
        academico.setApellidoMaterno("Escobar");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("5646321");
        academico.setNumeroPersonal("4535");
        academico.setAreaEstudios("Humanidades");
        academico.setCorreoElectronico("Esther@Institucion.mx");
        academico.setNumeroTelefonico("522288536230");
        academico.setCategoriaContratacion("Fijo");
        academico.setIdFacultad(1);

        assertThrows(ErrorDAO.class, () -> AcademicoDB.agregarAcademico(academico));

    }

    @Test
    void pruebaGetAcademicoPorCedulaExitosa () {
        System.out.println("pruebaGetAcademicoPorCedulaExitosa");

        Academico academicoEsperado = new Academico();
        academicoEsperado.setIdPersona(1);
        academicoEsperado.setNombre("Esther");
        academicoEsperado.setApellidoPaterno("Ramirez");
        academicoEsperado.setApellidoMaterno("Escobar");
        academicoEsperado.setIdUniversidad(1);
        academicoEsperado.setCedulaProfesional("5646321");
        academicoEsperado.setNumeroPersonal("4535");
        academicoEsperado.setAreaEstudios("Humanidades");
        academicoEsperado.setCorreoElectronico("Esther@Institucion.mx");
        academicoEsperado.setNumeroTelefonico("522288536230");
        academicoEsperado.setCategoriaContratacion("Fijo");
        academicoEsperado.setIdFacultad(1);

        Academico academicoObtenido = null;

        try {
            academicoObtenido = AcademicoDB.getAcademicoPorCedula("5646321");

        }
        catch (ErrorDAO error) {
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
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoPorCedulaInexistenteFallida");
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
        academico.setCedulaProfesional("201130");
        academico.setNumeroPersonal("4535");
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);

        try {
            obtenido = AcademicoDB.editarAcademico(academico);

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarAcademicoExitoso");
        }
        assertEquals(esperado, obtenido);

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
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarAcademicoExitoso");
        }
        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaEditarAcademicoUniversidadInexistenteFallida () {

        System.out.println("pruebaEditarAcademicoCedulaInexistenteFallida");

        Academico academico = new Academico();
        academico.setNombre("Fernando");
        academico.setApellidoPaterno("Martinez");
        academico.setApellidoMaterno("Ramirez");
        academico.setIdUniversidad(2);
        academico.setCedulaProfesional("201130");
        academico.setNumeroPersonal("4535");
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(1);

        assertThrows(ErrorDAO.class, () -> AcademicoDB.editarAcademico(academico));

    }

    @Test
    void pruebaEditarAcademicoFacultadInexistenteFallida () {

        System.out.println("pruebaEditarAcademicoFacultadInexistenteFallida");

        Academico academico = new Academico();
        academico.setNombre("Fernando");
        academico.setApellidoPaterno("Martinez");
        academico.setApellidoMaterno("Ramirez");
        academico.setIdUniversidad(1);
        academico.setCedulaProfesional("201130");
        academico.setNumeroPersonal("4535");
        academico.setAreaEstudios("Informatica");
        academico.setCorreoElectronico("fer@Institucion.mx");
        academico.setNumeroTelefonico("523311756676");
        academico.setIdFacultad(3);

        assertThrows(ErrorDAO.class, () -> AcademicoDB.editarAcademico(academico));

    }


    public void registrarUniversidadMexicana () {
        int esperado = 1;

        Universidad universidad = new Universidad("UV", "Mexico");
        UniversidadDB universidadDB = new UniversidadDB();

        int obtenido = universidadDB.registrarUniversidad(universidad);

        assertEquals(esperado, obtenido);
    }

    public static void registrarAcademico () {
        int esperado1 = 2;

        Academico academico1 = new Academico();
        academico1.setNombre("Esther");
        academico1.setApellidoPaterno("Ramirez");
        academico1.setApellidoMaterno("Escobar");
        academico1.setIdUniversidad(1);
        academico1.setCedulaProfesional("5646321");
        academico1.setNumeroPersonal("4535");
        academico1.setAreaEstudios("Humanidades");
        academico1.setCorreoElectronico("Esther@Institucion.mx");
        academico1.setNumeroTelefonico("522288536230");
        academico1.setCategoriaContratacion("Fijo");
        academico1.setIdFacultad(1);

        int obtenido1 = AcademicoDB.agregarAcademico(academico1);

        assertEquals(esperado1, obtenido1);

        int esperado2 = 2;

        Academico academico2 = new Academico();
        academico2.setNombre("Fernando");
        academico2.setApellidoPaterno("Martinez");
        academico2.setApellidoMaterno("Ramirez");
        academico2.setIdUniversidad(1);
        academico2.setCedulaProfesional("201130");
        academico2.setNumeroPersonal("4535");
        academico2.setAreaEstudios("Informatica");
        academico2.setCorreoElectronico("fer@Institucion.mx");
        academico2.setNumeroTelefonico("523311756676");
        academico2.setIdFacultad(1);

        int obtenido2 = AcademicoDB.agregarAcademico(academico2);

        assertEquals(esperado2, obtenido2);

    }


}