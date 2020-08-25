package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class LogDtEntity {

    /**
     * STATUS : 0
     * DATA : [{"RFIDINID":"3814444","PACKAGENO":"10002","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814453","PACKAGENO":"10003","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814450","PACKAGENO":"10004","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814449","PACKAGENO":"10007","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814452","PACKAGENO":"10009","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814447","PACKAGENO":"10010","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814448","PACKAGENO":"10011","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814442","PACKAGENO":"10013","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814445","PACKAGENO":"10015","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814446","PACKAGENO":"10016","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"9","FINALCHECK":"首款首色"},{"RFIDINID":"3814443","PACKAGENO":"10017","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"M","QUANTITY":"8","FINALCHECK":"首款首色"},{"RFIDINID":"3814451","PACKAGENO":"10035","FEPOCODE":"9KC34A1FX01","COMBNAME":"010","SIZENAME":"L","QUANTITY":"8","FINALCHECK":"首款首色"}]
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
         * RFIDINID : 3814444
         * PACKAGENO : 10002
         * FEPOCODE : 9KC34A1FX01
         * COMBNAME : 010
         * SIZENAME : M
         * QUANTITY : 9
         * FINALCHECK : 首款首色
         */

        private String RFIDINID;
        private String PACKAGENO;
        private String FEPOCODE;
        private String COMBNAME;
        private String SIZENAME;
        private String QUANTITY;
        private String FINALCHECK;

        public String getCUSTOMERPO() {
            return CUSTOMERPO;
        }

        public void setCUSTOMERPO(String CUSTOMERPO) {
            this.CUSTOMERPO = CUSTOMERPO;
        }

        private String CUSTOMERPO;

        public String getRFIDINID() {
            return RFIDINID;
        }

        public void setRFIDINID(String RFIDINID) {
            this.RFIDINID = RFIDINID;
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

        public String getFINALCHECK() {
            return FINALCHECK;
        }

        public void setFINALCHECK(String FINALCHECK) {
            this.FINALCHECK = FINALCHECK;
        }
    }
}
