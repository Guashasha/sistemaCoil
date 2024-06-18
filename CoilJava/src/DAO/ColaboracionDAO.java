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

    /**
     * Obtiene una colaboración por su ID.
     *
     * @param idColaboracion el ID de la colaboración.
     * @return un Optional que contiene la colaboración si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        String consultaSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicoDesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idColaboracion = ?""";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                        .prepareStatement(consultaSQL);

            consultaColaboracion.setInt(1, idColaboracion);
            ResultSet resultado = consultaColaboracion.executeQuery();
            if (resultado.next()) {
                colaboracionDTO = convertirResultSetAColaboracionDTO(resultado);
                colaboracionDTO.setAnfitrion(convertirAcademico(resultado));

                if (resultado.next()) {
                    colaboracionDTO.setAcademicoPar(convertirAcademico(resultado));
                }
            }
            resultado.close();
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las colaboraciones por su identificador", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Agrega un periodo a una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int agregarPeriodoAColaboracion (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String actualizacionSQL = "UPDATE colaboracion SET fechaInicio = ?, fechaFin = ? WHERE idColaboracion = ?";
        int filasAfectadas;

        try {
            PreparedStatement actualizarColaboracion = AdministradorBaseDatos.getInstancia()
                                                                     .prepareStatement(actualizacionSQL);
            actualizarColaboracion.setDate(1, java.sql.Date.valueOf(colaboracionDTO.getPeriodo()
                                                                           .getFechaInicio()));
            actualizarColaboracion.setDate(2, java.sql.Date.valueOf(colaboracionDTO.getPeriodo()
                                                                           .getFechaFin()));
            actualizarColaboracion.setInt(3, colaboracionDTO.getIdColaboracion());

            filasAfectadas = actualizarColaboracion.executeUpdate();
            actualizarColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al agregar el periodo", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Obtiene la lista de estudiantes de una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @return la lista de estudiantes.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String consultaSQL = "{CALL obtener_estudiantes_colaboracion(?)}";
        List<EstudianteDTO> listaEstudianteDTOS = new ArrayList<>();
        try {
            CallableStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                      prepareCall(consultaSQL);
            consultaColaboracion.setInt(1, colaboracionDTO.getIdColaboracion());
            ResultSet resultado = consultaColaboracion.executeQuery();

            while (resultado.next()) {
                EstudianteDTO estudianteDTO = convertirEstudiante(resultado);
                listaEstudianteDTOS.add(estudianteDTO);
            }
            resultado.close();
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener los estudiantes participantes en la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaEstudianteDTOS;
    }

    /**
     * Obtiene la lista de académicos participantes en una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @return la lista de académicos.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<AcademicoDTO> getAcademicosParticipantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE idColaboracion = ? AND (estadoAcademico = 'anfitrion' OR estadoAcademico = 'aceptado')";
        List<AcademicoDTO> listaAcademicoDTOS = new ArrayList<>();
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                           prepareStatement(consultaSQL);
            consultaColaboracion.setInt(1, colaboracionDTO.getIdColaboracion());

            ResultSet resultado = consultaColaboracion.executeQuery();
            while (resultado.next()) {
                AcademicoDTO academicoDTO = convertirAcademico(resultado);
                listaAcademicoDTOS.add(academicoDTO);
            }
            resultado.close();
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtneer lo academicos participantes en la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaAcademicoDTOS;
    }

    /**
     * Obtiene la lista de colaboraciones dentro de un periodo.
     *
     * @param periodoDTO el periodo.
     * @return la lista de colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<ColaboracionDTO> getColaboracionPorPeriodo (PeriodoDTO periodoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE fechaInicio = ? AND fechaFin = ?";

        List<ColaboracionDTO> listaColaboracionDTO = new ArrayList<>();
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                                             prepareStatement(consultaSQL);
            consultaColaboracion.setDate(1, Date.valueOf(periodoDTO.getFechaInicio()));
            consultaColaboracion.setDate(2, Date.valueOf(periodoDTO.getFechaFin()));

            ResultSet resultado = consultaColaboracion.executeQuery();

            procesarResultadosColaboracionConLista(resultado, listaColaboracionDTO);

            consultaColaboracion.close();
            resultado.close();

        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las colaboraciones por periodoDTO", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracionDTO;
    }

    /**
     * Obtiene la lista de colaboraciones por idioma.
     *
     * @param idioma el idioma.
     * @return la lista de colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<ColaboracionDTO> getColaboracionPorIdioma (String idioma) throws ErrorDAO {
        String consultaSQL = "SELECT * from vista_colaboracion_con_academico WHERE idioma = ?";
        List<ColaboracionDTO> listaColaboracionDTO = new ArrayList<>();

        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                                            prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, idioma);

            ResultSet resultadoColaboracionPorIdioma = consultaColaboracion.executeQuery();

            procesarResultadosColaboracionConLista(resultadoColaboracionPorIdioma, listaColaboracionDTO);
            consultaColaboracion.close();
            resultadoColaboracionPorIdioma.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las colaboraciones por idioma", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracionDTO;
    }

    /**
     * Obtiene la lista de colaboraciones por estado.
     *
     * @param estado el estado.
     * @return la lista de colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<ColaboracionDTO> getColaboracionPorEstado (String estado) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = ?";
        List<ColaboracionDTO> listaColaboraciones = new ArrayList<>();

        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                                            prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, estado);

            ResultSet resultado = consultaColaboracion.executeQuery();

            procesarResultadosColaboracionConLista(resultado, listaColaboraciones);
            resultado.close();
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return listaColaboraciones;
    }

    /**
     * Cambia el estado de una colaboración.
     *
     * @param nuevoEstado el nuevo estado.
     * @param idColaboracion el ID de la colaboración.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int cambiarEstadoColaboracion (String nuevoEstado, int idColaboracion) throws ErrorDAO {
        String actualizarSQL = "UPDATE colaboracion SET estado = ? WHERE idColaboracion = ?";
        int filasAfectadas;

        try {
            PreparedStatement actualizarColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                prepareStatement(actualizarSQL);
            actualizarColaboracion.setString(1, nuevoEstado);
            actualizarColaboracion.setInt(2, idColaboracion);
            filasAfectadas = actualizarColaboracion.executeUpdate();
            actualizarColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al cambiar el estado de la colaboración", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Agrega un estudiante a una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @param estudianteDTO el estudiante.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO {
        String insercionSQL = "INSERT INTO estudiantesColaboracion (idEstudiante, idColaboracion) VALUES (?, ?)";
        int filasAfectadas;

        try {

            PreparedStatement insercionColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                     prepareStatement(insercionSQL);
            insercionColaboracion.setInt(1, estudianteDTO.getIdEstudiante());
            insercionColaboracion.setInt(2, colaboracionDTO.getIdColaboracion());

            filasAfectadas = insercionColaboracion.executeUpdate();

            insercionColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("El error al agregar un estudianteDTO a la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;

    }

    /**
     * Registra una solicitud de participación de un académico en una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @param academicoDTO el académico.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int registrarSolicitudParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String agregarAcademicoAColaboracionSQL = "INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (?, ?, 'pendiente')";
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
            BITACORA.warn(error);
            throw new ErrorDAO("Error al agregar a un académic a una colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Obtiene la colaboración activa de un académico.
     *
     * @param academicoDTO el académico.
     * @return un Optional que contiene la colaboración si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'activa' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirResultSetAColaboracionDTO(resultado);
                getAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboracion activa por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Obtiene la colaboración aceptada de un académico.
     *
     * @param academicoDTO el académico.
     * @return un Optional que contiene la colaboración aceptada si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    public Optional<ColaboracionDTO> getColaboracionAceptadaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'aceptada' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                      .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                getAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboracion activa por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Obtiene la propuesta de colaboración de un académico.
     *
     * @param academicoDTO el académico.
     * @return un Optional que contiene la colaboración si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getPropuestaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'propuesta' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                      .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                getAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboracion activa por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Registra una propuesta de colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @param academicoDTO el académico.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int registrarPropuestaColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String insercionColaboracionSQL = "INSERT INTO colaboracion (temaInteres, objetivo, estado) VALUES (?,?,?)";
        String insercionAcademicoEnColaboracionSQL = "INSERT INTO academicoDesarrolla (idColaboracion, idAcademico, estado) VALUES (?, ?, ?)";
        int filasAfectadas;
        int idGenerado = -1;
        try {
            AdministradorBaseDatos.getInstancia()
                                  .setAutoCommit(false);
            PreparedStatement insercionColaboracion = AdministradorBaseDatos.getInstancia()
                                                                         .prepareStatement(insercionColaboracionSQL, Statement.RETURN_GENERATED_KEYS);
            insercionColaboracion.setString(1, colaboracionDTO.getTemaInteres());
            insercionColaboracion.setString(2, colaboracionDTO.getObjetivo());
            insercionColaboracion.setString(3, ColaboracionDTO.EstadoColaboracion.propuesta.toString());
            filasAfectadas = insercionColaboracion.executeUpdate();
            ResultSet resultado = insercionColaboracion.getGeneratedKeys();
            while (resultado.next()) {
                idGenerado = resultado.getInt(1);
            }

            PreparedStatement insercionAcademicoEnColaboracion = AdministradorBaseDatos.getInstancia()
                                                                       .prepareStatement(insercionAcademicoEnColaboracionSQL);
            insercionAcademicoEnColaboracion.setInt(1, idGenerado);
            insercionAcademicoEnColaboracion.setString(2, academicoDTO.getCedulaProfesional());
            insercionAcademicoEnColaboracion.setString(3, "anfitrion");
            filasAfectadas += insercionAcademicoEnColaboracion.executeUpdate();

            AdministradorBaseDatos.getInstancia()
                                  .commit();

        }
        catch (SQLException error) {
            BITACORA.warn(error);
            AdministradorBaseDatos.rollback();
            throw new ErrorDAO("Error al ingresar la propuesta de colaboracion y vincularla", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Obtiene la lista de propuestas de colaboración.
     *
     * @return la lista de colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<ColaboracionDTO> getPropuestasColaboracion () throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND estado = 'propuesta'";
        List<ColaboracionDTO> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                    .prepareStatement(consultaSQL);
            ResultSet resultado = consultaColaboracion.executeQuery();
            while (resultado.next()) {
                listaColaboracion.add(convertirPropuesta(resultado));
            }
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la propuesta de colaboracion", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracion;
    }

    /**
     * Obtiene la lista de colaboraciones disponibles para un académico en una universidad.
     *
     * @param cedulaProfesional la cédula profesional del académico.
     * @param idUniversidad el ID de la universidad.
     * @return la lista de colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<ColaboracionDTO> getColaboracionesDisponibles (String cedulaProfesional, int idUniversidad) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND estado = 'disponible' AND cedulaProfesional != ? AND idUniversidad != ?";
        List<ColaboracionDTO> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                        .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, cedulaProfesional);
            consultaColaboracion.setInt(2, idUniversidad);
            ResultSet resultado = consultaColaboracion.executeQuery();
            while (resultado.next()) {
                ColaboracionDTO colaboracionDTO = convertirResultSetAColaboracionDTO(resultado);
                getAcademico(colaboracionDTO, resultado);
                listaColaboracion.add(colaboracionDTO);
            }
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las colaboraciones disponibles", ErrorDAO.Tipo.CONSULTA);
        }
        return listaColaboracion;
    }

    /**
     * Obtiene la colaboración actual de un académico por su cédula profesional.
     *
     * @param cedulaProfesional la cédula profesional del académico.
     * @return un Optional que contiene la colaboración actual si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getColaboracionActualPorAcademico (String cedulaProfesional) {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE (estadoAcademico = 'anfitrion' OR estadoAcademico = 'aceptado') AND (estado = 'disponible' OR estado = 'vinculada' OR estado = 'activa' OR estado = 'enRevision') AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;

        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, cedulaProfesional);
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                colaboracionDTO.setIdioma(resultado.getString("idioma"));
                colaboracionDTO.setTipo(ColaboracionDTO.TipoColaboracion.valueOf(resultado.getString("tipo")));
                colaboracionDTO.setPerfilEstudiante(resultado.getString("perfilEstudiante"));
                Date fechaInicio = resultado.getDate("fechaInicio");
                Date fechaFin = resultado.getDate("fechaFin");

                if (fechaInicio != null && fechaFin != null) {
                    colaboracionDTO.setPeriodo(new PeriodoDTO(fechaInicio.toLocalDate(), fechaFin.toLocalDate()));
                }
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las colaboracion actual del académico", ErrorDAO.Tipo.CONSULTA);
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Obtiene la colaboración disponible para un académico por su cédula profesional.
     *
     * @param cedulaProfesional la cédula profesional del académico.
     * @return un Optional que contiene la colaboración si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getColaboracionDisponiblePorAcademico (String cedulaProfesional) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'anfitrion' AND estado = 'disponible' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, cedulaProfesional);
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirPropuesta(resultado);
                getAcademico(colaboracionDTO, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las colaboraciones disponibles", ErrorDAO.Tipo.CONSULTA);
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Verifica si existe una solicitud previa de un académico en una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @param academicoDTO el académico.
     * @return true si existe una solicitud previa, false de lo contrario.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public boolean existeUnaSolicitudPrevia (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE idColaboracion = ? AND cedulaProfesional = ? AND estadoAcademico = 'pendiente'";
        Optional<ColaboracionDTO> colaboracion = Optional.empty();
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                       .prepareStatement(consultaSQL);
            consultaColaboracion.setInt(1, colaboracionDTO.getIdColaboracion());
            consultaColaboracion.setString(2, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();
            if (resultado.next()) {
                colaboracion = Optional.of(convertirResultSetAColaboracionDTO(resultado));
            }
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al comprobar si existe una solicitud previa", ErrorDAO.Tipo.CONSULTA);
        }
        return colaboracion.isPresent();
    }


    /**
     * Obtiene la lista de solicitudes de académicos para una colaboración.
     *
     * @param idColaboracion el ID de la colaboración.
     * @return la lista de académicos.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<AcademicoDTO> getSolicitudAcademicoColaboracion (int idColaboracion) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'pendiente' AND idColaboracion = ?";
        List<AcademicoDTO> listaAcademico = new ArrayList<>();
        try {
            PreparedStatement consultaAcademico = AdministradorBaseDatos.getInstancia()
                                                                       .prepareStatement(consultaSQL);
            consultaAcademico.setInt(1, idColaboracion);
            ResultSet resultado = consultaAcademico.executeQuery();
            while (resultado.next()) {
                listaAcademico.add(convertirAcademico(resultado));
            }
            consultaAcademico.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las solicitudes de la colaboracion", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaAcademico;
    }

    /**
     * Obtiene la lista de solicitudes de un académico.
     *
     * @param academicoDTO el académico.
     * @return la lista de colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public List<ColaboracionDTO> getSolicitudesDeAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'pendiente' AND cedulaProfesional = ?";
        List<ColaboracionDTO> listaColaboracion = new ArrayList<>();
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();
            while (resultado.next()) {
                listaColaboracion.add(convertirResultSetAColaboracionDTO(resultado));
            }
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener las solicitudes de la colaboracion", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaColaboracion;
    }

    /**
     * Actualiza el estado de una solicitud de participación.
     *
     * @param idColaboracion el ID de la colaboración.
     * @param cedulaProfesional el ID del académico.
     * @param nuevoEstado el nuevo estado.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int actualizarEstadoSolicitudDeParticipacion (int idColaboracion, String cedulaProfesional, String nuevoEstado) throws ErrorDAO {
        String actualizacionSQL = "UPDATE academicoDesarrolla SET estado = ? WHERE idColaboracion = ? AND idAcademico = ?";
        int filasAfectadas;

        try {
            PreparedStatement actualizacionColaboracion = AdministradorBaseDatos.getInstancia()
                                                                 .prepareStatement(actualizacionSQL);
            actualizacionColaboracion.setString(1, nuevoEstado);
            actualizacionColaboracion.setInt(2, idColaboracion);
            actualizacionColaboracion.setString(3, cedulaProfesional);

            filasAfectadas = actualizacionColaboracion.executeUpdate();

            actualizacionColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al actualizar el estado de la solicitud de participación", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Rechaza otras solicitudes de participación de un académico en colaboraciones distintas a la especificada.
     *
     * @param idColaboracion el ID de la colaboración actual.
     * @param cedulaProfesional la cédula profesional del académico.
     * @return el número de filas afectadas por la actualización.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int rechazarOtrasSolicitudesDeParticipacion (int idColaboracion, String cedulaProfesional) throws ErrorDAO {
        String actualizacionSQL = "UPDATE academicoDesarrolla SET estado = 'rechazado' WHERE idAcademico = ? AND idColaboracion != ?";
        int filasAfectadas;

        try {
            PreparedStatement actualizacionSolicitud = AdministradorBaseDatos.getInstancia()
                                                                     .prepareStatement(actualizacionSQL);
            actualizacionSolicitud.setString(1, cedulaProfesional);
            actualizacionSolicitud.setInt(2, idColaboracion);

            filasAfectadas = actualizacionSolicitud.executeUpdate();

            actualizacionSolicitud.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al actualizar las solicitudes de participación", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Elimina una solicitud de participación de un académico en una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @param academicoDTO el académico.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int eliminarSolicitudDeParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String actualizacionSQL = "Update academicoDesarrolla SET estado = 'rechazado' WHERE idColaboracion = ? AND idAcademico = ?";
        int filasAfectadas;

        try {
            PreparedStatement actualizacionSolicitud = AdministradorBaseDatos.getInstancia()
                                                                   .prepareStatement(actualizacionSQL);
            actualizacionSolicitud.setInt(1, colaboracionDTO.getIdColaboracion());
            actualizacionSolicitud.setString(2, academicoDTO.getCedulaProfesional());

            filasAfectadas = actualizacionSolicitud.executeUpdate();

            actualizacionSolicitud.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al eliminar la solicitud de participación", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }


    @Override
    public int agregar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_Colaboracion(?,?,?,?,?,?,?,?)}";
        int filasAfectadas;

        try {

            CallableStatement procedimientoColaboracion = AdministradorBaseDatos.getInstancia().
                                                                                         prepareCall(procedimientoSQL);
            procedimientoColaboracion.setString(1, colaboracionDTO.getEstado()
                                                                           .name());
            procedimientoColaboracion.setString(2, colaboracionDTO.getTipo()
                                                                           .name());
            procedimientoColaboracion.setString(3, colaboracionDTO.getTemaInteres());
            procedimientoColaboracion.setString(4, colaboracionDTO.getIdioma());
            procedimientoColaboracion.setString(5, colaboracionDTO.getObjetivo());
            procedimientoColaboracion.setDate(6, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                      getFechaInicio()));
            procedimientoColaboracion.setDate(7, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                      getFechaFin()));
            procedimientoColaboracion.setString(8, colaboracionDTO.getPerfilEstudiante());

            filasAfectadas = procedimientoColaboracion.executeUpdate();

            procedimientoColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al registrar la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    @Override
    public int modificar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL actualizar_Colaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int filasAfectadas;
        try {

            CallableStatement procedimientoColaboracion = AdministradorBaseDatos.getInstancia()
                                                                                          .prepareCall(procedimientoSQL);

            procedimientoColaboracion.setInt(1, colaboracionDTO.getIdColaboracion());
            procedimientoColaboracion.setString(2, colaboracionDTO.getEstado()
                                                                            .name());
            procedimientoColaboracion.setString(3, colaboracionDTO.getTipo()
                                                                            .name());
            procedimientoColaboracion.setString(4, colaboracionDTO.getTemaInteres());
            procedimientoColaboracion.setString(5, colaboracionDTO.getIdioma());
            procedimientoColaboracion.setString(6, colaboracionDTO.getObjetivo());
            if (colaboracionDTO.getPeriodo() != null) {
                procedimientoColaboracion.setDate(7, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                           getFechaInicio()));
            }
            else {
                procedimientoColaboracion.setDate(7, null);
            }
            if (colaboracionDTO.getPeriodo() != null) {
                procedimientoColaboracion.setDate(8, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                           getFechaFin()));
            }
            else {
                procedimientoColaboracion.setDate(8, null);
            }
            procedimientoColaboracion.setString(9, colaboracionDTO.getPerfilEstudiante());

            filasAfectadas = procedimientoColaboracion.executeUpdate();

            procedimientoColaboracion.close();

        }
        catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("Error al actualizar la colaboración", ErrorDAO.Tipo.MODIFICACION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }


    @Override
    public Optional<ColaboracionDTO> getPorId (Integer id) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE idColaboracion = ?";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                               prepareStatement(consultaSQL);
            consultaColaboracion.setInt(1, id);

            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracionDTO = convertirResultSetAColaboracionDTO(resultado);
            }
            consultaColaboracion.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboración", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracionDTO);
    }

    /**
     * Obtiene el académico par de una colaboración.
     *
     * @param colaboracionDTO la colaboración.
     * @return un Optional que contiene el académico si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<AcademicoDTO> getAcademicoPar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estadoAcademico = 'aceptado' AND idColaboracion = ?";
        AcademicoDTO academico = null;
        try {
            PreparedStatement consultaAcademico = AdministradorBaseDatos.getInstancia()
                                                                      .prepareStatement(consultaSQL);
            consultaAcademico.setInt(1, colaboracionDTO.getIdColaboracion());
            ResultSet resultado = consultaAcademico.executeQuery();

            if (resultado.next()) {
                academico = convertirAcademico(resultado);
            }
            consultaAcademico.close();
            resultado.close();

        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener al académico par", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(academico);
    }

    /**
     * Obtiene todas las colaboraciones.
     *
     * @return la lista de todas las colaboraciones.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    public List<ColaboracionDTO> getTodos () throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico";
        List<ColaboracionDTO> listaColaboracionDTO = new ArrayList<>();

        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia().
                                                               prepareStatement(consultaSQL);
            ResultSet resultado = consultaColaboracion.executeQuery();

            procesarResultadosColaboracionConLista(resultado, listaColaboracionDTO);

            resultado.close();
            consultaColaboracion.close();
        }
        catch (SQLException error) {
            BITACORA.info(error);
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

    private static void getAcademico (ColaboracionDTO colaboracionDTO, ResultSet resultado) throws SQLException {
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
        estudianteDTO.setApellidos(resultado.getString("apellidos"));
        estudianteDTO.setIdEstudiante(resultado.getInt("idEstudiante"));
        estudianteDTO.setMatricula(resultado.getString("matricula"));
        estudianteDTO.setIdUniversidad(resultado.getInt("universidad"));

        return estudianteDTO;
    }

    private static AcademicoDTO convertirAcademico (ResultSet resultado) throws SQLException {
        AcademicoDTO academicoDTO = new AcademicoDTO();

        academicoDTO.setIdPersona(resultado.getInt("idPersona"));
        academicoDTO.setNombre(resultado.getString("nombre"));
        academicoDTO.setApellidos(resultado.getString("apellidos"));
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
            getAcademico(colaboracionDTOActual, resultados);
        }
    }

    /**
     * Obtiene la numeralia de colaboraciones por región en un periodo dado.
     *
     * @param periodo el periodo.
     * @return un mapa con la numeralia por región.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Map<String, int[]> getNumeraliaRegion (PeriodoDTO periodo) throws ErrorDAO {
        String numeraliaRegionSQL = "{CALL numeralia_region(?,?)}";
        return ejecutarConsultaNumeralia(numeraliaRegionSQL, periodo);
    }

    /**
     * Obtiene la numeralia de colaboraciones por área académica en un periodo dado.
     *
     * @param periodo el periodo.
     * @return un mapa con la numeralia por área académica.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Map<String, int[]> getNumeraliaAreaAcademica (PeriodoDTO periodo) throws ErrorDAO {
        String numeraliaAreaAcademicaSQL = "{CALL numeralia_area_academica(?,?)}";
        return ejecutarConsultaNumeralia(numeraliaAreaAcademicaSQL, periodo);
    }


    /**
     * Obtiene la fecha de la colaboración más antigua.
     *
     * @return un Optional que contiene la fecha de la colaboración más antigua si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<LocalDate> getFechaColaboracionMasAntigua () throws ErrorDAO {
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
                    int anio = Integer.parseInt(fechaString.substring(0, 4));
                    int mes = Integer.parseInt(fechaString.substring(5, 7));
                    int dia = Integer.parseInt(fechaString.substring(8, 10));
                    fechaMasAntigua = LocalDate.of(anio, mes, dia);
                }
            }

            consulta.close();
            resultado.close();
            AdministradorBaseDatos.desconectar();
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al consultar colaboraciones", ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.ofNullable(fechaMasAntigua);
    }

    private Map<String, int[]> ejecutarConsultaNumeralia (String consultaSQL, PeriodoDTO periodo) throws ErrorDAO {
        Map<String, int[]> numeralia = new HashMap<>();
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
            BITACORA.warn(error);
            throw new ErrorDAO("Error al obtener la numeralia", ErrorDAO.Tipo.CONSULTA);
        }
        return numeralia;
    }

    private Map<String, int[]> convertirResultSetNumeralia (ResultSet resultSet) throws SQLException {
        Map<String, int[]> numeralia = new HashMap<>();
        do {
            String categoria = resultSet.getString(1);
            int alumnos = resultSet.getInt("alumnos");
            int profesores = resultSet.getInt("profesores");
            int[] cantidad = new int[]{alumnos, profesores};
            numeralia.put(categoria, cantidad);
        } while (resultSet.next());
        return numeralia;
    }

    /**
     * Obtiene la colaboración vinculada para un académico.
     *
     * @param academicoDTO el académico.
     * @return un Optional que contiene la colaboración si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getEnRevisionPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'enRevision' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracion = null;

        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                    .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracion = convertirResultSetAColaboracionDTO(resultado);
                getAcademico(colaboracion, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboracion en estaod \"vinculada\" por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracion);
    }

    /**
     * Obtiene la colaboración vinculada para un académico.
     *
     * @param academicoDTO el académico.
     * @return un Optional que contiene la colaboración si se encuentra, de lo contrario está vacío.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public Optional<ColaboracionDTO> getVinculadaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        String consultaSQL = "SELECT * FROM vista_colaboracion_con_academico WHERE estado = 'vinculada' AND cedulaProfesional = ?";
        ColaboracionDTO colaboracion = null;

        try {
            PreparedStatement consultaColaboracion = AdministradorBaseDatos.getInstancia()
                                                                          .prepareStatement(consultaSQL);
            consultaColaboracion.setString(1, academicoDTO.getCedulaProfesional());
            ResultSet resultado = consultaColaboracion.executeQuery();

            if (resultado.next()) {
                colaboracion = convertirResultSetAColaboracionDTO(resultado);
                getAcademico(colaboracion, resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.info(error);
            throw new ErrorDAO("Error al obtener la colaboracion en estaod \"vinculada\" por academico", ErrorDAO.Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(colaboracion);
    }

    /**
     * Retira un estudiante de una colaboración.
     *
     * @param colaboracion la colaboración.
     * @param estudiante el estudiante.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error de acceso a datos.
     */
    @Override
    public int retirarEstudianteDeColaboracion (ColaboracionDTO colaboracion, EstudianteDTO estudiante) throws ErrorDAO {
        String retirarEstudianteDeColaboracionSQL = "DELETE FROM estudiantesColaboracion WHERE idColaboracion = ? AND idEstudiante = ?";
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
            BITACORA.warn(error);
            throw new ErrorDAO("El error al retirar el estudiante " + estudiante.getMatricula() + " de la colaboración", ErrorDAO.Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }
}
