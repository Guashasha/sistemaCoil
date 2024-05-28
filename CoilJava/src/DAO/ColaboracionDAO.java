package DAO;

import DAO.Interfaces.IColaboracionDAO;
import DTO.*;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ColaboracionDAO implements IColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(ColaboracionDAO.class);

    @Override
    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        String colaboracionPorIdSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idColaboracion = ?""";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement colaboracionPorId = AdministradorBaseDatos.getInstancia()
                                                                        .prepareStatement(colaboracionPorIdSQL);

            colaboracionPorId.setInt(1, idColaboracion);
            ResultSet resultadoColaboracionPorId = colaboracionPorId.executeQuery();
            if (resultadoColaboracionPorId.next()) {
                colaboracionDTO = convertirResultSetAColaboracionDTO(resultadoColaboracionPorId);
                colaboracionDTO.setAnfitrion(convertirAcademico(resultadoColaboracionPorId));

                if (resultadoColaboracionPorId.next()) {
                    colaboracionDTO.setAcademicoPar(convertirAcademico(resultadoColaboracionPorId));
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
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    @Override
    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String listaDeEstudiantesSQL = "{CALL obtener_estudiantes_colaboracion(?)}";
        List<EstudianteDTO> listaEstudianteDTOS = new ArrayList<>();
        try {
            CallableStatement procedimientoListaDeEstudiantes = AdministradorBaseDatos.getInstancia().
                                                                                      prepareCall(listaDeEstudiantesSQL);
            procedimientoListaDeEstudiantes.setInt(1, colaboracionDTO.getIdColaboracion());
            ResultSet resultadoListaDeEstudiantes = procedimientoListaDeEstudiantes.executeQuery();

            while (resultadoListaDeEstudiantes.next()) {
                EstudianteDTO estudianteDTO = convertirEstudiante(resultadoListaDeEstudiantes);
                listaEstudianteDTOS.add(estudianteDTO);
            }
            resultadoListaDeEstudiantes.close();
            procedimientoListaDeEstudiantes.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener los estudiantes participantes en la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaEstudianteDTOS;
    }

    @Override
    public List<AcademicoDTO> getAcademicosParticipantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String academicosParticipantes = "SELECT * FROM vista_colaboracion_con_academico WHERE idColaboracion = ? AND (estadoAcademico = 'anfitrion' OR estadoAcademico = 'aceptado')";
        List<AcademicoDTO> listaAcademicoDTOS = new ArrayList<>();
        try {
            CallableStatement procedimientoAcademicosParticipantes = AdministradorBaseDatos.getInstancia().
                                                                                           prepareCall(academicosParticipantes);
            procedimientoAcademicosParticipantes.setInt(1, colaboracionDTO.getIdColaboracion());

            ResultSet resultadoAcademicosParticipantes = procedimientoAcademicosParticipantes.executeQuery();
            while (resultadoAcademicosParticipantes.next()) {
                AcademicoDTO academicoDTO = convertirAcademico(resultadoAcademicosParticipantes);
                listaAcademicoDTOS.add(academicoDTO);
            }
            resultadoAcademicosParticipantes.close();
            procedimientoAcademicosParticipantes.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtneer lo academicos participantes en la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaAcademicoDTOS;
    }

    @Override
    public List<ColaboracionDTO> getColaboracionPorPeriodo (PeriodoDTO periodoDTO) throws ErrorDAO {
        String colaboracionPorPeriodoSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.fechaInicio =? AND fechaFin = ?""";

        List<ColaboracionDTO> listaColaboracionDTO = new ArrayList<>();
        try {
            PreparedStatement colaboracionPorPeriodo = AdministradorBaseDatos.getInstancia().
                                                                             prepareStatement(colaboracionPorPeriodoSQL);
            colaboracionPorPeriodo.setDate(1, Date.valueOf(periodoDTO.getFechaInicio()));
            colaboracionPorPeriodo.setDate(2, Date.valueOf(periodoDTO.getFechaFin()));

            ResultSet resultadoColaboracionPorPeriodo = colaboracionPorPeriodo.executeQuery();

            procesarResultadosColaboracionConLista(resultadoColaboracionPorPeriodo, listaColaboracionDTO);

            colaboracionPorPeriodo.close();
            resultadoColaboracionPorPeriodo.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones por periodoDTO", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracionDTO;
    }

    @Override
    public List<ColaboracionDTO> getColaboracionPorIdioma (String idioma) throws ErrorDAO {
        String colaboracionPorIdiomaSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idioma = ?""";
        List<ColaboracionDTO> listaColaboracionDTO = new ArrayList<>();

        try {
            PreparedStatement colaboracionPorIdioma = AdministradorBaseDatos.getInstancia().
                                                                            prepareStatement(colaboracionPorIdiomaSQL);
            colaboracionPorIdioma.setString(1, idioma);

            ResultSet resultadoColaboracionPorIdioma = colaboracionPorIdioma.executeQuery();

            procesarResultadosColaboracionConLista(resultadoColaboracionPorIdioma, listaColaboracionDTO);
            colaboracionPorIdioma.close();
            resultadoColaboracionPorIdioma.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones por idioma", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracionDTO;
    }

    @Override
    public List<ColaboracionDTO> getColaboracionPorEstado (String estado) throws ErrorDAO {
        String colaboracionPorEstadoSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.estado = ?""";
        List<ColaboracionDTO> listaColaboraciones = new ArrayList<>();

        try {
            PreparedStatement colaboracionPorEstado = AdministradorBaseDatos.getInstancia().
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
            AdministradorBaseDatos.desconectar();
        }

        return listaColaboraciones;
    }

    @Override
    public int cambiarEstadoColaboracion (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String cambiarEstadoColaboracionSQL = "UPDATE colaboracion SET estado = ? WHERE idColaboracion = ?";
        int filasAfectadas;

        try {
            PreparedStatement cambiarEstadoColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                prepareStatement(cambiarEstadoColaboracionSQL);
            cambiarEstadoColaboracion.setString(1, colaboracionDTO.getEstado().
                                                                  name()
                                                                  .toLowerCase());
            cambiarEstadoColaboracion.setInt(2, colaboracionDTO.getIdColaboracion());
            filasAfectadas = cambiarEstadoColaboracion.executeUpdate();
            cambiarEstadoColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al cambiar el estado de la colaboración", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO {
        String agregarEstudianteAColaboracionSQL = "INSERT INTO estudiantescolaboracion (idEstudiante, idColaboracion) VALUES (?, ?)";
        int filasAfectadas;

        try {

            PreparedStatement agregarEstudianteAColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                     prepareStatement(agregarEstudianteAColaboracionSQL);
            agregarEstudianteAColaboracion.setInt(1, estudianteDTO.getIdEstudiante());
            agregarEstudianteAColaboracion.setInt(2, colaboracionDTO.getIdColaboracion());

            filasAfectadas = agregarEstudianteAColaboracion.executeUpdate();

            agregarEstudianteAColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("El error al agregar un estudianteDTO a la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;

    }

    @Override
    public int registrarSolicitudParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String agregarAcademicoAColaboracionSQL = "INSERT INTO academicodesarrolla (idColaboracion, idAcademico, estado) VALUES (?, ?, 'pendiente')";
        int filasAfectadas;

        try {
            PreparedStatement agregarAcademicoAColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                    prepareStatement(agregarAcademicoAColaboracionSQL);
            agregarAcademicoAColaboracion.setInt(1, colaboracionDTO.getIdColaboracion());
            agregarAcademicoAColaboracion.setString(2, academicoDTO.getCedulaProfesional());

            filasAfectadas = agregarAcademicoAColaboracion.executeUpdate();
            agregarAcademicoAColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al agregar a un académic a una colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String obtenerColaboracionActivaAcademicoSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'activa' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(obtenerColaboracionActivaAcademicoSQL);
            obtenerColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = obtenerColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirResultSetAColaboracionDTO(resultado);
                obtenerAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboracion activa por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    public Optional<ColaboracionDTO> getColaboracionAceptadaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String obtenerColaboracionAceptadaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'aceptada' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(obtenerColaboracionAceptadaSQL);
            obtenerColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = obtenerColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                obtenerAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboracion activa por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    @Override
    public Optional<ColaboracionDTO> getPropuestaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String obtenerPropuestaPorAcademico = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'propuesta' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(obtenerPropuestaPorAcademico);
            obtenerColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = obtenerColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                obtenerAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboracion activa por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    @Override
    public int registrarPropuestaColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String registrarPropuestaSQL = "INSERT INTO colaboracion (temaInteres, objetivo, estado) VALUES (?,?,?)";
        String asociarAcademicaPropuestaSQL = "INSERT INTO academicodesarrolla (idColaboracion, idAcademico, estado) VALUES (?, ?, ?)";
        int filasAfectadas;
        int idGenerado = -1;
        try {
            AdministradorBaseDatos.getInstancia()
                                  .setAutoCommit(false);
            PreparedStatement registrarPropuesta = AdministradorBaseDatos.getInstancia()
                                                                         .prepareStatement(registrarPropuestaSQL, Statement.RETURN_GENERATED_KEYS);
            registrarPropuesta.setString(1, colaboracionDTO.getTemaInteres());
            registrarPropuesta.setString(2, colaboracionDTO.getObjetivo());
            registrarPropuesta.setString(3, ColaboracionDTO.EstadoColaboracion.propuesta.toString());
            filasAfectadas = registrarPropuesta.executeUpdate();
            ResultSet resultSet = registrarPropuesta.getGeneratedKeys();
            while (resultSet.next()) {
                idGenerado = resultSet.getInt(1);
            }

            PreparedStatement asociarAcademico = AdministradorBaseDatos.getInstancia().prepareStatement(asociarAcademicaPropuestaSQL);
            asociarAcademico.setInt(1,idGenerado);
            asociarAcademico.setString(2, academicoDTO.getCedulaProfesional());
            asociarAcademico.setString(3, "anfitrion");
            filasAfectadas += asociarAcademico.executeUpdate();

            AdministradorBaseDatos.getInstancia()
                                  .commit();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            AdministradorBaseDatos.rollback();
            throw new ErrorDAO("Error al ingresar la propuesta de colaboracion y vincularla", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public List<ColaboracionDTO> obtenerPropuestasColaboracion () throws ErrorDAO {
        String obtenerPropuestasSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND estado = 'propuesta'";
        List<ColaboracionDTO> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement obtenerPropuestas = AdministradorBaseDatos.getInstancia()
                                                                        .prepareStatement(obtenerPropuestasSQL);
            ResultSet resultado = obtenerPropuestas.executeQuery();
            while (resultado.next()) {
                listaColaboracion.add(convertirPropuesta(resultado));
            }
            obtenerPropuestas.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la propuesta de colaboracion", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracion;
    }

    @Override
    public List<ColaboracionDTO> obtenerColaboracionesDisponibles (String cedulaProfesional) throws ErrorDAO {
        String colaboracionDisponibleSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND estado = 'disponible' AND cedulaProfesional != ? ";
        List<ColaboracionDTO> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement obtenerColaboraciones = AdministradorBaseDatos.getInstancia()
                                                                            .prepareStatement(colaboracionDisponibleSQL);
            obtenerColaboraciones.setString(1, cedulaProfesional);
            ResultSet resultado = obtenerColaboraciones.executeQuery();
            while (resultado.next()) {
                ColaboracionDTO colaboracionDTO = convertirResultSetAColaboracionDTO(resultado);
                obtenerAcademico(colaboracionDTO, resultado);
                listaColaboracion.add(colaboracionDTO);
            }
            obtenerColaboraciones.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones disponibles", ErrorDAO.Tipo.CONSULTA);
        }
        return listaColaboracion;
    }

    @Override
    public Optional<ColaboracionDTO> getColaboracionActualPorAcademico (String cedulaProfesional) {
        String colaboracionDisponibleSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND (estado = 'disponible' OR estado = 'aceptada' OR estado = 'vinculada' OR estado = 'activa' OR estado = 'enRevision') AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        
        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                    .prepareStatement(colaboracionDisponibleSQL);
            obtenerColaboracion.setString(1, cedulaProfesional);
            ResultSet resultado = obtenerColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                obtenerAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones disponibles", ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.ofNullable(colaboracionDTO);
    }

    public Optional<ColaboracionDTO> getColaboracionDisponiblePorAcademico(String cedulaProfesional) throws ErrorDAO {
        String colaboracionDisponibleSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND estado = 'disponible' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(colaboracionDisponibleSQL);
            obtenerColaboracion.setString(1, cedulaProfesional);
            ResultSet resultado = obtenerColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                obtenerAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las colaboraciones disponibles", ErrorDAO.Tipo.CONSULTA);
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    @Override
    public boolean existeUnaSolicitudPrevia (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String obtenerAcademicoSolicitud = "SELECT * FROM vista_colaboracion_con_academico WHERE idColaboracion = ? AND cedulaProfesional = ? AND estadoAcademico = 'pendiente'";
        Optional<ColaboracionDTO> colaboracionDTOOptional = Optional.empty();
        try {
            PreparedStatement obtenerAcademico = AdministradorBaseDatos.getInstancia()
                                                                       .prepareStatement(obtenerAcademicoSolicitud);
            obtenerAcademico.setInt(1, colaboracionDTO.getIdColaboracion());
            obtenerAcademico.setString(2, academicoDTO.getCedulaProfesional());
            ResultSet resultado = obtenerAcademico.executeQuery();
            if (resultado.next()) {
                colaboracionDTOOptional = Optional.ofNullable(convertirResultSetAColaboracionDTO(resultado));
            }
            obtenerAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al comprobar si existe una solicitud previa", ErrorDAO.Tipo.CONSULTA);
        }
        return colaboracionDTOOptional.isPresent();
    }

    @Override
    public List<AcademicoDTO> obtenerSolicitudAcademicoColaboracion (int idColaboracion) throws ErrorDAO {
        String obtenerAcademicoSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'pendiente' AND idColaboracion = ?";
        List<AcademicoDTO> listaAcademico = new ArrayList<>();
        try {
            PreparedStatement obtenerAcademico = AdministradorBaseDatos.getInstancia()
                                                                       .prepareStatement(obtenerAcademicoSQL);
            obtenerAcademico.setInt(1, idColaboracion);
            ResultSet resultado = obtenerAcademico.executeQuery();
            while (resultado.next()) {
                listaAcademico.add(convertirAcademico(resultado));
            }
            obtenerAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las solicitudes de la colaboracion", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaAcademico;
    }

    @Override
    public List<ColaboracionDTO> obtenerSolicitudesDeAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String obtenerColaboracionSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'pendiente' AND cedulaProfesional = ?";
        List<ColaboracionDTO> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(obtenerColaboracionSQL);
            obtenerColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = obtenerColaboracion.executeQuery();
            while (resultado.next()) {
                listaColaboracion.add(convertirResultSetAColaboracionDTO(resultado));
            }
            obtenerColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las solicitudes de la colaboracion", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracion;

    }

    @Override
    public int actualizarEstadoSolicitudDeParticipacion(int idColaboracion, String idAcademico, String nuevoEstado) throws ErrorDAO {
        String actualizarSQL = "UPDATE academicoDesarrolla SET estado = ? WHERE idColaboracion = ? AND idAcademico = ?";
        int filasAfectadas = 0;

        try {
            PreparedStatement actualizarStmt = AdministradorBaseDatos.getInstancia().prepareStatement(actualizarSQL);
            actualizarStmt.setString(1, nuevoEstado);
            actualizarStmt.setInt(2, idColaboracion);
            actualizarStmt.setString(3, idAcademico);

            filasAfectadas = actualizarStmt.executeUpdate();

            actualizarStmt.close();
        } catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al actualizar el estado de la solicitud de participación", ErrorDAO.Tipo.INSERCION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public int eliminarSolicitudDeParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String eliminarSQL = "DELETE FROM academicoDesarrolla WHERE idColaboracion = ? AND idAcademico = ?";
        int filasAfectadas = 0;

        try {
            PreparedStatement eliminarStmt = AdministradorBaseDatos.getInstancia()
                                                                   .prepareStatement(eliminarSQL);
            eliminarStmt.setInt(1, colaboracionDTO.getIdColaboracion());
            eliminarStmt.setString(2, academicoDTO.getCedulaProfesional());

            filasAfectadas = eliminarStmt.executeUpdate();

            eliminarStmt.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al eliminar la solicitud de participación", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public int agregar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String registrarColaboracionSQL = "{CALL registrar_Colaboracion(?,?,?,?,?,?,?,?)}";
        int filasAfectadas;

        try {

            CallableStatement procedimientoRegistrarColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                         prepareCall(registrarColaboracionSQL);
            procedimientoRegistrarColaboracion.setString(1, colaboracionDTO.getEstado()
                                                                           .name());
            procedimientoRegistrarColaboracion.setString(2, colaboracionDTO.getTipo()
                                                                           .name());
            procedimientoRegistrarColaboracion.setString(3, colaboracionDTO.getTemaInteres());
            procedimientoRegistrarColaboracion.setString(4, colaboracionDTO.getIdioma());
            procedimientoRegistrarColaboracion.setString(5, colaboracionDTO.getObjetivo());
            procedimientoRegistrarColaboracion.setDate(6, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                      getFechaInicio()));
            procedimientoRegistrarColaboracion.setDate(7, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                      getFechaFin()));
            procedimientoRegistrarColaboracion.setString(8, colaboracionDTO.getPerfilEstudiante());

            filasAfectadas = procedimientoRegistrarColaboracion.executeUpdate();

            procedimientoRegistrarColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al registrar la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public int modificar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String actualizarColaboracionSQL = "{CALL actualizar_Colaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int filasAfectadas;
        try {

            CallableStatement procedimientoActualizarColaboracion = AdministradorBaseDatos.getInstancia()
                                                                                          .prepareCall(actualizarColaboracionSQL);

            procedimientoActualizarColaboracion.setInt(1, colaboracionDTO.getIdColaboracion());
            procedimientoActualizarColaboracion.setString(2, colaboracionDTO.getEstado()
                                                                            .name());
            procedimientoActualizarColaboracion.setString(3, colaboracionDTO.getTipo()
                                                                            .name());
            procedimientoActualizarColaboracion.setString(4, colaboracionDTO.getTemaInteres());
            procedimientoActualizarColaboracion.setString(5, colaboracionDTO.getIdioma());
            procedimientoActualizarColaboracion.setString(6, colaboracionDTO.getObjetivo());
            if (colaboracionDTO.getPeriodo() != null) {
                procedimientoActualizarColaboracion.setDate(7, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                           getFechaInicio()));
            }
            else {
                procedimientoActualizarColaboracion.setDate(7, null);
            }
            if (colaboracionDTO.getPeriodo() != null) {
                procedimientoActualizarColaboracion.setDate(8, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                           getFechaFin()));
            }
            else {
                procedimientoActualizarColaboracion.setDate(8, null);
            }
            procedimientoActualizarColaboracion.setString(9, colaboracionDTO.getPerfilEstudiante());

            filasAfectadas = procedimientoActualizarColaboracion.executeUpdate();

            procedimientoActualizarColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al actualizar la colaboración", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }


    @Override
    public Optional<ColaboracionDTO> getPorId (Integer id) throws ErrorDAO {
        String getPorIdSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE idColaboracion = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement getPorId = AdministradorBaseDatos.getInstancia().
                                                               prepareStatement(getPorIdSQL);
            getPorId.setInt(1, id);

            ResultSet resultadoGetPorId = getPorId.executeQuery();

            if (resultadoGetPorId.next()) {
                colaboracionDTO = convertirResultSetAColaboracionDTO(resultadoGetPorId);
            }
            getPorId.close();
            resultadoGetPorId.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    public Optional<AcademicoDTO> getAcademicoPar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String getAcademicoParSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'aceptado' AND idColaboracion = ?";
        AcademicoDTO academicoDTO = null;
        try {
            PreparedStatement getAcademicoPar = AdministradorBaseDatos.getInstancia().prepareStatement(getAcademicoParSQL);
            getAcademicoPar.setInt(1, colaboracionDTO.getIdColaboracion());
            ResultSet resultadoGetAcademicoPar = getAcademicoPar.executeQuery();

            if (resultadoGetAcademicoPar.next()) {
                academicoDTO = convertirAcademico(resultadoGetAcademicoPar);
            }
            getAcademicoPar.close();
            resultadoGetAcademicoPar.close();

        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener al académico par", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(academicoDTO);
    }

    public List<ColaboracionDTO> getTodos () throws ErrorDAO {
        String getTodosSQL = "SELECT * FROM vista_colaboracion_con_academico";
        List<ColaboracionDTO> listaColaboracionDTO = new ArrayList<>();

        try {
            PreparedStatement getTodos = AdministradorBaseDatos.getInstancia().
                                                               prepareStatement(getTodosSQL);
            ResultSet resultadoGetTodos = getTodos.executeQuery();

            procesarResultadosColaboracionConLista(resultadoGetTodos, listaColaboracionDTO);

            resultadoGetTodos.close();
            getTodos.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener todas las colaboraciones registradas", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracionDTO;
    }

    private static ColaboracionDTO convertirPropuesta (ResultSet resultado) throws SQLException {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres(resultado.getString("temaInteres"));
        colaboracionDTO.setObjetivo(resultado.getString("objetivo"));
        colaboracionDTO.setIdColaboracion(resultado.getInt("idColaboracion"));
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.valueOf(resultado.getString("estado")));
        colaboracionDTO.setAnfitrion(convertirAcademico(resultado));
        return colaboracionDTO;
    }

    private static void obtenerAcademico (ColaboracionDTO colaboracionDTO, ResultSet resultado) throws SQLException {
        if (resultado.getString("estadoAcademico")
                     .equals("anfitrion")) {
            colaboracionDTO.setAnfitrion(convertirAcademico(resultado));
        }
        else {
            colaboracionDTO.setAcademicoPar(convertirAcademico(resultado));
        }
    }


    private static ColaboracionDTO convertirResultSetAColaboracionDTO (ResultSet resultado) throws SQLException {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();

        colaboracionDTO.setIdColaboracion(resultado.getInt("idColaboracion"));

        String estado = resultado.getString("estado");

        ColaboracionDTO.EstadoColaboracion estadoColaboracion = ColaboracionDTO.EstadoColaboracion.
                valueOf(estado);
        colaboracionDTO.setEstado(estadoColaboracion);

        String tipo = resultado.getString("tipo");

        ColaboracionDTO.TipoColaboracion tipoColaboracion = ColaboracionDTO.TipoColaboracion.
                valueOf(tipo);

        colaboracionDTO.setTipo(tipoColaboracion);
        colaboracionDTO.setTemaInteres(resultado.getString("temaInteres"));
        colaboracionDTO.setIdioma(resultado.getString("idioma"));
        colaboracionDTO.setObjetivo(resultado.getString("objetivo"));

        PeriodoDTO periodoDTO = new PeriodoDTO();
        if (resultado.getDate("fechaInicio") != null || resultado.getDate("fechaFin") != null) {
            periodoDTO.setFechaInicio(resultado.getDate("fechaInicio").
                                               toLocalDate());
            periodoDTO.setFechaFin(resultado.getDate("fechaFin").
                                            toLocalDate());
        }
        colaboracionDTO.setPeriodo(periodoDTO);
        colaboracionDTO.setPerfilEstudiante(resultado.getString("perfilEstudiante"));

        return colaboracionDTO;
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

    private static AcademicoDTO convertirAcademico (ResultSet resultado) throws SQLException {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        academicoDTO.setIdPersona(resultado.getInt("idPersona"));
        academicoDTO.setNombre(resultado.getString("nombre"));
        academicoDTO.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        academicoDTO.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        academicoDTO.setIdUniversidad(resultado.getInt("idUniversidad"));
        academicoDTO.setCedulaProfesional(resultado.getString("cedulaProfesional"));

        if (resultado.getString("categoriaContratacion") != null) {
            academicoDTO.setCategoriaContratacion(resultado.getString("categoriaContratacion"));
        }
        if (resultado.getObject("idFacultad") != null) {
            academicoDTO.setIdFacultad(resultado.getInt("idFacultad"));
        }
        academicoDTO.setNumeroPersonal(resultado.getString("numeroDePersonal"));
        academicoDTO.setAreaEstudios(resultado.getString("areaEstudios"));
        academicoDTO.setCorreoElectronico(resultado.getString("correoElectronico"));
        academicoDTO.setNumeroTelefonico(resultado.getString("numeroTelefonico"));
        return academicoDTO;
    }

    private static void procesarResultadosColaboracionConLista (ResultSet resultados, List<ColaboracionDTO> listaColaboracionDTO) throws SQLException {
        ColaboracionDTO colaboracionDTOActual = null;

        while (resultados.next()) {
            ColaboracionDTO colaboracionDTO = convertirResultSetAColaboracionDTO(resultados);

            if (colaboracionDTOActual == null || colaboracionDTO.getIdColaboracion() != colaboracionDTOActual.getIdColaboracion()) {
                colaboracionDTOActual = colaboracionDTO;
                listaColaboracionDTO.add(colaboracionDTOActual);
            }

            obtenerAcademico(colaboracionDTOActual, resultados);
        }
    }

    public Map<String, int[]> getNumeraliaRegion (PeriodoDTO periodo) throws ErrorDAO {
        String numeraliaRegionSQL = "{CALL numeralia_region(?,?)}";
        return ejecutarConsultaNumeralia(numeraliaRegionSQL, periodo);
    }

    public Map<String, int[]> getNumeraliaAreaAcademica (PeriodoDTO periodo) throws ErrorDAO {
        String numeraliaAreaAcademicaSQL = "{CALL numeralia_area_academica(?,?)}";
        return ejecutarConsultaNumeralia(numeraliaAreaAcademicaSQL, periodo);
    }

    public Optional<LocalDate> getFechaColaboracionMasAntigua () {
        LocalDate fechaMasAntigua = null;
        String consultaSQL = "SELECT MIN(fechaFin) FROM numeralia";
        PreparedStatement consulta;
        ResultSet resultado;

        try {
            consulta = AdministradorBaseDatos.getInstancia()
                                             .prepareStatement(consultaSQL);
            resultado = consulta.executeQuery();

            if (resultado.next()) {
                Date fecha = resultado.getDate(1);
                if (fecha != null) {
                    String fechaString = fecha.toString();
                    int anio = Integer.parseInt(fechaString.substring(0,4));
                    int mes = Integer.parseInt(fechaString.substring(5,7));
                    int dia = Integer.parseInt(fechaString.substring(8,10));
                    fechaMasAntigua = LocalDate.of(anio,mes,dia);
                }
            }

            consulta.close();
            resultado.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al consultar colaboraciones", ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.ofNullable(fechaMasAntigua);
    }

    private Map<String,int[]> ejecutarConsultaNumeralia (String consultaSQL, PeriodoDTO periodo) throws ErrorDAO {
        Map<String,int[]> numeralia = new HashMap<>();
        CallableStatement llamadaProcedimiento;
        ResultSet resultado;

        try {
            llamadaProcedimiento = AdministradorBaseDatos.getInstancia()
                                                         .prepareCall(consultaSQL);
            llamadaProcedimiento.setDate(1, Date.valueOf(periodo.getFechaInicio()));
            llamadaProcedimiento.setDate(2, Date.valueOf(periodo.getFechaFin()));
            resultado = llamadaProcedimiento.executeQuery();

            if (resultado.next()) {
                numeralia = convertirResultSetNumeralia(resultado);
            }

            llamadaProcedimiento.close();
            resultado.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la numeralia", ErrorDAO.Tipo.CONSULTA);
        }
        return numeralia;
    }

    private Map<String,int[]> convertirResultSetNumeralia (ResultSet resultSet) throws SQLException {
        Map<String,int[]> numeralia = new HashMap<>();
        do {
            String categoria = resultSet.getString(1);
            int alumnos = resultSet.getInt("alumnos");
            int profesores = resultSet.getInt("profesores");
            int[] cantidad = new int[]{alumnos,profesores};
            numeralia.put(categoria,cantidad);
        } while (resultSet.next());
        return numeralia;
    }

    public Optional<ColaboracionDTO> getVinculadaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String obtenerColaboracionVinculadaAcademicoSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'vinculada' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracion = null;

        try {
            PreparedStatement obtenerColaboracion = AdministradorBaseDatos.getInstancia()
                    .prepareStatement(obtenerColaboracionVinculadaAcademicoSQL);
            obtenerColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = obtenerColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracion = convertirResultSetAColaboracionDTO(resultado);
                obtenerAcademico(colaboracion, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la colaboracion en estaod \"vinculada\" por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracion);
    }

    public int retirarEstudianteDeColaboracion(ColaboracionDTO colaboracion, EstudianteDTO estudiante) throws ErrorDAO {
        String retirarEstudianteDeColaboracionSQL = "DELETE FROM estudiantescolaboracion WHERE idColaboracion = ? AND idEstudiante = ?";
        int filasAfectadas;

        try {
            PreparedStatement retirarEstudianteDeColaboracion = AdministradorBaseDatos.getInstancia().
                    prepareStatement(retirarEstudianteDeColaboracionSQL);
            retirarEstudianteDeColaboracion.setInt(1, colaboracion.getIdColaboracion());
            retirarEstudianteDeColaboracion.setInt(2, estudiante.getIdEstudiante());

            filasAfectadas = retirarEstudianteDeColaboracion.executeUpdate();

            retirarEstudianteDeColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("El error al retirar el estudiante " + estudiante.getMatricula() + " de la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;

    }
}
