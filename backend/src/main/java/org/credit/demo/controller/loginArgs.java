package org.credit.demo.controller;

public class loginArgs {
    private String account;
    private String passwd;

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "loginArgs{" +
                "account='" + account + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
