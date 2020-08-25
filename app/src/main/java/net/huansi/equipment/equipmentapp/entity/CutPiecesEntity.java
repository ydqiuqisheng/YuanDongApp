package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CutPiecesEntity {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"6K0085A1","SN":"19NBS656110-061FEC2-00010*1","MATERIALCODE":"#656110","COLORCODE":"G#2","CHECKDATE":"","INFODATE":"2018-08-20","VATNO":"001","PNO":"001","FLAW":"","CLOTH_LEVEL":"A","VATREMARK":"色朱，未抽验","VATREMARK2":"","DIMENSIONALSTABILITY_LENGTH":"-0.900","DIMENSIONALSTABILITY_WIDTH":"0.100","IRONINGSHRINKAGE_LENGTH":"0.200","IRONINGSHRINKAGE_WIDTH":"0.200"}]
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
         * SN : 19NBS656110-061FEC2-00010*1
         * MATERIALCODE : #656110
         * COLORCODE : G#2
         * CHECKDATE :
         * INFODATE : 2018-08-20
         * VATNO : 001
         * PNO : 001
         * FLAW :
         * CLOTH_LEVEL : A
         * VATREMARK : 色朱，未抽验
         * VATREMARK2 :
         * DIMENSIONALSTABILITY_LENGTH : -0.900
         * DIMENSIONALSTABILITY_WIDTH : 0.100
         * IRONINGSHRINKAGE_LENGTH : 0.200
         * IRONINGSHRINKAGE_WIDTH : 0.200
         */

        private String FEPO;
        private String SN;
        private String MATERIALCODE;
        private String COLORCODE;
        private String CHECKDATE;
        private String INFODATE;
        private String VATNO;
        private String PNO;
        private String FLAW;
        private String CLOTH_LEVEL;
        private String VATREMARK;
        private String VATREMARK2;
        private String DIMENSIONALSTABILITY_LENGTH;
        private String DIMENSIONALSTABILITY_WIDTH;
        private String IRONINGSHRINKAGE_LENGTH;
        private String IRONINGSHRINKAGE_WIDTH;
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

        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
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

        public String getCHECKDATE() {
            return CHECKDATE;
        }

        public void setCHECKDATE(String CHECKDATE) {
            this.CHECKDATE = CHECKDATE;
        }

        public String getINFODATE() {
            return INFODATE;
        }

        public void setINFODATE(String INFODATE) {
            this.INFODATE = INFODATE;
        }

        public String getVATNO() {
            return VATNO;
        }

        public void setVATNO(String VATNO) {
            this.VATNO = VATNO;
        }

        public String getPNO() {
            return PNO;
        }

        public void setPNO(String PNO) {
            this.PNO = PNO;
        }

        public String getFLAW() {
            return FLAW;
        }

        public void setFLAW(String FLAW) {
            this.FLAW = FLAW;
        }

        public String getCLOTH_LEVEL() {
            return CLOTH_LEVEL;
        }

        public void setCLOTH_LEVEL(String CLOTH_LEVEL) {
            this.CLOTH_LEVEL = CLOTH_LEVEL;
        }

        public String getVATREMARK() {
            return VATREMARK;
        }

        public void setVATREMARK(String VATREMARK) {
            this.VATREMARK = VATREMARK;
        }

        public String getVATREMARK2() {
            return VATREMARK2;
        }

        public void setVATREMARK2(String VATREMARK2) {
            this.VATREMARK2 = VATREMARK2;
        }

        public String getDIMENSIONALSTABILITY_LENGTH() {
            return DIMENSIONALSTABILITY_LENGTH;
        }

        public void setDIMENSIONALSTABILITY_LENGTH(String DIMENSIONALSTABILITY_LENGTH) {
            this.DIMENSIONALSTABILITY_LENGTH = DIMENSIONALSTABILITY_LENGTH;
        }

        public String getDIMENSIONALSTABILITY_WIDTH() {
            return DIMENSIONALSTABILITY_WIDTH;
        }

        public void setDIMENSIONALSTABILITY_WIDTH(String DIMENSIONALSTABILITY_WIDTH) {
            this.DIMENSIONALSTABILITY_WIDTH = DIMENSIONALSTABILITY_WIDTH;
        }

        public String getIRONINGSHRINKAGE_LENGTH() {
            return IRONINGSHRINKAGE_LENGTH;
        }

        public void setIRONINGSHRINKAGE_LENGTH(String IRONINGSHRINKAGE_LENGTH) {
            this.IRONINGSHRINKAGE_LENGTH = IRONINGSHRINKAGE_LENGTH;
        }

        public String getIRONINGSHRINKAGE_WIDTH() {
            return IRONINGSHRINKAGE_WIDTH;
        }

        public void setIRONINGSHRINKAGE_WIDTH(String IRONINGSHRINKAGE_WIDTH) {
            this.IRONINGSHRINKAGE_WIDTH = IRONINGSHRINKAGE_WIDTH;
        }
    }
}
