package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PDFEntity {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"9KC34J39","SAMPLETYPE":"CAF","BUYMSG":"","PDF":"http://10.17.111.23:80//Pic/PDM/2018/47e4b1b7-b879-49cb-8a68-8f868f017e90.pdf"}]
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
         * FEPO : 9KC34J39
         * SAMPLETYPE : CAF
         * BUYMSG :
         * PDF : http://10.17.111.23:80//Pic/PDM/2018/47e4b1b7-b879-49cb-8a68-8f868f017e90.pdf
         */

        private String FEPO;
        private String SAMPLETYPE;
        private String PRODUCEORDERITEMID;
        private String BUYMSG;
        private String USERTYPE;
        private String SUBMITDATE;
        private String RECEIVEDATE;
        private String PDF;
        private String REMARK;
        private String FEPOLIST;
        private String STYLEYEAR;
        private String STYLESEASON;

        public String getSTYLEYEAR() {
            return STYLEYEAR;
        }

        public void setSTYLEYEAR(String STYLEYEAR) {
            this.STYLEYEAR = STYLEYEAR;
        }

        public String getSTYLESEASON() {
            return STYLESEASON;
        }

        public void setSTYLESEASON(String STYLESEASON) {
            this.STYLESEASON = STYLESEASON;
        }

        public String getFEPOLIST() {
            return FEPOLIST;
        }

        public void setFEPOLIST(String FEPOLIST) {
            this.FEPOLIST = FEPOLIST;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getSAMPLETYPE() {
            return SAMPLETYPE;
        }

        public void setSAMPLETYPE(String SAMPLETYPE) {
            this.SAMPLETYPE = SAMPLETYPE;
        }

        public String getPRODUCEORDERITEMID() {
            return PRODUCEORDERITEMID;
        }

        public void setPRODUCEORDERITEMID(String PRODUCEORDERITEMID) {
            this.PRODUCEORDERITEMID = PRODUCEORDERITEMID;
        }

        public String getBUYMSG() {
            return BUYMSG;
        }

        public void setBUYMSG(String BUYMSG) {
            this.BUYMSG = BUYMSG;
        }

        public String getUSERTYPE() {
            return USERTYPE;
        }

        public void setUSERTYPE(String USERTYPE) {
            this.USERTYPE = USERTYPE;
        }

        public String getSUBMITDATE() {
            return SUBMITDATE;
        }

        public void setSUBMITDATE(String SUBMITDATE) {
            this.SUBMITDATE = SUBMITDATE;
        }

        public String getRECEIVEDATE() {
            return RECEIVEDATE;
        }

        public void setRECEIVEDATE(String RECEIVEDATE) {
            this.RECEIVEDATE = RECEIVEDATE;
        }

        public String getPDF() {
            return PDF;
        }

        public void setPDF(String PDF) {
            this.PDF = PDF;
        }
        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }
    }
}
