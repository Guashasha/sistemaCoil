package AccesoADatos;

import Logica.Dominio.Region;
import Logica.ErrorDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDB {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public List<Region> getTodasAlfabeticamente() throws ErrorDAO {
        List<Region> listaRegiones = new ArrayList<>();
        String consultaRegionesSQL = "SELECT idRegion, nombre FROM region";
        PreparedStatement consultaRegiones;
        ResultSet resultadoConsulta;

        try {
            consultaRegiones = this.CONEXION_BASE_DATOS.getConexion()
                    .prepareStatement(consultaRegionesSQL);
            resultadoConsulta = consultaRegiones.executeQuery();

            while (resultadoConsulta.next()) {
                listaRegiones.add(convertirResultSetARegion(resultadoConsulta));
            }

            consultaRegiones.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        } catch (SQLException excepcionSQL) {
            throw new ErrorDAO("SQLException: Error al consultar Regiones\n" + excepcionSQL.getMessage());
        }

        return listaRegiones;
    }

    public Region convertirResultSetARegion (ResultSet resultado) throws SQLException {
        Region region = new Region();
        region.setId(resultado.getInt(1));
        region.setNombre(resultado.getString(2));
        return region;
    }
}
