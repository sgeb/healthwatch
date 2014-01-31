package me.sgeb.healthwatch;

public class MySecretKeys {
    // Register your app and fill in with your own application keys
    // See https://runkeeper.com/partner/applications/

    // Once you filled in the keys, make sure to run
    //   git update-index --assume-unchanged MySecretKeys.java
    // to avoid accidentally committing sensitive information.

    private final String HEALTHGRAPH_CLIENT_ID = "fill_in_with_your_details";
    private final String HEALTHGRAPH_CLIENT_SECRET = "fill_in_with_your_details";
    private final String TESTFLIGHT_APP_TOKEN = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";

    public String getHealthGraphClientSecret() { return HEALTHGRAPH_CLIENT_SECRET; }
    public String getHealthGraphClientId() { return HEALTHGRAPH_CLIENT_ID; }
    public String getTestFlightAppToken() { return TESTFLIGHT_APP_TOKEN; }
}
