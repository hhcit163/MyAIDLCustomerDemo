// IMyAidlInterface.aidl
package com.example.ym.myaidlservicedemo;

// Declare any non-default types here with import statements
import com.example.ym.myaidlservicedemo.Book;
import com.example.ym.myaidlservicedemo.IOnNewBookArricedListener;

interface IMyAidlInterface {

    List<Book> getBooklist();
    void addBookInOut(inout Book book);
    void addBookIn(in Book book);
    void addBookOut(out Book book);

    void registListener(IOnNewBookArricedListener listener);
    void unregistListener(IOnNewBookArricedListener listener);
}
