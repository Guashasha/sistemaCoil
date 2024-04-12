package test.Logica;

import Logica.DAO.DAOColaboracion;
import Logica.DAO.DAORetroalimentacionColaboracion;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;
import Logica.Dominio.RetroalimentacionColaboracion;
import Logica.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DAORetroalimentacionColaboracionTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        DAOColaboracion col = new DAOColaboracion();

        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setEstado(Colaboracion.EstadoColaboracion.enRevision);
        colaboracion.setIdioma("Español");
        colaboracion.setObjetivo("probar la clase retroalimentacion colaboracion");
        colaboracion.setPerfilEstudiante("ninguno xd");
        colaboracion.setPeriodo(new Periodo(LocalDate.of(2024, Month.FEBRUARY, 28), LocalDate.now()));
        colaboracion.setTemaInteres("tambien ninguno xd");
        colaboracion.setTipo(Colaboracion.TipoColaboracion.COIL);

        col.agregar(colaboracion);

        Colaboracion colaboracion2 = new Colaboracion();
        colaboracion.setEstado(Colaboracion.EstadoColaboracion.enRevision);
        colaboracion.setIdioma("Ingles");
        colaboracion.setObjetivo("segunda prueba de la clase retroalimentacion colaboracion");
        colaboracion.setPerfilEstudiante("haber pasado redes con 6 almenos");
        colaboracion.setPeriodo(new Periodo(LocalDate.of(2023, Month.FEBRUARY, 25), LocalDate.of(2024, Month.APRIL, 1)));
        colaboracion.setTemaInteres("ninguno xd");
        colaboracion.setTipo(Colaboracion.TipoColaboracion.claseEspejo);

        col.agregar(colaboracion2);

        Colaboracion colaboracion3 = new Colaboracion();
        colaboracion.setEstado(Colaboracion.EstadoColaboracion.propuesta);
        colaboracion.setIdioma("Ingles");
        colaboracion.setObjetivo("Tercera prueba de la clase retroalimentacion colaboracion");
        colaboracion.setPerfilEstudiante("que esté estudiando");
        colaboracion.setPeriodo(new Periodo(LocalDate.of(2023, Month.FEBRUARY, 25), LocalDate.of(2024, Month.APRIL, 1)));
        colaboracion.setTemaInteres("ninguno xd");
        colaboracion.setTipo(Colaboracion.TipoColaboracion.claseEspejo);

        col.agregar(colaboracion3);

        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();
        ret.agregar(retroalimentacion);
    }

    @Test
    public void testAgregar () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();
        int resultado = -1;

        try {
            resultado = ret.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(1, resultado);
    }

    @Test
    public void testAgregarIncorrecto () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(8);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(9);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        try {
            ret.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void testAgregarExistente () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        try {
            ret.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.DUPLICIDAD, error.getTipo());
        }
    }

    @Test
    public void testAgregarSinColaboracion () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(100);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        try {
            ret.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.CONSULTA, error.getTipo());
        }
    }

    @Test
    public void testAgregarConColaboracionSinConcluir () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(3);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        try {
            ret.agregar(retroalimentacion);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void testGetPorId () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraDelLenguaje(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        Optional<RetroalimentacionColaboracion> resultado = Optional.empty();

        try {
            resultado = ret.getPorId(1);
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultado.isPresent()) {
            assertEquals(retroalimentacion, resultado.get());
        }
        else {
            fail();
        }
    }

    @Test
    public void testGetPorIdInexistente () {
        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        Optional<RetroalimentacionColaboracion> resultado = Optional.empty();

        try {
            resultado = ret.getPorId(1000);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    public void testPorIdIncorrecta () {
        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        try {
            ret.getPorId(-1000);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }
}
