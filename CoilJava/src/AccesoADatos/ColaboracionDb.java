package AccesoADatos;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.ErrorDAO;

import java.sql.SQLException;
import java.sql.preparedStatement;

public class ColaboracionDb {
    public static Colaboracion getColaboracionPorAcademicos(Academico academico1, Academico academico2) {
        ConexionBaseDatos conexion = new ConexionBaseDatos();

        Colaboracion colaboracion = null;

        try {
            conexion.getConexion().prepareStatement("select * from Colaboracion where ");
        } catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }

        return colaboracion;
    }
}
