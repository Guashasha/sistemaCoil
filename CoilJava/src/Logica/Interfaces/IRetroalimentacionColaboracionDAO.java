package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.Dominio.Retroalimentacion;
import Logica.Dominio.RetroalimentacionColaboracion;
import Logica.ErrorDAO;

import java.util.Optional;

public interface IRetroalimentacionColaboracionDAO extends IDAO<RetroalimentacionColaboracion, Integer> {
    public Optional<RetroalimentacionColaboracion> getPorAcademicosInvolucrados (Academico academico1, Academico academico2);
}
