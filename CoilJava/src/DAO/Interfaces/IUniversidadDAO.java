package DAO.Interfaces;

import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import java.util.List;
import java.util.Optional;

public interface IUniversidadDAO {
    int registrarUniversidad (UniversidadDTO universidad) throws ErrorDAO;
    int editarUniversidad (UniversidadDTO universidad) throws ErrorDAO;
    Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws ErrorDAO;
    List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO;
    List<UniversidadDTO> getUniversidadesPorNombre (String nombre) throws ErrorDAO;
    List<UniversidadDTO> getTodasAlfabeticamente () throws ErrorDAO;
    Optional<UniversidadDTO> getUniversidadPorNombreYPais (String nombre, String pais) throws ErrorDAO;
    Optional<UniversidadDTO> getUniversidadPorId (int id) throws ErrorDAO;

}
