package AccesoADatos;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColaboracionDB {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static Colaboracion getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws SQLException {
        String getColaboracionPorAcademicoSQL = "{CALL obtener_colaboracion_academicos(?,?)}";
        Colaboracion colaboracion = null;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement getColaboracionPorAcademico = CONEXION_BASE_DATOS.getConexion().
                                                                               prepareCall(getColaboracionPorAcademicoSQL);
            getColaboracionPorAcademico.setString(1, academico1.getCedulaProfesional());
            getColaboracionPorAcademico.setString(2, academico2.getCedulaProfesional());

            ResultSet resultadoGetColaboracionPorAcademico = getColaboracionPorAcademico.executeQuery();

            if (resultadoGetColaboracionPorAcademico.next()) {
                colaboracion = convertirColaboracion(resultadoGetColaboracionPorAcademico);
            }

            resultadoGetColaboracionPorAcademico.close();
            getColaboracionPorAcademico.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return colaboracion;
    }

    public static Colaboracion getColaboracionPorId (int idColaboracion) throws SQLException {
        String colaboracionPorIdSQL = "SELECT * from colaboracion WHERE idColaboracion = ?";
        Colaboracion colaboracion = null;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement colaboracionPorId = CONEXION_BASE_DATOS.getConexion()
                                                                     .prepareStatement
                                                                             (colaboracionPorIdSQL);

            colaboracionPorId.setInt(1, idColaboracion);

            ResultSet resultadoColaboracionPorId = colaboracionPorId.executeQuery();

            if (resultadoColaboracionPorId.next()) {
                colaboracion = convertirColaboracion(resultadoColaboracionPorId);
            }

            resultadoColaboracionPorId.close();
            colaboracionPorId.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return colaboracion;

    }

    public static List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws SQLException {
        String listaDeEstudiantesSQL = "{CALL obtener_estudiantes_colaboracion(?)}";
        List<Estudiante> listaEstudiantes = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement procedimientoListaDeEstudiantes = CONEXION_BASE_DATOS.getConexion().
                                                                                   prepareCall(listaDeEstudiantesSQL);
            procedimientoListaDeEstudiantes.setInt(1, colaboracion.getIdColaboracion());
            ResultSet resultadoListaDeEstudiantes = procedimientoListaDeEstudiantes.executeQuery();

            while (resultadoListaDeEstudiantes.next()) {
                Estudiante estudiante = convertirEstudiante(resultadoListaDeEstudiantes);
                listaEstudiantes.add(estudiante);
            }

            resultadoListaDeEstudiantes.close();
            procedimientoListaDeEstudiantes.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return listaEstudiantes;
    }

    public static List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws SQLException {
        String academicosParticipantes = "{CALL obtener_academicos_colaboracion(?)}";
        List<Academico> listaAcademicos = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement procedimientoAcademicosParticipantes = CONEXION_BASE_DATOS.getConexion().
                                                                                        prepareCall(academicosParticipantes);
            procedimientoAcademicosParticipantes.setInt(1, colaboracion.getIdColaboracion());

            ResultSet resultadoAcademicosParticipantes = procedimientoAcademicosParticipantes.executeQuery();

            while (resultadoAcademicosParticipantes.next()) {
                Academico academico = convertirAcademico(resultadoAcademicosParticipantes);
                listaAcademicos.add(academico);
            }

            resultadoAcademicosParticipantes.close();
            procedimientoAcademicosParticipantes.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return listaAcademicos;

    }

    public static List<Colaboracion> getColaboracionPorPeriodo (Periodo periodo) throws SQLException {
        String colaboracionPorPeriodoSQL = "SELECT * from colaboracion WHERE fechaInicio = ? AND fechaFin = ?";
        List<Colaboracion> listaColaboracion = new ArrayList<>();
        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement colaboracionPorPeriodo = CONEXION_BASE_DATOS.getConexion().
                                                                          prepareStatement(colaboracionPorPeriodoSQL);
            colaboracionPorPeriodo.setDate(1, Date.valueOf(periodo.getFechaInicio()));
            colaboracionPorPeriodo.setDate(2, Date.valueOf(periodo.getFechaFin()));

            ResultSet resultadoColaboracionPorPeriodo = colaboracionPorPeriodo.executeQuery();

            while (resultadoColaboracionPorPeriodo.next()) {
                Colaboracion colaboracion = convertirColaboracion(resultadoColaboracionPorPeriodo);
                listaColaboracion.add(colaboracion);
            }

            colaboracionPorPeriodo.close();
            resultadoColaboracionPorPeriodo.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return listaColaboracion;

    }
    public static List<Colaboracion> getColaboracionPorIdioma (String idioma) throws SQLException {
        String colaboracionPorIdiomaSQL = "SELECT * FROM colaboracion WHERE idioma = ?";
        List<Colaboracion> listaColaboracion = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement colaboracionPorIdioma = CONEXION_BASE_DATOS.getConexion().
                                                                        prepareStatement(colaboracionPorIdiomaSQL);
            colaboracionPorIdioma.setString(1, idioma);

            ResultSet resultadoColaboracionPorIdioma = colaboracionPorIdioma.executeQuery();

            while (resultadoColaboracionPorIdioma.next()) {
                Colaboracion colaboracion = convertirColaboracion(resultadoColaboracionPorIdioma);
                listaColaboracion.add(colaboracion);
            }
            colaboracionPorIdioma.close();
            resultadoColaboracionPorIdioma.close();
        }
        finally {
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaColaboracion;
    }

    public static List<Colaboracion> getColaboracionPorEstado (String estado) throws SQLException {
        String colaboracionPorEstadoSQL = "SELECT * FROM colaboracion WHERE estado = ?";
        List<Colaboracion> listaColaboraciones = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement colaboracionPorEstado = CONEXION_BASE_DATOS.getConexion().
                                                                         prepareStatement(colaboracionPorEstadoSQL);
            colaboracionPorEstado.setString(1, estado);

            ResultSet resultadoColaboracionPorEstado = colaboracionPorEstado.executeQuery();

            while (resultadoColaboracionPorEstado.next()) {
                Colaboracion colaboracion = convertirColaboracion(resultadoColaboracionPorEstado);
                listaColaboraciones.add(colaboracion);
            }
            resultadoColaboracionPorEstado.close();
            colaboracionPorEstado.close();
        }
        finally {
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaColaboraciones;
    }

    public static int cambiarEstadoColaboracion (Colaboracion colaboracion) throws SQLException{
        String cambiarEstadoColaboracionSQL = "UPDATE colaboracion SET estado = ? WHERE idColaboracion = ?";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement cambiarEstadoColaboracion = CONEXION_BASE_DATOS.getConexion().
                                                                             prepareStatement(cambiarEstadoColaboracionSQL);
            cambiarEstadoColaboracion.setString(1, colaboracion.getEstado().
                                                               name().toLowerCase());

            cambiarEstadoColaboracion.setInt(2, colaboracion.getIdColaboracion());

            filasAfectadas = cambiarEstadoColaboracion.executeUpdate();

            cambiarEstadoColaboracion.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;

    }


    public static int agregarEstudianteAColaboracion (Colaboracion colaboracion, Estudiante estudiante) throws SQLException {
        String agregarEstudianteAColaboracionSQL = "INSERT INTO estudiantescolaboracion (idEstudiante, idColaboracion) VALUES (?, ?)";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement agregarEstudianteAColaboracion = CONEXION_BASE_DATOS.getConexion().
                                                                                  prepareStatement(agregarEstudianteAColaboracionSQL);
            agregarEstudianteAColaboracion.setInt(1, estudiante.getIdEstudiante());
            agregarEstudianteAColaboracion.setInt(2, colaboracion.getIdColaboracion());

            filasAfectadas = agregarEstudianteAColaboracion.executeUpdate();

            agregarEstudianteAColaboracion.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;

    }

    public static int agregarAcademicoAColaboracion (Colaboracion colaboracion, Academico academico) throws SQLException {
        String agregarAcademicoAColaboracionSQL = "INSERT INTO academicodesarrolla (idColaboracion, idAcademico) VALUES (?, ?)";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement agregarAcademicoAColaboracion = CONEXION_BASE_DATOS.getConexion().
                                                                                 prepareStatement(agregarAcademicoAColaboracionSQL);
            agregarAcademicoAColaboracion.setInt(1, colaboracion.getIdColaboracion());
            agregarAcademicoAColaboracion.setString(2, academico.getCedulaProfesional());

            filasAfectadas = agregarAcademicoAColaboracion.executeUpdate();

            agregarAcademicoAColaboracion.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;
    }

    public static int registrarColaboracion (Colaboracion colaboracion) throws SQLException {
        String registrarColaboracionSQL = "{CALL registrar_Colaboracion(?,?,?,?,?,?,?,?)}";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement procedimientoRegistrarColaboracion = CONEXION_BASE_DATOS.getConexion().
                                                                                      prepareCall(registrarColaboracionSQL);
            procedimientoRegistrarColaboracion.setString(1, colaboracion.getEstado()
                                                                        .name());
            procedimientoRegistrarColaboracion.setString(2, colaboracion.getTipo()
                                                                        .name());
            procedimientoRegistrarColaboracion.setString(3, colaboracion.getTemaInteres());
            procedimientoRegistrarColaboracion.setString(4, colaboracion.getIdioma());
            procedimientoRegistrarColaboracion.setString(5, colaboracion.getObjetivo());
            procedimientoRegistrarColaboracion.setDate(6, Date.valueOf(colaboracion.getPeriodo().
                                                                      getFechaInicio()));
            procedimientoRegistrarColaboracion.setDate(7, Date.valueOf(colaboracion.getPeriodo().
                                                                      getFechaFin()));
            procedimientoRegistrarColaboracion.setString(8, colaboracion.getPerfilEstudiante());

            filasAfectadas = procedimientoRegistrarColaboracion.executeUpdate();

            procedimientoRegistrarColaboracion.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;

    }

    public static int actualizarColaboracion (Colaboracion colaboracion) throws SQLException {
        String actualizarColaboracionSQL = "{CALL actualizar_Colaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement procedimientoActualizarColaboracion = CONEXION_BASE_DATOS.getConexion()
                                                                                       .prepareCall(actualizarColaboracionSQL);

            procedimientoActualizarColaboracion.setInt(1, colaboracion.getIdColaboracion());
            procedimientoActualizarColaboracion.setString(2, colaboracion.getEstado()
                                                                         .name());
            procedimientoActualizarColaboracion.setString(3, colaboracion.getTipo()
                                                                         .name());
            procedimientoActualizarColaboracion.setString(4, colaboracion.getTemaInteres());
            procedimientoActualizarColaboracion.setString(5, colaboracion.getIdioma());
            procedimientoActualizarColaboracion.setString(6, colaboracion.getObjetivo());
            procedimientoActualizarColaboracion.setDate(7, Date.valueOf(colaboracion.getPeriodo().
                                                                       getFechaInicio()));
            procedimientoActualizarColaboracion.setDate(8, Date.valueOf(colaboracion.getPeriodo().
                                                                       getFechaFin()));
            procedimientoActualizarColaboracion.setString(9, colaboracion.getPerfilEstudiante());

            filasAfectadas = procedimientoActualizarColaboracion.executeUpdate();

            procedimientoActualizarColaboracion.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;
    }

    public static Colaboracion getPorId (int id) throws SQLException {
        String getPorIdSQL = "SELECT * FROM colaboracion WHERE idColaboraion = ?";
        Colaboracion colaboracion = null;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement getPorId = CONEXION_BASE_DATOS.getConexion().
                                                            prepareStatement(getPorIdSQL);
            getPorId.setInt(1, id);

            ResultSet resultadoGetPorId = getPorId.executeQuery();

            if (resultadoGetPorId.next()) {
                colaboracion = convertirColaboracion(resultadoGetPorId);
            }
            getPorId.close();
            resultadoGetPorId.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return colaboracion;

    }

    public static List<Colaboracion> getTodos () throws SQLException {
        String getTodosSQL = "SELECT * FROM colaboracion";
        List<Colaboracion> listaColaboracion = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement getTodos = CONEXION_BASE_DATOS.getConexion().
                                                            prepareStatement(getTodosSQL);
            ResultSet resultadoGetTodos = getTodos.executeQuery();

            while (resultadoGetTodos.next()) {
                Colaboracion colaboracion = convertirColaboracion(resultadoGetTodos);
                listaColaboracion.add(colaboracion);
            }

            resultadoGetTodos.close();
            getTodos.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return listaColaboracion;

    }


    private static Colaboracion convertirColaboracion (ResultSet resultado) throws SQLException {
        Colaboracion colaboracion = new Colaboracion();

        colaboracion.setIdColaboracion(resultado.getInt("idColaboracion"));

        String estado = resultado.getString("estado");

        Colaboracion.EstadoColaboracion estadoColaboracion = Colaboracion.EstadoColaboracion.
                valueOf(estado);
        colaboracion.setEstado(estadoColaboracion);

        String tipo = resultado.getString("tipo");

        Colaboracion.TipoColaboracion tipoColaboracion = Colaboracion.TipoColaboracion.
                valueOf(tipo);

        colaboracion.setTipo(tipoColaboracion);
        colaboracion.setTemaInteres(resultado.getString("temaInteres"));
        colaboracion.setIdioma(resultado.getString("idioma"));
        colaboracion.setObjetivo(resultado.getString("objetivo"));

        Periodo periodo = new Periodo();
        periodo.setFechaInicio(resultado.getDate("fechaInicio").
                                        toLocalDate());
        periodo.setFechaFin(resultado.getDate("fechaFin").
                                     toLocalDate());

        colaboracion.setPeriodo(periodo);
        colaboracion.setPerfilEstudiante(resultado.getString("perfilEstudiante"));

        return colaboracion;
    }

    //todo

    public Colaboracion getActivaPorAcademico (Academico academico) throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        return colaboracion;
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

    private static Periodo convertirPeriodo (ResultSet resultSet) throws SQLException {
        Periodo periodo = new Periodo();

        periodo.setFechaInicio(resultSet.getDate("fechaInicio").
                                        toLocalDate());
        periodo.setFechaFin(resultSet.getDate("fechaFin").
                                     toLocalDate());

        return periodo;
    }

}
