package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.Academico;
import Logica.Dominio.Universidad;
import Logica.Interfaces.IAcademicoDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class DAOAcademico implements IAcademicoDAO {
    private final ConexionBaseDatos conexionBaseDatos = new ConexionBaseDatos();

    @Override
    public int registrarAcademicoExterno (Academico academicoExterno) throws SQLTimeoutException, SQLException {
        String nombre = academicoExterno.getNombre();
        String apellidoPaterno = academicoExterno.getApellidoPaterno();
        String apellidoMaterno = academicoExterno.getApellidoMaterno();
        int cedulaProfesional = academicoExterno.getCedulaProfesional();
        //Universidad universidad = academicoExterno.getUniversidad();
        String areaEstudios = academicoExterno.getAreaEstudios();
        String correoElectronico = academicoExterno.getCorreoElectronico();
        String numeroTelefono = academicoExterno.getNumeroTelefono();
        String insertarAcademicoSQL = "INSERT INTO academico (cedulaProfesional,institucion,areaEstudios,correoElectronico,numeroTelefono) VALUES (?.?.?.?.?);";
        PreparedStatement insercionAcademico;
        int filasAfectadas;

        this.conexionBaseDatos.conectar();

        insercionAcademico = this.conexionBaseDatos.getConexion()
                .prepareStatement(insertarAcademicoSQL);
        insercionAcademico.setInt(1, cedulaProfesional);

        //SE USARÁN PROCEDURES PARA REGISTRAR EN VARIAS TABLAS AL MISMO TIEMPO?
        return 0;
    }

    @Override
    public boolean academicoRegistrado(int cedulaProfesional) throws SQLException {
        boolean registroExistente;
        String consultaAcademicoSQL = "SELECT cedulaProfesional FROM academico WHERE cedulaProfesional = ?";
        PreparedStatement consultaAcademico = null;
        ResultSet resultadoConsultaAcademico = null;

        try {
            this.conexionBaseDatos.conectar();
            consultaAcademico = this.conexionBaseDatos.getConexion()
                    .prepareStatement(consultaAcademicoSQL);
            consultaAcademico.setInt(1,cedulaProfesional);
            resultadoConsultaAcademico = consultaAcademico.executeQuery();
            registroExistente = resultadoConsultaAcademico.next();
        } catch (SQLException excepcionSQL) {
            this.conexionBaseDatos.desconectar();
            consultaAcademico.close();
            resultadoConsultaAcademico.close();
            throw excepcionSQL;
        }

        this.conexionBaseDatos.desconectar();
        consultaAcademico.close();
        resultadoConsultaAcademico.close();
        return registroExistente;
    }
}
