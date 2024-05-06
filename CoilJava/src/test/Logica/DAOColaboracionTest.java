package test.Logica;

import Logica.DAO.DAOColaboracion;
import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DAOColaboracionTest {
    private final DAOColaboracion INSTANCIA = new DAOColaboracion();

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
    void pruebaGetColaboracionPorAcademicosParticipantesExitosa () {
        System.out.println("getColaboracionPorAcademicosParticipantes");
        Academico academico1 = new Academico();
        academico1.setCedulaProfesional("ABC123");

        Academico academico2 = new Academico();
        academico2.setCedulaProfesional("200011");

        Colaboracion colaboracion = null;

        try {
            Optional optional = INSTANCIA.getColaboracionPorAcademicosParticipantes(academico1, academico2);
            colaboracion = (Colaboracion) optional.get();

        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: getColaboracionPorAcademicosParticipantes");
        }
        assertNotNull(colaboracion);

    }

    @Test
    void pruebaGetColaboracionPorIdExitosa () {
        System.out.println("pruebaGetColaboracionPorIdExitosa");
        Colaboracion colaboracion = null;

        try {
            Optional optional = INSTANCIA.getColaboracionPorId(1);
            colaboracion = (Colaboracion) optional.get();
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruebaGetColaboracionPorIdExitosa");
        }
        assertNotNull(colaboracion);
    }

    @Test
    void pruetaGetListaDeEstudiantesExitosa () {
        List<Estudiante> listaEstudiante = null;
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdColaboracion(1);
        int tamanoEsperado = 1;
        try {
            listaEstudiante = INSTANCIA.getListaDeEstudiantes(colaboracion);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruetaGetListaDeEstudiantesExitosa");
        }
        assertEquals(tamanoEsperado, listaEstudiante.size());
    }

    @Test
    void pruebaGetAcademicosParticipantesExitoso () {
        List<Academico> listaAcademicos = null;
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdColaboracion(1);
        int tamanoEsperado = 2;
        try {
            listaAcademicos = INSTANCIA.getAcademicosParticipantes(colaboracion);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallido: pruebaGetAcademicosParticipantesExitoso");
        }
        assertEquals(tamanoEsperado, listaAcademicos.size());
    }

    @Test
    void pruebaGetColaboracionPorPeriodoExitoso () {

        LocalDate fechaInicio = LocalDate.of(2024, 5, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);

        Periodo periodo = new Periodo();
        periodo.setFechaInicio(fechaInicio);
        periodo.setFechaFin(fechaFin);

        List<Colaboracion> listaColaboraciones = null;
        int colaboracionesEsperadas = 1;

        try {
            listaColaboraciones = INSTANCIA.getColaboracionPorPeriodo(periodo);
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorPeriodoExitoso");
        }
        assertEquals(colaboracionesEsperadas, listaColaboraciones.size());
    }

    @Test
    void pruebaGetColaboracionPorIdiomaExitosa () {
        System.out.println("pruebaGetColaboracionPorIdiomaExitosa");
        List<Colaboracion> listaColaboraciones = null;
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

        List<Colaboracion> listaColaboracion = null;

        String estado = "propuesta";
        int tamanoEsperado = 1;

        try {
            listaColaboracion = INSTANCIA.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO errorDAO){
            fail("Fallido: pruebaGetColaboracionPorEstadoExitoso");
        }
        assertEquals(tamanoEsperado, listaColaboracion.size());
    }

    @Test
    void pruebaCambiarEstadoColaboracionExitoso () {
        System.out.println("pruebaCambiarEstadoColaboracionExitosa");


        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);
        colaboracionPrueba.setEstado(Colaboracion.EstadoColaboracion.vinculada);


        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = INSTANCIA.cambiarEstadoColaboracion(colaboracionPrueba);
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
        Colaboracion colaboracionPrueba = new Colaboracion();

        colaboracionPrueba.setTipo(Colaboracion.TipoColaboracion.claseEspejo);
        colaboracionPrueba.setEstado(Colaboracion.EstadoColaboracion.propuesta);
        colaboracionPrueba.setTemaInteres("Inteligencia Artificial");
        colaboracionPrueba.setIdioma("Español");
        colaboracionPrueba.setObjetivo("Mejorar habilidades en IA");
        colaboracionPrueba.setPerfilEstudiante("Estudiantes de informática");

        Periodo periodoPrueba = new Periodo();
        periodoPrueba.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodoPrueba.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracionPrueba.setPeriodo(periodoPrueba);

        int filasEsperadas = 1;
        int filasObtenidas = 0;
        try {
            filasObtenidas = INSTANCIA.agregar(colaboracionPrueba);
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