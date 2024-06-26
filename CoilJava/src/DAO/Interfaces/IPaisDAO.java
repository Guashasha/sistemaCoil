package DAO.Interfaces;

import DTO.PaisDTO;
import Utilidades.ErrorDAO;
import java.util.List;
import java.util.Optional;

public interface IPaisDAO {
    List<PaisDTO> getPaisesAlfabeticamente () throws ErrorDAO;
    Optional<PaisDTO> getPaisPorNombre (String nombre) throws ErrorDAO;
    Optional<PaisDTO> getPaisPorId (int id) throws ErrorDAO;
}
