package Logica.DAO;

import Logica.Dominio.Retroalimentacion;
import Logica.ErrorDAO;
import Logica.Interfaces.IActividadDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOActividad implements IActividadDAO {
    @Override
    public int agregar (Retroalimentacion t) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar (Retroalimentacion obj) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<Retroalimentacion> getPorId (Integer y) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public List<Retroalimentacion> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Retroalimentacion resultSetAObjeto (ResultSet resultados) {
        return null;
    }
}
