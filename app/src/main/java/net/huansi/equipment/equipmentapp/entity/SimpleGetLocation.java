package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SimpleGetLocation {

    /**
     * STATUS : 0
     * DATA : [{"UUID":"3d4db14d-4546-44ff-a55f-e3d36656908d","COMPANYSTYLE":"b","BILLNO":"c","SEASON":"g","COMB":"d","STYLENO":"e","BOXNO":"","REGISTERQTY":"2","QCLOCATION":""}]
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
         * UUID : 3d4db14d-4546-44ff-a55f-e3d36656908d
         * COMPANYSTYLE : b
         * BILLNO : c
         * SEASON : g
         * COMB : d
         * STYLENO : e
         * BOXNO :
         * REGISTERQTY : 2
         * QCLOCATION :
         */

        private String UUID;
        private String COMPANYSTYLE;
        private String BILLNO;
        private String SEASON;
        private String COMB;
        private String STYLENO;
        private String BOXNO;
        private String REGISTERQTY;
        private String QCLOCATION;

        public String getUUID() {
            return UUID;
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }

        public String getCOMPANYSTYLE() {
            return COMPANYSTYLE;
        }

        public void setCOMPANYSTYLE(String COMPANYSTYLE) {
            this.COMPANYSTYLE = COMPANYSTYLE;
        }

        public String getBILLNO() {
            return BILLNO;
        }

        public void setBILLNO(String BILLNO) {
            this.BILLNO = BILLNO;
        }

        public String getSEASON() {
            return SEASON;
        }

        public void setSEASON(String SEASON) {
            this.SEASON = SEASON;
        }

        public String getCOMB() {
            return COMB;
        }

        public void setCOMB(String COMB) {
            this.COMB = COMB;
        }

        public String getSTYLENO() {
            return STYLENO;
        }

        public void setSTYLENO(String STYLENO) {
            this.STYLENO = STYLENO;
        }

        public String getBOXNO() {
            return BOXNO;
        }

        public void setBOXNO(String BOXNO) {
            this.BOXNO = BOXNO;
        }

        public String getREGISTERQTY() {
            return REGISTERQTY;
        }

        public void setREGISTERQTY(String REGISTERQTY) {
            this.REGISTERQTY = REGISTERQTY;
        }

        public String getQCLOCATION() {
            return QCLOCATION;
        }

        public void setQCLOCATION(String QCLOCATION) {
            this.QCLOCATION = QCLOCATION;
        }
    }
}
