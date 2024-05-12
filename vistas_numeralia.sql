CREATE VIEW colaboraciones_uv_finalizadas AS 
SELECT ad.idColaboracion, cedulaProfesional, ad.estado, nombre, nombreUniversidad, areaEstudios,
nombreFacultad, nombreRegion, fechaFin, COUNT(idEstudiante) AS alumnosUvTotales
FROM academicodesarrolla AS ad 
LEFT JOIN vista_academico AS va
ON ad.idAcademico = va.cedulaProfesional
LEFT JOIN colaboracion AS c
ON ad.idColaboracion = c.idColaboracion
LEFT JOIN estudiantescolaboracion AS ec
ON ad.idColaboracion = ec.idColaboracion
WHERE va.nombreUniversidad = 'Universidad Veracruzana' 
AND (ad.estado = 'anfitrion' OR ad.estado = 'aceptado')
AND idEstudiante IN 
(SELECT idEstudiante FROM vista_estudiante WHERE universidad IN (SELECT universidad.idUniversidad FROM universidad WHERE nombre = 'Universidad Veracruzana'))
AND ad.idColaboracion IN (SELECT colaboracion.idColaboracion FROM colaboracion WHERE estado = 'finalizada') 
GROUP BY idColaboracion;


CREATE VIEW numeralia AS 
SELECT ad.idColaboracion, areaEstudios AS areaAcademica,
nombreFacultad AS facultad, nombreRegion AS region, fechaFin, COUNT(idEstudiante) AS alumnosUvTotales
FROM academicodesarrolla AS ad 
LEFT JOIN vista_academico AS va
ON ad.idAcademico = va.cedulaProfesional
LEFT JOIN colaboracion AS c
ON ad.idColaboracion = c.idColaboracion
LEFT JOIN estudiantescolaboracion AS ec
ON ad.idColaboracion = ec.idColaboracion
WHERE va.nombreUniversidad = 'Universidad Veracruzana' 
AND (ad.estado = 'anfitrion' OR ad.estado = 'aceptado')
AND idEstudiante IN 
(SELECT idEstudiante FROM vista_estudiante WHERE universidad IN (SELECT universidad.idUniversidad FROM universidad WHERE nombre = 'Universidad Veracruzana'))
AND ad.idColaboracion IN (SELECT colaboracion.idColaboracion FROM colaboracion WHERE estado = 'finalizada') 
GROUP BY idColaboracion;