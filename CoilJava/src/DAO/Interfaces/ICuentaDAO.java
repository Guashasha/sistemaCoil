package DAO.Interfaces;

import DTO.CuentaDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface ICuentaDAO extends IDAO<CuentaDTO, Integer> {
    Optional<CuentaDTO> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO;
    Optional<CuentaDTO> getCuentaPorPersona (int idPersona) throws ErrorDAO;
    int actualizarNombreUsuario(CuentaDTO cuentaDTO) throws ErrorDAO;
    boolean verificarCredenciales(String nombreUsuario, String contrasena) throws ErrorDAO;
    int actualizarContrasena(CuentaDTO cuentaDTO, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO;
    int cambiarEstadoCuenta (CuentaDTO cuentaDTO, String estado) throws ErrorDAO;
    List<CuentaDTO> getCuentasPorTipo (String tipo) throws ErrorDAO;
    List<CuentaDTO> getCuentasPorEstado (String estado) throws ErrorDAO;

}
