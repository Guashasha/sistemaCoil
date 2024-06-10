package DAO;

import DAO.Interfaces.IFacultadDAO;
import DTO.FacultadDTO;
import AccesoDatos.AdministradorBaseDatos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase FacultadDAO se encarga de obtener información de las facultades en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 * @author pale
 */
public class FacultadDAO implements IFacultadDAO {
    /**
     * Obtiene una facultad que esté registrada con un nombre específico.
     * @param nombre Nombre de la facultad a buscar.
     * @return Objeto Optional con una facultad inicializada con su id, nombre e idRegion; o un objeto Optional vacío si no se encontraron resultados.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public Optional<FacultadDTO> getFacultadPorNombre (String nombre) throws SQLException {
        FacultadDTO facultadDTO = null;
        String consultaUniversidadSQL = "SELECT * FROM facultad_con_region WHERE facultad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = AdministradorBaseDatos.getInstancia().
                                                        prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1, nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                facultadDTO = convertirResultSetAFacultad(resultadoConsulta);
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

        return Optional.ofNullable(facultadDTO);
    }

    /**
     * Obtiene las facultades que están asociadas a una región específica.
     * @param region Nombre de la región con la cual se quieren buscar facultades.
     * @return Lista con las facultades pertenecientes a la región especificada.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    @Override
    public List<FacultadDTO> getFacultadPorRegion (String region) throws SQLException {
        List<FacultadDTO> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region WHERE region = ?";
        PreparedStatement consultaFacultades;
        ResultSet resultadoConsulta;

        try {
            consultaFacultades = AdministradorBaseDatos.getInstancia().
                                                       prepareStatement(consultaFacultadesSQL);
            consultaFacultades.setString(1, region);
            resultadoConsulta = consultaFacultades.executeQuery();

            while (resultadoConsulta.next()) {
                listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
            }
            consultaFacultades.close();
            resultadoConsulta.close();
        }
        catch (SQLException excepcionSQL) {
            throw excepcionSQL;
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaFacultades;
    }

    /**
     * Convierte un objeto ResultSet a un objeto FacultadDTO, para poder transferir los datos obtenidos de una consulta SQL.
     * @param resultado ResultSet que se obtuvo de una consulta SQL.
     * @return Facultad inicializada con su id, nombre y el id de la región a la que se asocia.
     * @throws SQLException si ocurre un error de acceso a la base de datos.
     */
    private static FacultadDTO convertirResultSetAFacultad (ResultSet resultado) throws SQLException {
        FacultadDTO facultadDTO = new FacultadDTO();

        facultadDTO.setId(resultado.getInt(1));
        facultadDTO.setNombre(resultado.getString(2));
        facultadDTO.setIdRegion(resultado.getInt(3));

        return facultadDTO;
    }
}
