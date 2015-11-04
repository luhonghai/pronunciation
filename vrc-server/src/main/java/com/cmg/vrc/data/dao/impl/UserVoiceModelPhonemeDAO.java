package com.cmg.vrc.data.dao.impl;

import com.cmg.lesson.data.jdo.history.PhonemeLessonScore;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Phoneme;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.Score;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModelPhonemeDAO extends DataAccess<UserVoiceModel> {

    public UserVoiceModelPhonemeDAO() {
        super(UserVoiceModel.class);
    }
    public List<Phoneme> listAll(int start, int length,String search,int column,String order,String username1,String phoneme1,String country1,int score1, String type, Date dateFrom, Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query = new StringBuffer();
        StringBuffer first = new StringBuffer();
        StringBuffer second = new StringBuffer();

        TypeMetadata metaUserVoiceModel = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserVoiceModel.class.getCanonicalName());
        TypeMetadata metaPhonemeScoreDB = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(PhonemeScoreDB.class.getCanonicalName());
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        TypeMetadata metaPhonemeLessonScore = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(PhonemeLessonScore.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select userVoice.id, userVoice.username , phonemeScore.phonemeWord, phonemeScore.score, userVoice.country, userVoice.serverTime,  from  " + metaUserVoiceModel.getTable()
                        + " userVoice inner join " + metaPhonemeScoreDB.getTable()
                        + " phonemeScore on phonemeScore.userVoiceId=userVoice.id where ";
                query.append(firstQuery);
                query.append(" (userVoice.username LIKE '%" + search + "%' or  phonemeScore.phonemeWord LIKE '%" + search + "%' or phonemeScore.score LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and userVoice.username LIKE '%" + username1 + "%'");
                }
                if (phoneme1.length() > 0) {
                    query.append(" and phonemeScore.phonemeWord LIKE '" + phoneme1 + "'");
                }
                if (country1.length() > 0) {
                    query.append(" and userVoice.country LIKE '" + country1 + "'");
                }
                if(score1==1){
                    query.append(" and 0 <= phonemeScore.score <=50");
                }
                if(score1==2){
                    query.append(" and 51 <= phonemeScore.score <=100");
                }
                if (dateFrom!=null && dateTo==null) {
                    query.append(" and userVoice.serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==null && dateTo!=null) {
                    query.append(" and userVoice.serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=null && dateTo!=null) {
                    query.append(" and userVoice.serverTime >= '" + dateFrom + "' and userVoice.serverTime <= '" + dateTo + "'");
                }

                if (column == 0 && order.equals("asc")) {
                    query.append(" ORDER BY userVoice.username ASC");
                } else if (column == 0 && order.equals("desc")) {
                    query.append(" ORDER BY userVoice.username DESC");
                }
                if (column == 1 && order.equals("asc")) {
                    query.append(" ORDER BY phonemeScore.phonemeWord ASC");
                } else if (column == 1 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeScore.phonemeWord DESC");
                }
                if (column == 2 && order.equals("asc")) {
                    query.append(" ORDER BY phonemeScore.score ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeScore.score DESC");
                }
                if (column == 3 && order.equals("asc")) {
                    query.append(" ORDER BY userVoice.country ASC");
                } else if (column == 3 && order.equals("desc")) {
                    query.append(" ORDER BY userVoice.country DESC");
                }
                if (column == 4 && order.equals("asc")) {
                    query.append(" ORDER BY userVoice.serverTime ASC");
                } else if (column == 4 && order.equals("desc")) {
                    query.append(" ORDER BY userVoice.serverTime DESC");
                }
                query.append(" limit " + start + "," + length);
                Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
                List<Phoneme> list = new ArrayList<Phoneme>();
                try {
                    List<Object> tmp = (List<Object>) q.execute();
                    for (Object obj : tmp) {
                        Phoneme phoneme = new Phoneme();
                        Object[] array = (Object[]) obj;
                        if (array[0].toString().length() > 0) {
                            phoneme.setId(array[0].toString());
                        } else {
                            phoneme.setId(null);
                        }
                        if (array[1] != null) {
                            phoneme.setUsername(array[1].toString());
                        } else {
                            phoneme.setUsername(null);
                        }
                        if (array[2] != null) {
                            phoneme.setPhoneme(array[2].toString());
                        } else {
                            phoneme.setPhoneme(null);
                        }
                        if (array[3] != null) {
                            phoneme.setScore((int) array[3]);
                        } else {
                            phoneme.setScore(0);
                        }
                        if (array[4] != null) {
                            phoneme.setCountry(array[4].toString());
                        } else {
                            phoneme.setCountry(null);
                        }
                        if (array[5] != null) {
                            phoneme.setServerTime((long) array[5]);
                        } else {
                            phoneme.setServerTime(0);
                        }
                        phoneme.setType("F");
                        list.add(phoneme);
                    }

                    return list;
                } catch (Exception e) {
                    throw e;
                } finally {

                    q.closeAll();
                    pm.close();
                }

            }else{
                String firstQuery = "select userLesson.id, userLesson.username , phonemeLessonScore.phoneme, phonemeLessonScore.score, userLesson.country, userLesson.serverTime, userLesson.type  from  " + metaUserLessonHistory.getTable() +
                        " userLesson inner join " + metaPhonemeLessonScore.getTable()
                        + " phonemeLessonScore on phonemeLessonScore.idUserLessonHistory=userLesson.id where ";
                query.append(firstQuery);
                query.append(" ( userLesson.username LIKE '%" + search + "%' or phonemeLessonScore.phoneme LIKE '%" + search + "%' or phonemeLessonScore.score LIKE '%" + search + "%' or userLesson.country LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and  userLesson.username LIKE '%" + username1 + "%'");
                }
                query.append(" and type LIKE '" + type + "'");
                if (phoneme1.length() > 0) {
                    query.append(" and phonemeLessonScore.phoneme LIKE '" + phoneme1 + "'");
                }
                if (country1.length() > 0) {
                    query.append(" and userLesson.country LIKE '" + country1 + "'");
                }
                if(score1==1){
                    query.append(" and 0 <= phonemeLessonScore.score <=50");
                }
                if(score1==2){
                    query.append(" and 51 <= phonemeLessonScore.score <=100");
                }
                if (dateFrom!=null && dateTo==null) {
                    query.append(" and userLesson.serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==null && dateTo!=null) {
                    query.append(" and userLesson.serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=null && dateTo!=null) {
                    query.append(" and userLesson.serverTime >= '" + dateFrom + "' and userLesson.serverTime <= '" + dateTo + "'");
                }

                if (column == 0 && order.equals("asc")) {
                    query.append(" ORDER BY  userLesson.username ASC");
                } else if (column == 0 && order.equals("desc")) {
                    query.append(" ORDER BY  userLesson.username DESC");
                }
                if (column == 1 && order.equals("asc")) {
                    query.append(" ORDER BY phonemeLessonScore.phoneme ASC");
                } else if (column == 1 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeLessonScore.phoneme DESC");
                }
                if (column == 2 && order.equals("asc")) {
                    query.append(" ORDER BY phonemeLessonScore.score ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeLessonScore.score DESC");
                }
                if (column == 3 && order.equals("asc")) {
                    query.append(" ORDER BY userLesson.country ASC");
                } else if (column == 3 && order.equals("desc")) {
                    query.append(" ORDER BY userLesson.country DESC");
                }
                if (column == 4 && order.equals("asc")) {
                    query.append(" ORDER BY userLesson.serverTime ASC");
                } else if (column == 4 && order.equals("desc")) {
                    query.append(" ORDER BY userLesson.serverTime DESC");
                }
                query.append(" limit " + start + "," + length);
                Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
                List<Phoneme> list = new ArrayList<Phoneme>();
                try {
                    List<Object> tmp = (List<Object>) q.execute();
                    for (Object obj : tmp) {
                        Phoneme phoneme = new Phoneme();
                        Object[] array = (Object[]) obj;
                        if (array[0].toString().length() > 0) {
                            phoneme.setId(array[0].toString());
                        } else {
                            phoneme.setId(null);
                        }
                        if (array[1] != null) {
                            phoneme.setUsername(array[1].toString());
                        } else {
                            phoneme.setUsername(null);
                        }
                        if (array[2] != null) {
                            phoneme.setPhoneme(array[2].toString());
                        } else {
                            phoneme.setPhoneme(null);
                        }
                        if (array[3] != null) {
                            phoneme.setScore((int) array[3]);
                        } else {
                            phoneme.setScore(0);
                        }
                        if (array[4] != null) {
                            phoneme.setCountry(array[4].toString());
                        } else {
                            phoneme.setCountry(null);
                        }
                        if (array[5] != null) {
                            phoneme.setServerTime((long) array[5]);
                        } else {
                            phoneme.setServerTime(0);
                        }
                       if(array[6]!=null){
                           phoneme.setType(array[6].toString());
                       }else {
                           phoneme.setType(null);
                       }
                        list.add(phoneme);
                    }

                    return list;
                } catch (Exception e) {
                    throw e;
                } finally {

                    q.closeAll();
                    pm.close();
                }
            }
        }else {
            String firstQuery = "select id, username , word, score, serverTime, 'F' as type, latitude, longitude  from  " + metaUserVoiceModel.getTable() + " where";
            String secondQuery = "select id, username , word, score, serverTime, type, 0 as latitude, 0 as longitude from  " + metaUserLessonHistory.getTable() + " where";
            first.append(firstQuery);
            second.append(secondQuery);
            first.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");
            second.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");
            if (username1.length() > 0) {
                first.append(" and username LIKE '%" + username1 + "%'");
                second.append(" and username LIKE '%" + username1 + "%'");
            }
            if (phoneme1.length() > 0) {
                first.append(" and word LIKE '" + phoneme1 + "'");
                second.append(" and word LIKE '" + phoneme1 + "'");
            }
            if (country1.length() > 0) {
                first.append(" and word LIKE '" + country1 + "'");
                second.append(" and word LIKE '" + phoneme1 + "'");
            }
            if (dateFrom!=null && dateTo==null) {
                first.append(" and serverTime >= '" + dateFrom + "'");
                second.append(" and serverTime >= '" + dateFrom + "'");
            }
            if(score1==1){
                first.append(" and 0 <= score <=50");
                second.append(" and 0 <= score <=50");
            }
            if(score1==2){
                first.append(" and 51 <= score <=100");
                second.append(" and 51 <= score <=100");
            }
            if (dateFrom==null && dateTo!=null) {
                first.append(" and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=null && dateTo!=null) {
                first.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
            }
            query.append("select * from ("+ first + " UNION " + second + ") as tmp ");

            if (column == 0 && order.equals("asc")) {
                query.append(" ORDER BY tmp.username ASC");
            } else if (column == 0 && order.equals("desc")) {
                query.append(" ORDER BY tmp.username DESC");
            }
            if (column == 1 && order.equals("asc")) {
                query.append(" ORDER BY tmp.word ASC");
            } else if (column == 1 && order.equals("desc")) {
                query.append(" ORDER BY tmp.word DESC");
            }
            if (column == 2 && order.equals("asc")) {
                query.append(" ORDER BY tmp.score ASC");
            } else if (column == 2 && order.equals("desc")) {
                query.append(" ORDER BY tmp.score DESC");
            }
            if (column == 3 && order.equals("asc")) {
                query.append(" ORDER BY tmp.serverTime ASC");
            } else if (column == 3 && order.equals("desc")) {
                query.append(" ORDER BY tmp.serverTime DESC");
            }
            if (column == 4 && order.equals("asc")) {
                query.append(" ORDER BY tmp.type ASC");
            } else if (column == 4 && order.equals("desc")) {
                query.append(" ORDER BY tmp.type DESC");
            }
            query.append(" limit " + start + "," + length);
            Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
            List<Phoneme> list = new ArrayList<Phoneme>();
            try {
                List<Object> tmp = (List<Object>) q.execute();
                for (Object obj : tmp) {
                    Phoneme phoneme = new Phoneme();
                    Object[] array = (Object[]) obj;
                    if (array[0].toString().length() > 0) {
                        phoneme.setId(array[0].toString());
                    } else {
                        phoneme.setId(null);
                    }
                    if (array[1] != null) {
                        phoneme.setUsername(array[1].toString());
                    } else {
                        phoneme.setUsername(null);
                    }
                    if (array[2] != null) {
                        phoneme.setPhoneme(array[2].toString());
                    } else {
                        phoneme.setPhoneme(null);
                    }
                    if (array[3] != null) {
                        phoneme.setScore((double) array[3]);
                    } else {
                        phoneme.setScore(0);
                    }
                    if (array[4] != null) {
                        phoneme.setServerTime((long) array[4]);
                    } else {
                        phoneme.setServerTime(0);
                    }
                    if(array[5]!=null){
                        phoneme.setType(array[5].toString());
                    } else {
                        phoneme.setType(null);
                    }

                    list.add(phoneme);
                }

                return list;
            } catch (Exception e) {
                throw e;
            } finally {

                q.closeAll();
                pm.close();
            }
        }
    }



    public List<Score> getCountSearch(String search,int column,String order,String username1,String phoneme1, String country1,int score1, String type, Date dateFrom, Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query = new StringBuffer();
        StringBuffer first = new StringBuffer();
        StringBuffer second = new StringBuffer();

        TypeMetadata metaUserVoiceModel = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserVoiceModel.class.getCanonicalName());
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select id, username , word, score, serverTime, latitude, longitude  from  " + metaUserVoiceModel.getTable() + " where";
                query.append(firstQuery);
                query.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and username LIKE '%" + username1 + "%'");
                }
                if (phoneme1.length() > 0) {
                    query.append(" and word LIKE '" + phoneme1 + "'");
                }
                if (country1.length() > 0) {
                    query.append(" and word LIKE '" + country1 + "'");
                }
                if(score1==1){
                    query.append(" and 0 <= score <=50");
                }
                if(score1==2){
                    query.append(" and 51 <= score <=100");
                }
                if (dateFrom!=null && dateTo==null) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==null && dateTo!=null) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=null && dateTo!=null) {
                    query.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                }

                if (column == 0 && order.equals("asc")) {
                    query.append(" ORDER BY username ASC");
                } else if (column == 0 && order.equals("desc")) {
                    query.append(" ORDER BY username DESC");
                }
                if (column == 1 && order.equals("asc")) {
                    query.append(" ORDER BY word ASC");
                } else if (column == 1 && order.equals("desc")) {
                    query.append(" ORDER BY word DESC");
                }
                if (column == 2 && order.equals("asc")) {
                    query.append(" ORDER BY score ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY score DESC");
                }
                if (column == 3 && order.equals("asc")) {
                    query.append(" ORDER BY serverTime ASC");
                } else if (column == 3 && order.equals("desc")) {
                    query.append(" ORDER BY serverTime DESC");
                }
                Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
                List<Score> list = new ArrayList<Score>();
                try {
                    List<Object> tmp = (List<Object>) q.execute();
                    for (Object obj : tmp) {
                        Score score = new Score();
                        Object[] array = (Object[]) obj;
                        if (array[0].toString().length() > 0) {
                            score.setId(array[0].toString());
                        } else {
                            score.setId(null);
                        }
                        if (array[1] != null) {
                            score.setUsername(array[1].toString());
                        } else {
                            score.setUsername(null);
                        }
                        if (array[2] != null) {
                            score.setWord(array[2].toString());
                        } else {
                            score.setWord(null);
                        }
                        if (array[3] != null) {
                            score.setScore((int) array[3]);
                        } else {
                            score.setScore(0);
                        }
                        if (array[4] != null) {
                            score.setServerTime((long) array[4]);
                        } else {
                            score.setServerTime(0);
                        }
                        if (array[5] != null) {
                            score.setLatitude((double) array[5]);
                        } else {
                            score.setLatitude(0);
                        }
                        if (array[6] != null) {
                            score.setLongitude((double) array[6]);
                        } else {
                            score.setLongitude(0);
                        }
                        score.setType("F");
                        list.add(score);
                    }

                    return list;
                } catch (Exception e) {
                    throw e;
                } finally {

                    q.closeAll();
                    pm.close();
                }

            }else{
                String firstQuery = "select id, username , word, score, serverTime, type  from  " + metaUserLessonHistory.getTable() + " where";
                query.append(firstQuery);
                query.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and username LIKE '%" + username1 + "%'");
                }
                query.append(" and type LIKE '" + type + "'");
                if (phoneme1.length() > 0) {
                    query.append(" and word LIKE '" + phoneme1 + "'");
                }
                if (country1.length() > 0) {
                    query.append(" and word LIKE '" + country1 + "'");
                }
                if(score1==1){
                    query.append(" and 0 <= score <=50");
                }
                if(score1==2){
                    query.append(" and 51 <= score <=100");
                }
                if (dateFrom!=null && dateTo==null) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==null && dateTo!=null) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=null && dateTo!=null) {
                    query.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                }

                if (column == 0 && order.equals("asc")) {
                    query.append(" ORDER BY username ASC");
                } else if (column == 0 && order.equals("desc")) {
                    query.append(" ORDER BY username DESC");
                }
                if (column == 1 && order.equals("asc")) {
                    query.append(" ORDER BY word ASC");
                } else if (column == 1 && order.equals("desc")) {
                    query.append(" ORDER BY word DESC");
                }
                if (column == 2 && order.equals("asc")) {
                    query.append(" ORDER BY score ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY score DESC");
                }
                if (column == 3 && order.equals("asc")) {
                    query.append(" ORDER BY serverTime ASC");
                } else if (column == 3 && order.equals("desc")) {
                    query.append(" ORDER BY serverTime DESC");
                }
                Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
                List<Score> list = new ArrayList<Score>();
                try {
                    List<Object> tmp = (List<Object>) q.execute();
                    for (Object obj : tmp) {
                        Score score = new Score();
                        Object[] array = (Object[]) obj;
                        if (array[0].toString().length() > 0) {
                            score.setId(array[0].toString());
                        } else {
                            score.setId(null);
                        }
                        if (array[1] != null) {
                            score.setUsername(array[1].toString());
                        } else {
                            score.setUsername(null);
                        }
                        if (array[2] != null) {
                            score.setWord(array[2].toString());
                        } else {
                            score.setWord(null);
                        }
                        if (array[3] != null) {
                            score.setScore((int) array[3]);
                        } else {
                            score.setScore(0);
                        }
                        if (array[4] != null) {
                            score.setServerTime((long) array[4]);
                        } else {
                            score.setServerTime(0);
                        }
                        score.setLatitude(0);
                        score.setLongitude(0);
                        if(array[5]!=null){
                            score.setType(array[5].toString());
                        }else {
                            score.setType(null);
                        }
                        list.add(score);
                    }

                    return list;
                } catch (Exception e) {
                    throw e;
                } finally {

                    q.closeAll();
                    pm.close();
                }
            }
        }else {
            String firstQuery = "select id, username , word, score, serverTime, 'F' as type, latitude, longitude  from  " + metaUserVoiceModel.getTable() + " where";
            String secondQuery = "select id, username , word, score, serverTime, type, 0 as latitude, 0 as longitude from  " + metaUserLessonHistory.getTable() + " where";
            first.append(firstQuery);
            second.append(secondQuery);
            first.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");
            second.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");
            if (username1.length() > 0) {
                first.append(" and username LIKE '%" + username1 + "%'");
                second.append(" and username LIKE '%" + username1 + "%'");
            }
            if (phoneme1.length() > 0) {
                first.append(" and word LIKE '" + phoneme1 + "'");
                second.append(" and word LIKE '" + phoneme1 + "'");
            }
            if (country1.length() > 0) {
                first.append(" and word LIKE '" + country1 + "'");
                second.append(" and word LIKE '" + phoneme1 + "'");
            }
            if (dateFrom!=null && dateTo==null) {
                first.append(" and serverTime >= '" + dateFrom + "'");
                second.append(" and serverTime >= '" + dateFrom + "'");
            }
            if(score1==1){
                first.append(" and 0 <= score <=50");
                second.append(" and 0 <= score <=50");
            }
            if(score1==2){
                first.append(" and 51 <= score <=100");
                second.append(" and 51 <= score <=100");
            }
            if (dateFrom==null && dateTo!=null) {
                first.append(" and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=null && dateTo!=null) {
                first.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
            }
            query.append("select * from ("+ first + " UNION " + second + ") as tmp ");

            if (column == 0 && order.equals("asc")) {
                query.append(" ORDER BY tmp.username ASC");
            } else if (column == 0 && order.equals("desc")) {
                query.append(" ORDER BY tmp.username DESC");
            }
            if (column == 1 && order.equals("asc")) {
                query.append(" ORDER BY tmp.word ASC");
            } else if (column == 1 && order.equals("desc")) {
                query.append(" ORDER BY tmp.word DESC");
            }
            if (column == 2 && order.equals("asc")) {
                query.append(" ORDER BY tmp.score ASC");
            } else if (column == 2 && order.equals("desc")) {
                query.append(" ORDER BY tmp.score DESC");
            }
            if (column == 3 && order.equals("asc")) {
                query.append(" ORDER BY tmp.serverTime ASC");
            } else if (column == 3 && order.equals("desc")) {
                query.append(" ORDER BY tmp.serverTime DESC");
            }
            if (column == 4 && order.equals("asc")) {
                query.append(" ORDER BY tmp.type ASC");
            } else if (column == 4 && order.equals("desc")) {
                query.append(" ORDER BY tmp.type DESC");
            }
            Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
            List<Score> list = new ArrayList<Score>();
            try {
                List<Object> tmp = (List<Object>) q.execute();
                for (Object obj : tmp) {
                    Score score = new Score();
                    Object[] array = (Object[]) obj;
                    if (array[0].toString().length() > 0) {
                        score.setId(array[0].toString());
                    } else {
                        score.setId(null);
                    }
                    if (array[1] != null) {
                        score.setUsername(array[1].toString());
                    } else {
                        score.setUsername(null);
                    }
                    if (array[2] != null) {
                        score.setWord(array[2].toString());
                    } else {
                        score.setWord(null);
                    }
                    if (array[3] != null) {
                        score.setScore((double) array[3]);
                    } else {
                        score.setScore(0);
                    }
                    if (array[4] != null) {
                        score.setServerTime((long) array[4]);
                    } else {
                        score.setServerTime(0);
                    }
                    if(array[5]!=null){
                        score.setType(array[5].toString());
                    } else {
                        score.setType(null);
                    }
                    if (array[6] != null) {
                        score.setLatitude((double) array[6]);
                    } else {
                        score.setLatitude(0);
                    }
                    if (array[7] != null) {
                        score.setLongitude((double) array[7]);
                    } else {
                        score.setLongitude(0);
                    }

                    list.add(score);
                }

                return list;
            } catch (Exception e) {
                throw e;
            } finally {

                q.closeAll();
                pm.close();
            }
        }
    }

    /**
     *
     * @param username
     * @return max version for table UserVoiceModel with filter username
     */
    public int getMaxVersion(String username){
        PersistenceManager pm = PersistenceManagerHelper.get();
        int maxVersion = 0;
        Query q = pm.newQuery("SELECT MAX(version) FROM " + UserVoiceModel.class.getCanonicalName());
        q.setFilter("username==paramUsername");
        q.declareParameters("String paramUsername");
        try {
            maxVersion = (int) q.execute(username);
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return maxVersion;
    }

    /**
     *
     * @param username
     * @param version
     * @return list UserVoiceModel filter by username and version
     */
    public List<UserVoiceModel> getByUsernameAndVersion(String username, int version){
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + UserVoiceModel.class.getCanonicalName());
        q.setFilter("username==paramUsername && version > paramVersion");
        q.declareParameters("String paramUsername, int paramVersion");
        try {
            return detachCopyAllList(pm, q.execute(username,version));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
}
