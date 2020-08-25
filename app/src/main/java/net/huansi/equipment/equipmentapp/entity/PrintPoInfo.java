package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PrintPoInfo {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"0K060A39","STYLE":"PR2830","ENGLISHNAME":"FALL","COMBNAME":"","SIZEID":""}]
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
         * STYLE : PR2830
         * ENGLISHNAME : FALL
         * COMBNAME :
         * SIZEID :
         */

        private String FEPO;
        private String STYLE;
        private String ENGLISHNAME;
        private String COMBNAME;
        private String SIZEID;
        private String QUANTITY;
        public String getQUANTITY() {
            return QUANTITY;
        }

        public void setQUANTITY(String QUANTITY) {
            this.QUANTITY = QUANTITY;
        }

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getSTYLE() {
            return STYLE;
        }

        public void setSTYLE(String STYLE) {
            this.STYLE = STYLE;
        }

        public String getENGLISHNAME() {
            return ENGLISHNAME;
        }

        public void setENGLISHNAME(String ENGLISHNAME) {
            this.ENGLISHNAME = ENGLISHNAME;
        }

        public String getCOMBNAME() {
            return COMBNAME;
        }

        public void setCOMBNAME(String COMBNAME) {
            this.COMBNAME = COMBNAME;
        }

        public String getSIZEID() {
            return SIZEID;
        }

        public void setSIZEID(String SIZEID) {
            this.SIZEID = SIZEID;
        }
    }
}
