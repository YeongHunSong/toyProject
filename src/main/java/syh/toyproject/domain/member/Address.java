package syh.toyproject.domain.member;

import lombok.Getter;

@Getter
public class Address {
    private String addressCode;
    private String address;
    private String addressDetail;

    public Address(String addressCode, String address, String addressDetail) {
        this.addressCode = addressCode;
        this.address = address;
        this.addressDetail = addressDetail;
    }
}
