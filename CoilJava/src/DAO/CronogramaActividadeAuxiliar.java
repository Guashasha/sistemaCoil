package DAO;

import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IDAO;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CronogramaActividadeAuxiliar implements IDAO<ActividadVinculadaDTO, Integer> {
    private static final Logger BITACORA = Logger.getLogger(CronogramaActividadeAuxiliar.class);

    @Override
    public int agregar (ActividadVinculadaDTO actividadVinculadaDTO) throws ErrorDAO {
        if (!actividadVinculadaDTO.getPeriodo()
                .esCorrecto()) {
            throw new ErrorDAO("El periodo especificado es incorrecto.", ErrorDAO.Tipo.VALIDACION);
        } else if (!actividadVinculadaDTO.getActividad()
                .esCorrecta()) {
            throw new ErrorDAO("La actividad es incorrecta", ErrorDAO.Tipo.VALIDACION);
        } else if (!actividadVinculadaDTO.getColaboracion()
                .esValido()) {
            throw new ErrorDAO("La colaboración es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }

        int resultado = -1;

        try {
            resultado = CronogramaActividadDAO.agregar(actividadVinculadaDTO.getActividad(), actividadVinculadaDTO.getColaboracion(), actividadVinculadaDTO.getPeriodo());
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error de sql: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        if (resultado < -1) {
            throw new ErrorDAO("No se pudo registrar la actividad al cronograma", ErrorDAO.Tipo.INSERCION);
        }

        return 1;
    }

    @Override
    public int modificar (ActividadVinculadaDTO actividadVinculadaDTO) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("La función no está implementada en la clase CronogramaActividadeAuxiliar");
    }

    @Override
    public Optional<ActividadVinculadaDTO> getPorId (Integer id) throws ErrorDAO {
        throw new ErrorDAO("La función no está implementada en la clase CronogramaActividadeAuxiliar", ErrorDAO.Tipo.CONSULTA);
    }

    public Optional<ActividadVinculadaDTO> getPorActividadYColaboracion (int idActividad, int idColaboracion) {
        ResultSet rsActividad = null;

        try {
            rsActividad = CronogramaActividadDAO.getPorActividadYColaboracion(idActividad, idColaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error de conexion a la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        ActividadVinculadaDTO actividad = null;

        try {
            if (rsActividad != null && rsActividad.next()) {
                actividad = resultSetAObjeto(rsActividad);

                rsActividad.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(actividad);
    }

    @Override
    public List<ActividadVinculadaDTO> getTodos () throws ErrorDAO {
        ResultSet actividades;

        try {
            actividades = CronogramaActividadDAO.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error al recuperar las actividades", ErrorDAO.Tipo.CONEXION);
        }

        List<ActividadVinculadaDTO> actividadesLista = new ArrayList<>();

        if (actividades == null) {
            return actividadesLista;
        }

        try {
            while (actividades.next()) {
                ActividadVinculadaDTO actividadVinculadaDTO = resultSetAObjeto(actividades);

                if (actividadVinculadaDTO.esCorrecto()) {
                    actividadesLista.add(actividadVinculadaDTO);
                }
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error al recuperar las actividades de la colaboración", ErrorDAO.Tipo.CONEXION);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return actividadesLista;
    }

    @Override
    public ActividadVinculadaDTO resultSetAObjeto (ResultSet resultados) {
        ActividadVinculadaDTO actividadVinculadaDTO = null;
        try {
            ActividadAuxiliar actividadAuxiliar = new ActividadAuxiliar();
            ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();

            int id = resultados.getInt(1);
            Optional<ActividadDTO> actividad = actividadAuxiliar.getPorId(resultados.getInt(2));
            Optional<ColaboracionDTO> colaboracion = colaboracionAuxiliar.getPorId(resultados.getInt(3));
            LocalDate fechaInicio = resultados.getDate(4)
                    .toLocalDate();
            LocalDate fechaFin = resultados.getDate(5)
                    .toLocalDate();

            if (actividad.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            if (colaboracion.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            actividadVinculadaDTO = new ActividadVinculadaDTO(actividad.get(), colaboracion.get(), new PeriodoDTO(fechaInicio, fechaFin));
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return actividadVinculadaDTO;
    }
}
