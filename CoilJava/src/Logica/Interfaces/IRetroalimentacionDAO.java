package Logica.Interfaces;

import Logica.Dominio.Retroalimentacion;

public interface IRetroalimentacionDAO extends IDAO<Retroalimentacion, Integer> {
    int subirCalificación ();
}
