package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.AcademicoExterno;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoExternoDAO;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DAOAcademicoExterno implements IAcademicoExternoDAO {
    private final ConexionBaseDatos conexionBaseDatos = new ConexionBaseDatos();

    @Override
    public AcademicoExterno getAcademicoExternoPorCedula(int cedulaProfesional) throws ErrorDAO {
        AcademicoExterno academico;
        String consultaAcademicosSQL = "SELECT * FROM datos_academicosExternos WHERE cedulaProfesional = ?";
        PreparedStatement consultaAcademico;
        ResultSet resultadoConsultaAcademico;

        try {
            this.conexionBaseDatos.conectar();
            consultaAcademico = this.conexionBaseDatos.getConexion()
                    .prepareStatement(consultaAcademicosSQL);
            consultaAcademico.setInt(1,cedulaProfesional);
            resultadoConsultaAcademico = consultaAcademico.executeQuery();

            academico = convertirAcademicoExterno(resultadoConsultaAcademico);

            this.conexionBaseDatos.desconectar();
            consultaAcademico.close();
            resultadoConsultaAcademico.close();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return academico;
    }

    @Override
    public int agregarAcademicoExterno (AcademicoExterno academico) throws ErrorDAO {
        int filasAfectadas;
        Universidad institucion = academico.getInstitucion();
        String insertarAcademicoSQL = "{CALL insertar_academicoExterno (?,?,?,?,?,?,?,?)}";
        CallableStatement insertarAcademico;

        try {
            this.conexionBaseDatos.conectar();

            insertarAcademico = this.conexionBaseDatos.getConexion()
                    .prepareCall(insertarAcademicoSQL);
            insertarAcademico.setInt(1, academico.getCedulaProfesional());
            insertarAcademico.setString(2,institucion.getNombre());
            insertarAcademico.setString(3,academico.getNombre());
            insertarAcademico.setString(4,academico.getApellidoPaterno());
            insertarAcademico.setString(5,academico.getApellidoMaterno());
            insertarAcademico.setString(6,academico.getAreaEstudios());
            insertarAcademico.setString(7,academico.getCorreoElectronico());
            insertarAcademico.setString(8,academico.getNumeroTelefono());
            filasAfectadas = insertarAcademico.executeUpdate();

            insertarAcademico.close();
            this.conexionBaseDatos.desconectar();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }
        //El procedure utilizado afecta 4 filas en una inserción correcta
        return filasAfectadas;
    }

    @Override
    public List<AcademicoExterno> getAcademicosPorUniversidad (String institucion) throws ErrorDAO {
        List<AcademicoExterno> listaAcademicos;
        String consultaAcademicosSQL = "SELECT * FROM datos_academicosExternos WHERE universidad = ?";
        PreparedStatement consultaAcademicos;
        ResultSet resultadoConsulta;

        try {
            this.conexionBaseDatos.conectar();

            consultaAcademicos = this.conexionBaseDatos.getConexion()
                    .prepareStatement(consultaAcademicosSQL);
            consultaAcademicos.setString(1, institucion);
            resultadoConsulta = consultaAcademicos.executeQuery();

            listaAcademicos = convertirListaAcademicos(resultadoConsulta);

            this.conexionBaseDatos.desconectar();
            consultaAcademicos.close();
            resultadoConsulta.close();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return listaAcademicos;
    }

    public List<AcademicoExterno> convertirListaAcademicos (ResultSet resultadoConsulta) throws SQLException {
        List<AcademicoExterno> listaAcademicos = null;

        while (resultadoConsulta.next()) {
            listaAcademicos.add(convertirAcademicoExterno(resultadoConsulta));
        }

        return listaAcademicos;
    }

    public AcademicoExterno convertirAcademicoExterno (ResultSet resultadoConsulta) throws SQLException {
        String universidad;
        String paisOrigen;
        AcademicoExterno academico = null;
        Universidad institucion;

        if (resultadoConsulta.next()) {
            academico = new AcademicoExterno();
            institucion = new Universidad();
            academico.setCedulaProfesional(resultadoConsulta.getInt("cedulaProfesional"));
            universidad = resultadoConsulta.getString("universidad");
            paisOrigen = resultadoConsulta.getString("paisOrigen");
            institucion.setNombre(universidad);
            institucion.setPaisOrigen(paisOrigen);
            academico.setInstitucion(institucion);
            academico.setNombre(resultadoConsulta.getString("nombre"));
            academico.setApellidoPaterno(resultadoConsulta.getString("apellidoPaterno"));
            academico.setApellidoMaterno(resultadoConsulta.getString("apellidoMaterno"));
            academico.setAreaEstudios(resultadoConsulta.getString("areaEstudios"));
            academico.setCorreoElectronico(resultadoConsulta.getString("correoElectronico"));
            academico.setNumeroTelefono(resultadoConsulta.getString("numeroTelefonico"));
        }

        return academico;
    }
}
