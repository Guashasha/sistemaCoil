package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.ErrorDAO;
import java.util.List;

public interface IAcademicoDAO extends IDAO<Academico>{
    List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO;
    Academico getAcademicoPorCedula (int cedula) throws ErrorDAO;
    List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO;
    List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO;
    List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO;
    List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO;
    Academico getAcademicoPorIdPersona (int idPersona) throws ErrorDAO;

}
