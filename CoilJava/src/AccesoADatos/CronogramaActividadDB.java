package AccesoADatos;

import Logica.Dominio.Actividad;
import Logica.Dominio.ActividadVinculada;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;

import java.sql.SQLException;
import java.util.List;

public class CronogramaActividadDB {
    public static int agregar (Actividad actividad, Colaboracion colaboracion, Periodo periodo) throws SQLException {
        return -1;
    }

    public static List<ActividadVinculada> getTodos () throws SQLException {
        return null;
    }
}
