package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PackagingPicEntity {

    /**
     * STATUS : 0
     * DATA : [{"PICPATH":"http://10.17.111.4:8002/pic/20180626083536.jpg"},{"PICPATH":"http://10.17.111.4:8002/pic/20180626083546.jpg"}]
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
         * PICPATH : http://10.17.111.4:8002/pic/20180626083536.jpg
         */

        private String PICPATH;

        public String getPICPATH() {
            return PICPATH;
        }

        public void setPICPATH(String PICPATH) {
            this.PICPATH = PICPATH;
        }
    }
}
