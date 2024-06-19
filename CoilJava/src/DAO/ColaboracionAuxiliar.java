package DAO;

import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * La clase ColaboracionAuxiliar se encarga de obtener información de las regiones en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 *
 * @author FerRMZ
 */
public class ColaboracionAuxiliar {
    private final ColaboracionDAO COLABORACION_DAO = new ColaboracionDAO();

    /**
     * Obtiene un objeto ColaboracionDTO basado en el ID de la colaboración.
     *
     * @param idColaboracion el ID de la colaboración por la cual se desea filtrar.
     * @return un objeto Optional que contiene el ColaboracionDTO que cumple con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Optional<ColaboracionDTO> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        if (esIdInvalido(idColaboracion)) {
            throw new ErrorDAO("Error en el identificador de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorId(idColaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos EstudianteDTO asociados a una colaboración.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO por el cual se desea filtrar.
     * @return una lista de objetos EstudianteDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<EstudianteDTO> getListaDeEstudiantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getListaDeEstudiantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos AcademicoDTO asociados a una colaboración.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO por el cual se desea filtrar.
     * @return una lista de objetos AcademicoDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<AcademicoDTO> getAcademicosParticipantes (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getAcademicosParticipantes(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos ColaboracionDTO basado en un periodo específico.
     *
     * @param periodoDTO el objeto PeriodoDTO por el cual se desea filtrar.
     * @return una lista de objetos ColaboracionDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<ColaboracionDTO> getColaboracionPorPeriodo (PeriodoDTO periodoDTO) {
        if (!periodoDTO.validarNulo()) {
            throw new ErrorDAO("PeriodoDTO con fecha de inicio o de fin vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorPeriodo(periodoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos ColaboracionDTO basado en el idioma.
     *
     * @param idioma el idioma por el cual se desea filtrar.
     * @return una lista de objetos ColaboracionDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<ColaboracionDTO> getColaboracionPorIdioma (String idioma) {
        if (esCadaInvalida(idioma)) {
            throw new ErrorDAO("Idioma invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorIdioma(idioma);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de objetos ColaboracionDTO basado en el estado.
     *
     * @param estado el estado por el cual se desea filtrar.
     * @return una lista de objetos ColaboracionDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<ColaboracionDTO> getColaboracionPorEstado (String estado) throws ErrorDAO {
        if (esCadaInvalida(estado)) {
            throw new ErrorDAO("PaisDTO invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getColaboracionPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Cambia el estado de una colaboración.
     *
     * @param nuevoEstado el nuevo estado de la colaboración.
     * @param idColaboracion el ID de la colaboración a cambiar el estado.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int cambiarEstadoColaboracion (String nuevoEstado, int idColaboracion) throws ErrorDAO {

        if (esIdInvalido(idColaboracion)) {
            throw new ErrorDAO("id de la colaboracionDTO invalido", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(nuevoEstado)) {
            throw new ErrorDAO("Error en el estado de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.cambiarEstadoColaboracion(nuevoEstado, idColaboracion);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Acepta una solicitud de participación en una colaboración.
     *
     * @param idColaboracion el ID de la colaboración.
     * @param cedulaProfesional la cédula profesional del académico.
     * @param nuevoEstado el nuevo estado de la colaboración.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int aceptarSolicitud (int idColaboracion, String cedulaProfesional, String nuevoEstado) throws ErrorDAO {
        int filasAfectadas = 0;

        try {
            filasAfectadas = COLABORACION_DAO.actualizarEstadoSolicitudDeParticipacion(idColaboracion, cedulaProfesional, nuevoEstado);
            filasAfectadas += COLABORACION_DAO.rechazarOtrasSolicitudesDeParticipacion(idColaboracion, cedulaProfesional);
            filasAfectadas += COLABORACION_DAO.cambiarEstadoColaboracion("vinculada", idColaboracion);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return filasAfectadas;
    }

    /**
     * Agrega un estudiante a una colaboración.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO de la colaboración.
     * @param estudianteDTO el objeto EstudianteDTO del estudiante.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int agregarEstudianteAColaboracion (ColaboracionDTO colaboracionDTO, EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(estudianteDTO.getIdEstudiante())) {
            throw new ErrorDAO("Error en el id del estudiante", ErrorDAO.Tipo.VALIDACION);
        }

        return COLABORACION_DAO.agregarEstudianteAColaboracion(colaboracionDTO, estudianteDTO);
    }

    /**
     * Registra una solicitud de participación en una colaboración.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO de la colaboración.
     * @param academicoDTO el objeto AcademicoDTO del académico.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int registrarSolicitudParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        if (COLABORACION_DAO.existeUnaSolicitudPrevia(colaboracionDTO, academicoDTO)) {
            throw new ErrorDAO("Ya has solicitado participar en esta colaboración", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(colaboracionDTO.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracionDTO", ErrorDAO.Tipo.VALIDACION);
        }
        if (esCadaInvalida(academicoDTO.getCedulaProfesional())) {
            throw new ErrorDAO("Error en la cedula del academicoDTO", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Registra una propuesta de colaboración.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO de la colaboración.
     * @param academicoDTO el objeto AcademicoDTO del académico.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int registrarPropuestaColaboracion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return COLABORACION_DAO.registrarPropuestaColaboracion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    /**
     * Obtiene una lista de todas las propuestas de colaboración.
     *
     * @return una lista de objetos ColaboracionDTO de todas las propuestas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<ColaboracionDTO> obtenerPropuestasColaboracion () throws ErrorDAO {
        try {
            return COLABORACION_DAO.getPropuestasColaboracion();
        }
        catch (ErrorDAO errorDAO) {
            throw new ErrorDAO(errorDAO.getMessage(), errorDAO.getTipo());
        }
    }

    /**
     * Obtiene una lista de colaboraciones disponibles para un académico específico en una universidad.
     *
     * @param cedulaProfesional la cédula profesional del académico.
     * @param idUniversidad el ID de la universidad.
     * @return una lista de objetos ColaboracionDTO de las colaboraciones disponibles.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<ColaboracionDTO> getColaboracionDisponible (String cedulaProfesional, int idUniversidad) throws ErrorDAO {
        try {
            return COLABORACION_DAO.getColaboracionesDisponibles(cedulaProfesional, idUniversidad);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
    }

    /**
     * Obtiene una colaboración activa para un académico específico.
     *
     * @param academicoDTO el objeto AcademicoDTO del académico.
     * @return un objeto Optional que contiene el ColaboracionDTO activo.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Optional<ColaboracionDTO> getActivaPorAcademico (AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return COLABORACION_DAO.getActivaPorAcademico(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
    }

    /**
     * Elimina una solicitud de participación en una colaboración.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO de la colaboración.
     * @param academicoDTO el objeto AcademicoDTO del académico.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int eliminarSolicitudDeParticipacion (ColaboracionDTO colaboracionDTO, AcademicoDTO academicoDTO) throws ErrorDAO {
        try {
            return COLABORACION_DAO.eliminarSolicitudDeParticipacion(colaboracionDTO, academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
    }

    /**
     * Agrega un objeto ColaboracionDTO.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO que se desea agregar.
     * @return el ID de la colaboración agregada.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int agregar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (!colaboracionDTO.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracionDTO esta vacia", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.agregar(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Modifica un objeto ColaboracionDTO.
     *
     * @param colaboracionDTO el objeto ColaboracionDTO que se desea modificar.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int modificar (ColaboracionDTO colaboracionDTO) throws ErrorDAO {
        if (!colaboracionDTO.esValido()) {
            throw new ErrorDAO("Al menos un dato de la colaboracionDTO esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.modificar(colaboracionDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene un objeto ColaboracionDTO basado en el ID.
     *
     * @param id el ID de la colaboración por la cual se desea filtrar.
     * @return un objeto Optional que contiene el ColaboracionDTO que cumple con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Optional<ColaboracionDTO> getPorId (Integer id) throws ErrorDAO {
        if (esIdInvalido(id)) {
            throw new ErrorDAO("Id invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return COLABORACION_DAO.getPorId(id);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene una lista de todas las colaboraciones.
     *
     * @return una lista de objetos ColaboracionDTO de todas las colaboraciones.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<ColaboracionDTO> getTodos () throws ErrorDAO {
        try {
            return COLABORACION_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Obtiene la numeralia de colaboraciones por región durante un periodo específico.
     *
     * @param periodo el objeto PeriodoDTO por el cual se desea filtrar.
     * @return un mapa con la numeralia por región.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Map<String, int[]> getNumeraliaRegion (PeriodoDTO periodo) throws ErrorDAO {
        Map<String, int[]> numeralia;

        if (periodo.validarNulo()) {
            numeralia = COLABORACION_DAO.getNumeraliaRegion(periodo);
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        return numeralia;
    }

    /**
     * Retira a un estudiante de una colaboración.
     *
     * @param colaboracion el objeto ColaboracionDTO de la colaboración.
     * @param estudiante el objeto EstudianteDTO del estudiante.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int retirarEstudianteDeColaboracion (ColaboracionDTO colaboracion, EstudianteDTO estudiante) throws ErrorDAO {
        if (esIdInvalido(colaboracion.getIdColaboracion())) {
            throw new ErrorDAO("Error en el id de la colaboracion", ErrorDAO.Tipo.VALIDACION);
        }
        if (esIdInvalido(estudiante.getIdEstudiante())) {
            throw new ErrorDAO("Error en el id del estudiante", ErrorDAO.Tipo.VALIDACION);
        }

        return COLABORACION_DAO.retirarEstudianteDeColaboracion(colaboracion, estudiante);
    }

    /**
     * Obtiene la numeralia de colaboraciones por área académica durante un periodo específico.
     *
     * @param periodo el objeto PeriodoDTO por el cual se desea filtrar.
     * @return un mapa con la numeralia por área académica.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Map<String, int[]> getNumeraliaAreaAcademica (PeriodoDTO periodo) throws ErrorDAO {
        Map<String, int[]> numeralia;

        if (periodo.validarNulo()) {
            numeralia = COLABORACION_DAO.getNumeraliaAreaAcademica(periodo);
        }
        else {
            throw new ErrorDAO("Ocurrió un error. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        return numeralia;
    }

    private boolean esCadaInvalida (String cadena) {
        return cadena == null || cadena.isBlank();
    }

    private boolean esIdInvalido (int id) {
        return id <= 0;
    }
}
