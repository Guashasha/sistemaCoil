package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.Region;
import Logica.ErrorDAO;
import Logica.Interfaces.IRegionDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DAORegion implements IRegionDAO {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    @Override
    public List<Region> getTodasAlfabeticamente() throws ErrorDAO {
        List<Region> listaRegiones;
        String consultaRegionesSQL = "SELECT nombre FROM region";
        PreparedStatement consultaRegiones;
        ResultSet resultadoConsulta;

        try {
            this.CONEXION_BASE_DATOS.conectar();
            consultaRegiones = this.CONEXION_BASE_DATOS.getConexion()
                    .prepareStatement(consultaRegionesSQL);
            resultadoConsulta = consultaRegiones.executeQuery();

            listaRegiones = convertirListaRegiones(resultadoConsulta);

            this.CONEXION_BASE_DATOS.desconectar();
            consultaRegiones.close();
            resultadoConsulta.close();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return listaRegiones;
    }

    public List<Region> convertirListaRegiones(ResultSet resultado) throws SQLException {
        List<Region> listaRegiones = null;
        Region region = new Region();

        while (resultado.next()) {
            region.setNombre(resultado.getString("nombre"));
            listaRegiones.add(region);
        }

        return listaRegiones;
    }

}
