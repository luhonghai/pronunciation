package com.cmg.vrc.data.dao.impl;

import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Score;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModelDAO extends DataAccess<UserVoiceModel> {

    public UserVoiceModelDAO() {
        super(UserVoiceModel.class);
    }
    public List<Score> listAll(int start, int length,String search,int column,String order,String username1,String word1,int score1, String type, Date dateFrom1, Date dateTo1) throws Exception {

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
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select id, username , word, score, serverTime, latitude, longitude  from  " + metaUserVoiceModel.getTable() + " where";
                query.append(firstQuery);
                query.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and username LIKE '%" + username1 + "%'");
                }
                if (word1.length() > 0) {
                    query.append(" and word LIKE '" + word1 + "'");
                }
                if(score1==1){
                    query.append(" and (score >=0 and score<=50)");
                }
                if(score1==2){
                    query.append(" and (score >=51 and score<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                query.append(" limit " + start + "," + length);
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
                if (word1.length() > 0) {
                    query.append(" and word LIKE '" + word1 + "'");
                }
                if(score1==1){
                    query.append(" and (score >=0 and score<=50)");
                }
                if(score1==2){
                    query.append(" and (score >=51 and score<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                query.append(" limit " + start + "," + length);
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
            if (word1.length() > 0) {
                first.append(" and word LIKE '" + word1 + "'");
                second.append(" and word LIKE '" + word1 + "'");
            }

            if(score1==1){
                first.append(" and 0 <= score <=50");
                second.append(" and 0 <= score <=50");
            }
            if(score1==2){
                first.append(" and 51 <= score <=100");
                second.append(" and 51 <= score <=100");
            }
            if (dateFrom!=0 && dateTo==0) {
                first.append(" and serverTime >= '" + dateFrom + "'");
                second.append(" and serverTime >= '" + dateFrom + "'");
            }
            if (dateFrom==0 && dateTo!=0) {
                first.append(" and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=0 && dateTo!=0) {
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

    public List<Score> listAllScore(String search, String username1,String word1,int score1, String type, Date dateFrom1, Date dateTo1) throws Exception {

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
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select id, username , word, score, serverTime, latitude, longitude  from  " + metaUserVoiceModel.getTable() + " where";
                query.append(firstQuery);
                query.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and username LIKE '%" + username1 + "%'");
                }
                if (word1.length() > 0) {
                    query.append(" and word LIKE '" + word1 + "'");
                }
                if(score1==1){
                    query.append(" and (score >=0 and score<=50)");
                }
                if(score1==2){
                    query.append(" and (score >=51 and score<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
                    query.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                }
                query.append(" ORDER BY serverTime DESC");

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
                if (word1.length() > 0) {
                    query.append(" and word LIKE '" + word1 + "'");
                }
                if(score1==1){
                    query.append(" and (score >=0 and score<=50)");
                }
                if(score1==2){
                    query.append(" and (score >=51 and score<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
                    query.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                }
                query.append(" ORDER BY serverTime DESC");
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
            if (word1.length() > 0) {
                first.append(" and word LIKE '" + word1 + "'");
                second.append(" and word LIKE '" + word1 + "'");
            }

            if(score1==1){
                first.append(" and 0 <= score <=50");
                second.append(" and 0 <= score <=50");
            }
            if(score1==2){
                first.append(" and 51 <= score <=100");
                second.append(" and 51 <= score <=100");
            }
            if (dateFrom!=0 && dateTo==0) {
                first.append(" and serverTime >= '" + dateFrom + "'");
                second.append(" and serverTime >= '" + dateFrom + "'");
            }
            if (dateFrom==0 && dateTo!=0) {
                first.append(" and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=0 && dateTo!=0) {
                first.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime >= '" + dateFrom + "' and serverTime <= '" + dateTo + "'");
            }
            query.append("select * from ("+ first + " UNION " + second + ") as tmp ");
            query.append(" ORDER BY tmp.serverTime DESC");
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


    public List<UserVoiceModel> listAllScore() throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + UserVoiceModel.class.getCanonicalName());
        q.setRange(0, 10000);
        q.setOrdering("serverTime asc");
        try {
            return detachCopyAllList(pm, q.execute());
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }



//    public double getCountSearch(String search,String username1,String word1,int score1, String type, Date dateFrom1, Date dateTo1) throws Exception {
//        PersistenceManager pm = PersistenceManagerHelper.get();
//        Long count;
//        Query q = pm.newQuery("SELECT COUNT(id) FROM " + UserVoiceModel.class.getCanonicalName());
//        StringBuffer string=new StringBuffer();
//        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
//                "(word.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
//                "(uuid.toLowerCase().indexOf(search.toLowerCase()) != -1))";
//        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
//                "(word == null || word.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
//                "(uuid == null || uuid.toLowerCase().indexOf(search.toLowerCase()) != -1))";
//
//
//        if(username1.length()>0){
//            string.append("(username.toLowerCase().indexOf(username1.toLowerCase()) != -1) &&");
//        }
//        if(search.length()>0){
//            string.append(a);
//        }
//        if(search.length()==0){
//            string.append(b);
//        }
//        q.setFilter(string.toString());
//        q.declareParameters("String search, String username1,String word1,String uuid1");
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("search", search);
//        params.put("username1", username1);
//
//        try {
//            count = (Long) q.executeWithMap(params);
//            return count.doubleValue();
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            q.closeAll();
//            pm.close();
//        }
//    }

    public List<Score> getCountSearch(String search,int column,String order,String username1,String word1,int score1, String type, Date dateFrom1, Date dateTo1) throws Exception {

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
        TypeMetadata metaUserLessonHistory = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(UserLessonHistory.class.getCanonicalName());
        if(type!=null && type.length()>0) {
            if(type.equalsIgnoreCase("F")) {
                String firstQuery = "select id, username , word, score, serverTime, latitude, longitude  from  " + metaUserVoiceModel.getTable() + " where";
                query.append(firstQuery);
                query.append(" (username LIKE '%" + search + "%' or word LIKE '%" + search + "%' or score LIKE '%" + search + "%')");

                if (username1.length() > 0) {
                    query.append(" and username LIKE '%" + username1 + "%'");
                }
                if (word1.length() > 0) {
                    query.append(" and word LIKE '" + word1 + "'");
                }
                if(score1==1){
                    query.append(" and (score >=0 and score<=50)");
                }
                if(score1==2){
                    query.append(" and (score >=51 and score<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                            score.setScore((double) array[3]);
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
                if (word1.length() > 0) {
                    query.append(" and word LIKE '" + word1 + "'");
                }
                if(score1==1){
                    query.append(" and (score >=0 and score<=50)");
                }
                if(score1==2){
                    query.append(" and (score >=51 and score<=100)");
                }
                if (dateFrom!=0 && dateTo==0) {
                    query.append(" and serverTime >= '" + dateFrom + "'");
                }
                if (dateFrom==0 && dateTo!=0) {
                    query.append(" and serverTime <= '" + dateTo + "'");
                }

                if (dateFrom!=0 && dateTo!=0) {
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
                            score.setScore((double) array[3]);
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
            if (word1.length() > 0) {
                first.append(" and word LIKE '" + word1 + "'");
                second.append(" and word LIKE '" + word1 + "'");
            }

            if(score1==1){
                first.append(" and (score >=0 and score<=50)");
                second.append(" and (score >=0 and score<=50)");
            }
            if(score1==2){
                first.append(" and (score >=51 and score<=100)");
                second.append(" and (score >=51 and score<=100)");
            }
            if (dateFrom!=0 && dateTo==0) {
                first.append(" and serverTime >= '" + dateFrom + "'");
                second.append(" and serverTime >= '" + dateFrom + "'");
            }
            if (dateFrom==0 && dateTo!=0) {
                first.append(" and serverTime <= '" + dateTo + "'");
                second.append(" and serverTime <= '" + dateTo + "'");
            }

            if (dateFrom!=0 && dateTo!=0) {
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
