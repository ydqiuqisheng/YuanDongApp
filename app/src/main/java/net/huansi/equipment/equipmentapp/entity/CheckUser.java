package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CheckUser {

    /**
     * STATUS : 0
     * DATA : [{"USERID":"9090319"},{"USERID":"A53450"},{"USERID":"A53878"},{"USERID":"B78780"},{"USERID":"B81201"}]
     */

    private String STATUS;
    private List<DATABean> DATA;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public List<DATABean> getDATA() {
        return DATA;
    }

    public void setDATA(List<DATABean> DATA) {
        this.DATA = DATA;
    }

    public static class DATABean {
        /**
         * USERID : 9090319
         */

        private String USERID;

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }
    }
}
