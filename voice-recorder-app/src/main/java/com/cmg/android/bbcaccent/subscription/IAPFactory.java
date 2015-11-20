package com.cmg.android.bbcaccent.subscription;

import android.content.Context;

import com.anjlab.android.iab.v3.BillingProcessor;

/**
 * Created by luhonghai on 17/11/2015.
 */
public class IAPFactory {

    public enum Subscription {
        MONTHLY("com.cmg.accenteasy.monthly"),
        YEARLY("com.cmg.accenteasy.yearly")
        ;
        String id;
        Subscription(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    private static final String LICENSE_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyrhH2j9ek8T5+5n9c4dfLrKSccb4m3YNVDGq67zdvFUcVYUOcnQCDeZ5QDgjWt5WqZAZ7uEVFNglG7pUXjRwzISexLIWPFTu0yCp3Xy2v7z1tczr88gFU6HDy6WDpo4eh5mhtb2uQ21DItfc+Tgb970zjW/+6HYZi6tSwXSZqQGcXumml3CF8Lx1j4Q/hRWCTur7aw1siy9pf6NbS2fG1xsOxXxr94qruxTdzDXMhKgONAWBDIRXa//U3WGsr3ps3c9zJ+r6gV2raA6rjelz6Nfw73+6dEUhnPp2hIo91zH1Hp34BN0tKcXUQSIVbgepbwBuTyj3p0Ydp3eYcvOBtwIDAQAB";
    private static final String MERCHANT_ID="09486777748621801705";

    private IAPFactory() {}

    public static BillingProcessor getBillingProcessor(Context context, BillingProcessor.IBillingHandler billingHandler) {
        return new BillingProcessor(context, LICENSE_KEY, MERCHANT_ID, billingHandler);
    }
}
