package test.Logica;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

public class DAORetroalimentacionColaboracionTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacion();
        ConfiguracionPrueba.borrarDatosTablaRetroalimentacionColaboracion();
        
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
