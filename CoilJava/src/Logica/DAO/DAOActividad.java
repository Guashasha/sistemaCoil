package Logica.DAO;

import Logica.Bitacora;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import Logica.ErrorDAO.Tipo;
import Logica.Interfaces.IActividadDAO;
import AccesoADatos.ActividadDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOActividad implements IActividadDAO {
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (Actividad actividad) throws ErrorDAO {
        if (!actividadCorrecta(actividad)) {
            throw new ErrorDAO("La actividad tiene campos vacios", Tipo.VALIDACION);
        }

        int resultado = -1;

        try {
            resultado = ActividadDB.agregarActividad(actividad);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);

            throw error;
        }

        return resultado;
    }

    @Override
    public boolean actividadCorrecta (Actividad actividad) {
        boolean resultado = true;

        if (actividad.getDescripcion().isBlank()) {
            resultado = false;
        }

        if (actividad.getTitulo().isBlank()) {
            resultado = false;
        }

        // FIXME
        return resultado;
    }

    @Override
    public int modificar (Actividad obj) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<Actividad> getPorId (Integer idActividad) throws ErrorDAO {
        ResultSet resultado = null;

        try {
            resultado = ActividadDB.getPorId(idActividad);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);

            throw error;
        }

        return Optional.ofNullable(resultSetAObjeto(resultado));
    }

    @Override
    public List<Actividad> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Actividad resultSetAObjeto (ResultSet resultados) {
        Actividad actividad = new Actividad();

        try {
            if (resultados.next()) {
                actividad.setIdActividad(resultados.getInt(0));
                actividad.setTitulo(resultados.getString(1));
                actividad.setDescripcion(resultados.getString(2));
                actividad.setTipo(Actividad.TipoActividad.valueOf(resultados.getString(3)));
            }
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        }

        return actividad;
    }
}
