package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SimpleSizesEntity {

    /**
     * STATUS : 0
     * DATA : [{"SIZEID":"M"}]
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
         * SIZEID : M
         */

        private String SIZEID;
        private String DESIGNERADVICE;

        public String getDESIGNERADVICE() {
            return DESIGNERADVICE;
        }

        public void setDESIGNERADVICE(String DESIGNERADVICE) {
            this.DESIGNERADVICE = DESIGNERADVICE;
        }

        public String getSIZEID() {
            return SIZEID;
        }

        public void setSIZEID(String SIZEID) {
            this.SIZEID = SIZEID;
        }
    }
}
