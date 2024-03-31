package Logica.Interfaces;

import Logica.Dominio.Pais;
import Logica.ErrorDAO;
import java.util.List;
import java.util.Optional;

public interface IPaisDAO {
    public List<Pais> paisesAlfabeticamente () throws ErrorDAO;
    public Optional<Pais> getPaisPorNombre (String nombre) throws ErrorDAO;
    public Optional<Pais> getPaisPorId (int id) throws ErrorDAO;
}
