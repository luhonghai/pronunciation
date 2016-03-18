SELECT T.ID, T.NAME, T.DESCRIPTION, T.PERCENTPASS, T.DATECREATED
FROM
  countrymappingcourse AS CMC
    INNER JOIN COURSE AS C ON C.ID = CMC.IDCOURSE
    INNER JOIN COUNTRY AS CT ON CT.ID = CMC.IDCOUNTRY
    INNER JOIN COURSEMAPPINGLEVEL as CML on CMC.IDCOURSE = CML.IDCOURSE
    INNER JOIN LEVEL AS L on CML.IDLEVEL = L.ID
    INNER JOIN COURSEMAPPINGDETAIL as CMD on CMD.IDLEVEL = CML.IDLEVEL
    INNER JOIN test as T on T.ID = CMD.IDCHILD
WHERE CMC.IDCOUNTRY = ? AND CMD.IDLEVEL = ? AND CMD.ISTEST != '\0'