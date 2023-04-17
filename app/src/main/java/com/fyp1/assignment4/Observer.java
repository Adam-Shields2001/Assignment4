package com.fyp1.assignment4;

import java.util.Observable;

public interface Observer {
    void update(Observable observable, Object arg);
}
