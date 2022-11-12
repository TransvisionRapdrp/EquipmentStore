package com.example.equipmentstore.interfac;

import com.example.equipmentstore.models.EquipmentDetails;

public interface Recv_responseInterface {
    public void recvresponsesuccess(EquipmentDetails equipmentDetails);

    public void recvresponsefailure();
}
