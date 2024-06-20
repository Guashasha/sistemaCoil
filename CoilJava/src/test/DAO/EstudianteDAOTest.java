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
        ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
    }

    @BeforeEach
    void reiniciarBaseDatosParaTest () {
        ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (1, 'Jose', 'Lopez Perez', 1);");
        ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidos, universidad) VALUES (2, 'Juan', 'Negrete Incumplido', 1);");
        ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 1, 'zs22013690')");
        ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 2, 'zs22013688')");
    }

    @AfterEach
    void limpiarBaseDatosParaTest () {
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
        int esperado = 3;
        int obtenido = 0;

        try {
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs22013600");
            estudianteDTO.setIdUniversidad(1);
            obtenido = ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteExitoso\n" + error.getMessage());
        }
        assertEquals(esperado, obtenido, "pruebaAgregarEstudianteExitoso");
    }

    @Test
    void pruebaAgregaEstudianteDuplicado () {
        int esperado = 3;
        int obtenido = -1;

        try {
            obtenido = ESTUDIANTE_DAO.agregar(estudianteRegistrado1);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregaEstudianteDuplicado\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaAgregaEstudianteDuplicado");
     }

    @Test
    void pruebaAgregarEstudianteVacioFallida () {
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(new EstudianteDTO()),"pruebaAgregarEstudianteVacioFallida");
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
            ESTUDIANTE_DAO.agregar(estudianteDTO);
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
            ESTUDIANTE_DAO.agregar(estudianteDTO);
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
            estudianteDTO.setNombre("Emmanuel@123saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.agregar(estudianteDTO);
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
            estudianteDTO.setApellidos("Lopz@_123asddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaAgregarEstudianteApellidoPaternoInvalido");
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
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidos("Lopez");
        estudianteDTO.setMatricula("zs22013690");
        estudianteDTO.setIdUniversidad(0);
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(estudianteDTO),"pruebaAgregarEstudianteUniversidadInvalida");
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
            obtenido = ESTUDIANTE_DAO.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarExitosa\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido);
    }

    @Test
    void pruebaModificarEstudianteVacio () {
        int esperado = 0;
        int obtenido = -1;
        try {
            obtenido = ESTUDIANTE_DAO.modificar(new EstudianteDTO());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarEstudianteVacio");
        }
        assertEquals(esperado,obtenido,"pruebaModificarEstudianteVacio");
    }

    @Test
    void pruebaModificarMatriculaInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013601");
        estudiante.setIdUniversidad(1);
        int esperado = 0;
        int obtenido = -1;

        try {
            obtenido = ESTUDIANTE_DAO.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarMatriculaInexistente\n" + error.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaModificarMatriculaInexistente");
    }

    @Test
    void pruebaModificarMatriculaInvalida () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs 22");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaModificarMatriculaInvalida");
    }

    @Test
    void pruebaModificarMatriculaNula () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula(null);
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaModificarMatriculaNula");
    }

    @Test
    void pruebaModificarNombreInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Emmanuel@123saddddddddddddddsdsadwwwdwdwdwddwddwdwdwddwdwddwdwdwdwwd");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaModificarNombreInvalido");
    }

    @Test
    void pruebaModificarApellidosInvalidos () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs2201369041213");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaModificarApellidosInvalidos");
    }

    @Test
    void pruebaModificarEstudianteUniversidadInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidos("Lopez");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(10);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.modificar(estudiante),"pruebaModificarEstudianteUniversidadInexistente");
    }

    @Test
    void pruebaGetEstudiantePorIDExitosa () {
        Optional<EstudianteDTO> estudianteDTOOptional = Optional.empty();
        try {
            estudianteDTOOptional = ESTUDIANTE_DAO.getPorId(estudianteRegistrado1.getIdEstudiante());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa " + error.getMessage());
        }
        assertTrue(estudianteDTOOptional.isPresent());
        assertEquals(estudianteRegistrado1, estudianteDTOOptional.get(), "pruebaGetEstudiantePorIDExitosa");
    }

    @Test
    void pruebaGetEstudiantePorIdInexistente () {
        try {
            Optional<EstudianteDTO> obtenido = ESTUDIANTE_DAO.getPorId(10);
            assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorIdInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdInvalido () {
        try {
            Optional<EstudianteDTO> obtenido = ESTUDIANTE_DAO.getPorId(-1);
            assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorIdInvalido");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdInvalido\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaExitosa () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorIdPersona(estudianteRegistrado1.getIdPersona());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaExitosa\n" + error.getMessage());
        }
        assertTrue(obtenido.isPresent());
        assertEquals(estudianteRegistrado1,obtenido.get(),"pruebaGetEstudiantePorIdPersonaExitosa");
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaInexistente () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_DAO.getEstudiantePorIdPersona(10);
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorIdPersonaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaInexistente" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaInvalido () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_DAO.getEstudiantePorIdPersona(-1);
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorIdPersonaInvalido");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaInvalido" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadExitosa () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadExitosa\n" + error.getMessage());
        }
        assertTrue(obtenido.isPresent());
        assertEquals(estudianteRegistrado1,obtenido.get(),"pruebaGetEstudiantePorMatriculaYUniversidadExitosa");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad("S21030125",estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente\n" + error.getMessage());
        }
        assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadMatriculaInexistente");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadInexistente () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidaInexistente\n" + error.getMessage());
        }
        assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidaInexistente");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadNegativo () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),-10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadNegativo\n" + error.getMessage());
        }
        assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadIdUniversidadNegativo");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(null,estudianteRegistrado1.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula\n" + error.getMessage());
        }
        assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorMatriculaYUniversidadMatriculaNula");
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
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_DAO.getEstudiantePorMatricula(estudianteRegistrado1.getMatricula());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatricula\n" + error.getMessage());
        }
        assertTrue(obtenido.isPresent());
        assertEquals(estudianteRegistrado1,obtenido.get(),"pruebaGetEstudiantePorMatriculaExitosa");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaInexistente () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_DAO.getEstudiantePorMatricula("zs22013029");
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorMatriculaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaInexistente\n" +  error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaNula () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_DAO.getEstudiantePorMatricula(null);
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorMatriculaNula");
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