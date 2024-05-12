CREATE VIEW colaboraciones_uv_finalizadas AS
SELECT idColaboracion, cedulaProfesional, estado, nombre, nombreUniversidad, areaEstudios,
nombreFacultad, nombreRegion
FROM academicodesarrolla AS ad LEFT JOIN vista_academico AS va
ON ad.idAcademico = va.cedulaProfesional
WHERE va.nombreUniversidad = 'Universidad Veracruzana' 
AND (ad.estado = 'anfitrion' OR ad.estado = 'aceptado')
AND idColaboracion IN (SELECT colaboracion.idColaboracion FROM colaboracion WHERE estado = 'finalizada');


CREATE VIEW estudiantes_uv_colaboracion AS
SELECT idColaboracion, COUNT(idEstudiante) AS alumnosUvTotales 
FROM estudiantescolaboracion
WHERE idEstudiante IN 
(SELECT idEstudiante FROM vista_estudiante WHERE universidad IN 
(SELECT universidad.idUniversidad FROM universidad WHERE nombre = 'Universidad Veracruzana'))
GROUP BY idColaboracion;


CREATE VIEW numeralia AS 
SELECT cuf.idColaboracion, areaEstudios AS areaAcademica, nombreFacultad AS facultad, 
nombreRegion AS region, alumnosUvTotales
FROM 
colaboraciones_uv_finalizadas AS cuf 
LEFT JOIN 
estudiantes_uv_colaboracion AS euc
ON cuf.idColaboracion = euc.idColaboracion;