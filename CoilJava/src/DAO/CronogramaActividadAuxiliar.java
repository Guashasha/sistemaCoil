package DAO;

import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class CronogramaActividadAuxiliar {
    private static final Logger BITACORA = Logger.getLogger(CronogramaActividadAuxiliar.class);

    /**
     * Valida y vincula una actividad con una colaboración, la actividad vinculada debe contener ambas, actividad y colaboración
     * @param actividadVinculadaDTO los datos de la actividad y la colaboración que serán vinculados
     * @return el numero de filas afectadas
     * @throws ErrorDAO tipo validación si la actividad o la colaboración son incorrectas, tipo inserción si no se pudieron vincular y tipo conexión si ocurre un error de sql
     */
    public int agregar (ActividadVinculadaDTO actividadVinculadaDTO) throws ErrorDAO {
        if (!actividadVinculadaDTO.getActividad()
                .esCorrecta()) {
            throw new ErrorDAO("La actividad es incorrecta", ErrorDAO.Tipo.VALIDACION);
        } else if (!actividadVinculadaDTO.getColaboracion()
                .esValido()) {
            throw new ErrorDAO("La colaboración es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }

        CronogramaActividadDAO cronogramaDAO = new CronogramaActividadDAO();

        if (cronogramaDAO.agregar(actividadVinculadaDTO) < 0) {
            throw new ErrorDAO("No se pudo registrar la actividad al cronograma", ErrorDAO.Tipo.INSERCION);
        }

        return 1;
    }

    /**
     * Desvincula una actividad de una colaboración
     * @param actividadVinculadaDTO los datos de la actividad que se desvinculará de la colaboración
     * @return el numero de filas afectadas
     * @throws ErrorDAO tipo validación si la actividad o la colaboración es incorrecta, tipo conexión si ocurre un error de sql
     */
    public int desvincular (ActividadVinculadaDTO actividadVinculadaDTO) throws ErrorDAO {
        if (!actividadVinculadaDTO.esCorrecto()) {
            throw new ErrorDAO("La actividad vinculada es incorrecta.", ErrorDAO.Tipo.VALIDACION);
        }

        int resultado = -1;
        CronogramaActividadDAO cronogramaDAO = new CronogramaActividadDAO();

        resultado = cronogramaDAO.desvincular(actividadVinculadaDTO);

        return resultado;
    }

    public int modificar (ActividadVinculadaDTO actividadVinculadaDTO) throws ErrorDAO {
        if (!actividadVinculadaDTO.esCorrecto()) {
            throw new ErrorDAO("La actividad vinculada es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }

        CronogramaActividadDAO cronogramaDAO = new CronogramaActividadDAO();

        if (cronogramaDAO.modificar(actividadVinculadaDTO) < 0) {
            throw new ErrorDAO("No se pudo modificar el periodo de la actividad", ErrorDAO.Tipo.INSERCION);
        }

        return 1;
    }

    public Optional<ActividadVinculadaDTO> getPorActividadYColaboracion (int idActividad, int idColaboracion) {
        Optional<ActividadVinculadaDTO> rsActividad;
        CronogramaActividadDAO cronogramaDAO = new CronogramaActividadDAO();

        try {
            rsActividad = cronogramaDAO.getPorActividadYColaboracion(idActividad, idColaboracion);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error de conexion a la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return rsActividad;
    }

    public List<ActividadVinculadaDTO> getTodos () throws ErrorDAO {
        CronogramaActividadDAO cronogramaDAO = new CronogramaActividadDAO();

        return cronogramaDAO.getTodos();
    }
}
