package Logica.Interfaces;

import Logica.Dominio.Academico;
import Logica.ErrorDAO;
import java.util.ArrayList;
import java.util.List;

public interface IAcademicoDAO {
    public boolean academicoRegistrado(int cedulaProfesional) throws ErrorDAO;
    public ArrayList<Academico> getAcademicosPorAreaEstudios(String areaEstudios) throws ErrorDAO;
    public int cambiarCorreoElectronico(String correo, int cedulaProfesional) throws ErrorDAO;
    public int cambiarTelefono(String telefono, int cedulaProfesional) throws ErrorDAO;
}
