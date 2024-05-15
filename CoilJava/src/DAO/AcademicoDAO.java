package DAO;

import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicoDAO {
    private static final Logger BITACORA = Logger.getLogger(AcademicoDAO.class);

    public static List<AcademicoDTO> getListaAcademicoPorCampos (String campo, String valor) throws ErrorDAO {
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

    public static AcademicoDTO getAcademicoPorCedula (String cedula) throws ErrorDAO {
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
        return academicoDTO;
    }

    public static int agregarAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado = -1;
        try {

            CallableStatement registrarAcademico = AdministradorBaseDatos.getInstancia().
                                                                    prepareCall(procedimientoSQL);
            setAcademicoParametros(registrarAcademico, academicoDTO);
            resultado = registrarAcademico.executeUpdate();
            registrarAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al registrar al academicoDTO", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return resultado;
    }

    public static AcademicoDTO getAcademicoPorId (int id) throws ErrorDAO {
        String consulta = "SELECT * from vista_Academico WHERE idPersona = ?";
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
        return academicoDTO;
    }

    public static List<AcademicoDTO> getTodos () throws ErrorDAO {
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

    public static int editarAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        int resultado = -1;
        String procedimientoSQL = "{CALL editar_academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {

            CallableStatement editarAcademico = AdministradorBaseDatos.getInstancia().
                                                                 prepareCall(procedimientoSQL);
            setAcademicoParametros (editarAcademico, academicoDTO);
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

    public static int agregarAcademicoConCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado = -1;
        try {
            Connection conexion = AdministradorBaseDatos.getInstancia();
            conexion.setAutoCommit(false);
            CallableStatement registrarAcademico = conexion.prepareCall(procedimientoSQL);
            setAcademicoParametros(registrarAcademico, academicoDTO);
            registrarAcademico.registerOutParameter(12, Types.INTEGER);
            resultado = registrarAcademico.executeUpdate();
            int idPersona = registrarAcademico.getInt(12);

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
            throw new ErrorDAO("Error al registrar al academicoDTO junto con su cuentaDTO", ErrorDAO.Tipo.INSERCION);
        }
        return resultado;
    }

    private static void setAcademicoParametros (CallableStatement declaracion, AcademicoDTO academicoDTO) throws SQLException {
        declaracion.setString(1, academicoDTO.getNombre());
        declaracion.setString(2, academicoDTO.getApellidoPaterno());
        declaracion.setString(3, academicoDTO.getApellidoMaterno());
        declaracion.setInt(4, academicoDTO.getIdUniversidad());
        declaracion.setString(5, academicoDTO.getCedulaProfesional());
        declaracion.setString(6, academicoDTO.getNumeroPersonal());
        declaracion.setString(7, academicoDTO.getAreaEstudios());
        declaracion.setString(8, academicoDTO.getCorreoElectronico());
        declaracion.setString(9, academicoDTO.getNumeroTelefonico());
        declaracion.setString(10, academicoDTO.getCategoriaContratacion());
        declaracion.setObject(11, academicoDTO.getIdFacultad());
    }


    private static AcademicoDTO convertirAcademico (ResultSet resultado) throws SQLException {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        academicoDTO.setIdPersona(resultado.getInt("idPersona"));
        academicoDTO.setNombre(resultado.getString("nombre"));
        academicoDTO.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        academicoDTO.setApellidoMaterno(resultado.getString("apellidoMaterno"));
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
