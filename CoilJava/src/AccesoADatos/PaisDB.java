package AccesoADatos;

import Logica.Dominio.Pais;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaisDB {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static List<Pais> paisesAlfabeticamente () throws SQLException {
        List<Pais> listaPaises = new ArrayList<>();
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais ORDER BY nombre ASC";
        PreparedStatement consultaPaises = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaPaises = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaPaisesSQL);
            resultadoConsulta = consultaPaises.executeQuery();

            while (resultadoConsulta.next()) {
                listaPaises.add(convertirResultSetAPais(resultadoConsulta));
            }
            consultaPaises.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return listaPaises;
    }

    public static Pais getPaisPorNombre (String nombre) throws SQLException {
        Pais pais = new Pais(0);
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais WHERE nombre = ?";
        PreparedStatement consultaPaises = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaPaises = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaPaisesSQL);
            consultaPaises.setString(1,nombre);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                pais = convertirResultSetAPais(resultadoConsulta);
            }
            consultaPaises.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return pais;
    }

    public static Pais getPaisPorId (int id) throws SQLException {
        Pais pais = new Pais(0);
        String consultaPaisesSQL = "SELECT idPais, iso, nombre FROM pais WHERE idPais = ?";
        PreparedStatement consultaPaises = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaPaises = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaPaisesSQL);
            consultaPaises.setInt(1,id);
            resultadoConsulta = consultaPaises.executeQuery();

            if (resultadoConsulta.next()) {
                pais = convertirResultSetAPais(resultadoConsulta);
            }
            consultaPaises.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw error;
        }

        return pais;
    }

    private static Pais convertirResultSetAPais (ResultSet resultado) throws SQLException {
        Pais pais = new Pais();

        pais.setId(resultado.getInt(1));
        pais.setIso(resultado.getString(2));
        pais.setNombre(resultado.getString(3));

        return pais;
    }

}
