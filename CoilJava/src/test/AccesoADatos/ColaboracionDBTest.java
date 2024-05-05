package test.AccesoADatos;

import AccesoADatos.ColaboracionDB;
import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColaboracionDBTest {

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
        System.out.println("pruebaGetColaboracionPorAcademicosParticipantesExitosa");

        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdColaboracion(1);
        colaboracion.setEstado(Colaboracion.EstadoColaboracion.valueOf("propuesta"));
        colaboracion.setTipo(Colaboracion.TipoColaboracion.claseEspejo);
        colaboracion.setTemaInteres("Inteligencia Artificial");
        colaboracion.setIdioma("Español");
        colaboracion.setObjetivo("Mejorar habilidades en IA");

        Periodo periodo = new Periodo();

        periodo.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodo.setFechaFin(LocalDate.parse("2024-06-30"));
        colaboracion.setPeriodo(periodo);
        colaboracion.setPerfilEstudiante("Estudiantes de informática");

        Academico academico1 = new Academico();
        academico1.setCedulaProfesional("ABC123");

        Academico academico2 = new Academico();
        academico2.setCedulaProfesional("200011");

        Colaboracion colaboracionReal = null;

        try {
            colaboracionReal = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
            System.out.println(colaboracionReal.toString());

        }
        catch (SQLException error) {
            fail("Error en pruebaGetColaboracionPorAcademicosParticipantesExitosa" + error.getMessage());
        }

        assertEquals(colaboracion.getIdColaboracion(), colaboracionReal.getIdColaboracion());
    }

    @Test
    void pruebaGetColaboracionPorAcademicosFallida () {
        System.out.println("pruebaGetColaboracionPorAcademicosFallida");

        Academico academico1 = new Academico();
        academico1.setCedulaProfesional("ACDC123");

        Academico academico2 = new Academico();
        academico2.setCedulaProfesional("22342011");

        Colaboracion colaboracion = null;

        try {
            colaboracion = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (SQLException error) {
            fail("Error en pruebaGetColaboracion");

        }

        assertNull(colaboracion);
    }

    @Test
    void pruebaGetColaboracionPorAcademicoUnAcademicoVacioFallida () {
        Academico academico1 = new Academico();

        Academico academico2 = new Academico();
        academico2.setCedulaProfesional("22342011");

        Colaboracion colaboracion = null;

        try {
            colaboracion = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (SQLException error) {
            fail("Error en pruebaGetColaboracion");

        }

        assertNull(colaboracion);
    }

    @Test
    void pruebaGetColaboracionPorAcademicosVacios () {
        Academico academico1 = new Academico();

        Academico academico2 = new Academico();

        Colaboracion colaboracion = null;

        try {
            colaboracion = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (SQLException error) {
            fail("Error en pruebaGetColaboracion");

        }

        assertNull(colaboracion);
    }


    @Test
    void pruebaGetColaboracionPorIdExitosa () {
        System.out.println("pruebaGetColaboracionPorIdExitosa");

        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);
        colaboracionPrueba.setEstado(Colaboracion.EstadoColaboracion.valueOf("propuesta"));
        colaboracionPrueba.setTipo(Colaboracion.TipoColaboracion.claseEspejo);
        colaboracionPrueba.setTemaInteres("Inteligencia Artificial");
        colaboracionPrueba.setIdioma("Español");
        colaboracionPrueba.setObjetivo("Mejorar habilidades en IA");

        Periodo periodo = new Periodo();

        periodo.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodo.setFechaFin(LocalDate.parse("2024-06-30"));
        colaboracionPrueba.setPeriodo(periodo);
        colaboracionPrueba.setPerfilEstudiante("Estudiantes de informática");

        Colaboracion colaboracionReal = null;

        try {
            colaboracionReal = ColaboracionDB.getColaboracionPorId(1);
            System.out.print(colaboracionReal.toString());

        }
        catch (SQLException errorDAO) {
            fail("Error en pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }

        assertEquals(colaboracionPrueba.getIdColaboracion(), colaboracionReal.getIdColaboracion());
    }

    @Test
    void pruebaGetColaboracionIdInexistente () {
        System.out.println("pruebaGetColaboracionIdInexistente");

        Colaboracion colaboracionReal = null;

        try {
            colaboracionReal = ColaboracionDB.getColaboracionPorId(10);

        }
        catch (SQLException errorDAO) {
            fail("Error en pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }

        assertNull(colaboracionReal);
    }

    @Test
    void pruebaGetListaDeEstudiantesExitosa () {
        System.out.println("pruebaGetListaDeEstudiantesExitosa");

        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);

        List<Estudiante> listaEstudiante =null;

        int tamanoEsperado = 1;

        try {
            listaEstudiante = ColaboracionDB.getListaDeEstudiantes(colaboracionPrueba);

        }
        catch (SQLException error) {
            fail("Error pruebaGetListaDeEstudiantesExitosa " + error.getMessage());

        }
        assertEquals(tamanoEsperado, listaEstudiante.size());
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionInexistente () {
        System.out.println("pruebaGetListadeEstudiantesColaboracionInexistente");

        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(40);

        List<Estudiante> listaEstudiante =null;
        int tamanoEsperado = 0;

        try {
            listaEstudiante = ColaboracionDB.getListaDeEstudiantes(colaboracionPrueba);

        }
        catch (SQLException error) {
            fail("Error pruebaGetListaDeEstudiantesExitosa " + error.getMessage());

        }
        assertEquals(tamanoEsperado, listaEstudiante.size());
    }

    @Test
    void pruebaGetAcademicosParticipantesExitoso () {
        System.out.println("pruebaGetAcademicosParticipantesExitoso");

        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);

        List<Academico> listaAcademico = null;

        int tamanoEsperado = 2;

        try {
            listaAcademico = ColaboracionDB.getAcademicosParticipantes(colaboracionPrueba);

        }
        catch (SQLException error) {
            fail("pruebaGetAcademicosParticipantesExitoso " + error.getMessage());
        }

        assertEquals(tamanoEsperado, listaAcademico.size());
    }

    @Test
    void pruebaGetColaboracionPorPeriodoExitosa() {
        System.out.println("pruebaGetColaboracionPorPeriodoExitosa");

        int colaboracionesEsperadas = 1;
        List<Colaboracion> listaColaboraciones = null;

        LocalDate fechaInicio = LocalDate.of(2024, 5, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);

        Periodo periodo = new Periodo();
        periodo.setFechaInicio(fechaInicio);
        periodo.setFechaFin(fechaFin);

        try {
            listaColaboraciones = ColaboracionDB.getColaboracionPorPeriodo(periodo);
            System.out.println(listaColaboraciones.get(0).toString());

        }
        catch (SQLException error) {
            fail("Error en pruebaGetColaboracionPorPeriodoExitosa: " + error.getMessage());

        }

        assertEquals(colaboracionesEsperadas, listaColaboraciones.size());

    }

    @Test
    void pruebaCambiarEstadoColaboracionExitosa() {
        System.out.println("pruebaCambiarEstadoColaboracionExitosa");


        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);
        colaboracionPrueba.setEstado(Colaboracion.EstadoColaboracion.vinculada);


        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = ColaboracionDB.cambiarEstadoColaboracion(colaboracionPrueba);


        } catch (SQLException error) {

            fail("Error en pruebaCambiarEstadoColaboracionExitosa: " + error.getMessage());
        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaAgregarEstudianteAColaboracionExitoso () {
        System.out.println("pruebaAgregarEstudianteAColaboracionExitoso");

        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);

        Estudiante estudiantePrueba = new Estudiante();
        estudiantePrueba.setIdEstudiante(2);

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = ColaboracionDB.agregarEstudianteAColaboracion(colaboracionPrueba, estudiantePrueba);

        }
        catch (SQLException error) {
            fail("pruebaAgregarEstudianteAColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaAgregarAcademicoAColaboracionExitoso () {
        System.out.println("pruebaAgregarAcademicoAColaboracionExitoso");

        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(2);

        Academico academicoPrueba = new Academico();
        academicoPrueba.setCedulaProfesional("102939");

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = ColaboracionDB.agregarAcademicoAColaboracion(colaboracionPrueba,academicoPrueba);

        }
        catch (SQLException error) {
            fail("pruebaAgregarAcademicoAColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaRegistrarColaboracionExitoso () {
        System.out.println("registrarColaboracion");

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

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = ColaboracionDB.registrarColaboracion(colaboracionPrueba);

        }
        catch (SQLException error) {
            fail("pruebaRegistrarColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaActualizarColaboracionExitoso () {
        System.out.println("pruebaActualizarColaboracionExitoso");

        Colaboracion colaboracionPrueba = new Colaboracion();

        colaboracionPrueba.setIdColaboracion(1);
        colaboracionPrueba.setTipo(Colaboracion.TipoColaboracion.claseEspejo);
        colaboracionPrueba.setEstado(Colaboracion.EstadoColaboracion.propuesta);
        colaboracionPrueba.setTemaInteres("La existence: El todo a traves del nada");
        colaboracionPrueba.setIdioma("Español");
        colaboracionPrueba.setObjetivo("Mejorar habilidades en IA");
        colaboracionPrueba.setPerfilEstudiante("Estudiantes de informática");

        Periodo periodoPrueba = new Periodo();
        periodoPrueba.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodoPrueba.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracionPrueba.setPeriodo(periodoPrueba);

        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = ColaboracionDB.actualizarColaboracion(colaboracionPrueba);

        }
        catch (SQLException error) {
            fail("pruebaActualizarColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

   @Test
    void pruebaGetTodosExitosa () {
        System.out.println("pruebaGetTodosExitosa");

        List<Colaboracion> listaColaboracion = null;

        int tamanoEsperado = 2;

        try {
            listaColaboracion = ColaboracionDB.getTodos();
            System.out.println(listaColaboracion.size());

        }
        catch (SQLException errorDAO) {
            fail("pruebaGetTodosExitosa " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaColaboracion.size());

   }




}