package DAO.Interfaces;

import DTO.PaisDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IPaisDAO {
    List<PaisDTO> getPaisesAlfabeticamente () throws SQLException;
    Optional<PaisDTO> getPaisPorNombre (String nombre) throws SQLException;
    Optional<PaisDTO> getPaisPorId (int id) throws SQLException;
}
