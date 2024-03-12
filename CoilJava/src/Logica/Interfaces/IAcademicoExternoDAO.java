package Logica.Interfaces;

import Logica.Dominio.AcademicoExterno;
import Logica.ErrorDAO;
import java.util.List;

public interface IAcademicoExternoDAO {
    public AcademicoExterno getAcademicoExternoPorCedula(int cedulaProfesional) throws ErrorDAO;
    public int agregarAcademicoExterno (AcademicoExterno academico) throws ErrorDAO;
    public List<AcademicoExterno> getAcademicosPorUniversidad (String institucion) throws ErrorDAO;
}
