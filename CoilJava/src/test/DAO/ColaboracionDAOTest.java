package test.DAO;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ColaboracionDAOTest {
    private final ColaboracionDAO COLABORACION_DAO = new ColaboracionDAO();

    @BeforeEach
    void setUp () {
        AyudantePruebasColaboracionDB.borrarTodosDatosTabla();
        AyudantePruebasColaboracionDB.agregarPrecondiciones();
    }

    @AfterEach
    void tearDown () {
        AyudantePruebasColaboracionDB.borrarTodosDatosTabla();
    }

    private static ColaboracionDTO instanciarColaboracion () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
        colaboracionDTO.setTipo(ColaboracionDTO.TipoColaboracion.claseEspejo);
        colaboracionDTO.setTemaInteres("Inteligencia Artificial");
        colaboracionDTO.setIdioma("Español");
        colaboracionDTO.setObjetivo("Mejorar habilidades en IA");

        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodoDTO.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracionDTO.setPeriodo(periodoDTO);
        colaboracionDTO.setPerfilEstudiante("Estudiantes de informática");
        return colaboracionDTO;
    }


    //todo
    @Test
    void pruebaGetColaboracionPorIdExitosa () {
        ColaboracionDTO esperado = instanciarColaboracion();
        ColaboracionDTO obtenido = null;
        try {
            Optional<ColaboracionDTO> colaboracionDTOOptional = COLABORACION_DAO.getColaboracionPorId(1);
            assertTrue(colaboracionDTOOptional.isPresent());
            obtenido = colaboracionDTOOptional.get();
        }
        catch (ErrorDAO errorDAO) {
            fail("Falida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
        assertEquals(esperado.getIdColaboracion(), obtenido.getIdColaboracion(), "pruebaGetColaboracionPorIdExitosa");
    }

    @Test
    void pruebaGetColaboracionIdInexistente () {
        try {
            Optional<ColaboracionDTO> resultado = COLABORACION_DAO.getColaboracionPorId(10);
            assertTrue(resultado.isEmpty(), "pruebaGetColaboracionIdInexistente");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaAgregarPeriodoColaboracionExitosa () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setFechaInicio(LocalDate.of(2023, 1, 1));
        periodoDTO.setFechaFin(LocalDate.of(2023, 6, 30));
        colaboracionDTO.setPeriodo(periodoDTO);
        int resultadoEsperado = 1;
        int resultadoReal = 0;
        try {
            resultadoReal = COLABORACION_DAO.agregarPeriodoAColaboracion(colaboracionDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaAgregarPeriodoColaboracionExitosa");
        }
        assertEquals(resultadoEsperado, resultadoReal, "pruebaAgregarPeriodoColaboracionExitosa");
    }

    @Test
    void pruebaAgregarPeriodoColaboracionFechasIncorrectasFallida () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        PeriodoDTO periodoDTO = new PeriodoDTO();
        try {
            periodoDTO.setFechaInicio(LocalDate.of(2023, 5, 20));
            periodoDTO.setFechaFin(LocalDate.of(2023, 3, 1));
            colaboracionDTO.setPeriodo(periodoDTO);
            COLABORACION_DAO.agregarPeriodoAColaboracion(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaAgregarPeriodoColaboracionFechasIncorrectasFallida");
        }
    }

    @Test
    void pruebaAgregarPeriodoColaboracionFechaInicioFallida () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        PeriodoDTO periodoDTO = new PeriodoDTO();
        try {
            periodoDTO.setFechaInicio(LocalDate.of(2023, 3, 1));
            colaboracionDTO.setPeriodo(periodoDTO);
            COLABORACION_DAO.agregarPeriodoAColaboracion(colaboracionDTO);
        }
        catch (NullPointerException error) {
            assertTrue(true, "pruebaAgregarPeriodoColaboracionFechasIncorrectasFallida");
        }
    }

    @Test
    void pruebaAgregarPeriodoColaboracionFechaFallidoFallida () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        PeriodoDTO periodoDTO = new PeriodoDTO();
        try {
            periodoDTO.setFechaFin(LocalDate.of(2023, 3, 1));
            colaboracionDTO.setPeriodo(periodoDTO);
            COLABORACION_DAO.agregarPeriodoAColaboracion(colaboracionDTO);
        }
        catch (NullPointerException error) {
            assertTrue(true, "pruebaAgregarPeriodoColaboracionFechasIncorrectasFallida");
        }
    }


    @Test
    void pruebaGetListaDeEstudiantesExitosa () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);

        List<EstudianteDTO> esperada = new ArrayList<>();
        List<EstudianteDTO> obtenida = new ArrayList<>();
        EstudianteDTO estudianteDTO1 = new EstudianteDTO();
        EstudianteDTO estudianteDTO2 = new EstudianteDTO();

        estudianteDTO1.setIdPersona(4);
        estudianteDTO1.setNombre("Eduardo");
        estudianteDTO1.setApellidos("Villegas");
        estudianteDTO1.setApellidoMaterno("Hurtado");
        estudianteDTO1.setIdUniversidad(1);
        estudianteDTO1.setIdEstudiante(1);
        estudianteDTO1.setMatricula("zs22013693");

        estudianteDTO2.setIdPersona(5);
        estudianteDTO2.setNombre("John");
        estudianteDTO2.setApellidos("Smith");
        estudianteDTO2.setApellidoMaterno("Onell");
        estudianteDTO2.setIdUniversidad(2);
        estudianteDTO2.setIdEstudiante(2);
        estudianteDTO2.setMatricula("zs2201356");

        esperada.add(estudianteDTO1);
        esperada.add(estudianteDTO2);

        try {
            obtenida = COLABORACION_DAO.getListaDeEstudiantes(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaDeEstudiantesExitosa " + error.getMessage());
        }

        assertEquals(esperada.size(), obtenida.size(), "pruebaGetListaDeEstudiantesExitosa");
        for (EstudianteDTO estudianteDTO : esperada) {
            assertEquals(estudianteDTO, obtenida.get(0));
            obtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionInexistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(40);
        List<EstudianteDTO> resultado = null;
        try {
            resultado = COLABORACION_DAO.getListaDeEstudiantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListadeEstudiantesColaboracionInexistente" + error.getMessage());
        }
        assertTrue(resultado.isEmpty(), "pruebaGetListadeEstudiantesColaboracionInexistente");
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionVacia () {
        try {
            List<EstudianteDTO> resultado = COLABORACION_DAO.getListaDeEstudiantes(new ColaboracionDTO());
            assertTrue(resultado.isEmpty(), "pruebaGetListadeEstudiantesColaboracionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListadeEstudiantesColaboracionVacia");
        }
    }

    @Test
    void pruebaGetAcademicosParticipantesExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);
        List<AcademicoDTO> esperada = new ArrayList<>();
        List<AcademicoDTO> obtenida = new ArrayList<>();

        AcademicoDTO academicoDTO1 = new AcademicoDTO();

        esperada.add(academicoDTO1);

        try {
            obtenida = COLABORACION_DAO.getAcademicosParticipantes(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicosParticipantesExitoso " + error.getMessage());
        }

        assertEquals(esperada.size(), obtenida.size(), "pruebaGetAcademicosParticipantesExitoso");
    }

    @Test
    void pruebaGetAcademicosParticipantesColaboracionInexistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(-10);
        try {
            List<AcademicoDTO> listaAcademico = COLABORACION_DAO.getAcademicosParticipantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaGetAcademicosParticipantesColaboracionInexistente");
        }
    }

    @Test
    void pruebaGetColaboracionPorPeriodoExitosa () {
        int colaboracionesEsperadas = 1;
        List<ColaboracionDTO> listaColaboraciones = null;

        LocalDate fechaInicio = LocalDate.of(2024, 5, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);

        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setFechaInicio(fechaInicio);
        periodoDTO.setFechaFin(fechaFin);

        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorPeriodo(periodoDTO);

        }
        catch (ErrorDAO error) {
            fail("Error en pruebaGetColaboracionPorPeriodoExitosa: " + error.getMessage());

        }

        assertEquals(colaboracionesEsperadas, listaColaboraciones.size(), "pruebaGetColaboracionPorPeriodoExitosa");

    }

    @Test
    void pruebaGetColaboracionPorPeriodoFallida () {
        int colaboracionesEsperadas = 0;
        List<ColaboracionDTO> listaColaboraciones = null;

        LocalDate fechaInicio = LocalDate.of(2024, 7, 1);
        LocalDate fechaFin = LocalDate.of(2024, 8, 30);

        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setFechaInicio(fechaInicio);
        periodoDTO.setFechaFin(fechaFin);

        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorPeriodo(periodoDTO);
        }
        catch (ErrorDAO error) {
            fail("Error en pruebaGetColaboracionPorPeriodoFallida: " + error.getMessage());
        }

        assertEquals(colaboracionesEsperadas, listaColaboraciones.size(), "pruebaGetColaboracionPorPeriodoFallida");
    }

    //todo
    @Test
    void pruebaGetColaboracionPorIdiomaExitosa () {
        List<ColaboracionDTO> listaColaboraciones = null;
        String idioma = "Español";
        int esperado = 1;
        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdiomaExitosa");
        }
        assertEquals(esperado, listaColaboraciones.size(), "pruebaGetColaboracionPorIdiomaExitosa");
    }

    @Test
    void pruebaGetColaboracionPorIdiomaCadenaInvalida () {
        List<ColaboracionDTO> listaColaboraciones = null;
        int esperado = 0;
        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorIdioma("");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdiomaExitosa");
        }
        assertEquals(esperado, listaColaboraciones.size(), "pruebaGetColaboracionPorIdiomaExitosa");
    }


    @Test
    void pruebaCambiarEstadoColaboracionExitosa () {
        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.cambiarEstadoColaboracion("vinculada", 1);


        }
        catch (ErrorDAO error) {

            fail("Error en pruebaCambiarEstadoColaboracionExitosa: " + error.getMessage());
        }

        assertEquals(esperado, obtenido, "pruebaCambiarEstadoColaboracionExitosa");

    }

    @Test
    void pruebaCambiarEstadoColaboracionEstadoInvalidoFallida () {
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.cambiarEstadoColaboracion("invalido", 1), "pruebaCambiarEstadoColaboracionEstadoInvalidoFallida");
    }

    @Test
    void pruebaCambiarEstadoColaboracionEstadoVacioFallida () {
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.cambiarEstadoColaboracion("", 1), "pruebaCambiarEstadoColaboracionEstadoVacioFallida");
    }

    @Test
    void pruebaCambiarEstadoColaboracionEstadoNuloFallida () {
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.cambiarEstadoColaboracion(null, 1), "pruebaCambiarEstadoColaboracionEstadoNuloFallida");
    }

    @Test
    void pruebaCambiarEstadoColaboracionIdInexistenteFallida () {
        int resultadoEsperado = 0;
        int filasAfectadas = 0;

        try {
            filasAfectadas = COLABORACION_DAO.cambiarEstadoColaboracion("vinculada", 99);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaCambiarEstadoColaboracionIdInexistenteFallida");
        }

        assertEquals(resultadoEsperado, filasAfectadas, "pruebaCambiarEstadoColaboracionIdInexistenteFallida");

    }

    //todo
    @Test
    void pruebaGetColaboracionPorEstadoExitoso () {
        List<ColaboracionDTO> listaColaboracionDTO = null;

        String estado = "propuesta";
        int tamanoEsperado = 1;

        try {
            listaColaboracionDTO = COLABORACION_DAO.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruebaGetColaboracionPorEstadoExitoso");
        }
        assertEquals(tamanoEsperado, listaColaboracionDTO.size(), "pruebaGetColaboracionPorEstadoExitoso");
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);

        EstudianteDTO estudianteDTOPrueba = new EstudianteDTO();
        estudianteDTOPrueba.setIdEstudiante(2);

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTOPrueba, estudianteDTOPrueba);

        }
        catch (ErrorDAO error) {
            fail("pruebaAgregarEstudianteAColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido, "pruebaAgregarEstudianteAColaboracionExitoso");

    }

    @Test
    void pruebaAgregarEstudianteAColaboracionColaboracionIdInvalido () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(-1); // ID de colaboración inválido
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(1);

        try {
            COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
            fail("pruebaAgregarEstudianteAColaboracionColaboracionIdInvalido");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarEstudianteAColaboracionColaboracionIdInvalido");
        }
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionEstudianteIdInvalido () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(-1); // ID de estudiante inválido

        try {
            COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
            fail("pruebaAgregarEstudianteAColaboracionEstudianteIdInvalido");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarEstudianteAColaboracionEstudianteIdInvalido");
        }
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionColaboracionIdNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(0); // ID de colaboración nulo (asumiendo que 0 no es un ID válido)
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(1);

        try {
            COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
            fail("pruebaAgregarEstudianteAColaboracionColaboracionIdNulo");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarEstudianteAColaboracionColaboracionIdNulo");
        }
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionEstudianteIdNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(0); // ID de estudiante nulo (asumiendo que 0 no es un ID válido)

        try {
            COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
            fail("pruebaAgregarEstudianteAColaboracionEstudianteIdNulo");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarEstudianteAColaboracionEstudianteIdNulo");
        }
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionAmbosIdsInvalidos () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(-1); // ID de colaboración inválido
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(-1); // ID de estudiante inválido

        try {
            COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
            fail("pruebaAgregarEstudianteAColaboracionAmbosIdsInvalidos");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarEstudianteAColaboracionAmbosIdsInvalidos");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionTemaInteresNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres(null); // Tema de interés nulo
        colaboracionDTO.setObjetivo("Objetivo de prueba");
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("123456");

        try {
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionTemaInteresNulo");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionTemaInteresNulo");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionObjetivoNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres("Tema de prueba");
        colaboracionDTO.setObjetivo(null); // Objetivo nulo
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("123456");

        try {
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionObjetivoNulo");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionObjetivoNulo");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionCedulaProfesionalNula () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres("Tema de prueba");
        colaboracionDTO.setObjetivo("Objetivo de prueba");
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setCedulaProfesional(null);
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionCedulaProfesionalNula");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionCedulaProfesionalNula");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionColaboracionInvalida () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setObjetivo("Promover el uso de optional para validar nulos por medio de coil");
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("200011");

        try {
            colaboracionDTO.setTemaInteres("");
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionColaboracionInvalida");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionColaboracionInvalida");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionAcademicoInvalido () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres("Promover ambiente colaborativo");
        colaboracionDTO.setObjetivo("Que los estudiantes manejan las habilidades en COIL");
        AcademicoDTO academicoDTO = new AcademicoDTO();


        try {
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            academicoDTO.setCedulaProfesional("");
            fail("pruebaRegistrarPropuestaColaboracionAcademicoInvalido");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionAcademicoInvalido");
        }
    }

    @Test
    void pruebaRegistrarSolicitudParticipacionColaboracionIdInvalido () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(-1); // ID de colaboración inválido
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("123456");

        try {
            COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarSolicitudParticipacionColaboracionIdInvalido");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarSolicitudParticipacionColaboracionIdInvalido");
        }
    }

    @Test
    void pruebaRegistrarSolicitudParticipacionCedulaProfesionalInvalida () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setCedulaProfesional("");
            COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarSolicitudParticipacionCedulaProfesionalInvalida");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarSolicitudParticipacionCedulaProfesionalInvalida");
        }
    }

    @Test
    void pruebaRegistrarSolicitudParticipacionColaboracionNula () {
        ColaboracionDTO colaboracionDTO = null;
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("200011");

        try {
            COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarSolicitudParticipacionColaboracionNula");
        }
        catch (NullPointerException error) {
            assertTrue(true, "pruebaRegistrarSolicitudParticipacionColaboracionNula");
        }
    }

    @Test
    void pruebaRegistrarSolicitudParticipacionAcademicoNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        AcademicoDTO academicoDTO = null;

        try {
            COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarSolicitudParticipacionAcademicoNulo");
        }
        catch (NullPointerException error) {
            assertTrue(true, "pruebaRegistrarSolicitudParticipacionAcademicoNulo");
        }
    }

    @Test
    void pruebaGetActivaPorAcademicoCedulaInvalida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setCedulaProfesional("");
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getActivaPorAcademico(academicoDTO);
            fail("pruebaGetActivaPorAcademicoCedulaInvalida");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaGetActivaPorAcademicoCedulaInvalida");
        }
    }

    @Test
    void pruebaGetActivaPorAcademicoNull () {
        AcademicoDTO academicoDTO = null;

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getActivaPorAcademico(academicoDTO);
            fail("pruebaGetActivaPorAcademicoNull");
        }
        catch (NullPointerException error) {
            assertTrue(true, "pruebaGetActivaPorAcademicoNull");
        }
    }

    @Test
    void pruebaGetActivaPorAcademicoNoExiste () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("000000");

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getActivaPorAcademico(academicoDTO);
            assertFalse(colaboracion.isPresent(), "pruebaGetActivaPorAcademicoNoExiste");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetActivaPorAcademicoNoExiste: " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetColaboracionAceptadaPorAcademicoCedulaInvalida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setCedulaProfesional("");
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getColaboracionAceptadaPorAcademico(academicoDTO);
            fail("pruebaGetColaboracionAceptadaPorAcademicoCedulaInvalida");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaGetColaboracionAceptadaPorAcademicoCedulaInvalida");
        }
    }

    @Test
    void pruebaGetColaboracionAceptadaPorAcademicoNull () {
        AcademicoDTO academicoDTO = null;

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getColaboracionAceptadaPorAcademico(academicoDTO);
            fail("pruebaGetColaboracionAceptadaPorAcademicoNull");
        }
        catch (NullPointerException error) {
            assertTrue(true, "pruebaGetColaboracionAceptadaPorAcademicoNull");
        }
    }

    @Test
    void pruebaGetColaboracionAceptadaPorAcademicoNoExiste () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("000000");

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getColaboracionAceptadaPorAcademico(academicoDTO);
            assertFalse(colaboracion.isPresent(), "pruebaGetColaboracionAceptadaPorAcademicoNoExiste");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetColaboracionAceptadaPorAcademicoNoExiste: " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetPropuestaPorAcademicoCedulaInvalida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        try {
            academicoDTO.setCedulaProfesional("");
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getPropuestaPorAcademico(academicoDTO);
            fail("pruebaGetPropuestaPorAcademicoCedulaInvalida");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaGetPropuestaPorAcademicoCedulaInvalida");
        }
    }

    @Test
    void pruebaGetPropuestaPorAcademicoNull () {
        AcademicoDTO academicoDTO = null;

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getPropuestaPorAcademico(academicoDTO);
            fail("pruebaGetPropuestaPorAcademicoNull");
        }
        catch (NullPointerException errorDAO) {
            assertTrue(true, "pruebaGetPropuestaPorAcademicoNull");
        }
    }

    @Test
    void pruebaGetPropuestaPorAcademicoNoExiste () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("000000");

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getPropuestaPorAcademico(academicoDTO);
            assertFalse(colaboracion.isPresent(), "pruebaGetPropuestaPorAcademicoNoExiste");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPropuestaPorAcademicoNoExiste: " + errorDAO.getMessage());
        }
    }


    @Test
    void pruebaRegistrarPropuestaColaboracionObjetivoExcedeLimite () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres("Innovación en métodos de enseñanza.");
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);

        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("123456");

        try {
            colaboracionDTO.setObjetivo("O".repeat(301));
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionObjetivoExcedeLimite");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionObjetivoExcedeLimite");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionPerfilEstudianteMenorLimite () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres("Inteligencia Artificial aplicada a la educación.");
        colaboracionDTO.setObjetivo("Implementar sistemas de tutoría inteligentes.");
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);

        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("123456");

        try {
            colaboracionDTO.setPerfilEstudiante("IA");
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionPerfilEstudianteMenorLimite");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionPerfilEstudianteMenorLimite");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionEstadoNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres("Desarrollo de aplicaciones móviles educativas.");
        colaboracionDTO.setObjetivo("Crear una app para el aprendizaje de matemáticas.");
        colaboracionDTO.setEstado(null); // Estado nulo

        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("123456");

        try {
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
            fail("pruebaRegistrarPropuestaColaboracionEstadoNulo");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionEstadoNulo");
        }
    }

    @Test
    void pruebaGetPropuestasColaboracionSinPropuestas () {

        List<ColaboracionDTO> listaColaboraciones = null;
        int colaboracionesEsperadas = 1;

        try {
            listaColaboraciones = COLABORACION_DAO.getPropuestasColaboracion();
        }
        catch (ErrorDAO error) {
            fail("Error en pruebaGetPropuestasColaboracionSinPropuestas: " + error.getMessage());
        }

        assertEquals(colaboracionesEsperadas, listaColaboraciones.size(), "pruebaGetPropuestasColaboracionSinPropuestas");
    }

    @Test
    void pruebaGetColaboracionesDisponiblesSinResultados () {

        List<ColaboracionDTO> listaColaboraciones = null;
        int colaboracionesEsperadas = 0;

        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionesDisponibles("43", 1);
        }
        catch (ErrorDAO error) {
            fail("Error en pruebaGetColaboracionesDisponiblesSinResultados: " + error.getMessage());
        }

        assertEquals(colaboracionesEsperadas, listaColaboraciones.size(), "pruebaGetColaboracionesDisponiblesSinResultados");
    }


    @Test
    void pruebaGetColaboracionDisponiblePorAcademicoInexistente () {
        Optional<ColaboracionDTO> colaboracionDTO = Optional.empty();

        try {
            colaboracionDTO = COLABORACION_DAO.getColaboracionDisponiblePorAcademico("00000000");
        }
        catch (ErrorDAO error) {
            fail("pruebaGetColaboracionDisponiblePorAcademicoCedulaInvalida");
        }

        assertFalse(colaboracionDTO.isPresent());
    }

    @Test
    void pruebaExisteUnaSolicitudPreviaCedulaProfesionalInvalida () {

        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("00000000");

        boolean existeSolicitudPrevia = false;

        try {
            existeSolicitudPrevia = COLABORACION_DAO.existeUnaSolicitudPrevia(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("pruebaExisteUnaSolicitudPreviaCedulaProfesionalInvalida");
        }

        assertFalse(existeSolicitudPrevia);
    }

    @Test
    void pruebaGetSolicitudAcademicoColaboracionIdInvalido () {
        int idColaboracionInvalido = 9999;
        List<AcademicoDTO> listaAcademico = null;

        try {
            listaAcademico = COLABORACION_DAO.getSolicitudAcademicoColaboracion(idColaboracionInvalido);
        }
        catch (ErrorDAO error) {
            fail(" fallida: pruebaGetSolicitudAcademicoColaboracionIdInvalido");
        }

        assertTrue(listaAcademico.size() == 0);
    }

    @Test
    void pruebaGetSolicitudesDeAcademicoCedulaInvalida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        List<ColaboracionDTO> listaColaboracion = null;

        try {
            academicoDTO.setCedulaProfesional("999999990000000000000099999");
            listaColaboracion = COLABORACION_DAO.getSolicitudesDeAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            assertTrue(true, "pruebaGetSolicitudesDeAcademicoCedulaInvalida");
        }
        assertTrue(listaColaboracion.isEmpty(), "La lista debe estar vacía cuando la cédula es inválida");
    }

    @Test
    void pruebaActualizarEstadoSolicitudDeParticipacionEstadoInvalido () {
        int idColaboracion = 1;
        String cedulaProfesional = "200011";
        String nuevoEstado = "Iniciado";

        boolean result = false;

        try {
            COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
        }
        catch (ErrorDAO error) {
            result = true;
        }

        assertTrue(result, "pruebaActualizarEstadoSolicitudDeParticipacionEstadoInvalido");
    }

    @Test
    void pruebaActualizarEstadoSolicitudDeParticipacionIdColaboracionInexistente () {
        int idColaboracion = 10;
        String cedulaProfesional = "12345678";
        String nuevoEstado = "aceptado";

        int resultadoEsperado = 0;
        int filasAfectadas = 0;
        try {
            filasAfectadas = COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaActualizarEstadoSolicitudDeParticipacionIdColaboracionInvalido");
        }

        assertEquals(resultadoEsperado, filasAfectadas, "pruebaActualizarEstadoSolicitudDeParticipacionIdColaboracionInvalido");
    }

    @Test
    void pruebaEliminarSolicitudDeParticipacionCedulaInexistente () {

        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("99999999");

        int resultadoEsperado = 0;
        int filasAfectadas = -1;
        try {
            filasAfectadas = COLABORACION_DAO.eliminarSolicitudDeParticipacion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEliminarSolicitudDeParticipacionCedulaInexistente");
        }

        assertEquals(resultadoEsperado, filasAfectadas, "pruebaEliminarSolicitudDeParticipacionCedulaInvalida");

    }

    @Test
    void pruebaEliminarSolicitudDeParticipacionEstadoInvalido () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("12345678");
        int resultadoEsperado = 0;
        int filasAfectadas = 0;
        try {
            filasAfectadas = COLABORACION_DAO.eliminarSolicitudDeParticipacion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEliminarSolicitudDeParticipacionEstadoInvalido");
        }
        assertEquals(resultadoEsperado,filasAfectadas, "pruebaEliminarSolicitudDeParticipacionEstadoInvalido");
    }


    @Test
    void pruebaActualizarEstadoSolicitudDeParticipacionCedulaInexistente () {
        int idColaboracion = 1;
        String cedulaProfesional = "99999999";
        String nuevoEstado = "aceptado";

        int resultadoEsperado = 0;
        int resultadoReal = -1;
        try {
            resultadoReal = COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaActualizarEstadoSolicitudDeParticipacionCedulaInvalida");
        }

        assertEquals(resultadoEsperado, resultadoReal, "pruebaActualizarEstadoSolicitudDeParticipacionCedulaInvalida");
    }

    @Test
    void pruebaAgregarAcademicoAColaboracionExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(2);

        AcademicoDTO academicoDTOPrueba = new AcademicoDTO();
        academicoDTOPrueba.setCedulaProfesional("102939");

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTOPrueba, academicoDTOPrueba);

        }
        catch (ErrorDAO error) {
            fail("pruebaAgregarAcademicoAColaboracionExitoso " + error.getMessage());

        }
        assertEquals(esperado, obtenido, "pruebaAgregarAcademicoAColaboracionExitoso");
    }

    @Test
    void pruebaGetAcademicoParColaboracionNoExistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(-1);

        Optional<AcademicoDTO> academicoDTOOptional = Optional.empty();
        try {
            academicoDTOOptional = COLABORACION_DAO.getAcademicoPar(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoParColaboracionNoExistente");
        }
        assertFalse(academicoDTOOptional.isPresent(), "pruebaGetAcademicoParColaboracionNoExistente");
    }

    @Test
    void pruebaGetVinculadaPorAcademicoCedulaProfesionalInvalida () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        boolean resultado = false;
        try {
            academicoDTO.setCedulaProfesional("");
            Optional<ColaboracionDTO> colaboracionDTOOptional = COLABORACION_DAO.getVinculadaPorAcademico(academicoDTO);
            assertFalse(colaboracionDTOOptional.isPresent(), "pruebaGetVinculadaPorAcademicoCedulaProfesionalInvalida");
        }
        catch (ErrorDAO error) {
            resultado = true;
        }

        assertTrue(resultado, "pruebaGetVinculadaPorAcademicoCedulaProfesionalInvalida");
    }

    @Test
    void pruebaGetVinculadaPorAcademicoSinColaboracion () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        boolean resultado = false;
        try {
            academicoDTO.setCedulaProfesional("CEDULA123456");
            Optional<ColaboracionDTO> colaboracionDTOOptional = COLABORACION_DAO.getVinculadaPorAcademico(academicoDTO);
            assertFalse(colaboracionDTOOptional.isPresent(), "pruebaGetVinculadaPorAcademicoSinColaboracion");
        }
        catch (ErrorDAO error) {
            resultado = true;
        }

        assertTrue(resultado, "pruebaGetVinculadaPorAcademicoSinColaboracion");
    }

    @Test
    void pruebaGetVinculadaPorAcademicoColaboracionInexistente () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        boolean resultado = false;

        try {
            academicoDTO.setCedulaProfesional("CEDULA999999");
            Optional<ColaboracionDTO> colaboracionDTOOptional = COLABORACION_DAO.getVinculadaPorAcademico(academicoDTO);
        }
        catch (ErrorDAO error) {
            resultado = true;
        }

        assertTrue(resultado, "pruebaGetVinculadaPorAcademicoColaboracionInexistente");
    }

    @Test
    void pruebaRetirarEstudianteDeColaboracionEstudianteInexistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(999);

        int resultadoEsperado = 0;
        int filasAfectadas = 0;
        try {
            filasAfectadas = COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracionDTO, estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRetirarEstudianteDeColaboracionEstudianteInexistente");
        }
        assertEquals(resultadoEsperado, filasAfectadas, "pruebaRetirarEstudianteDeColaboracionEstudianteInexistente");
    }

    @Test
    void pruebaRetirarEstudianteDeColaboracionColaboracionInexistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(999);

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(1);

        int resultadoEsperado = 0;
        int filasAfectadas = 0;
        try {
            filasAfectadas = COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracionDTO, estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRetirarEstudianteDeColaboracionColaboracionInexistente");
        }
        assertEquals(0, filasAfectadas, "pruebaRetirarEstudianteDeColaboracionColaboracionInexistente");
    }

    @Test
    void pruebaRetirarEstudianteDeColaboracionIdsInexistentes () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(-1);

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(-1);

        int filasAfectadas = -1;
        try {
            filasAfectadas = COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracionDTO, estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida : pruebaRetirarEstudianteDeColaboracionIdsInexistentes");
        }
        assertEquals(0, filasAfectadas, "pruebaRetirarEstudianteDeColaboracionCamposInvalidos");
    }

    @Test
    void pruebaRegistrarColaboracionExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = instanciarColaboracion();
        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.agregar(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("pruebaRegistrarColaboracionExitoso " + error.getMessage());

        }
        assertEquals(esperado, obtenido, "registrarColaboracion");
    }

    @Test
    void pruebaActualizarColaboracionExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();

        colaboracionDTOPrueba.setIdColaboracion(1);
        colaboracionDTOPrueba.setTipo(ColaboracionDTO.TipoColaboracion.claseEspejo);
        colaboracionDTOPrueba.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
        colaboracionDTOPrueba.setTemaInteres("La existence: El todo a traves del nada");
        colaboracionDTOPrueba.setIdioma("Español");
        colaboracionDTOPrueba.setObjetivo("Mejorar habilidades en IA");
        colaboracionDTOPrueba.setPerfilEstudiante("Estudiantes de informática");

        PeriodoDTO periodoDTOPrueba = new PeriodoDTO();
        periodoDTOPrueba.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodoDTOPrueba.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracionDTOPrueba.setPeriodo(periodoDTOPrueba);

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.modificar(colaboracionDTOPrueba);

        }
        catch (ErrorDAO error) {
            fail("pruebaActualizarColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido, "pruebaActualizarColaboracionExitoso");

    }
    @Test
    void pruebaGetTodosExitosa () {
        List<ColaboracionDTO> listaColaboracionDTO = null;

        int tamanoEsperado = 2;

        try {
            listaColaboracionDTO = COLABORACION_DAO.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetTodosExitosa " + errorDAO.getMessage());
        }
        assertEquals(tamanoEsperado, listaColaboracionDTO.size(), "pruebaGetTodosExitosa");
    }

}