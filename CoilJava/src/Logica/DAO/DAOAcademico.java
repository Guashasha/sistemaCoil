package Logica.DAO;

import AccesoADatos.AcademicoDB;
import Logica.Dominio.Academico;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOAcademico implements IAcademicoDAO {
    @Override
    public List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        return AcademicoDB.getListaAcademicoPorCampos("facultad", nombrefacultad);
    }

    @Override
    public Optional<Academico> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        return Optional.ofNullable(AcademicoDB.getAcademicoPorCedula(cedula));
    }

    @Override
    public List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        return AcademicoDB.getListaAcademicoPorCampos("universidad", nombreUniversidad);
    }

    @Override
    public List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        return AcademicoDB.getListaAcademicoPorCampos("area", areaEstudios);
    }

    @Override
    public List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        return AcademicoDB.getListaAcademicoPorCampos("categoria", categoriaContratacion);
    }

    @Override
    public List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO {
        return AcademicoDB.getListaAcademicoPorCampos("region", region);
    }

    @Override
    public Optional<Academico> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO {
        return Optional.ofNullable(AcademicoDB.getAcademicoPorId(idPersona));
    }

    @Override
    public int agregar (Academico academico) throws ErrorDAO {
        return AcademicoDB.agregarAcademico(academico);
    }

    @Override
    public int modificar (Academico obj) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<Academico> getPorId (String y) throws ErrorDAO {
        return Optional.empty();
    }


    @Override
    public int modificarAcademico (Academico academico) throws ErrorDAO {
        return AcademicoDB.editarAcademico(academico);
    }



    @Override
    public List<Academico> getTodos () throws ErrorDAO {
        return AcademicoDB.getTodos();
    }

    @Override
    public Academico resultSetAObjeto (ResultSet resultados) {
        return null;
    }
}
