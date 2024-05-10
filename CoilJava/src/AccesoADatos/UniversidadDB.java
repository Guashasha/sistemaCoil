package AccesoADatos;

import Logica.Dominio.Universidad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniversidadDB {
    

    public static int registrarUniversidad (Universidad universidad) throws SQLException {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad = null;

        try {
            insertarUniversidad = ConexionBaseDatos.getInstancia().
                                                     prepareStatement(insertarUniversidadSQL);
            insertarUniversidad.setString(1, universidad.getNombre());
            insertarUniversidad.setInt(2, universidad.getIdPais());
            filasAfectadas = insertarUniversidad.executeUpdate();

            insertarUniversidad.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return filasAfectadas;
    }

    public static int editarUniversidad (Universidad universidad) throws SQLException {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad = null;

        try {
            actualizarUniversidad = ConexionBaseDatos.getInstancia().
                                                       prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1, universidad.getNombre());
            actualizarUniversidad.setInt(2, universidad.getIdPais());
            actualizarUniversidad.setInt(3, universidad.getId());
            filasAfectadas = actualizarUniversidad.executeUpdate();

            actualizarUniversidad.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return filasAfectadas;
    }

    public static Universidad getUniversidadPorNombre (String nombre) throws SQLException {
        Universidad universidad = new Universidad(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidad = ConexionBaseDatos.getInstancia().
                                                     prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1, nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidad = convertirResultSetAUniversidad(resultadoConsulta);
            }
            consultaUniversidad.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return universidad;
    }

    public static List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws SQLException {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais WHERE pais = ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidades = ConexionBaseDatos.getInstancia().
                                                       prepareStatement(consultarUniversidadesSQL);
            consultaUniversidades.setString(1, paisOrigen);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
            consultaUniversidades.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaUniversidades;
    }

    public static List<Universidad> getUniversidadesPorNombre (String nombre) throws SQLException {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad WHERE nombre LIKE ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidades = ConexionBaseDatos.getInstancia().
                    prepareStatement(consultarUniversidadesSQL);
            consultaUniversidades.setString(1, "%" + nombre + "%");
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
            consultaUniversidades.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaUniversidades;
    }

    public static List<Universidad> getTodasAlfabeticamente () throws SQLException {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais ORDER BY universidad ASC";
        PreparedStatement consultaUniversidades = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidades = ConexionBaseDatos.getInstancia().
                                                       prepareStatement(consultarUniversidadesSQL);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
            consultaUniversidades.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaUniversidades;
    }

    public static Universidad getUniversidadPorNombreYPais (String nombre, String pais) throws SQLException {
        Universidad universidad = new Universidad(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, universidad, idPais FROM universidad_con_pais WHERE universidad = ? AND pais = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidad = ConexionBaseDatos.getInstancia().
                                                     prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1, nombre);
            consultaUniversidad.setString(2, pais);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidad = convertirResultSetAUniversidad(resultadoConsulta);
            }
            consultaUniversidad.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return universidad;
    }

    public static Universidad getUniversidadPorId (int idUniversidad) throws SQLException {
        Universidad universidad = new Universidad(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE idUniversidad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = ConexionBaseDatos.getInstancia().
                                                     prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setInt(1, idUniversidad);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidad = convertirResultSetAUniversidad(resultadoConsulta);
            }
            consultaUniversidad.close();
            resultadoConsulta.close();
            ConexionBaseDatos.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return universidad;
    }

    private static Universidad convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        Universidad universidad = new Universidad();

        universidad.setId(resultado.getInt(1));
        universidad.setNombre(resultado.getString(2));
        universidad.setIdPais(resultado.getInt(3));

        return universidad;
    }
}
