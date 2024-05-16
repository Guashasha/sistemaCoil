package DAO.Interfaces;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import java.util.List;
import java.util.Optional;

public interface IUniversidadDAO {
    public int registrarUniversidad (UniversidadDTO universidadDTO, PaisDTO paisDTO) throws ErrorDAO;
    public int editarUniversidad (UniversidadDTO universidadDTOActual, UniversidadDTO nuevaUniversidadDTO, PaisDTO nuevoPaisDTO) throws ErrorDAO;
    public Optional<UniversidadDTO> getUniversidadPorNombre (String nombre) throws ErrorDAO;
    public List<UniversidadDTO> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO;
    public List<UniversidadDTO> getUniversidadesPorNombre (UniversidadDTO universidadDTO) throws ErrorDAO;
    public List<UniversidadDTO> getTodasAlfabeticamente () throws ErrorDAO;
    public Optional<UniversidadDTO> getUniversidadPorId (int id) throws ErrorDAO;
}
