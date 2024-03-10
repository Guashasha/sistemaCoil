package Logica.DAO;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;
import Logica.Interfaces.IColaboracionDAO;

import java.util.List;

public class DAOColaboracion implements IColaboracionDAO {
    @Override
    public Colaboracion getColaboracionPorAcademicosParticipantes(Academico academico1, Academico academico2) throws ErrorDAO {
        return null;
    }

    @Override
    public Colaboracion getColaboracionPorId(int idColaboracion) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Estudiante> getListaDeEstudiantes(Colaboracion colaboracion) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Academico> getAcademicosParticipantes(Colaboracion colaboracion) throws ErrorDAO {
        return null;
    }

    @Override
    public int agregar(Colaboracion colaboracion) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar(Colaboracion colaboracion) throws ErrorDAO {
        return 0;
    }

    @Override
    public List<Colaboracion> getTodos() throws ErrorDAO {
        return null;
    }
}
