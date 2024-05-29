package test.DAO;

import DAO.ColaboracionAuxiliar;
import DAO.RetroalimentacionColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RetroalimentacionColaboracionAuxiliarTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        ColaboracionAuxiliar col = new ColaboracionAuxiliar();

        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.enRevision);
        colaboracionDTO.setIdioma("Español");
        colaboracionDTO.setObjetivo("probar la clase retroalimentacion colaboracionDTO");
        colaboracionDTO.setPerfilEstudiante("ninguno xd");
        colaboracionDTO.setPeriodo(new PeriodoDTO(LocalDate.of(2024, Month.FEBRUARY, 28), LocalDate.now()));
        colaboracionDTO.setTemaInteres("tambien ninguno xd");
        colaboracionDTO.setTipo(ColaboracionDTO.TipoColaboracion.COIL);

        col.agregar(colaboracionDTO);

        ColaboracionDTO colaboracionDTO2 = new ColaboracionDTO();
        colaboracionDTO2.setEstado(ColaboracionDTO.EstadoColaboracion.enRevision);
        colaboracionDTO2.setIdioma("Ingles");
        colaboracionDTO2.setObjetivo("segunda prueba de la clase retroalimentacion colaboracionDTO");
        colaboracionDTO2.setPerfilEstudiante("haber pasado redes con 6 almenos");
        colaboracionDTO2.setPeriodo(new PeriodoDTO(LocalDate.of(2023, Month.FEBRUARY, 25), LocalDate.of(2024, Month.APRIL, 1)));
        colaboracionDTO2.setTemaInteres("ninguno xd");
        colaboracionDTO2.setTipo(ColaboracionDTO.TipoColaboracion.claseEspejo);

        col.agregar(colaboracionDTO2);

        ColaboracionDTO colaboracionDTO3 = new ColaboracionDTO();
        colaboracionDTO3.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
        colaboracionDTO3.setIdioma("Ingles");
        colaboracionDTO3.setObjetivo("Tercera prueba de la clase retroalimentacion colaboracionDTO");
        colaboracionDTO3.setPerfilEstudiante("que esté estudiando");
        colaboracionDTO3.setPeriodo(new PeriodoDTO(LocalDate.of(2023, Month.FEBRUARY, 25), LocalDate.of(2024, Month.APRIL, 1)));
        colaboracionDTO3.setTemaInteres("ninguno xd");
        colaboracionDTO3.setTipo(ColaboracionDTO.TipoColaboracion.claseEspejo);

        col.agregar(colaboracionDTO3);

        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(3);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();
        ret.agregar(retroalimentacion);
    }

    @Test
    public void pruebaAgregar () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();
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
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(8);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(9);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

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
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

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
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(100);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

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
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(3);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

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
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(3);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

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
        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

        Optional<RetroalimentacionColaboracionDTO> resultado = Optional.empty();

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
        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

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
        ArrayList<RetroalimentacionColaboracionDTO> retroalimentaciones = new ArrayList<>();

        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(1);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(3);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);
        retroalimentaciones.add(retroalimentacion);

        retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(2);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);
        retroalimentaciones.add(retroalimentacion);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();
        List<RetroalimentacionColaboracionDTO> resultado= null;

        try {
            resultado = ret.getTodos();
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (resultado == null) {
            fail();
        }

        assertEquals(retroalimentaciones, resultado);
    }
}
