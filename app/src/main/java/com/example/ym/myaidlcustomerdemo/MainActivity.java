package com.example.ym.myaidlcustomerdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ym.myaidlservicedemo.Book;
import com.example.ym.myaidlservicedemo.IMyAidlInterface;
import com.example.ym.myaidlservicedemo.IOnNewBookArricedListener;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String TAG="MainActivity";
    @BindView(R.id.btn_addbook)
    private Button addBtn;
    @BindView(R.id.btn_getbooks)
    private Button getBooksListBtn;
    private EditText inPutXEdt;
    private EditText inPutYEdt;
    public  TextView resultTv;
    private boolean isConec=false;

    @BindView(R.id.btn_add_data)
    private Button addDataBtn;
    @BindView(R.id.btn_update_data)
            Button updateBtn;
    @BindView(R.id.btn_delete_data)
            Button deleteBtn;
    @BindView(R.id.btn_query_data)
            Button queryBtn;

    IMyAidlInterface mStub;
    private List<Book> mBooks;

    private MyHandle myHandle;
    private MySQLiteOpenHelper dbHelper;

    private IOnNewBookArricedListener mListener=new IOnNewBookArricedListener.Stub() {
        @Override
        public void onNewBookArrived(final Book book) throws RemoteException {

            myHandle.sendMessage(myHandle.obtainMessage(1,book));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    resultTv.setText(book.getName());
//                }
//            });

        }
    };

    private ServiceConnection mConnecion=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
             isConec=true;
             mStub=IMyAidlInterface.Stub.asInterface(iBinder);
            try {
                iBinder.linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                mStub.registListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConec=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        resultTv=findViewById(R.id.tv_bookname);



        myHandle=new MyHandle(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService();

    }
    private void bindService(){
        Intent intent=new Intent("com.example.ym.myaidlservicedemo.mainservice");
        intent.setPackage("com.example.ym.myaidlservicedemo");
        bindService(intent,mConnecion,BIND_AUTO_CREATE);
    }

    private IBinder.DeathRecipient mDeathRecipient=new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mStub==null){
                return;
            }
            mStub.asBinder().unlinkToDeath(mDeathRecipient,0);
            mStub=null;
            bindService();

        }
    };

    static class MyHandle extends Handler{
        WeakReference<Activity> mWeakReference;

        public MyHandle(Activity activity){
            mWeakReference=new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final MainActivity activity= (MainActivity) mWeakReference.get();
            if (activity==null){
                 return;
            }
            if (msg.what==1)
            activity.resultTv.setText(((Book)msg.obj).getName());

        }
    }
    @OnClick(R.id.btn_addbook)
    private void addBookData(){
        Log.e(TAG,"增加书籍");
        if (isConec){
            if(mStub !=null){
                try {
                    Book book=new Book("<<道德经>> 客户段新增书籍");
                    mStub.addBookInOut(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @OnClick(R.id.btn_getbooks)
    private void getBooksListe(){
        if(mStub !=null){
            try {
                mBooks=mStub.getBooklist();
                for (Book book:mBooks){
                    Log.e(TAG,book.getName().toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

     @OnClick(R.id.btn_add_data)
    private void addData(){

    }
    @OnClick(R.id.btn_update_data)
    private void updateData(){

    }
    @OnClick(R.id.btn_query_data)
    private void queryData(){

    }
    @OnClick(R.id.btn_delete_data)
    private void deleteData(){

    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mStub.unregistListener(mListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(mConnecion);
    }
}
