package DAO;

import DAO.Interfaces.IEstudianteDAO;
import DTO.EstudianteDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstudianteDAO implements IEstudianteDAO {
    private static final Logger BITACORA = Logger.getLogger(EstudianteDAO.class);

    /**
     * Agrega un nuevo estudiante a la base de datos.
     *
     * @param estudianteDTO el objeto EstudianteDTO que contiene la información del estudiante a agregar.
     * @return el número de filas afectadas por la inserción.
     * @throws ErrorDAO si ocurre un error durante la inserción en la base de datos.
     */
    @Override
    public int agregar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Estudiante(?, ?, ?, ?)}";
        int resultado;
        try {
            CallableStatement registrarEstudiante = AdministradorBaseDatos.getInstancia().
                                                                          prepareCall(procedimientoSQL);
            registrarEstudiante.setString(1, estudianteDTO.getNombre());
            registrarEstudiante.setString(2, estudianteDTO.getApellidos());
            registrarEstudiante.setInt(3, estudianteDTO.getIdUniversidad());
            registrarEstudiante.setString(4, estudianteDTO.getMatricula());

            resultado = registrarEstudiante.executeUpdate();
            registrarEstudiante.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al agregar estudiantes", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return resultado;
    }

    /**
     * Modifica un estudiante existente en la base de datos.
     *
     * @param estudianteDTO el objeto EstudianteDTO que contiene la información del estudiante a modificar.
     * @return el número de filas afectadas por la modificación.
     * @throws ErrorDAO si ocurre un error durante la modificación en la base de datos.
     */
    @Override
    public int modificar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL editar_Estudiante(?, ?, ?, ?)}";
        int resultado;
        try {
            CallableStatement editarEstudiante = AdministradorBaseDatos.getInstancia().
                                                                       prepareCall(procedimientoSQL);
            editarEstudiante.setString(1, estudianteDTO.getNombre());
            editarEstudiante.setString(2, estudianteDTO.getApellidos());
            editarEstudiante.setString(3, estudianteDTO.getMatricula());
            editarEstudiante.setInt(4, estudianteDTO.getIdUniversidad());

            resultado = editarEstudiante.executeUpdate();
            editarEstudiante.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al editar al estudianteDTO", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return resultado;
    }

    /**
     * Obtiene un estudiante basado en su ID.
     *
     * @param id el ID del estudiante que se desea obtener.
     * @return un objeto Optional que contiene el estudiante encontrado o vacío si no se encuentra el estudiante.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<EstudianteDTO> getPorId (Integer id) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE idEstudiante = ?";
        EstudianteDTO estudianteDTO = null;

        try {
            PreparedStatement consultaEstudianteId = AdministradorBaseDatos.getInstancia().
                                                                           prepareStatement(consulta);
            consultaEstudianteId.setInt(1, id);
            ResultSet resultadoConsulta = consultaEstudianteId.executeQuery();
            if (resultadoConsulta.next()) {
                estudianteDTO = convertirEstudiante(resultadoConsulta);
            }
            consultaEstudianteId.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener un estudianteDTO", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(estudianteDTO);
    }

    /**
     * Obtiene un estudiante basado en el ID de la persona asociada.
     *
     * @param idPersona el ID de la persona asociada al estudiante que se desea obtener.
     * @return un objeto Optional que contiene el estudiante encontrado o vacío si no se encuentra el estudiante.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<EstudianteDTO> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE idPersona = ?";
        EstudianteDTO estudianteDTO = null;
        try {
            PreparedStatement consultaEstudianteIdPersona = AdministradorBaseDatos.getInstancia().
                                                                                  prepareStatement(consulta);
            consultaEstudianteIdPersona.setInt(1, idPersona);
            ResultSet resultadoConsulta = consultaEstudianteIdPersona.executeQuery();
            if (resultadoConsulta.next()) {
                estudianteDTO = convertirEstudiante(resultadoConsulta);
            }
            consultaEstudianteIdPersona.close();
            resultadoConsulta.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al consultar al estudianteDTO", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(estudianteDTO);
    }


    /**
     * Obtiene un estudiante basado en su matrícula y la universidad.
     *
     * @param matricula la matrícula del estudiante.
     * @param idUniversidad el ID de la universidad.
     * @return un objeto Optional que contiene el estudiante encontrado o vacío si no se encuentra el estudiante.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<EstudianteDTO> getEstudiantePorMatriculaYUniversidad (String matricula, int idUniversidad) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE matricula = ? AND universidad = ?";
        EstudianteDTO estudianteDTO = null;
        try {
            PreparedStatement consultaEstudianteMatricula = AdministradorBaseDatos.getInstancia().
                                                                                  prepareStatement(consulta);
            consultaEstudianteMatricula.setString(1, matricula);
            consultaEstudianteMatricula.setInt(2, idUniversidad);
            ResultSet resultadoConsulta = consultaEstudianteMatricula.executeQuery();
            if (resultadoConsulta.next()) {
                estudianteDTO = convertirEstudiante(resultadoConsulta);
            }
            consultaEstudianteMatricula.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al consultar al estudianteDTO", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(estudianteDTO);
    }

    /**
     * Obtiene una lista de estudiantes que no tienen una colaboración activa o vinculada, basada en la universidad.
     *
     * @param idUniversidad el ID de la universidad.
     * @return una lista de objetos EstudianteDTO que cumplen con el criterio especificado.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public List<EstudianteDTO> getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad (int idUniversidad) throws ErrorDAO {
        String consulta = "SELECT * from estudiantes_sin_colaboracion_vinculada_activa WHERE universidad = ?";
        ArrayList<EstudianteDTO> listaEstudianteDTOS = new ArrayList<>();
        try {
            PreparedStatement consultaEstudianteUniversidad = AdministradorBaseDatos.getInstancia().
                                                                                    prepareStatement(consulta);
            consultaEstudianteUniversidad.setInt(1, idUniversidad);
            ResultSet resultadoConsulta = consultaEstudianteUniversidad.executeQuery();

            while (resultadoConsulta.next()) {
                EstudianteDTO estudianteDTO = convertirEstudiante(resultadoConsulta);
                listaEstudianteDTOS.add(estudianteDTO);
            }
            consultaEstudianteUniversidad.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener a los estudiantes", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaEstudianteDTOS;
    }

    /**
     * Obtiene un estudiante basado en su matrícula.
     *
     * @param matricula la matrícula del estudiante que se desea obtener.
     * @return un objeto Optional que contiene el estudiante encontrado o vacío si no se encuentra el estudiante.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE matricula = ?";
        EstudianteDTO estudianteDTO = null;
        try {
            PreparedStatement consultaEstudianteMatricula = AdministradorBaseDatos.getInstancia().
                                                                                  prepareStatement(consulta);
            consultaEstudianteMatricula.setString(1, matricula);
            ResultSet resultadoConsulta = consultaEstudianteMatricula.executeQuery();
            if (resultadoConsulta.next()) {
                estudianteDTO = convertirEstudiante(resultadoConsulta);
            }
            consultaEstudianteMatricula.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al consultar al estudianteDTO", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(estudianteDTO);
    }

    /**
     * Obtiene todos los estudiantes registrados en la base de datos.
     *
     * @return una lista de todos los objetos EstudianteDTO registrados en la base de datos.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public List<EstudianteDTO> getTodos () throws ErrorDAO {
        String consulta = "SELECT * FROM vista_estudiante";
        List<EstudianteDTO> listaEstudianteDTOS = new ArrayList<>();
        try {
            PreparedStatement consultaEstudiante = AdministradorBaseDatos.getInstancia().
                                                                         prepareStatement(consulta);
            ResultSet resultadoConsulta = consultaEstudiante.executeQuery();
            while (resultadoConsulta.next()) {
                EstudianteDTO estudianteDTO = convertirEstudiante(resultadoConsulta);
                listaEstudianteDTOS.add(estudianteDTO);
            }
            consultaEstudiante.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener los estudiantes", Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaEstudianteDTOS;
    }

    private static EstudianteDTO convertirEstudiante (ResultSet resultado) throws SQLException {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setIdPersona(resultado.getInt("idPersona"));
        estudianteDTO.setNombre(resultado.getString("nombre"));
        estudianteDTO.setApellidos(resultado.getString("apellidos"));
        estudianteDTO.setIdEstudiante(resultado.getInt("idEstudiante"));
        estudianteDTO.setMatricula(resultado.getString("matricula"));
        estudianteDTO.setIdUniversidad(resultado.getInt("universidad"));
        return estudianteDTO;
    }


}
