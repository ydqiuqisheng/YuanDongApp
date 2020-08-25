package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class blockBindInfo {

    /**
     * STATUS : 0
     * DATA : [{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"7"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"13"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"11"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"7"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"13"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"11"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"7"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"13"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"11"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"7"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"13"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"11"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"7"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"12"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"13"},{"SBILLNO":"CT19070316","SBEDNO":"17","SSUBFEPOCODE":"9KC34K96X01","SCOMBNAME":"010","QTY":"11"}]
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
         * SBILLNO : CT19070316
         * SBEDNO : 17
         * SSUBFEPOCODE : 9KC34K96X01
         * SCOMBNAME : 010
         * QTY : 7
         */

        private String SBILLNO;
        private String SBEDNO;
        private String SSUBFEPOCODE;
        private String SCOMBNAME;
        private String QTY;
        private String SPACKAGENO;
        private String SBARCODE;
        private String SMODIFYSIZE;
        private String PACKAGECOUNT;
        private String FRAMECODE;

        public String getFRAMECODE() {
            return FRAMECODE;
        }

        public void setFRAMECODE(String FRAMECODE) {
            this.FRAMECODE = FRAMECODE;
        }

        public String getPACKAGECOUNT() {
            return PACKAGECOUNT;
        }

        public void setPACKAGECOUNT(String PACKAGECOUNT) {
            this.PACKAGECOUNT = PACKAGECOUNT;
        }



        public String getSPACKAGENO() {
            return SPACKAGENO;
        }

        public void setSPACKAGENO(String SPACKAGENO) {
            this.SPACKAGENO = SPACKAGENO;
        }

        public String getSBARCODE() {
            return SBARCODE;
        }

        public void setSBARCODE(String SBARCODE) {
            this.SBARCODE = SBARCODE;
        }

        public String getSMODIFYSIZE() {
            return SMODIFYSIZE;
        }

        public void setSMODIFYSIZE(String SMODIFYSIZE) {
            this.SMODIFYSIZE = SMODIFYSIZE;
        }

        public String getSBILLNO() {
            return SBILLNO;
        }

        public void setSBILLNO(String SBILLNO) {
            this.SBILLNO = SBILLNO;
        }

        public String getSBEDNO() {
            return SBEDNO;
        }

        public void setSBEDNO(String SBEDNO) {
            this.SBEDNO = SBEDNO;
        }

        public String getSSUBFEPOCODE() {
            return SSUBFEPOCODE;
        }

        public void setSSUBFEPOCODE(String SSUBFEPOCODE) {
            this.SSUBFEPOCODE = SSUBFEPOCODE;
        }

        public String getSCOMBNAME() {
            return SCOMBNAME;
        }

        public void setSCOMBNAME(String SCOMBNAME) {
            this.SCOMBNAME = SCOMBNAME;
        }

        public String getQTY() {
            return QTY;
        }

        public void setQTY(String QTY) {
            this.QTY = QTY;
        }
    }
}
