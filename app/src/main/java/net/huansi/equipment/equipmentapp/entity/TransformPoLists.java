package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class TransformPoLists {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"0KC34BD3","SEWLINE":"1B-3","TRANSFORMDAY":"2020-05-12"},{"FEPO":"0KC34BG5","SEWLINE":"1B-7","TRANSFORMDAY":"2020-05-15"},{"FEPO":"0KC34CJ4","SEWLINE":"1C-5","TRANSFORMDAY":"2020-05-12"},{"FEPO":"0KC34U28","SEWLINE":"1B-4","TRANSFORMDAY":"2020-05-11"},{"FEPO":"0KC34U28","SEWLINE":"1B-5","TRANSFORMDAY":"2020-05-06"}]
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
         * FEPO : 0KC34BD3
         * SEWLINE : 1B-3
         * TRANSFORMDAY : 2020-05-12
         */

        private String FEPO;
        private String SEWLINE;
        private String TRANSFORMDAY;

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public void setCUSTOMERNAME(String CUSTOMERNAME) {
            this.CUSTOMERNAME = CUSTOMERNAME;
        }

        private String CUSTOMERNAME;

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getSEWLINE() {
            return SEWLINE;
        }

        public void setSEWLINE(String SEWLINE) {
            this.SEWLINE = SEWLINE;
        }

        public String getTRANSFORMDAY() {
            return TRANSFORMDAY;
        }

        public void setTRANSFORMDAY(String TRANSFORMDAY) {
            this.TRANSFORMDAY = TRANSFORMDAY;
        }
    }
}
