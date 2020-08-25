package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CutPartsQueryEntity {

    /**
     * STATUS : 0
     * DATA : [{"BEDNO":"20","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","QUANTITY":"344","STORAGELOCATION":"1A-1","POSTNAME":"","LAYER":"无"}]
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
         * BEDNO : 20
         * FEPOCODE : 9KC34A1FX01
         * COMBNAME : 010
         * QUANTITY : 344
         * STORAGELOCATION : 1A-1
         * POSTNAME :
         * LAYER : 无
         */

        private String BEDNO;
        private String FEPOCODE;
        private String COMBNAME;
        private String QUANTITY;
        private String STORAGELOCATION;
        private String POSTNAME;
        private String LAYER;

        public String getRECEIVEQTY() {
            return RECEIVEQTY;
        }

        public void setRECEIVEQTY(String RECEIVEQTY) {
            this.RECEIVEQTY = RECEIVEQTY;
        }

        private String RECEIVEQTY;

        public String getBEDNO() {
            return BEDNO;
        }

        public void setBEDNO(String BEDNO) {
            this.BEDNO = BEDNO;
        }

        public String getFEPOCODE() {
            return FEPOCODE;
        }

        public void setFEPOCODE(String FEPOCODE) {
            this.FEPOCODE = FEPOCODE;
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

        public String getSTORAGELOCATION() {
            return STORAGELOCATION;
        }

        public void setSTORAGELOCATION(String STORAGELOCATION) {
            this.STORAGELOCATION = STORAGELOCATION;
        }

        public String getPOSTNAME() {
            return POSTNAME;
        }

        public void setPOSTNAME(String POSTNAME) {
            this.POSTNAME = POSTNAME;
        }

        public String getLAYER() {
            return LAYER;
        }

        public void setLAYER(String LAYER) {
            this.LAYER = LAYER;
        }
    }
}
