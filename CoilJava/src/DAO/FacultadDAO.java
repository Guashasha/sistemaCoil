package DAO;

import DAO.Interfaces.IFacultadDAO;
import DTO.FacultadDTO;
import AccesoDatos.AdministradorBaseDatos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultadDAO implements IFacultadDAO {
    @Override
    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws SQLException {
        FacultadDTO facultadDTO = null;
        String consultaUniversidadSQL = "SELECT * FROM facultad_con_region WHERE facultad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        consultaUniversidad = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaUniversidadSQL);
        consultaUniversidad.setString(1,nombre);
        resultadoConsulta = consultaUniversidad.executeQuery();

        if (resultadoConsulta.next()) {
            facultadDTO = convertirResultSetAFacultad(resultadoConsulta);
        }
        consultaUniversidad.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return Optional.ofNullable(facultadDTO);
    }

    @Override
    public List<FacultadDTO> getFacultadPorRegion (String region) throws SQLException {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region WHERE region = ?";
        PreparedStatement consultaFacultades;
        ResultSet resultadoConsulta;

        consultaFacultades = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaFacultadesSQL);
        consultaFacultades.setString(1, region);
        resultadoConsulta = consultaFacultades.executeQuery();

        while (resultadoConsulta.next()) {
            listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
        }
        consultaFacultades.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return listaFacultades;
    }

    @Override
    public List<FacultadDTO> getTodasAlfabeticamente () throws SQLException {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region ORDER BY facultad ASC";
        PreparedStatement consultaFacultades;
        ResultSet resultadoConsulta;

        consultaFacultades = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaFacultadesSQL);
        resultadoConsulta = consultaFacultades.executeQuery();

        while (resultadoConsulta.next()) {
            listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
        }
        consultaFacultades.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return listaFacultades;
    }

    private static FacultadDTO convertirResultSetAFacultad (ResultSet resultado) throws SQLException {
        FacultadDTO facultadDTO = new FacultadDTO();

        facultadDTO.setId(resultado.getInt(1));
        facultadDTO.setNombre(resultado.getString(2));
        facultadDTO.setIdRegion(resultado.getInt(3));

        return facultadDTO;
    }
}
