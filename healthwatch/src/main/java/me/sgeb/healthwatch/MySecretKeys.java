package me.sgeb.healthwatch;

public class MySecretKeys {
    // Register your app and fill in with your own application keys
    // See https://runkeeper.com/partner/applications/

    // Once you filled in the keys, make sure to run
    //   git update-index --assume-unchanged MySecretKeys.java
    // to avoid accidentally committing sensitive information.

    private final String CLIENT_ID = "fill_in_with_your_details";
    private final String CLIENT_SECRET = "fill_in_with_your_details";

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    public String getClientId() {
        return CLIENT_ID;
    }
}
