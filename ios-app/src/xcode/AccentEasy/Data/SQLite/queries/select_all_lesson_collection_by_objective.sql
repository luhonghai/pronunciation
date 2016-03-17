SELECT LC.ROWID as _id, LC.ID, LC.NAME, LC.DESCRIPTION, LC.DATECREATED, LC.TITLE
FROM
  countrymappingcourse AS CMC
    INNER JOIN COURSE AS C ON C.ID = CMC.IDCOURSE
    INNER JOIN COUNTRY AS CT ON CT.ID = CMC.IDCOUNTRY
    INNER JOIN COURSEMAPPINGLEVEL as CML on CMC.IDCOURSE = CML.IDCOURSE
    INNER JOIN LEVEL AS L ON CML.IDLEVEL = L.ID
    INNER JOIN COURSEMAPPINGDETAIL as CMD on CMD.IDLEVEL = CML.IDLEVEL
    INNER JOIN objectivemapping AS OM on OM.IDOBJECTIVE = CMD.IDCHILD
    INNER JOIN OBJECTIVE AS O ON O.ID = OM.IDOBJECTIVE
    INNER JOIN lessoncollection AS LC ON LC.ID = OM.IDLESSONCOLLECTION
WHERE CMC.IDCOUNTRY = ? AND CMD.IDLEVEL = ? AND CMD.IDCHILD = ? AND CMD.ISTEST = '\0'
ORDER BY OM.[INDEX] ASC