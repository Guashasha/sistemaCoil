package Logica.Interfaces;

import Logica.Dominio.Facultad;

import java.sql.SQLException;

public interface IFacultadDAO {
    int agregarFacultad(Facultad facultad) throws SQLException;
    Facultad consultarPorNombre(String nombre) throws SQLException;
}
