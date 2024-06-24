package Utilidades;

/**
 * La clase ErrorDAO es un error personalizado que tiene el propósito de propagar errores menores o mensajes específicos relacionados a otra excepción
 */
public class ErrorDAO extends Error {
    /**
     * Tipos en los que se puede clasificar el error que comunica la clase ErrorDAO
     */
    public enum Tipo {
        INSERCION,
        MODIFICACION,
        CONSULTA,
        CONEXION,
        DUPLICIDAD,
        VALIDACION,
        INICIO_SESION,
        NO_IMPLEMENTADO,
        ERROR_CONEXION_INTERNET,
        CORREO
    }

    private String mensaje;
    private Tipo tipo;

    /**
     * Inicializa un objeto ErrorDAO con su mensaje y su tipo.
     * @param mensaje Mensaje que se quiere propagar.
     * @param tipo Tipo de ErrorDAO relacionado al mensaje del error.
     */
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
