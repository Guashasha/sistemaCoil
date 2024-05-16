package Utilidades;

import Logica.Dominio.Cuenta;

public class PlantillasCorreo {
    private static final String HTML_IMAGEN = """
             <a href="https://ibb.co/XY7yw7P">
                    <img src="https://i.ibb.co/2Zv8bvr/banner-Coil.png" alt="banner-Coil" border="0" width="300">
                </a>\
            """;

    public static String cuentaAceptada (String nombreCompleto) {
        return "<html>\n" +
                "<head>\n" +
                "<title>Bienvenida a MiCoil</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Estimado(a)" + nombreCompleto + ",</h1>\n" +
                "<p>Su cuenta ha sido aceptada en el sistema MiCoil.</p>\n" +
                "<p>Cosas por hacer:</p>\n" +
                "<ol>\n" +
                "<li>Realice su primer inicio de sesión:</li>\n" +
                "<ul>\n" +
                "<li>Se le pedirá que ingrese algunos datos faltantes.</li>\n" +
                "</ul>\n" +
                "</ol>\n" +
                "<p>Disfrute de la aplicación MiCoil.</p>\n" +
                HTML_IMAGEN +
                "</body>\n" +
                "</html>";
    }

    public static String cuentaRechazada (String nombreCompleto) {
        return "<html>\n" +
                "<head>\n" +
                "    <title>Rechazo de cuenta en MiCoil</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Estimado(a)" + nombreCompleto + ",</h1>\n" +
                "    <p>Su cuenta ha sido rechazada en el sistema MiCoil.</p>\n" +
                "    <p>En caso de dudas sobre esta decisión, por favor comuníquese al siguiente correo electrónico:</p>\n" +
                "    <p><a href=\"mailto:vic@uv.mx\">vic@uv.mx</a></p>\n" +
                HTML_IMAGEN +
                "</body>\n" +
                "</html>";
    }


    public static String cuentaEstudiante (String nombreEstudiante, String nombreAcademico, Cuenta cuenta) {
        return "<html>\n" +
                "<head>\n" +
                "    <title>Registro en MiCoil</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Estimado estudiante " + nombreEstudiante + ",</h1>\n" +
                "    <p>Ha sido registrado por " + nombreAcademico + " a la aplicación MiCoil.</p>\n" +
                "    <p>Usuario: " + cuenta.getNombreUsuario() + "</p>\n" +
                "    <p>Contraseña: " + cuenta.getContrasena() + "</p>\n" +
                HTML_IMAGEN +
                "</body>\n" +
                "</html>";
    }
}
