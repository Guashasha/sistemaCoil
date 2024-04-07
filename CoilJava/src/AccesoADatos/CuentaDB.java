package AccesoADatos;

import Logica.Dominio.Cuenta;
import Logica.ErrorDAO;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuentaDB {
    //toDo corregir odio mi vida

    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();




    private static Cuenta convertirCuentaAcademico (ResultSet resultado) throws SQLException {
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(resultado.getInt("idCuenta"));


        return cuenta;
    }

}
