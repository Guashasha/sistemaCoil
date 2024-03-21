package Logica.DAO;

import AccesoADatos.RegionDB;
import Logica.Dominio.Region;
import Logica.ErrorDAO;
import Logica.Interfaces.IRegionDAO;
import java.util.List;

public class DAORegion implements IRegionDAO {
    private final RegionDB REGION_DB = new RegionDB();

    @Override
    public List<Region> getTodasAlfabeticamente() throws ErrorDAO {
        return this.REGION_DB.getTodasAlfabeticamente();
    }

}
