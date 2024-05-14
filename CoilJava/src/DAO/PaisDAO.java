package DAO;

import DTO.PaisDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaisDAO {
    public static List<PaisDTO> paisesAlfabeticamente () throws SQLException {
        List<PaisDTO> listaPaises = new ArrayList<>();
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais ORDER BY nombre ASC";
        PreparedStatement consultaPaises = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaPaises = AdministradorBaseDatos.getInstancia().
                    prepareStatement(consultaPaisesSQL);
            resultadoConsulta = consultaPaises.executeQuery();

            while (resultadoConsulta.next()) {
                listaPaises.add(convertirResultSetAPais(resultadoConsulta));
            }
            consultaPaises.close();
            resultadoConsulta.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaPaises;
    }

    public static PaisDTO getPaisPorNombre (String nombre) throws SQLException {
        PaisDTO paisDTO = new PaisDTO(0);
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais WHERE nombre = ?";
        PreparedStatement consultaPaises = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaPaises = AdministradorBaseDatos.getInstancia().
                    prepareStatement(consultaPaisesSQL);
            consultaPaises.setString(1,nombre);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                paisDTO = convertirResultSetAPais(resultadoConsulta);
            }
            consultaPaises.close();
            resultadoConsulta.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return paisDTO;
    }

    public static PaisDTO getPaisPorId (int id) throws SQLException {
        PaisDTO paisDTO = new PaisDTO(0);
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais WHERE idPais = ?";
        PreparedStatement consultaPaises = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaPaises = AdministradorBaseDatos.getInstancia().
                    prepareStatement(consultaPaisesSQL);
            consultaPaises.setInt(1,id);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                paisDTO = convertirResultSetAPais(resultadoConsulta);
            }
            consultaPaises.close();
            resultadoConsulta.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return paisDTO;
    }

    private static PaisDTO convertirResultSetAPais (ResultSet resultado) throws SQLException {
        PaisDTO paisDTO = new PaisDTO();

        paisDTO.setId(resultado.getInt(1));
        paisDTO.setIso(resultado.getString(2));
        paisDTO.setNombre(resultado.getString(3));

        return paisDTO;
    }

}
