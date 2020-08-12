package com.miiguar.hfms.api.mpesa;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.MPESA_URL;

/**
 * @author bernard
 */
//@WebServlet(API + MPESA_URL)
public class MpesaResponseReceiver {
    public static final long serializeVersionUID = 1L;

//    @Override
//    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
//        Log.d(TAG, "Received response from mpesa");
//    }
}
