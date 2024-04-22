package AccesoADatos;

import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacultadDB {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static Facultad getFacultadPorNombre (String nombre) throws SQLException {
        Facultad facultad = new Facultad(0);
        String consultaUniversidadSQL = "SELECT * FROM facultad_con_region WHERE facultad = ?";
        PreparedStatement consultaUniversidad = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaUniversidad = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1,nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                facultad = convertirResultSetAFacultad(resultadoConsulta);
            }
        }
        catch (SQLException error) {
            throw error;
        }
        finally {
            consultaUniversidad.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return facultad;
    }

    public static List<Facultad> getFacultadPorRegion (String region) throws SQLException {
        List<Facultad> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region WHERE region = ?";
        PreparedStatement consultaFacultades = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaFacultades = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaFacultadesSQL);
            consultaFacultades.setString(1, region);
            resultadoConsulta = consultaFacultades.executeQuery();

            while (resultadoConsulta.next()) {
                listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
            }
        } catch (SQLException error) {
            throw error;
        }
        finally {
            consultaFacultades.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaFacultades;
    }

    public static List<Facultad> getTodasAlfabeticamente () throws SQLException {
        List<Facultad> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region ORDER BY facultad ASC";
        PreparedStatement consultaFacultades = null;
        ResultSet resultadoConsulta = null;

        try {
            consultaFacultades = CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaFacultadesSQL);
            resultadoConsulta = consultaFacultades.executeQuery();

            while (resultadoConsulta.next()) {
                listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
            }
        } catch (SQLException error) {
            throw error;
        }
        finally {
            consultaFacultades.close();
            resultadoConsulta.close();
            CONEXION_BASE_DATOS.desconectar();
        }

        return listaFacultades;
    }

    private static Facultad convertirResultSetAFacultad (ResultSet resultado) throws SQLException {
        Facultad facultad = new Facultad();

        facultad.setId(resultado.getInt(1));
        facultad.setNombre(resultado.getString(2));
        facultad.setIdRegion(resultado.getInt(3));

        return facultad;
    }
}
