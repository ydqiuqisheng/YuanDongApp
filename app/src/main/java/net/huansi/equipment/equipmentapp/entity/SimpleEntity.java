package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SimpleEntity {

    /**
     * STATUS : 0
     * DATA : [{"SAMPLETYPE":"a","COMPANYSTYLE":"b","BILLNO":"c","COMB":"d","STYLENO":"e","SEASON":"g","SAMPLELEVEL":"g","BOXNO":"i","REGISTERQTY":"2","CREATEUSERID":"ADMIN","SOURCEID":"","AIMUSERID":"","QTY":"","LOGDATE":""}]
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
         * RESULT:
         * SAMPLETYPE : a
         * COMPANYSTYLE : b
         * BILLNO : c
         * COMB : d
         * STYLENO : e
         * SEASON : g
         * SIZES:
         * SAMPLELEVEL : g
         * BOXNO : i
         * REGISTERQTY : 2
         * CREATEUSERID : ADMIN
         * SOURCEID :
         * AIMUSERID :
         * QTY :
         * LOGDATE :
         */
        private String RESULT;
        private String UUID;
        private String SAMPLETYPE;
        private String COMPANYSTYLE;
        private String BILLNO;
        private String COMB;
        private String STYLENO;
        private String SEASON;
        private String SIZES;
        private String SAMPLELEVEL;
        private String BOXNO;
        private String REGISTERQTY;
        private String CREATEUSERID;
        private String SUSERNO;
        private String SOURCEID;
        private String AIMUSERID;
        private String QTY;
        private String LOGDATE;

        public String getSUSERNO() {
            return SUSERNO;
        }

        public void setSUSERNO(String SUSERNO) {
            this.SUSERNO = SUSERNO;
        }

        public String getRESULT() {
            return RESULT;
        }

        public void setRESULT(String RESULT) {
            this.RESULT = RESULT;
        }

        public String getUUID() {
            return UUID;
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }
        public String getSAMPLETYPE() {
            return SAMPLETYPE;
        }

        public void setSAMPLETYPE(String SAMPLETYPE) {
            this.SAMPLETYPE = SAMPLETYPE;
        }

        public String getCOMPANYSTYLE() {
            return COMPANYSTYLE;
        }

        public void setCOMPANYSTYLE(String COMPANYSTYLE) {
            this.COMPANYSTYLE = COMPANYSTYLE;
        }

        public String getBILLNO() {
            return BILLNO;
        }

        public void setBILLNO(String BILLNO) {
            this.BILLNO = BILLNO;
        }

        public String getCOMB() {
            return COMB;
        }

        public void setCOMB(String COMB) {
            this.COMB = COMB;
        }

        public String getSTYLENO() {
            return STYLENO;
        }

        public void setSTYLENO(String STYLENO) {
            this.STYLENO = STYLENO;
        }

        public String getSEASON() {
            return SEASON;
        }
        public void setSEASON(String SEASON) {
            this.SEASON = SEASON;
        }
        public void setSIZES(String SIZES) {
            this.SIZES = SIZES;
        }
        public String getSIZES() {
            return SIZES;
        }



        public String getSAMPLELEVEL() {
            return SAMPLELEVEL;
        }

        public void setSAMPLELEVEL(String SAMPLELEVEL) {
            this.SAMPLELEVEL = SAMPLELEVEL;
        }

        public String getBOXNO() {
            return BOXNO;
        }

        public void setBOXNO(String BOXNO) {
            this.BOXNO = BOXNO;
        }

        public String getREGISTERQTY() {
            return REGISTERQTY;
        }

        public void setREGISTERQTY(String REGISTERQTY) {
            this.REGISTERQTY = REGISTERQTY;
        }

        public String getCREATEUSERID() {
            return CREATEUSERID;
        }

        public void setCREATEUSERID(String CREATEUSERID) {
            this.CREATEUSERID = CREATEUSERID;
        }

        public String getSOURCEID() {
            return SOURCEID;
        }

        public void setSOURCEID(String SOURCEID) {
            this.SOURCEID = SOURCEID;
        }

        public String getAIMUSERID() {
            return AIMUSERID;
        }

        public void setAIMUSERID(String AIMUSERID) {
            this.AIMUSERID = AIMUSERID;
        }

        public String getQTY() {
            return QTY;
        }

        public void setQTY(String QTY) {
            this.QTY = QTY;
        }

        public String getLOGDATE() {
            return LOGDATE;
        }

        public void setLOGDATE(String LOGDATE) {
            this.LOGDATE = LOGDATE;
        }
    }
}
