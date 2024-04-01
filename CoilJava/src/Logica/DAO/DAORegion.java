package Logica.DAO;

import AccesoADatos.RegionDB;
import Logica.Bitacora;
import Logica.Dominio.Region;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IRegionDAO;
import java.util.List;

public class DAORegion implements IRegionDAO {
    private final RegionDB REGION_DB = new RegionDB();
    private static Bitacora bitacora = new Bitacora(Universidad.class.getName());

    @Override
    public List<Region> getTodasAlfabeticamente() throws ErrorDAO {
        try {
            return this.REGION_DB.getTodasAlfabeticamente();
        } catch (ErrorDAO error) {
            bitacora.escribirError(error);
            throw error;
        }
    }

}
