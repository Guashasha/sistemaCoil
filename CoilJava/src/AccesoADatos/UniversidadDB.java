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

    public int registrarUniversidad (Universidad universidad) throws ErrorDAO {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad;

        try {
            insertarUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(insertarUniversidadSQL);
            insertarUniversidad.setString(1, universidad.getNombre());
            insertarUniversidad.setInt(2, universidad.getIdPais());
            filasAfectadas = insertarUniversidad.executeUpdate();

            insertarUniversidad.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO("SQLExcption: Error al registrar la universidad\n" + error.getMessage());
        }

        return filasAfectadas;
    }

    public int editarUniversidad (Universidad universidad) throws ErrorDAO {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad;

        try {
            actualizarUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1,universidad.getNombre());
            actualizarUniversidad.setInt(2,universidad.getIdPais());
            actualizarUniversidad.setInt(3,universidad.getId());
            filasAfectadas = actualizarUniversidad.executeUpdate();

            actualizarUniversidad.close();
            this.CONEXION_BASE_DATOS.desconectar();
        } catch (SQLException e){
            throw new ErrorDAO("SQLExcption: Error al aditar la universidad\n" + e.getMessage());
        }

        return filasAfectadas;
    }

    public Universidad getUniversidadPorNombre (String nombre) throws ErrorDAO {
        Universidad universidad = new Universidad(0);
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
            throw new ErrorDAO("SQLException: Error al consultar universidad por nombre\n" + e.getMessage());
        }

        return universidad;
    }

    public List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais WHERE pais = ?";
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
            throw new ErrorDAO("SQLException: Error al consultar universidades por pais de origen\n" + e.getMessage());
        }

        return listaUniversidades;
    }

    public List<Universidad> getTodasAlfabeticamente () throws ErrorDAO {
        List<Universidad> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais ORDER BY universidad ASC";
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
            throw new ErrorDAO("SQLException: Error al consultar universidades por nombre\n" + e.getMessage());
        }

        return listaUniversidades;
    }

    public Universidad convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        Universidad universidad = new Universidad();

        universidad.setId(resultado.getInt(1));
        universidad.setNombre(resultado.getString(2));
        universidad.setIdPais(resultado.getInt(3));

        return universidad;
    }
}
