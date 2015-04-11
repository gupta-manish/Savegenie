package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.ShippingAddress;


public class ShippingAddressResponse {
    public ArrayList<ShippingAddress> addressList;

    public ShippingAddressResponse(ArrayList<ShippingAddress> addressList) {
        this.addressList = addressList;
    }
}
