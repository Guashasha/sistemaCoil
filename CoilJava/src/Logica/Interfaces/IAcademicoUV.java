package Logica.Interfaces;

import Logica.Dominio.AcademicoUV;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IAcademicoUV {
    int agregarAcademicoUV(AcademicoUV academicoUV) throws SQLException;
    int modificarAcademicoUV(String cedulaProfesional) throws SQLException;
    int academicoPorCedula(int cedulaProfesional) throws SQLException;
    ArrayList<AcademicoUV> academicosPorContrato(String categoriaContratacion) throws SQLException;
    ArrayList<AcademicoUV> academicosPorRegion(String region) throws SQLException;

}
