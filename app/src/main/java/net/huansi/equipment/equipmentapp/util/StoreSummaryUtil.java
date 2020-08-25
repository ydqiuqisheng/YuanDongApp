package net.huansi.equipment.equipmentapp.util;

import java.util.List;

public class StoreSummaryUtil {

    /**
     * STATUS : 0
     * DATA : [{"STOCKPOSITION_CURRENT":"420215400","NUM":"11","QUANTITY":"312","CHTNAME":"國內採購"},{"STOCKPOSITION_CURRENT":"GPK8C05180","NUM":"3","QUANTITY":"90","CHTNAME":"國內採購"}]
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
         * NUM : 11
         * QUANTITY : 312
         * CHTNAME : 國內採購
         */

        private String STOCKPOSITION_CURRENT;
        private String NUM;
        private String QUANTITY;
        private String CHTNAME;

        public String getSTOCKPOSITION_CURRENT() {
            return STOCKPOSITION_CURRENT;
        }

        public void setSTOCKPOSITION_CURRENT(String STOCKPOSITION_CURRENT) {
            this.STOCKPOSITION_CURRENT = STOCKPOSITION_CURRENT;
        }

        public String getNUM() {
            return NUM;
        }

        public void setNUM(String NUM) {
            this.NUM = NUM;
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
