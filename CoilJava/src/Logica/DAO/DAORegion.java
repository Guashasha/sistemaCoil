package Logica.DAO;

import AccesoADatos.RegionDB;
import Logica.Bitacora;
import Logica.Dominio.Region;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IRegionDAO;
import java.sql.SQLException;
import java.util.List;

public class DAORegion implements IRegionDAO {
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public List<Region> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return RegionDB.getTodasAlfabeticamente();
        } catch (SQLException error) {

            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }
    }

}
