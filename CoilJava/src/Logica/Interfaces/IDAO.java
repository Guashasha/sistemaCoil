package Logica.Interfaces;

import java.util.List;
import java.util.Optional;

public interface IDAO<Tipo> {
    int agregar (Tipo t);
    int modificar (Tipo t);
    List<Tipo> getTodos ();
}
