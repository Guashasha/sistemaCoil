package test.DAO;

import DAO.RetroalimentacionColaboracionAuxiliar;
import DAO.RetroalimentacionColaboracionDAO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;
import java.util.Optional;

public class RetroalimentacionColaboracionDAOTest {
    private static final RetroalimentacionColaboracionDAO dao = new RetroalimentacionColaboracionDAO();

    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        AyudantePruebasColaboracionDB.agregarPrecondiciones();
        AyudantePruebasColaboracionDB.agregarColaboracionesParaRetroalimentacion();

        RetroalimentacionColaboracionAuxiliar rt = new RetroalimentacionColaboracionAuxiliar();
        rt.agregar(crearRetroalimentacion2());
    }

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
    }

    private static RetroalimentacionColaboracionDTO crearRetroalimentacion () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setColaboracion(5);
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(4);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(5);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        return retroalimentacion;
    }

    private static RetroalimentacionColaboracionDTO crearRetroalimentacion2 () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setIdUsuario(2);
        retroalimentacion.setColaboracion(6);
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(5);
        retroalimentacion.setMejoraDelLenguaje(5);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(4);

        return retroalimentacion;
    }

    @Test
    public void pruebaAgregarRetroalimentacion () {
        int filasAfectadasObtenidas = -1;

        try {
            filasAfectadasObtenidas = dao.agregar(crearRetroalimentacion());
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, filasAfectadasObtenidas);
    }

    @Test
    public void pruebaAgregarRetroalimentacionIncorrecta () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setIdUsuario(500);
        retroalimentacion.setColaboracion(4);
        retroalimentacion.setComentario("hola mundo");
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(5);
        retroalimentacion.setMejoraDelLenguaje(5);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(4);
        assertThrows(ErrorDAO.class,()->dao.agregar(retroalimentacion));
    }

    @Test
    public void pruebaGetPorId () {
        RetroalimentacionColaboracionDTO retroalimentacionEsperada = crearRetroalimentacion2();
        Optional<RetroalimentacionColaboracionDTO> retroalimentacionObtenida = Optional.empty();
        try {
            retroalimentacionObtenida = dao.getPorId(1);
        }
        catch (ErrorDAO e) {
            fail();
        }
        assertEquals(retroalimentacionEsperada, retroalimentacionObtenida.get());
    }

    @Test
    public void pruebaGetPorIdIncorrecto () {
        Optional<RetroalimentacionColaboracionDTO> resultadoConsulta = Optional.empty();

        try {
            resultadoConsulta = dao.getPorId(1000);
        }
        catch (ErrorDAO e) {
            fail();
        }

        assert (resultadoConsulta.isEmpty());
    }

    @Test
    public void pruebaGetPorPersonaYColaboracion () {
        RetroalimentacionColaboracionDTO retroalimentacionEsperada = crearRetroalimentacion();
        Optional<RetroalimentacionColaboracionDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYColaboracion(1, 5);
        }
        catch (ErrorDAO e) {
            fail();
        }

        RetroalimentacionColaboracionDTO retroalimentacion = retroalimentacionObtenida.get();

        assertEquals(retroalimentacionEsperada, retroalimentacion);
    }

    @Test
    public void pruebaPorPersonaSinColaboracion () {
        Optional<RetroalimentacionColaboracionDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYColaboracion(1, 3000);
        }
        catch (ErrorDAO e) {
            fail();
        }

        assert (retroalimentacionObtenida.isEmpty());
    }

    @Test
    public void pruebaPorColaboracionSinPersona () {
        Optional<RetroalimentacionColaboracionDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = dao.getPorPersonaYColaboracion(1000, 3);
        }
        catch (ErrorDAO e) {
            fail();
        }

        assert (retroalimentacionObtenida.isEmpty());
    }
}
