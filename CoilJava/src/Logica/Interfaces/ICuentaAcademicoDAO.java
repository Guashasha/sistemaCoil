package Logica.Interfaces;

import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;

import java.util.Optional;

public interface ICuentaAcademicoDAO extends IDAO<CuentaAcademico, Integer> {
    Optional<CuentaAcademico> getCuentaPorCedulaProfesional (String cedulaProfesional) throws ErrorDAO;
    Optional<CuentaAcademico> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO;
    int actualizarInformacionCuenta(CuentaAcademico cuenta) throws ErrorDAO;
    boolean verificarCredenciales(String nombreUsuario, String contrasena) throws ErrorDAO;
    int cambiarContrasena(String nombreUsuario, String nuevaContrasena) throws ErrorDAO;
    int cambiarEstadoCuenta (CuentaAcademico cuenta) throws ErrorDAO;

}
