SELECT Q.ROWID as _id,Q.ID, Q.NAME, Q.DESCRIPTION, Q.TIMECREATED
FROM
  lessonmappingquestion as LSQ INNER JOIN question as Q on Q.ID = LSQ.IDQUESTION
WHERE LSQ.IDLESSON = ?
ORDER BY Q.NAME ASC