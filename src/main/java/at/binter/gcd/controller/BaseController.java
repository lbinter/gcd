package at.binter.gcd.controller;

import at.binter.gcd.GCDApplication;

public abstract class BaseController {
    protected GCDApplication gcd;

    public void setApplication(GCDApplication gcd) {
        this.gcd = gcd;
    }
}
