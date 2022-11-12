package com.example.equipmentstore.interfac;

import com.example.equipmentstore.models.Staff_OutGoingModel;

public interface ResponseInterface2 {
    public void responseSuccess(Staff_OutGoingModel staff_outGoingModel);
    public void responseFailure();
}
