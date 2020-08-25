package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class blockUnbindInfo {

    /**
     * STATUS : 0
     * DATA : [{"BARCODE":"190704100516","PACKAGENO":"30835","FEPOCODE":"9KC34K96X01","COMBNAME":"010","SIZENAME":"XXL","QUANTITY":"7"}]
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
         * BARCODE : 190704100516
         * PACKAGENO : 30835
         * FEPOCODE : 9KC34K96X01
         * COMBNAME : 010
         * SIZENAME : XXL
         * QUANTITY : 7
         */

        private String BARCODE;
        private String PACKAGENO;
        private String FEPOCODE;
        private String COMBNAME;
        private String SIZENAME;
        private String QUANTITY;
        private String FRAMECODESTR;
        public String getFRAMECODESTR() {
            return FRAMECODESTR;
        }

        public void setFRAMECODESTR(String FRAMECODESTR) {
            this.FRAMECODESTR = FRAMECODESTR;
        }



        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getPACKAGENO() {
            return PACKAGENO;
        }

        public void setPACKAGENO(String PACKAGENO) {
            this.PACKAGENO = PACKAGENO;
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
    }
}
