package test.AccesoADatos;

import AccesoADatos.ColaboracionDB;
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
import java.util.ArrayList;
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

    private static Colaboracion instanciarColaboracion () {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdColaboracion(1);
        colaboracion.setEstado(Colaboracion.EstadoColaboracion.propuesta);
        colaboracion.setTipo(Colaboracion.TipoColaboracion.claseEspejo);
        colaboracion.setTemaInteres("Inteligencia Artificial");
        colaboracion.setIdioma("Español");
        colaboracion.setObjetivo("Mejorar habilidades en IA");

        Periodo periodo = new Periodo();
        periodo.setFechaInicio(LocalDate.parse("2024-05-01"));
        periodo.setFechaFin(LocalDate.parse("2024-06-30"));

        colaboracion.setPeriodo(periodo);
        colaboracion.setPerfilEstudiante("Estudiantes de informática");
        return colaboracion;
    }

    @Test
    void pruebaGetColaboracionPorAcademicosParticipantesExitosa () {
        Colaboracion esperada = instanciarColaboracion();
        Colaboracion obtenida = null;
        Academico academico1 = new Academico();
        Academico academico2 = new Academico();
        academico1.setCedulaProfesional("ABC123");
        academico2.setCedulaProfesional("200011");
        try {
            obtenida = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetColaboracionPorAcademicosParticipantesExitosa" + error.getMessage());
        }

        assertEquals(esperada,obtenida,"pruebaGetColaboracionPorAcademicosParticipantesExitosa");
    }

    @Test
    void pruebaGetColaboracionPorAcademicosParticipantesFallida () {
        Academico academico1 = new Academico();
        Academico academico2 = new Academico();
        academico1.setCedulaProfesional("ACDC123");
        academico2.setCedulaProfesional("22342011");
        Colaboracion resultado = null;
        try {
            resultado = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetColaboracionPorAcademicosParticipantesFallida");
        }
        assertNull(resultado,"pruebaGetColaboracionPorAcademicosParticipantesFallida");
    }

    @Test
    void pruebaGetColaboracionPorAcademicosParticipantesAcademicoVacio () {
        Academico academico1 = new Academico();
        Academico academico2 = new Academico();
        academico2.setCedulaProfesional("22342011");
        Colaboracion resultado = null;
        try {
            resultado = ColaboracionDB.getColaboracionPorAcademicosParticipantes(academico1, academico2);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetColaboracionPorAcademicosParticipantesAcademicoVacio");
        }
        assertNull(resultado,"pruebaGetColaboracionPorAcademicosParticipantesAcademicoVacio");
    }

    @Test
    void pruebaGetColaboracionPorIdExitosa () {
        Colaboracion esperado = instanciarColaboracion();
        Colaboracion obtenido = null;
        try {
            obtenido = ColaboracionDB.getColaboracionPorId(1);
        }
        catch (ErrorDAO errorDAO) {
            fail("Falida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
        assertEquals(esperado,obtenido,"pruebaGetColaboracionPorIdExitosa");
    }

    @Test
    void pruebaGetColaboracionIdInexistente () {
        try {
            Colaboracion resultado = ColaboracionDB.getColaboracionPorId(10);
            assertNull(resultado,"pruebaGetColaboracionIdInexistente");
        }
        catch (ErrorDAO errorDAO) {
            fail("Fallida: pruebaGetColaboracionPorIdExitosa" + errorDAO.getMessage());
        }
    }

    @Test
    void pruebaGetListaDeEstudiantesExitosa () {
        System.out.println("pruebaGetListaDeEstudiantesExitosa");
        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);

        List<Estudiante> esperada = new ArrayList<>();
        List<Estudiante> obtenida = new ArrayList<>();
        Estudiante estudiante1 = new Estudiante();
        Estudiante estudiante2 = new Estudiante();

        estudiante1.setIdPersona(4);
        estudiante1.setNombre("Eduardo");
        estudiante1.setApellidoPaterno("Villegas");
        estudiante1.setApellidoMaterno("Hurtado");
        estudiante1.setIdUniversidad(1);
        estudiante1.setIdEstudiante(1);
        estudiante1.setMatricula("zs22013693");

        estudiante2.setIdPersona(5);
        estudiante2.setNombre("John");
        estudiante2.setApellidoPaterno("Smith");
        estudiante2.setApellidoMaterno("Onell");
        estudiante2.setIdUniversidad(2);
        estudiante2.setIdEstudiante(2);
        estudiante2.setMatricula("zs2201356");

        esperada.add(estudiante1);
        esperada.add(estudiante2);

        try {
            obtenida = ColaboracionDB.getListaDeEstudiantes(colaboracionPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListaDeEstudiantesExitosa " + error.getMessage());
        }

        assertEquals(esperada.size(),obtenida.size());
        for (Estudiante estudiante : esperada) {
            assertEquals(estudiante,obtenida.get(0));
            obtenida.remove(0);
        }
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionInexistente () {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdColaboracion(40);
        List<Estudiante> resultado = null;
        try {
            resultado = ColaboracionDB.getListaDeEstudiantes(colaboracion);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListadeEstudiantesColaboracionInexistente" + error.getMessage());
        }
        assertTrue(resultado.isEmpty(),"pruebaGetListadeEstudiantesColaboracionInexistente");
    }

    @Test
    void pruebaGetListadeEstudiantesColaboracionVacia () {
        try {
            List<Estudiante> resultado = ColaboracionDB.getListaDeEstudiantes(new Colaboracion());
            assertTrue(resultado.isEmpty(), "pruebaGetListadeEstudiantesColaboracionVacia");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetListadeEstudiantesColaboracionVacia");
        }
    }

    @Test
    void pruebaGetAcademicosParticipantesExitoso () {
        System.out.println("pruebaGetAcademicosParticipantesExitoso");
        Colaboracion colaboracionPrueba = new Colaboracion();
        colaboracionPrueba.setIdColaboracion(1);
        List<Academico> esperada = new ArrayList<>();
        List<Academico> obtenida = new ArrayList<>();

        Academico academico1 = new Academico();

        Academico academico2 = new Academico();

        esperada.add(academico1);
        obtenida.add(academico2);

        try {
            obtenida = ColaboracionDB.getAcademicosParticipantes(colaboracionPrueba);
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetAcademicosParticipantesExitoso " + error.getMessage());
        }

        assertEquals(esperada.size(), obtenida.size());
        for (Academico academico : esperada) {
            assertEquals(academico,obtenida.get(0));
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
        List<Colaboracion> listaColaboraciones = null;

        LocalDate fechaInicio = LocalDate.of(2024, 5, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);

        Periodo periodo = new Periodo();
        periodo.setFechaInicio(fechaInicio);
        periodo.setFechaFin(fechaFin);

        try {
            listaColaboraciones = ColaboracionDB.getColaboracionPorPeriodo(periodo);

        }
        catch (ErrorDAO error) {
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


        } catch (ErrorDAO error) {

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
        catch (ErrorDAO error) {
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
        catch (ErrorDAO error) {
            fail("pruebaAgregarAcademicoAColaboracionExitoso " + error.getMessage());

        }

        assertEquals(esperado, obtenido);

    }

    @Test
    void pruebaRegistrarColaboracionExitoso () {
        System.out.println("registrarColaboracion");
        Colaboracion colaboracionPrueba = instanciarColaboracion();
        int esperado = 1;
        int obtenido = -1;

        try {
            obtenido = ColaboracionDB.registrarColaboracion(colaboracionPrueba);

        }
        catch (ErrorDAO error) {
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
        catch (ErrorDAO error) {
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

        }
        catch (ErrorDAO errorDAO) {
            fail("pruebaGetTodosExitosa " + errorDAO.getMessage());

        }

        assertEquals(tamanoEsperado, listaColaboracion.size());

   }




}