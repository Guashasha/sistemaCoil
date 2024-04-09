package Logica.DAO;

import AccesoADatos.RetroalimentacionActividadDB;
import Logica.Bitacora;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import Logica.Interfaces.IRetroalimentacionActividadDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Logica.ErrorDAO.Tipo;

public class DAORetroalimentacionActividad implements IRetroalimentacionActividadDAO {
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        if (!retroalimentacion.esCorrecto()) {
            throw new ErrorDAO("la retroalimentacion es incorrecta", Tipo.VALIDACION);
        }

        if (getPorPersonaYActividad(retroalimentacion.getIdUsuario(), retroalimentacion.getIdActividad()).isPresent()) {
            throw new ErrorDAO("la actividad ya fue calificada por el usuario", Tipo.DUPLICIDAD);
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionActividadDB.agregarRetroalimentacion((RetroalimentacionActividad) retroalimentacion);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        throw new ErrorDAO("metodo no disponible para el objeto", Tipo.VALIDACION);
    }

    @Override
    public Optional<RetroalimentacionActividad> getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("El id es invalido" + id, Tipo.VALIDACION);
        }

        ResultSet rsRetroalimentacion = null;

        try {
            rsRetroalimentacion = RetroalimentacionActividadDB.getPorId(id);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }


        RetroalimentacionActividad objRetroalimentacion = null;

        try {
            objRetroalimentacion = resultSetAObjeto(rsRetroalimentacion);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);

            throw error;
        }

        return Optional.ofNullable(objRetroalimentacion);
    }

    @Override
    public List<RetroalimentacionActividad> getTodos () throws ErrorDAO {
        ArrayList<RetroalimentacionActividad> retroalimentaciones = new ArrayList<>();
        ResultSet resultsRetroalimentaciones = null;

        try {
            resultsRetroalimentaciones = RetroalimentacionActividadDB.getTodos();
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        try {
            while (resultsRetroalimentaciones.next()) {
                RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();

                retroalimentacion.setIdRetroalimentacion(resultsRetroalimentaciones.getInt(1));
                retroalimentacion.setInteraccionConPar(resultsRetroalimentaciones.getInt(2));
                retroalimentacion.setComentario(resultsRetroalimentaciones.getString(3));
                retroalimentacion.setDificultad(resultsRetroalimentaciones.getInt(4));
                retroalimentacion.setInteres(resultsRetroalimentaciones.getInt(5));
                retroalimentacion.setIdUsuario(resultsRetroalimentaciones.getInt(6));
                retroalimentacion.setIdActividad(resultsRetroalimentaciones.getInt(7));

                retroalimentaciones.add(retroalimentacion);
            }
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        return retroalimentaciones;
    }

    @Override
    public Optional<RetroalimentacionActividad> getPorPersonaYActividad (int idPersona, int idActividad) throws ErrorDAO {
        if (idPersona < 1 || idActividad < 1) {
            throw new ErrorDAO("Las id's ingresadas son incorrectas", Tipo.VALIDACION);
        }

        ResultSet resultados = null;

        try {
            resultados = RetroalimentacionActividadDB.getPorPersonaYActividad(idPersona, idActividad);
        }
        catch(SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        RetroalimentacionActividad retroalimentacion = null;

        try {
            if (resultados.next()) {
                retroalimentacion = new RetroalimentacionActividad();
                retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
                retroalimentacion.setInteraccionConPar(resultados.getInt(2));
                retroalimentacion.setComentario(resultados.getString(3));
                retroalimentacion.setDificultad(resultados.getInt(4));
                retroalimentacion.setInteres(resultados.getInt(5));
                retroalimentacion.setIdUsuario(idPersona);
                retroalimentacion.setIdActividad(idActividad);
            }
        } catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        return Optional.ofNullable(retroalimentacion);
    }

    @Override
    public RetroalimentacionActividad resultSetAObjeto (ResultSet resultados) throws ErrorDAO {
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();

        try {
            if (resultados.next()) {
                retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
                retroalimentacion.setInteraccionConPar(resultados.getInt(2));
                retroalimentacion.setComentario(resultados.getString(3));
                retroalimentacion.setDificultad(resultados.getInt(4));
                retroalimentacion.setInteres(resultados.getInt(5));
            }
            else {
                throw new ErrorDAO("Error de conversion a objeto: la retroalimentacion no existe", Tipo.CONEXION);
            }
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        return retroalimentacion;
    }
}
