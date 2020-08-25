package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class LogBillGroup {

    /**
     * STATUS : 0
     * DATA : [{"POSTNAME":"1A-1"},{"POSTNAME":"1E-7"}]
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
         * POSTNAME : 1A-1
         */

        private String POSTNAME;

        public String getPOSTNAME() {
            return POSTNAME;
        }

        public void setPOSTNAME(String POSTNAME) {
            this.POSTNAME = POSTNAME;
        }
    }
}
