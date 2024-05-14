package AccesoADatos;

import Logica.Dominio.Region;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDB {
    

    public static List<Region> getTodasAlfabeticamente () throws SQLException {
        List<Region> listaRegiones = new ArrayList<>();
        String consultaRegionesSQL = "SELECT idRegion, nombre FROM region ORDER BY nombre ASC";
        PreparedStatement consultaRegiones = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaRegiones = ConexionBaseDatos.getInstancia()
                    .prepareStatement(consultaRegionesSQL);
            resultadoConsulta = consultaRegiones.executeQuery();

            while (resultadoConsulta.next()) {
                listaRegiones.add(convertirResultSetARegion(resultadoConsulta));
            }
            consultaRegiones.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaRegiones;
    }

    private static Region convertirResultSetARegion (ResultSet resultado) throws SQLException {
        Region region = new Region();
        region.setId(resultado.getInt(1));
        region.setNombre(resultado.getString(2));
        return region;
    }
}
