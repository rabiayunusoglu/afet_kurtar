package com.example.afetkurtar;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class AccountInformation extends Application {

    private GoogleSignInAccount account;

    public GoogleSignInAccount getAccount() {
        return account;
    }
    public void setAccount(GoogleSignInAccount someVariable) {
        this.account = someVariable;
    }
}