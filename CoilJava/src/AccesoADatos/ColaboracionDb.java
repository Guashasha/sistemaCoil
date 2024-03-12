package AccesoADatos;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.ErrorDAO;
import java.sql.SQLException;

public class ColaboracionDb {
    public static Colaboracion getColaboracionPorAcademicos(Academico academico1, Academico academico2) throws ErrorDAO {
        ConexionBaseDatos conexion = new ConexionBaseDatos();

        Colaboracion colaboracion = null;

        // --------------- TODO ------------------
        try {
            conexion.getConexion().prepareStatement("select * from Colaboracion where ");
        } catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }

        return colaboracion;
    }

    public static Colaboracion getColaboracionPorId(int idColaboracion) throws ErrorDAO {
        // TODO ---------------------------
    }
}
