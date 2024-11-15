package ai.cloudshelf.exponursdk;

public class RFIDTagGS1Data {
    public String fullGS1String;
    public String companyPrefix;
    public String itemReference;
    public String serialNumber;

    public RFIDTagGS1Data(String fullGS1String, String companyPrefix, String itemReference, String serialNumber) {
        this.fullGS1String = fullGS1String;
        this.companyPrefix = companyPrefix;
        this.itemReference = itemReference;
        this.serialNumber = serialNumber;
    }
}