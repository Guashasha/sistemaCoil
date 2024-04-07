package AccesoADatos;

import Logica.Dominio.Facultad;
import Logica.ErrorDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacultadDB {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public Facultad getFacultadPorNombre (String nombre) throws ErrorDAO {
        Facultad facultad = new Facultad(0);
        String consultaUniversidadSQL = "SELECT * FROM facultad_con_region WHERE facultad = ?";
        PreparedStatement consultaUniversidad;
        ResultSet resultadoConsulta;

        try {
            consultaUniversidad = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaUniversidadSQL);
            consultaUniversidad.setString(1,nombre);
            resultadoConsulta = consultaUniversidad.executeQuery();

            if (resultadoConsulta.next()) {
                facultad = convertirResultSetAFacultad(resultadoConsulta);
            }

            consultaUniversidad.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO("SQLException: Error al consultar facultad por nombre\n" + error.getMessage());
        }

        return facultad;
    }

    public List<Facultad> getFacultadPorRegion (String region) throws ErrorDAO {
        List<Facultad> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region WHERE region = ?";
        PreparedStatement consultaFacultades;
        ResultSet resultadoConsulta;

        try {
            consultaFacultades = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaFacultadesSQL);
            consultaFacultades.setString(1,region);
            resultadoConsulta = consultaFacultades.executeQuery();

            while (resultadoConsulta.next()) {
                listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
            }

            consultaFacultades.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        } catch (SQLException excepcion) {
            throw new ErrorDAO("SQLException: Error al consultar Facultades por region\n" + excepcion.getMessage());
        }

        return listaFacultades;
    }

    public List<Facultad> getTodasAlfabeticamente() throws ErrorDAO {
        List<Facultad> listaFacultades = new ArrayList<>();
        String consultaFacultadesSQL = "SELECT * FROM facultad_con_region ORDER BY facultad ASC";
        PreparedStatement consultaFacultades;
        ResultSet resultadoConsulta;

        try {
            consultaFacultades = this.CONEXION_BASE_DATOS.getConexion().
                    prepareStatement(consultaFacultadesSQL);
            resultadoConsulta = consultaFacultades.executeQuery();

            while (resultadoConsulta.next()) {
                listaFacultades.add(convertirResultSetAFacultad(resultadoConsulta));
            }

            consultaFacultades.close();
            resultadoConsulta.close();
            this.CONEXION_BASE_DATOS.desconectar();
        } catch (SQLException excepcion) {
            throw new ErrorDAO("SQLException: Error al consultar Facultades\n" + excepcion.getMessage());
        }

        return listaFacultades;
    }

    public Facultad convertirResultSetAFacultad (ResultSet resultado) throws SQLException {
        Facultad facultad = new Facultad();

        facultad.setId(resultado.getInt(1));
        facultad.setNombre(resultado.getString(2));
        facultad.setIdRegion(resultado.getInt(3));

        return facultad;
    }
}
