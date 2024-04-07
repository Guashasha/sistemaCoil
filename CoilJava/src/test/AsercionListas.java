package test;

import Logica.Dominio.Universidad;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class AsercionListas extends Assertions {
    public static void assertEqualList (List<Universidad> esperada, List<Universidad> obtenida) {
        if (esperada.size() != obtenida.size()) {
            throw new AssertionError("Tamaño esperada = " + esperada.size() + ", tamaño obtenida = " + obtenida.size());
        }

        Universidad universidadEsperada;
        Universidad universidadObtenida;
        while (!esperada.isEmpty() && !obtenida.isEmpty()) {
            universidadEsperada = esperada.get(0);
            esperada.remove(0);
            universidadObtenida = obtenida.get(0);
            obtenida.remove(0);
            if (universidadEsperada.getId() != universidadObtenida.getId()) {
                throw new AssertionError("Id esperada = " + universidadEsperada.getId() + ", Id obtenida = " + universidadObtenida.getId());
            }
            else if (!universidadEsperada.getNombre().equals(universidadObtenida.getNombre())) {
                throw new AssertionError("Nombre esperada = " + universidadEsperada.getNombre() + ", Nombre obtenida = " + universidadObtenida.getNombre());
            }
            else if (!universidadEsperada.getPaisOrigen().equals(universidadObtenida.getPaisOrigen())) {
                throw new AssertionError("Pais esperada = " + universidadEsperada.getPaisOrigen() + ", Pais obtenida = " + universidadObtenida.getPaisOrigen());
            }
        }
    }
}
