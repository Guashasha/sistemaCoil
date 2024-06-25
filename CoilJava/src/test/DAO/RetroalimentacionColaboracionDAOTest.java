package test.DAO;

import DAO.RetroalimentacionColaboracionAuxiliar;
import DAO.RetroalimentacionColaboracionDAO;
import DTO.RetroalimentacionActividadDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionColaboracionDAOTest {
    private static final RetroalimentacionColaboracionDAO dao = new RetroalimentacionColaboracionDAO();

    @BeforeAll
    public static void setUp() {
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
        retroalimentacion.setColaboracion(3);
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
        retroalimentacion.setColaboracion(4);
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
        int resultado = -1;

        try {
            resultado = dao.agregar(crearRetroalimentacion());
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
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

        try {
            dao.agregar(retroalimentacion);
            fail();
        } catch (ErrorDAO e) {
            assertEquals(ErrorDAO.Tipo.CONEXION, e.getTipo());
        }
    }

    @Test
    public void pruebaGetPorId () {
        RetroalimentacionColaboracionDTO esperado = crearRetroalimentacion2();
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorId(1);
        } catch (ErrorDAO e) {
            fail();
        }

        if (resultado.isEmpty()) {
            fail("no se encontró la retroalimentacion esperada");
        }

        RetroalimentacionColaboracionDTO retroalimentacion = resultado.get();

        assertEquals(esperado, retroalimentacion);
    }

    @Test
    public void pruebaGetPorIdIncorrecto () {
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorId(1000);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isEmpty());
    }

  @Test
    public void pruebaGetPorPersonaYColaboracion () {
        RetroalimentacionColaboracionDTO esperado = crearRetroalimentacion();
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYColaboracion(1, 3);
        } catch (ErrorDAO e) {
            fail();
      }

      if (resultado.isEmpty()) {
          fail("no se encontró la retroalimentacion esperada");
      }

      RetroalimentacionColaboracionDTO retroalimentacion = resultado.get();

      assertEquals(esperado, retroalimentacion);
    }

    @Test
    public void pruebaPorPersonaSinColaboracion () {
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYColaboracion(1, 3000);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isEmpty());
    }

    @Test
    public void pruebaPorColaboracionSinPersona () {
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYColaboracion(1000, 3);
        } catch (ErrorDAO e) {
            fail();
        }

        assert(resultado.isEmpty());
    }
}
