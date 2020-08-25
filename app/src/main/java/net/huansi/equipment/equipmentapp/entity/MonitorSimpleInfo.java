package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class MonitorSimpleInfo {

    /**
     * STATUS : 0
     * DATA : [{"PRODUCEORDERITEMID":"c10d778d-d0c9-4376-9fcd-314432dd3a82","COLOR":"481","SIZE":"M","NODECATEGORY":"Sample","HOWMANYTIMES":"1"}]
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
         * PRODUCEORDERITEMID : c10d778d-d0c9-4376-9fcd-314432dd3a82
         * COLOR : 481
         * SIZE : M
         * NODECATEGORY : Sample
         * HOWMANYTIMES : 1
         */

        private String PRODUCEORDERITEMID;
        private String COLOR;
        private String SIZE;
        private String NODECATEGORY;
        private String HOWMANYTIMES;
        private String DESIGNERADVICE;

        public String getDESIGNERADVICE() {
            return DESIGNERADVICE;
        }

        public void setDESIGNERADVICE(String DESIGNERADVICE) {
            this.DESIGNERADVICE = DESIGNERADVICE;
        }

        public String getPRODUCEORDERITEMID() {
            return PRODUCEORDERITEMID;
        }

        public void setPRODUCEORDERITEMID(String PRODUCEORDERITEMID) {
            this.PRODUCEORDERITEMID = PRODUCEORDERITEMID;
        }

        public String getCOLOR() {
            return COLOR;
        }

        public void setCOLOR(String COLOR) {
            this.COLOR = COLOR;
        }

        public String getSIZE() {
            return SIZE;
        }

        public void setSIZE(String SIZE) {
            this.SIZE = SIZE;
        }

        public String getNODECATEGORY() {
            return NODECATEGORY;
        }

        public void setNODECATEGORY(String NODECATEGORY) {
            this.NODECATEGORY = NODECATEGORY;
        }

        public String getHOWMANYTIMES() {
            return HOWMANYTIMES;
        }

        public void setHOWMANYTIMES(String HOWMANYTIMES) {
            this.HOWMANYTIMES = HOWMANYTIMES;
        }
    }
}
