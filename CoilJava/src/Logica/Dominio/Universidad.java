package Logica.Dominio;

public class Universidad {
    private int id;
    private String nombre;
    private String paisOrigen;

    public Universidad() {
    }

    public Universidad (String nombre, String paisOrigen) {
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
    }

    public Universidad (int id, String nombre, String paisOrigen) {
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

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }
}
