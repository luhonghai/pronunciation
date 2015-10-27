SELECT ROWID as _id,L.id, L.NAME, L.ISDEMO, L.DESCRIPTION, L.DATECREATED, L.COLOR
FROM
  ((countrymappingcourse AS CMC INNER JOIN coursemappinglevel AS CML ON CMC.IDCOURSE = CML.IDCOURSE)
    INNER JOIN level as L on L.ID = CML.IDLEVEL)
WHERE countrymappingcourse.IDCOUNTRY = ?
ORDER BY [index] ASC