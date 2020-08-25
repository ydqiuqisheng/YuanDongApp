package net.huansi.equipment.equipmentapp.util;

import java.util.List;

/**
 * Created by zhou.mi on 2018/3/14.
 */

public class GetSnUtils {


    /**
     * STATUS : 0
     * DATA : [{"SN":"18NBF657644-022FEC47-00010*5"},{"SN":"18NBH644639-051FEC36-00010*1"},{"SN":"18NBU596198-102FEC58-GRS-00010*5"}]
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
         * SN : 18NBF657644-022FEC47-00010*5
         */

        private String SN;

        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
        }
    }
}
