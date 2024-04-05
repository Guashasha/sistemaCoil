package Logica.Interfaces;

import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;

import java.util.Optional;

public interface ICuentaAcademicoDAO extends IDAO<CuentaAcademico, Integer> {
    Optional<CuentaAcademico> getCuentaPorCedulaProfesional (String cedulaProfesional) throws ErrorDAO;
    Optional<CuentaAcademico> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO;
    int actualizarNombreUsuario(String nombreUsuario) throws ErrorDAO;
    boolean verificarCredenciales(String nombreUsuario, String contrasena) throws ErrorDAO;
    int actualizarContrasena(CuentaAcademico cuenta, String contrasenaAntigua,String nuevaContrasena) throws ErrorDAO;
    int cambiarEstadoCuenta (CuentaAcademico cuenta, String estado) throws ErrorDAO;


}
