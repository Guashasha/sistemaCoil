package test.DAO;

import DAO.EstudianteDAO;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static test.ConfiguracionPrueba.ejecutarInstruccionSQL;
import static org.junit.jupiter.api.Assertions.*;

class EstudianteDAOTest {
    private final EstudianteDAO ESTUDIANTE_DAO = new EstudianteDAO();
    private static EstudianteDTO estudianteRegistrado1;
    private static EstudianteDTO estudianteRegistrado2;

    static void crearObjetosParaTest () {
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
        ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México');");
        ejecutarInstruccionSQL("INSERT INTO universidad (idUniversidad,nombre,paisOrigen) VALUES (1,'Universidad Veracruzana',1), (2,'BUAP',1);");
    }

    @BeforeEach
    void reiniciarBaseDatosParaTest () {

        ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (1, 'Jose', 'Lopez Perez', 1)");
        ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (2, 'Juan', 'Negrete Incumplido', 1)");
        ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 1, 'zs22013690')");
        ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 2, 'zs22013688')");
    }

    @AfterEach
    void limpiarBaseDatos () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaPersona();
    }

    @AfterAll
    static void limpiarBaseDatosFinTest () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaCuenta();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaAgregarEstudianteExitoso () {
        int filasAfectadasEsperadas = 3;
        int filasAfectadasObtenidas = 0;

        try {
            EstudianteDTO estudiante = new EstudianteDTO();
            estudiante.setNombre("Jose");
            estudiante.setApellidos("Lopez");
            estudiante.setMatricula("zs22013600");
            estudiante.setIdUniversidad(1);
            filasAfectadasObtenidas = ESTUDIANTE_DAO.agregar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteExitoso\n" + error.getMessage());
        }
        assertEquals(filasAfectadasEsperadas, filasAfectadasObtenidas, "pruebaAgregarEstudianteExitoso");
    }

    @Test
    void pruebaAgregaEstudianteDuplicado () {
        int filasAfectadasEsperadas = 3;
        int filasAfectadasObtenidas = 0;

        try {
            filasAfectadasObtenidas = ESTUDIANTE_DAO.agregar(estudianteRegistrado1);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregaEstudianteDuplicado\n" + error.getMessage());
        }
        assertEquals(filasAfectadasEsperadas,filasAfectadasObtenidas,"pruebaAgregaEstudianteDuplicado");
     }

    @Test
    void pruebaAgregarEstudianteVacioFallida () {
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(new EstudianteDTO()),"pruebaAgregarEstudianteVacioFallida");
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(100);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.agregar(estudiante),"pruebaAgregarEstudianteUniversidadInexistente");
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInvalida () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(-1);
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(estudiante),"pruebaAgregarEstudianteUniversidadInvalida");
    }

    @Test
    void pruebaModificarExitosa () {
        EstudianteDTO estudiante = new EstudianteDTO();
        int filasAfectadasObtenidas = 0;
        int filasAfectadasEsperadas = 2;
        try {
            estudiante.setNombre("Mario");
            estudiante.setApellidos("López");
            estudiante.setMatricula(estudianteRegistrado1.getMatricula());
            estudiante.setIdUniversidad(1);
            filasAfectadasObtenidas = ESTUDIANTE_DAO.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarExitosa\n" + error.getMessage());
        }
        assertEquals(filasAfectadasEsperadas,filasAfectadasObtenidas);
    }

    @Test
    void pruebaModificarEstudianteVacio () {
        int filasAfectadasEsperadas = 0;
        int filasAfectadasObtenidas = -1;
        try {
            filasAfectadasObtenidas = ESTUDIANTE_DAO.modificar(new EstudianteDTO());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarEstudianteVacio");
        }
        assertEquals(filasAfectadasEsperadas,filasAfectadasObtenidas,"pruebaModificarEstudianteVacio");
    }

    @Test
    void pruebaModificarMatriculaInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013601");
        estudiante.setIdUniversidad(1);
        int filasAfectadasEsperadas = 0;
        int filasAfectadasObtenidas = -1;

        try {
            filasAfectadasObtenidas = ESTUDIANTE_DAO.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarMatriculaInexistente\n" + error.getMessage());
        }
        assertEquals(filasAfectadasEsperadas,filasAfectadasObtenidas,"pruebaModificarMatriculaInexistente");
    }

    @Test
    void pruebaModificarEstudianteUniversidadInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula(estudianteRegistrado1.getMatricula());
        estudiante.setIdUniversidad(10);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.modificar(estudiante),"pruebaModificarEstudianteUniversidadInexistente");
    }

    @Test
    void pruebaGetEstudiantePorIDExitosa () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getPorId(estudianteRegistrado1.getIdEstudiante());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa\n" + error.getMessage());
        }
        assertEquals(estudianteRegistrado1, estudianteObtenido.get(), "pruebaGetEstudiantePorIDExitosa");
    }

    @Test
    void pruebaGetEstudiantePorIdInexistente () {
        try {
            Optional<EstudianteDTO> estudianteObtenido = ESTUDIANTE_DAO.getPorId(10);
            assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorIdInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdInvalido () {
        try {
            Optional<EstudianteDTO> estudianteObtenido = ESTUDIANTE_DAO.getPorId(-1);
            assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorIdInvalido");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdInvalido\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaExitosa () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorIdPersona(estudianteRegistrado1.getIdPersona());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaExitosa\n" + error.getMessage());
        }
        assertEquals(estudianteRegistrado1,estudianteObtenido.get(),"pruebaGetEstudiantePorIdPersonaExitosa");
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaInexistente () {
        try {
            Optional<EstudianteDTO> estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorIdPersona(10);
            assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorIdPersonaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaInexistente" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaInvalido () {
        try {
            Optional<EstudianteDTO> estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorIdPersona(-1);
            assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorIdPersonaInvalido");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaInvalido" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadExitosa () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadExitosa\n" + error.getMessage());
        }
        assertEquals(estudianteRegistrado1,estudianteObtenido.get(),"pruebaGetEstudiantePorMatriculaYUniversidadExitosa");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad("S21030125",estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente\n" + error.getMessage());
        }
        assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadInexistente () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidaInexistente\n" + error.getMessage());
        }
        assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidaInexistente");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadNegativo () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),-10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadNegativo\n" + error.getMessage());
        }
        assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadNegativo");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(null,estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula\n" + error.getMessage());
        }
        assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula");
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadExitosa () {
        List<EstudianteDTO> listaEsperada = new ArrayList<>();
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(estudianteRegistrado1);
        listaEsperada.add(estudianteRegistrado2);
        try {
            listaObtenida = ESTUDIANTE_DAO.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(1);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadExitosa\n" + error.getMessage());
        }
        assertEquals(listaEsperada,listaObtenida,"pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadExitosa");
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdInexistente () {
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = ESTUDIANTE_DAO.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdInexistente\n" + error.getMessage());
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdInexistente");
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdNegativo () {
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = ESTUDIANTE_DAO.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(-10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdNegativo\n" + error.getMessage());
        }
        assertTrue(listaObtenida.isEmpty(),"pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdNegativo");
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadSinResultados () {
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = ESTUDIANTE_DAO.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(2);
        }
        catch (ErrorDAO error) {
            fail("Fallida: getEstudiantesSinColaboracionActivaOVinculadaPorUniversidadSinResultados\n" + error.getMessage());
        }
        assertTrue(listaObtenida.isEmpty(),"getEstudiantesSinColaboracionActivaOVinculadaPorUniversidadSinResultados");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaExitosa () {
        Optional<EstudianteDTO> estudianteObtenido = Optional.empty();
        try {
            estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatricula(estudianteRegistrado1.getMatricula());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatricula\n" + error.getMessage());
        }
        assertEquals(estudianteRegistrado1,estudianteObtenido.get(),"pruebaGetEstudiantePorMatriculaExitosa");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaInexistente () {
        try {
            Optional<EstudianteDTO> estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatricula("zs22013029");
            assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaInexistente\n" +  error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaNula () {
        try {
            Optional<EstudianteDTO> estudianteObtenido = ESTUDIANTE_DAO.getEstudiantePorMatricula(null);
            assertTrue(estudianteObtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaNula");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaNula\n" +  error.getMessage());
        }
    }

    @Test
    void pruebaGetTodosExitosa () {
        List<EstudianteDTO> listaEsperada = new ArrayList<>();
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(estudianteRegistrado1);
        listaEsperada.add(estudianteRegistrado2);

        try {
            listaObtenida = ESTUDIANTE_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetTodosExitosa\n" + error.getMessage());
        }

        assertEquals(listaEsperada,listaObtenida,"pruebaGetTodosExitosa");
    }
}