package Logica.Interfaces;

import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import java.util.List;
import java.util.Optional;

public interface IUniversidadDAO {
    public int registrarUniversidad (Universidad universidad, Pais pais) throws ErrorDAO;
    public int editarUniversidad (Universidad universidadActual, Universidad nuevaUniversidad, Pais nuevoPais) throws ErrorDAO;
    public Optional<Universidad> getUniversidadPorNombre (String nombre) throws ErrorDAO;
    public List<Universidad> getUniversidadesPorPaisOrigen (String paisOrigen) throws ErrorDAO;
    public List<Universidad> getTodasAlfabeticamente () throws ErrorDAO;
    public Optional<Universidad> getUniversidadPorId (int id) throws ErrorDAO;
}
