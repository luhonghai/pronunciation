package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCodeCompany;
import com.cmg.vrc.data.jdo.LicenseCode;
import com.cmg.vrc.data.jdo.LicenseCodess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by cmg on 5/18/15.
 */
public class LicenseCodeDAO extends DataAccess<LicenseCode> {

    public LicenseCodeDAO() {
        super(LicenseCode.class);
    }

    public List<LicenseCodess> listAllByCompany(int start, int length, String search, int column, String order, String ac, String acti, String dateFrom, String dateTo, String dateFrom2, String dateTo2, String com) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query = new StringBuffer();
        TypeMetadata metaLicenseCode = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCode.class.getCanonicalName());
        TypeMetadata metaLicenseCodeCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCodeCompany.class.getCanonicalName());
        String firstQuery = "select code.id, code.account , code.activatedDate, code.code, code.imei, code.isActivated, code.isDeleted, code.createdDate, mapping.company from  " + metaLicenseCode.getTable()
                + " code inner join " + metaLicenseCodeCompany.getTable()
                + " mapping on mapping.CODE=code.CODE where ";
        query.append(firstQuery);
        query.append(" (code.account LIKE '%" + search + "%' or code.code LIKE '%" + search + "%' or mapping.company LIKE '%" + search + "%' or code.imei LIKE '%" + search + "%')");

        if (ac.length() > 0) {
            query.append(" and code.account LIKE '%" + ac + "%'");
        }
        if (com.length() > 0) {
            query.append(" and mapping.COMPANY LIKE '" + com + "'");
        }
        query.append(" and code.isDeleted=false");
        if (acti.equals("Yes")) {
            query.append(" and code.isActivated=true");
        }
        if (acti.equals("No")) {
            query.append(" and code.isActivated=false");
        }
        if (dateFrom.length() > 0 && dateTo.equalsIgnoreCase("")) {
            query.append(" and code.activatedDate >= '" + dateFrom + "'");
        }
        if (dateFrom.equalsIgnoreCase("") && dateTo.length() > 0) {
            query.append(" and code.activatedDate <= '" + dateTo + "'");
        }

        if (dateFrom.length() > 0 && dateTo.length() > 0) {
            query.append(" and code.activatedDate >= '" + dateFrom + "' and code.activatedDate <= '" + dateTo + "'");
        }

        if (dateFrom2.length() > 0 && dateTo2.equalsIgnoreCase("")) {
            query.append(" and code.createdDate >= '" + dateFrom2 + "'");
        }
        if (dateFrom2.equalsIgnoreCase("") && dateTo2.length() > 0) {
            query.append(" and code.createdDate <= '" + dateTo2 + "'");
        }

        if (dateFrom2.length() > 0 && dateTo2.length() > 0) {
            query.append(" and code.createdDate >= '" + dateFrom2 + "' and code.createdDate <= '" + dateTo2 + "'");
        }

        if (column == 0 && order.equals("asc")) {
            query.append(" ORDER BY code.account ASC");
        } else if (column == 0 && order.equals("desc")) {
            query.append(" ORDER BY code.account DESC");
        }
        if (column == 3 && order.equals("asc")) {
            query.append(" ORDER BY mapping.company ASC");
        } else if (column == 3 && order.equals("desc")) {
            query.append(" ORDER BY mapping.company DESC");
        }
        if (column == 4 && order.equals("asc")) {
            query.append(" ORDER BY code.createdDate ASC");
        } else if (column == 4 && order.equals("desc")) {
            query.append(" ORDER BY code.createdDate DESC");
        }
        if (column == 5 && order.equals("asc")) {
            query.append(" ORDER BY code.activatedDate ASC");
        } else if (column == 5 && order.equals("desc")) {
            query.append(" ORDER BY code.activatedDate DESC");
        }


        query.append(" limit " + start + "," + length);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        // q.setRange(start, start + length);
        List<LicenseCodess> list = new ArrayList<LicenseCodess>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            for (Object obj : tmp) {
                LicenseCodess licenseCodes = new LicenseCodess();
                Object[] array = (Object[]) obj;
                if (array[0].toString().length() > 0) {
                    licenseCodes.setId(array[0].toString());
                } else {
                    licenseCodes.setId(null);
                }
                if (array[1] != null) {
                    licenseCodes.setAccount(array[1].toString());
                } else {
                    licenseCodes.setAccount(null);
                }
                if (array[2] != null) {
                    licenseCodes.setActivatedDate((Date) array[2]);
                } else {
                    licenseCodes.setActivatedDate(null);
                }
                if (array[3] != null) {
                    licenseCodes.setCode(array[3].toString());
                } else {
                    licenseCodes.setCode(null);
                }
                if (array[5] != null) {
                    licenseCodes.setActivated((boolean) array[5]);
                } else {
                    licenseCodes.setActivated(false);
                }
                if (array[4] != null) {
                    licenseCodes.setImei(array[4].toString());
                } else {
                    licenseCodes.setImei(null);
                }
                if (array[6] != null) {
                    licenseCodes.setIsDeleted((boolean) array[6]);
                } else {
                    licenseCodes.setIsDeleted(false);
                }
                if (array[7] != null) {
                    licenseCodes.setCreatedDate((Date) array[7]);
                } else {
                    licenseCodes.setCreatedDate(null);
                }
                if (array[8] != null) {
                    licenseCodes.setCompany(array[8].toString());
                } else {
                    licenseCodes.setCompany(null);
                }


                list.add(licenseCodes);

            }

            return list;
        } catch (Exception e) {
            throw e;
        } finally {

            q.closeAll();
            pm.close();
        }
    }


    public LicenseCode getByCode(String code) throws Exception {
        List<LicenseCode> list = list("WHERE code == :1", code);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public LicenseCode getByEmail(String email) throws Exception {
        List<LicenseCode> list = listByEmail(email);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public List<LicenseCode> listByEmail(String email) throws Exception {
        return list("WHERE account == :1", email);
    }



    public List<LicenseCodess> listAllByCompanySearch(String search, int column, String order, String ac, String acti, String dateFrom, String dateTo, String dateFrom2, String dateTo2, String com) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query = new StringBuffer();
        TypeMetadata metaLicenseCode = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCode.class.getCanonicalName());
        TypeMetadata metaLicenseCodeCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCodeCompany.class.getCanonicalName());
        String firstQuery = "select code.id, code.account , code.activatedDate, code.code, code.imei, code.isActivated, code.isDeleted, code.createdDate, mapping.company from  " + metaLicenseCode.getTable()
                + " code inner join " + metaLicenseCodeCompany.getTable()
                + " mapping on mapping.CODE=code.CODE where ";
        query.append(firstQuery);
        query.append(" (code.account LIKE '%" + search + "%' or code.code LIKE '%" + search + "%' or mapping.company LIKE '%" + search + "%' or code.imei LIKE '%" + search + "%')");

        if (ac.length() > 0) {
            query.append(" and code.account LIKE '%" + ac + "%'");
        }
        if (com.length() > 0) {
            query.append(" and mapping.COMPANY LIKE '" + com + "'");
        }
        query.append(" and code.isDeleted=false");
        if (acti.equals("Yes")) {
            query.append(" and code.isActivated=true");
        }
        if (acti.equals("No")) {
            query.append(" and code.isActivated=false");
        }
        if (dateFrom.length() > 0 && dateTo.equalsIgnoreCase("")) {
            query.append(" and code.activatedDate >= '" + dateFrom + "'");
        }
        if (dateFrom.equalsIgnoreCase("") && dateTo.length() > 0) {
            query.append(" and code.activatedDate <= '" + dateTo + "'");
        }

        if (dateFrom.length() > 0 && dateTo.length() > 0) {
            query.append(" and code.activatedDate >= '" + dateFrom + "' and code.activatedDate <= '" + dateTo + "'");
        }

        if (dateFrom2.length() > 0 && dateTo2.equalsIgnoreCase("")) {
            query.append(" and code.createdDate >= '" + dateFrom2 + "'");
        }
        if (dateFrom2.equalsIgnoreCase("") && dateTo2.length() > 0) {
            query.append(" and code.createdDate <= '" + dateTo2 + "'");
        }

        if (dateFrom2.length() > 0 && dateTo2.length() > 0) {
            query.append(" and code.createdDate >= '" + dateFrom2 + "' and code.createdDate <= '" + dateTo2 + "'");
        }

        if (column == 0 && order.equals("asc")) {
            query.append(" ORDER BY code.account ASC");
        } else if (column == 0 && order.equals("desc")) {
            query.append(" ORDER BY code.account DESC");
        }
        if (column == 3 && order.equals("asc")) {
            query.append(" ORDER BY mapping.company ASC");
        } else if (column == 3 && order.equals("desc")) {
            query.append(" ORDER BY mapping.company DESC");
        }
        if (column == 4 && order.equals("asc")) {
            query.append(" ORDER BY code.createdDate ASC");
        } else if (column == 4 && order.equals("desc")) {
            query.append(" ORDER BY code.createdDate DESC");
        }
        if (column == 5 && order.equals("asc")) {
            query.append(" ORDER BY code.activatedDate ASC");
        } else if (column == 5 && order.equals("desc")) {
            query.append(" ORDER BY code.activatedDate DESC");
        }

        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        // q.setRange(start, start + length);
        List<LicenseCodess> list = new ArrayList<LicenseCodess>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            for (Object obj : tmp) {
                LicenseCodess licenseCodes = new LicenseCodess();
                Object[] array = (Object[]) obj;
                if (array[0].toString().length() > 0) {
                    licenseCodes.setId(array[0].toString());
                } else {
                    licenseCodes.setId(null);
                }
                if (array[1] != null) {
                    licenseCodes.setAccount(array[1].toString());
                } else {
                    licenseCodes.setAccount(null);
                }
                if (array[2] != null) {
                    licenseCodes.setActivatedDate((Date) array[2]);
                } else {
                    licenseCodes.setActivatedDate(null);
                }
                if (array[3] != null) {
                    licenseCodes.setCode(array[3].toString());
                } else {
                    licenseCodes.setCode(null);
                }
                if (array[5] != null) {
                    licenseCodes.setActivated((boolean) array[5]);
                } else {
                    licenseCodes.setActivated(false);
                }
                if (array[4] != null) {
                    licenseCodes.setImei(array[4].toString());
                } else {
                    licenseCodes.setImei(null);
                }
                if (array[6] != null) {
                    licenseCodes.setIsDeleted((boolean) array[6]);
                } else {
                    licenseCodes.setIsDeleted(false);
                }
                if (array[7] != null) {
                    licenseCodes.setCreatedDate((Date) array[7]);
                } else {
                    licenseCodes.setCreatedDate(null);
                }
                if (array[8] != null) {
                    licenseCodes.setCompany(array[8].toString());
                } else {
                    licenseCodes.setCompany(null);
                }


                list.add(licenseCodes);

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
