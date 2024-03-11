package Logica.Interfaces;

import Logica.ErrorDAO;
import Logica.Dominio.Region;
import java.util.List;

public interface IRegionDAO {
    public List<Region> getRegiones() throws ErrorDAO;
}
