package ai.cloudshelf.exponursdk;

public class RFIDTag {
    private Boolean isGS1Encoded;
    private String gS1String;
    private String epc;
    private int rssi;
    private String tid;
    private String usr;
    private Boolean usrSupported = false;

    public RFIDTag(String epc, int rssi, Boolean gs1Encoded, String gs1String) {
        this.isGS1Encoded = gs1Encoded;
        this.gS1String = gs1String;
        this.epc = epc;
        this.rssi = rssi;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUsr() {
        return usr;
    }

    public Boolean getUsrSupported() {
        return usrSupported;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public void setUsrSupported(Boolean supported) {
        this.usrSupported = supported;
    }

    public Boolean getIsGS1Encoded() {
        return isGS1Encoded;
    }

    public String getGS1String() {
        return gS1String;
    }
}
