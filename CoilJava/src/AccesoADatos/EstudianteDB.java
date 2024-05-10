package AccesoADatos;

import Logica.Dominio.Estudiante;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDB {
    public static int agregarEstudiante (Estudiante estudiante) throws SQLException {
        String procedimientoSQL = "{CALL registrar_Estudiante(?, ?, ?, ?, ?)}";
        int resultado = 0;

        try {
            
            CallableStatement registrarEstudiante = ConexionBaseDatos.getInstancia().
                                                                      prepareCall(procedimientoSQL);
            registrarEstudiante.setString(1, estudiante.getNombre());
            registrarEstudiante.setString(2, estudiante.getApellidoPaterno());
            registrarEstudiante.setString(3, estudiante.getApellidoMaterno());
            registrarEstudiante.setInt(4, estudiante.getIdUniversidad());
            registrarEstudiante.setString(5, estudiante.getMatricula());


            resultado = registrarEstudiante.executeUpdate();
            registrarEstudiante.close();

        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return resultado;
    }

    public static int editarEstudiante (Estudiante estudiante) throws SQLException {
        String procedimientoSQL = "{CALL editar_Estudiante(?, ?, ?, ?, ?)}";
        int resultado = 0;

        try {
            
            CallableStatement editarEstudiante = ConexionBaseDatos.getInstancia().
                                                                       prepareCall(procedimientoSQL);
            editarEstudiante.setString(1, estudiante.getNombre());
            editarEstudiante.setString(2, estudiante.getApellidoPaterno());
            editarEstudiante.setString(3, estudiante.getApellidoMaterno());
            editarEstudiante.setString(4, estudiante.getMatricula());
            editarEstudiante.setInt(5, estudiante.getIdUniversidad());

            resultado = editarEstudiante.executeUpdate();
            editarEstudiante.close();

        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return resultado;

    }

    public static Estudiante getPorId (int id) throws SQLException {
        String consulta = "SELECT * from vista_estudiante WHERE idEstudiante = ?";
        Estudiante estudiante = null;

        try {
            
            PreparedStatement consultaEstudianteId = ConexionBaseDatos.getInstancia().
                                                                      prepareStatement(consulta);
            consultaEstudianteId.setInt(1,id);
            ResultSet resultadoConsulta = consultaEstudianteId.executeQuery();
            if (resultadoConsulta.next()) {
                estudiante = convertirEstudiante(resultadoConsulta);
            }
            consultaEstudianteId.close();
            resultadoConsulta.close();
        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return estudiante;

    }

    public static Estudiante getEstudiantePorIdPersona (int idPersona) throws SQLException {
        String consulta = "SELECT * from vista_estudiante WHERE idPersona = ?";
        Estudiante estudiante = null;

        try {
            
            PreparedStatement cosnsultaEstudianteIdPersona = ConexionBaseDatos.getInstancia().
                                                                        prepareStatement(consulta);
            cosnsultaEstudianteIdPersona.setInt(1,idPersona);
            ResultSet resultadoConsulta = cosnsultaEstudianteIdPersona.executeQuery();
            if (resultadoConsulta.next()) {
                estudiante = convertirEstudiante(resultadoConsulta);
            }
            cosnsultaEstudianteIdPersona.close();
            resultadoConsulta.close();

        }
        finally {
            ConexionBaseDatos.desconectar();

        }

        return estudiante;

    }

    public static Estudiante getEstudiantePorMatricula (String matricula) throws SQLException {
        String consulta = "SELECT * from vista_estudiante WHERE matricula = ?";
        Estudiante estudiante = null;

        try {
            
            PreparedStatement cosnsultaEstudianteMatricula = ConexionBaseDatos.getInstancia().
                                                                                prepareStatement(consulta);
            cosnsultaEstudianteMatricula.setString(1,matricula);
            ResultSet resultadoConsulta = cosnsultaEstudianteMatricula.executeQuery();
            if (resultadoConsulta.next()) {
                estudiante = convertirEstudiante(resultadoConsulta);
            }
            cosnsultaEstudianteMatricula.close();
            resultadoConsulta.close();
        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return estudiante;

    }

    public static List<Estudiante> getEstudiantePorUniversidad (int idUniversidad) throws SQLException {
        String consulta = "SELECT * from vista_estudiante WHERE universidad = ?";
        ArrayList<Estudiante> listaEstudiantes = new ArrayList<>();

        try {
            
            PreparedStatement cosnsultaEstudianteUniversidad = ConexionBaseDatos.getInstancia().
                                                                                prepareStatement(consulta);
            cosnsultaEstudianteUniversidad.setInt(1,idUniversidad);
            ResultSet resultadoConsulta = cosnsultaEstudianteUniversidad.executeQuery();

            while (resultadoConsulta.next()) {
                Estudiante estudiante = convertirEstudiante(resultadoConsulta);
                listaEstudiantes.add(estudiante);
            }

            cosnsultaEstudianteUniversidad.close();
            resultadoConsulta.close();

        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return listaEstudiantes;

    }

    public static List<Estudiante> getTodos () throws SQLException {
        String consulta = "SELECT * FROM vista_estudiante";
        List<Estudiante> listaEstudiantes = new ArrayList<>();

        try {
            
            PreparedStatement consultaEstudiante = ConexionBaseDatos.getInstancia().
                                                                     prepareStatement(consulta);
            ResultSet resultadoConsulta = consultaEstudiante.executeQuery();
            while (resultadoConsulta.next()) {
                Estudiante estudiante = convertirEstudiante(resultadoConsulta);
                listaEstudiantes.add(estudiante);
            }
            consultaEstudiante.close();
            resultadoConsulta.close();
        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return listaEstudiantes;


    }

    private static Estudiante convertirEstudiante (ResultSet resultado) throws SQLException {

        Estudiante estudiante = new Estudiante();
        estudiante.setIdPersona(resultado.getInt("idPersona"));
        estudiante.setNombre(resultado.getString("nombre"));
        estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
        estudiante.setMatricula(resultado.getString("matricula"));
        estudiante.setIdUniversidad(resultado.getInt("universidad"));

        return estudiante;
    }


}
