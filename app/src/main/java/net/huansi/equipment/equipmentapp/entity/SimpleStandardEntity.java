package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SimpleStandardEntity {

    /**
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
         * PDMPRODUCEORDERID : dc9388ac-b1c7-4667-b206-49be95aefc8b
         * SEQUENCE : 1
         * CODE : 100-A
         * SPECIFICATION : 领高
         * 进单参考尺码 : M
         * FRANCHISE : 0.3
         * GUESTSTANDARD : 1.5
         */

        private String PDMPRODUCEORDERID;
        private String SEQUENCE;
        private String CODE;
        private String SPECIFICATION;
        private String 进单参考尺码;
        private String FRANCHISE;
        private String GUESTSTANDARD;
        private String MEASURERESULT;



        public String getPDMPRODUCEORDERID() {
            return PDMPRODUCEORDERID;
        }

        public void setPDMPRODUCEORDERID(String PDMPRODUCEORDERID) {
            this.PDMPRODUCEORDERID = PDMPRODUCEORDERID;
        }

        public String getSEQUENCE() {
            return SEQUENCE;
        }

        public void setSEQUENCE(String SEQUENCE) {
            this.SEQUENCE = SEQUENCE;
        }

        public String getCODE() {
            return CODE;
        }

        public void setCODE(String CODE) {
            this.CODE = CODE;
        }

        public String getSPECIFICATION() {
            return SPECIFICATION;
        }

        public void setSPECIFICATION(String SPECIFICATION) {
            this.SPECIFICATION = SPECIFICATION;
        }

        public String get进单参考尺码() {
            return 进单参考尺码;
        }

        public void set进单参考尺码(String 进单参考尺码) {
            this.进单参考尺码 = 进单参考尺码;
        }

        public String getFRANCHISE() {
            return FRANCHISE;
        }

        public void setFRANCHISE(String FRANCHISE) {
            this.FRANCHISE = FRANCHISE;
        }

        public String getGUESTSTANDARD() {
            return GUESTSTANDARD;
        }

        public void setGUESTSTANDARD(String GUESTSTANDARD) {
            this.GUESTSTANDARD = GUESTSTANDARD;
        }

        public String getMEASURERESULT() {
            return MEASURERESULT;
        }

        public void setMEASURERESULT(String MEASURERESULT) {
            this.MEASURERESULT = MEASURERESULT;
        }
    }
}
