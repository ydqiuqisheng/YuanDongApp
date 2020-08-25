package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class LogSearchEntity {

    /**
     * STATUS : 0
     * DATA : [{"FRAMECODE":"FB186","POSTNAME":"1C-10","FEPOCODE":"9KC34J36X06","CUSTOMERPO":"3502377862-10","COMBNAME":"010","SIZENAME":"2XL","QUANTITY":"202","FINALCHECK":"PASS"},{"FRAMECODE":"FB186","POSTNAME":"1C-10","FEPOCODE":"9KC34J36X06","CUSTOMERPO":"3502377862-10","COMBNAME":"010","SIZENAME":"L","QUANTITY":"120","FINALCHECK":"PASS"},{"FRAMECODE":"FB186","POSTNAME":"1C-10","FEPOCODE":"9KC34J36X06","CUSTOMERPO":"3502377862-10","COMBNAME":"010","SIZENAME":"XL","QUANTITY":"60","FINALCHECK":"PASS"},{"FRAMECODE":"FB186","POSTNAME":"1C-10","FEPOCODE":"9KC34J36X06","CUSTOMERPO":"选择","COMBNAME":"010","SIZENAME":"2XL","QUANTITY":"158","FINALCHECK":"PASS"},{"FRAMECODE":"FB186","POSTNAME":"1C-10","FEPOCODE":"9KC34J36X06","CUSTOMERPO":"选择","COMBNAME":"010","SIZENAME":"L","QUANTITY":"87","FINALCHECK":"PASS"}]
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
         * FRAMECODE : FB186
         * POSTNAME : 1C-10
         * FEPOCODE : 9KC34J36X06
         * CUSTOMERPO : 3502377862-10
         * COMBNAME : 010
         * SIZENAME : 2XL
         * QUANTITY : 202
         * FINALCHECK : PASS
         */
        private String ITEMID;
        private String FRAMECODE;
        private String POSTNAME;
        private String FEPOCODE;
        private String CUSTOMERPO;
        private String COMBNAME;
        private String SIZENAME;
        private String QUANTITY;
        private String FINALCHECK;

        public String getITEMID() {
            return ITEMID;
        }

        public void setITEMID(String ITEMID) {
            this.ITEMID = ITEMID;
        }

        public String getFRAMECODE() {
            return FRAMECODE;
        }

        public void setFRAMECODE(String FRAMECODE) {
            this.FRAMECODE = FRAMECODE;
        }

        public String getPOSTNAME() {
            return POSTNAME;
        }

        public void setPOSTNAME(String POSTNAME) {
            this.POSTNAME = POSTNAME;
        }

        public String getFEPOCODE() {
            return FEPOCODE;
        }

        public void setFEPOCODE(String FEPOCODE) {
            this.FEPOCODE = FEPOCODE;
        }

        public String getCUSTOMERPO() {
            return CUSTOMERPO;
        }

        public void setCUSTOMERPO(String CUSTOMERPO) {
            this.CUSTOMERPO = CUSTOMERPO;
        }

        public String getCOMBNAME() {
            return COMBNAME;
        }

        public void setCOMBNAME(String COMBNAME) {
            this.COMBNAME = COMBNAME;
        }

        public String getSIZENAME() {
            return SIZENAME;
        }

        public void setSIZENAME(String SIZENAME) {
            this.SIZENAME = SIZENAME;
        }

        public String getQUANTITY() {
            return QUANTITY;
        }

        public void setQUANTITY(String QUANTITY) {
            this.QUANTITY = QUANTITY;
        }

        public String getFINALCHECK() {
            return FINALCHECK;
        }

        public void setFINALCHECK(String FINALCHECK) {
            this.FINALCHECK = FINALCHECK;
        }
    }
}
