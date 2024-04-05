package Logica.DAO;

import AccesoADatos.RetroalimentacionActividadDB;
import Logica.Bitacora;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import Logica.Interfaces.IRetroalimentacionActividadDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAORetroalimentacionActividad implements IRetroalimentacionActividadDAO {
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    @Override
    public int agregar (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        if (!validarRetroalimentacion(retroalimentacion)) {
            throw new ErrorDAO("la retroalimentacion es incorrecta");
        }

        if (getPorPersonaYActividad(retroalimentacion.getIdUsuario()).isPresent()) {
            throw new ErrorDAO("la actividad ya fue calificada por el usuario");
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionActividadDB.agregarRetroalimentacion((RetroalimentacionActividad) retroalimentacion);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);

            throw error;
        }

        return resultado;
    }

    @Override
    public int modificar (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<RetroalimentacionActividad> getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("El id es invalido" + id);
        }

        ResultSet rsRetroalimentacion = null;

        try {
            rsRetroalimentacion = RetroalimentacionActividadDB.getPorId(id);
        }
        catch (ErrorDAO error) {
            bitacora.escribirError(error);

            throw error;
        }

        RetroalimentacionActividad objRetroalimentacion = resultSetAObjeto(rsRetroalimentacion);

        return Optional.ofNullable(objRetroalimentacion);
    }

    @Override
    public List<RetroalimentacionActividad> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Optional<RetroalimentacionActividad> getPorPersonaYActividad (int idPersona) throws ErrorDAO {
        return null;
    }

    @Override
    public RetroalimentacionActividad resultSetAObjeto (ResultSet resultados) {
        RetroalimentacionActividad retroalimentacion = new RetroalimentacionActividad();

        try {
            if (resultados.next()) {
                retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
                retroalimentacion.setInteraccionConPar(resultados.getInt(2));
                retroalimentacion.setComentario(resultados.getString(3));
                retroalimentacion.setDificultad(resultados.getInt(5));
                retroalimentacion.setInteres(resultados.getInt(6));
            }
            else {
                retroalimentacion = null;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return retroalimentacion;
    }

    @Override
    public boolean validarRetroalimentacion (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        boolean resultado = true;

        if (!calificacionCorrecta(retroalimentacion.getInteres())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getDificultad())) {
            resultado = false;
        }

        if (!calificacionCorrecta(retroalimentacion.getInteraccionConPar())) {
            resultado = false;
        }

        return resultado;
    }

    @Override
    public boolean calificacionCorrecta (int calificacion) {
        return calificacion >= 1 && calificacion <= 5;
    }
}
