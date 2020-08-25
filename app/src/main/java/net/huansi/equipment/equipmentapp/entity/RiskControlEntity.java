package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class RiskControlEntity {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"9KC34S3JS90","SEASON":"HO19","ACCESSORIES":"","STYLE":"","PRODUCTION":"1.勾勾转印标激光印，皱","SIZE":"","WASHCONDITIONS":"","GUESTCOMMENT":"","CONCLUSION":""}]
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
         * FEPO : 9KC34S3JS90
         * SEASON : HO19
         * ACCESSORIES :
         * STYLE :
         * PRODUCTION : 1.勾勾转印标激光印，皱
         * SIZE :
         * WASHCONDITIONS :
         * GUESTCOMMENT :
         * CONCLUSION :
         */

        private String FEPO;
        private String SEASON;
        private String ACCESSORIES;
        private String STYLE;
        private String PRODUCTION;
        private String SIZE;
        private String WASHCONDITIONS;
        private String GUESTCOMMENT;
        private String CONCLUSION;

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getSEASON() {
            return SEASON;
        }

        public void setSEASON(String SEASON) {
            this.SEASON = SEASON;
        }

        public String getACCESSORIES() {
            return ACCESSORIES;
        }

        public void setACCESSORIES(String ACCESSORIES) {
            this.ACCESSORIES = ACCESSORIES;
        }

        public String getSTYLE() {
            return STYLE;
        }

        public void setSTYLE(String STYLE) {
            this.STYLE = STYLE;
        }

        public String getPRODUCTION() {
            return PRODUCTION;
        }

        public void setPRODUCTION(String PRODUCTION) {
            this.PRODUCTION = PRODUCTION;
        }

        public String getSIZE() {
            return SIZE;
        }

        public void setSIZE(String SIZE) {
            this.SIZE = SIZE;
        }

        public String getWASHCONDITIONS() {
            return WASHCONDITIONS;
        }

        public void setWASHCONDITIONS(String WASHCONDITIONS) {
            this.WASHCONDITIONS = WASHCONDITIONS;
        }

        public String getGUESTCOMMENT() {
            return GUESTCOMMENT;
        }

        public void setGUESTCOMMENT(String GUESTCOMMENT) {
            this.GUESTCOMMENT = GUESTCOMMENT;
        }

        public String getCONCLUSION() {
            return CONCLUSION;
        }

        public void setCONCLUSION(String CONCLUSION) {
            this.CONCLUSION = CONCLUSION;
        }
    }
}
