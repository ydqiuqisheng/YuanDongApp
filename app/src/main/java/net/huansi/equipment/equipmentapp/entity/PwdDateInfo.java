package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class PwdDateInfo {

    /**
     * STATUS : 0
     * DATA : [{"PWDUPDATE":"2019/10/16"}]
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
         * PWDUPDATE : 2019/10/16
         */

        private String PWDUPDATE;

        public String getPWDUPDATE() {
            return PWDUPDATE;
        }

        public void setPWDUPDATE(String PWDUPDATE) {
            this.PWDUPDATE = PWDUPDATE;
        }
    }
}
