create definer = root@localhost view vista_academico as
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
from ((((`coil`.`persona` `p` join `coil`.`academico` `a`
         on (`p`.`idPersona` = `a`.`idPersona`)) join `coil`.`facultad` `f`
        on (`f`.`idFacultad` = `a`.`facultad`)) join `coil`.`region` `r`
       on (`r`.`idRegion` = `f`.`region`)) join `coil`.`universidad` `u` on (`u`.`idUniversidad` = `p`.`universidad`));

create definer = root@localhost view vista_estudiante as
select `p`.`idPersona`       AS `idPersona`,
       `p`.`nombre`          AS `nombre`,
       `p`.`apellidoPaterno` AS `apellidoPaterno`,
       `p`.`apellidoMaterno` AS `apellidoMaterno`,
       `p`.`universidad`     AS `universidad`,
       `e`.`idEstudiante`    AS `idEstudiante`,
       `e`.`matricula`       AS `matricula`
from (`coil`.`persona` `p` join `coil`.`estudiante` `e` on (`p`.`idPersona` = `e`.`idPersona`));
