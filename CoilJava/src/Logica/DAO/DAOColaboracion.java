package Logica.DAO;

import Logica.Dominio.Academico;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Estudiante;
import Logica.Dominio.Periodo;
import Logica.ErrorDAO;
import Logica.Interfaces.IColaboracionDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOColaboracion implements IColaboracionDAO {

    @Override
    public Optional<Colaboracion> getColaboracionPorAcademicosParticipantes (Academico academico1, Academico academico2) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public Optional<Colaboracion> getColaboracionPorId (int idColaboracion) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public List<Estudiante> getListaDeEstudiantes (Colaboracion colaboracion) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Academico> getAcademicosParticipantes (Colaboracion colaboracion) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Periodo> getColaboracionPorPeriodo (Colaboracion colaboracion) {
        return null;
    }

    @Override
    public List<Colaboracion> getColaboracionPorIdioma (Colaboracion colaboracion) {
        return null;
    }

    @Override
    public int cambiarEstadoColaboracion (Colaboracion colaboracion) {
        return 0;
    }

    @Override
    public int agregarEstudianteAColaboracion (Colaboracion colaboracion, Estudiante estudiante) throws ErrorDAO {
        return 0;
    }


    @Override
    public int agregarAcademicoAColaboracion (Colaboracion colaboracion, Academico academico) throws ErrorDAO {
        return 0;
    }

    @Override
    public int agregar (Colaboracion t) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar (Colaboracion obj) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<Colaboracion> getPorId (String y) throws ErrorDAO {
        return Optional.empty();
    }


    @Override
    public List<Colaboracion> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Colaboracion resultSetAObjeto (ResultSet resultados) {
        return null;
    }
}
