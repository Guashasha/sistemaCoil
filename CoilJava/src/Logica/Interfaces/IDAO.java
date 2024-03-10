package Logica.Interfaces;

import Logica.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IDAO<Tipo> {
    int agregar (Tipo t) throws ErrorDAO;
    int modificar (Tipo t) throws ErrorDAO;
    List<Tipo> getTodos () throws ErrorDAO;
}
