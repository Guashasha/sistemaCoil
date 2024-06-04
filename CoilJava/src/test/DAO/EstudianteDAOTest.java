package test.DAO;

import DAO.EstudianteDAO;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteDAOTest {
    private final EstudianteDAO ESTUDIANTE_DAO = new EstudianteDAO();
    private static EstudianteDTO estudianteRegistrado1;

    @BeforeAll
    static void beforeAll () {
        estudianteRegistrado1 = new EstudianteDTO();
        estudianteRegistrado1.setIdPersona(1);
        estudianteRegistrado1.setIdEstudiante(1);
        estudianteRegistrado1.setNombre("Jose");
        estudianteRegistrado1.setApellidoPaterno("Lopez");
        estudianteRegistrado1.setApellidoMaterno("Perez");
        estudianteRegistrado1.setMatricula("zs22013690");
        estudianteRegistrado1.setIdUniversidad(1);
    }

    @BeforeEach
    void setUp () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (1, 'Jose', 'Lopez', 'Perez', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO persona (idPersona, nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (2, 'Juan', 'Negrete', 'Incumplido', 1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (1, 1, 'zs22013690')");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO estudiante (idEstudiante, idPersona, matricula) VALUES (2, 2, 'zs22013688')");
    }

    @AfterAll
    static void tearDown () {
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    void pruebaAgregarEstudianteExitoso () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidoPaterno("Lopez");
        estudianteDTO.setApellidoMaterno("Lara");
        estudianteDTO.setMatricula("zs22013600");
        estudianteDTO.setIdUniversidad(1);

        int esperado = 2;
        int obtenido = 0;

        try {
            obtenido = ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteExitoso");
        }
        assertEquals(esperado, obtenido, "pruebaAgregarEstudianteExitoso");
    }

    @Test
    void pruebaAgregaEstudianteDuplicado () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre(estudianteRegistrado1.getNombre());
        estudianteDTO.setApellidoPaterno(estudianteRegistrado1.getApellidoPaterno());
        estudianteDTO.setApellidoMaterno(estudianteRegistrado1.getApellidoMaterno());
        estudianteDTO.setMatricula(estudianteRegistrado1.getMatricula());
        estudianteDTO.setIdUniversidad(1);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.agregar(estudianteDTO),"pruebaAgregaEstudianteDuplicado");
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
            estudianteDTO.setNombre(null);
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
            estudianteDTO.setNombre(null);
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
            estudianteDTO.setNombre("Emmanuel @123");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
    void pruebaAgregarEstudianteApellidoPaternoInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopz@_123");
            estudianteDTO.setApellidoMaterno("Lara");
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
    void pruebaAgregarEstudianteApellidoMaternoInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("12_@Herrera;");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Herrera");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(100);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.agregar(estudiante),"pruebaAgregarEstudianteUniversidadInexistente");
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInvalida () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidoPaterno("Lopez");
        estudianteDTO.setApellidoMaterno("Lara");
        estudianteDTO.setMatricula("zs22013690");
        estudianteDTO.setIdUniversidad(0);
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(estudianteDTO),"pruebaAgregarEstudianteUniversidadInvalida");
    }

    @Test
    void pruebaModificarExitosa () {
        EstudianteDTO estudiante = new EstudianteDTO();
        int filasAfectadas = 0;
        try {
            estudiante.setNombre("Jose");
            estudiante.setApellidoPaterno("Lópezz");
            estudiante.setApellidoMaterno("Lara");
            estudiante.setMatricula(estudianteRegistrado1.getMatricula());
            estudiante.setIdUniversidad(1);
            filasAfectadas = ESTUDIANTE_DAO.modificar(estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaModificarExitosa\n" + error.getMessage());
        }
        assertEquals(1,filasAfectadas);
    }

    @Test
    void pruebaModificarEstudianteVacio () {
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.modificar(new EstudianteDTO()),"pruebaModificarEstuianteVacio");
    }

    @Test
    void prubaModificarMatriculaInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Perez");
        estudiante.setMatricula("zs22013601");
        estudiante.setIdUniversidad(1);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.modificar(estudiante),"prubaModificarMatriculaInexistente");
    }

    @Test
    void pruebaModificarMatriculaInvalida () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
            estudianteDTO.setNombre("Emmanuel@123");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
    void pruebaModificarApellidoPaternoInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopz@_123");
            estudianteDTO.setApellidoMaterno("Lara");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaModificarApellidoPaternoInvalido");
    }

    @Test
    void pruebaModificarApellidoMaternoInvalido () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("12_@Herrera;");
            estudianteDTO.setMatricula("zs22013690");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado,"pruebaModificarApellidoMaternoInvalido");
    }

    @Test
    void pruebaModificarEstudianteUniversidadInexistente () {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Lopez");
        estudiante.setApellidoMaterno("Herrera");
        estudiante.setMatricula("zs22013690");
        estudiante.setIdUniversidad(10);
        assertThrows(ErrorDAO.class,()->ESTUDIANTE_DAO.modificar(estudiante),"pruebaModificarEstudianteUniversidadInexistente");
    }

    @Test
    void pruebaModificarEstudianteUniversidadInvalida () {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidoPaterno("Lopez");
        estudianteDTO.setApellidoMaterno("Lara");
        estudianteDTO.setMatricula("zs22013690");
        estudianteDTO.setIdUniversidad(0);
        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.modificar(estudianteDTO),"pruebaModificarEstudianteUniversidadInvalida");
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

    }

    @Test
    void pruebaGetEstudiantePorIdInvalido () {

    }

    @Test
    void pruebaGetEstudiantePorUniversidadExitosa () {
        System.out.println("pruebaGetEstudiantePorIDExitosa");

        int tamanoListaEsperado = 1;
        int tamanoListaReal = -1;

        List<EstudianteDTO> listaEstudianteDTOS;

        try {
            listaEstudianteDTOS = ESTUDIANTE_DAO.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(1);
            tamanoListaReal = listaEstudianteDTOS.size();

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa " + error.getMessage());

        }

        assertEquals(tamanoListaEsperado, tamanoListaReal);

    }

    @Test
    void pruebaEditarEstudianteExitoso () {
        System.out.println("pruebaEditarEstudianteExitoso");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdPersona(1);
        estudianteDTO.setIdEstudiante(1);
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidoPaterno("Lopez");
        estudianteDTO.setApellidoMaterno("Perez");
        estudianteDTO.setMatricula("zs22013690");
        estudianteDTO.setIdUniversidad(1);

        int resultadoEsperado = 2;
        int resultadoReal = -1;
        try {
            resultadoReal = ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEditarEstudianteExitoso " + error.getMessage());

        }
        assertEquals(resultadoEsperado, resultadoReal);
    }




}