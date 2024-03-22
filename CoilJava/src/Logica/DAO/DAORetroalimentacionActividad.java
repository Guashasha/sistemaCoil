package Logica.DAO;

import AccesoADatos.RetroalimentacionActividadDB;
import Logica.Dominio.Retroalimentacion;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;
import Logica.Interfaces.IRetroalimentacionActividadDAO;

import java.util.List;

public class DAORetroalimentacionActividad implements IRetroalimentacionActividadDAO {

    @Override
    public int agregar (Retroalimentacion retroalimentacion) throws ErrorDAO {
        if (retroalimentacion.getClass() != RetroalimentacionActividad.class) {
            throw new ErrorDAO("El metodo esperaba una retroalimentacion de actividad pero recibió " + retroalimentacion.getClass());
        }

        if (retroalimentacion.getInteraccionConPar() <= 0 || retroalimentacion.getInteraccionConPar() > 5) {
            throw new ErrorDAO("Las calificaciones de la retroalimentación están incompletas");
        }

        int resultado = -1;

        try {
            resultado = RetroalimentacionActividadDB.agregarRetroalimentacion((RetroalimentacionActividad) retroalimentacion);
        }
        catch (ErrorDAO error) {
            // TODO -----------------------------
            // Escribir a log

            throw error;
        }

        return resultado;
    }

    @Override
    public int modificar (Integer y) throws ErrorDAO {
        return 0;
    }

    @Override
    public Retroalimentacion getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("El id es invalido" + id);
        }

        RetroalimentacionActividad retroalimentacion = null;

        try {
            retroalimentacion = RetroalimentacionActividadDB.getPorId(id);
        }
        catch (ErrorDAO error) {
            // TODO ----------------------------------
            // Escribir a log

            throw error;
        }

        return retroalimentacion;
    }

    @Override
    public List<Retroalimentacion> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Retroalimentacion getPorIdAcademico (String idAcademico) throws ErrorDAO {
        return null;
    }

    @Override
    public Retroalimentacion getPorIdEstudiante (int idEstudiante) throws ErrorDAO {
        return null;
    }

    @Override
    public Retroalimentacion getPorIdPersona (int idPersona) throws ErrorDAO {
        return null;
    }
}
