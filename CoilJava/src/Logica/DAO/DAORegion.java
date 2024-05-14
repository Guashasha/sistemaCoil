package Logica.DAO;

import AccesoADatos.RegionDB;
import Logica.Dominio.Region;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IRegionDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.List;

public class DAORegion implements IRegionDAO {
    private static Logger bitacora = Logger.getLogger(DAORegion.class);

    @Override
    public List<Region> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return RegionDB.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO("Error en la conexión a la base de datos", ErrorDAO.Tipo.CONSULTA);
        }
    }

}
