package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class MeetingEntity {

    /**
     * STATUS : 0
     * DATA : [{"PDF":"http://10.17.111.23:80//Pic/PDM/2018/8KC34NA1_大货_10buy1.pdf"}]
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
         * PDF : http://10.17.111.23:80//Pic/PDM/2018/8KC34NA1_大货_10buy1.pdf
         */

        private String PDF;

        public String getPDF() {
            return PDF;
        }

        public void setPDF(String PDF) {
            this.PDF = PDF;
        }
    }
}
