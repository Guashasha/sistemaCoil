package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IDAO;
import Logica.Interfaces.IUniversidadDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DAOUniversidad implements IDAO<Universidad>, IUniversidadDAO {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    @Override
    public int agregar(Universidad universidad) throws ErrorDAO {
        int resultado;
        String sqlInsertar = "INSERT INTO universidad (nombre, paisOrigen) VALUES (?,?)";
        try (Connection conexion = CONEXION_BASE_DATOS.getConexion(); PreparedStatement stmInsertar = conexion.prepareStatement(sqlInsertar)) {
            stmInsertar.setString(1, universidad.getNombre());
            stmInsertar.setString(2, universidad.getPaisOrigen());
            resultado = stmInsertar.executeUpdate();
        }
        catch (SQLException e){
            throw new ErrorDAO("Error al registrar al academico" + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int modificar(Universidad universidad) throws ErrorDAO {
        return 0;
    }

    @Override
    public List getTodos() throws ErrorDAO {
        return null;
    }

    @Override
    public Universidad getUniversidadPorNombre(String nombreUniversidad) throws ErrorDAO {
        Universidad universidad = null;
        String sqlQuery = "SELECT * FROM universidad WHERE nombre = ?";
        try (Connection conexion = CONEXION_BASE_DATOS.getConexion(); PreparedStatement stm = conexion.prepareStatement(sqlQuery)) {
            stm.setString(1, nombreUniversidad);
            try (ResultSet rs = stm.executeQuery()) {
                String nombre = rs.getString("nombre");
                String paisOrigen = rs.getString("paisOrigen");

                universidad = new Universidad();
                universidad.setNombre(nombre);
                universidad.setPaisOrigen(paisOrigen);
            }

        }
        catch (SQLException e) {
            throw new ErrorDAO("Error al realizar la consulta por contrato" + e.getMessage());
        }
        return universidad;
    }

    @Override
    public List<Universidad> getUniversiadesPorPaisOrigen(String paisOrigen) throws ErrorDAO {
        return null;
    }
}
