package com.example.equipmentstore.interfac;


import com.example.equipmentstore.models.LoginDetails;

public interface Login_responseInterface {

    public void responseSuccess(LoginDetails loginModel);
    public void responseFailure();
}
