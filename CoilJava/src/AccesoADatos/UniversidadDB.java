package AccesoADatos;

import Logica.Dominio.Universidad;
import Logica.ErrorDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniversidadDB {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public int registrarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad;

        try {
            insertarUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(insertarUniversidadSQL);
            insertarUniversidad.setString(1, universidad.getNombre());
            insertarUniversidad.setString(2, universidad.getPaisOrigen());
            filasAfectadas = insertarUniversidad.executeUpdate();

            insertarUniversidad.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException e) {
            throw new ErrorDAO("SQLExcption: Error al registrar la universidad" + e.getMessage());
        }

        return filasAfectadas;
    }

    public int editarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad;

        try {
            actualizarUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1,universidad.getNombre());
            actualizarUniversidad.setString(2,universidad.getPaisOrigen());
            actualizarUniversidad.setInt(3,universidad.getId());
            filasAfectadas = actualizarUniversidad.executeUpdate();

            actualizarUniversidad.close();
            this.CONEXION_BASE_DATOS.desconectar();
        } catch (SQLException e){
            throw new ErrorDAO("SQLExcption: Error al registrar la universidad" + e.getMessage());
        }

        return filasAfectadas;
    }

    public Universidad getUniversidadPorNombre(String nombre) throws ErrorDAO {
        Universidad universidad = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1,nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidad = convertirResultSetAUniversidad(resultadoConsulta);
            }

            consultaUniversidad.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException e) {
            throw new ErrorDAO("SQLException: Error al realizar la consulta por nombre" + e.getMessage());
        }

        return universidad;
    }

    public List<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE paisOrigen = ?";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidades = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultarUniversidadesSQL);
            consultaUniversidades.setString(1,paisOrigen);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }

            consultaUniversidades.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException e) {
            throw new ErrorDAO("SQLException: Error al realizar la consulta por nombre" + e.getMessage());
        }

        return listaUniversidades;
    }

    public List<Universidad> getTodasAlfabeticamente() throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad ORDER BY nombre ASC";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidades = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultarUniversidadesSQL);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }

            consultaUniversidades.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException e) {
            throw new ErrorDAO("SQLException: Error al realizar la consulta por nombre" + e.getMessage());
        }

        return listaUniversidades;
    }

    public Universidad convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        Universidad universidad = new Universidad();

        universidad.setId(resultado.getInt(1));
        universidad.setNombre(resultado.getString(2));
        universidad.setPaisOrigen(resultado.getString(3));

        return universidad;
    }
}
