package Logica.Interfaces;

import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import java.util.List;

public interface IUniversidadDAO {
    Universidad getUniversidadPorNombre(String nombreUniversidad) throws ErrorDAO;
    List<Universidad> getUniversiadesPorPaisOrigen(String paisOrigen) throws ErrorDAO;
}
