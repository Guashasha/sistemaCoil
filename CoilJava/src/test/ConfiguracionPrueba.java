package test;


import org.apache.log4j.Logger;

import java.sql.*;

public class ConfiguracionPrueba {
    private static final Logger BITACORA = Logger.getLogger(ConfiguracionPrueba.class);

    public static void borrarDatosTablaAcademico () {
        ejecutarInstruccionSQL("DELETE FROM academico;");
    }
    public static void borrarDatosTablaPersona () {
        ejecutarInstruccionSQL("DELETE FROM persona;");
        ejecutarInstruccionSQL("ALTER TABLE persona AUTO_INCREMENT = 0;");
    }
    public static void borrarDatosTablaEstudiante () {
        ejecutarInstruccionSQL("DELETE FROM estudiante;");
        ejecutarInstruccionSQL("ALTER TABLE estudiante AUTO_INCREMENT = 0");
    }
    public static void borrarDatosTablaUniversidad () {
        ejecutarInstruccionSQL("DELETE FROM universidad;");
        ejecutarInstruccionSQL("ALTER TABLE universidad AUTO_INCREMENT = 0;");
    }
    public static void borrarDatosTablaAcademicoDesarrolla () {
        ejecutarInstruccionSQL("DELETE FROM academicodesarrolla;");
    }
    public static void borrarDatosTablaActividad () {
        ejecutarInstruccionSQL("DELETE FROM actividad;");
        ejecutarInstruccionSQL("ALTER TABLE actividad AUTO_INCREMENT = 0;");
    }
    public static void borrarDatosTablaColaboracion () {
        ejecutarInstruccionSQL("DELETE FROM colaboracion;");
        ejecutarInstruccionSQL("ALTER TABLE colaboracion AUTO_INCREMENT = 0;");
    }

    public static void borrarDatosTablaCuenta () {
        ejecutarInstruccionSQL("DELETE FROM cuenta;");
        ejecutarInstruccionSQL("ALTER TABLE cuenta AUTO_INCREMENT = 0;");
    }
    public static void borrarDatosTablaEstudiantesColaboracion () {
        ejecutarInstruccionSQL("DELETE FROM estudiantesColaboracion;");
    }
    public static void borrarDatosTablaFacultad () {
        ejecutarInstruccionSQL("DELETE FROM facultad;");
        ejecutarInstruccionSQL("ALTER TABLE facultad AUTO_INCREMENT = 0;");
    }
    public static void borrarDatosTablaRegion () {
        ejecutarInstruccionSQL("DELETE FROM region;");
        ejecutarInstruccionSQL("ALTER TABLE region AUTO_INCREMENT = 0;");
    }
    public static void borrarDatosTablaRetroalimentacion () {
        ejecutarInstruccionSQL("DELETE FROM retroalimentacion;");
        ejecutarInstruccionSQL("ALTER TABLE retroalimentacion AUTO_INCREMENT = 1;");
    }
    public static void borrarDatosTablaRetroalimentacionActividad () {
        ejecutarInstruccionSQL("DELETE FROM retroalimentacionActividad;");
    }
    public static void borrarDatosTablaRetroalimentacionColaboracion () {
        ejecutarInstruccionSQL("DELETE FROM retroalimentacionColaboracion;");
    }
    public static void borrarDatosTablaSolicitaParticiparColaboracion () {
        ejecutarInstruccionSQL("DELETE FROM solicitaparticiparcolaboracion;");
    }
    public static void borrarDatosTablaPais () {
        ejecutarInstruccionSQL("DELETE FROM pais;");
        ejecutarInstruccionSQL("ALTER TABLE pais AUTO_INCREMENT = 0;");
    }

    public static void borrarDatosTablaCalendarioActividades () {
        ejecutarInstruccionSQL("DELETE FROM calendarioactividades;");
    }

    public static void ejecutarInstruccionSQL (String instruccionSQL) {
        try {
            String urlBaseDatos = "jdbc:mariadb://localhost:3307/COIL";
            String usuario = "CarrionMartinezPale";
            String contrasena = "cremaxx";

            Connection conexion = DriverManager.getConnection(urlBaseDatos, usuario, contrasena);

            PreparedStatement declaracionSQL = conexion.prepareStatement(instruccionSQL);

            declaracionSQL.execute();
            declaracionSQL.close();
            conexion.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
        }
    }

}
