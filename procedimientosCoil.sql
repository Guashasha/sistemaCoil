DELIMITER //

DROP PROCEDURE IF EXISTS insertarRetroalimentacionActividad//
CREATE PROCEDURE insertarRetroalimentacionActividad (interaccionPar int, dificultad int, interes int, actividad int, comentario varchar(200), usuario int)
BEGIN
  INSERT INTO retroalimentacion (interaccionPar, comentario, usuario)
  VALUES (interaccionPar, comentario, usuario);

  INSERT INTO retroalimentacionActividad (idRetroalimentacion, dificultad, interes, actividad)
  VALUES (LAST_INSERT_ID(), dificultad, interes, actividad);
END //

DROP PROCEDURE IF EXISTS insertarRetroalimentacionColaboracion//
CREATE PROCEDURE IF NOT EXISTS insertarRetroalimentacionColaboracion (interaccionPar int, comentario varchar(200), habilidadesObtenidas int, calificacion int, intercambioCultural int, mejoraDelLenguaje int, trabajoColaborativo int, mejoraFormacionProfesional int, usuario int, colaboracion int)
BEGIN
  INSERT INTO retroalimentacion (interaccionPar, comentario, usuario)
  VALUES (interaccionPar, comentario, usuario);

  INSERT INTO retroalimentacionColaboracion (idRetroalimentacion, habilidadesObtenidas, calificacion, intercambioCultural, mejoraDelLenguaje, trabajoColaborativo, mejoraFormacionProfesional, colaboracion)
  VALUES (LAST_INSERT_ID(), habilidadesObtenidas, calificacion, intercambioCultural, mejoraDelLenguaje, trabajoColaborativo, mejoraFormacionProfesional, colaboracion);
END //


DROP PROCEDURE IF EXISTS cambiar_Estado_Colaboracion//
create procedure cambiar_Estado_Colaboracion(IN p_idColaboracion int, IN nuevoEstado varchar(20))
BEGIN
   UPDATE colaboracion
   SET estado = nuevoEstado
   WHERE idColaboracion = p_idColaboracion;
END //


DROP PROCEDURE IF EXISTS consultar_academico_cedula//
create procedure consultar_academico_cedula(IN p_cedula varchar(40)) sql security invoker
BEGIN
	SELECT * FROM academico WHERE cedulaProfesional = p_cedula;
END //


DROP PROCEDURE IF EXISTS consultar_academicos_nombreFacultad//
create procedure consultar_academicos_nombreFacultad(IN p_nombreFacultad varchar(50))
BEGIN
	SELECT * FROM vista_academico 
	WHERE nombreFacultad = p_nombreFacultad;
END //


DROP PROCEDURE IF EXISTS obtener_academicos_campos;
create procedure obtener_academicos_campos(IN p_campo varchar(50), IN p_valor varchar(50))
BEGIN
	CASE p_campo
        WHEN 'facultad' THEN
            SELECT * FROM vista_academico WHERE nombreFacultad = p_valor;
        WHEN 'cedula' THEN
            SELECT * FROM vista_academico WHERE cedulaProfesional = p_valor;
        WHEN 'universidad' THEN
            SELECT * FROM vista_academico WHERE nombreUniversidad = p_valor;
        WHEN 'area' THEN
            SELECT * FROM vista_academico WHERE areaEstudios = p_valor;
        WHEN 'categoria' THEN
            SELECT * FROM vista_academico WHERE categoriaContratacion = p_valor;
        WHEN 'region' THEN
            SELECT * FROM vista_academico WHERE nombreRegion = p_valor;
        ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Campo no válido';
    END CASE;
END //


