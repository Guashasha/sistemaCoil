package test.DAO;

import DAO.EstudianteAuxiliar;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
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
        estudianteRegistrado1.setApellidos("Lopez");
        estudianteRegistrado1.setIdUniversidad(1);
        estudianteRegistrado1.setIdEstudiante(1);
        estudianteRegistrado1.setMatricula("zs22013690");

        estudianteRegistrado2 = new EstudianteDTO();
        estudianteRegistrado2.setIdPersona(2);
        estudianteRegistrado2.setNombre("Juan");
        estudianteRegistrado2.setApellidos("Negrete");
        estudianteRegistrado2.setIdUniversidad(1);
        estudianteRegistrado2.setIdEstudiante(2);
        estudianteRegistrado2.setMatricula("zs22013688");
    }

    @BeforeAll
    static void prepararBaseDatosParaTest () {
        crearObjetosParaTest();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (idPais,Iso,nombre) VALUES (1,'MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
    }

    @BeforeEach
    void reiniciarDatosParaTest () {
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Juan', 'Negrete', 'Incumplido', 1);");
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
        EstudianteDTO estudiante = new EstudianteDTO();
        try {
            estudiante.setNombre(estudianteRegistrado1.getNombre());
            estudiante.setApellidos(estudianteRegistrado1.getApellidos());
            estudiante.setMatricula(estudianteRegistrado1.getMatricula());
            estudiante.setIdUniversidad(1);
            assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.agregar(estudiante),"pruebaAgregarEstudianteDuplicado");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregaEstudianteDuplicado\n" + error.getMessage());
        }
    }

    @Test
    void pruebaAgregarEstudianteSinDatos () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.agregar(new EstudianteDTO()),"pruebaAgregarEstudianteSinDatos");
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
    void pruebaAgregarEstudianteApellidoPaternoInvalido () {
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
        assertTrue(resultado,"pruebaAgregarEstudianteApellidoPaternoInvalido");
    }

    @Test
    void pruebaAgregarEstudianteApellidoMaternoInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidos("Lopez");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_AUXILIAR.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaAgregarEstudianteApellidoMaternoInvalido");
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
    void pruebaGetPorIdExitosa () {
        Optional<EstudianteDTO> estudianteDTOOptional = Optional.empty();
        try {
            estudianteDTOOptional = ESTUDIANTE_AUXILIAR.getPorId(estudianteRegistrado1.getIdEstudiante());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa\n" + error.getMessage());
        }
        assertTrue(estudianteDTOOptional.isPresent());
        assertEquals(estudianteRegistrado1, estudianteDTOOptional.get(), "pruebaGetEstudiantePorIDExitosa");
    }

    @Test
    void pruebaGetEstudiantePorIdInexistente () {
        try {
            Optional<EstudianteDTO> obtenido = ESTUDIANTE_AUXILIAR.getPorId(10);
            assertTrue(obtenido.isEmpty(),"pruebaGetEstudiantePorIdInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdInvalido () {
        assertThrows(ErrorDAO.class, ()->ESTUDIANTE_AUXILIAR.getPorId(-1),"pruebaGetEstudiantePorIdInvalido");
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaExitosa () {
        Optional<EstudianteDTO> obtenido = Optional.empty();
        try {
            obtenido = ESTUDIANTE_AUXILIAR.getEstudiantePorIdPersona(estudianteRegistrado1.getIdPersona());
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
            Optional<EstudianteDTO> resultado = ESTUDIANTE_AUXILIAR.getEstudiantePorIdPersona(10);
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorIdPersonaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIdPersonaInexistente\n" + error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorIdPersonaInvalido () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.getEstudiantePorIdPersona(-1),"pruebaGetEstudiantePorIdPersonaInvalido");
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
    void pruebaGetEstudiantePorMatriculaYUniversidadInexistente () {
        try {
            Optional<EstudianteDTO> resultado = ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad("zs22013029",1);
            assertTrue(resultado.isEmpty(),"pruebaGetEstudiantePorMatriculaInexistente");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorMatriculaInexistente\n" +  error.getMessage());
        }
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadIdInvalido () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad(estudianteRegistrado1.getMatricula(),0),"pruebaGetEstudiantePorMatriculaYUniversidadIdInvalido");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadNula () {
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_AUXILIAR.getEstudiantePorMatriculaYUniversidad(null,1), "pruebaGetEstudiantePorMatriculaNula");
    }

    @Test
    void pruebaGetEstudiantePorMatriculaYUniversidadInvalida () {
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


    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadExitosa () {
        List<EstudianteDTO> listaEsperada = new ArrayList<>();
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        listaEsperada.add(estudianteRegistrado1);
        listaEsperada.add(estudianteRegistrado2);

        try {
            listaObtenida = ESTUDIANTE_AUXILIAR.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(1);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadExitosa\n" + error.getMessage());
        }

        assertEquals(listaEsperada.size(),listaObtenida.size(),"pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadExitosa");
        for (EstudianteDTO estudiante : listaEsperada) {
            assertEquals(estudiante,listaObtenida.get(0));
            listaObtenida.remove(0);
        }
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadInexistente () {
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = ESTUDIANTE_AUXILIAR.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(10);
        }
        catch (ErrorDAO error) {
            fail("Fallida: getEstudiantesSinColaboracionActivaOVinculadaPorUniversidadInexistente\n" + error.getMessage());
        }
        assertTrue(listaObtenida.isEmpty(),"getEstudiantesSinColaboracionActivaOVinculadaPorUniversidadInexistente");
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadSinResultados () {
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        try {
            listaObtenida = ESTUDIANTE_AUXILIAR.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(2);
        }
        catch (ErrorDAO error) {
            fail("Fallida: getEstudiantesSinColaboracionActivaOVinculadaPorUniversidadSinResultados\n" + error.getMessage());
        }
        assertTrue(listaObtenida.isEmpty(),"getEstudiantesSinColaboracionActivaOVinculadaPorUniversidadSinResultados");
    }

    @Test
    void pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdInvalido () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_AUXILIAR.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(-1),"pruebaGetEstudiantesSinColaboracionActivaOVinculadaPorUniversidadIdInvalido");
    }
}