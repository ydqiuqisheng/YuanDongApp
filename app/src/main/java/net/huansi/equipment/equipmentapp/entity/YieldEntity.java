package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class YieldEntity {

    /**
     * STATUS : 0
     * DATA : [{"COLUMN1":"200"}]
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
         * COLUMN1 : 200
         */

        private String COLUMN1;

        public String getCOLUMN1() {
            return COLUMN1;
        }

        public void setCOLUMN1(String COLUMN1) {
            this.COLUMN1 = COLUMN1;
        }
    }
}
