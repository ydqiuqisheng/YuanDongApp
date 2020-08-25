package net.huansi.equipment.equipmentapp.util;

import java.util.List;

/**
 * Created by zhou.mi on 2018/1/9.
 */

public class SewLineUtil {

    /**
     * STATUS : 0
     * DATA : [{"DEPTNAME":"一厂户外机缝/1E-1","POSTID":"31318C04-61D8-4BC5-B671-1F79FB14B23B"},{"DEPTNAME":"一厂户外机缝/1E-2","POSTID":"7E1B3215-8F1B-48C5-9FC0-6D1B22F5CF7C"},{"DEPTNAME":"一厂户外机缝/1E-3","POSTID":"66FE083F-E945-4D82-A05A-0BE275EBB9CD"},{"DEPTNAME":"一厂户外机缝/1E-4","POSTID":"63471372-F7D8-4AAA-9BF3-9F04631A4718"}]
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
         * DEPTNAME : 一厂户外机缝/1E-1
         * POSTID : 31318C04-61D8-4BC5-B671-1F79FB14B23B
         */

        private String DEPTNAME;
        private String POSTID;

        public String getDEPTNAME() {
            return DEPTNAME;
        }

        public void setDEPTNAME(String DEPTNAME) {
            this.DEPTNAME = DEPTNAME;
        }

        public String getPOSTID() {
            return POSTID;
        }

        public void setPOSTID(String POSTID) {
            this.POSTID = POSTID;
        }
    }
}
