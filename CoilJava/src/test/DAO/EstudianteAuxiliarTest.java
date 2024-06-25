package test.DAO;

import DAO.EstudianteAuxiliar;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class EstudianteAuxiliarTest {
    private final EstudianteAuxiliar ESTUDIANTE_AUXILIAR = new EstudianteAuxiliar();
    private static  EstudianteDTO estudianteRegistrado1;
    private static EstudianteDTO estudianteRegistrado2;

    private static void crearObjetosParaTest () {
        estudianteRegistrado1 = new EstudianteDTO();
        estudianteRegistrado1.setIdPersona(1);
        estudianteRegistrado1.setNombre("Jose");
        estudianteRegistrado1.setApellidos("Lopez Perez");
        estudianteRegistrado1.setIdUniversidad(1);
        estudianteRegistrado1.setIdEstudiante(1);
        estudianteRegistrado1.setMatricula("zs22013690");

        estudianteRegistrado2 = new EstudianteDTO();
        estudianteRegistrado2.setIdPersona(2);
        estudianteRegistrado2.setNombre("Juan");
        estudianteRegistrado2.setApellidos("Negrete Incumplido");
        estudianteRegistrado2.setIdUniversidad(1);
        estudianteRegistrado2.setIdEstudiante(2);
        estudianteRegistrado2.setMatricula("zs22013688");
    }

    @BeforeAll
    static void prepararBaseDatosParaTest () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
        crearObjetosParaTest();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
    }

    @BeforeEach
    void reiniciarDatosParaTest () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (1, 'Jose', 'Lopez Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (2, 'Juan', 'Negrete Incumplido', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 1, 'zs22013690')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 2, 'zs22013688')");
    }

    @AfterEach
    void limpiarBaseDatosParaTest () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaPersona();
    }

    @AfterAll
    static void limpiarBaseDatosFinDeTest () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaAgregarEstudianteExitosa () {
        int esperado = 3;
        int obtenido = 0;
        try {
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setNombre("Emmanuel");
            estudianteDTO.setApellidos("Pale");
            estudianteDTO.setMatricula("zs20013600");
            estudianteDTO.setIdUniversidad(1);
            obtenido = ESTUDIANTE_AUXILIAR.agregar(estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteExitoso\n" + error.getMessage());
        }
        assertEquals(esperado, obtenido, "pruebaAgregarEstudianteExitoso");
    }

    @Test
    void pruebaAgregarEstudianteDuplicado () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.agregar(estudianteRegistrado1),"pruebaAgregarEstudianteDuplicado");
    }

    @Test
    void pruebaAgregarEstudianteSinDatos () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.agregar(new EstudianteDTO()),"pruebaAgregarEstudianteSinDatos");
    }

    @Test
    void pruebaAgregarEstudianteParametroNulo () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.agregar(null),"pruebaAgregarEstudianteParametroNulo");
    }

    @Test
    void pruebaAgregarEstudianteMatriculaInvalida () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Emmanuel");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs 22");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_AUXILIAR.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaAgregarEstudianteMatriculaInvalida");
    }

    @Test
    void pruebaAgregarEstudianteMatriculaNula () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Emmanuel");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula(null);
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_AUXILIAR.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaAgregarEstudianteMatriculaNula");
    }

    @Test
    void pruebaAgregarEstudianteNombreInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Emmanuel@123");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_AUXILIAR.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaAgregarEstudianteNombreInvalido");
    }

    @Test
    void pruebaAgregarEstudianteApellidosInvalidos () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopz@_123");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_AUXILIAR.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaAgregarEstudianteApellidosInvalidos");
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInexistente () {
        try {
            EstudianteDTO estudiante = new EstudianteDTO();
            estudiante.setNombre("Jose");
            estudiante.setApellidos("Lopez");
            estudiante.setMatricula("zs22013690");
            estudiante.setIdUniversidad(10);
            assertThrows(ErrorDAO.class, () -> ESTUDIANTE_AUXILIAR.agregar(estudiante), "pruebaAgregarEstudianteUniversidadInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteUniversidadInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInvalida () {
        try {
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(0);
            assertThrows(ErrorDAO.class, () -> ESTUDIANTE_AUXILIAR.agregar(estudianteDTO), "pruebaAgregarEstudianteUniversidadInvalida");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteUniversidadInvalida\n" + error.getMessage());
        }
    }

    @Test
    void pruebaModificarExitosa () {
        EstudianteDTO estudiante = new EstudianteDTO();
        int obtenido = -1;
        int esperado = 2;
        try {
            estudiante.setNombre("Jose");
            estudiante.setApellidos("López");
            estudiante.setMatricula(estudianteRegistrado1.getMatricula());
            estudiante.setIdUniversidad(1);
            obtenido = ESTUDIANTE_AUXILIAR.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaModificarExitosa");
    }

    @Test
    void pruebaModifificarEstudianteVacio () {
        int esperado = 0;
        int obtenido = -1;
        try {
            obtenido = ESTUDIANTE_AUXILIAR.modificar(new EstudianteDTO());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarEstudianteVacio");
        }
        assertEquals(esperado,obtenido,"pruebaModificarEstudianteVacio");
    }

    @Test
    void pruebaModifificarMatriculaInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013601");
        estudiante.setIdUniversidad(1);
        int esperado = 0;
        int obtenido = -1;

        try {
            obtenido = ESTUDIANTE_AUXILIAR.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarMatriculaInexistente\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaModificarMatriculaInexistente");
    }

    @Test
    void pruebaModifificarConUniversidadInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(10);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.modificar(estudiante),"pruebaModificarEstudianteUniversidadInexistente");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadExitosa () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadExitosa\n" + error.getMessage());
        }
        assertTrue(obtenido.isPresent());
        assertEquals(estudianteRegistrado1,obtenido.get(),"pruebaGetEstudiantePorMatriculaYUniversidadExitosa");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad("zs22013029",1);
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente\n" +  error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadInvalido () {
        try {
            Optional<EstudianteDTO> optionalObtenido = ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),0);
            assertTrue(optionalObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadInvalido");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadInvalido\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula () {
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad(null,1), "pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInvalida () {
        assertThrows(ErrorDAO.class, ()->ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad("123@_ 10",1), "pruebaGetEstudiantePorMatriculaInvalida");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaExitosa () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_AUXILIAR.getEstudiantePorMatricula(estudianteRegistrado1.getMatricula());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaExitosa\n" + error.getMessage());
        }
        assertTrue(obtenido.isPresent());
        assertEquals(estudianteRegistrado1,obtenido.get(),"pruebaGetEstudiantePorMatriculaExitosa");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaInexistente () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_AUXILIAR.getEstudiantePorMatricula("zs22013029");
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorMatriculaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaInexistente\n" +  error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaNula () {
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_AUXILIAR.getEstudiantePorMatricula(null), "pruebaGetEstudiantePorMatriculaNula");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaInvalida () {
        assertThrows(ErrorDAO.class, ()->ESTUDIANTE_AUXILIAR.getEstudiantePorMatricula("S123@_/10"), "pruebaGetEstudiantePorMatriculaInvalida");
    }
}