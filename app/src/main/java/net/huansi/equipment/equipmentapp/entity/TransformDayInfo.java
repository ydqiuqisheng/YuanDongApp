package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class TransformDayInfo {

    /**
     * STATUS : 0
     * DATA : [{"ONLINEDATE":"2019-12-25"}]
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
         * ONLINEDATE : 2019-12-25
         */

        private String ONLINEDATE;

        public String getONLINEDATE() {
            return ONLINEDATE;
        }

        public void setONLINEDATE(String ONLINEDATE) {
            this.ONLINEDATE = ONLINEDATE;
        }
    }
}
