package Logica.DAO;

import Logica.Bitacora;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import Logica.ErrorDAO.Tipo;
import Logica.Interfaces.IActividadDAO;
import AccesoADatos.ActividadDB;

import javax.swing.text.html.Option;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOActividad implements IActividadDAO {
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (Actividad actividad) throws ErrorDAO {
        if (!actividad.esCorrecta()) {
            throw new ErrorDAO("La actividad es incorrecta", Tipo.VALIDACION);
        }

        if (getPorTitulo(actividad.getTitulo()).isPresent()) {
            throw new ErrorDAO("la actividad ya existe", Tipo.DUPLICIDAD);
        }

        int resultado = -1;

        try {
            resultado = ActividadDB.agregarActividad(actividad);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return resultado;
    }

    @Override
    public int modificar (Actividad actividad) throws ErrorDAO {
        if (!actividad.esCorrecta()) {
            throw new ErrorDAO("La actividad es incorrecta", Tipo.VALIDACION);
        }

        if (getPorTitulo(actividad.getTitulo()).isPresent()) {
            throw new ErrorDAO("la actividad ya existe", Tipo.DUPLICIDAD);
        }

        int resultado = -1;

        try {
            resultado = ActividadDB.modificarActividad(actividad);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return resultado;
    }

    @Override
    public Optional<Actividad> getPorId (Integer idActividad) throws ErrorDAO {
        ResultSet resultado = null;

        try {
            resultado = ActividadDB.getPorId(idActividad);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return Optional.ofNullable(resultSetAObjeto(resultado));
    }

    @Override
    public Optional<Actividad> getPorTitulo (String titulo) {
        ResultSet resultado = null;

        try {
            resultado = ActividadDB.getPorTitulo(titulo);
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        Actividad actividad = resultSetAObjeto(resultado);

        return Optional.ofNullable(actividad);
    }

    @Override
    public List<Actividad> getTodos () throws ErrorDAO {
        ResultSet resultados = null;

        try {
            resultados = ActividadDB.getTodos();
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        List<Actividad> actividades = new ArrayList<>();

        try {
            while (resultados.next()) {
                Actividad actividad = resultSetAObjeto(resultados);

                if (actividad.esCorrecta()) {
                    actividades.add(actividad);
                }
            }
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return actividades;
    }

    @Override
    public Actividad resultSetAObjeto (ResultSet resultados) {
        Actividad actividad = null;

        try {
            actividad = new Actividad();
            actividad.setIdActividad(resultados.getInt(0));
            actividad.setTitulo(resultados.getString(1));
            actividad.setDescripcion(resultados.getString(2));
            actividad.setTipo(Actividad.TipoActividad.valueOf(resultados.getString(3)));
        }
        catch (SQLException error) {
            bitacora.escribirError(error);
        }

        return actividad;
    }
}
