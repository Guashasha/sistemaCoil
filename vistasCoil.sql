use COIL;

create definer = root@localhost view if not exists vista_academico as
select `p`.`idPersona`             AS `idPersona`,
       `p`.`nombre`                AS `nombre`,
       `p`.`apellidoPaterno`       AS `apellidoPaterno`,
       `p`.`apellidoMaterno`       AS `apellidoMaterno`,
       `p`.`universidad`           AS `idUniversidad`,
       `u`.`nombre`                AS `nombreUniversidad`,
       `u`.`paisOrigen`            AS `paisOrigen`,
       `a`.`cedulaProfesional`     AS `cedulaProfesional`,
       `a`.`numeroDePersonal`      AS `numeroDePersonal`,
       `a`.`areaEstudios`          AS `areaEstudios`,
       `a`.`correoElectronico`     AS `correoElectronico`,
       `a`.`numeroTelefonico`      AS `numeroTelefonico`,
       `a`.`categoriaContratacion` AS `categoriaContratacion`,
       `a`.`facultad`              AS `idFacultad`,
       `f`.`nombre`                AS `nombreFacultad`,
       `r`.`idRegion`              AS `idRegion`,
       `r`.`nombre`                AS `nombreRegion`
from ((((`persona` `p` join `academico` `a`
         on (`p`.`idPersona` = `a`.`idPersona`)) join `facultad` `f`
        on (`f`.`idFacultad` = `a`.`facultad`)) join `region` `r`
       on (`r`.`idRegion` = `f`.`region`)) join `universidad` `u` on (`u`.`idUniversidad` = `p`.`universidad`));

create definer = root@localhost view if not exists vista_estudiante as
select `p`.`idPersona`       AS `idPersona`,
       `p`.`nombre`          AS `nombre`,
       `p`.`apellidoPaterno` AS `apellidoPaterno`,
       `p`.`apellidoMaterno` AS `apellidoMaterno`,
       `p`.`universidad`     AS `universidad`,
       `e`.`idEstudiante`    AS `idEstudiante`,
       `e`.`matricula`       AS `matricula`
from (`persona` `p` join `estudiante` `e` on (`p`.`idPersona` = `e`.`idPersona`));

CREATE VIEW if not exists universidad_con_pais AS 
SELECT idUniversidad, universidad.nombre AS universidad, idPais, pais.nombre AS pais FROM universidad LEFT JOIN pais ON universidad.paisOrigen = pais.idPais;
CREATE VIEW if not exists facultad_con_region AS
SELECT idFacultad, facultad.nombre AS facultad, idRegion, region.nombre AS region FROM facultad LEFT JOIN region ON facultad.region = region.idRegion;
