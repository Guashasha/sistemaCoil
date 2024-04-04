package Logica.Interfaces;

import Logica.ErrorDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface IDAO<Tipo, TipoId> {
    int agregar (Tipo t) throws ErrorDAO;
    int modificar (Tipo obj) throws ErrorDAO;
    Optional<Tipo> getPorId (TipoId y) throws ErrorDAO;
    List<Tipo> getTodos () throws ErrorDAO;
    Tipo resultSetAObjeto (ResultSet resultados);
}
