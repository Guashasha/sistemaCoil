package AccesoADatos;

import Logica.Dominio.Universidad;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniversidadDB {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static int registrarUniversidad (Universidad universidad) throws SQLException {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad = null;

        try {
            insertarUniversidad = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(insertarUniversidadSQL);
            insertarUniversidad.setString(1, universidad.getNombre());
            insertarUniversidad.setInt(2, universidad.getIdPais());
            filasAfectadas = insertarUniversidad.executeUpdate();
        }
        catch (SQLException error) {
            throw error;
        }
        finally {
            insertarUniversidad.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return filasAfectadas;
    }

    public static int editarUniversidad (Universidad universidad) throws SQLException {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad = null;

        try {
            actualizarUniversidad = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1,universidad.getNombre());
            actualizarUniversidad.setInt(2,universidad.getIdPais());
            actualizarUniversidad.setInt(3,universidad.getId());
            filasAfectadas = actualizarUniversidad.executeUpdate();
        }
        catch (SQLException error){
            throw error;
        }
        finally {
            actualizarUniversidad.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return filasAfectadas;
    }

    public static Universidad getUniversidadPorNombre (String nombre) throws SQLException {
        Universidad universidad = new Universidad(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidad = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1, nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidad = convertirResultSetAUniversidad(resultadoConsulta);
            }
        }
        catch (SQLException error) {
            throw error;
        }
        finally {
            consultaUniversidad.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return universidad;
    }

    public static List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws SQLException {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais WHERE pais = ?";
        PreparedStatement consultaUniversidades = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidades = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultarUniversidadesSQL);
            consultaUniversidades.setString(1,paisOrigen);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
        }
        catch (SQLException error) {
            throw error;
        }
        finally {
            consultaUniversidades.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaUniversidades;
    }

    public static List<Universidad> getTodasAlfabeticamente () throws SQLException {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais ORDER BY universidad ASC";
        PreparedStatement consultaUniversidades = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidades = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultarUniversidadesSQL);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
        }
        catch (SQLException error) {
            throw error;
        }
        finally {
            consultaUniversidades.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaUniversidades;
    }

    public static Universidad getUniversidadPorNombreYPais (String nombre, String pais) throws SQLException {
        Universidad universidad = new Universidad(0);
        String consultaUniversidadSQL = "SELECT idUniversidad, universidad, idPais FROM universidad_con_pais WHERE universidad = ? AND pais = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidad = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1, nombre);
            consultaUniversidad.setString(2,pais);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidad = convertirResultSetAUniversidad(resultadoConsulta);
            }
        }
        catch (SQLException error) {
            throw error;
        }
        finally {
            consultaUniversidad.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return universidad;
    }

    public static Universidad convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        Universidad universidad = new Universidad();

        universidad.setId(resultado.getInt(1));
        universidad.setNombre(resultado.getString(2));
        universidad.setIdPais(resultado.getInt(3));

        return universidad;
    }
}
