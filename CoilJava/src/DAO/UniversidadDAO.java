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

/**
 * La clase UniversidadDAO se encarga de obtener o manipular información de las universidades en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 * @author pale
 */
public class UniversidadDAO implements IUniversidadDAO {
    /**
     * Registra una universidad en la base de datos.
     * @param universidad universidad a registrar, inicializada con nombre e id de pais
     * @return número de filas afectadas por la sentencia SQL.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public int registrarUniversidad (UniversidadDTO universidad) throws SQLException {
        int filasAfectadas;
        String insertarUniversidadSQL = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        PreparedStatement insertarUniversidad;

        try {
            insertarUniversidad = AdministradorBaseDatos.getInstancia().
                                                        prepareStatement(insertarUniversidadSQL);
            insertarUniversidad.setString(1, universidad.getNombre());
            insertarUniversidad.setInt(2, universidad.getIdPais());
            filasAfectadas = insertarUniversidad.executeUpdate();

            insertarUniversidad.close();

        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return filasAfectadas;
    }

    /**
     * Actualiza la información de una universidad contenida en la base de datos
     * @param universidad universidad inicializada con su id, nombre e id de pais
     * @return número de filas afectadas por la sentencia SQL
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public int editarUniversidad (UniversidadDTO universidad) throws SQLException {
        int filasAfectadas;
        String actualizarUniversidadSQL = "UPDATE universidad SET nombre = ?, paisOrigen = ? WHERE idUniversidad = ?";
        PreparedStatement actualizarUniversidad;

        try {
            actualizarUniversidad = AdministradorBaseDatos.getInstancia().
                                                          prepareStatement(actualizarUniversidadSQL);
            actualizarUniversidad.setString(1, universidad.getNombre());
            actualizarUniversidad.setInt(2, universidad.getIdPais());
            actualizarUniversidad.setInt(3, universidad.getId());
            filasAfectadas = actualizarUniversidad.executeUpdate();

            actualizarUniversidad.close();
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return filasAfectadas;
    }

    /**
     * Obtiene la información de una universidad de acuerdo a su nombre.
     * @param nombre nombre de la universidad que se quiere buscar.
     * @return Objeto Optional con una universidad inicializada con su id, nombre e id de país; o un objeto Optional vacío si no se encuentran resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws SQLException {
        UniversidadDTO universidadDTO = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE nombre = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

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
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(universidadDTO);
    }

    /**
     * Obtiene una lista de las universidades que pertenecen a un pais determinado.
     * @param paisOrigen nombre del país.
     * @return Lista con las universidades pertenecientes al pais especificado o una lista vacía si no se encuentran resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws SQLException {
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
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaUniversidades;
    }

    /**
     * Obtiene la lista de universidades que tienen el nombre coincidente con una cadena determinada.
     * @param nombre cadena coincidente en el nombre.
     * @return lista con las universidades coincidentes con la cadena especificada o una lista vacía si no se encuentran resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<UniversidadDTO> getUniversidadesPorNombre (String nombre) throws SQLException {
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
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaUniversidades;
    }

    /**
     * Obtiene una lista de todas las universidades que se encuentran en la base de datos, ordenadas de manera alfabética de acuerdo a su nombre.
     * @return Lista de universidades ordenada de manera alfabética o una lista vacía si no se encuentran resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<UniversidadDTO> getTodasAlfabeticamente () throws SQLException {
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        String consultarUniversidadesSQL = "SELECT * FROM universidad_con_pais ORDER BY universidad ASC";
        PreparedStatement consultaUniversidades;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidades = AdministradorBaseDatos.getInstancia().
                                                          prepareStatement(consultarUniversidadesSQL);
            resultadoConsulta = consultaUniversidades.executeQuery();

            while (resultadoConsulta.next()) {
                listaUniversidades.add(convertirResultSetAUniversidad(resultadoConsulta));
            }
            consultaUniversidades.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaUniversidades;
    }

    /**
     * Obtiene una universidad que contenga un nombre y un país específico.
     * @param nombre Nombre de la universidad.
     * @param pais Nombre del país al que pertenece la universidad.
     * @return Objeto Optional con una universidad inicializada con su id, nombre e id de país; o un objeto Optional vacío si no se encuentran resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<UniversidadDTO> getUniversidadPorNombreYPais (String nombre, String pais) throws SQLException {
        UniversidadDTO universidadDTO = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, universidad, idPais FROM universidad_con_pais WHERE universidad = ? AND pais = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

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
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(universidadDTO);
    }

    /**
     * Obtiene una universidad que esté registrada con un id específico.
     * @param id id de la universidad que se quiere obtener.
     * @return Objeto Optional con una universidad inicializada con su id, nombre e id de país; o un objeto Optional vacío si no se encuentran resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws SQLException {
        UniversidadDTO universidadDTO = null;
        String consultaUniversidadSQL = "SELECT idUniversidad, nombre, paisOrigen FROM universidad WHERE idUniversidad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = AdministradorBaseDatos.getInstancia().
                                                        prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setInt(1, id);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                universidadDTO = convertirResultSetAUniversidad(resultadoConsulta);
            }
            consultaUniversidad.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(universidadDTO);
    }

    /**
     * Convierte un objeto ResultSet a un objeto UniversidadDTO, para poder transferir los datos obtenidos de una consulta SQL.
     * @param resultado ResultSet que se obtuvo de una consulta SQL.
     * @return Universidad inicializada con su id, nombre e id de país.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    private UniversidadDTO convertirResultSetAUniversidad (ResultSet resultado) throws SQLException {
        UniversidadDTO universidadDTO = new UniversidadDTO();

        universidadDTO.setId(resultado.getInt(1));
        universidadDTO.setNombre(resultado.getString(2));
        universidadDTO.setIdPais(resultado.getInt(3));

        return universidadDTO;
    }
}
