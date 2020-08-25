package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

/**
 * Created by zhou.mi on 2018/4/17.
 */

public class MoveMergeInfo {

    /**
     * STATUS : 0
     * DATA : [{"BARCODE":"A1803081070002","SN":"18NBF630144-022FEC10-GRS-00010*1","FEPOCODES":"8KC34U36G03/8KC34U36X04","FEPOQUANTITY":"8KC34U36X04/","MATERIALCODE":"#630144","MATERIALNAME":"单面布","COLORCODE":"10A","COLORNAME":"白","QUANTITYPS":"90.00","PNO":"002"}]
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
         * BARCODE : A1803081070002
         * SN : 18NBF630144-022FEC10-GRS-00010*1
         * FEPOCODES : 8KC34U36G03/8KC34U36X04
         * FEPOQUANTITY : 8KC34U36X04/
         * MATERIALCODE : #630144
         * MATERIALNAME : 单面布
         * COLORCODE : 10A
         * COLORNAME : 白
         * QUANTITYPS : 90.00
         * PNO : 002
         */

        private String BARCODE;
        private String SN;
        private String FEPOCODES;
        private String FEPOQUANTITY;
        private String MATERIALCODE;
        private String MATERIALNAME;
        private String COLORCODE;
        private String COLORNAME;
        private String QUANTITYPS;
        private String PNO;

        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
        }

        public String getFEPOCODES() {
            return FEPOCODES;
        }

        public void setFEPOCODES(String FEPOCODES) {
            this.FEPOCODES = FEPOCODES;
        }

        public String getFEPOQUANTITY() {
            return FEPOQUANTITY;
        }

        public void setFEPOQUANTITY(String FEPOQUANTITY) {
            this.FEPOQUANTITY = FEPOQUANTITY;
        }

        public String getMATERIALCODE() {
            return MATERIALCODE;
        }

        public void setMATERIALCODE(String MATERIALCODE) {
            this.MATERIALCODE = MATERIALCODE;
        }

        public String getMATERIALNAME() {
            return MATERIALNAME;
        }

        public void setMATERIALNAME(String MATERIALNAME) {
            this.MATERIALNAME = MATERIALNAME;
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

        public String getQUANTITYPS() {
            return QUANTITYPS;
        }

        public void setQUANTITYPS(String QUANTITYPS) {
            this.QUANTITYPS = QUANTITYPS;
        }

        public String getPNO() {
            return PNO;
        }

        public void setPNO(String PNO) {
            this.PNO = PNO;
        }
    }
}
