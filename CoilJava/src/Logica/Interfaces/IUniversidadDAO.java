package Logica.Interfaces;

import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import java.util.List;
import java.util.Optional;


public interface IUniversidadDAO {
    public int registrarUniversidad (String universidad, String pais) throws ErrorDAO;
    public int editarUniversidad (String universidad, String pais) throws ErrorDAO;
    public Universidad getUniversidadPorNombre (String nombre) throws ErrorDAO;
    public List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO;
    public List<Universidad> getTodasAlfabeticamente () throws ErrorDAO;
}
