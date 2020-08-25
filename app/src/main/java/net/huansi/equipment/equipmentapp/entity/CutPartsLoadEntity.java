package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CutPartsLoadEntity {

    /**
     * STATUS : 0
     * DATA : [{"SBILLNO":"CT19050151","SBEDNO":"2","SSUBFEPOCODE":"9KC34S1KV04","SCOMBNAME":"063","QTY":"753"},{"SBILLNO":"CT19050151","SBEDNO":"2","SSUBFEPOCODE":"9KC34S1KZ06","SCOMBNAME":"063","QTY":"157"}]
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
         * SBILLNO : CT19050151
         * SBEDNO : 2
         * SSUBFEPOCODE : 9KC34S1KV04
         * SCOMBNAME : 063
         * QTY : 753
         */

        private String SBILLNO;
        private String SBEDNO;
        private String SSUBFEPOCODE;
        private String SCOMBNAME;
        private String QTY;

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
