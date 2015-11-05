SELECT L.ROWID as _id,L.id, L.NAME, L.ISDEMO, L.DESCRIPTION, L.DATECREATED, L.COLOR
FROM
  ((countrymappingcourse AS CMC INNER JOIN coursemappinglevel AS CML ON CMC.IDCOURSE = CML.IDCOURSE)
    INNER JOIN level as L on L.ID = CML.IDLEVEL)
WHERE CMC.IDCOUNTRY = ? and CML.[index] < (
  select CML.[index] from (countrymappingcourse AS CMC INNER JOIN coursemappinglevel AS CML ON CMC.IDCOURSE = CML.IDCOURSE)
    where CMC.IDCOUNTRY = ? and CML.IDLEVEL = ?
)
ORDER BY CML.[index] DESC limit 1;