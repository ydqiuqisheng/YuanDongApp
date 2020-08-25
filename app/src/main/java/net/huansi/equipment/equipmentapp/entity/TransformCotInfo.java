package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class TransformCotInfo {

    /**
     * STATUS : 0
     * DATA : [{"SMV":"20.8","FEPO":"9KC34SE7"}]
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
         * SMV : 20.8
         * FEPO : 9KC34SE7
         */

        private String SMV;
        private String FEPO;

        public String getSMV() {
            return SMV;
        }

        public void setSMV(String SMV) {
            this.SMV = SMV;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }
    }
}
