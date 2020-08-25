package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class MoveBoxInfo {

    /**
     * STATUS : 0
     * DATA : [{"CUSTOMERPO":"4505987093 PO ITEM:20","BOXBARCODE":"0006921665001951865"},{"CUSTOMERPO":"4505987093 PO ITEM:20","BOXBARCODE":"0006921665001951966"}]
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
         * CUSTOMERPO : 4505987093 PO ITEM:20
         * BOXBARCODE : 0006921665001951865
         */

        private String CUSTOMERPO;
        private String BOXBARCODE;

        public String getCUSTOMERPO() {
            return CUSTOMERPO;
        }

        public void setCUSTOMERPO(String CUSTOMERPO) {
            this.CUSTOMERPO = CUSTOMERPO;
        }

        public String getBOXBARCODE() {
            return BOXBARCODE;
        }

        public void setBOXBARCODE(String BOXBARCODE) {
            this.BOXBARCODE = BOXBARCODE;
        }
    }
}
