package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class WorkGuidanceEntity {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"9KC34401","TYPE":"PicGuidance","NUM":"ftp://10.17.111.23:21/PIC/GSD/Style/2019/0807a9f7-2f88-4dc0-bf51-c0716f17b035C.png"},{"FEPO":"9KC34401","TYPE":"PicInstruct","NUM":"ftp://10.17.111.23:21/PIC/GSD/Style/2019/0807a9f7-2f88-4dc0-bf51-c0716f17b035D.png"}]
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
         * FEPO : 9KC34401
         * TYPE : PicGuidance
         * NUM : ftp://10.17.111.23:21/PIC/GSD/Style/2019/0807a9f7-2f88-4dc0-bf51-c0716f17b035C.png
         */

        private String FEPO;
        private String TYPE;
        private String NUM;

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getNUM() {
            return NUM;
        }

        public void setNUM(String NUM) {
            this.NUM = NUM;
        }
    }
}
