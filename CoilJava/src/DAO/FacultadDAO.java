package DAO;

import DAO.Interfaces.IFacultadDAO;
import DTO.FacultadDTO;
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
 * La clase FacultadDAO se encarga de obtener información de las facultades en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 *
 * @author pale
 */
public class FacultadDAO implements IFacultadDAO {
    private final Logger BITACORA = Logger.getLogger(FacultadDAO.class);

    /**
     * Obtiene una facultad que esté registrada con un nombre específico.
     *
     * @param nombre Nombre de la facultad a buscar.
     * @return Objeto Optional con una facultad inicializada con su id, nombre e idRegion; o un objeto Optional vacío si no se encontraron resultados.
     * @throws ErrorDAO si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws ErrorDAO {
        FacultadDTO facultadDTO = null;
        String consultaSQL = "SELECT * FROM facultad_con_region WHERE facultad = ?";
        PreparedStatement ConsultaFacultad;
        ResultSet resultadoConsulta;

        try {
            ConsultaFacultad = AdministradorBaseDatos.getInstancia().
                                                     prepareStatement(consultaSQL);
            ConsultaFacultad.setString(1, nombre);
            resultadoConsulta = ConsultaFacultad.executeQuery();

            if (resultadoConsulta.next()) {
                facultadDTO = convertirResultSetAFacultad(resultadoConsulta);
            }
            ConsultaFacultad.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            BITACORA.warn(excepcionSQL.getMessage());
            throw new ErrorDAO("Ocurrió un error al intentar obtener la facultad. Si el problema persiste contacte a soporte", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(facultadDTO);
    }

    /**
     * Obtiene las facultades que están asociadas a una región específica.
     *
     * @param region Nombre de la región con la cual se quieren buscar facultades.
     * @return Lista con las facultades pertenecientes a la región especificada.
     * @throws ErrorDAO si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<FacultadDTO> getFacultadesPorRegion (String region) throws ErrorDAO {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        String consultaSQL = "SELECT * FROM facultad_con_region WHERE region = ?";
        PreparedStatement consultaFacultades;
        ResultSet resultadoConsulta;

        try {
            consultaFacultades = AdministradorBaseDatos.getInstancia().
                                                       prepareStatement(consultaSQL);
            consultaFacultades.setString(1, region);
            resultadoConsulta = consultaFacultades.executeQuery();

            while (resultadoConsulta.next()) {
                listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
            }
            consultaFacultades.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            BITACORA.warn(excepcionSQL.getMessage());
            throw new ErrorDAO("Ocurrió un error al intentar obtener las Facultades. Si el problema persiste contacte a soporte", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaFacultades;
    }

    private static FacultadDTO convertirResultSetAFacultad (ResultSet resultado) throws SQLException {
        FacultadDTO facultadDTO = new FacultadDTO();

        facultadDTO.setId(resultado.getInt(1));
        facultadDTO.setNombre(resultado.getString(2));
        facultadDTO.setIdRegion(resultado.getInt(3));

        return facultadDTO;
    }
}
