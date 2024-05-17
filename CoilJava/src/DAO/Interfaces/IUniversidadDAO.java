package DAO.Interfaces;

import DTO.UniversidadDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUniversidadDAO {
    int registrarUniversidad (UniversidadDTO universidad) throws SQLException;
    int editarUniversidad (UniversidadDTO universidad) throws SQLException;
    Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws SQLException;
    List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws SQLException;
    List<UniversidadDTO> getUniversidadesPorNombre (String nombre) throws SQLException;
    List<UniversidadDTO> getTodasAlfabeticamente () throws SQLException;
    Optional<UniversidadDTO> getUniversidadPorNombreYPais (String nombre, String pais) throws SQLException;
    Optional<UniversidadDTO> getUniversidadPorId (int id) throws SQLException;

}
