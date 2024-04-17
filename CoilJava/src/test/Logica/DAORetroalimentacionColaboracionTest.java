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
        colaboracion2.setEstado(Colaboracion.EstadoColaboracion.enRevision);
        colaboracion2.setIdioma("Ingles");
        colaboracion2.setObjetivo("segunda prueba de la clase retroalimentacion colaboracion");
        colaboracion2.setPerfilEstudiante("haber pasado redes con 6 almenos");
        colaboracion2.setPeriodo(new Periodo(LocalDate.of(2023, Month.FEBRUARY, 25), LocalDate.of(2024, Month.APRIL, 1)));
        colaboracion2.setTemaInteres("ninguno xd");
        colaboracion2.setTipo(Colaboracion.TipoColaboracion.claseEspejo);

        col.agregar(colaboracion2);

        Colaboracion colaboracion3 = new Colaboracion();
        colaboracion3.setEstado(Colaboracion.EstadoColaboracion.propuesta);
        colaboracion3.setIdioma("Ingles");
        colaboracion3.setObjetivo("Tercera prueba de la clase retroalimentacion colaboracion");
        colaboracion3.setPerfilEstudiante("que esté estudiando");
        colaboracion3.setPeriodo(new Periodo(LocalDate.of(2023, Month.FEBRUARY, 25), LocalDate.of(2024, Month.APRIL, 1)));
        colaboracion3.setTemaInteres("ninguno xd");
        colaboracion3.setTipo(Colaboracion.TipoColaboracion.claseEspejo);

        col.agregar(colaboracion3);

        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(3);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();
        ret.agregar(retroalimentacion);
    }

    @Test
    public void pruebaAgregar () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();
        int resultado = -1;

        try {
            resultado = ret.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(2, resultado);
    }

    @Test
    public void pruebaAgregarIncorrecto () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(8);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(9);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

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
    public void pruebaAgregarExistente () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

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
    public void pruebaAgregarSinColaboracion () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(100);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

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
    public void pruebaAgregarConColaboracionSinConcluir () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(3);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

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
    public void pruebaGetPorId () {
        RetroalimentacionColaboracion retroalimentacion = new RetroalimentacionColaboracion();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(3);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

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
    public void pruebaGetPorIdInexistente () {
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
    public void pruebaGetPorIdIncorrecta() {
        DAORetroalimentacionColaboracion ret = new DAORetroalimentacionColaboracion();

        try {
            ret.getPorId(-1000);
            fail();
        }
        catch (ErrorDAO error) {
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebagetTodos () {

    }
}
