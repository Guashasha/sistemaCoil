package Logica.Interfaces;

import Logica.Dominio.Universidad;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IUniversidadDAO {
    int agregarUniversidad(Universidad universidad) throws SQLException;
    int modificarUniversidad(int idUniversiad) throws SQLException;
    ArrayList<Universidad> universiadesPorPaisOrigen(String paisOrigen) throws SQLException;


}
