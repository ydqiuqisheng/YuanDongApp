package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PoTransformMainInfo {

    /**
     * STATUS : 0
     * DATA : [{"ITEMID":"4ac7f9u0","FACTORY":"8500","PRODUCINGAREA":"TL","SEWLINE":"1B-4","FEPO":"9kc34952","TRANSFORMDAY":"2020-04-10","CREATEUSER":"A10086","CREATEDATE":"2020-04-16"}]
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
         * ITEMID : 4ac7f9u0
         * FACTORY : 8500
         * PRODUCINGAREA : TL
         * SEWLINE : 1B-4
         * FEPO : 9kc34952
         * TRANSFORMDAY : 2020-04-10
         * CREATEUSER : A10086
         * CREATEDATE : 2020-04-16
         */

        private String ITEMID;
        private String FACTORY;
        private String PRODUCINGAREA;
        private String SEWLINE;
        private String FEPO;
        private String TRANSFORMDAY;
        private String CREATEUSER;
        private String CREATEDATE;

        public String getITEMID() {
            return ITEMID;
        }

        public void setITEMID(String ITEMID) {
            this.ITEMID = ITEMID;
        }

        public String getFACTORY() {
            return FACTORY;
        }

        public void setFACTORY(String FACTORY) {
            this.FACTORY = FACTORY;
        }

        public String getPRODUCINGAREA() {
            return PRODUCINGAREA;
        }

        public void setPRODUCINGAREA(String PRODUCINGAREA) {
            this.PRODUCINGAREA = PRODUCINGAREA;
        }

        public String getSEWLINE() {
            return SEWLINE;
        }

        public void setSEWLINE(String SEWLINE) {
            this.SEWLINE = SEWLINE;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getTRANSFORMDAY() {
            return TRANSFORMDAY;
        }

        public void setTRANSFORMDAY(String TRANSFORMDAY) {
            this.TRANSFORMDAY = TRANSFORMDAY;
        }

        public String getCREATEUSER() {
            return CREATEUSER;
        }

        public void setCREATEUSER(String CREATEUSER) {
            this.CREATEUSER = CREATEUSER;
        }

        public String getCREATEDATE() {
            return CREATEDATE;
        }

        public void setCREATEDATE(String CREATEDATE) {
            this.CREATEDATE = CREATEDATE;
        }
    }
}
