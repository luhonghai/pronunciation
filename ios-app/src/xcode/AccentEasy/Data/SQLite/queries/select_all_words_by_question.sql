SELECT WC.ID, WC.WORD, WC.ARPABET, WC.DEFINITION, WC.MP3PATH, WC.PRONUNCIATION FROM
  wordofquestion AS WOQ INNER JOIN  wordcollection AS WC ON WC.ID = WOQ.IDWORDCOLLECTION
WHERE
  WOQ.IDQUESTION = ?
