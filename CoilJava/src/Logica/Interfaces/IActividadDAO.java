package Logica.Interfaces;

import Logica.Dominio.Actividad;

public interface IActividadDAO extends IDAO<Actividad, Integer> {
    public boolean actividadCorrecta (Actividad actividad);
}
