package Logica;

public class ErrorDAO extends Error {
    private String mensaje;

    public ErrorDAO (String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje () {
        return this.mensaje;
    }
}
