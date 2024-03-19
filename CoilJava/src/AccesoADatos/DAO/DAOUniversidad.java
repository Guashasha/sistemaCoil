package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IUniversidadDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DAOUniversidad implements IUniversidadDAO {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    @Override
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
        catch (SQLException e){
            throw new ErrorDAO("SQLExcption: Error al registrar la universidad" + e.getMessage());
        }

        return filasAfectadas;
    }

    @Override
    public int editarUniversidad(Universidad universidad, int id) throws ErrorDAO {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad;

        try {
            actualizarUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1,universidad.getNombre());
            actualizarUniversidad.setString(2,universidad.getPaisOrigen());
            actualizarUniversidad.setInt(3,id);
            filasAfectadas = actualizarUniversidad.executeUpdate();

            actualizarUniversidad.close();
            this.CONEXION_BASE_DATOS.desconectar();
        } catch (SQLException e){
            throw new ErrorDAO("SQLExcption: Error al registrar la universidad" + e.getMessage());
        }

        return filasAfectadas;
    }

    @Override
    public Universidad getUniversidadPorNombre(String nombre) throws ErrorDAO {
        Universidad universidad;
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1,nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            universidad = convertirResultSetAUniversidad(resultadoConsulta);

            consultaUniversidad.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException e) {
            throw new ErrorDAO("SQLException: Error al realizar la consulta por nombre" + e.getMessage());
        }

        return universidad;
    }

    @Override
    public ArrayList<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        ArrayList<Universidad> listaUniversidades = new ArrayList<>();
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

    @Override
    public ArrayList<Universidad> getTodasAlfabeticamente() throws ErrorDAO {
        ArrayList<Universidad> listaUniversidades = new ArrayList<>();
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
        Universidad universidad = null;

        universidad.setId(resultado.getInt(1));
        universidad.setNombre(resultado.getString(2));
        universidad.setPaisOrigen(resultado.getString(3));

        return universidad;
    }
}
