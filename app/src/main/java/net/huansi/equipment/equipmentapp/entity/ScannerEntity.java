package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class ScannerEntity {

    /**
     * STATUS : 0
     * DATA : [{"AREA":"C","LOCATION":"4","SHELF":"3","MATERIALCODE":"#629409","BARCODE":"B1903190990024W","COLORCODE":"00A","COLORNAME":"黑","QUANTITY":"20.00","CREATEDATETIME":"2019/4/19 16:48:48","VATNO":"018"}]
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
         * AREA : C
         * LOCATION : 4
         * SHELF : 3
         * MATERIALCODE : #629409
         * BARCODE : B1903190990024W
         * COLORCODE : 00A
         * COLORNAME : 黑
         * QUANTITY : 20.00
         * CREATEDATETIME : 2019/4/19 16:48:48
         * VATNO : 018
         */

        private String AREA;
        private String LOCATION;
        private String SHELF;
        private String MATERIALCODE;
        private String BARCODE;
        private String COLORCODE;
        private String COLORNAME;
        private String QUANTITY;
        private String CREATEDATETIME;
        private String VATNO;
        private String PNO;

        public String getPNO() {
            return PNO;
        }

        public void setPNO(String PNO) {
            this.PNO = PNO;
        }

        public String getQUANTITYPS() {
            return QUANTITYPS;
        }

        public void setQUANTITYPS(String QUANTITYPS) {
            this.QUANTITYPS = QUANTITYPS;
        }

        private String QUANTITYPS;

        public String getAREA() {
            return AREA;
        }

        public void setAREA(String AREA) {
            this.AREA = AREA;
        }

        public String getLOCATION() {
            return LOCATION;
        }

        public void setLOCATION(String LOCATION) {
            this.LOCATION = LOCATION;
        }

        public String getSHELF() {
            return SHELF;
        }

        public void setSHELF(String SHELF) {
            this.SHELF = SHELF;
        }

        public String getMATERIALCODE() {
            return MATERIALCODE;
        }

        public void setMATERIALCODE(String MATERIALCODE) {
            this.MATERIALCODE = MATERIALCODE;
        }

        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getCOLORCODE() {
            return COLORCODE;
        }

        public void setCOLORCODE(String COLORCODE) {
            this.COLORCODE = COLORCODE;
        }

        public String getCOLORNAME() {
            return COLORNAME;
        }

        public void setCOLORNAME(String COLORNAME) {
            this.COLORNAME = COLORNAME;
        }

        public String getQUANTITY() {
            return QUANTITY;
        }

        public void setQUANTITY(String QUANTITY) {
            this.QUANTITY = QUANTITY;
        }

        public String getCREATEDATETIME() {
            return CREATEDATETIME;
        }

        public void setCREATEDATETIME(String CREATEDATETIME) {
            this.CREATEDATETIME = CREATEDATETIME;
        }

        public String getVATNO() {
            return VATNO;
        }

        public void setVATNO(String VATNO) {
            this.VATNO = VATNO;
        }
    }
}
