package Logica.Interfaces;

import Logica.ErrorDAO;
import Logica.Dominio.Region;

import java.util.ArrayList;

public interface IRegionDAO {
    public ArrayList<Region> getTodasAlfabeticamente () throws ErrorDAO;
}
