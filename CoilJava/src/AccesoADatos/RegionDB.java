package AccesoADatos;

import Logica.Dominio.Region;
import Logica.ErrorDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDB {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static List<Region> getTodasAlfabeticamente () throws SQLException {
        List<Region> listaRegiones = new ArrayList<>();
        String consultaRegionesSQL = "SELECT idRegion, nombre FROM region ORDER BY nombre ASC";
        PreparedStatement consultaRegiones = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaRegiones = CONEXION_BASE_DATOS.getConexion()
                    .prepareStatement(consultaRegionesSQL);
            resultadoConsulta = consultaRegiones.executeQuery();

            while (resultadoConsulta.next()) {
                listaRegiones.add(convertirResultSetARegion(resultadoConsulta));
            }
        } catch (SQLException error) {
            throw error;
        }
        finally {
            consultaRegiones.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaRegiones;
    }

    public static Region convertirResultSetARegion (ResultSet resultado) throws SQLException {
        Region region = new Region();
        region.setId(resultado.getInt(1));
        region.setNombre(resultado.getString(2));
        return region;
    }
}
