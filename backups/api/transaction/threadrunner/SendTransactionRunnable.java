package com.benardmathu.hfms.api.transaction.threadrunner;

import com.benardmathu.hfms.data.jar.model.MoneyJar;

/**
 * @author bernard
 */
public class SendTransactionRunnable implements Runnable {

    public MpesaResponseListener listener;
    private MoneyJar jar;
    private final Long tId;

    public SendTransactionRunnable(MoneyJar jar, Long tId) {
        this.jar = jar;
        this.tId = tId;
    }

    @Override
    public void run() {
        listener = new MpesaResponseListener(jar, tId);
    }
}
