package Logica.Interfaces;

import Logica.Dominio.Academico;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IAcademicoDAO extends IDAO<Academico, String>{
    List<Academico> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO;
    Optional<Academico> getAcademicoPorCedula (String cedula) throws ErrorDAO;
    List<Academico> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO;
    List<Academico> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO;
    List<Academico> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO;
    List<Academico> getAcademicosPorRegion (String region) throws ErrorDAO;
    Optional<Academico> getAcademicoPorIdPersona (int idPersona) throws ErrorDAO;
    int agregarAcademicoExterno (Academico academico) throws ErrorDAO;

}
