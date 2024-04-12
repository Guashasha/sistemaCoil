package AccesoADatos;

import Logica.Dominio.*;
import Logica.ErrorDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicoDB {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static List<Academico> getListaAcademicoPorCampos (String campo, String valor) throws SQLException {

        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        List<Academico> listaAcademicos = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement obtenerPorCampo = CONEXION_BASE_DATOS.getConexion().
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
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return listaAcademicos;
    }

    public static Academico getAcademicoPorCedula (String cedula) throws SQLException {
        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        Academico academico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement obtenerPorCampo = CONEXION_BASE_DATOS.getConexion().
                                                                   prepareCall(procedimientoSQL);
            obtenerPorCampo.setString(1, "cedula");
            obtenerPorCampo.setString(2, cedula);
            ResultSet resultadoLLamada = obtenerPorCampo.executeQuery();

            if (resultadoLLamada.next()) {
                academico = convertirAcademico(resultadoLLamada);
            }

            obtenerPorCampo.close();
            CONEXION_BASE_DATOS.desconectar();
            resultadoLLamada.close();
        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return academico;
    }

    public static int agregarAcademico (Academico academico) throws SQLException {
        String procedimientoSQL = "{CALL registrar_Academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado = -1;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement registrarAcademico = CONEXION_BASE_DATOS.getConexion().
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
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return resultado;
    }

    public static Academico getAcademicoPorId (int id) throws SQLException {
        String consulta = "SELECT * from vista_Academico WHERE idPersona = ?";
        Academico academico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultarAcademico = CONEXION_BASE_DATOS.getConexion().
                                                                      prepareStatement(consulta);
            consultarAcademico.setInt(1,id);
            ResultSet resultadoConsulta = consultarAcademico.executeQuery();
            if (resultadoConsulta.next()) {
                academico = convertirAcademico(resultadoConsulta);
            }
            consultarAcademico.close();
            resultadoConsulta.close();
        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return academico;
    }

    public static List<Academico> getTodos () throws SQLException {
        List<Academico> listaAcademicos = new ArrayList<>();
        String consulta = "SELECT * FROM vista_academico";

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaAcademico = CONEXION_BASE_DATOS.getConexion().
                                                                     prepareStatement(consulta);
            ResultSet resultadoConsulta = consultaAcademico.executeQuery();
            while (resultadoConsulta.next()) {
                Academico academico = convertirAcademico(resultadoConsulta);
                listaAcademicos.add(academico);
            }
            consultaAcademico.close();
            resultadoConsulta.close();
        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return listaAcademicos;

    }


    public static int editarAcademico (Academico academico) throws SQLException {
        int resultado = -1;
        String procedimientoSQL = "{CALL editar_academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement editarAcademico = CONEXION_BASE_DATOS.getConexion().
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
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return resultado;
    }


    private static Academico convertirAcademico (ResultSet resultado) throws SQLException {

        Academico academico = new Academico();

        academico.setIdPersona(resultado.getInt("idPersona"));
        academico.setNombre(resultado.getString("nombre"));
        academico.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        academico.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        academico.setIdUniversidad(resultado.getInt("idUniversidad"));
        academico.setCategoriaContratacion(resultado.getString("cedulaProfesional"));
        academico.setNumeroPersonal(resultado.getString("numeroDePersonal"));
        academico.setAreaEstudios(resultado.getString("areaEstudios"));
        academico.setCorreoElectronico(resultado.getString("correoElectronico"));
        academico.setNumeroTelefonico(resultado.getString("numeroTelefonico"));
        academico.setCategoriaContratacion(resultado.getString("categoriaContratacion"));
        academico.setIdFacultad(resultado.getInt("idFacultad"));

        return academico;
    }

}
