package test.AccesoADatos;

import AccesoADatos.EstudianteDB;
import AccesoADatos.UniversidadDB;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteDBTest {
    //TODO  getEstudiantePorUniversidad
    // getTodos
    @BeforeEach
    void setUp () {
        registrarUniversidadMexicana();
        agregarEstudiante();
    }

    @AfterEach
    void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
    }

    @Test
    void pruebaAgregarEstudianteExitoso () {
        System.out.println("pruebaAgregarEstudianteExitoso");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Lara");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(1);

        int esperado = 2;
        int obtenido = 0;

        try {
            obtenido = EstudianteDB.agregarEstudiante(estudiante);

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteExitoso");

        }
        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaAgregarEstudianteVacioFallida () {
        System.out.println("pruebaAgregarEstudianteVacioFallida");

        Estudiante estudiante = new Estudiante();

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));
    }

    @Test
    void pruebaAgregarEstudianteMatriculaLargaFallida () {
        System.out.println("pruebaAgregarEstudianteMatriculaLargaFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Lara");
        estudiante.setMatricula("12345678912345");
        estudiante.setIdUniversidad(1);

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));
    }

    @Test
    void pruebaAgregarEstudianteMatriculaVaciaFallida () {
        System.out.println("pruebaAgregarEstudianteMatriculaVaciaFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Lara");
        estudiante.setMatricula(null);
        estudiante.setIdUniversidad(1);

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));
    }

    @Test
    void pruebaAgregarEstudianteNombreVacioFallida () {
        System.out.println("pruebaAgregarEstudianteNombreVacioFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(null);
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Lara");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(1);

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));

    }

    @Test
    void pruebaAgregarEstudianteApellidoPaternoVacioFallida () {
        System.out.println("pruebaAgregarEstudianteNombreVaciaFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno(null);
        estudiante.setApellidoMaterno("Lara");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(1);

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));

    }

    @Test
    void pruebaAgregarEstudianteApellidoMaternoVacioFallida () {
        System.out.println("pruebaAgregarEstudianteApellidoMaternoVacioFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno(null);
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(1);

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));

    }

    @Test
    void pruebaAgregarEstudianteUniversidadVaciaFallida () {
        System.out.println("pruebaAgregarEstudianteUniversidadVaciaFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("");
        estudiante.setMatricula("zs22013690");

        assertThrows(ErrorDAO.class,
                     () -> EstudianteDB.agregarEstudiante(estudiante));

    }

    @Test
    void pruebaAgregarEstudianteUniversidadInexistenteFallida () {
        System.out.println("pruebaAgregarEstudianteApellidoMaternoVacioFallida");

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Lara");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(10);

        assertThrows(ErrorDAO.class, () -> EstudianteDB.agregarEstudiante(estudiante));

    }

    @Test
    void pruebaGetEstudiantePorIDExitosa () {
        System.out.println("pruebaGetEstudiantePorIDExitosa");

        Estudiante estudianteEsperado = new Estudiante();
        estudianteEsperado.setIdPersona(1);
        estudianteEsperado.setIdEstudiante(1);
        estudianteEsperado.setNombre("Hernan");
        estudianteEsperado.setApellidoPaterno("Gonzales");
        estudianteEsperado.setApellidoMaterno("Mercado");
        estudianteEsperado.setMatricula("zs22013620");
        estudianteEsperado.setIdUniversidad(1);

        Estudiante estudianteObtenido = null;

        try {
            estudianteObtenido = EstudianteDB.getPorId(1);

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa");

        }

        assertEquals(estudianteEsperado.getMatricula(), estudianteObtenido.getMatricula());
        assertEquals(estudianteEsperado.getIdPersona(), estudianteObtenido.getIdPersona());

    }



















    public static void agregarEstudiante () {
        int esperado = 2;

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Hernan");
        estudiante.setApellidoPaterno("Gonzales");
        estudiante.setApellidoMaterno("Mercado");
        estudiante.setMatricula("zs22013620");
        estudiante.setIdUniversidad(1);

        int obtenido = EstudianteDB.agregarEstudiante(estudiante);

        assertEquals(esperado, obtenido);
    }

    public void registrarUniversidadMexicana () {
        int esperado = 1;

        Universidad universidad = new Universidad("UV", "Mexico");
        UniversidadDB universidadDB = new UniversidadDB();

        int obtenido = universidadDB.registrarUniversidad(universidad);

        assertEquals(esperado, obtenido);
    }

}