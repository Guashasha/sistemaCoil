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

    @Test
    void pruebaGetColaboracionPorIdExitosa () {
        try {
            Optional<ColaboracionDTO> colaboracionObtenido = COLABORACION_DAO.getColaboracionPorId(1);
            assertTrue(colaboracionObtenido.isPresent());
        }
        catch (ErrorDAO errorDAO) {
            fail("Falida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetColaboracionIdInexistente () {
        try {
            Optional<ColaboracionDTO> colaboracionObtenido = COLABORACION_DAO.getColaboracionPorId(10);
            assertTrue(colaboracionObtenido.isEmpty(), "pruebaGetColaboracionIdInexistente");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaAgregarPeriodoColaboracionExitosa () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(1);
        PeriodoDTO periodo = new PeriodoDTO();
        periodo.setFechaInicio(LocalDate.of(2023, 1, 1));
        periodo.setFechaFin(LocalDate.of(2023, 6, 30));
        colaboracion.setPeriodo(periodo);
        int filasAfectadasObtenido = 1;
        int filasAfectadasObtenidos = 0;
        try {
            filasAfectadasObtenidos = COLABORACION_DAO.agregarPeriodoAColaboracion(colaboracion);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaAgregarPeriodoColaboracionExitosa");
        }
        assertEquals(filasAfectadasObtenido, filasAfectadasObtenidos, "pruebaAgregarPeriodoColaboracionExitosa");
    }

    @Test
    void pruebaGetListaDeEstudiantesExitosa () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);

        List<EstudianteDTO> listaEsperada = new ArrayList<>();
        List<EstudianteDTO> listaObtenida = new ArrayList<>();
        EstudianteDTO estudiante1 = new EstudianteDTO();
        EstudianteDTO estudiante2 = new EstudianteDTO();

        estudiante1.setIdPersona(4);
        estudiante1.setNombre("Eduardo");
        estudiante1.setApellidos("Villegas Hurtado");
        estudiante1.setIdUniversidad(1);
        estudiante1.setIdEstudiante(1);
        estudiante1.setMatricula("zs22013693");

        estudiante2.setIdPersona(5);
        estudiante2.setNombre("John");
        estudiante2.setApellidos("Smith Onell");
        estudiante2.setIdUniversidad(2);
        estudiante2.setIdEstudiante(2);
        estudiante2.setMatricula("zs2201356");

        listaEsperada.add(estudiante1);
        listaEsperada.add(estudiante2);

        try {
            listaObtenida = COLABORACION_DAO.getListaDeEstudiantes(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaDeEstudiantesExitosa " + error.getMessage());
        }
        assertEquals(listaEsperada.size(), listaObtenida.size(), "pruebaGetListaDeEstudiantesExitosa");
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionInexistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(40);
        List<EstudianteDTO> listaEstudiantesObtenido = null;
        try {
            listaEstudiantesObtenido = COLABORACION_DAO.getListaDeEstudiantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListadeEstudiantesColaboracionInexistente" + error.getMessage());
        }
        assertTrue(listaEstudiantesObtenido.isEmpty(), "pruebaGetListadeEstudiantesColaboracionInexistente");
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionVacia () {
        try {
            List<EstudianteDTO> listaEstudiantesObtenidos = COLABORACION_DAO.getListaDeEstudiantes(new ColaboracionDTO());
            assertTrue(listaEstudiantesObtenidos.isEmpty(), "pruebaGetListadeEstudiantesColaboracionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListadeEstudiantesColaboracionVacia");
        }
    }

    @Test
    void pruebaGetAcademicosParticipantesExitoso () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(1);
        List<AcademicoDTO> listaEsperada = new ArrayList<>();
        List<AcademicoDTO> listaObtenida = new ArrayList<>();

        AcademicoDTO academicoDTO1 = new AcademicoDTO();

        listaEsperada.add(academicoDTO1);

        try {
            listaObtenida = COLABORACION_DAO.getAcademicosParticipantes(colaboracion);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicosParticipantesExitoso " + error.getMessage());
        }

        assertEquals(listaEsperada.size(), listaObtenida.size(), "pruebaGetAcademicosParticipantesExitoso");
    }

    @Test
    void pruebaGetColaboracionPorPeriodoExitosa () {
        int colaboracionesEsperadas = 2;
        List<ColaboracionDTO> listaColaboraciones = null;

        LocalDate fechaInicio = LocalDate.of(2024, 5, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);

        PeriodoDTO periodo = new PeriodoDTO();
        periodo.setFechaInicio(fechaInicio);
        periodo.setFechaFin(fechaFin);

        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorPeriodo(periodo);
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

    @Test
    void pruebaGetColaboracionPorIdiomaExitosa () {
        List<ColaboracionDTO> listaColaboraciones = null;
        String idioma = "Español";
        int tamanoEsperado = 2;
        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdiomaExitosa");
        }
        assertEquals(tamanoEsperado, listaColaboraciones.size(), "pruebaGetColaboracionPorIdiomaExitosa");
    }

    @Test
    void pruebaGetColaboracionPorIdiomaCadenaVacio () {
        List<ColaboracionDTO> listaColaboraciones = null;
        int tamanoEsperado = 0;
        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionPorIdioma("");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdiomaExitosa");
        }
        assertEquals(tamanoEsperado, listaColaboraciones.size(), "pruebaGetColaboracionPorIdiomaExitosa");
    }


    @Test
    void pruebaCambiarEstadoColaboracionExitosa () {
        int tamanoEsperado = 1;
        int tamanoObtenido = 0;
        try {
            tamanoObtenido = COLABORACION_DAO.cambiarEstadoColaboracion("vinculada", 1);
        }
        catch (ErrorDAO error) {
            fail("Error en pruebaCambiarEstadoColaboracionExitosa: " + error.getMessage());
        }
        assertEquals(tamanoEsperado, tamanoObtenido, "pruebaCambiarEstadoColaboracionExitosa");
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
        int filasAfectadasEsperado = 0;
        int filasAfectadasObtenido = 0;

        try {
            filasAfectadasObtenido = COLABORACION_DAO.cambiarEstadoColaboracion("vinculada", 99);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaCambiarEstadoColaboracionIdInexistenteFallida");
        }
        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido, "pruebaCambiarEstadoColaboracionIdInexistenteFallida");
    }


    @Test
    void pruebaAgregarEstudianteAColaboracionExitoso () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(1);

        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setIdEstudiante(2);

        int filasAfectadasEsperadas = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracion, estudiante);
        }
        catch (ErrorDAO error) {
            fail("pruebaAgregarEstudianteAColaboracionExitoso " + error.getMessage());

        }
        assertEquals(filasAfectadasEsperadas, filasAfectadasObtenido, "pruebaAgregarEstudianteAColaboracionExitoso");
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionColaboracionIdInvalido () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(-1);
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setIdEstudiante(1);

        try {
            COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracion, estudiante);
            fail("pruebaAgregarEstudianteAColaboracionColaboracionIdInvalido");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaAgregarEstudianteAColaboracionColaboracionIdInvalido");
        }
    }

    @Test
    void pruebaAgregarEstudianteAColaboracionEstudianteIdNulo () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(0);

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
        colaboracionDTO.setIdColaboracion(-1);
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(-1);

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
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setTemaInteres(null);
        colaboracion.setObjetivo("Objetivo de prueba");
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("123456");

        try {
            COLABORACION_DAO.registrarPropuestaColaboracion(colaboracion, academico);
            fail("pruebaRegistrarPropuestaColaboracionTemaInteresNulo");
        }
        catch (ErrorDAO errorDAO) {
            assertTrue(true, "pruebaRegistrarPropuestaColaboracionTemaInteresNulo");
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionObjetivoNulo () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setTemaInteres("Tema de prueba");
        colaboracion.setObjetivo(null);
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("123456");
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.registrarPropuestaColaboracion(colaboracion, academico));
    }

    @Test
    void pruebaRegistrarSolicitudParticipacionColaboracionExitosa () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("102939");

        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(1);

        int filasAfectadasObtenidas = 0;
        try {
            filasAfectadasObtenidas = COLABORACION_DAO.registrarSolicitudParticipacion(colaboracion, academico);
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaRegistrarSolicitudParticipacionExitossa");
        }
        assertEquals(1, filasAfectadasObtenidas);
    }


    @Test
    void pruebaRegistrarSolicitudParticipacionColaboracionIdInvalido () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(-1);
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("123456");
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.registrarSolicitudParticipacion(colaboracion, academico), "pruebaRegistrarSolicitudParticipacionColaboracionIdInvalido");

    }

    @Test
    void pruebaRegistrarSolicitudParticipacionColaboracionNula () {
        ColaboracionDTO colaboracion = null;
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("200011");
        assertThrows(NullPointerException.class, () -> COLABORACION_DAO.registrarSolicitudParticipacion(colaboracion, academico), "pruebaRegistrarSolicitudParticipacionColaboracionNula");

    }

    @Test
    void pruebaRegistrarSolicitudParticipacionAcademicoNulo () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(1);
        AcademicoDTO academico = null;

        assertThrows(NullPointerException.class, () -> COLABORACION_DAO.registrarSolicitudParticipacion(colaboracion, academico), "pruebaRegistrarSolicitudParticipacionAcademicoNulo");
    }

    @Test
    void pruebaGetActivaPorAcademicoCedulaExitosa () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("200011");

        Optional<ColaboracionDTO> colaboracionObtenido = Optional.empty();

        try {
            colaboracionObtenido = COLABORACION_DAO.getActivaPorAcademico(academico);
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetActivaPorAcademicoCedulaExitosa");
        }

        assertTrue(colaboracionObtenido.isPresent());
    }

    @Test
    void pruebaGetActivaPorAcademicoNull () {
        AcademicoDTO academico = null;
        assertThrows(NullPointerException.class, () -> COLABORACION_DAO.getActivaPorAcademico(academico), "pruebaGetActivaPorAcademicoNull");
    }

    @Test
    void pruebaGetActivaPorAcademicoNoExiste () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("000000");

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getActivaPorAcademico(academico);
            assertFalse(colaboracion.isPresent(), "pruebaGetActivaPorAcademicoNoExiste");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetActivaPorAcademicoNoExiste: " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetColaboracionAceptadaExitosa () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setCedulaProfesional("102939");
        Optional<ColaboracionDTO> colaboracionDTOOptional = Optional.empty();
        try {
            colaboracionDTOOptional = COLABORACION_DAO.getColaboracionAceptadaPorAcademico(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetColaboracionAceptadaExitosa");
        }
        assertTrue(colaboracionDTOOptional.isPresent());
    }

    @Test
    void pruebaGetColaboracionAceptadaPorAcademicoNull () {
        AcademicoDTO academico = null;
        assertThrows(NullPointerException.class, () -> COLABORACION_DAO.getColaboracionAceptadaPorAcademico(academico), "pruebaGetColaboracionAceptadaPorAcademicoNull");
    }

    @Test
    void pruebaGetColaboracionAceptadaPorAcademicoNoExiste () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("000000");

        try {
            Optional<ColaboracionDTO> colaboracion = COLABORACION_DAO.getColaboracionAceptadaPorAcademico(academico);
            assertFalse(colaboracion.isPresent(), "pruebaGetColaboracionAceptadaPorAcademicoNoExiste");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetColaboracionAceptadaPorAcademicoNoExiste: " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetPropuestaPorAcademicoNull () {
        AcademicoDTO academicoDTO = null;
        assertThrows(NullPointerException.class, () -> COLABORACION_DAO.getPropuestaPorAcademico(academicoDTO), "pruebaGetPropuestaPorAcademicoNull");
    }

    @Test
    void pruebaGetPropuestaPorAcademicoNoExiste () {
        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("000000");

        try {
            Optional<ColaboracionDTO> colaboracionObtenida = COLABORACION_DAO.getPropuestaPorAcademico(academico);
            assertFalse(colaboracionObtenida.isPresent(), "pruebaGetPropuestaPorAcademicoNoExiste");
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetPropuestaPorAcademicoNoExiste: " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionExitosa () {
        System.out.println("pruebaRegistrarPropuestaColaboracionExitosa");

        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setTemaInteres("Inteligencia Artificial");
        colaboracion.setObjetivo("Mejorar habilidades en IA");
        colaboracion.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);

        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("102939");

        int filasAfectadas = 0;
        try {
            filasAfectadas = COLABORACION_DAO.registrarPropuestaColaboracion(colaboracion, academico);
        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaRegistrarPropuestaColaboracionExitosa " + errorDAO.getMessage());
        }

        assertEquals(2, filasAfectadas);
    }

    @Test
    void pruebaRegistrarPropuestaColaboracionEstadoNulo () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setTemaInteres("Desarrollo de aplicaciones móviles educativas.");
        colaboracion.setObjetivo("Crear una app para el aprendizaje de matemáticas.");
        colaboracion.setEstado(null);

        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("123456");
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.registrarPropuestaColaboracion(colaboracion, academico), "pruebaRegistrarPropuestaColaboracionEstadoNulo");

    }

    @Test
    void pruebaGetPropuestasColaboracionExitosa () {
        try {
            List<ColaboracionDTO> listaColaboracion = COLABORACION_DAO.getPropuestasColaboracion();
            int tamanoEsperado = 1;
            assertEquals(tamanoEsperado, listaColaboracion.size());
        }
        catch (ErrorDAO errorDAO) {
            fail("Error al obtener propuestas de colaboración: " + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetPropuestasColaboracionSinPropuestas () {
        List<ColaboracionDTO> listaColaboraciones = null;
        int ListacolaboracionesEsperadas = 1;
        try {
            listaColaboraciones = COLABORACION_DAO.getPropuestasColaboracion();
        }
        catch (ErrorDAO error) {
            fail("Error en pruebaGetPropuestasColaboracionSinPropuestas: " + error.getMessage());
        }
        assertEquals(ListacolaboracionesEsperadas, listaColaboraciones.size(), "pruebaGetPropuestasColaboracionSinPropuestas");
    }

    @Test
    void pruebaGetColaboracionesDisponiblesSinResultados () {
        List<ColaboracionDTO> listaColaboraciones = null;
        int listaColaboracionesEsperadas = 0;

        try {
            listaColaboraciones = COLABORACION_DAO.getColaboracionesDisponibles("43", 1);
        }
        catch (ErrorDAO error) {
            fail("Error en pruebaGetColaboracionesDisponiblesSinResultados: " + error.getMessage());
        }

        assertEquals(listaColaboracionesEsperadas, listaColaboraciones.size(), "pruebaGetColaboracionesDisponiblesSinResultados");
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
    void pruebaActualizarEstadoSolicitudExitoso () {
        int idColaboracion = 1;
        String cedulaProfesional = "200011";
        String nuevoEstado = "aceptado";

        int filasAfectadasObtenida = 0;
        try {
            filasAfectadasObtenida = COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
        }
        catch (ErrorDAO error) {
            fail("pruebaActualizarEstadoSolicitudExitoso");
        }
        assertEquals(1, filasAfectadasObtenida);
    }

    @Test
    void pruebaActualizarEstadoSolicitudDeParticipacionEstadoInvalido () {
        int idColaboracion = 1;
        String cedulaProfesional = "200011";
        String nuevoEstado = "Iniciado";
        assertThrows(ErrorDAO.class, () -> COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado));
    }

    @Test
    void pruebaActualizarEstadoSolicitudDeParticipacionIdColaboracionInexistente () {
        int idColaboracion = 10;
        String cedulaProfesional = "12345678";
        String nuevoEstado = "aceptado";

        int filasAfectadasEsperados = 0;
        int filasAfectadasObtenido = 0;
        try {
            filasAfectadasObtenido = COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaActualizarEstadoSolicitudDeParticipacionIdColaboracionInvalido");
        }

        assertEquals(filasAfectadasEsperados, filasAfectadasObtenido, "pruebaActualizarEstadoSolicitudDeParticipacionIdColaboracionInvalido");
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
        int filasAfectadasEsperada = 0;
        int filasAfectadasObtenidas = 0;
        try {
            filasAfectadasObtenidas = COLABORACION_DAO.eliminarSolicitudDeParticipacion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaEliminarSolicitudDeParticipacionEstadoInvalido");
        }
        assertEquals(filasAfectadasEsperada, filasAfectadasObtenidas, "pruebaEliminarSolicitudDeParticipacionEstadoInvalido");
    }


    @Test
    void pruebaActualizarEstadoSolicitudDeParticipacionCedulaInexistente () {
        int idColaboracion = 1;
        String cedulaProfesional = "99999999";
        String nuevoEstado = "aceptado";

        int filasAfectadasEsperado = 0;
        int filasAfectadasObtenidos = -1;
        try {
            filasAfectadasObtenidos = COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaActualizarEstadoSolicitudDeParticipacionCedulaInvalida");
        }

        assertEquals(filasAfectadasEsperado, filasAfectadasObtenidos, "pruebaActualizarEstadoSolicitudDeParticipacionCedulaInvalida");
    }

    @Test
    void pruebaAgregarAcademicoAColaboracionExitoso () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(2);

        AcademicoDTO academico = new AcademicoDTO();
        academico.setCedulaProfesional("102939");

        int filasAfectadasEsperado = 1;
        int filasAfectadasObtenido = -1;

        try {
            filasAfectadasObtenido = COLABORACION_DAO.registrarSolicitudParticipacion(colaboracion, academico);

        }
        catch (ErrorDAO error) {
            fail("pruebaAgregarAcademicoAColaboracionExitoso " + error.getMessage());

        }
        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido, "pruebaAgregarAcademicoAColaboracionExitoso");
    }

    @Test
    void pruebaGetAcademicoParColaboracionNoExistente () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(-1);

        Optional<AcademicoDTO> academicoObtenido = Optional.empty();
        try {
            academicoObtenido = COLABORACION_DAO.getAcademicoPar(colaboracion);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicoParColaboracionNoExistente");
        }
        assertFalse(academicoObtenido.isPresent(), "pruebaGetAcademicoParColaboracionNoExistente");
    }

    @Test
    void pruebaRetirarEstudianteDeColaboracionEstudianteInexistente () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdEstudiante(999);

        int filasAfectadosEsperado = 0;
        int filasAfectadosObtenido = 0;
        try {
            filasAfectadosObtenido = COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracionDTO, estudianteDTO);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRetirarEstudianteDeColaboracionEstudianteInexistente");
        }
        assertEquals(filasAfectadosEsperado, filasAfectadosObtenido, "pruebaRetirarEstudianteDeColaboracionEstudianteInexistente");
    }

    @Test
    void pruebaRetirarEstudianteDeColaboracionColaboracionInexistente () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(999);

        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setIdEstudiante(1);

        int filasAfectadasEsperado = 0;
        int filasAfectadasObtenido = 0;
        try {
            filasAfectadasObtenido = COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracion, estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaRetirarEstudianteDeColaboracionColaboracionInexistente");
        }
        assertEquals(filasAfectadasEsperado, filasAfectadasObtenido, "pruebaRetirarEstudianteDeColaboracionColaboracionInexistente");
    }

    @Test
    void pruebaRetirarEstudianteDeColaboracionIdsInexistentes () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();
        colaboracion.setIdColaboracion(-1);

        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setIdEstudiante(-1);

        int filasAfectadas = 0;
        try {
            filasAfectadas = COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracion, estudiante);
        }
        catch (ErrorDAO error) {
            fail("Fallida : pruebaRetirarEstudianteDeColaboracionIdsInexistentes");
        }
        assertEquals(0, filasAfectadas, "pruebaRetirarEstudianteDeColaboracionCamposInvalidos");
    }

    @Test
    void pruebaRegistrarColaboracionExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = instanciarColaboracion();
        int filasAfectadasEsperadas = 1;
        int filasAfectadasObtenido = 0;

        try {
            filasAfectadasObtenido = COLABORACION_DAO.agregar(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("pruebaRegistrarColaboracionExitoso " + error.getMessage());

        }
        assertEquals(filasAfectadasEsperadas, filasAfectadasObtenido, "registrarColaboracion");
    }

    @Test
    void pruebaActualizarColaboracionExitoso () {
        ColaboracionDTO colaboracion = new ColaboracionDTO();

        colaboracion.setIdColaboracion(1);
        colaboracion.setTipo(ColaboracionDTO.TipoColaboracion.claseEspejo);
        colaboracion.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
        colaboracion.setTemaInteres("La existence: El todo a traves del nada");
        colaboracion.setIdioma("Español");
        colaboracion.setObjetivo("Mejorar habilidades en IA");
        colaboracion.setPerfilEstudiante("Estudiantes de informática");

        PeriodoDTO periodo = new PeriodoDTO();
        periodo.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodo.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracion.setPeriodo(periodo);

        int filasAfectadasEsperada = 1;
        int filasAfectadasObtenido = 0;

        try {
            filasAfectadasObtenido = COLABORACION_DAO.modificar(colaboracion);

        }
        catch (ErrorDAO error) {
            fail("pruebaActualizarColaboracionExitoso " + error.getMessage());

        }

        assertEquals(filasAfectadasEsperada, filasAfectadasObtenido, "pruebaActualizarColaboracionExitoso");

    }

    @Test
    void pruebaGetTodosExitosa () {
        List<ColaboracionDTO> listaColaboracionDTO = null;

        int tamanoEsperado = 3;

        try {
            listaColaboracionDTO = COLABORACION_DAO.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetTodosExitosa " + errorDAO.getMessage());
        }
        assertEquals(tamanoEsperado, listaColaboracionDTO.size(), "pruebaGetTodosExitosa");
    }

}