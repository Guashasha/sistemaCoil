package Logica.DAO;

import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;
import Logica.ErrorDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DAOColaboracionTest {
    private final DAOColaboracion INSTANCIA = new DAOColaboracion();

    @BeforeEach
    void setUp () {
        AyudantePruebasColaboracionDB.agregarPrecondiciones();
    }

    @AfterEach
    void tearDown () {
        AyudantePruebasColaboracionDB.borrarTodosDatosTabla();
    }

    @Test
    void getColaboracionPorAcademicosParticipantes () {
    }

    @Test
    void getColaboracionPorId () {
    }

    @Test
    void getListaDeEstudiantes () {
    }

    @Test
    void getAcademicosParticipantes () {
    }

    @Test
    void getColaboracionPorPeriodo () {
    }

    @Test
    void getColaboracionPorIdioma () {
    }

    @Test
    void getColaboracionPorEstado () {
    }

    @Test
    void cambiarEstadoColaboracion () {
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