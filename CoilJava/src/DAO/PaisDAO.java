package DAO;

import DAO.Interfaces.IPaisDAO;
import DTO.PaisDTO;
import AccesoDatos.AdministradorBaseDatos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaisDAO implements IPaisDAO {
    @Override
    public List<PaisDTO> getPaisesAlfabeticamente () throws SQLException {
        List<PaisDTO> listaPaises = new ArrayList<>();
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais ORDER BY nombre ASC";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        consultaPaises = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaPaisesSQL);
        resultadoConsulta = consultaPaises.executeQuery();

        while (resultadoConsulta.next()) {
            listaPaises.add(convertirResultSetAPais(resultadoConsulta));
        }
        consultaPaises.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return listaPaises;
    }

    @Override
    public Optional<PaisDTO> getPaisPorNombre (String nombre) throws SQLException {
        PaisDTO paisDTO = null;
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais WHERE nombre = ?";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

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

        return Optional.ofNullable(paisDTO);
    }

    @Override
    public Optional<PaisDTO> getPaisPorId (int id) throws SQLException {
        PaisDTO paisDTO = null;
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais WHERE idPais = ?";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

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

        return Optional.ofNullable(paisDTO);
    }

    private PaisDTO convertirResultSetAPais (ResultSet resultado) throws SQLException {
        PaisDTO paisDTO = new PaisDTO();

        paisDTO.setId(resultado.getInt(1));
        paisDTO.setIso(resultado.getString(2));
        paisDTO.setNombre(resultado.getString(3));

        return paisDTO;
    }

}
