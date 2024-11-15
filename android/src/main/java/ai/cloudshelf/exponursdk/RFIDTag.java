package ai.cloudshelf.exponursdk;

public class RFIDTag {
    private RFIDTagGS1Data gs1Data;
    private String epc;
    private int rssi;
    private String tid;
    private String usr;

    public RFIDTag(String epc, int rssi) {
        this.epc = epc;
        this.rssi = rssi;
    }

    public String getEpc() {
        return this.epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }


    public void setGS1Data(RFIDTagGS1Data data) {
        this.gs1Data = data;
    }

    public Boolean getIsGS1Encoded() {
        return this.gs1Data != null;
    }

    public RFIDTagGS1Data getGS1Data() {
        return this.gs1Data;
    }
}
