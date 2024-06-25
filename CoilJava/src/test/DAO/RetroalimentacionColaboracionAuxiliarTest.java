package test.DAO;

import DAO.ColaboracionAuxiliar;
import DAO.RetroalimentacionColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.AyudantePruebasColaboracionDB;
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
        ConfiguracionPrueba.borrarDatosTablaAcademicoDesarrolla();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        AyudantePruebasColaboracionDB.agregarPrecondiciones();
        AyudantePruebasColaboracionDB.agregarColaboracionesParaRetroalimentacion();

        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(3);
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

    @AfterAll
    static void limpiarBase () {
        ConfiguracionPrueba.borrarDatosTodasLasTablas();
    }

    @Test
    public void pruebaAgregar () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(4);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(4);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();
        int resultadoConsulta = -1;

        try {
            resultadoConsulta = ret.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assertEquals(2, resultadoConsulta);
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
            assertEquals(ErrorDAO.Tipo.VALIDACION, error.getTipo());
        }
    }

    @Test
    public void pruebaGetPorId () {
        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(3);
        retroalimentacion.setIdUsuario(1);
        retroalimentacion.setInteraccionConPar(5);
        retroalimentacion.setHabilidadesObtenidas(5);
        retroalimentacion.setCalificacion(5);
        retroalimentacion.setIntercambioCultural(4);
        retroalimentacion.setMejoraDelLenguaje(3);
        retroalimentacion.setTrabajoColaborativo(5);
        retroalimentacion.setMejoraFormacionProfesional(5);

        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

        Optional<RetroalimentacionColaboracionDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = ret.getPorId(1);
        }
        catch (ErrorDAO error) {
            fail();
        }

        if (retroalimentacionObtenida.isPresent()) {
            assertEquals(retroalimentacion, retroalimentacionObtenida.get());
        }
        else {
            fail();
        }
    }

    @Test
    public void pruebaGetPorIdInexistente () {
        RetroalimentacionColaboracionAuxiliar ret = new RetroalimentacionColaboracionAuxiliar();

        Optional<RetroalimentacionColaboracionDTO> retroalimentacionObtenida = Optional.empty();

        try {
            retroalimentacionObtenida = ret.getPorId(1000);
        }
        catch (ErrorDAO error) {
            fail();
        }

        assert(retroalimentacionObtenida.isEmpty());
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
}
