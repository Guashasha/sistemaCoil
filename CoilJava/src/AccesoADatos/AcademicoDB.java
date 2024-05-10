package AccesoADatos;

import Logica.Dominio.*;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicoDB {
    private static final Logger BITACORA = Logger.getLogger(AcademicoDB.class);

    public static List<Academico> getListaAcademicoPorCampos (String campo, String valor) throws ErrorDAO {

        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        List<Academico> listaAcademicos = new ArrayList<>();

        try {
            CallableStatement obtenerPorCampo = ConexionBaseDatos.getInstancia().
                                                                   prepareCall(procedimientoSQL);
            obtenerPorCampo.setString(1, campo);
            obtenerPorCampo.setString(2, valor);
            ResultSet resultadoLLamada = obtenerPorCampo.executeQuery();

            while (resultadoLLamada.next()) {
                Academico academico = convertirAcademico(resultadoLLamada);
                listaAcademicos.add(academico);
            }

            obtenerPorCampo.close();
            resultadoLLamada.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("No fue posible obtener a los academicos registrados", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return listaAcademicos;
    }

    public static Academico getAcademicoPorCedula (String cedula) throws ErrorDAO {
        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        Academico academico = null;
        try {
            CallableStatement obtenerPorCampo = ConexionBaseDatos.getInstancia().
                                                                   prepareCall(procedimientoSQL);
            obtenerPorCampo.setString(1, "cedula");
            obtenerPorCampo.setString(2, cedula);
            ResultSet resultadoLLamada = obtenerPorCampo.executeQuery();

            if (resultadoLLamada.next()) {
                academico = convertirAcademico(resultadoLLamada);
            }

            obtenerPorCampo.close();
            resultadoLLamada.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener a los academicos por cedula", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return academico;
    }

    public static int agregarAcademicoUV (Academico academico) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_AcademicoUV(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado = -1;

        try {
            
            CallableStatement registrarAcademico = ConexionBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            registrarAcademico.setString(1, academico.getNombre());
            registrarAcademico.setString(2, academico.getApellidoPaterno());
            registrarAcademico.setString(3, academico.getApellidoMaterno());
            registrarAcademico.setInt(4, academico.getIdUniversidad());
            registrarAcademico.setString(5, academico.getCedulaProfesional());
            registrarAcademico.setString(6, academico.getNumeroPersonal());
            registrarAcademico.setString(7, academico.getAreaEstudios());
            registrarAcademico.setString(8, academico.getCorreoElectronico());
            registrarAcademico.setString(9, academico.getNumeroTelefonico());
            registrarAcademico.setString(10, academico.getCategoriaContratacion());
            registrarAcademico.setInt(11, academico.getIdFacultad());
            resultado = registrarAcademico.executeUpdate();
            registrarAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al registrar al academico", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return resultado;
    }

    public static int agregarAcademicoExterno (Academico academico) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_AcademicoExterno(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado = -1;
        try {
            
            CallableStatement registrarAcademico = ConexionBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            registrarAcademico.setString(1, academico.getNombre());
            registrarAcademico.setString(2, academico.getApellidoPaterno());
            registrarAcademico.setString(3, academico.getApellidoMaterno());
            registrarAcademico.setInt(4, academico.getIdUniversidad());
            registrarAcademico.setString(5, academico.getCedulaProfesional());
            registrarAcademico.setString(6, academico.getNumeroPersonal());
            registrarAcademico.setString(7, academico.getAreaEstudios());
            registrarAcademico.setString(8, academico.getCorreoElectronico());
            registrarAcademico.setString(9, academico.getNumeroTelefonico());

            resultado = registrarAcademico.executeUpdate();
            registrarAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al agregar al academico externo", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return resultado;
    }

    public static Academico getAcademicoPorId (int id) throws ErrorDAO {
        String consulta = "SELECT * from vista_Academico WHERE idPersona = ?";
        Academico academico = null;
        try {
            PreparedStatement consultarAcademico = ConexionBaseDatos.getInstancia().
                                                                      prepareStatement(consulta);
            consultarAcademico.setInt(1, id);
            ResultSet resultadoConsulta = consultarAcademico.executeQuery();
            if (resultadoConsulta.next()) {
                academico = convertirAcademico(resultadoConsulta);
            }
            consultarAcademico.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener a un académico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return academico;
    }

    public static List<Academico> getTodos () throws ErrorDAO {
        List<Academico> listaAcademicos = new ArrayList<>();
        String consulta = "SELECT * FROM vista_academico";

        try {
            PreparedStatement consultaAcademico = ConexionBaseDatos.getInstancia().
                                                                     prepareStatement(consulta);
            ResultSet resultadoConsulta = consultaAcademico.executeQuery();
            while (resultadoConsulta.next()) {
                Academico academico = convertirAcademico(resultadoConsulta);
                listaAcademicos.add(academico);
            }
            consultaAcademico.close();
            resultadoConsulta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener a todos los académicos registrados", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return listaAcademicos;
    }

    public static int editarAcademico (Academico academico) throws ErrorDAO {
        int resultado = -1;
        String procedimientoSQL = "{CALL editar_academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            
            CallableStatement editarAcademico = ConexionBaseDatos.getInstancia().
                                                                   prepareCall(procedimientoSQL);
            editarAcademico.setString(1, academico.getNombre());
            editarAcademico.setString(2, academico.getApellidoPaterno());
            editarAcademico.setString(3, academico.getApellidoMaterno());
            editarAcademico.setInt(4, academico.getIdUniversidad());
            editarAcademico.setString(5, academico.getCedulaProfesional());
            editarAcademico.setString(6, academico.getNumeroPersonal());
            editarAcademico.setString(7, academico.getAreaEstudios());
            editarAcademico.setString(8, academico.getCorreoElectronico());
            editarAcademico.setString(9, academico.getNumeroTelefonico());
            editarAcademico.setString(10, academico.getCategoriaContratacion());
            editarAcademico.setInt(11, academico.getIdFacultad());
            resultado = editarAcademico.executeUpdate();
            editarAcademico.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error en la modificación del académico", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return resultado;
    }

    private static Academico convertirAcademico(ResultSet resultado) throws SQLException {
        Academico academico = new Academico();

        academico.setIdPersona(resultado.getInt("idPersona"));
        academico.setNombre(resultado.getString("nombre"));
        academico.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        academico.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        academico.setIdUniversidad(resultado.getInt("idUniversidad"));
        academico.setCedulaProfesional(resultado.getString("cedulaProfesional"));

        if (resultado.getString("categoriaContratacion") != null) {
            academico.setCategoriaContratacion(resultado.getString("categoriaContratacion"));
        }
        if (resultado.getObject("idFacultad") != null) {
            academico.setIdFacultad(resultado.getInt("idFacultad"));
        }
        academico.setNumeroPersonal(resultado.getString("numeroDePersonal"));
        academico.setAreaEstudios(resultado.getString("areaEstudios"));
        academico.setCorreoElectronico(resultado.getString("correoElectronico"));
        academico.setNumeroTelefonico(resultado.getString("numeroTelefonico"));
        return academico;
    }
}
