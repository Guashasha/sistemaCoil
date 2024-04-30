package test.Logica;

import Logica.DAO.DAOActividad;
import Logica.Dominio.Actividad;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DAOActividadTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionActividad();
        ConfiguracionPrueba.borrarDatosTablaActividad();
        Actividad actividad = new Actividad("titulo 1", "descripcion 1", Actividad.TipoActividad.rompeHielo);
        Actividad actividad2 = new Actividad("titulo 2", "descripcion 2", Actividad.TipoActividad.cierre);

        DAOActividad act = new DAOActividad();
        act.agregar(actividad);
        act.agregar(actividad2);
    }

    @Test
    public void pruebaAgregar () {
        Actividad actividad = new Actividad("titulo 3", "descripcion 3", Actividad.TipoActividad.rompeHielo);

        DAOActividad act = new DAOActividad();

        try {
            assertEquals(1, act.agregar(actividad));
        } catch (ErrorDAO error) {
            fail();
        }
    }

    @Test
    public void pruebaAgregarInvalido () {
        Actividad actividad = new Actividad("", "hola", Actividad.TipoActividad.disciplinar);

        DAOActividad act = new DAOActividad();

        try {
            act.agregar(actividad);
            fail();
        } catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebaGetPorId () {
        Actividad actividad = new Actividad("titulo 2", "descripcion 2", Actividad.TipoActividad.cierre);

        Optional<Actividad> resultado = null;

        DAOActividad act = new DAOActividad();

        try {
            resultado = act.getPorId(2);
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultado.isPresent()) {
            assertEquals(actividad, resultado.get());
        }
        else {
            fail();
        }
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        DAOActividad act = new DAOActividad();

        Optional<Actividad> resultado = null;

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
        Actividad actividad = new Actividad("titulo 1", "descripcion 1", Actividad.TipoActividad.rompeHielo);

        DAOActividad act = new DAOActividad();
        Optional<Actividad> resultado = Optional.empty();

        try {
            resultado = act.getPorTitulo("titulo 1");
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultado.isPresent()) {
            assertEquals(actividad, resultado.get());
        }
        else {
            fail();
        }
    }

    @Test
    public void pruebaGetPorTituloInexistente () {
        DAOActividad act = new DAOActividad();
        Optional<Actividad> resultado = Optional.empty();

        try {
            resultado = act.getPorTitulo("titulo 391");
        }
        catch (ErrorDAO error) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    public void pruebaModificar () {

    }

    @Test
    public void pruebaGetTodos () {

    }
}
