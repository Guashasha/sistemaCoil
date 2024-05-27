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

    @Override
    public int agregar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Estudiante(?, ?, ?, ?, ?)}";
        int resultado = 0;
        try {
            CallableStatement registrarEstudiante = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            registrarEstudiante.setString(1, estudianteDTO.getNombre());
            registrarEstudiante.setString(2, estudianteDTO.getApellidoPaterno());
            registrarEstudiante.setString(3, estudianteDTO.getApellidoMaterno());
            registrarEstudiante.setInt(4, estudianteDTO.getIdUniversidad());
            registrarEstudiante.setString(5, estudianteDTO.getMatricula());

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

    @Override
    public int modificar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL editar_Estudiante(?, ?, ?, ?, ?)}";
        int resultado;
        try {
            CallableStatement editarEstudiante = AdministradorBaseDatos.getInstancia().
                                                                       prepareCall(procedimientoSQL);
            editarEstudiante.setString(1, estudianteDTO.getNombre());
            editarEstudiante.setString(2, estudianteDTO.getApellidoPaterno());
            editarEstudiante.setString(3, estudianteDTO.getApellidoMaterno());
            editarEstudiante.setString(4, estudianteDTO.getMatricula());
            editarEstudiante.setInt(5, estudianteDTO.getIdUniversidad());

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

    @Override
    public Optional<EstudianteDTO> getPorId (Integer id) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE idEstudiante = ?";
        EstudianteDTO estudianteDTO = null;

        try {
            PreparedStatement consultaEstudianteId = AdministradorBaseDatos.getInstancia().
                                                                      prepareStatement(consulta);
            consultaEstudianteId.setInt(1,id);
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

    @Override
    public Optional<EstudianteDTO> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE idPersona = ?";
        EstudianteDTO estudianteDTO = null;
        try {
            PreparedStatement cosnsultaEstudianteIdPersona = AdministradorBaseDatos.getInstancia().
                                                                        prepareStatement(consulta);
            cosnsultaEstudianteIdPersona.setInt(1,idPersona);
            ResultSet resultadoConsulta = cosnsultaEstudianteIdPersona.executeQuery();
            if (resultadoConsulta.next()) {
                estudianteDTO = convertirEstudiante(resultadoConsulta);
            }
            cosnsultaEstudianteIdPersona.close();
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

    @Override
    public Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        String consulta = "SELECT * from vista_estudiante WHERE matricula = ?";
        EstudianteDTO estudianteDTO = null;
        try {
            PreparedStatement cosnsultaEstudianteMatricula = AdministradorBaseDatos.getInstancia().
                                                                                prepareStatement(consulta);
            cosnsultaEstudianteMatricula.setString(1,matricula);
            ResultSet resultadoConsulta = cosnsultaEstudianteMatricula.executeQuery();
            if (resultadoConsulta.next()) {
                estudianteDTO = convertirEstudiante(resultadoConsulta);
            }
            cosnsultaEstudianteMatricula.close();
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

    @Override
    public List<EstudianteDTO> getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad (int idUniversidad) throws ErrorDAO {
        String consulta = "SELECT * from estudiantes_sin_colaboracion_vinculada_activa WHERE universidad = ?";
        ArrayList<EstudianteDTO> listaEstudianteDTOS = new ArrayList<>();
        try {
            PreparedStatement cosnsultaEstudianteUniversidad = AdministradorBaseDatos.getInstancia().
                                                                                prepareStatement(consulta);
            cosnsultaEstudianteUniversidad.setInt(1,idUniversidad);
            ResultSet resultadoConsulta = cosnsultaEstudianteUniversidad.executeQuery();

            while (resultadoConsulta.next()) {
                EstudianteDTO estudianteDTO = convertirEstudiante(resultadoConsulta);
                listaEstudianteDTOS.add(estudianteDTO);
            }
            cosnsultaEstudianteUniversidad.close();
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
        estudianteDTO.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        estudianteDTO.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        estudianteDTO.setIdEstudiante(resultado.getInt("idEstudiante"));
        estudianteDTO.setMatricula(resultado.getString("matricula"));
        estudianteDTO.setIdUniversidad(resultado.getInt("universidad"));
        return estudianteDTO;
    }


}
