package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class VideoEntity {

    /**
     * STATUS : 0
     * DATA : [{"PROCESSNO":"1","PROCESSNAME":"拼缝","PICURL":"http://10.17.111.23:8064/file/201703110134107138_thumb.jpg","VIDEOURL":"http://10.17.111.23:8064/mp4/bar.mp4"},{"PROCESSNO":"2","PROCESSNAME":"车缝","PICURL":"http://10.17.111.23:8064/file/201703110134128198_thumb.jpg","VIDEOURL":"http://10.17.111.23:8064/mp4/pcxb.mp4"},{"PROCESSNO":"3","PROCESSNAME":"拷边","PICURL":"http://10.17.111.23:8064/file/201709291013578121_thumb.jpg","VIDEOURL":"http://10.17.111.23:8064/mp4/pcxk.mp4"},{"PROCESSNO":"4","PROCESSNAME":"收尾","PICURL":"http://10.17.111.23:8064/file/201710100831555296_thumb.jpg","VIDEOURL":"http://10.17.111.23:8064/mp4/sbc.mp4"}]
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
         * PROCESSNO : 1
         * PROCESSNAME : 拼缝
         * PICURL : http://10.17.111.23:8064/file/201703110134107138_thumb.jpg
         * VIDEOURL : http://10.17.111.23:8064/mp4/bar.mp4
         */

        private String PROCESSNO;
        private String PROCESSNAME;
        private String PICURL;
        private String VIDEOURL;

        public String getPROCESSNO() {
            return PROCESSNO;
        }

        public void setPROCESSNO(String PROCESSNO) {
            this.PROCESSNO = PROCESSNO;
        }

        public String getPROCESSNAME() {
            return PROCESSNAME;
        }

        public void setPROCESSNAME(String PROCESSNAME) {
            this.PROCESSNAME = PROCESSNAME;
        }

        public String getPICURL() {
            return PICURL;
        }

        public void setPICURL(String PICURL) {
            this.PICURL = PICURL;
        }

        public String getVIDEOURL() {
            return VIDEOURL;
        }

        public void setVIDEOURL(String VIDEOURL) {
            this.VIDEOURL = VIDEOURL;
        }
    }
}
