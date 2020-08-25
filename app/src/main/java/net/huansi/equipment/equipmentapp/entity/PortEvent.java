package net.huansi.equipment.equipmentapp.entity;

import com.tools.io.PortManager;

public class PortEvent {
    public PortManager mPort;

    public PortEvent(PortManager mPort) {
        this.mPort = mPort;
    }

    public PortManager getmPort() {
        return mPort;
    }

    public void setmPort(PortManager mPort) {
        this.mPort = mPort;
    }
}
