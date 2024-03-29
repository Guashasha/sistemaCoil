DELIMITER //

CREATE PROCEDURE IF NOT EXISTS insertarRetroalimentacionActividad (interaccionPar int, dificultad int, interes int, actividad int, comentario varchar(200), usuario int)
BEGIN
  INSERT INTO retroalimentacion (interaccionPar, comentario, usuario)
  VALUES (interaccionPar, comentario, usuario);

  SELECT max(idRetroalimentacion) INTO @id FROM retroalimentacion;

  INSERT INTO retroalimentacionActividad (idRetroalimentacion, dificultad, interes, actividad)
  VALUES (id, dificultad, interes, actividad);
END //

DELIMITER ;

