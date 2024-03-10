package Logica.DAO;

import Logica.Dominio.Academico;
import Logica.Dominio.CursoTaller;
import Logica.ErrorDAO;
import Logica.Interfaces.IColaboracionDAO;
import Logica.Interfaces.ICursoTallerDAO;

import java.util.List;
import java.util.Optional;

public class DAOCursoTaller implements ICursoTallerDAO {
    @Override
    public Optional<CursoTaller> getCursoTallerPorId(int idCursoTaller) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public CursoTaller getCursoTallerPorNombre(String nombreCurso) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Academico> getAcademicosParticipantes(CursoTaller cursoTaller) throws ErrorDAO {
        return null;
    }

    @Override
    public int agregar(CursoTaller cursoTaller) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar(CursoTaller cursoTaller) throws ErrorDAO {
        return 0;
    }

    @Override
    public List<CursoTaller> getTodos() throws ErrorDAO {
        return null;
    }
}
