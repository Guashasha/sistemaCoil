create
    definer = root@localhost procedure cambiar_Estado_Colaboracion(IN p_idColaboracion int, IN nuevoEstado varchar(20))
BEGIN
   UPDATE colaboracion
   SET estado = nuevoEstado
   WHERE idColaboracion = p_idColaboracion;
END;

create
    definer = root@localhost procedure insertarRetroalimentacionActividad(IN interaccionPar int, IN dificultad int,
                                                                          IN interes int, IN actividad int,
                                                                          IN comentario varchar(200), IN usuario int)
BEGIN
  INSERT INTO retroalimentacion (interaccionPar, comentario, usuario)
  VALUES (interaccionPar, comentario, usuario);

  SELECT max(idRetroalimentacion) INTO @id FROM retroalimentacion;

  INSERT INTO retroalimentacionActividad (idRetroalimentacion, dificultad, interes, actividad)
  VALUES (id, dificultad, interes, actividad);
END;

create
    definer = root@localhost procedure obtener_academicos_campos(IN p_campo varchar(50), IN p_valor varchar(50))
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
END;

create
    definer = root@localhost procedure registrar_Academico(IN p_nombre varchar(50), IN p_apellidoPaterno varchar(50),
                                                           IN p_apellidoMaterno varchar(50), IN p_universidad int,
                                                           IN p_cedulaProfesional varchar(30),
                                                           IN p_numeroDePersonal varchar(40),
                                                           IN p_areaEstudios varchar(40),
                                                           IN p_correoElectronico varchar(30),
                                                           IN p_numeroTelefono varchar(12),
                                                           IN p_categoriaContratacion varchar(40), IN p_facultad int)
    sql security invoker
BEGIN
	DECLARE id_persona INT;
	INSERT INTO persona (nombre, apellidoPaterno, apellidoMaterno, universidad) 
	VALUES (p_nombre, p_apellidoPaterno, p_apellidoMaterno, p_universidad);
	SET id_persona = LAST_INSERT_ID();
	INSERT INTO academico (cedulaProfesional, numeroDePersonal, idPersona, areaEstudios,correoElectronico, numeroTelefonico, categoriaContratacion, facultad)
	VALUES (p_cedulaProfesional, p_numeroDePersonal, id_persona, p_areaEstudios,p_correoElectronico, p_numeroTelefono, p_categoriaContratacion, p_facultad);	
END;

create
    definer = root@localhost procedure registrar_Colaboracion(IN p_estado enum ('propuesta', 'aceptada', 'rechazada', 'disponible', 'vinculada', 'activa', 'enRevision', 'finalizada'),
                                                              IN p_tipo enum ('claseEspejo', 'COIL'),
                                                              IN p_temaInteres varchar(80), IN p_idioma varchar(30),
                                                              IN p_objetivo varchar(80), IN p_fechaInicio date,
                                                              IN p_fechaFinal date, IN p_perfilEstudiante varchar(50))
BEGIN
	INSERT INTO colaboracion (estado, tipo, temaInteres, idioma, objetivo, fechaInicio, fechaFinal, perfilEstudiante)
	VALUES (p_estado, p_tipo, p_temaInteres, p_idioma, p_objetivo, p_fechaInicio, p_fechaFinal, p_perfilEstudiante);
END;

create
    definer = root@localhost procedure registrar_Estudiante(IN p_nombre varchar(20), IN p_apellidoPaterno varchar(20),
                                                            IN p_apellidoMaterno varchar(20), IN p_universidad int,
                                                            IN p_matricula char(10)) sql security invoker
BEGIN
	DECLARE id_persona INT;
	INSERT INTO persona (nombre, apellidoPaterno, apellidoMaterno, universidad) VALUES (p_nombre, p_apellidoPaterno, p_apellidoMaterno, p_universidad);
	SET id_persona = LAST_INSERT_ID();
	INSERT INTO estudiante (idPersona, matricula) VALUES (id_persona, p_matricula);
END;

