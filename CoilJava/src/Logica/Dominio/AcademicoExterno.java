package Logica.Dominio;

public class AcademicoExterno extends Academico {
    private Universidad universidad;

    public AcademicoExterno() {
    }

    public Universidad getUniversidad() {
        return universidad;
    }

    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }
}
