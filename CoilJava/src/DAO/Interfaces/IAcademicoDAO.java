package DAO.Interfaces;

import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IAcademicoDAO extends IDAO<AcademicoDTO, Integer>{
    List<AcademicoDTO> getAcademicosPorFacultad (String nombrefacultad) throws ErrorDAO;
    Optional<AcademicoDTO> getAcademicoPorCedula (String cedula) throws ErrorDAO;
    List<AcademicoDTO> getAcademicosPorUniversidad (String nombreUniversidad) throws ErrorDAO;
    List<AcademicoDTO> getAcademicosPorAreaEstudios (String areaEstudios) throws ErrorDAO;
    List<AcademicoDTO> getAcademicosPorCategoriaContratacion (String categoriaContratacion) throws ErrorDAO;
    List<AcademicoDTO> getAcademicosPorRegion (String region) throws ErrorDAO;
    int agregarAcademicoConCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws ErrorDAO;

}
