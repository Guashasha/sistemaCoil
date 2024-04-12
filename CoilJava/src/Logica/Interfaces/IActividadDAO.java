package Logica.Interfaces;

import Logica.Dominio.Actividad;

import java.sql.SQLException;
import java.util.Optional;

public interface IActividadDAO extends IDAO<Actividad, Integer> {
    public Optional<Actividad> getPorTitulo (String titulo) throws SQLException ;
}
