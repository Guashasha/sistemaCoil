package Logica.Interfaces;

import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import java.util.List;

public interface IUniversidadDAO {
    public int registrarUniversidad (String universidad, String pais) throws ErrorDAO;
    public int editarUniversidad (String nombreActual, String nuevoNombre, String nuevoPais) throws ErrorDAO;
    public Universidad getUniversidadPorNombre (String nombre) throws ErrorDAO;
    public List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO;
    public List<Universidad> getTodasAlfabeticamente () throws ErrorDAO;
}
