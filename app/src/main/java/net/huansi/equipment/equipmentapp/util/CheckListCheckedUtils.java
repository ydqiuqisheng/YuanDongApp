//package net.huansi.equipment.equipmentapp.util;
//
//import java.util.List;
//
///**
// * Created by zhou.mi on 2018/1/11.
// */
//
//public class CheckListCheckedUtils {
//
//    /**
//     * STATUS : 0
//     * DATA : [{"BARCODE":"A1712081220015","CUSTOMERNAME":"远纺织染（苏州）有限公司","SN":"18NBU596198-102FEC58-GRS-00010*5","FEPOQUANTITY":"7KC349S6A01:14.60","MATERIALCODE":"#596198","MATERIALNAME":"色织","MATERIALID":"H2YP1632-02A-GRS","COLORCODE":"02K","COLORNAME":"碳麻灰","QUANTITY":"14.60","QUANTITYPS":"64.00","QUANTITYPSSUM":"567.00","VATNO":"003","PNO":"015","CHECKSTATUS":"已扫描通过","BARCODETTLNUM":"26","CHECKEDTTLNUM":"7","UNCHECKEDNUM":"19"}]
//     */
//
//    private String STATUS;
//    private List<DATABean> DATA;
//
//    public String getSTATUS() {
//        return STATUS;
//    }
//
//    public void setSTATUS(String STATUS) {
//        this.STATUS = STATUS;
//    }
//
//    public List<DATABean> getDATA() {
//        return DATA;
//    }
//
//    public void setDATA(List<DATABean> DATA) {
//        this.DATA = DATA;
//    }
//
//    public static class DATABean {
//        /**
//         * BARCODE : A1712081220015
//         * CUSTOMERNAME : 远纺织染（苏州）有限公司
//         * SN : 18NBU596198-102FEC58-GRS-00010*5
//         * FEPOQUANTITY : 7KC349S6A01:14.60
//         * MATERIALCODE : #596198
//         * MATERIALNAME : 色织
//         * MATERIALID : H2YP1632-02A-GRS
//         * COLORCODE : 02K
//         * COLORNAME : 碳麻灰
//         * QUANTITY : 14.60
//         * QUANTITYPS : 64.00
//         * QUANTITYPSSUM : 567.00
//         * VATNO : 003
//         * PNO : 015
//         * CHECKSTATUS : 已扫描通过
//         * BARCODETTLNUM : 26
//         * CHECKEDTTLNUM : 7
//         * UNCHECKEDNUM : 19
//         */
//
//        private String BARCODE;
//        private String CUSTOMERNAME;
//        private String SN;
//        private String FEPOCODES;
//        private String FEPOQUANTITY;
//        private String MATERIALCODE;
//        private String MATERIALNAME;
//        private String MATERIALID;
//        private String COLORCODE;
//        private String COLORNAME;
//        private String QUANTITY;
//        private String QUANTITYPS;
//        private String QUANTITYPSSUM;
//        private String QUANTITYPSCHECK;
//        private String QUANTITYPSUNCHECK;
//        private String VATNO;
//        private String PNO;
//        private String CHECKSTATUS;
//        private String BARCODETTLNUM;
//        private String CHECKEDTTLNUM;
//        private String UNCHECKEDNUM;
//
//        public String getBARCODE() {
//            return BARCODE;
//        }
//
//        public void setBARCODE(String BARCODE) {
//            this.BARCODE = BARCODE;
//        }
//
//        public String getCUSTOMERNAME() {
//            return CUSTOMERNAME;
//        }
//
//        public void setCUSTOMERNAME(String CUSTOMERNAME) {
//            this.CUSTOMERNAME = CUSTOMERNAME;
//        }
//
//        public String getSN() {
//            return SN;
//        }
//
//        public void setSN(String SN) {
//            this.SN = SN;
//        }
//        public String getFEPOCODES() {
//            return FEPOCODES;
//        }
//
//        public void setFEPOCODES(String FEPOCODES) {
//            this.FEPOCODES = FEPOCODES;
//        }
//
//        public String getFEPOQUANTITY() {
//            return FEPOQUANTITY;
//        }
//
//        public void setFEPOQUANTITY(String FEPOQUANTITY) {
//            this.FEPOQUANTITY = FEPOQUANTITY;
//        }
//
//        public String getMATERIALCODE() {
//            return MATERIALCODE;
//        }
//
//        public void setMATERIALCODE(String MATERIALCODE) {
//            this.MATERIALCODE = MATERIALCODE;
//        }
//
//        public String getMATERIALNAME() {
//            return MATERIALNAME;
//        }
//
//        public void setMATERIALNAME(String MATERIALNAME) {
//            this.MATERIALNAME = MATERIALNAME;
//        }
//
//        public String getMATERIALID() {
//            return MATERIALID;
//        }
//
//        public void setMATERIALID(String MATERIALID) {
//            this.MATERIALID = MATERIALID;
//        }
//
//        public String getCOLORCODE() {
//            return COLORCODE;
//        }
//
//        public void setCOLORCODE(String COLORCODE) {
//            this.COLORCODE = COLORCODE;
//        }
//
//        public String getCOLORNAME() {
//            return COLORNAME;
//        }
//
//        public void setCOLORNAME(String COLORNAME) {
//            this.COLORNAME = COLORNAME;
//        }
//
//        public String getQUANTITY() {
//            return QUANTITY;
//        }
//
//        public void setQUANTITY(String QUANTITY) {
//            this.QUANTITY = QUANTITY;
//        }
//
//        public String getQUANTITYPS() {
//            return QUANTITYPS;
//        }
//
//        public void setQUANTITYPS(String QUANTITYPS) {
//            this.QUANTITYPS = QUANTITYPS;
//        }
//
//        public String getQUANTITYPSSUM() {
//            return QUANTITYPSSUM;
//        }
//
//        public void setQUANTITYPSSUM(String QUANTITYPSSUM) {
//            this.QUANTITYPSSUM = QUANTITYPSSUM;
//        }
//        public String getQUANTITYPSCHECK() {
//            return QUANTITYPSCHECK;
//        }
//
//        public void setQUANTITYPSCHECK(String QUANTITYPSCHECK) {
//            this.QUANTITYPSCHECK = QUANTITYPSCHECK;
//        }
//        public String getQUANTITYPSUNCHECK() {
//            return QUANTITYPSUNCHECK;
//        }
//
//        public void setQUANTITYPSUNCHECK(String QUANTITYPSUNCHECK) {
//            this.QUANTITYPSUNCHECK = QUANTITYPSUNCHECK;
//        }
//
//
//
//        public String getVATNO() {
//            return VATNO;
//        }
//
//        public void setVATNO(String VATNO) {
//            this.VATNO = VATNO;
//        }
//
//        public String getPNO() {
//            return PNO;
//        }
//
//        public void setPNO(String PNO) {
//            this.PNO = PNO;
//        }
//
//        public String getCHECKSTATUS() {
//            return CHECKSTATUS;
//        }
//
//        public void setCHECKSTATUS(String CHECKSTATUS) {
//            this.CHECKSTATUS = CHECKSTATUS;
//        }
//
//        public String getBARCODETTLNUM() {
//            return BARCODETTLNUM;
//        }
//
//        public void setBARCODETTLNUM(String BARCODETTLNUM) {
//            this.BARCODETTLNUM = BARCODETTLNUM;
//        }
//
//        public String getCHECKEDTTLNUM() {
//            return CHECKEDTTLNUM;
//        }
//
//        public void setCHECKEDTTLNUM(String CHECKEDTTLNUM) {
//            this.CHECKEDTTLNUM = CHECKEDTTLNUM;
//        }
//
//        public String getUNCHECKEDNUM() {
//            return UNCHECKEDNUM;
//        }
//
//        public void setUNCHECKEDNUM(String UNCHECKEDNUM) {
//            this.UNCHECKEDNUM = UNCHECKEDNUM;
//        }
//    }
//}