-- Procedimientos academicos
DROP PROCEDURE IF EXISTS registrar_Academico;
CREATE PROCEDURE registrar_Academico (
    IN p_nombre VARCHAR(50), 
    IN p_apellidoPaterno VARCHAR(50),
    IN p_apellidoMaterno VARCHAR(50), 
    IN p_universidad INT,
    IN p_cedulaProfesional VARCHAR(30),
    IN p_numeroDePersonal VARCHAR(40),
    IN p_areaEstudios VARCHAR(40),
    IN p_correoElectronico VARCHAR(30),
    IN p_numeroTelefono VARCHAR(12),
    IN p_categoriaContratacion VARCHAR(40), 
    IN p_facultad INT,
    OUT p_id_persona INT
)
BEGIN
    DECLARE id_persona INT;

    INSERT INTO persona (nombre, apellidoPaterno, apellidoMaterno, universidad) 
    VALUES (p_nombre, p_apellidoPaterno, p_apellidoMaterno, p_universidad);

    SET id_persona = LAST_INSERT_ID();
    SET p_id_persona = id_persona;

    INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios, correoElectronico, numeroTelefonico, categoriaContratacion, facultad)
    VALUES (p_cedulaProfesional, p_numeroDePersonal, id_persona, p_areaEstudios, p_correoElectronico, p_numeroTelefono, p_categoriaContratacion, p_facultad);
END;


DROP PROCEDURE IF EXISTS editar_academico;
CREATE PROCEDURE editar_academico (
    IN p_nombre varchar(50), 
    IN p_apellidoPaterno varchar(50),
    IN p_apellidoMaterno varchar(50), 
    IN p_universidad int,
    IN p_cedulaProfesional varchar(30),
    IN p_numeroDePersonal varchar(40),
    IN p_areaEstudios varchar(40),
    IN p_correoElectronico varchar(30),
    IN p_numeroTelefono varchar(12),
    IN p_categoriaContratacion varchar(40), 
    IN p_facultad int
)
BEGIN
    DECLARE id_persona INT;
    
    SELECT idPersona INTO id_persona
    FROM academico 
    WHERE cedulaProfesional = p_cedulaProfesional;
    
    UPDATE persona 
    SET nombre = p_nombre,
        apellidoPaterno = p_apellidoPaterno,
        apellidoMaterno = p_apellidoMaterno,
        universidad = p_universidad
    WHERE idPersona = id_persona;
        
    UPDATE academico 
    SET numeroDePersonal = p_numeroDePersonal,
        areaEstudios = p_areaEstudios,
        correoElectronico = p_correoElectronico,
        numeroTelefonico = p_numeroTelefono,
        categoriaContratacion = p_categoriaContratacion,
        facultad = p_facultad
    WHERE idPersona = id_persona;
END //


-- Procedimientos estudiantes.
DROP PROCEDURE IF EXISTS registrar_Estudiante//
create procedure registrar_Estudiante(
    IN p_nombre varchar(20), 
    IN p_apellidoPaterno varchar(20),
    IN p_apellidoMaterno varchar(20),
    IN p_universidad int,
    IN p_matricula char(10))
