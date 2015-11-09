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
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.TimeZone;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModelPhonemeDAO extends DataAccess<UserVoiceModel> {

    public UserVoiceModelPhonemeDAO() {
        super(UserVoiceModel.class);
    }
    public List<Phoneme> listAll(int start, int length,String search,int column,String order,String username1,String phoneme1,String country1,int score1, String type, Date dateFrom1, Date dateTo1) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query = new StringBuffer();
        StringBuffer first = new StringBuffer();
        StringBuffer second = new StringBuffer();
        long dateTo=0;
        long dateFrom=0;
        if(dateFrom1!=null) {
            long output = dateFrom1.getTime() / 1000L;
            String str = Long.toString(output);
            dateFrom = Long.parseLong(str) * 1000;
        }
        if(dateTo1!=null) {
            long output1 = dateTo1.getTime() / 1000L;
            String str1 = Long.toString(output1);
            dateTo = Long.parseLong(str1) * 1000;
        }
        TypeMetadata metaUserVoiceModel = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserVoiceModel.class.getCanonicalName());
        TypeMetadata metaPhonemeScoreDB = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(PhonemeScoreDB.class.getCanonicalName());
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        TypeMetadata metaPhonemeLessonScore = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(PhonemeLessonScore.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select userVoice.id, userVoice.username , phonemeScore.phonemeWord, phonemeScore.totalScore, userVoice.country, userVoice.serverTime  from  " + metaUserVoiceModel.getTable()
                        + " userVoice inner join " + metaPhonemeScoreDB.getTable()
                        + " phonemeScore on phonemeScore.userVoiceId=userVoice.id where ";
                query.append(firstQuery);
                query.append(" (userVoice.username LIKE '%" + search + "%' or  phonemeScore.phonemeWord LIKE '%" + search + "%' or phonemeScore.totalScore LIKE '%" + search + "%')");

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
                    query.append(" and (totalScore >=0 and totalScore<=50)");
                }
                if(score1==2){
                    query.append(" and (totalScore >=51 and totalScore<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and userVoice.serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and userVoice.serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                    query.append(" ORDER BY phonemeScore.totalScore ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeScore.totalScore DESC");
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
                            phoneme.setScore((float) array[3]);
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
                String firstQuery = "select userLesson.id, userLesson.username , phonemeLessonScore.phoneme, phonemeLessonScore.totalScore, userLesson.country, userLesson.serverTime, userLesson.type  from  " + metaUserLessonHistory.getTable() +
                        " userLesson inner join " + metaPhonemeLessonScore.getTable()
                        + " phonemeLessonScore on phonemeLessonScore.idUserLessonHistory=userLesson.id where ";
                query.append(firstQuery);
                query.append(" ( userLesson.username LIKE '%" + search + "%' or phonemeLessonScore.phoneme LIKE '%" + search + "%' or phonemeLessonScore.totalScore LIKE '%" + search + "%' or userLesson.country LIKE '%" + search + "%')");

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
                    query.append(" and (totalScore >=0 and totalScore<=50)");
                }
                if(score1==2){
                    query.append(" and (totalScore >=51 and totalScore<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and userLesson.serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and userLesson.serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                    query.append(" ORDER BY phonemeLessonScore.totalScore ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeLessonScore.totalScore DESC");
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
                            phoneme.setScore((float) array[3]);
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
            String firstQuery = "select userVoice.id, userVoice.username , phonemeScore.phonemeWord as phoneme, phonemeScore.totalScore, userVoice.country, userVoice.serverTime,'F' as type  from  " + metaUserVoiceModel.getTable()
                    + " userVoice inner join " + metaPhonemeScoreDB.getTable()
                    + " phonemeScore on phonemeScore.userVoiceId=userVoice.id where ";

            String secondQuery = "select userLesson.id, userLesson.username , phonemeLessonScore.phoneme, phonemeLessonScore.totalScore, userLesson.country, userLesson.serverTime, userLesson.type  from  " + metaUserLessonHistory.getTable() +
                    " userLesson inner join " + metaPhonemeLessonScore.getTable()
                    + " phonemeLessonScore on phonemeLessonScore.idUserLessonHistory=userLesson.id where ";

            first.append(firstQuery);
            second.append(secondQuery);
            first.append(" (userVoice.username LIKE '%" + search + "%' or  phonemeScore.phonemeWord LIKE '%" + search + "%' or phonemeScore.totalScore LIKE '%" + search + "%')");
            second.append(" (userLesson.username LIKE '%" + search + "%' or phonemeLessonScore.phoneme LIKE '%" + search + "%' or phonemeLessonScore.totalScore LIKE '%" + search + "%')");
            if (username1.length() > 0) {
                first.append(" and userVoice.username LIKE '%" + username1 + "%'");
                second.append(" and userLesson.username LIKE '%" + username1 + "%'");
            }
            if (phoneme1.length() > 0) {
                first.append(" and phonemeScore.phonemeWord LIKE '" + phoneme1 + "'");
                second.append(" and phonemeLessonScore.phoneme LIKE '" + phoneme1 + "'");
            }
            if (country1.length() > 0) {
                first.append(" and userVoice.country LIKE '" + country1 + "'");
                second.append(" and userLesson.country LIKE '" + phoneme1 + "'");
            }

            if(score1==1){
                first.append(" and (totalScore >=0 and totalScore<=50)");
                second.append(" and (totalScore >=0 and totalScore<=50)");
            }
            if(score1==2){
                first.append(" and (totalScore >=51 and totalScore<=100)");
                second.append(" and (totalScore >=51 and totalScore<=100)");
            }
            if (dateFrom!=0 && dateTo==0) {
                first.append(" and userVoice.serverTime >= '" + dateFrom + "'");
                second.append(" and userLesson.serverTime >= '" + dateFrom + "'");
            }
            if (dateFrom==0 && dateTo!=0) {
                first.append(" and userVoice.serverTime <= '" + dateTo + "'");
                second.append(" and userLesson.serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=0 && dateTo!=0) {
                first.append(" and userVoice.serverTime >= '" + dateFrom + "' and userVoice.serverTime <= '" + dateTo + "'");
                second.append(" and userLesson.serverTime >= '" + dateFrom + "' and userLesson.serverTime <= '" + dateTo + "'");
            }
            query.append("select * from ("+ first + " UNION " + second + ") as tmp ");

            if (column == 0 && order.equals("asc")) {
                query.append(" ORDER BY tmp.username ASC");
            } else if (column == 0 && order.equals("desc")) {
                query.append(" ORDER BY tmp.username DESC");
            }
            if (column == 2 && order.equals("asc")) {
                query.append(" ORDER BY tmp.totalScore ASC");
            } else if (column == 2 && order.equals("desc")) {
                query.append(" ORDER BY tmp.totalScore DESC");
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
                        phoneme.setScore((float) array[3]);
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



    public List<Phoneme> getCountSearch(String search,int column,String order,String username1,String phoneme1, String country1,int score1, String type, Date dateFrom1, Date dateTo1) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query = new StringBuffer();
        StringBuffer first = new StringBuffer();
        StringBuffer second = new StringBuffer();
        long dateTo=0;
        long dateFrom=0;
        if(dateFrom1!=null) {
            long output = dateFrom1.getTime() / 1000L;
            String str = Long.toString(output);
            dateFrom = Long.parseLong(str) * 1000;
        }
        if(dateTo1!=null) {
            long output1 = dateTo1.getTime() / 1000L;
            String str1 = Long.toString(output1);
            dateTo = Long.parseLong(str1) * 1000;
        }

        TypeMetadata metaUserVoiceModel = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserVoiceModel.class.getCanonicalName());
        TypeMetadata metaPhonemeScoreDB = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(PhonemeScoreDB.class.getCanonicalName());
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        TypeMetadata metaPhonemeLessonScore = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(PhonemeLessonScore.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select userVoice.id, userVoice.username , phonemeScore.phonemeWord, phonemeScore.totalScore, userVoice.country, userVoice.serverTime  from  " + metaUserVoiceModel.getTable()
                        + " userVoice inner join " + metaPhonemeScoreDB.getTable()
                        + " phonemeScore on phonemeScore.userVoiceId=userVoice.id where ";
                query.append(firstQuery);
                query.append(" (userVoice.username LIKE '%" + search + "%' or  phonemeScore.phonemeWord LIKE '%" + search + "%' or phonemeScore.totalScore LIKE '%" + search + "%')");

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
                    query.append(" and (totalScore >=0 and totalScore<=50)");
                }
                if(score1==2){
                    query.append(" and (totalScore >=51 and totalScore<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and userVoice.serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and userVoice.serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                    query.append(" ORDER BY phonemeScore.totalScore ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeScore.totalScore DESC");
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
                            phoneme.setScore((float) array[3]);
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
                String firstQuery = "select userLesson.id, userLesson.username , phonemeLessonScore.phoneme, phonemeLessonScore.totalScore, userLesson.country, userLesson.serverTime, userLesson.type  from  " + metaUserLessonHistory.getTable() +
                        " userLesson inner join " + metaPhonemeLessonScore.getTable()
                        + " phonemeLessonScore on phonemeLessonScore.idUserLessonHistory=userLesson.id where ";
                query.append(firstQuery);
                query.append(" ( userLesson.username LIKE '%" + search + "%' or phonemeLessonScore.phoneme LIKE '%" + search + "%' or phonemeLessonScore.totalScore LIKE '%" + search + "%' or userLesson.country LIKE '%" + search + "%')");

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
                    query.append(" and (totalScore >=0 and totalScore<=50)");
                }
                if(score1==2){
                    query.append(" and (totalScore >=51 and totalScore<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and userLesson.serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and userLesson.serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                    query.append(" ORDER BY phonemeLessonScore.totalScore ASC");
                } else if (column == 2 && order.equals("desc")) {
                    query.append(" ORDER BY phonemeLessonScore.totalScore DESC");
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
                            phoneme.setScore((float) array[3]);
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
            String firstQuery = "select userVoice.id, userVoice.username , phonemeScore.phonemeWord as phoneme, phonemeScore.totalScore, userVoice.country, userVoice.serverTime,'F' as type  from  " + metaUserVoiceModel.getTable()
                    + " userVoice inner join " + metaPhonemeScoreDB.getTable()
                    + " phonemeScore on phonemeScore.userVoiceId=userVoice.id where ";

            String secondQuery = "select userLesson.id, userLesson.username , phonemeLessonScore.phoneme, phonemeLessonScore.totalScore, userLesson.country, userLesson.serverTime, userLesson.type  from  " + metaUserLessonHistory.getTable() +
                    " userLesson inner join " + metaPhonemeLessonScore.getTable()
                    + " phonemeLessonScore on phonemeLessonScore.idUserLessonHistory=userLesson.id where ";

            first.append(firstQuery);
            second.append(secondQuery);
            first.append(" (userVoice.username LIKE '%" + search + "%' or  phonemeScore.phonemeWord LIKE '%" + search + "%' or phonemeScore.totalScore LIKE '%" + search + "%')");
            second.append(" ( userLesson.username LIKE '%" + search + "%' or phonemeLessonScore.phoneme LIKE '%" + search + "%' or phonemeLessonScore.totalScore LIKE '%" + search + "%')");
            if (username1.length() > 0) {
                first.append(" and userVoice.username LIKE '%" + username1 + "%'");
                second.append(" and userLesson.username LIKE '%" + username1 + "%'");
            }
            if (phoneme1.length() > 0) {
                first.append(" and phonemeScore.phonemeWord LIKE '" + phoneme1 + "'");
                second.append(" and phonemeLessonScore.phoneme LIKE '" + phoneme1 + "'");
            }
            if (country1.length() > 0) {
                first.append(" and userVoice.country LIKE '" + country1 + "'");
                second.append(" and userLesson.country LIKE '" + phoneme1 + "'");
            }

            if(score1==1){
                first.append(" and (totalScore >=0 and totalScore<=50)");
                second.append(" and (totalScore >=0 and totalScore<=50)");
            }
            if(score1==2){
                first.append(" and (totalScore >=51 and totalScore<=100)");
                second.append(" and (totalScore >=51 and totalScore<=100)");
            }
            if (dateFrom!=0 && dateTo==0) {
                first.append(" and userVoice.serverTime >= '" + dateFrom + "'");
                second.append(" and userLesson.serverTime >= '" + dateFrom + "'");
            }
            if (dateFrom==0 && dateTo!=0) {
                first.append(" and userVoice.serverTime <= '" + dateTo + "'");
                second.append(" and userLesson.serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=0 && dateTo!=0) {
                first.append(" and userVoice.serverTime >= '" + dateFrom + "' and userVoice.serverTime <= '" + dateTo + "'");
                second.append(" and userLesson.serverTime >= '" + dateFrom + "' and userLesson.serverTime <= '" + dateTo + "'");
            }
            query.append("select * from ("+ first + " UNION " + second + ") as tmp ");

            if (column == 0 && order.equals("asc")) {
                query.append(" ORDER BY tmp.username ASC");
            } else if (column == 0 && order.equals("desc")) {
                query.append(" ORDER BY tmp.username DESC");
            }
            if (column == 2 && order.equals("asc")) {
                query.append(" ORDER BY tmp.totalScore ASC");
            } else if (column == 2 && order.equals("desc")) {
                query.append(" ORDER BY tmp.totalScore DESC");
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
                        phoneme.setScore((float) array[3]);
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
