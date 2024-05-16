package DAO.Interfaces;

import DTO.PaisDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IPaisDAO {
    public List<String> getNombresPaisesAlfabeticamente () throws ErrorDAO;
    public Optional<PaisDTO> getPaisPorNombre (String nombre) throws ErrorDAO;
    public Optional<PaisDTO> getPaisPorId (int id) throws ErrorDAO;
}
