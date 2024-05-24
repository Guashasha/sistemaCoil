package test.Logica;

import DAO.ActividadAuxiliar;
import DTO.ActividadDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ActividadAuxiliarTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaActividad();
        ActividadDTO actividadDTO = new ActividadDTO("titulo 1", "descripcion 1", ActividadDTO.TipoActividad.rompeHielo);
        ActividadDTO actividadDTO2 = new ActividadDTO("titulo 2", "descripcion 2", ActividadDTO.TipoActividad.cierre);

        ActividadAuxiliar act = new ActividadAuxiliar();
        act.agregar(actividadDTO);
        act.agregar(actividadDTO2);
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

        Optional<ActividadDTO> resultado = Optional.empty();
        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            resultado = act.getPorId(2);
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultado.isEmpty()) {
            fail();
        }

        assertEquals(actividadDTO, resultado.get());
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
    public void pruebaGetTodos () {
        ArrayList<ActividadDTO> actividades = new ArrayList<>();
        actividades.add(new ActividadDTO("titulo 1", "descripcion 1", ActividadDTO.TipoActividad.rompeHielo));
        actividades.add(new ActividadDTO("titulo 2", "descripcion 2", ActividadDTO.TipoActividad.cierre));
        actividades.add(new ActividadDTO("titulo 3", "descripcion 3", ActividadDTO.TipoActividad.rompeHielo));

        List<ActividadDTO> resultados = null;
        ActividadAuxiliar act = new ActividadAuxiliar();

        try {
            resultados = act.getTodos();
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultados == null) {
            fail();
        }

        assertEquals(actividades, resultados);
    }
}
