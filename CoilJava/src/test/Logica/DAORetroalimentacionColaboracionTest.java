package test.Logica;

import Logica.DAO.DAOColaboracion;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

public class DAORetroalimentacionColaboracionTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();

        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setEstado(Colaboracion.EstadoColaboracion.en_revision);
        colaboracion.setIdioma("Español");
        colaboracion.setObjetivo("probar la clase retroalimentacion colaboracion");
        colaboracion.setPerfilEstudiante("ninguno xd");
        colaboracion.setPeriodo(new Periodo());
        colaboracion.setTemaInteres("tambien ninguno xd");
        colaboracion.setTipo(Colaboracion.TipoColaboracion.COIL);

        DAOColaboracion col = new DAOColaboracion();
        col.agregar(colaboracion);
    }

    @Test
    public void testAgregar () {
        // TODO
    }

    @Test public void testAgregarIncorrecto () {
        // TODO
    }

    @Test
    public void testGetPorId () {
        // TODO
    }

    @Test
    public void testGetPorIdInexistente () {
        // TODO
    }

        @Test
        public void testPorIdIncorrecta () {
        // TODO
    }
}
