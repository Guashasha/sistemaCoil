package Logica.Interfaces;


import Logica.Dominio.AcademicoUV;
import Logica.ErrorDAO;

import java.util.List;

public interface IAcademicoUVDAO {
    int agregarAcademicoUV(AcademicoUV academicoUV, int idFacultad) throws ErrorDAO;
    int modificarAdademicoUV(AcademicoUV academicoUV) throws ErrorDAO;
    List<AcademicoUV> getAcademicosUVPorContrato(String categoriaContratacion) throws ErrorDAO;
    List<AcademicoUV> getAcademicosUVPorFacultad(String nombreFacultad) throws ErrorDAO;
    List<AcademicoUV> getAcademicosUVPorRegion(String nombreRegion) throws ErrorDAO;

}
