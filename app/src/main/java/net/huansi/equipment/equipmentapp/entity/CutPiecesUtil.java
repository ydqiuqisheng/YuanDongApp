package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CutPiecesUtil {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"6K0085A1","MATERIALCODE":"H2P083-02A  Single jersey","COLORCODE":"09-090","RECEIVEDATE":"2017-03-07","VATNO":"","RESULT":"待检测","LEVEL":"","REMARK":"","ABNORMALTYPE":""},{"FEPO":"6K0085A1","MATERIALCODE":"H2P083-02A  Single jersey","COLORCODE":"09-090","RECEIVEDATE":"2017-03-20","VATNO":"","RESULT":"待检测","LEVEL":"","REMARK":"","ABNORMALTYPE":""},{"FEPO":"6K0085A1","MATERIALCODE":"H2P083-02A  Single jersey","COLORCODE":"09-090","RECEIVEDATE":"2017-03-21","VATNO":"","RESULT":"待检测","LEVEL":"","REMARK":"","ABNORMALTYPE":""}]
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
         * FEPO : 6K0085A1
         * MATERIALCODE : H2P083-02A  Single jersey
         * COLORCODE : 09-090
         * RECEIVEDATE : 2017-03-07
         * VATNO :
         * RESULT : 待检测
         * LEVEL :
         * REMARK :
         * ABNORMALTYPE :
         */

        private String FEPO;
        private String MATERIALCODE;
        private String COLORCODE;
        private String RECEIVEDATE;
        private String VATNO;
        private String RESULT;
        private String LEVEL;
        private String REMARK;
        private String ABNORMALTYPE;
        private String ACTUALWIDTH;
        private String STANDARDWIDTH;

        public String getACTUALWIDTH() {
            return ACTUALWIDTH;
        }

        public void setACTUALWIDTH(String ACTUALWIDTH) {
            this.ACTUALWIDTH = ACTUALWIDTH;
        }

        public String getSTANDARDWIDTH() {
            return STANDARDWIDTH;
        }

        public void setSTANDARDWIDTH(String STANDARDWIDTH) {
            this.STANDARDWIDTH = STANDARDWIDTH;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getMATERIALCODE() {
            return MATERIALCODE;
        }

        public void setMATERIALCODE(String MATERIALCODE) {
            this.MATERIALCODE = MATERIALCODE;
        }

        public String getCOLORCODE() {
            return COLORCODE;
        }

        public void setCOLORCODE(String COLORCODE) {
            this.COLORCODE = COLORCODE;
        }

        public String getRECEIVEDATE() {
            return RECEIVEDATE;
        }

        public void setRECEIVEDATE(String RECEIVEDATE) {
            this.RECEIVEDATE = RECEIVEDATE;
        }

        public String getVATNO() {
            return VATNO;
        }

        public void setVATNO(String VATNO) {
            this.VATNO = VATNO;
        }

        public String getRESULT() {
            return RESULT;
        }

        public void setRESULT(String RESULT) {
            this.RESULT = RESULT;
        }

        public String getLEVEL() {
            return LEVEL;
        }

        public void setLEVEL(String LEVEL) {
            this.LEVEL = LEVEL;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getABNORMALTYPE() {
            return ABNORMALTYPE;
        }

        public void setABNORMALTYPE(String ABNORMALTYPE) {
            this.ABNORMALTYPE = ABNORMALTYPE;
        }
    }
}
