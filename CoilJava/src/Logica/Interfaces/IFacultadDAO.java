package Logica.Interfaces;

import Logica.Dominio.Facultad;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IFacultadDAO {
    Optional<Facultad> getFacultadPorNombre (String nombre) throws ErrorDAO;
    List<Facultad> getFacultadPorRegion (String region) throws ErrorDAO;
    List<Facultad> getTodasAlfabeticamente () throws ErrorDAO;
}
