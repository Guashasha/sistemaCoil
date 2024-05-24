package test.AccesoADatos;

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

//    @AfterEach
//    void tearDown () {
//        AyudantePruebasColaboracionDB.borrarTodosDatosTabla();
//    }

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
        assertEquals(esperado,obtenido,"pruebaGetColaboracionPorIdExitosa");
    }

    @Test
    void pruebaGetColaboracionIdInexistente () {
        try {
            Optional<ColaboracionDTO> resultado = COLABORACION_DAO.getColaboracionPorId(10);
            assertTrue(resultado.isEmpty(),"pruebaGetColaboracionIdInexistente");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetListaDeEstudiantesExitosa () {
        System.out.println("pruebaGetListaDeEstudiantesExitosa");
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);

        List<EstudianteDTO> esperada = new ArrayList<>();
        List<EstudianteDTO> obtenida = new ArrayList<>();
        EstudianteDTO estudianteDTO1 = new EstudianteDTO();
        EstudianteDTO estudianteDTO2 = new EstudianteDTO();

        estudianteDTO1.setIdPersona(4);
        estudianteDTO1.setNombre("Eduardo");
        estudianteDTO1.setApellidoPaterno("Villegas");
        estudianteDTO1.setApellidoMaterno("Hurtado");
        estudianteDTO1.setIdUniversidad(1);
        estudianteDTO1.setIdEstudiante(1);
        estudianteDTO1.setMatricula("zs22013693");

        estudianteDTO2.setIdPersona(5);
        estudianteDTO2.setNombre("John");
        estudianteDTO2.setApellidoPaterno("Smith");
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

        assertEquals(esperada.size(),obtenida.size());
        for (EstudianteDTO estudianteDTO : esperada) {
            assertEquals(estudianteDTO,obtenida.get(0));
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
        assertTrue(resultado.isEmpty(),"pruebaGetListadeEstudiantesColaboracionInexistente");
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
        System.out.println("pruebaGetAcademicosParticipantesExitoso");
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);
        List<AcademicoDTO> esperada = new ArrayList<>();
        List<AcademicoDTO> obtenida = new ArrayList<>();

        AcademicoDTO academicoDTO1 = new AcademicoDTO();

        AcademicoDTO academicoDTO2 = new AcademicoDTO();

        esperada.add(academicoDTO1);
        esperada.add(academicoDTO2);

        try {
            obtenida = COLABORACION_DAO.getAcademicosParticipantes(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicosParticipantesExitoso " + error.getMessage());
        }

        assertEquals(esperada.size(), obtenida.size());
        for (AcademicoDTO academicoDTO : esperada) {
            assertEquals(academicoDTO,obtenida.get(0));
            obtenida.remove(0);
        }
    }

    @Test
    void pruebaGetAcademicosParticipantesColaboracionInexistente () {

    }

    @Test
    void pruebaGetAcademicosParticipantesColaboracionVacia () {

    }

    @Test
    void pruebaGetColaboracionPorPeriodoExitosa() {
        System.out.println("pruebaGetColaboracionPorPeriodoExitosa");

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

        assertEquals(colaboracionesEsperadas, listaColaboraciones.size());

    }

    @Test
    void pruebaCambiarEstadoColaboracionExitosa() {
        System.out.println("pruebaCambiarEstadoColaboracionExitosa");


        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(1);
        colaboracionDTOPrueba.setEstado(ColaboracionDTO.EstadoColaboracion.vinculada);


        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.cambiarEstadoColaboracion(colaboracionDTOPrueba);


        } catch (ErrorDAO error) {

            fail("Error en pruebaCambiarEstadoColaboracionExitosa: " + error.getMessage());
        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaAgregarEstudianteAColaboracionExitoso () {
        System.out.println("pruebaAgregarEstudianteAColaboracionExitoso");

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

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaAgregarAcademicoAColaboracionExitoso () {
        System.out.println("pruebaAgregarAcademicoAColaboracionExitoso");

        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();
        colaboracionDTOPrueba.setIdColaboracion(2);

        AcademicoDTO academicoDTOPrueba = new AcademicoDTO();
        academicoDTOPrueba.setCedulaProfesional("102939");

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido =  COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTOPrueba, academicoDTOPrueba);

        }
        catch (ErrorDAO error) {
            fail("pruebaAgregarAcademicoAColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaRegistrarColaboracionExitoso () {
        System.out.println("registrarColaboracion");
        ColaboracionDTO colaboracionDTOPrueba = instanciarColaboracion();
        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = COLABORACION_DAO.agregar(colaboracionDTOPrueba);

        }
        catch (ErrorDAO error) {
            fail("pruebaRegistrarColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaActualizarColaboracionExitoso () {
        System.out.println("pruebaActualizarColaboracionExitoso");

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

        assertEquals(esperado, obtenido);

    }

   @Test
    void pruebaGetTodosExitosa () {
        System.out.println("pruebaGetTodosExitosa");

        List<ColaboracionDTO> listaColaboracionDTO = null;

        int tamanoEsperado = 2;

        try {
            listaColaboracionDTO = COLABORACION_DAO.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            errorDAO.printStackTrace();
            fail("pruebaGetTodosExitosa " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaColaboracionDTO.size());

   }




}