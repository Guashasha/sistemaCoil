package test.DAO;

import DAO.ActividadAuxiliar;
import DTO.ActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.*;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ActividadDTODAOTest {
    private final ActividadDTO ACTIVIDAD1 = new ActividadDTO(1,"kahoot prueba", "descripcion de la actividad prueba", ActividadDTO.TipoActividad.cierre);
    private final ActividadDTO ACTIVIDAD2 = new ActividadDTO(2,"Presentacion","presentacion individual ante grupo", ActividadDTO.TipoActividad.rompeHielo);

    private final ActividadAuxiliar dao = new ActividadAuxiliar();

    @BeforeAll
    static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaCalendarioActividades();
        ConfiguracionPrueba.borrarDatosTablaActividad();
        AyudantePruebasColaboracionDB.agregarActividades();
    }

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarTodosLosDatosTabla();
    }

    @Test
    void pruebaAgregarActividadExitosa () {
        try {
            int resultado = dao.agregar(ACTIVIDAD1);
            assertEquals(1,resultado,"pruebaAgregarActividadExitosa");
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaAgregarActividadExitosa");
        }
    }

    @Test
    void pruebaAgregarActividadVacia () {
        assertThrows(NullPointerException.class,() -> dao.agregar(new ActividadDTO()));
    }

    @Test
    public void pruebaGetPorIdExitosa () {
        Optional<ActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorId(1);
        } catch (ErrorDAO e) {
            fail("Fallida: pruebaGetPorIdExitosa");
        }

        if (resultado.isEmpty()) {
            fail("No existe la actividad que se esperaba");
        }

        ActividadDTO actividad = resultado.get();
        assertEquals(ACTIVIDAD1.getTitulo(), actividad.getTitulo());
        assertEquals("descripcion de la actividad prueba", actividad.getDescripcion());
        assertEquals("cierre", actividad.getTipo().toString());
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        try {
            Optional<ActividadDTO> resultado = dao.getPorId(10);
            assert(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPorIdInexistente");
        }
    }

    @Test
    public void pruebaGetPorTituloExitosa () {
        Optional<ActividadDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorTitulo(ACTIVIDAD1.getTitulo());
        } catch (ErrorDAO e) {
            fail("Fallida: pruebaGetPorTituloExitosa");
        }

        if (resultado.isEmpty()) {
            fail("No existe la actividad que se esperaba");
        }

        ActividadDTO actividad = resultado.get();

        assertEquals(ACTIVIDAD1.getIdActividad(), actividad.getIdActividad());
        assertEquals(ACTIVIDAD1.getTitulo(), actividad.getTitulo());
        assertEquals(ACTIVIDAD1.getDescripcion(), actividad.getDescripcion());
        assertEquals(ACTIVIDAD1.getTipo().toString(), actividad.getTipo().toString());
    }

    @Test
    public void pruebaGetPorTituloInexistente () {
        try {
            Optional<ActividadDTO> resultado = dao.getPorTitulo("ActividadDTO cuatro");
            assert(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPorTituloInexistente");
        }
    }

    @Test
    public void pruebaGetPorTituloNulo () {
        try {
            Optional<ActividadDTO> resultado = dao.getPorTitulo(null);
            assert(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPorTituloNulo");
        }
    }
    
    @Test
    void pruebaGetPorIdColaboracionExitosa () {
        List<ActividadDTO> resultado = null;
        List<ActividadDTO> esperado = new ArrayList<>();
        esperado.add(ACTIVIDAD1);
        esperado.add(ACTIVIDAD2);

        try {
            AyudantePruebasColaboracionDB.vincularActividadConColaboracion();
            resultado = dao.getPorIdColaboracion(1);
        } catch (ErrorDAO e) {
            fail("Fallida: pruebaGetPorIdColaboracionExitosa");
        }

        if (resultado.isEmpty()) {
            fail("No se encontraron las actividades esperadas");
        }

        assertEquals(esperado, resultado);
    }

    @Test
    void pruebaGetPorIdColaboracionInexistente () {
        try {
            List<ActividadDTO> resultado = dao.getPorIdColaboracion(10);
            assert(resultado.isEmpty());
        }
        catch (ErrorDAO error) {
            fail("Fallida: pruebaGetPorIdColaboracionInexistente");
        }
    }
}
