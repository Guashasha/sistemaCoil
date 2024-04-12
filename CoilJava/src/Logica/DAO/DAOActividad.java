package Logica.DAO;

import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import Logica.ErrorDAO.Tipo;
import Logica.Interfaces.IActividadDAO;
import AccesoADatos.ActividadDB;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOActividad implements IActividadDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividad.class);

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
            BITACORA.error(error);
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
            BITACORA.error(error);
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
            BITACORA.error(error);
        }

        Actividad actividad = null;

        try {
            if (resultado.next()) {
                actividad = resultSetAObjeto(resultado);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            BITACORA.error(error);
        }

        Actividad actividad = null;

        try {
            if (resultado.next()) {
                actividad = resultSetAObjeto(resultado);
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(actividad);
    }

    @Override
    public List<Actividad> getTodos () throws ErrorDAO {
        ResultSet resultados = null;

        try {
            resultados = ActividadDB.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
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
            BITACORA.error(error);
        }

        return actividades;
    }

    @Override
    public Actividad resultSetAObjeto (ResultSet resultados) {
        Actividad actividad = null;

        try {
            actividad = new Actividad();
            actividad.setIdActividad(resultados.getInt(1));
            actividad.setTitulo(resultados.getString(2));
            actividad.setDescripcion(resultados.getString(3));
            actividad.setTipo(Actividad.TipoActividad.valueOf(resultados.getString(4)));
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return actividad;
    }
}
