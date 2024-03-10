package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.Dominio.CursoTaller;
import Logica.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface ICursoTallerDAO extends IDAO<CursoTaller> {
    public Optional<CursoTaller> getCursoTallerPorId (int idCursoTaller) throws ErrorDAO;
    public CursoTaller getCursoTallerPorNombre (String nombreCurso) throws ErrorDAO;
    public List<Academico> getAcademicosParticipantes (CursoTaller cursoTaller) throws ErrorDAO;
}
