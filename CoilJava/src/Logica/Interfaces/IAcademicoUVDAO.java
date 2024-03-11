package Logica.Interfaces;


import Logica.Dominio.AcademicoUV;
import Logica.ErrorDAO;

import java.util.List;

public interface IAcademicoUVDAO {

    int getAcademicoUVPorCedula(int cedulaProfesional) throws ErrorDAO;
    List<AcademicoUV> getAcademicosUVPorContrato(String categoriaContratacion) throws ErrorDAO;
    List<AcademicoUV> getAcademicosUVPorFacultad(String nombreFacultad) throws ErrorDAO;
    List<AcademicoUV> getAcademicosUVPorRegion(String nombreRegion) throws ErrorDAO;

}
