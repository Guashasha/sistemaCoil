package Logica.Interfaces;

import Logica.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IDAO<Tipo, TipoId> {
    int agregar (Tipo t) throws ErrorDAO;
    int modificar (TipoId y) throws ErrorDAO;
    Tipo getPorId (TipoId y) throws ErrorDAO;
    List<Tipo> getTodos () throws ErrorDAO;
}
