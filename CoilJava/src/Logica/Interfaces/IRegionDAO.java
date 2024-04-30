package Logica.Interfaces;

import Utilidades.ErrorDAO;
import Logica.Dominio.Region;

import java.util.List;

public interface IRegionDAO {
    public List<Region> getTodasAlfabeticamente () throws ErrorDAO;
}
