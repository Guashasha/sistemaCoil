package test.DAO;

import DAO.ActividadAuxiliar;
import DAO.CronogramaActividadAuxiliar;
import DTO.ActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ActividadAuxiliarTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaCalendarioActividades();
        ConfiguracionPrueba.borrarDatosTablaActividad();
        AyudantePruebasColaboracionDB.vincularActividadConColaboracion();
        ActividadDTO actividadDTO = new ActividadDTO("titulo 1", "descripcion 1", ActividadDTO.TipoActividad.rompeHielo);
        ActividadDTO actividadDTO2 = new ActividadDTO("titulo 2", "descripcion 2", ActividadDTO.TipoActividad.cierre);

        ActividadAuxiliar act = new ActividadAuxiliar();
        act.agregar(actividadDTO);
        act.agregar(actividadDTO2);
    }

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
    }

    @Test
    public void pruebaAgregar () {
        ActividadDTO actividadDTO = new ActividadDTO("titulo 3", "descripcion 3", ActividadDTO.TipoActividad.rompeHielo);

        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            assertEquals(1, act.agregar(actividadDTO));
        } catch (ErrorDAO error) {
            fail();
        }
    }

    @Test
    public void pruebaAgregarInvalido () {
        ActividadDTO actividadDTO = new ActividadDTO("", "hola", ActividadDTO.TipoActividad.disciplinar);

        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            act.agregar(actividadDTO);
            fail();
        } catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebaGetPorId () {
        ActividadDTO actividadDTO = new ActividadDTO("titulo 2", "descripcion 2", ActividadDTO.TipoActividad.cierre);

        Optional<ActividadDTO> actividadObtenida = Optional.empty();
        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            actividadObtenida = act.getPorId(4);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(actividadDTO, actividadObtenida.get());
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        ActividadAuxiliar act = new ActividadAuxiliar();

        Optional<ActividadDTO> resultado = Optional.empty();

        try {
            resultado = act.getPorId(500);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    public void pruebaGetPorTitulo () {
        ActividadDTO actividadDTO = new ActividadDTO("titulo 1", "descripcion 1", ActividadDTO.TipoActividad.rompeHielo);

        ActividadAuxiliar act = new ActividadAuxiliar();
        Optional<ActividadDTO> resultado = Optional.empty();

        try {
            resultado = act.getPorTitulo("titulo 1");
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultado.isPresent()) {
            assertEquals(actividadDTO, resultado.get());
        }
        else {
            fail();
        }
    }

    @Test
    public void pruebaGetPorTituloInexistente () {
        ActividadAuxiliar act = new ActividadAuxiliar();
        Optional<ActividadDTO> resultado = Optional.empty();

        try {
            resultado = act.getPorTitulo("titulo 391");
        }
        catch (ErrorDAO error) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    public void pruebaGetPorColaboracion () {
        final ActividadDTO ACTIVIDAD1 = new ActividadDTO(1,"kahoot prueba", "descripcion de la actividad prueba", ActividadDTO.TipoActividad.cierre);
        final ActividadDTO ACTIVIDAD2 = new ActividadDTO(2,"Presentacion","presentacion individual ante grupo", ActividadDTO.TipoActividad.rompeHielo);
        ArrayList<ActividadDTO> esperado = new ArrayList<>();
        esperado.add(ACTIVIDAD1);
        esperado.add(ACTIVIDAD2);

        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            assertEquals(esperado, act.getPorIdColaboracion(1));
        } catch (ErrorDAO e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void pruebaGetPorColaboracionInexistente () {
        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            assert(act.getPorIdColaboracion(1000).isEmpty());
        } catch (ErrorDAO e) {
            fail(e.getMessage());
        }
    }
}
