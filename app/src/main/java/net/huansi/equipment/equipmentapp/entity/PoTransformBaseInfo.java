package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PoTransformBaseInfo {


    /**
     * STATUS : 0
     * DATA : [{"ITEMID":"001","BILLITEMID":"4ac7f9u0","CODE":"code01","PROJECTNAME":"PE履历表","TARGETKPI":"1.00","ACTUALKPI":"0.80","TARGETCONFIRMDATE":"2020-04-01","ACTUALCONFIRMDATE":"2020-04-10","ADVANCEDAY":"8","RESPONSIBLEMAN":"技术处","REMARK":"忘记了"}]
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
         * ITEMID : 001
         * BILLITEMID : 4ac7f9u0
         * CODE : code01
         * PROJECTNAME : PE履历表
         * TARGETKPI : 1.00
         * ACTUALKPI : 0.80
         * TARGETCONFIRMDATE : 2020-04-01
         * ACTUALCONFIRMDATE : 2020-04-10
         * ADVANCEDAY : 8
         * RESPONSIBLEMAN : 技术处
         * REMARK : 忘记了
         */

        private String ITEMID;
        private String BILLITEMID;
        private String CODE;
        private String PROJECTNAME;
        private String TARGETKPI;
        private String ACTUALKPI;
        private String TARGETCONFIRMDATE;
        private String ACTUALCONFIRMDATE;
        private String ADVANCEDAY;
        private String RESPONSIBLEMAN;
        private String REMARK;

        public String getITEMID() {
            return ITEMID;
        }

        public void setITEMID(String ITEMID) {
            this.ITEMID = ITEMID;
        }

        public String getBILLITEMID() {
            return BILLITEMID;
        }

        public void setBILLITEMID(String BILLITEMID) {
            this.BILLITEMID = BILLITEMID;
        }

        public String getCODE() {
            return CODE;
        }

        public void setCODE(String CODE) {
            this.CODE = CODE;
        }

        public String getPROJECTNAME() {
            return PROJECTNAME;
        }

        public void setPROJECTNAME(String PROJECTNAME) {
            this.PROJECTNAME = PROJECTNAME;
        }

        public String getTARGETKPI() {
            return TARGETKPI;
        }

        public void setTARGETKPI(String TARGETKPI) {
            this.TARGETKPI = TARGETKPI;
        }

        public String getACTUALKPI() {
            return ACTUALKPI;
        }

        public void setACTUALKPI(String ACTUALKPI) {
            this.ACTUALKPI = ACTUALKPI;
        }

        public String getTARGETCONFIRMDATE() {
            return TARGETCONFIRMDATE;
        }

        public void setTARGETCONFIRMDATE(String TARGETCONFIRMDATE) {
            this.TARGETCONFIRMDATE = TARGETCONFIRMDATE;
        }

        public String getACTUALCONFIRMDATE() {
            return ACTUALCONFIRMDATE;
        }

        public void setACTUALCONFIRMDATE(String ACTUALCONFIRMDATE) {
            this.ACTUALCONFIRMDATE = ACTUALCONFIRMDATE;
        }

        public String getADVANCEDAY() {
            return ADVANCEDAY;
        }

        public void setADVANCEDAY(String ADVANCEDAY) {
            this.ADVANCEDAY = ADVANCEDAY;
        }

        public String getRESPONSIBLEMAN() {
            return RESPONSIBLEMAN;
        }

        public void setRESPONSIBLEMAN(String RESPONSIBLEMAN) {
            this.RESPONSIBLEMAN = RESPONSIBLEMAN;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }
    }
}
