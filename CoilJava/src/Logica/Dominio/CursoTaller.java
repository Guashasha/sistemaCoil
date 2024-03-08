package Logica.Dominio;

public class CursoTaller {
    private String nombre;
    private String expositor;
    private Periodo periodo;
    private String modalidad;

    public CursoTaller(String nombre, String expositor, Periodo periodo, String modalidad) {
        this.nombre = nombre;
        this.expositor = expositor;
        this.periodo = periodo;
        this.modalidad = modalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExpositor() {
        return expositor;
    }

    public void setExpositor(String expositor) {
        this.expositor = expositor;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}
