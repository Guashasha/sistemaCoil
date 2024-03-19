package Logica.Interfaces;

import Logica.Dominio.Retroalimentacion;

public interface IRetroalimentacionDAO {
    int subirCalificación ();
    Retroalimentacion descargarPorId (int idRetroalimentacion);
}
