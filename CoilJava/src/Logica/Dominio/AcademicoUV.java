package Logica.Dominio;

import Logica.Dominio.Academico;

public class AcademicoUV extends Academico {
    private String categoriaContracion;
    private String region;
    private String facultad;

    public AcademicoUV() {
    }

    public String getCategoriaContracion() {
        return categoriaContracion;
    }

    public void setCategoriaContracion(String categoriaContracion) {
        this.categoriaContracion = categoriaContracion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }
}
