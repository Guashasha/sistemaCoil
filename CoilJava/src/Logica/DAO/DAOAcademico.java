package Logica.DAO;

import AccesoADatos.AcademicoBD;
import Logica.Dominio.Academico;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;

import java.util.List;
import java.util.Optional;

public class DAOAcademico implements IAcademicoDAO {
    @Override
    public List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO {
        return AcademicoBD.getListaAcademicoPorCampos("facultad", nombrefacultad);
    }

    @Override
    public Optional<Academico> getAcademicoPorCedula (String cedula) throws ErrorDAO {
        return Optional.ofNullable(AcademicoBD.getAcademicoPorCampo("cedula", cedula));
    }

    @Override
    public List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO {
        return AcademicoBD.getListaAcademicoPorCampos("universidad", nombreUniversidad);
    }

    @Override
    public List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO {
        return AcademicoBD.getListaAcademicoPorCampos("area", areaEstudios);
    }

    @Override
    public List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO {
        return AcademicoBD.getListaAcademicoPorCampos("categoria", categoriaContratacion);
    }

    @Override
    public List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO {
        return AcademicoBD.getListaAcademicoPorCampos("region", region);
    }

    @Override
    public Optional<Academico> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO {
        return Optional.ofNullable(AcademicoBD.getAcademicoPorId(idPersona));
    }

    @Override
    public int agregar (Academico academico) throws ErrorDAO {
        return AcademicoBD.agregarAcademico(academico);
    }

    @Override
    public int modificar (String y) throws ErrorDAO {
        return 0;
    }

    @Override
    public Academico getPorId (String cedula) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Academico> getTodos () throws ErrorDAO {
        return AcademicoBD.getTodos();
    }
}
