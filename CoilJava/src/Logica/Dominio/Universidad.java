package Logica.Dominio;

public class Universidad {
    private int id;
    private String nombre;
    private Pais paisOrigen;

    public Universidad() {
        this.id = -1;
        this.nombre = "";
    }

    public Universidad (String nombre, Pais paisOrigen) {
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
    }

    public Universidad (int id, String nombre, Pais paisOrigen) {
        this.id = id;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pais getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(Pais paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }
}
