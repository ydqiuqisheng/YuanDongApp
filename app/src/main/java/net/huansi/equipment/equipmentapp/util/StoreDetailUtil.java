package net.huansi.equipment.equipmentapp.util;

import java.util.List;

public class StoreDetailUtil {

    /**
     * STATUS : 0
     * DATA : [{"STOCKPOSITION_CURRENT":"420215400","BOXBARCODE":"0006921665001931978","QUANTITY":"30","CHTNAME":"國內採購"}]
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
         * STOCKPOSITION_CURRENT : 420215400
         * BOXBARCODE : 0006921665001931978
         * QUANTITY : 30
         * CHTNAME : 國內採購
         */

        private String STOCKPOSITION_CURRENT;
        private String BOXBARCODE;
        private String QUANTITY;
        private String CHTNAME;

        public String getSTOCKPOSITION_CURRENT() {
            return STOCKPOSITION_CURRENT;
        }

        public void setSTOCKPOSITION_CURRENT(String STOCKPOSITION_CURRENT) {
            this.STOCKPOSITION_CURRENT = STOCKPOSITION_CURRENT;
        }

        public String getBOXBARCODE() {
            return BOXBARCODE;
        }

        public void setBOXBARCODE(String BOXBARCODE) {
            this.BOXBARCODE = BOXBARCODE;
        }

        public String getQUANTITY() {
            return QUANTITY;
        }

        public void setQUANTITY(String QUANTITY) {
            this.QUANTITY = QUANTITY;
        }

        public String getCHTNAME() {
            return CHTNAME;
        }

        public void setCHTNAME(String CHTNAME) {
            this.CHTNAME = CHTNAME;
        }
    }
}
