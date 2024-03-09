package Logica.Interfaces;

import Logica.Dominio.CursoTaller;
import Logica.ErrorDAO;

import java.util.List;

public interface ICursoTallerDAO {
    public List<CursoTaller> getCursosTerminados () throws ErrorDAO;
    public List<CursoTaller> getCursosVigentes () throws ErrorDAO;
    public void registrarCursoTaller (CursoTaller cursoTaller) throws ErrorDAO;
    public CursoTaller getCursoTallerPorId (int idCursoTaller) throws ErrorDAO;
}
