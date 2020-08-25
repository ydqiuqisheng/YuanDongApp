package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class RequestSimpleInfo {

    /**
     * STATUS : 0
     * DATA : [{"COMBNAME":"010","SNO":"CI7479","SIZENAME":"S"}]
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
         * COMBNAME : 010
         * SNO : CI7479
         * SIZENAME : S
         */

        private String COMBNAME;
        private String SNO;
        private String SIZENAME;
        private String SEASON;

        public String getSEASON() {
            return SEASON;
        }

        public void setSEASON(String SEASON) {
            this.SEASON = SEASON;
        }

        public String getCOMBNAME() {
            return COMBNAME;
        }

        public void setCOMBNAME(String COMBNAME) {
            this.COMBNAME = COMBNAME;
        }

        public String getSNO() {
            return SNO;
        }

        public void setSNO(String SNO) {
            this.SNO = SNO;
        }

        public String getSIZENAME() {
            return SIZENAME;
        }

        public void setSIZENAME(String SIZENAME) {
            this.SIZENAME = SIZENAME;
        }
    }
}
