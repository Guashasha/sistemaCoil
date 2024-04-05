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

    public static void assertEqualListPais (List<Pais> listaEsperada, List<Pais> listaObtenida) {
        compararTamano(listaEsperada,listaObtenida);

        Pais esperado;
        Pais obtenido;
        for (int i = 0; i < listaEsperada.size(); i++) {
            esperado = listaEsperada.get(0);
            obtenido = listaObtenida.get(0);

            if (esperado.getId() != obtenido.getId()) {
                throw new AssertionError("Pais " + i + ": Id esperado = " + esperado.getId() + ", Id obtenido = " + obtenido.getId());
            }
            else if (!esperado.getIso().equals(obtenido.getIso())) {
                throw new AssertionError("Pais " + i + ": Iso esperado = " + esperado.getIso() + ", Iso obtenido = " + obtenido.getIso());
            }
            else if (!esperado.getNombre().equals(obtenido.getNombre())) {
                throw new AssertionError("Pais " + i + ": Nombre esperado = " + esperado.getNombre() + ", Nombre obtenido = " + obtenido.getNombre());
            }

            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    public static void assertEqualListUniversidad (List<Universidad> listaEsperada, List<Universidad> listaObtenida) {
        compararTamano(listaEsperada, listaObtenida);

        Universidad esperada;
        Universidad obtenida;
        for (int i = 0; i < listaEsperada.size(); i++) {
            esperada = listaEsperada.get(0);
            obtenida = listaObtenida.get(0);

            if (esperada.getId() != obtenida.getId()) {
                throw new AssertionError("Universidad " + i + "Id esperado = " + obtenida.getId() + ", Id obtenido = " + obtenida.getId());
            }
            else if (!esperada.getNombre().equals(obtenida.getNombre())) {
                throw new AssertionError("Universidad " + i + "Nombre esperado = " + esperada.getNombre() + ", Nombre obtenido = " + obtenida.getNombre());
            }
            else if (esperada.getIdPais() != obtenida.getIdPais()) {
                throw new AssertionError("Universidad " + i + "idPais esperado = " + esperada.getIdPais() + ", idPais obtenido = " + obtenida.getIdPais());
            }

            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    public static void assertEqualListFacultad (List<Facultad> listaEsperada, List<Facultad> listaObtenida) {
        compararTamano(listaEsperada,listaObtenida);

        Facultad esperada;
        Facultad obtenida;
        for (int i = 0; i < listaEsperada.size(); i++) {
            esperada = listaEsperada.get(0);
            obtenida = listaObtenida.get(0);

            if (esperada.getId() != obtenida.getId()) {
                throw new AssertionError("Facultad " + i + "Id esperado = " + esperada.getId() + ", Id obtenido = " + obtenida.getId());
            }
            else if (!esperada.getNombre().equals(obtenida.getNombre())) {
                throw new AssertionError("Facultad " + i + "Nombre esperado = " + esperada.getNombre() + ", Nombre obtenido = " + obtenida.getNombre());
            }
            else if (esperada.getIdRegion() != obtenida.getIdRegion()) {
                throw new AssertionError("Facultad " + i + "idRegion esperado = " + esperada.getIdRegion() + ", idRegion obtenido = " + obtenida.getIdRegion());
            }

            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }

    public static void assertEqualListRegion (List<Region> listaEsperada, List<Region> listaObtenida) {
        compararTamano(listaEsperada,listaObtenida);

        Region esperada;
        Region obtenida;
        for (int i = 0; i < listaEsperada.size(); i++) {
            esperada = listaEsperada.get(0);
            obtenida = listaObtenida.get(0);

            if (esperada.getId() != obtenida.getId()) {
                throw new AssertionError("Region " + i + "Id esperado = " + esperada.getId() + ", Id obtenido = " + obtenida.getId());
            }
            else if (!esperada.getNombre().equals(obtenida.getNombre())) {
                throw new AssertionError("Region " + i + "Nombre esperado = " + esperada.getNombre() + ", Nombre obtenido = " + obtenida.getNombre());
            }
            listaEsperada.remove(0);
            listaObtenida.remove(0);
        }
    }
    
}