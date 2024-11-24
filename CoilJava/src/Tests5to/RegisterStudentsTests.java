package Tests5to;

import DAO.EstudianteAuxiliar;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.ConfiguracionPrueba;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterStudentsTests {
    private final EstudianteAuxiliar auxEstudiante = new EstudianteAuxiliar();

    @BeforeAll
    public static void setupDatabase() {
        borrarTodosDatosTabla();
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO pais (Iso,nombre) VALUES ('MX','México');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO universidad (nombre,paisOrigen) VALUES ('Universidad Veracruzana',1);");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO region (nombre) VALUES ('XALAPA');");
        ConfiguracionPrueba.ejecutarInstruccionSQL("INSERT INTO facultad (nombre, region) VALUES ('Economia', 1);");
    }

    public static void borrarTodosDatosTabla () {
        ConfiguracionPrueba.borrarDatosTablaEstudiantesColaboracion();
        ConfiguracionPrueba.borrarDatosTablaAcademicoDesarrolla();
        ConfiguracionPrueba.borrarDatosTablaColaboracion();
        ConfiguracionPrueba.borrarDatosTablaEstudiante();
        ConfiguracionPrueba.borrarDatosTablaAcademico();
        ConfiguracionPrueba.borrarDatosTablaPersona();
        ConfiguracionPrueba.borrarDatosTablaUniversidad();
        ConfiguracionPrueba.borrarDatosTablaFacultad();
        ConfiguracionPrueba.borrarDatosTablaRegion();
        ConfiguracionPrueba.borrarDatosTablaPais();
    }

    @Test
    public void testCA_01() {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Emmanuel");
        estudiante.setApellidos("Pale Molina");
        estudiante.setMatricula("S22010101");
        estudiante.setIdUniversidad(1);

        int filasAfectadas = auxEstudiante.agregar(estudiante);

        assert(filasAfectadas > 0);
    }

    @Test
    public void testCA_02() {
        EstudianteDTO estudiante = new EstudianteDTO();
        try {
            auxEstudiante.agregar(estudiante);
        }
        catch (ErrorDAO error) {
            assertEquals("La matricula no puede estar vacía", error.getMessage());
        }
    }

    @Test
    public void testCA_03() {
        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setNombre("Emmanuel");
        estudiante.setApellidos("Pale Molina");
        estudiante.setMatricula("S22010101");
        estudiante.setIdUniversidad(1);

        int filasAfectadas = auxEstudiante.agregar(estudiante);

        assert(filasAfectadas > 0);
    }

    @Test
    public void testCA_04() {
        try {
            auxEstudiante.agregar(null);
        }
        catch (ErrorDAO error) {
            assertEquals("Algo salió mal, inténtelo de nuevo más tarde", error.getMessage());
        }
    }
}
