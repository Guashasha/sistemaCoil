package Utilidades;

public class ErrorDAO extends Error {
    public enum Tipo {
        INSERCION,
        MODIFICACION,
        CONSULTA,
        CONEXION,
        DUPLICIDAD,
        VALIDACION,
        INICIO_SESION,
        NO_IMPLEMENTADO
    }

    private String mensaje;
    private Tipo tipo;

    public ErrorDAO (String mensaje, Tipo tipo) {
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    @Override
    public String getMessage () {
        return this.mensaje;
    }

    public Tipo getTipo () {
        return this.tipo;
    }
}
