package net.huansi.equipment.equipmentapp.util;

import java.util.List;

/**
 * Created by zhou.mi on 2017/10/26.
 */

public class CallRepairUtil {


    /**
     * STATUS : 0
     * DATA : [{"ID":"1","CALLREPAIRITEMID":"7AD6C50B-3DAD-40A2-9A3A-E65199010807","CALLREPAIRDATE":"Line:1A-1/ Time: 2018-08-09 08:54","ASSETSCODE":"123533","SEWLINE":"1A-1","CALLREPAIR":"2018-08-09 08:54","CALLREPAIREMPLOYEEID":"Admin","STATUS":"叫修","COSTCENTER":"","OUTFACTORYCODE":"22633","EPCCODE":"2016040608701A0110400399","EQUIPMENTNAME":"四针拼缝车","MODEL":"单切 FD-62G-01"}]
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
         * CALLREPAIRITEMID : 7AD6C50B-3DAD-40A2-9A3A-E65199010807
         * CALLREPAIRDATE : Line:1A-1/ Time: 2018-08-09 08:54
         * ASSETSCODE : 123533
         * SEWLINE : 1A-1
         * CALLREPAIR : 2018-08-09 08:54
         * CALLREPAIREMPLOYEEID : Admin
         * STATUS : 叫修
         * COSTCENTER :
         * OUTFACTORYCODE : 22633
         * EPCCODE : 2016040608701A0110400399
         * EQUIPMENTNAME : 四针拼缝车
         * MODEL : 单切 FD-62G-01
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
    }
}
