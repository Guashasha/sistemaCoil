package Logica.Dominio;

public class AcademicoUV extends Academico {
    private String categoriaContracion;
    private Facultad facultad;

    public AcademicoUV() {
    }

    public String getCategoriaContracion() {
        return categoriaContracion;
    }

    public void setCategoriaContracion(String categoriaContracion) {
        this.categoriaContracion = categoriaContracion;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
}
