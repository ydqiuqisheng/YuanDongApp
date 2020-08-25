package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class StoreLocation {

    /**
     * STATUS : 0
     * DATA : [{"STOCKPOSITION_CURRENT":"A1-4"},{"STOCKPOSITION_CURRENT":"B1-4"},{"STOCKPOSITION_CURRENT":"C1-4"},{"STOCKPOSITION_CURRENT":"C2-4"},{"STOCKPOSITION_CURRENT":"C3-4"},{"STOCKPOSITION_CURRENT":"D1-4"},{"STOCKPOSITION_CURRENT":"D2-4"},{"STOCKPOSITION_CURRENT":"D3-4"},{"STOCKPOSITION_CURRENT":"E2-7"},{"STOCKPOSITION_CURRENT":"E3-6"},{"STOCKPOSITION_CURRENT":"E3-7"}]
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
         * STOCKPOSITION_CURRENT : A1-4
         */

        private String STOCKPOSITION_CURRENT;

        public String getSTOCKPOSITION_CURRENT() {
            return STOCKPOSITION_CURRENT;
        }

        public void setSTOCKPOSITION_CURRENT(String STOCKPOSITION_CURRENT) {
            this.STOCKPOSITION_CURRENT = STOCKPOSITION_CURRENT;
        }
    }
}
