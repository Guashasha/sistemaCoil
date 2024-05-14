package AccesoADatos;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColaboracionDB {
    private static final Logger BITACORA = Logger.getLogger(ColaboracionDB.class);

    public static Colaboracion getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO {
        String getColaboracionPorAcademicoSQL = "{CALL obtener_colaboracion_academicos(?,?)}";
        Colaboracion colaboracion = null;

        try {
            CallableStatement getColaboracionPorAcademico = ConexionBaseDatos.getInstancia().
                                                                             prepareCall(getColaboracionPorAcademicoSQL);
            getColaboracionPorAcademico.setString(1, academico1.getCedulaProfesional());
            getColaboracionPorAcademico.setString(2, academico2.getCedulaProfesional());

            ResultSet resultadoGetColaboracionPorAcademico = getColaboracionPorAcademico.executeQuery();

            if (resultadoGetColaboracionPorAcademico.next()) {
                colaboracion = convertirColaboracion(resultadoGetColaboracionPorAcademico);
                colaboracion.setAnfitrion(convertirAcademico(resultadoGetColaboracionPorAcademico));

                if (resultadoGetColaboracionPorAcademico.next()) {
                    colaboracion.setAcademicoPar(convertirAcademico(resultadoGetColaboracionPorAcademico));
                }
            }
            resultadoGetColaboracionPorAcademico.close();
            getColaboracionPorAcademico.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();

        }
        return colaboracion;
    }

    public static Colaboracion getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        String colaboracionPorIdSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idColaboracion = ?""";
        Colaboracion colaboracion = null;
        try {
            PreparedStatement colaboracionPorId = ConexionBaseDatos.getInstancia()
                                                                   .prepareStatement(colaboracionPorIdSQL);

            colaboracionPorId.setInt(1, idColaboracion);
            ResultSet resultadoColaboracionPorId = colaboracionPorId.executeQuery();
            if (resultadoColaboracionPorId.next()) {
                colaboracion = convertirColaboracion(resultadoColaboracionPorId);
                colaboracion.setAnfitrion(convertirAcademico(resultadoColaboracionPorId));

                if (resultadoColaboracionPorId.next()) {
                    colaboracion.setAcademicoPar(convertirAcademico(resultadoColaboracionPorId));
                }
            }
            resultadoColaboracionPorId.close();
            colaboracionPorId.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones por su identificador", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return colaboracion;
    }

    public static List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws ErrorDAO {
        String listaDeEstudiantesSQL = "{CALL obtener_estudiantes_colaboracion(?)}";
        List<Estudiante> listaEstudiantes = new ArrayList<>();
        try {
            CallableStatement procedimientoListaDeEstudiantes = ConexionBaseDatos.getInstancia().
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
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener los estudiantes participantes en la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return listaEstudiantes;
    }

    public static List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws ErrorDAO {
        String academicosParticipantes = "{CALL obtener_academicos_colaboracion(?)}";
        List<Academico> listaAcademicos = new ArrayList<>();
        try {
            CallableStatement procedimientoAcademicosParticipantes = ConexionBaseDatos.getInstancia().
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
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtneer lo academicos participantes en la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return listaAcademicos;
    }


    public static List<Colaboracion> getColaboracionPorPeriodo (Periodo periodo) throws ErrorDAO {
        String colaboracionPorPeriodoSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.fechaInicio =? AND fechaFin = ?""";

        List<Colaboracion> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement colaboracionPorPeriodo = ConexionBaseDatos.getInstancia().
                                                                        prepareStatement(colaboracionPorPeriodoSQL);
            colaboracionPorPeriodo.setDate(1, Date.valueOf(periodo.getFechaInicio()));
            colaboracionPorPeriodo.setDate(2, Date.valueOf(periodo.getFechaFin()));

            ResultSet resultadoColaboracionPorPeriodo = colaboracionPorPeriodo.executeQuery();

            procesarResultadosColaboracionConLista(resultadoColaboracionPorPeriodo, listaColaboracion);

            colaboracionPorPeriodo.close();
            resultadoColaboracionPorPeriodo.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones por periodo", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return listaColaboracion;
    }

    public static List<Colaboracion> getColaboracionPorIdioma (String idioma) throws ErrorDAO {
        String colaboracionPorIdiomaSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idioma = ?""";
        List<Colaboracion> listaColaboracion = new ArrayList<>();

        try {
            PreparedStatement colaboracionPorIdioma = ConexionBaseDatos.getInstancia().
                                                                       prepareStatement(colaboracionPorIdiomaSQL);
            colaboracionPorIdioma.setString(1, idioma);

            ResultSet resultadoColaboracionPorIdioma = colaboracionPorIdioma.executeQuery();

            procesarResultadosColaboracionConLista(resultadoColaboracionPorIdioma, listaColaboracion);
            colaboracionPorIdioma.close();
            resultadoColaboracionPorIdioma.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones por idioma", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return listaColaboracion;
    }

    public static List<Colaboracion> getColaboracionPorEstado (String estado) throws ErrorDAO {
        String colaboracionPorEstadoSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.estado = ?""";
        List<Colaboracion> listaColaboraciones = new ArrayList<>();

        try {
            PreparedStatement colaboracionPorEstado = ConexionBaseDatos.getInstancia().
                                                                       prepareStatement(colaboracionPorEstadoSQL);
            colaboracionPorEstado.setString(1, estado);

            ResultSet resultadoColaboracionPorEstado = colaboracionPorEstado.executeQuery();

            procesarResultadosColaboracionConLista(resultadoColaboracionPorEstado, listaColaboraciones);
            resultadoColaboracionPorEstado.close();
            colaboracionPorEstado.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }

        return listaColaboraciones;
    }

    public static int cambiarEstadoColaboracion (Colaboracion colaboracion) throws ErrorDAO {
        String cambiarEstadoColaboracionSQL = "UPDATE colaboracion SET estado = ? WHERE idColaboracion = ?";
        int filasAfectadas;

        try {
            PreparedStatement cambiarEstadoColaboracion = ConexionBaseDatos.getInstancia().
                                                                           prepareStatement(cambiarEstadoColaboracionSQL);
            cambiarEstadoColaboracion.setString(1, colaboracion.getEstado().
                                                               name()
                                                               .toLowerCase());
            cambiarEstadoColaboracion.setInt(2, colaboracion.getIdColaboracion());
            filasAfectadas = cambiarEstadoColaboracion.executeUpdate();
            cambiarEstadoColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al cambiar el estado de la colaboración", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return filasAfectadas;
    }


    public static int agregarEstudianteAColaboracion (Colaboracion colaboracion, Estudiante estudiante) throws ErrorDAO {
        String agregarEstudianteAColaboracionSQL = "INSERT INTO estudiantescolaboracion (idEstudiante, idColaboracion) VALUES (?, ?)";
        int filasAfectadas;

        try {

            PreparedStatement agregarEstudianteAColaboracion = ConexionBaseDatos.getInstancia().
                                                                                prepareStatement(agregarEstudianteAColaboracionSQL);
            agregarEstudianteAColaboracion.setInt(1, estudiante.getIdEstudiante());
            agregarEstudianteAColaboracion.setInt(2, colaboracion.getIdColaboracion());

            filasAfectadas = agregarEstudianteAColaboracion.executeUpdate();

            agregarEstudianteAColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("El error al agregar un estudiante a la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return filasAfectadas;

    }

    public static int agregarAcademicoAColaboracion (Colaboracion colaboracion, Academico academico) throws ErrorDAO {
        String agregarAcademicoAColaboracionSQL = "INSERT INTO academicodesarrolla (idColaboracion, idAcademico) VALUES (?, ?)";
        int filasAfectadas;

        try {

            PreparedStatement agregarAcademicoAColaboracion = ConexionBaseDatos.getInstancia().
                                                                               prepareStatement(agregarAcademicoAColaboracionSQL);
            agregarAcademicoAColaboracion.setInt(1, colaboracion.getIdColaboracion());
            agregarAcademicoAColaboracion.setString(2, academico.getCedulaProfesional());

            filasAfectadas = agregarAcademicoAColaboracion.executeUpdate();
            agregarAcademicoAColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al agregar a un académic a una colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    public static int registrarColaboracion (Colaboracion colaboracion) throws ErrorDAO {
        String registrarColaboracionSQL = "{CALL registrar_Colaboracion(?,?,?,?,?,?,?,?)}";
        int filasAfectadas;

        try {

            CallableStatement procedimientoRegistrarColaboracion = ConexionBaseDatos.getInstancia().
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
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al registrar la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    public static int actualizarColaboracion (Colaboracion colaboracion) throws ErrorDAO {
        String actualizarColaboracionSQL = "{CALL actualizar_Colaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int filasAfectadas;
        try {

            CallableStatement procedimientoActualizarColaboracion = ConexionBaseDatos.getInstancia()
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
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al actualizar la colaboración", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    public static Colaboracion getPorId (int id) throws ErrorDAO {
        String getPorIdSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idColaboracion = ?""";
        Colaboracion colaboracion = null;
        try {
            PreparedStatement getPorId = ConexionBaseDatos.getInstancia().
                                                          prepareStatement(getPorIdSQL);
            getPorId.setInt(1, id);

            ResultSet resultadoGetPorId = getPorId.executeQuery();

            if (resultadoGetPorId.next()) {
                colaboracion = convertirColaboracion(resultadoGetPorId);
            }
            getPorId.close();
            resultadoGetPorId.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
        }
        return colaboracion;
    }

    public static List<Colaboracion> getTodos () throws ErrorDAO {
        String getTodosSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                LEFT JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                LEFT JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional""";
        List<Colaboracion> listaColaboracion = new ArrayList<>();

        try {
            PreparedStatement getTodos = ConexionBaseDatos.getInstancia().
                                                          prepareStatement(getTodosSQL);
            ResultSet resultadoGetTodos = getTodos.executeQuery();

            procesarResultadosColaboracionConLista(resultadoGetTodos, listaColaboracion);

            resultadoGetTodos.close();
            getTodos.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener todas las colaboraciones registradas", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            ConexionBaseDatos.desconectar();
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

    private static void procesarResultadosColaboracionConLista (ResultSet resultados, List<Colaboracion> listaColaboracion) throws SQLException {
        while (resultados.next()) {
            Colaboracion colaboracion = convertirColaboracion(resultados);
            Academico academico = convertirAcademico(resultados);

            if (!listaColaboracion.isEmpty()) {
                Colaboracion colaboracionActual = listaColaboracion.get(listaColaboracion.size() - 1);
                if (colaboracion.getIdColaboracion() == colaboracionActual.getIdColaboracion()) {
                    if (colaboracionActual.getAnfitrion() == null) {
                        colaboracionActual.setAnfitrion(academico);
                    }
                    else {
                        colaboracionActual.setAcademicoPar(academico);
                    }
                }
                else {
                    colaboracion.setAnfitrion(academico);
                    listaColaboracion.add(colaboracion);
                }
            }
            else {
                listaColaboracion.add(colaboracion);
                colaboracion.setAnfitrion(academico);
            }
        }
    }
}
