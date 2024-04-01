package AccesoADatos;

import Logica.Dominio.Pais;
import Logica.ErrorDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaisDB {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public List<Pais> paisesAlfabeticamente() throws ErrorDAO {
        List<Pais> listaPaises = new ArrayList<>();
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM paises ORDER BY nombre ASC";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        try {
            consultaPaises = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaPaisesSQL);
            resultadoConsulta = consultaPaises.executeQuery();

            while (resultadoConsulta.next()) {
                listaPaises.add(convertirResultSetAPais(resultadoConsulta));
            }

            consultaPaises.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO("SQLException: Error al consultar paises por nombre\n" + error.getMessage());
        }

        return listaPaises;
    }

    public Pais getPaisPorNombre(String nombre) throws ErrorDAO {
        Pais pais = null;
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM paises WHERE nombre = ?";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        try {
            consultaPaises = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaPaisesSQL);
            consultaPaises.setString(1,nombre);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                pais = convertirResultSetAPais(resultadoConsulta);
            }

            consultaPaises.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO("SQLException: Error al consultar paises por nombre\n" + error.getMessage());
        }

        return pais;
    }

    public Pais getPaisPorId(int id) throws ErrorDAO {
        Pais pais = null;
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM paises WHERE idPais = ?";
        PreparedStatement consultaPaises;
        ResultSet resultadoConsulta;

        try {
            consultaPaises = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaPaisesSQL);
            consultaPaises.setInt(1,id);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                pais = convertirResultSetAPais(resultadoConsulta);
            }

            consultaPaises.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO("SQLException: Error al consultar paises por nombre\n" + error.getMessage());
        }

        return pais;
    }

    private Pais convertirResultSetAPais (ResultSet resultado) throws SQLException {
        Pais pais = new Pais();

        pais.setId(resultado.getInt(1));
        pais.setIso(resultado.getString(2));
        pais.setNombre(resultado.getString(3));

        return pais;
    }

}
