// IOnNewBookArricedListener.aidl
package com.example.ym.myaidlservicedemo;

// Declare any non-default types here with import statements
import com.example.ym.myaidlservicedemo.Book;
interface IOnNewBookArricedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book book);
}
