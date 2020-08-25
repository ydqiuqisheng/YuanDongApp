package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class EvaluateEntities {


    /**
     * STATUS : 0
     * DATA : [{"ID":"1","CALLREPAIRITEMID":"96293289-73FE-41FB-BC8F-5A8478F8E36B","CALLREPAIRDATE":"Line:1A-10/ Time: 2018-10-29 08:40","ASSETSCODE":"123456","SEWLINE":"1A-10","CALLREPAIR":"2018-10-29 08:40","CALLREPAIREMPLOYEEID":"Admin","STATUS":"维修完成","COSTCENTER":"","OUTFACTORYCODE":"O2878","EPCCODE":"2016040708701A01104000F3","EQUIPMENTNAME":"单针平车","MODEL":"DDL8700N-7WB  SC-910","EQUIPMENTDETAILID":"546EDC27-D5F7-4EE7-A2A2-6F407660BDAF","POSTID":"7640C07A-2566-434E-93B1-73C3158DE8DB","REPAIRUSER":"许彬","REPAIRSTARTDATE":"2018-10-29 09:02:52"}]
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
         * ID : 1
         * CALLREPAIRITEMID : 96293289-73FE-41FB-BC8F-5A8478F8E36B
         * CALLREPAIRDATE : Line:1A-10/ Time: 2018-10-29 08:40
         * ASSETSCODE : 123456
         * SEWLINE : 1A-10
         * CALLREPAIR : 2018-10-29 08:40
         * CALLREPAIREMPLOYEEID : Admin
         * STATUS : 维修完成
         * COSTCENTER :
         * OUTFACTORYCODE : O2878
         * EPCCODE : 2016040708701A01104000F3
         * EQUIPMENTNAME : 单针平车
         * MODEL : DDL8700N-7WB  SC-910
         * EQUIPMENTDETAILID : 546EDC27-D5F7-4EE7-A2A2-6F407660BDAF
         * POSTID : 7640C07A-2566-434E-93B1-73C3158DE8DB
         * REPAIRUSER : 许彬
         * REPAIRSTARTDATE : 2018-10-29 09:02:52
         */

        private String ID;
        private String CALLREPAIRITEMID;
        private String CALLREPAIRDATE;
        private String ASSETSCODE;
        private String SEWLINE;
        private String CALLREPAIR;
        private String CALLREPAIREMPLOYEEID;
        private String STATUS;
        private String COSTCENTER;
        private String OUTFACTORYCODE;
        private String EPCCODE;
        private String EQUIPMENTNAME;
        private String MODEL;
        private String EQUIPMENTDETAILID;
        private String POSTID;
        private String REPAIRUSER;
        private String REPAIRSTARTDATE;
        private String EQUIPCOMPLETEDATE;
        private String SEWCOMPLETEDATE;

        public String getEQUIPCOMPLETEDATE() {
            return EQUIPCOMPLETEDATE;
        }

        public void setEQUIPCOMPLETEDATE(String EQUIPCOMPLETEDATE) {
            this.EQUIPCOMPLETEDATE = EQUIPCOMPLETEDATE;
        }

        public String getSEWCOMPLETEDATE() {
            return SEWCOMPLETEDATE;
        }

        public void setSEWCOMPLETEDATE(String SEWCOMPLETEDATE) {
            this.SEWCOMPLETEDATE = SEWCOMPLETEDATE;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getCALLREPAIRITEMID() {
            return CALLREPAIRITEMID;
        }

        public void setCALLREPAIRITEMID(String CALLREPAIRITEMID) {
            this.CALLREPAIRITEMID = CALLREPAIRITEMID;
        }

        public String getCALLREPAIRDATE() {
            return CALLREPAIRDATE;
        }

        public void setCALLREPAIRDATE(String CALLREPAIRDATE) {
            this.CALLREPAIRDATE = CALLREPAIRDATE;
        }

        public String getASSETSCODE() {
            return ASSETSCODE;
        }

        public void setASSETSCODE(String ASSETSCODE) {
            this.ASSETSCODE = ASSETSCODE;
        }

        public String getSEWLINE() {
            return SEWLINE;
        }

        public void setSEWLINE(String SEWLINE) {
            this.SEWLINE = SEWLINE;
        }

        public String getCALLREPAIR() {
            return CALLREPAIR;
        }

        public void setCALLREPAIR(String CALLREPAIR) {
            this.CALLREPAIR = CALLREPAIR;
        }

        public String getCALLREPAIREMPLOYEEID() {
            return CALLREPAIREMPLOYEEID;
        }

        public void setCALLREPAIREMPLOYEEID(String CALLREPAIREMPLOYEEID) {
            this.CALLREPAIREMPLOYEEID = CALLREPAIREMPLOYEEID;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getCOSTCENTER() {
            return COSTCENTER;
        }

        public void setCOSTCENTER(String COSTCENTER) {
            this.COSTCENTER = COSTCENTER;
        }

        public String getOUTFACTORYCODE() {
            return OUTFACTORYCODE;
        }

        public void setOUTFACTORYCODE(String OUTFACTORYCODE) {
            this.OUTFACTORYCODE = OUTFACTORYCODE;
        }

        public String getEPCCODE() {
            return EPCCODE;
        }

        public void setEPCCODE(String EPCCODE) {
            this.EPCCODE = EPCCODE;
        }

        public String getEQUIPMENTNAME() {
            return EQUIPMENTNAME;
        }

        public void setEQUIPMENTNAME(String EQUIPMENTNAME) {
            this.EQUIPMENTNAME = EQUIPMENTNAME;
        }

        public String getMODEL() {
            return MODEL;
        }

        public void setMODEL(String MODEL) {
            this.MODEL = MODEL;
        }

        public String getEQUIPMENTDETAILID() {
            return EQUIPMENTDETAILID;
        }

        public void setEQUIPMENTDETAILID(String EQUIPMENTDETAILID) {
            this.EQUIPMENTDETAILID = EQUIPMENTDETAILID;
        }

        public String getPOSTID() {
            return POSTID;
        }

        public void setPOSTID(String POSTID) {
            this.POSTID = POSTID;
        }

        public String getREPAIRUSER() {
            return REPAIRUSER;
        }

        public void setREPAIRUSER(String REPAIRUSER) {
            this.REPAIRUSER = REPAIRUSER;
        }

        public String getREPAIRSTARTDATE() {
            return REPAIRSTARTDATE;
        }

        public void setREPAIRSTARTDATE(String REPAIRSTARTDATE) {
            this.REPAIRSTARTDATE = REPAIRSTARTDATE;
        }
    }
}
