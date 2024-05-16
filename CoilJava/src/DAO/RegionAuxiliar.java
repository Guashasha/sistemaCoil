package DAO;

import DTO.RegionDTO;
import Utilidades.ErrorDAO;
import DAO.Interfaces.IRegionDAO;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.List;

public class RegionAuxiliar implements IRegionDAO {
    private static Logger bitacora = Logger.getLogger(RegionAuxiliar.class);

    @Override
    public List<RegionDTO> getTodasAlfabeticamente () throws ErrorDAO {
        try {
            return RegionDAO.getTodasAlfabeticamente();
        }
        catch (SQLException error) {
            bitacora.info(error.getMessage());
            throw new ErrorDAO("Error en la conexión a la base de datos", ErrorDAO.Tipo.CONSULTA);
        }
    }

}
