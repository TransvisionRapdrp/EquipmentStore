package com.example.equipmentstore.interfac;

import com.example.equipmentstore.models.PostOfficeModel;

public interface ResponseInterface1 {

    public void responseSuccess(PostOfficeModel postOfficeModel);
    public void responseFailure();
}
