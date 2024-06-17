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
            CallableStatement obtenerPorCampo = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            obtenerPorCampo.setString(1, campo);
            obtenerPorCampo.setString(2, valor);
            ResultSet resultadoLLamada = obtenerPorCampo.executeQuery();

            while (resultadoLLamada.next()) {
                AcademicoDTO academicoDTO = convertirAcademico(resultadoLLamada);
                listaAcademicoDTOS.add(academicoDTO);
            }
            obtenerPorCampo.close();
            resultadoLLamada.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
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
            CallableStatement obtenerPorCampo = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            obtenerPorCampo.setString(1, "cedula");
            obtenerPorCampo.setString(2, cedula);
            ResultSet resultadoLLamada = obtenerPorCampo.executeQuery();

            if (resultadoLLamada.next()) {
                academicoDTO = convertirAcademico(resultadoLLamada);
            }

            obtenerPorCampo.close();
            resultadoLLamada.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
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
        int resultado = -1;
        try {
            CallableStatement registrarAcademico = AdministradorBaseDatos.getInstancia().
                                                                         prepareCall(procedimientoSQL);
            setAcademicoParametros(registrarAcademico, academicoDTO);
            registrarAcademico.registerOutParameter(11, Types.INTEGER);
            resultado = registrarAcademico.executeUpdate();
            registrarAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al registrar al academico", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return resultado;
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
        String consulta = "SELECT * from vista_academico WHERE idPersona = ?";
        AcademicoDTO academicoDTO = null;
        try {
            PreparedStatement consultarAcademico = AdministradorBaseDatos.getInstancia().
                                                                         prepareStatement(consulta);
            consultarAcademico.setInt(1, id);
            ResultSet resultadoConsulta = consultarAcademico.executeQuery();
            if (resultadoConsulta.next()) {
                academicoDTO = convertirAcademico(resultadoConsulta);
            }
            consultarAcademico.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
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
        List<AcademicoDTO> listaAcademicoDTOS = new ArrayList<>();
        String consulta = "SELECT * FROM vista_academico";

        try {
            PreparedStatement consultaAcademico = AdministradorBaseDatos.getInstancia().
                                                                        prepareStatement(consulta);
            ResultSet resultadoConsulta = consultaAcademico.executeQuery();
            while (resultadoConsulta.next()) {
                AcademicoDTO academicoDTO = convertirAcademico(resultadoConsulta);
                listaAcademicoDTOS.add(academicoDTO);
            }
            consultaAcademico.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
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
        int resultado;
        String procedimientoSQL = "{CALL editar_academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            CallableStatement editarAcademico = AdministradorBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            setAcademicoParametros(editarAcademico, academicoDTO);

            resultado = editarAcademico.executeUpdate();

            editarAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error en la modificación del académico", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return resultado;
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
            CallableStatement registrarAcademico = conexion.prepareCall(procedimientoSQL);
            setAcademicoParametros(registrarAcademico, academicoDTO);
            registrarAcademico.registerOutParameter(11, Types.INTEGER);
            resultado = registrarAcademico.executeUpdate();
            int idPersona = registrarAcademico.getInt(11);

            CallableStatement agregarCuenta = conexion.prepareCall("{CALL registrar_cuenta(?,?,?,?,?)}");
            cuentaDTO.setIdPersona(idPersona);
            agregarCuenta.setInt(1, cuentaDTO.getIdPersona());
            agregarCuenta.setString(2, cuentaDTO.getNombreUsuario());
            agregarCuenta.setString(3, cuentaDTO.getContrasena());
            agregarCuenta.setString(4, cuentaDTO.getTipo().
                                                toString());
            agregarCuenta.setString(5, cuentaDTO.getEstado().
                                                toString());
            resultado += agregarCuenta.executeUpdate();

            agregarCuenta.close();
            registrarAcademico.close();
            conexion.commit();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            AdministradorBaseDatos.rollback();
            throw new ErrorDAO("Error al registrar al academico junto con su cuenta", ErrorDAO.Tipo.INSERCION);
        }
        return resultado;
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
