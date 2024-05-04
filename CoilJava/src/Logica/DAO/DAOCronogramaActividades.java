package Logica.DAO;

import AccesoADatos.CronogramaActividadDB;
import Logica.Dominio.Actividad;
import Logica.Dominio.ActividadVinculada;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IDAO;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOCronogramaActividades implements IDAO<ActividadVinculada, Integer> {
    private static final Logger BITACORA = Logger.getLogger(DAOCronogramaActividades.class);

    @Override
    public int agregar (ActividadVinculada actividadVinculada) throws ErrorDAO {
        if (!actividadVinculada.getPeriodo()
                .esCorrecto()) {
            throw new ErrorDAO("El periodo especificado es incorrecto.", ErrorDAO.Tipo.VALIDACION);
        } else if (!actividadVinculada.getActividad()
                .esCorrecta()) {
            throw new ErrorDAO("La actividad es incorrecta", ErrorDAO.Tipo.VALIDACION);
        } else if (!actividadVinculada.getColaboracion()
                .esValido()) {
            throw new ErrorDAO("La colaboración es incorrecta", ErrorDAO.Tipo.VALIDACION);
        }

        int resultado = -1;

        try {
            resultado = CronogramaActividadDB.agregar(actividadVinculada.getActividad(), actividadVinculada.getColaboracion(), actividadVinculada.getPeriodo());
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error de sql: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        if (resultado < -1) {
            throw new ErrorDAO("No se pudo registrar la actividad al cronograma", ErrorDAO.Tipo.INSERCION);
        }

        return 1;
    }

    @Override
    public int modificar (ActividadVinculada actividadVinculada) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("La función no está implementada en la clase DAOCronogramaActividades");
    }

    @Override
    public Optional<ActividadVinculada> getPorId (Integer id) throws ErrorDAO {
        throw new ErrorDAO("La función no está implementada en la clase DAOCronogramaActividades", ErrorDAO.Tipo.CONSULTA);
    }

    public Optional<ActividadVinculada> getPorActividadYColaboracion (int idActividad, int idColaboracion) {
        ResultSet rsActividad = null;

        try {
            rsActividad = CronogramaActividadDB.getPorActividadYColaboracion(idActividad, idColaboracion);
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error de conexion a la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        ActividadVinculada actividad = null;

        try {
            if (rsActividad != null && rsActividad.next()) {
                actividad = resultSetAObjeto(rsActividad);

                rsActividad.close();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return Optional.ofNullable(actividad);
    }

    @Override
    public List<ActividadVinculada> getTodos () throws ErrorDAO {
        ResultSet actividades;

        try {
            actividades = CronogramaActividadDB.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Error al recuperar las actividades", ErrorDAO.Tipo.CONEXION);
        }

        List<ActividadVinculada> actividadesLista = new ArrayList<>();

        if (actividades == null) {
            return actividadesLista;
        }

        try {
            while (actividades.next()) {
                ActividadVinculada actividadVinculada = resultSetAObjeto(actividades);

                if (actividadVinculada.esCorrecto()) {
                    actividadesLista.add(actividadVinculada);
                }
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error al recuperar las actividades de la colaboración", ErrorDAO.Tipo.CONEXION);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return actividadesLista;
    }

    @Override
    public ActividadVinculada resultSetAObjeto (ResultSet resultados) {
        ActividadVinculada actividadVinculada = null;
        try {
            DAOActividad daoActividad = new DAOActividad();
            DAOColaboracion daoColaboracion = new DAOColaboracion();

            int id = resultados.getInt(1);
            Optional<Actividad> actividad = daoActividad.getPorId(resultados.getInt(2));
            Optional<Colaboracion> colaboracion = daoColaboracion.getPorId(resultados.getInt(3));
            LocalDate fechaInicio = resultados.getDate(4)
                    .toLocalDate();
            LocalDate fechaFin = resultados.getDate(5)
                    .toLocalDate();

            if (actividad.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            if (colaboracion.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            actividadVinculada = new ActividadVinculada(actividad.get(), colaboracion.get(), new Periodo(fechaInicio, fechaFin));
        }
        catch (SQLException error) {
            BITACORA.error(error);
        }

        return actividadVinculada;
    }
}
