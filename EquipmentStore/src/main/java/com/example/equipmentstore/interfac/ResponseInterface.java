package com.example.equipmentstore.interfac;

import com.example.equipmentstore.models.EquipmentModel;

public interface ResponseInterface {

    public void responseSuccess(EquipmentModel equipmentModel);
    public void responseFailure();

    public void responseSuccess1(EquipmentModel equipmentModel);
    public void responseFailure1();
}
