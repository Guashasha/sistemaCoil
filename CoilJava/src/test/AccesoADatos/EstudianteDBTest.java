package test.AccesoADatos;

import AccesoADatos.EstudianteDB;
import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteDBTest {
    //TODO
    // getTodos
    // editar caso fallido
    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");

        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 1, 'zs22013690')");

    }

    @AfterEach
    void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
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
        estudianteEsperado.setNombre("Jose");
        estudianteEsperado.setApellidoPaterno("Lopez");
        estudianteEsperado.setApellidoMaterno("Perez");
        estudianteEsperado.setMatricula("zs22013690");
        estudianteEsperado.setIdUniversidad(1);

        Estudiante estudianteObtenido = null;

        try {
            estudianteObtenido = EstudianteDB.getPorId(1);

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa " + error.getMessage());

        }

        assertEquals(estudianteEsperado.getMatricula(), estudianteObtenido.getMatricula());
        assertEquals(estudianteEsperado.getIdPersona(), estudianteObtenido.getIdPersona());

    }

    @Test
    void pruebaGetEstudiantePorUniversidadExitosa () {
        System.out.println("pruebaGetEstudiantePorIDExitosa");

        int tamanoListaEsperado = 1;
        int tamanoListaReal = -1;

        List<Estudiante> listaEstudiantes;

        try {
            listaEstudiantes = EstudianteDB.getEstudiantePorUniversidad(1);
            tamanoListaReal = listaEstudiantes.size();

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa " + error.getMessage());

        }

        assertEquals(tamanoListaEsperado, tamanoListaReal);

    }

    @Test
    void pruebaEditarEstudianteExitoso () {
        System.out.println("pruebaEditarEstudianteExitoso");

        Estudiante estudiante = new Estudiante();
        estudiante.setIdPersona(1);
        estudiante.setIdEstudiante(1);
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Perez");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(1);

        int resultadoEsperado = 2;
        int resultadoReal = -1;

        try {
            resultadoReal = EstudianteDB.editarEstudiante(estudiante);

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarEstudianteExitoso " + error.getMessage());

        }

        assertEquals(resultadoEsperado, resultadoReal);


    }




}