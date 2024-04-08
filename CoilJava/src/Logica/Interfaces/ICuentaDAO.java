package Logica.Interfaces;

import Logica.Dominio.Colaboracion;
import Logica.Dominio.Cuenta;
import Logica.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface ICuentaDAO extends IDAO<Cuenta, Integer> {
    Optional<Cuenta> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO;
    int actualizarNombreUsuario(Cuenta cuenta) throws ErrorDAO;
    boolean verificarCredenciales(String nombreUsuario, String contrasena) throws ErrorDAO;
    int actualizarContrasena(Cuenta cuenta, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO;
    int cambiarEstadoCuenta (Cuenta cuenta, String estado) throws ErrorDAO;
    List<Cuenta> getCuentasPorTipo (String tipo) throws ErrorDAO;
    List<Cuenta> getCuentasPorEstado (String estado) throws ErrorDAO;

}
