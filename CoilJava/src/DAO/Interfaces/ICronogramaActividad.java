package DAO.Interfaces;

import DTO.ActividadVinculadaDTO;
import Utilidades.ErrorDAO;

import java.util.Optional;

public interface ICronogramaActividad extends IDAO<ActividadVinculadaDTO, Integer> {
    Optional<ActividadVinculadaDTO> getPorActividadYColaboracion(int idActividad, int idColaboracion) throws ErrorDAO;
    int desvincular (ActividadVinculadaDTO actividadVinculada) throws ErrorDAO;
}
