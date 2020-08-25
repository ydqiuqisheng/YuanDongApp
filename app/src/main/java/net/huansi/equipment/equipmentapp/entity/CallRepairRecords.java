package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class CallRepairRecords {

    /**
     * STATUS : 0
     * DATA : [{"SEWLINE":"1A-1","CALLREPAIREMPLOYEEID":"a56733","EQUNAME":"121353 四线拷克","CALLREPTIME":"2018-09-12 10:56","RESPTIME":"2018-09-12 11:01:22","RESPONSETIMES":"5","REPAIRTIMES":"0"},{"SEWLINE":"1A-1","CALLREPAIREMPLOYEEID":"a56733","EQUNAME":"124202 五线拷克","CALLREPTIME":"2018-09-12 10:55","RESPTIME":"","RESPONSETIMES":"","REPAIRTIMES":""},{"SEWLINE":"1A-1","CALLREPAIREMPLOYEEID":"a56733","EQUNAME":"123025 细横筒三","CALLREPTIME":"2018-09-12 10:54","RESPTIME":"2018-09-12 11:02:07","RESPONSETIMES":"8","REPAIRTIMES":"0"},{"SEWLINE":"1A-1","CALLREPAIREMPLOYEEID":"a56733","EQUNAME":"123025 细横筒三","CALLREPTIME":"2018-09-12 10:54","RESPTIME":"2018-09-12 11:01:42","RESPONSETIMES":"7","REPAIRTIMES":"0"},{"SEWLINE":"1A-1","CALLREPAIREMPLOYEEID":"a56733","EQUNAME":"126101 四针拼缝","CALLREPTIME":"2018-09-12 10:54","RESPTIME":"","RESPONSETIMES":"","REPAIRTIMES":""}]
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
         * SEWLINE : 1A-1
         * CALLREPAIREMPLOYEEID : a56733
         * EQUNAME : 121353 四线拷克
         * CALLREPTIME : 2018-09-12 10:56
         * RESPTIME : 2018-09-12 11:01:22
         * RESPONSETIMES : 5
         * REPAIRTIMES : 0
         */

        private String SEWLINE;
        private String CALLREPAIREMPLOYEEID;
        private String EQUNAME;
        private String CALLREPTIME;
        private String RESPTIME;
        private String RESPONSETIMES;
        private String REPAIRTIMES;
        private String REPAIREDTIME;

        public String getSEWLINE() {
            return SEWLINE;
        }

        public void setSEWLINE(String SEWLINE) {
            this.SEWLINE = SEWLINE;
        }

        public String getCALLREPAIREMPLOYEEID() {
            return CALLREPAIREMPLOYEEID;
        }

        public void setCALLREPAIREMPLOYEEID(String CALLREPAIREMPLOYEEID) {
            this.CALLREPAIREMPLOYEEID = CALLREPAIREMPLOYEEID;
        }

        public String getEQUNAME() {
            return EQUNAME;
        }

        public void setEQUNAME(String EQUNAME) {
            this.EQUNAME = EQUNAME;
        }

        public String getCALLREPTIME() {
            return CALLREPTIME;
        }

        public void setCALLREPTIME(String CALLREPTIME) {
            this.CALLREPTIME = CALLREPTIME;
        }

        public String getRESPTIME() {
            return RESPTIME;
        }

        public void setRESPTIME(String RESPTIME) {
            this.RESPTIME = RESPTIME;
        }

        public String getRESPONSETIMES() {
            return RESPONSETIMES;
        }

        public void setRESPONSETIMES(String RESPONSETIMES) {
            this.RESPONSETIMES = RESPONSETIMES;
        }

        public String getREPAIRTIMES() {
            return REPAIRTIMES;
        }

        public void setREPAIRTIMES(String REPAIRTIMES) {
            this.REPAIRTIMES = REPAIRTIMES;
        }
        public String getREPAIREDTIME() {
            return REPAIREDTIME;
        }

        public void setREPAIREDTIME(String REPAIREDTIME) {
            this.REPAIREDTIME = REPAIREDTIME;
        }
    }
}
