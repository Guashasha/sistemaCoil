package DAO;

import DTO.RegionDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO {
    

    public static List<RegionDTO> getTodasAlfabeticamente () throws SQLException {
        List<RegionDTO> listaRegiones = new ArrayList<>();
        String consultaRegionesSQL = "SELECT idRegion, nombre FROM region ORDER BY nombre ASC";
        PreparedStatement consultaRegiones = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaRegiones = AdministradorBaseDatos.getInstancia()
                    .prepareStatement(consultaRegionesSQL);
            resultadoConsulta = consultaRegiones.executeQuery();

            while (resultadoConsulta.next()) {
                listaRegiones.add(convertirResultSetARegion(resultadoConsulta));
            }
            consultaRegiones.close();
            resultadoConsulta.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaRegiones;
    }

    private static RegionDTO convertirResultSetARegion (ResultSet resultado) throws SQLException {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(resultado.getInt(1));
        regionDTO.setNombre(resultado.getString(2));
        return regionDTO;
    }
}
