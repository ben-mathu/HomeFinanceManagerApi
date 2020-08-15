package com.miiguar.hfms.api.mpesa.threadrunner;

/**
 * @author bernard
 */
public class SendTransactionRunnable implements Runnable {

    public MpesaResponseListener listener;
    private String randomString;

    public SendTransactionRunnable(String randomString) {
        this.randomString = randomString;
    }

    @Override
    public void run() {
        listener = new MpesaResponseListener(randomString);
    }
}
