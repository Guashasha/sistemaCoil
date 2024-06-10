package test.DAO;

import DAO.ColaboracionAuxiliar;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ColaboracionDTOAuxiliarTest {
    private final ColaboracionAuxiliar INSTANCIA = new ColaboracionAuxiliar();

    @BeforeEach
    void setUp () {
        AyudantePruebasColaboracionDB.borrarTodosDatosTabla();
        AyudantePruebasColaboracionDB.agregarPrecondiciones();
    }

    @AfterEach
    void tearDown () {
        AyudantePruebasColaboracionDB.borrarTodosDatosTabla();
    }


    @Test
    void pruebaGetColaboracionPorIdExitosa () {
        System.out.println("pruebaGetColaboracionPorIdExitosa");
        ColaboracionDTO colaboracionDTO = null;

        try {
            Optional<ColaboracionDTO> optional = INSTANCIA.getColaboracionPorId(1);
            colaboracionDTO = optional.get();
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruebaGetColaboracionPorIdExitosa");
        }
        assertNotNull(colaboracionDTO);
    }

    @Test
    void pruetaGetListaDeEstudiantesExitosa () {
        List<EstudianteDTO> listaEstudianteDTO = null;
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        int tamanoEsperado = 1;
        try {
            listaEstudianteDTO = INSTANCIA.getListaDeEstudiantes(colaboracionDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruetaGetListaDeEstudiantesExitosa");
        }
        assertEquals(tamanoEsperado, listaEstudianteDTO.size());
    }

    @Test
    void pruebaGetAcademicosParticipantesExitoso () {
        List<AcademicoDTO> listaAcademicoDTOS = null;
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setIdColaboracion(1);
        int tamanoEsperado = 2;
        try {
            listaAcademicoDTOS = INSTANCIA.getAcademicosParticipantes(colaboracionDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruebaGetAcademicosParticipantesExitoso");
        }
        assertEquals(tamanoEsperado, listaAcademicoDTOS.size());
    }

    @Test
    void pruebaGetColaboracionPorPeriodoExitoso () {

        LocalDate fechaInicio = LocalDate.of(2024, 5, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);

        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setFechaInicio(fechaInicio);
        periodoDTO.setFechaFin(fechaFin);

        List<ColaboracionDTO> listaColaboraciones = null;
        int colaboracionesEsperadas = 1;

        try {
            listaColaboraciones = INSTANCIA.getColaboracionPorPeriodo(periodoDTO);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorPeriodoExitoso");
        }
        assertEquals(colaboracionesEsperadas, listaColaboraciones.size());
    }

    @Test
    void pruebaGetColaboracionPorIdiomaExitosa () {
        System.out.println("pruebaGetColaboracionPorIdiomaExitosa");
        List<ColaboracionDTO> listaColaboraciones = null;
        String idioma = "Español";
        int esperado = 1;
        try {
            listaColaboraciones = INSTANCIA.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdiomaExitosa");
        }
        assertEquals(esperado, listaColaboraciones.size());
    }

    @Test
    void pruebaGetColaboracionPorEstadoExitoso () {
        System.out.println("pruebaGetColaboracionPorEstadoExitoso");

        List<ColaboracionDTO> listaColaboracionDTO = null;

        String estado = "propuesta";
        int tamanoEsperado = 1;

        try {
            listaColaboracionDTO = INSTANCIA.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO errorDAO){
            fail("Fallido: pruebaGetColaboracionPorEstadoExitoso");
        }
        assertEquals(tamanoEsperado, listaColaboracionDTO.size());
    }

    @Test
    void pruebaCambiarEstadoColaboracionExitoso () {
        System.out.println("pruebaCambiarEstadoColaboracionExitosa");

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = INSTANCIA.cambiarEstadoColaboracion("vinculada", 1);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaCambiarEstadoColaboracionExitoso");
        }
        assertEquals(esperado, obtenido);
    }

    @Test
    void agregarEstudianteAColaboracion () {
    }

    @Test
    void agregarAcademicoAColaboracion () {
    }

    @Test
    void pruebaAgregarExitoso () {
        ColaboracionDTO colaboracionDTOPrueba = new ColaboracionDTO();

        colaboracionDTOPrueba.setTipo(ColaboracionDTO.TipoColaboracion.claseEspejo);
        colaboracionDTOPrueba.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
        colaboracionDTOPrueba.setTemaInteres("Inteligencia Artificial");
        colaboracionDTOPrueba.setIdioma("Español");
        colaboracionDTOPrueba.setObjetivo("Mejorar habilidades en IA");
        colaboracionDTOPrueba.setPerfilEstudiante("Estudiantes de informática");

        PeriodoDTO periodoDTOPrueba = new PeriodoDTO();
        periodoDTOPrueba.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodoDTOPrueba.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracionDTOPrueba.setPeriodo(periodoDTOPrueba);

        int filasEsperadas = 1;
        int filasObtenidas = 0;
        try {
            filasObtenidas = INSTANCIA.agregar(colaboracionDTOPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarExitoso");
        }

        assertEquals(filasEsperadas, filasObtenidas);

    }

    @Test
    void modificar () {
    }

    @Test
    void getPorId () {
    }

    @Test
    void getTodos () {
    }

    @Test
    void resultSetAObjeto () {
    }
}