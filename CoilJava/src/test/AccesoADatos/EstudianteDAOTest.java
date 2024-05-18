package test.AccesoADatos;

import DAO.EstudianteDAO;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteDAOTest {
    private final EstudianteDAO ESTUDIANTE_DAO = new EstudianteDAO();

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

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidoPaterno("Lopez");
        estudianteDTO.setApellidoMaterno("Lara");
        estudianteDTO.setMatricula("zs22013690");
        estudianteDTO.setIdUniversidad(1);

        int esperado = 2;
        int obtenido = 0;

        try {
            obtenido = ESTUDIANTE_DAO.agregar(estudianteDTO);

        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarEstudianteExitoso");

        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void pruebaAgregarEstudianteVacioFallida () {
        System.out.println("pruebaAgregarEstudianteVacioFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();

        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(estudianteDTO));
    }

    @Test
    void pruebaAgregarEstudianteMatriculaLargaFallida () {
        System.out.println("pruebaAgregarEstudianteMatriculaLargaFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
            estudianteDTO.setMatricula("12345678912345");
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaAgregarEstudianteMatriculaVaciaFallida () {
        System.out.println("pruebaAgregarEstudianteMatriculaVaciaFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
            estudianteDTO.setMatricula(null);
            estudianteDTO.setIdUniversidad(1);
            ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaAgregarEstudianteNombreVacioFallida () {
        System.out.println("pruebaAgregarEstudianteNombreVacioFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre(null);
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("Lara");
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
    void pruebaAgregarEstudianteApellidoPaternoVacioFallida () {
        System.out.println("pruebaAgregarEstudianteNombreVaciaFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno(null);
            estudianteDTO.setApellidoMaterno("Lara");
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
    void pruebaAgregarEstudianteApellidoMaternoVacioFallida () {
        System.out.println("pruebaAgregarEstudianteApellidoMaternoVacioFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno(null);
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
    void pruebaAgregarEstudianteUniversidadVaciaFallida () {
        System.out.println("pruebaAgregarEstudianteUniversidadVaciaFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        boolean resultado = false;
        try {
            estudianteDTO.setNombre("Jose");
            estudianteDTO.setApellidoPaterno("Lopez");
            estudianteDTO.setApellidoMaterno("");
            estudianteDTO.setMatricula("zs22013690");
            ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO errorDAO) {
            resultado = true;
        }
        assertTrue(resultado);
    }

    @Test
    void pruebaAgregarEstudianteUniversidadInexistenteFallida () {
        System.out.println("pruebaAgregarEstudianteApellidoMaternoVacioFallida");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setNombre("Jose");
        estudianteDTO.setApellidoPaterno("Lopez");
        estudianteDTO.setApellidoMaterno("Lara");
        estudianteDTO.setMatricula("zs22013690");
        estudianteDTO.setIdUniversidad(10);

        assertThrows(ErrorDAO.class, () -> ESTUDIANTE_DAO.agregar(estudianteDTO));

    }

    @Test
    void pruebaGetEstudiantePorIDExitosa () {
        System.out.println("pruebaGetEstudiantePorIDExitosa");

        EstudianteDTO estudianteDTOEsperado = new EstudianteDTO();
        estudianteDTOEsperado.setIdPersona(1);
        estudianteDTOEsperado.setIdEstudiante(1);
        estudianteDTOEsperado.setNombre("Jose");
        estudianteDTOEsperado.setApellidoPaterno("Lopez");
        estudianteDTOEsperado.setApellidoMaterno("Perez");
        estudianteDTOEsperado.setMatricula("zs22013690");
        estudianteDTOEsperado.setIdUniversidad(1);

        EstudianteDTO estudianteDTOObtenido = null;

        try {
            Optional<EstudianteDTO> estudianteDTOOptional = ESTUDIANTE_DAO.getPorId(1);
            assertTrue(estudianteDTOOptional.isPresent());
            estudianteDTOObtenido = estudianteDTOOptional.get();
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetEstudiantePorIDExitosa " + error.getMessage());

        }

        assertEquals(estudianteDTOEsperado.getMatricula(), estudianteDTOObtenido.getMatricula());
        assertEquals(estudianteDTOEsperado.getIdPersona(), estudianteDTOObtenido.getIdPersona());

    }

    @Test
    void pruebaGetEstudiantePorUniversidadExitosa () {
        System.out.println("pruebaGetEstudiantePorIDExitosa");

        int tamanoListaEsperado = 1;
        int tamanoListaReal = -1;

        List<EstudianteDTO> listaEstudianteDTOS;

        try {
            listaEstudianteDTOS = ESTUDIANTE_DAO.getEstudiantePorUniversidad(1);
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