package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CutPartsTakeEntity {

    /**
     * STATUS : 0
     * DATA : [{"ITEMID":"d1ce563b-b0f6-41d5-8730-7f9f662cde3e","FEPOCODE":"9KC34S1XB01","BEDNO":"7","COMBNAME":"013","QUANTITY":"1305","LAYER":"中"}]
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
         * ITEMID : d1ce563b-b0f6-41d5-8730-7f9f662cde3e
         * FEPOCODE : 9KC34S1XB01
         * BEDNO : 7
         * COMBNAME : 013
         * QUANTITY : 1305
         * LAYER : 中
         */

        private String ITEMID;
        private String FEPOCODE;
        private String BEDNO;
        private String COMBNAME;
        private String QUANTITY;
        private String LAYER;

        public String getITEMID() {
            return ITEMID;
        }

        public void setITEMID(String ITEMID) {
            this.ITEMID = ITEMID;
        }

        public String getFEPOCODE() {
            return FEPOCODE;
        }

        public void setFEPOCODE(String FEPOCODE) {
            this.FEPOCODE = FEPOCODE;
        }

        public String getBEDNO() {
            return BEDNO;
        }

        public void setBEDNO(String BEDNO) {
            this.BEDNO = BEDNO;
        }

        public String getCOMBNAME() {
            return COMBNAME;
        }

        public void setCOMBNAME(String COMBNAME) {
            this.COMBNAME = COMBNAME;
        }

        public String getQUANTITY() {
            return QUANTITY;
        }

        public void setQUANTITY(String QUANTITY) {
            this.QUANTITY = QUANTITY;
        }

        public String getLAYER() {
            return LAYER;
        }

        public void setLAYER(String LAYER) {
            this.LAYER = LAYER;
        }
    }
}
