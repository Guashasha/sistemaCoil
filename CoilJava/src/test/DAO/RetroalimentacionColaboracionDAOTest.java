package test.DAO;

import DAO.RetroalimentacionColaboracionAuxiliar;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
import test.ConfiguracionPrueba;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionColaboracionDAOTest {
    private static final RetroalimentacionColaboracionAuxiliar dao = new RetroalimentacionColaboracionAuxiliar();

    @BeforeAll
    public static void setUp() {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        AyudantePruebasColaboracionDB.agregarPrecondiciones();

        RetroalimentacionColaboracionAuxiliar rt = new RetroalimentacionColaboracionAuxiliar();
        rt.agregar(crearRetroalimentacion2());
    }

    private static RetroalimentacionColaboracionDTO crearRetroalimentacion () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setColaboracion(1);
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
        retroalimentacion.setColaboracion(2);
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
            dao.agregar(crearRetroalimentacion());
        }
        catch (ErrorDAO error) {
            fail(error.getMessage());
        }

        assertEquals(2, resultado);
  }

    @Test
    public void pruebaGetPorId () {
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorId(1);
        } catch (ErrorDAO e) {
            fail();
        }

        if (resultado.isEmpty()) {
            fail("no se encontró la retroalimentacion esperada");
        }

        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();

        assertEquals(2, retroalimentacion.getIdRetroalimentacion());
        assertEquals(1, retroalimentacion.getIdUsuario());
        assertEquals(1, retroalimentacion.getColaboracion());
        assertEquals(5, retroalimentacion.getInteraccionConPar());
        assertEquals(4, retroalimentacion.getIntercambioCultural());
        assertEquals(4, retroalimentacion.getCalificacion());
    }

  @Test
    public void pruebaGetPorPersonaYColaboracion () {
        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

        try {
            resultado = dao.getPorPersonaYColaboracion(1, 1);
        } catch (ErrorDAO e) {
            fail();
      }

      if (resultado.isEmpty()) {
          fail("no se encontró la retroalimentacion esperada");
      }

      RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();

      assertEquals(2, retroalimentacion.getIdRetroalimentacion());
      assertEquals(1, retroalimentacion.getIdUsuario());
      assertEquals(1, retroalimentacion.getColaboracion());
      assertEquals(5, retroalimentacion.getInteraccionConPar());
      assertEquals(4, retroalimentacion.getIntercambioCultural());
      assertEquals(4, retroalimentacion.getCalificacion());
    }

    @Test
    public void pruebaGetTodos () {
      List<RetroalimentacionColaboracionDTO> resultado = null;

        try {
            resultado = dao.getTodos();
        } catch (ErrorDAO e) {
            fail();
        }

        if (resultado == null) {
            fail();
        }

        assertEquals(2, resultado.get(0).getIdRetroalimentacion());
        assertEquals(1, resultado.get(0).getIdUsuario());
        assertEquals(1, resultado.get(0).getColaboracion());
        assertEquals(5, resultado.get(0).getInteraccionConPar());
        assertEquals(4, resultado.get(0).getIntercambioCultural());

        assertEquals(1, resultado.get(1).getIdRetroalimentacion());
        assertEquals(2, resultado.get(1).getIdUsuario());
        assertEquals(2, resultado.get(1).getColaboracion());
        assertEquals(5, resultado.get(1).getInteraccionConPar());
        assertEquals(5, resultado.get(1).getIntercambioCultural());
        assertEquals(4, resultado.get(1).getMejoraFormacionProfesional());
    }
}
