package Logica.Interfaces;

import Logica.Dominio.Facultad;
import Logica.ErrorDAO;

import java.util.ArrayList;
import java.util.List;

public interface IFacultadDAO {
    Facultad getFacultadPorNombre (String nombre) throws ErrorDAO;
    ArrayList<Facultad> getFacultadPorRegion (String region) throws ErrorDAO;
    ArrayList<Facultad> getTodasAlfabeticamente () throws ErrorDAO;
}
