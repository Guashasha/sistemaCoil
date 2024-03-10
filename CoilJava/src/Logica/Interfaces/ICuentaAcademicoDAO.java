package Logica.Interfaces;

import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;

import java.util.Optional;

public interface ICuentaAcademicoDAO extends IDAO<CuentaAcademico> {
    Optional<CuentaAcademico> getCuentaPorCedulaProfesional (int cedulaProfesional) throws ErrorDAO;
}
