package Logica.Interfaces;

import Logica.Dominio.Academico;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public interface IAcademicoDAO {
    public int registrarAcademicoExterno (Academico academicoExterno) throws SQLTimeoutException, SQLException;
    public boolean academicoRegistrado(int cedulaProfesional) throws SQLException;

}
