package DAO;

import DAO.Interfaces.IRegionDAO;
import DTO.RegionDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase RegionDAO se encarga de obtener información de las regiones en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 * @author pale
 */
public class RegionDAO implements IRegionDAO {
    /**
     * Instancia del logger para registrar las excepciones que se pueden atrapar en las funciones de la clase.
     */
    private final Logger BITACORA = Logger.getLogger(RegionDAO.class);

    /**
     * Obtiene una lista de todas las regiones que se encuentran en la base de datos, ordenadas de manera alfabética de acuerdo a su nombre.
     * @return Lista de universidades ordenada de manera alfabética o una lista vacía si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<RegionDTO> getTodasAlfabeticamente () throws ErrorDAO {
        List<RegionDTO> listaRegiones = new ArrayList<>();
        String consultaSQL = "SELECT idRegion, nombre FROM region ORDER BY nombre ASC";
        PreparedStatement consultaRegiones;
        ResultSet resultadoConsulta;

        try {
            consultaRegiones = AdministradorBaseDatos.getInstancia()
                                                     .prepareStatement(consultaSQL);
            resultadoConsulta = consultaRegiones.executeQuery();

            while (resultadoConsulta.next()) {
                listaRegiones.add(convertirResultSetARegion(resultadoConsulta));
            }
            consultaRegiones.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            BITACORA.info(excepcionSQL.getMessage());
            throw new ErrorDAO("Ocurrió un error al intentar obtener las regiones. Si el problema persiste contacte a soporte", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaRegiones;
    }

    /**
     * Convierte un objeto ResultSet a un objeto RegionDTO, para poder transferir los datos obtenidos de una consulta SQL.
     * @param resultado ResultSet que se obtuvo de una consulta SQL.
     * @return Region inicializada con su id y nombre.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    private RegionDTO convertirResultSetARegion (ResultSet resultado) throws SQLException {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(resultado.getInt(1));
        regionDTO.setNombre(resultado.getString(2));
        return regionDTO;
    }
}
