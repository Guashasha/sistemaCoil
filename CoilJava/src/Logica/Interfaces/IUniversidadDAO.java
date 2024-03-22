package Logica.Interfaces;

import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import java.util.List;
import java.util.Optional;


public interface IUniversidadDAO {
    public int registrarUniversidad(Universidad universidad) throws ErrorDAO;
    public int editarUniversidad(Universidad universidad) throws ErrorDAO;
    Optional<Universidad> getUniversidadPorNombre(String nombre) throws ErrorDAO;
    List<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO;
    List<Universidad> getTodasAlfabeticamente() throws ErrorDAO;
}
