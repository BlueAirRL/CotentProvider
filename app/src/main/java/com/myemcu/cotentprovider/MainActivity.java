package com.myemcu.cotentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import static android.Manifest.permission.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //定义向用户申请取得联系人的变量
    private static final int REQUEST_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //检测APP是否已向用户申请读取联系人权限
        int permission = ActivityCompat.checkSelfPermission(
                                                            this,
                                                            Manifest.permission.READ_CONTACTS
                                                           );

        //若未取得权限，则向用户权限申请
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                                              new String[] {READ_CONTACTS,WRITE_CONTACTS},
                                              REQUEST_CONTACTS);
        }
        else {
            readContacts(); // 读取"联系人"方法
        }
    }

    private void readContacts() {
        // 获取内容解析器对象
        ContentResolver resolver = getContentResolver();
        // 查询所有联系人并取得游标cursor
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        // 处理每一笔资料
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //Toast.makeText(this,id+"/"+name,Toast.LENGTH_SHORT).show();
            Log.d("RECORD",id+"/"+name);    // 在LogCat中显示所有联系人记录
        }
    }

    @Override//重写权限处理的实现方法
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults); //屏蔽父类调用
        switch (requestCode) {
            case REQUEST_CONTACTS:
                 if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                     readContacts(); // 取得联系人权限,进行存取
                 }
                 else {
                     new AlertDialog.Builder(this)
                                    .setMessage("必须允许联系人权限才能显示")
                                    .setPositiveButton("确定",null)
                                    .show();
                 }
                 break;
        }
    }
}
