SELECT T.ROWID as _id,T.ID, T.NAME, T.DESCRIPTION, T.DATECREATED, T.PERCENTPASS
FROM
  ((countrymappingcourse AS CMC INNER JOIN coursemappingdetail as CMD on CMC.IDCOURSE = CMD.IDCOURSE)
    INNER JOIN test as T on T.ID = CMD.IDCHILD)
WHERE CMC.IDCOUNTRY = ? AND CMD.IDLEVEL = ? AND CMD.ISTEST != '\0'