BEGIN
	DECLARE id_persona INT;
	INSERT INTO persona (nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (p_nombre, p_apellidoPaterno, p_apellidoMaterno, p_universidad);
	SET id_persona = LAST_INSERT_ID();
	INSERT INTO estudiante (idPersona, matricula) VALUES (id_persona, p_matricula);
	CALL registrar_cuenta (id_Persona, p_matricula, p_matricula, 'estudiante', 'aceptada');
END //


DROP PROCEDURE IF EXISTS editar_estudiante//
create procedure editar_estudiante (
    in p_nombre varchar(50),
    in p_apellidoPaterno varchar(50),
    in p_apellidoMaterno varchar(50),
    in p_matricula char(10),
    in p_universidad int
)
begin
    declare id_persona int;

    select idPersona into id_persona
    from estudiante
    where matricula = p_matricula;

    UPDATE persona 
    SET nombre = p_nombre,
        apellidoPaterno = p_apellidoPaterno,
        apellidoMaterno = p_apellidoMaterno,
        universidad = p_universidad
    WHERE idPersona = id_persona;
end //


-- Procedimientos cuenta
DROP PROCEDURE IF EXISTS registrar_cuenta;
create procedure registrar_cuenta (
    in p_idPersona int,
    in p_nombreUsuario varchar(50),
    in p_contrasena varchar(300),
    in p_tipo enum ('academico', 'estudiante', 'administrador'),
    in p_estado enum ('pendiente', 'aceptada', 'rechazada')
)
begin
    declare v_contrasena_encriptada varchar(64);

    set v_contrasena_encriptada = SHA2(p_contrasena, 256);

    insert into cuenta (idPersona, nombreUsuario, contrasena, tipo, estado)
    values (p_idPersona, p_nombreUsuario, v_contrasena_encriptada, p_tipo, p_estado);

end //


DROP PROCEDURE IF EXISTS cambiar_contrasena;
create procedure cambiar_contrasena (
    in p_idCuenta int,
    in p_nombreUsuario varchar(50),
    in p_contrasenaAntigua varchar(300),
    in p_contrasenaNueva varchar(300)
)
begin
    declare v_contrasena_antigua_encriptada varchar(64);
    declare v_contrasena_nueva_encriptada varchar(64);
    declare v_contrasena_recuperada_encriptada varchar(64);

    set v_contrasena_antigua_encriptada = SHA2(p_contrasenaAntigua, 256);
    
    select contrasena into v_contrasena_recuperada_encriptada
    from cuenta
    where idCuenta = p_idCuenta;

    IF v_contrasena_antigua_encriptada = v_contrasena_recuperada_encriptada then
        set v_contrasena_nueva_encriptada = SHA2(p_contrasenaNueva, 256);
        update cuenta
        set contrasena = v_contrasena_nueva_encriptada
        where idCuenta = p_idCuenta;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Las contrasenas son distintas';
    END IF;

end //


DROP PROCEDURE IF EXISTS verificar_credenciales;
create procedure verificar_credenciales (
    in p_nombreUsuario varchar(50),
    in p_contrasena varchar(300),
    out p_validacion boolean
)
begin 
    declare v_contrasena_encriptada varchar(300);

    set v_contrasena_encriptada = SHA2(p_contrasena, 256);

    select COUNT(*) into p_validacion
    from cuenta
    where BINARY nombreUsuario = p_nombreUsuario and contrasena = v_contrasena_encriptada;
end //


-- Procedimientos colaboracion

DROP PROCEDURE IF EXISTS registrar_Colaboracion;
create procedure registrar_Colaboracion(
    IN p_estado enum ('propuesta', 'aceptada', 'rechazada', 'disponible', 'vinculada', 'activa', 'enRevision', 'finalizada'),
    IN p_tipo enum ('claseEspejo', 'COIL'),
    IN p_temaInteres varchar(80), 
    IN p_idioma varchar(30),
    IN p_objetivo varchar(80), 
    IN p_fechaInicio date,
    IN p_fechaFinal date, 
    IN p_perfilEstudiante varchar(50)
)
BEGIN
	INSERT INTO colaboracion (estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFin, perfilEstudiante)
	VALUES (p_estado, p_tipo, p_temaInteres, p_idioma, p_objetivo, p_fechaInicio, p_fechaFinal, p_perfilEstudiante);
END //


DROP PROCEDURE IF EXISTS obtener_colaboracion_academicos;
create procedure obtener_colaboracion_academicos (
    IN p_idAcademico1 VARCHAR(30),
    IN p_idAcademico2 VARCHAR(30)
)
begin
    declare v_idColaboracion int;

    select idColaboracion into v_idColaboracion
    from academicodesarrolla
    where (idAcademico = p_idAcademico1 or idAcademico = p_idAcademico2)
    group by idColaboracion
    having COUNT(DISTINCT idAcademico) = 2;

    SELECT c.*, va.*
    FROM colaboracion c
    INNER JOIN academicodesarrolla ad ON c.idColaboracion = ad.idColaboracion
    INNER JOIN vista_academico va ON ad.idAcademico = va.cedulaProfesional
    WHERE c.idColaboracion = v_idColaboracion;
end //


DROP PROCEDURE IF EXISTS obtener_estudiantes_colaboracion;
create procedure obtener_estudiantes_colaboracion (
    in p_idColaboracion int
)
begin
    select v_e.*
    from vista_estudiante v_e
    join estudiantesColaboracion e_col ON v_e.idEstudiante = e_col.idEstudiante
    where e_col.idColaboracion = p_idColaboracion;
end //


DROP PROCEDURE IF EXISTS obtener_academicos_colaboracion;
create procedure obtener_academicos_colaboracion (
    in p_idColaboracion int
)
begin
    select v_a.*
    from vista_academico v_a
    join academicodesarrolla a_des ON v_a.cedulaProfesional = a_des.idAcademico
    where a_des.idColaboracion = p_idColaboracion;
end //


DROP PROCEDURE IF EXISTS actualizar_Colaboracion;
CREATE PROCEDURE actualizar_Colaboracion(
    IN p_idColaboracion INT,
    IN p_estado ENUM('propuesta', 'aceptada', 'rechazada', 'disponible', 'vinculada', 'activa', 'enRevision', 'finalizada'),
    IN p_tipo ENUM('claseEspejo', 'COIL'),
    IN p_temaInteres VARCHAR(80),
    IN p_idioma VARCHAR(30),
    IN p_objetivo VARCHAR(80),
    IN p_fechaInicio DATE,
    IN p_fechaFinal DATE,
    IN p_perfilEstudiante VARCHAR(50)
)
BEGIN
    UPDATE colaboracion
    SET estado = p_estado,
        tipo = p_tipo,
        temaInteres = p_temaInteres,
        idioma = p_idioma,
        objetivo = p_objetivo,
        fechaInicio = p_fechaInicio,
        fechaFin = p_fechaFinal,
        perfilEstudiante = p_perfilEstudiante
    WHERE idColaboracion = p_idColaboracion;
END //


-- Procedimientos numeralia
DROP PROCEDURE if EXISTS numeralia_region//
CREATE PROCEDURE numeralia_region (IN inicio DATE, IN fin DATE)
BEGIN
	SELECT region, SUM(alumnosUvTotales) AS alumnos, COUNT(idColaboracion) AS profesores
	FROM numeralia 
	WHERE fechaFin BETWEEN inicio AND fin
	GROUP BY region; 
END //


DROP PROCEDURE if EXISTS numeralia_area_academica//
CREATE PROCEDURE numeralia_area_academica (IN inicio DATE, IN fin DATE)
BEGIN
	SELECT areaAcademica, SUM(alumnosUvTotales) AS alumnos, COUNT(idColaboracion) AS profesores
	FROM numeralia 
	WHERE fechaFin BETWEEN inicio AND fin
	GROUP BY areaAcademica; 
END //

DROP PROCEDURE IF EXISTS registrar_cuenta_administrador//
CREATE PROCEDURE registrar_cuenta_administrador (
    IN p_nombre VARCHAR(20),
    IN p_apellidoPaterno VARCHAR(20),
    IN p_apellidoMaterno VARCHAR(20),
    IN p_universidad INT,
    IN p_nombreUsuario VARCHAR(50),
    IN p_contrasena VARCHAR(300)
)
BEGIN
    DECLARE v_contrasena_encriptada VARCHAR(64);
    DECLARE v_idPersona INT;
    SET v_contrasena_encriptada = SHA2(p_contrasena, 256);

    INSERT INTO persona (nombre, apellidoPaterno, apellidoMaterno, universidad)
    VALUES (p_nombre, p_apellidoPaterno, p_apellidoMaterno, p_universidad);
    
    SET v_idPersona = LAST_INSERT_ID();

    INSERT INTO cuenta (idPersona, nombreUsuario, contrasena, tipo, estado)
    VALUES (v_idPersona, p_nombreUsuario, v_contrasena_encriptada, 'administrador', 'aceptada');
END //

DELIMITER ;



