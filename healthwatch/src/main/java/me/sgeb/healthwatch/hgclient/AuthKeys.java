package me.sgeb.healthwatch.hgclient;

public class AuthKeys {
    // Register your app and fill in with your own application keys
    // See https://runkeeper.com/partner/applications/

    // Once you filled in the keys, make sure to run
    //   git update-index --assume-unchanged AuthKeys.java
    // to avoid accidentally committing sensitive information.

    private final String CLIENT_ID = "fill in with your details";
    private final String CLIENT_SECRET = "fill in with your details";

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    public String getClientId() {
        return CLIENT_ID;
    }
}
