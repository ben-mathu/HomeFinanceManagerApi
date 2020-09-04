package com.miiguar.hfms.api.mpesa.threadrunner;

import com.miiguar.hfms.data.jar.model.MoneyJar;

/**
 * @author bernard
 */
public class SendTransactionRunnable implements Runnable {

    public MpesaResponseListener listener;
    private MoneyJar jar;
    private final String tId;

    public SendTransactionRunnable(MoneyJar jar, String tId) {
        this.jar = jar;
        this.tId = tId;
    }

    @Override
    public void run() {
        listener = new MpesaResponseListener(jar, tId);
    }
}