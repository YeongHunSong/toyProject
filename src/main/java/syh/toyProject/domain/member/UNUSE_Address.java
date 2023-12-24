package syh.toyProject.domain.member;

import lombok.Getter;

@Getter
public class UNUSE_Address {
    private String addressCode;
    private String address;
    private String addressDetail;

    public UNUSE_Address(String addressCode, String address, String addressDetail) {
        this.addressCode = addressCode;
        this.address = address;
        this.addressDetail = addressDetail;
    }
}
