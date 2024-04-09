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
        }

        try {
            while (resultsRetroalimentaciones.next()) {
                RetroalimentacionActividad retroalimentacion = resultSetAObjeto(resultsRetroalimentaciones);

                if (retroalimentacion.esCorrecto()) {
                    retroalimentaciones.add(retroalimentacion);
                }
            }
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
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
        }

        RetroalimentacionActividad retroalimentacion = null;

        try {
            if (resultados.next()) {
                retroalimentacion = resultSetAObjeto(resultados);
            }
        } catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return Optional.ofNullable(retroalimentacion);
    }

    @Override
    public RetroalimentacionActividad resultSetAObjeto (ResultSet resultados) {
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();

        try {
            retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
            retroalimentacion.setInteraccionConPar(resultados.getInt(2));
            retroalimentacion.setComentario(resultados.getString(3));
            retroalimentacion.setDificultad(resultados.getInt(4));
            retroalimentacion.setInteres(resultados.getInt(5));
            retroalimentacion.setIdUsuario(resultados.getInt(6));
            retroalimentacion.setIdActividad(resultados.getInt(7));
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return retroalimentacion;
    }
}
