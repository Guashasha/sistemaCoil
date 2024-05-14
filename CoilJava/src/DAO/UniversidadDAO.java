package DAO;

import DTO.UniversidadDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniversidadDAO {
    

    public static int registrarUniversidad (UniversidadDTO universidadDTO) throws SQLException {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad;

        try {
            insertarUniversidad = AdministradorBaseDatos.getInstancia().
                                                     prepareStatement(insertarUniversidadSQL);
            insertarUniversidad.setString(1, universidadDTO.getNombre());
            insertarUniversidad.setInt(2, universidadDTO.getIdPais());
            filasAfectadas = insertarUniversidad.executeUpdate();

            insertarUniversidad.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return filasAfectadas;
    }

    public static int editarUniversidad (UniversidadDTO universidadDTO) throws SQLException {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad = null;

        try {
            actualizarUniversidad = AdministradorBaseDatos.getInstancia().
                                                       prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1, universidadDTO.getNombre());
            actualizarUniversidad.setInt(2, universidadDTO.getIdPais());
            actualizarUniversidad.setInt(3, universidadDTO.getId());
            filasAfectadas = actualizarUniversidad.executeUpdate();

            actualizarUniversidad.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return filasAfectadas;
    }

    public static UniversidadDTO getUniversidadPorNombre (String nombre) throws SQLException {
        UniversidadDTO universidadDTO = new UniversidadDTO(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
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
        }
        catch (SQLException error) {
            throw error;
        }

        return universidadDTO;
    }

    public static List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais WHERE pais = ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
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
        }
        catch (SQLException error) {
            throw error;
        }

        return listaUniversidades;
    }

    public static List<UniversidadDTO> getUniversidadesPorNombre (String nombre) throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad WHERE nombre LIKE ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
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
        }
        catch (SQLException error) {
            throw error;
        }

        return listaUniversidades;
    }

    public static List<UniversidadDTO> getTodasAlfabeticamente () throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais ORDER BY universidad ASC";
        PreparedStatement consultaUniversidades = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidades = AdministradorBaseDatos.getInstancia().
                                                       prepareStatement(consultarUniversidadesSQL);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
            consultaUniversidades.close();
            resultadoConsulta.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaUniversidades;
    }

    public static UniversidadDTO getUniversidadPorNombreYPais (String nombre, String pais) throws SQLException {
        UniversidadDTO universidadDTO = new UniversidadDTO(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, universidad, idPais FROM universidad_con_pais WHERE universidad = ? AND pais = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
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
        }
        catch (SQLException error) {
            throw error;
        }

        return universidadDTO;
    }

    public static UniversidadDTO getUniversidadPorId (int idUniversidad) throws SQLException {
        UniversidadDTO universidadDTO = new UniversidadDTO(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE idUniversidad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = AdministradorBaseDatos.getInstancia().
                                                     prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setInt(1, idUniversidad);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidadDTO = convertirResultSetAUniversidad(resultadoConsulta);
            }
            consultaUniversidad.close();
            resultadoConsulta.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return universidadDTO;
    }

    private static UniversidadDTO convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        UniversidadDTO universidadDTO = new UniversidadDTO();

        universidadDTO.setId(resultado.getInt(1));
        universidadDTO.setNombre(resultado.getString(2));
        universidadDTO.setIdPais(resultado.getInt(3));

        return universidadDTO;
    }
}
