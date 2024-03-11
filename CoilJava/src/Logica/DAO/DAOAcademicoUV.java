package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.AcademicoUV;
import Logica.Dominio.Facultad;
import Logica.Dominio.Region;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoUVDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOAcademicoUV implements IAcademicoUVDAO {
    private final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();
    @Override
    public int agregarAcademicoUV(AcademicoUV academicoUV, int idFacultad) throws ErrorDAO {
        int resultado;

        String insertarAcademicoUV = "{CALL insertarAcademicoUV(?,?,?,?,?,?,?,?,?)}";
        try (CallableStatement llamadaProcedimiento = this.CONEXION_BASE_DATOS.getConexion().prepareCall(insertarAcademicoUV)) {
            establecerParametrosComunes(llamadaProcedimiento, academicoUV, idFacultad);
            resultado = llamadaProcedimiento.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorDAO("Error al registrar al academico" + e.getMessage());
        }
        return resultado;
    }

    @Override
    public List<AcademicoUV> getAcademicosUVPorRegion(String nombreRegion) throws ErrorDAO {
        List<AcademicoUV> listaAcademicoUV = new ArrayList<>();

        String sqlQuery = "SELECT * FROM vista_academico_uv WHERE nombreRegion = ?";
        try (Connection conexion = CONEXION_BASE_DATOS.getConexion(); PreparedStatement stm = conexion.prepareStatement(sqlQuery)) {
            stm.setObject(1,nombreRegion);
            try (ResultSet rs = stm.executeQuery()) {
                while(rs.next()) {
                    AcademicoUV academicoUV = construirAcademicoUV(rs);
                    listaAcademicoUV.add(academicoUV);
                }
            }
        }
        catch (SQLException e) {
            throw new ErrorDAO("Error al realizar la consulta por region" + e.getMessage());
        }
        return listaAcademicoUV;
    }

    @Override
    public List<AcademicoUV> getAcademicosUVPorContrato(String categoriaContratacion) throws ErrorDAO {
        List<AcademicoUV> listaAcademicoUV = new ArrayList<>();

        String sqlQuery = "SELECT * FROM vista_academico_uv WHERE categoriaContratacion = ?";
        try (Connection conexion = CONEXION_BASE_DATOS.getConexion(); PreparedStatement stm = conexion.prepareStatement(sqlQuery)) {
            stm.setObject(1,categoriaContratacion);
            try (ResultSet rs = stm.executeQuery()) {
                while(rs.next()) {
                    AcademicoUV academicoUV = construirAcademicoUV(rs);
                    listaAcademicoUV.add(academicoUV);
                }
            }
        }
        catch (SQLException e) {
            throw new ErrorDAO("Error al realizar la consulta por contrato" + e.getMessage());
        }
        return listaAcademicoUV;
    }

    @Override
    public List<AcademicoUV> getAcademicosUVPorFacultad(String nombreFacultad) throws ErrorDAO {
        List<AcademicoUV> listaAcademicoUV = new ArrayList<>();

        String sqlQuery = "SELECT * FROM vista_academico_uv WHERE nombreFacultad = ?";
        try (Connection conexion = CONEXION_BASE_DATOS.getConexion(); PreparedStatement stm = conexion.prepareStatement(sqlQuery)) {
            stm.setObject(1,nombreFacultad);
            try (ResultSet rs = stm.executeQuery()) {
                while(rs.next()) {
                    AcademicoUV academicoUV = construirAcademicoUV(rs);
                    listaAcademicoUV.add(academicoUV);
                }
            }
        }
        catch (SQLException e) {
            throw new ErrorDAO("Error al realizar la consulta por facultad" + e.getMessage());
        }
        return listaAcademicoUV;
    }

    @Override
    public int modificarAdademicoUV(AcademicoUV academicoUV) throws ErrorDAO {
        return 0;
    }
    private void establecerParametrosComunes(CallableStatement llamadaProcedimiento, AcademicoUV academicoUV, int idFacultad) throws SQLException {
        llamadaProcedimiento.setString(1, academicoUV.getNombre());
        llamadaProcedimiento.setString(2, academicoUV.getApellidoPaterno());
        llamadaProcedimiento.setString(3, academicoUV.getApellidoMaterno());
        llamadaProcedimiento.setInt(4, academicoUV.getCedulaProfesional());
        llamadaProcedimiento.setString(5, academicoUV.getAreaEstudios());
        llamadaProcedimiento.setString(6, academicoUV.getCorreoElectronico());
        llamadaProcedimiento.setString(7, academicoUV.getNumeroTelefono());
        llamadaProcedimiento.setString(8, academicoUV.getCategoriaContracion());
        llamadaProcedimiento.setInt(9, idFacultad);
    }

    private AcademicoUV construirAcademicoUV(ResultSet rs) throws SQLException {
        AcademicoUV academicoUV = new AcademicoUV();
        Facultad facultad = new Facultad();
        Region region = new Region();

        academicoUV.setNombre(rs.getString("nombre"));
        academicoUV.setApellidoPaterno(rs.getString("apellidoPaterno"));
        academicoUV.setApellidoMaterno(rs.getString("apellidoMaterno"));
        academicoUV.setCedulaProfesional(rs.getInt("cedulaProfesional"));
        academicoUV.setAreaEstudios(rs.getString("areaEstudios"));
        academicoUV.setCorreoElectronico(rs.getString("correoElectronico"));
        academicoUV.setNumeroTelefono(rs.getString("numeroTelefonico"));
        academicoUV.setCategoriaContracion(rs.getString("categoriaContratacion"));

        facultad.setNombre(rs.getString("nombreFacultad"));
        region.setNombre(rs.getString("nombreRegion"));

        facultad.setRegion(region);
        academicoUV.setFacultad(facultad);

        return academicoUV;
    }

}
