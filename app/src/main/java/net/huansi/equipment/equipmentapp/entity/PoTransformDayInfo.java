package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PoTransformDayInfo {

    /**
     * STATUS : 0
     * DATA : [{"THE_DATE":"2019-12-24"}]
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
         * THE_DATE : 2019-12-24
         */

        private String THE_DATE;

        public String getTHE_DATE() {
            return THE_DATE;
        }

        public void setTHE_DATE(String THE_DATE) {
            this.THE_DATE = THE_DATE;
        }
    }
}
