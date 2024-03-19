package Logica.Interfaces;

import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import java.util.ArrayList;


public interface IUniversidadDAO {
    public int registrarUniversidad(Universidad universidad) throws ErrorDAO;
    public int editarUniversidad(Universidad universidad, int id) throws ErrorDAO;
    Universidad getUniversidadPorNombre(String nombre) throws ErrorDAO;
    ArrayList<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO;
    ArrayList<Universidad> getTodasAlfabeticamente() throws ErrorDAO;
}
