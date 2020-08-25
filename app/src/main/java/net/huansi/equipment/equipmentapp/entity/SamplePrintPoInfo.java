package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class SamplePrintPoInfo {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"0K060A39","LASTUPDATEDATE":"2019-11-28 01:23:43","STATUS":"CheckOK","CUSTOMERNAME":"COLUMBIA"}]
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
         * FEPO : 0K060A39
         * LASTUPDATEDATE : 2019-11-28 01:23:43
         * STATUS : CheckOK
         * CUSTOMERNAME : COLUMBIA
         */

        private String FEPO;
        private String LASTUPDATEDATE;
        private String STATUS;
        private String CUSTOMERNAME;

        public String getSAMPLETYPE() {
            return SAMPLETYPE;
        }

        public void setSAMPLETYPE(String SAMPLETYPE) {
            this.SAMPLETYPE = SAMPLETYPE;
        }

        private String SAMPLETYPE;

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getLASTUPDATEDATE() {
            return LASTUPDATEDATE;
        }

        public void setLASTUPDATEDATE(String LASTUPDATEDATE) {
            this.LASTUPDATEDATE = LASTUPDATEDATE;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getCUSTOMERNAME() {
            return CUSTOMERNAME;
        }

        public void setCUSTOMERNAME(String CUSTOMERNAME) {
            this.CUSTOMERNAME = CUSTOMERNAME;
        }
    }
}
