package DAO;

import DAO.Interfaces.IPaisDAO;
import DTO.PaisDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase PaisDAO se encarga de obtener información de los países en la base de datos y mandarlos a capas superiores mediante Transfer Objects
 * @author pale
 */
public class PaisDAO implements IPaisDAO {
    /**
     * Instancia del logger para registrar las excepciones que se pueden atrapar en las funciones de la clase.
     */
    private final static Logger BITACORA = Logger.getLogger(PaisDAO.class);

    /**
     * Obtiene una lista de todos los países que se encuentran en la base de datos, ordenadas de manera alfabética de acuerdo a su nombre.
     * @return Lista de universidades ordenada de manera alfabética o una lista vacía si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<PaisDTO> getPaisesAlfabeticamente () throws ErrorDAO {
        List<PaisDTO> listaPaises = new ArrayList<>();
        String consultaSQL = "SELECT idPais, iso, nombre FROM pais ORDER BY nombre ASC";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        try {
            consultaPaises = AdministradorBaseDatos.getInstancia().
                                                   prepareStatement(consultaSQL);
            resultadoConsulta = consultaPaises.executeQuery();

            while (resultadoConsulta.next()) {
                listaPaises.add(convertirResultSetAPais(resultadoConsulta));
            }
            consultaPaises.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            BITACORA.info(excepcionSQL.getMessage());
            throw new ErrorDAO("Ocurrió un error al intentar obtener los paises. Si el problema persiste contacte a soporte", ErrorDAO.Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaPaises;
    }

    /**
     * Obtiene un país que esté registrado con un nombre específico
     * @param nombre Nombre del país que se quiere buscar.
     * @return Objeto Optional con un pais inicializado con su id, iso y nombre; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<PaisDTO> getPaisPorNombre (String nombre) throws ErrorDAO {
        PaisDTO paisDTO = null;
        String consultaSQL = "SELECT idPais, iso, nombre FROM pais WHERE nombre = ?";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        try {
            consultaPaises = AdministradorBaseDatos.getInstancia().
                                                   prepareStatement(consultaSQL);
            consultaPaises.setString(1, nombre);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                paisDTO = convertirResultSetAPais(resultadoConsulta);
            }
            consultaPaises.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            BITACORA.info(excepcionSQL.getMessage());
            throw new ErrorDAO("Ocurrió un error al intentar obtener el pais. Si el problema persiste contacte a soporte", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(paisDTO);
    }

    /**
     * Obtiene un país que esté asociado a un id específico.
     * @param id id del país que se quiere buscar.
     * @return Objeto Optional con un pais inicializado con su id, iso y nombre; o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<PaisDTO> getPaisPorId (int id) throws ErrorDAO {
        PaisDTO paisDTO = null;
        String consultaSQL = "SELECT idPais, iso, nombre FROM pais WHERE idPais = ?";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        try {
            consultaPaises = AdministradorBaseDatos.getInstancia().
                                                   prepareStatement(consultaSQL);
            consultaPaises.setInt(1, id);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                paisDTO = convertirResultSetAPais(resultadoConsulta);
            }
            consultaPaises.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            BITACORA.info(excepcionSQL.getMessage());
            throw new ErrorDAO("Ocurrió un error al intentar obtener el pais. Si el problema persiste contacte a soporte", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(paisDTO);
    }

    /**
     * Convierte un objeto ResultSet a un objeto PaisDTO, para poder transferir los datos obtenidos de una consulta SQL.
     * @param resultado ResultSet que se obtuvo de una consulta SQL.
     * @return Pais inicializada con su id, iso y nombre.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    private PaisDTO convertirResultSetAPais (ResultSet resultado) throws SQLException {
        PaisDTO paisDTO = new PaisDTO();

        paisDTO.setId(resultado.getInt(1));
        paisDTO.setIso(resultado.getString(2));
        paisDTO.setNombre(resultado.getString(3));

        return paisDTO;
    }

}
