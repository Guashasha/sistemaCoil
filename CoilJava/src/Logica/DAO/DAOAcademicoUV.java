package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.AcademicoUV;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoUVDAO;
import Logica.Interfaces.IDAO;

public class DAOAcademicoUV implements IDAO<AcademicoUV>, IAcademicoUVDAO {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    @Override
    public int agregar(AcademicoUV academicoUV) throws ErrorDAO {

    }
}
