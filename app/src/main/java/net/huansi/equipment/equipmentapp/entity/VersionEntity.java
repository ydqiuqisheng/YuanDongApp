package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class VersionEntity {

    /**
     * STATUS : 0
     * DATA : [{"VERSION":"1","UPDATELOG":"基础版本"}]
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
         * VERSION : 1
         * UPDATELOG : 基础版本
         */

        private String VERSION;
        private String UPDATELOG;

        public String getVERSION() {
            return VERSION;
        }

        public void setVERSION(String VERSION) {
            this.VERSION = VERSION;
        }

        public String getUPDATELOG() {
            return UPDATELOG;
        }

        public void setUPDATELOG(String UPDATELOG) {
            this.UPDATELOG = UPDATELOG;
        }
    }
}
