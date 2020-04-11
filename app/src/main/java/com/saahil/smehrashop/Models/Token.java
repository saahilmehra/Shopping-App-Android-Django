package com.saahil.smehrashop.Models;

public class Token {
    String client_token;
    public Token(){

    }

    public Token(String client_token) {
        this.client_token = client_token;
    }

    public String getClient_token() {
        return client_token;
    }

    public void setClient_token(String client_token) {
        this.client_token = client_token;
    }
}
