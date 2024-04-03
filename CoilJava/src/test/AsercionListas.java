package test;

import Logica.Dominio.Facultad;
import Logica.Dominio.Pais;
import Logica.Dominio.Region;
import Logica.Dominio.Universidad;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class AsercionListas extends Assertions {
    private static void  compararTamano (List esperada, List obtenida) {
        if (esperada.size() != obtenida.size()) {
            throw new AssertionError("Tamaño esperada = " + esperada.size() + ", tamaño obtenida = " + obtenida.size());
        }
    }

    private static void compararRegiones (int elemento, Region esperada, Region obtenida) {
        if (esperada.getId() != obtenida.getId()) {
            throw new AssertionError("Region " + elemento + "Id esperado = " + esperada.getId() + ", Id obtenido = " + obtenida.getId());
        }
        else if (!esperada.getNombre().equals(obtenida.getNombre())) {
            throw new AssertionError("Region " + elemento + "Nombre esperado = " + esperada.getNombre() + ", Nombre obtenido = " + obtenida.getNombre());
        }
    }

    public static void compararPaises (int elemento, Pais esperado, Pais obtenido) {
        if (esperado.getId() != obtenido.getId()) {
            throw new AssertionError("Pais " + elemento + ": Id esperado = " + esperado.getId() + ", Id obtenido = " + obtenido.getId());
        }
        else if (!esperado.getIso().equals(obtenido.getIso())) {
            throw new AssertionError("Pais " + elemento + ": Iso esperado = " + esperado.getIso() + ", Iso obtenido = " + obtenido.getIso());
        }
        else if (!esperado.getNombre().equals(obtenido.getNombre())) {
            throw new AssertionError("Pais " + elemento + ": Nombre esperado = " + esperado.getNombre() + ", Nombre obtenido = " + obtenido.getNombre());
        }
    }

    public static void assertEqualListPais (List<Pais> esperada, List<Pais> obtenida) {
        compararTamano(esperada,obtenida);

        for (int i = 0; i < esperada.size(); i++) {
            compararPaises(i,esperada.get(0),obtenida.get(0));
            esperada.remove(0);
            obtenida.remove(0);
        }
    }

    public static void assertEqualListUniversidad (List<Universidad> esperada, List<Universidad> obtenida) {
        compararTamano(esperada, obtenida);

        Universidad universidadEsperada;
        Universidad universidadObtenida;
        for (int i = 0; i < esperada.size(); i++) {
            universidadEsperada = esperada.get(0);
            universidadObtenida = obtenida.get(0);

            if (universidadEsperada.getId() != universidadObtenida.getId()) {
                throw new AssertionError("Universidad " + i + "Id esperado = " + universidadObtenida.getId() + ", Id obtenido = " + universidadObtenida.getId());
            }
            else if (!universidadEsperada.getNombre().equals(universidadObtenida.getNombre())) {
                throw new AssertionError("Universidad " + i + "Nombre esperado = " + universidadEsperada.getNombre() + ", Nombre obtenido = " + universidadObtenida.getNombre());
            }
            else if (universidadEsperada.getIdPais() != universidadObtenida.getIdPais()) {
                throw new AssertionError("Universidad " + i + "idPais esperado = " + universidadEsperada.getIdPais() + ", idPais obtenido = " + universidadObtenida.getIdPais());
            }

            esperada.remove(0);
            obtenida.remove(0);
        }
    }

    public static void assertEqualListFacultad (List<Facultad> esperada, List<Facultad> obtenida) {
        compararTamano(esperada,obtenida);

        Facultad facultadEsperada;
        Facultad facultadObtenida;
        for (int i = 0; i < esperada.size(); i++) {
            facultadEsperada = esperada.get(0);
            facultadObtenida = obtenida.get(0);

            if (facultadEsperada.getId() != facultadObtenida.getId()) {
                throw new AssertionError("Facultad " + i + "Id esperado = " + facultadEsperada.getId() + ", Id obtenido = " + facultadObtenida.getId());
            }
            else if (!facultadEsperada.getNombre().equals(facultadObtenida.getNombre())) {
                throw new AssertionError("Facultad " + i + "Nombre esperado = " + facultadEsperada.getNombre() + ", Nombre obtenido = " + facultadObtenida.getNombre());
            }
            else if (facultadEsperada.getIdRegion() != facultadObtenida.getIdRegion()) {
                throw new AssertionError("Facultad " + i + "idRegion esperado = " + facultadEsperada.getIdRegion() + ", idRegion obtenido = " + facultadObtenida.getIdRegion());
            }

            esperada.remove(0);
            obtenida.remove(0);
        }
    }

    public static void assertEqualListRegion (List<Region> esperada, List<Region> obtenida) {
        compararTamano(esperada,obtenida);

        for (int i = 0; i < esperada.size(); i++) {
            compararRegiones(i,esperada.get(0),obtenida.get(0));
            esperada.remove(0);
            obtenida.remove(0);
        }
    }



}