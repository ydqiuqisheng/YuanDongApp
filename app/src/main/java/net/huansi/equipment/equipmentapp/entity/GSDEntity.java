package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class GSDEntity {

    /**
     * STATUS : 0
     * DATA : [{"PICURL":"","SEASON":"aa","FEPO":"8KC34CK8","SAMPLETYPE":"cc","MOBANDS":"dd","FUZHUGONGJUDS":"ee","TESHUSHEBEIDS":"ff","SHEBEICANSHUDS":"gg","MOBANZHONGDIANDS":"hh","GONGYIDS":"gg","XIANYANGFENGXIANDS":"ii","OTHERDS":"jj"}]
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
         * PICURL :
         * SEASON : aa
         * FEPO : 8KC34CK8
         * SAMPLETYPE : cc
         * MOBANDS : dd
         * FUZHUGONGJUDS : ee
         * TESHUSHEBEIDS : ff
         * SHEBEICANSHUDS : gg
         * MOBANZHONGDIANDS : hh
         * GONGYIDS : gg
         * XIANYANGFENGXIANDS : ii
         * OTHERDS : jj
         * SHAREDVIDEO:ll
         */

        private String PICURL;
        private String SEASON;
        private String FEPO;
        private String SAMPLETYPE;
        private String MOBANDS;
        private String FUZHUGONGJUDS;
        private String TESHUSHEBEIDS;
        private String SHEBEICANSHUDS;
        private String MOBANZHONGDIANDS;
        private String GONGYIDS;
        private String XIANYANGFENGXIANDS;
        private String OTHERDS;
        private String SHAREDVIDEO;
        public String getSHAREDVIDEO() {
            return SHAREDVIDEO;
        }

        public void setSHAREDVIDEO(String SHAREDVIDEO) {
            this.SHAREDVIDEO = SHAREDVIDEO;
        }
        public String getPICURL() {
            return PICURL;
        }

        public void setPICURL(String PICURL) {
            this.PICURL = PICURL;
        }

        public String getSEASON() {
            return SEASON;
        }

        public void setSEASON(String SEASON) {
            this.SEASON = SEASON;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getSAMPLETYPE() {
            return SAMPLETYPE;
        }

        public void setSAMPLETYPE(String SAMPLETYPE) {
            this.SAMPLETYPE = SAMPLETYPE;
        }

        public String getMOBANDS() {
            return MOBANDS;
        }

        public void setMOBANDS(String MOBANDS) {
            this.MOBANDS = MOBANDS;
        }

        public String getFUZHUGONGJUDS() {
            return FUZHUGONGJUDS;
        }

        public void setFUZHUGONGJUDS(String FUZHUGONGJUDS) {
            this.FUZHUGONGJUDS = FUZHUGONGJUDS;
        }

        public String getTESHUSHEBEIDS() {
            return TESHUSHEBEIDS;
        }

        public void setTESHUSHEBEIDS(String TESHUSHEBEIDS) {
            this.TESHUSHEBEIDS = TESHUSHEBEIDS;
        }

        public String getSHEBEICANSHUDS() {
            return SHEBEICANSHUDS;
        }

        public void setSHEBEICANSHUDS(String SHEBEICANSHUDS) {
            this.SHEBEICANSHUDS = SHEBEICANSHUDS;
        }

        public String getMOBANZHONGDIANDS() {
            return MOBANZHONGDIANDS;
        }

        public void setMOBANZHONGDIANDS(String MOBANZHONGDIANDS) {
            this.MOBANZHONGDIANDS = MOBANZHONGDIANDS;
        }

        public String getGONGYIDS() {
            return GONGYIDS;
        }

        public void setGONGYIDS(String GONGYIDS) {
            this.GONGYIDS = GONGYIDS;
        }

        public String getXIANYANGFENGXIANDS() {
            return XIANYANGFENGXIANDS;
        }

        public void setXIANYANGFENGXIANDS(String XIANYANGFENGXIANDS) {
            this.XIANYANGFENGXIANDS = XIANYANGFENGXIANDS;
        }

        public String getOTHERDS() {
            return OTHERDS;
        }

        public void setOTHERDS(String OTHERDS) {
            this.OTHERDS = OTHERDS;
        }
    }
}
