package DAO;

import DAO.Interfaces.IUniversidadDAO;
import DTO.UniversidadDTO;
import AccesoDatos.AdministradorBaseDatos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversidadDAO implements IUniversidadDAO {
    @Override
    public int registrarUniversidad (UniversidadDTO universidad) throws SQLException {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad;

        insertarUniversidad = AdministradorBaseDatos.getInstancia().
                prepareStatement(insertarUniversidadSQL);
        insertarUniversidad.setString(1, universidad.getNombre());
        insertarUniversidad.setInt(2, universidad.getIdPais());
        filasAfectadas = insertarUniversidad.executeUpdate();

        insertarUniversidad.close();
        AdministradorBaseDatos.desconectar();

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad (UniversidadDTO universidad) throws SQLException {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad;

        actualizarUniversidad = AdministradorBaseDatos.getInstancia().
                prepareStatement(actualizarUniversidadSQL);
        actualizarUniversidad.setString(1, universidad.getNombre());
        actualizarUniversidad.setInt(2, universidad.getIdPais());
        actualizarUniversidad.setInt(3, universidad.getId());
        filasAfectadas = actualizarUniversidad.executeUpdate();

        actualizarUniversidad.close();
        AdministradorBaseDatos.desconectar();

        return filasAfectadas;
    }

    @Override
    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws SQLException {
        UniversidadDTO universidadDTO = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        consultaUniversidad = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaUniversidadSQL);
        consultaUniversidad.setString(1, nombre);
        resultadoConsulta = consultaUniversidad.executeQuery();

        if (resultadoConsulta.next()) {
            universidadDTO = convertirResultSetAUniversidad(resultadoConsulta);
        }
        consultaUniversidad.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return Optional.ofNullable(universidadDTO);
    }

    @Override
    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais WHERE pais = ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        consultaUniversidades = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultarUniversidadesSQL);
        consultaUniversidades.setString(1, paisOrigen);
        resultadoConsulta = consultaUniversidades.executeQuery();

        while (resultadoConsulta.next()) {
            listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
        }
        consultaUniversidades.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return listaUniversidades;
    }

    @Override
    public List<UniversidadDTO> getUniversidadesPorNombre (String nombre) throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad WHERE nombre LIKE ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        consultaUniversidades = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultarUniversidadesSQL);
        consultaUniversidades.setString(1, "%" + nombre + "%");
        resultadoConsulta = consultaUniversidades.executeQuery();

        while (resultadoConsulta.next()) {
            listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
        }
        consultaUniversidades.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return listaUniversidades;
    }

    @Override
    public List<UniversidadDTO> getTodasAlfabeticamente () throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais ORDER BY universidad ASC";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        consultaUniversidades = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultarUniversidadesSQL);
        resultadoConsulta = consultaUniversidades.executeQuery();

        while (resultadoConsulta.next()) {
            listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
        }
        consultaUniversidades.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return listaUniversidades;
    }

    @Override
    public Optional<UniversidadDTO> getUniversidadPorNombreYPais (String nombre, String pais) throws SQLException {
        UniversidadDTO universidadDTO = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, universidad, idPais FROM universidad_con_pais WHERE universidad = ? AND pais = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        consultaUniversidad = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaUniversidadSQL);
        consultaUniversidad.setString(1, nombre);
        consultaUniversidad.setString(2, pais);
        resultadoConsulta = consultaUniversidad.executeQuery();

        if (resultadoConsulta.next()) {
            universidadDTO = convertirResultSetAUniversidad(resultadoConsulta);
        }
        consultaUniversidad.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return Optional.ofNullable(universidadDTO);
    }

    @Override
    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws SQLException {
        UniversidadDTO universidadDTO = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE idUniversidad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        consultaUniversidad = AdministradorBaseDatos.getInstancia().
                prepareStatement(consultaUniversidadSQL);
        consultaUniversidad.setInt(1, id);
        resultadoConsulta = consultaUniversidad.executeQuery();

        if (resultadoConsulta.next()) {
            universidadDTO = convertirResultSetAUniversidad(resultadoConsulta);
        }
        consultaUniversidad.close();
        resultadoConsulta.close();
        AdministradorBaseDatos.desconectar();

        return Optional.ofNullable(universidadDTO);
    }

    private UniversidadDTO convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        UniversidadDTO universidadDTO = new UniversidadDTO();

        universidadDTO.setId(resultado.getInt(1));
        universidadDTO.setNombre(resultado.getString(2));
        universidadDTO.setIdPais(resultado.getInt(3));

        return universidadDTO;
    }
}
