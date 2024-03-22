package AccesoADatos;

import Logica.Dominio.Academico;
import Logica.Dominio.Facultad;
import Logica.Dominio.Region;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcademicoBD {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static List<Academico> getListaAcademicoPorCampos (String campo, String valor) {
        List<Academico> listaAcademicos = new ArrayList<>();
        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
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


            CONEXION_BASE_DATOS.desconectar();
            obtenerPorCampo.close();
            resultadoLLamada.close();
        }
        catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }
        return listaAcademicos;
    }

    public static Academico getAcademicoPorCampo (String campo, String valor) {
        String procedimientoSQL = "{CALL obtener_academicos_campos(?,?)}";
        Academico academico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement obtenerPorCampo = CONEXION_BASE_DATOS.getConexion().
                                                                   prepareCall(procedimientoSQL);
            obtenerPorCampo.setString(1, campo);
            obtenerPorCampo.setString(2, valor);
            ResultSet resultadoLLamada = obtenerPorCampo.executeQuery();

            if (resultadoLLamada.next()) {
                academico = convertirAcademico(resultadoLLamada);
            }

            obtenerPorCampo.close();
            CONEXION_BASE_DATOS.desconectar();
            resultadoLLamada.close();
        }
        catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }
        return academico;
    }

    public static int agregarAcademico (Academico academico) {
        String procedimientoSQL = "{CALL registrar_Academico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int resultado;
        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement registrarAcademico = CONEXION_BASE_DATOS.getConexion().
                                                                      prepareCall(procedimientoSQL);
            registrarAcademico.setString(1, academico.getNombre());
            registrarAcademico.setString(2, academico.getApellidoPaterno());
            registrarAcademico.setString(3, academico.getApellidoMaterno());
            registrarAcademico.setInt(4, academico.getUniversidad().
                                                  getId());
            registrarAcademico.setString(5, academico.getCedulaProfesional());
            registrarAcademico.setString(6, academico.getNumeroPersonal());
            registrarAcademico.setString(7, academico.getAreaEstudios());
            registrarAcademico.setString(8, academico.getCorreoElectronico());
            registrarAcademico.setString(9, academico.getNumeroTelefonico());
            registrarAcademico.setString(10, academico.getCategoriaContratacion());
            //registrarAcademico.setInt(11, academico.getFacultad().getId);
            resultado = registrarAcademico.executeUpdate();
            registrarAcademico.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }
        return resultado;
    }
    public static Academico getAcademicoPorId (int id) {
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
            CONEXION_BASE_DATOS.desconectar();
            resultadoConsulta.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return academico;
    }

    public static List<Academico> getTodos () {
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
            CONEXION_BASE_DATOS.desconectar();
            consultaAcademico.close();
            resultadoConsulta.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaAcademicos;

    }


    private static Academico convertirAcademico (ResultSet resultado) throws SQLException {

        Academico academico = new Academico();
        academico.setIdPersona(resultado.getInt("idPersona"));
        academico.setNombre(resultado.getString("nombre"));
        academico.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        academico.setApellidoMaterno(resultado.getString("apellidoMaterno"));

        Universidad universidad = new Universidad();
        universidad.setId(resultado.getInt("idUniversidad"));
        universidad.setNombre(resultado.getString("nombreUniversidad"));
        universidad.setPaisOrigen(resultado.getString("paisOrigen"));

        academico.setUniversidad(universidad);
        academico.setCategoriaContratacion(resultado.getString("cedulaProfesional"));
        academico.setNumeroPersonal(resultado.getString("numeroDePersonal"));
        academico.setAreaEstudios(resultado.getString("areaEstudios"));
        academico.setCorreoElectronico(resultado.getString("correoElectronico"));
        academico.setNumeroTelefonico(resultado.getString("numeroTelefonico"));
        academico.setCategoriaContratacion(resultado.getString("categoriaContratacion"));

        Region region = new Region();
        region.setNombre(resultado.getString("nombreRegion"));

        Facultad facultad = new Facultad();
        //facultad.setIdFacultad(resultado.getString("idFacultad"));
        facultad.setNombre(resultado.getString("nombreFacultad"));
        facultad.setRegion(region);

        academico.setFacultad(facultad);

        return academico;
    }
}
