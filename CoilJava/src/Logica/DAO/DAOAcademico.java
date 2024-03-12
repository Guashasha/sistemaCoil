package Logica.DAO;

import AccesoADatos.ConexionBaseDatos;
import Logica.Dominio.Academico;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import Logica.Interfaces.IAcademicoDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAcademico implements IAcademicoDAO {
    private final ConexionBaseDatos conexionBaseDatos = new ConexionBaseDatos();

    @Override
    public boolean academicoRegistrado (int cedulaProfesional) throws ErrorDAO {
        boolean registroExistente;
        String consultaAcademicoSQL = "SELECT cedulaProfesional FROM academico WHERE cedulaProfesional = ?";
        PreparedStatement consultaAcademico;
        ResultSet resultadoConsultaAcademico;

        try {
            this.conexionBaseDatos.conectar();

            consultaAcademico = this.conexionBaseDatos.getConexion()
                    .prepareStatement(consultaAcademicoSQL);
            consultaAcademico.setInt(1,cedulaProfesional);
            resultadoConsultaAcademico = consultaAcademico.executeQuery();
            registroExistente = resultadoConsultaAcademico.next();

            this.conexionBaseDatos.desconectar();
            consultaAcademico.close();
            resultadoConsultaAcademico.close();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return registroExistente;
    }
    @Override
    public ArrayList<Academico> getAcademicosPorAreaEstudios(String areaEstudios) throws ErrorDAO {
        ArrayList<Academico> listaAcademicos;
        String consultaAcademicosSQL = "SELECT * FROM datos_todosacademicos WHERE areaEstudios = ?";
        PreparedStatement consultaAcademicos;
        ResultSet resultadoConsulta;

        try {
            this.conexionBaseDatos.conectar();

            consultaAcademicos = this.conexionBaseDatos.getConexion()
                    .prepareStatement(consultaAcademicosSQL);
            consultaAcademicos.setString(1,areaEstudios);
            resultadoConsulta = consultaAcademicos.executeQuery();

            listaAcademicos = convertirListaAcademicos(resultadoConsulta);

            this.conexionBaseDatos.desconectar();
            consultaAcademicos.close();
            resultadoConsulta.close();
        } catch (SQLException excepcion) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return listaAcademicos;
    }

    @Override
    public int cambiarCorreoElectronico (String correo, int cedulaProfesional) throws ErrorDAO {
        int filasAfectadas;
        String actulizarAcademicoSQL = "UPDATE academico SET correoElectronico = ? WHERE cedulaProfesional = ?";
        PreparedStatement actualizarAcademico;

        try {
            this.conexionBaseDatos.conectar();

            actualizarAcademico = this.conexionBaseDatos.getConexion()
                    .prepareStatement(actulizarAcademicoSQL);
            actualizarAcademico.setString(1,correo);
            actualizarAcademico.setInt(2,cedulaProfesional);
            filasAfectadas = actualizarAcademico.executeUpdate();

            this.conexionBaseDatos.desconectar();
            actualizarAcademico.close();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return filasAfectadas;
    }
    @Override
    public int cambiarTelefono (String telefono, int cedulaProfesional) throws ErrorDAO {
        int filasAfectadas;
        String actulizarAcademicoSQL = "UPDATE academico SET numeroTelefonico = ? WHERE cedulaProfesional = ?";
        PreparedStatement actualizarAcademico;

        try {
            this.conexionBaseDatos.conectar();

            actualizarAcademico = this.conexionBaseDatos.getConexion()
                    .prepareStatement(actulizarAcademicoSQL);
            actualizarAcademico.setString(1, telefono);
            actualizarAcademico.setInt(2,cedulaProfesional);
            filasAfectadas = actualizarAcademico.executeUpdate();

            this.conexionBaseDatos.desconectar();
            actualizarAcademico.close();
        } catch (SQLException excepcionSQL) {
            ErrorDAO error = new ErrorDAO("SQLException");
            throw error;
        }

        return filasAfectadas;
    }

    public ArrayList<Academico> convertirListaAcademicos (ResultSet resultadoConsulta) throws SQLException {
        ArrayList<Academico> listaAcademicos = new ArrayList<>();
        String universidad;
        String paisOrigen;
        Academico academico = new Academico();
        Universidad institucion = new Universidad();

        while (resultadoConsulta.next()) {
            academico.setCedulaProfesional(resultadoConsulta.getInt("cedulaProfesional"));
            universidad = resultadoConsulta.getString("universidad");
            paisOrigen = resultadoConsulta.getString("paisOrigen");
            if (universidad == null) {
                institucion.setNombre("Universidad Veracruzana");
                institucion.setPaisOrigen("México");
            }
            else {
                institucion.setNombre(universidad);
                institucion.setPaisOrigen(paisOrigen);
            }
            academico.setInstitucion(institucion);
            academico.setNombre(resultadoConsulta.getString("nombre"));
            academico.setApellidoPaterno(resultadoConsulta.getString("apellidoPaterno"));
            academico.setApellidoMaterno(resultadoConsulta.getString("apellidoMaterno"));
            academico.setAreaEstudios(resultadoConsulta.getString("areaEstudios"));
            academico.setCorreoElectronico(resultadoConsulta.getString("correoElectronico"));
            academico.setNumeroTelefono(resultadoConsulta.getString("numeroTelefonico"));

            listaAcademicos.add(academico);
        }

        return listaAcademicos;
    }

}
