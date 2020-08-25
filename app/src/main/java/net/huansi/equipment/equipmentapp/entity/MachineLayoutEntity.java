package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class MachineLayoutEntity  {

    /**
     * STATUS : 0
     * DATA : [{"FEPO":"8KC349M9","COLUMN1":"http://10.17.111.23/PIC/GSD/Style/2019/7150e7f5-ec70-4f3b-81b0-d150a4da2f22H.jpg"}]
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
         * FEPO : 8KC349M9
         * COLUMN1 : http://10.17.111.23/PIC/GSD/Style/2019/7150e7f5-ec70-4f3b-81b0-d150a4da2f22H.jpg
         */

        private String FEPO;
        private String COLUMN1;

        public String getFEPO() {
            return FEPO;
        }

        public void setFEPO(String FEPO) {
            this.FEPO = FEPO;
        }

        public String getCOLUMN1() {
            return COLUMN1;
        }

        public void setCOLUMN1(String COLUMN1) {
            this.COLUMN1 = COLUMN1;
        }
    }
}
