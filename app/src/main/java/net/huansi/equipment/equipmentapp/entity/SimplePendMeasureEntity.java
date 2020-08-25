package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SimplePendMeasureEntity {

    /**
     * STATUS : 0
     * DATA : [{"STYLESEASON":"SUMMER","CHECKGROUPINDEX":"1","CUSTOMERNAME":"NIKE, INC.","CUSTOMERID":"bb10132a-42ee-44f3-b9c8-14ce6146be8b","CATEGORYNAME":"JORDAN","SUBMITDATE":"2018-11-02 11:57:23","CREATEUSERID":"B82957","SUBMITUSERNAME":"高建赐","SAMPLETYPENAME":"AP1","PRODUCEORDERID":"dc9388ac-b1c7-4667-b206-49be95aefc8b","PDMDEVPLANORDERID":"86db6250-7993-4873-b524-e87292a01461","STATUS":"CheckOK","FEPO":"9KC34951"}]
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
         * STYLESEASON : SUMMER
         * CHECKGROUPINDEX : 1
         * CUSTOMERNAME : NIKE, INC.
         * CUSTOMERID : bb10132a-42ee-44f3-b9c8-14ce6146be8b
         * CATEGORYNAME : JORDAN
         * SUBMITDATE : 2018-11-02 11:57:23
         * CREATEUSERID : B82957
         * SUBMITUSERNAME : 高建赐
         * SAMPLETYPENAME : AP1
         * PRODUCEORDERID : dc9388ac-b1c7-4667-b206-49be95aefc8b
         * PDMDEVPLANORDERID : 86db6250-7993-4873-b524-e87292a01461
         * STATUS : CheckOK
         * FEPO : 9KC34951
         */

        private String STYLESEASON;
        private String CHECKGROUPINDEX;
        private String CUSTOMERNAME;
        private String CUSTOMERID;
        private String CATEGORYNAME;
        private String SUBMITDATE;
        private String CREATEUSERID;
        private String SUBMITUSERNAME;
        private String SAMPLETYPENAME;
        private String PRODUCEORDERID;
        private String PDMDEVPLANORDERID;
        private String STATUS;
        private String FEPO;

        public String getSTYLESEASON() {
            return STYLESEASON;
        }

        public void setSTYLESEASON(String STYLESEASON) {
            this.STYLESEASON = STYLESEASON;
        }

        public String getCHECKGROUPINDEX() {
            return CHECKGROUPINDEX;
        }

        public void setCHECKGROUPINDEX(String CHECKGROUPINDEX) {
            this.CHECKGROUPINDEX = CHECKGROUPINDEX;
        }

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public void setCUSTOMERNAME(String CUSTOMERNAME) {
            this.CUSTOMERNAME = CUSTOMERNAME;
        }

        public String getCUSTOMERID() {
            return CUSTOMERID;
        }

        public void setCUSTOMERID(String CUSTOMERID) {
            this.CUSTOMERID = CUSTOMERID;
        }

        public String getCATEGORYNAME() {
            return CATEGORYNAME;
        }

        public void setCATEGORYNAME(String CATEGORYNAME) {
            this.CATEGORYNAME = CATEGORYNAME;
        }

        public String getSUBMITDATE() {
            return SUBMITDATE;
        }

        public void setSUBMITDATE(String SUBMITDATE) {
            this.SUBMITDATE = SUBMITDATE;
        }

        public String getCREATEUSERID() {
            return CREATEUSERID;
        }

        public void setCREATEUSERID(String CREATEUSERID) {
            this.CREATEUSERID = CREATEUSERID;
        }

        public String getSUBMITUSERNAME() {
            return SUBMITUSERNAME;
        }

        public void setSUBMITUSERNAME(String SUBMITUSERNAME) {
            this.SUBMITUSERNAME = SUBMITUSERNAME;
        }

        public String getSAMPLETYPENAME() {
            return SAMPLETYPENAME;
        }

        public void setSAMPLETYPENAME(String SAMPLETYPENAME) {
            this.SAMPLETYPENAME = SAMPLETYPENAME;
        }

        public String getPRODUCEORDERID() {
            return PRODUCEORDERID;
        }

        public void setPRODUCEORDERID(String PRODUCEORDERID) {
            this.PRODUCEORDERID = PRODUCEORDERID;
        }

        public String getPDMDEVPLANORDERID() {
            return PDMDEVPLANORDERID;
        }

        public void setPDMDEVPLANORDERID(String PDMDEVPLANORDERID) {
            this.PDMDEVPLANORDERID = PDMDEVPLANORDERID;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }
    }
}
