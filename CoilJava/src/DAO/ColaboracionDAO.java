package DAO;

import DAO.Interfaces.IColaboracionDAO;
import DTO.*;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ColaboracionDAO implements IColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(ColaboracionDAO.class);

    @Override
    public Optional<ColaboracionDTO> getColaboracionPorAcademicosParticipantes (AcademicoDTO academicoDTO1, AcademicoDTO academicoDTO2) throws ErrorDAO {
        String getColaboracionPorAcademicoSQL = "{CALL obtener_colaboracion_academicos(?,?)}";
        ColaboracionDTO colaboracionDTO = null;

        try {
            CallableStatement getColaboracionPorAcademico = AdministradorBaseDatos.getInstancia().
                                                                                  prepareCall(getColaboracionPorAcademicoSQL);
            getColaboracionPorAcademico.setString(1, academicoDTO1.getCedulaProfesional());
            getColaboracionPorAcademico.setString(2, academicoDTO2.getCedulaProfesional());

            ResultSet resultadoGetColaboracionPorAcademico = getColaboracionPorAcademico.executeQuery();

            if (resultadoGetColaboracionPorAcademico.next()) {
                colaboracionDTO = convertirColaboracion(resultadoGetColaboracionPorAcademico);
                colaboracionDTO.setAnfitrion(convertirAcademico(resultadoGetColaboracionPorAcademico));

                if (resultadoGetColaboracionPorAcademico.next()) {
                    colaboracionDTO.setAcademicoPar(convertirAcademico(resultadoGetColaboracionPorAcademico));
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
            AdministradorBaseDatos.desconectar();

        }
        return Optional.ofNullable(colaboracionDTO);
    }

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
                colaboracionDTO = convertirColaboracion(resultadoColaboracionPorId);
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
        String academicosParticipantes = "{CALL obtener_academicos_colaboracion(?)}";
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
    public int agregarAcademicoAColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        String agregarAcademicoAColaboracionSQL = "INSERT INTO academicodesarrolla (idColaboracion, idAcademico) VALUES (?, ?)";
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
        return Optional.empty();
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
            procedimientoActualizarColaboracion.setDate(7, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                       getFechaInicio()));
            procedimientoActualizarColaboracion.setDate(8, Date.valueOf(colaboracionDTO.getPeriodo().
                                                                                       getFechaFin()));
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
        String getPorIdSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
                WHERE c.idColaboracion = ?""";
        ColaboracionDTO colaboracionDTO = null;
        try {
            PreparedStatement getPorId = AdministradorBaseDatos.getInstancia().
                                                               prepareStatement(getPorIdSQL);
            getPorId.setInt(1, id);

            ResultSet resultadoGetPorId = getPorId.executeQuery();

            if (resultadoGetPorId.next()) {
                colaboracionDTO = convertirColaboracion(resultadoGetPorId);
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

    public List<ColaboracionDTO> getTodos () throws ErrorDAO {
        String getTodosSQL = """
                SELECT c.*, va.*
                FROM colaboracion c
                LEFT JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
                LEFT JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional""";
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

    @Override
    public ColaboracionDTO resultSetAObjeto (ResultSet resultados) {
        return null;
    }


    private static ColaboracionDTO convertirColaboracion (ResultSet resultado) throws SQLException {
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
        periodoDTO.setFechaInicio(resultado.getDate("fechaInicio").
                                           toLocalDate());
        periodoDTO.setFechaFin(resultado.getDate("fechaFin").
                                        toLocalDate());

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
        while (resultados.next()) {
            ColaboracionDTO colaboracionDTO = convertirColaboracion(resultados);
            AcademicoDTO academicoDTO = convertirAcademico(resultados);

            if (!listaColaboracionDTO.isEmpty()) {
                ColaboracionDTO colaboracionDTOActual = listaColaboracionDTO.get(listaColaboracionDTO.size() - 1);
                if (colaboracionDTO.getIdColaboracion() == colaboracionDTOActual.getIdColaboracion()) {
                    if (colaboracionDTOActual.getAnfitrion() == null) {
                        colaboracionDTOActual.setAnfitrion(academicoDTO);
                    }
                    else {
                        colaboracionDTOActual.setAcademicoPar(academicoDTO);
                    }
                }
                else {
                    colaboracionDTO.setAnfitrion(academicoDTO);
                    listaColaboracionDTO.add(colaboracionDTO);
                }
            }
            else {
                listaColaboracionDTO.add(colaboracionDTO);
                colaboracionDTO.setAnfitrion(academicoDTO);
            }
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

    public Optional<Date> getFechaColaboracionMasAntigua () {
        Date fechaMasAntigua = null;
        String consultaSQL = "SELECT MIN(fechaFin) FROM numeralia;";
        PreparedStatement consulta;
        ResultSet resultado;

        try {
            consulta = AdministradorBaseDatos.getInstancia()
                                             .prepareStatement(consultaSQL);
            resultado = consulta.executeQuery();

            if (resultado.next()) {
                fechaMasAntigua = resultado.getDate(1);
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

    private Map<String, int[]> ejecutarConsultaNumeralia (String consultaSQL, PeriodoDTO periodo) throws ErrorDAO {
        Map<String, int[]> numeralia;
        CallableStatement llamadaProcedimiento;
        ResultSet resultado;

        try {
            llamadaProcedimiento = AdministradorBaseDatos.getInstancia()
                                                         .prepareCall(consultaSQL);
            llamadaProcedimiento.setDate(1, Date.valueOf(periodo.getFechaInicio()));
            llamadaProcedimiento.setDate(2, Date.valueOf(periodo.getFechaFin()));
            resultado = llamadaProcedimiento.executeQuery();

            numeralia = convertirResultSetNumeralia(resultado);

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

    private Map<String, int[]> convertirResultSetNumeralia (ResultSet resultSet) throws SQLException {
        Map<String, int[]> numeralia = new HashMap<>();
        while (resultSet.next()) {
            String categoria = resultSet.getString(1);
            int alumnos = resultSet.getInt("alumnos");
            int profesores = resultSet.getInt("profesores");
            int[] cantidad = new int[]{alumnos, profesores};
            numeralia.put(categoria, cantidad);
        }
        return numeralia;
    }
}
