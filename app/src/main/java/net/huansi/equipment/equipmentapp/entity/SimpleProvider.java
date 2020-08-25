package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SimpleProvider {

    /**
     * STATUS : 0
     * DATA : [{"USERID":"ADMIN a","QTY":"9"}]
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
         * USERID : ADMIN a
         * QTY : 9
         */

        private String USERID;
        private String QTY;

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getQTY() {
            return QTY;
        }

        public void setQTY(String QTY) {
            this.QTY = QTY;
        }
    }
}
