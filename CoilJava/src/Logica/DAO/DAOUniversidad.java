package Logica.DAO;

import AccesoADatos.UniversidadDB;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IUniversidadDAO;
import java.util.List;
import java.util.Optional;


public class DAOUniversidad implements IUniversidadDAO {
    private final UniversidadDB UNIVERSIDAD_DB = new UniversidadDB();

    @Override
    public int registrarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas = this.UNIVERSIDAD_DB.registrarUniversidad(universidad);
        return filasAfectadas;
    }

    @Override
    public int editarUniversidad(Universidad universidad) throws ErrorDAO {
        int filasAfectadas = this.UNIVERSIDAD_DB.editarUniversidad(universidad);
        return filasAfectadas;
    }

    @Override
    public Optional<Universidad> getUniversidadPorNombre(String nombre) throws ErrorDAO {
        return Optional.ofNullable(this.UNIVERSIDAD_DB.getUniversidadPorNombre(nombre));
    }

    @Override
    public List<Universidad> getUniversidadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        return this.UNIVERSIDAD_DB.getUniversidadesPorPaisOrigen(paisOrigen);
    }

    @Override
    public List<Universidad> getTodasAlfabeticamente() throws ErrorDAO {
        return this.UNIVERSIDAD_DB.getTodasAlfabeticamente();
    }

}
