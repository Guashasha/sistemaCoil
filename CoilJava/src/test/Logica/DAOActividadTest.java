package test.Logica;

import AccesoADatos.ActividadDB;
import Logica.DAO.DAOActividad;
import Logica.Dominio.Actividad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DAOActividadTest {
    @BeforeAll
    public static void setUp () {
        ConfiguracionPrueba.borrarDatosTablaActividad();
        Actividad actividad = new Actividad("titulo 1", "descripcion 1", Actividad.TipoActividad.rompeHielo);
        Actividad actividad2 = new Actividad("titulo 2", "descripcion 2", Actividad.TipoActividad.cierre);

        DAOActividad act = new DAOActividad();
        act.agregar(actividad);
        act.agregar(actividad2);
    }

    @Test
    public void testAgregar () {
        Actividad actividad = new Actividad("titulo 3", "descripcion 3", Actividad.TipoActividad.rompeHielo);

        DAOActividad act = new DAOActividad();

        try {
            assertEquals(1, ActividadDB.agregarActividad(actividad));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
