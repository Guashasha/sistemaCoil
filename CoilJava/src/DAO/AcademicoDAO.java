package DAO;

import DAO.Interfaces.IAcademicoDAO;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AcademicoDAO implements IAcademicoDAO {
    private static final Logger BITACORA = Logger.getLogger(AcademicoDAO.class);

    /**
     * Obtiene una lista de objetos AcademicoDTO basado en nombre de la fila de la base de datos y el patrón a buscar .
     *
     * @param campo el nombre de la fila por el cual se desea filtrar los académicos (por ejemplo, "facultad", "universidad").
     * @param valor el patrón correspondiente al campo que se desea buscar.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    private List<AcademicoDTO> getListaAcademicoPorCampos (String campo, String valor) throws ErrorDAO {
        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        List<AcademicoDTO> listaAcademicoDTOS = new ArrayList<>();
        try {
            CallableStatement procedimientoAcademico = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            procedimientoAcademico.setString(1, campo);
            procedimientoAcademico.setString(2, valor);
            ResultSet resultado = procedimientoAcademico.executeQuery();

            while (resultado.next()) {
                AcademicoDTO academicoDTO = convertirAcademico(resultado);
                listaAcademicoDTOS.add(academicoDTO);
            }
            procedimientoAcademico.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("No fue posible obtener a los academicos registrados", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaAcademicoDTOS;
    }

    /**
     * Obtiene una lista de académicos por nombre de facultad.
     *
     * @param nombrefacultad el nombre de la facultad por la cual se va a filtrar
     * @return una lista de objetos AcademicoDTO que pertenecen a la facultad especificada
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public List<AcademicoDTO> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        return getListaAcademicoPorCampos("facultad", nombrefacultad);
    }

    /**
     * Obtiene un académico por su cédula profesional.
     *
     * @param cedula la cédula profesional del académico que se desea obtener
     * @return un objeto Optional que contiene el objeto AcademicoDTO si se encuentra, de lo contrario está vacío
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public Optional<AcademicoDTO> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        AcademicoDTO academicoDTO = null;
        try {
            CallableStatement procedimientoAcademico = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            procedimientoAcademico.setString(1, "cedula");
            procedimientoAcademico.setString(2, cedula);
            ResultSet resultadoLLamada = procedimientoAcademico.executeQuery();

            if (resultadoLLamada.next()) {
                academicoDTO = convertirAcademico(resultadoLLamada);
            }

            procedimientoAcademico.close();
            resultadoLLamada.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al obtener a los academicos por cedula", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(academicoDTO);
    }

    /**
     * Obtiene una lista de académicos por nombre de universidad.
     *
     * @param nombreUniversidad el nombre de la universidad por la cual se va a filtrar
     * @return una lista de objetos AcademicoDTO que pertenecen a la universidad especificada
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public List<AcademicoDTO> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        return getListaAcademicoPorCampos("universidad", nombreUniversidad);
    }

    /**
     * Obtiene una lista de académicos por área de estudios.
     *
     * @param areaEstudios el área de estudios por la cual se va a filtrar
     * @return una lista de objetos AcademicoDTO que pertenecen al área de estudios especificada
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public List<AcademicoDTO> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        return getListaAcademicoPorCampos("area", areaEstudios);
    }

    /**
     * Obtiene una lista de académicos por categoría de contratación.
     *
     * @param categoriaContratacion la categoría de contratación por la cual se va a filtrar
     * @return una lista de objetos AcademicoDTO que pertenecen a la categoría de contratación especificada
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public List<AcademicoDTO> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        return getListaAcademicoPorCampos("categoria", categoriaContratacion);
    }

    /**
     * Obtiene una lista de académicos por región.
     *
     * @param region la región por la cual se va a filtrar
     * @return una lista de objetos AcademicoDTO que pertenecen a la región especificada
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public List<AcademicoDTO> getAcademicosPorRegion (String region) throws ErrorDAO {
        return getListaAcademicoPorCampos("region", region);
    }

    /**
     * Agrega un nuevo académico a la base de datos.
     *
     * @param academicoDTO el objeto AcademicoDTO que contiene la información del académico a agregar
     * @return un entero indicando el resultado de la operación (por ejemplo, 1 si la operación fue exitosa)
     * @throws ErrorDAO tipo inserción si ocurre un error durante la inserción
     */
    @Override
    public int agregar (AcademicoDTO academicoDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int filasAfectadas = -1;
        try {
            CallableStatement procedimientoAcademico = AdministradorBaseDatos.getInstancia().
                                                                         prepareCall(procedimientoSQL);
            setAcademicoParametros(procedimientoAcademico, academicoDTO);
            procedimientoAcademico.registerOutParameter(11, Types.INTEGER);
            filasAfectadas = procedimientoAcademico.executeUpdate();
            procedimientoAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al registrar al academico", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Obtiene un académico por su ID.
     *
     * @param id el ID del académico que se desea obtener
     * @return un objeto Optional que contiene el objeto AcademicoDTO si se encuentra, de lo contrario está vacío
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    @Override
    public Optional<AcademicoDTO> getPorId (Integer id) throws ErrorDAO {
        String consultaSQL = "SELECT * from vista_academico WHERE idPersona = ?";
        AcademicoDTO academicoDTO = null;
        try {
            PreparedStatement consultaAcademico = AdministradorBaseDatos.getInstancia().
                                                                         prepareStatement(consultaSQL);
            consultaAcademico.setInt(1, id);
            ResultSet resultado = consultaAcademico.executeQuery();
            if (resultado.next()) {
                academicoDTO = convertirAcademico(resultado);
            }
            consultaAcademico.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al obtener a un académico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(academicoDTO);
    }

    /**
     * Obtiene una lista de todos los académicos.
     *
     * @return una lista de objetos AcademicoDTO que contiene todos los académicos registrados
     * @throws ErrorDAO tipo consulta si ocurre un error durante la ejecución de la consulta
     */
    public List<AcademicoDTO> getTodos () throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_academico";
        List<AcademicoDTO> listaAcademicoDTOS = new ArrayList<>();

        try {
            PreparedStatement consultaAcademico = AdministradorBaseDatos.getInstancia().
                                                                        prepareStatement(consultaSQL);
            ResultSet resultado = consultaAcademico.executeQuery();
            while (resultado.next()) {
                AcademicoDTO academicoDTO = convertirAcademico(resultado);
                listaAcademicoDTOS.add(academicoDTO);
            }
            consultaAcademico.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al obtener a todos los académicos registrados", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaAcademicoDTOS;
    }

    /**
     * Modifica la información de un académico en la base de datos.
     *
     * @param academicoDTO el objeto AcademicoDTO que contiene la información del académico a modificar
     * @return un entero indicando el resultado de la operación (por ejemplo, 1 si la operación fue exitosa)
     * @throws ErrorDAO tipo modificación si ocurre un error durante la modificación
     */
    public int modificar (AcademicoDTO academicoDTO) throws ErrorDAO {
        int filasAfectadas;
        String procedimientoSQL = "{CALL editar_academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            CallableStatement procedimientoAcademico = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            setAcademicoParametros(procedimientoAcademico, academicoDTO);

            filasAfectadas = procedimientoAcademico.executeUpdate();

            procedimientoAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error en la modificación del académico", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Agrega un académico junto con su cuenta a la base de datos.
     *
     * @param academicoDTO el objeto AcademicoDTO que contiene la información del académico a agregar
     * @param cuentaDTO el objeto CuentaDTO que contiene la información de la cuenta del académico
     * @return un entero indicando el resultado de la operación (por ejemplo, 1 si la operación fue exitosa)
     * @throws ErrorDAO tipo inserción si ocurre un error durante la inserción
     */
    @Override
    public int agregarAcademicoConCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado = -1;
        try {
            Connection conexion = AdministradorBaseDatos.getInstancia();
            conexion.setAutoCommit(false);
            CallableStatement procedimientoAcademico = conexion.prepareCall(procedimientoSQL);
            setAcademicoParametros(procedimientoAcademico, academicoDTO);
            procedimientoAcademico.registerOutParameter(11, Types.INTEGER);
            resultado = procedimientoAcademico.executeUpdate();
            int idPersona = procedimientoAcademico.getInt(11);

            CallableStatement procedimientoCuenta = conexion.prepareCall("{CALL registrar_cuenta(?,?,?,?,?)}");
            cuentaDTO.setIdPersona(idPersona);
            procedimientoCuenta.setInt(1, cuentaDTO.getIdPersona());
            procedimientoCuenta.setString(2, cuentaDTO.getNombreUsuario());
            procedimientoCuenta.setString(3, cuentaDTO.getContrasena());
            procedimientoCuenta.setString(4, cuentaDTO.getTipo().
                                                toString());
            procedimientoCuenta.setString(5, cuentaDTO.getEstado().
                                                toString());
            resultado += procedimientoCuenta.executeUpdate();

            procedimientoCuenta.close();
            procedimientoAcademico.close();
            conexion.commit();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            AdministradorBaseDatos.rollback();
            throw new ErrorDAO("Error al registrar al academico junto con su cuenta", ErrorDAO.Tipo.INSERCION);
        }
        return resultado;
    }

    @Override
    public Optional<AcademicoDTO> getAcademicoPorCorreo (String correo) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_academico WHERE correoElectronico = ?";
        AcademicoDTO academico = null;
        try {
            PreparedStatement consultaAcademico = AdministradorBaseDatos.getInstancia().prepareStatement(consultaSQL);
            consultaAcademico.setString(1, correo);
            ResultSet resultado = consultaAcademico.executeQuery();

            if (resultado.next()) {
                academico = convertirAcademico(resultado);
            }

            consultaAcademico.close();
            resultado.close();
        }
        catch (SQLException error) {
            error.printStackTrace();
            BITACORA.warn(error);
            throw new ErrorDAO("Error al buscar correo", ErrorDAO.Tipo.CONSULTA);
        }
        return Optional.ofNullable(academico);
    }

    private void setAcademicoParametros (CallableStatement declaracion, AcademicoDTO academicoDTO) throws SQLException {
        declaracion.setString(1, academicoDTO.getNombre());
        declaracion.setString(2, academicoDTO.getApellidos());
        declaracion.setInt(3, academicoDTO.getIdUniversidad());
        declaracion.setString(4, academicoDTO.getCedulaProfesional());
        declaracion.setString(5, academicoDTO.getNumeroPersonal());
        declaracion.setString(6, academicoDTO.getAreaEstudios());
        declaracion.setString(7, academicoDTO.getCorreoElectronico());
        declaracion.setString(8, academicoDTO.getNumeroTelefonico());
        declaracion.setString(9, academicoDTO.getCategoriaContratacion());
        declaracion.setObject(10, academicoDTO.getIdFacultad());
    }


    private AcademicoDTO convertirAcademico (ResultSet resultado) throws SQLException {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        academicoDTO.setIdPersona(resultado.getInt("idPersona"));
        academicoDTO.setNombre(resultado.getString("nombre"));
        academicoDTO.setApellidos(resultado.getString("apellidos"));
        academicoDTO.setIdUniversidad(resultado.getInt("idUniversidad"));
        academicoDTO.setCedulaProfesional(resultado.getString("cedulaProfesional"));
        academicoDTO.setCategoriaContratacion(resultado.getString("categoriaContratacion"));
        academicoDTO.setIdFacultad((Integer) resultado.getObject("idFacultad"));
        academicoDTO.setNumeroPersonal(resultado.getString("numeroDePersonal"));
        academicoDTO.setAreaEstudios(resultado.getString("areaEstudios"));
        academicoDTO.setCorreoElectronico(resultado.getString("correoElectronico"));
        academicoDTO.setNumeroTelefonico(resultado.getString("numeroTelefonico"));
        return academicoDTO;
    }
}
