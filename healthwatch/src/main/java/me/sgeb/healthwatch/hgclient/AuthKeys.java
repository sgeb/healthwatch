package me.sgeb.healthwatch.hgclient;

public class AuthKeys {
    // Register your app and fill in with your own application keys
    // See https://runkeeper.com/partner/applications/
    private final String CLIENT_ID = "fill in with your details";
    private final String CLIENT_SECRET = "fill in with your details";

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    public String getClientId() {
        return CLIENT_ID;
    }
}
