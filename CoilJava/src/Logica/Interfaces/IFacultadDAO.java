package Logica.Interfaces;

import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import java.util.List;

public interface IFacultadDAO {
    Facultad getFacultadPorNombre (String nombre) throws ErrorDAO;
    List<Facultad> getFacultadPorRegion (String region) throws ErrorDAO;
    List<Facultad> getTodasAlfabeticamente () throws ErrorDAO;
}
