package com.example.hierysmb;

import static com.hierynomus.msdtyp.AccessMask.FILE_ADD_SUBDIRECTORY;
import static com.hierynomus.msdtyp.AccessMask.FILE_LIST_DIRECTORY;
import static com.hierynomus.msdtyp.AccessMask.FILE_READ_ATTRIBUTES;
import static com.hierynomus.msdtyp.AccessMask.FILE_READ_EA;
import static com.hierynomus.msfscc.FileAttributes.FILE_ATTRIBUTE_DIRECTORY;
import static com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_CREATE;
import static com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_OPEN;
import static com.hierynomus.mssmb2.SMB2CreateOptions.FILE_DIRECTORY_FILE;
import static com.hierynomus.mssmb2.SMB2ShareAccess.ALL;
import static java.util.EnumSet.of;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.common.SmbPath;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.Directory;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.utils.SmbFiles;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //Code Working for SMB
    SMBClient client = new SMBClient();
    EditText ip,folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("jashwant", "onCreate: ");
        ip=(EditText) findViewById(R.id.ipaddress);
        folder=(EditText)findViewById(R.id.folderr) ;




        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    func();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        Button connecttt= (Button) findViewById(R.id.button);
        connecttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread2 = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            func();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread2.start();
            }
        });




    }
    void func(){
        Log.d("jashwant", "onCreate:func client"+client.getServerList().toString());
        try (Connection connection = client.connect(String.valueOf(ip.getText()))) {
            Log.d("jashwant", "onCreate: try connection");
            AuthenticationContext ac = new AuthenticationContext("smbtest", "smbtest".toCharArray(), null);
            Log.d("jashwant", "onCreate: try connection ac.tostring()-"+ac.toString());
            Session session = connection.authenticate(ac);

            Log.d("jashwant", "onCreate: try connection session.tostring()-"+session.toString());
            Log.d("jashwant", "onCreate: try connection session-"+session);
            Log.d("jashwant", "onCreate: try connection session-"+session.getConnection().toString());
            Log.d("jashwant", "onCreate: try connection session-"+session.getSessionId());
            Log.d("jashwant", "onCreate: try connection session-"+session.getAuthenticationContext().toString());
            Log.d("jashwant", "onCreate: try connection session-"+session.getSessionContext().toString());
            Log.d("jashwant", "onCreate: try connection session-"+session.getConnection());

            // Connect to Share
            try (DiskShare share = (DiskShare) session.connectShare(String.valueOf(folder.getText()))) {
                Log.d("jashwant", "onCreate: try connection tarun folder share-"+share);
                SmbPath pathh = share.getSmbPath();
                FileAllInformation fileAllInformation =share.getFileInformation(pathh.getPath());
                Log.d("jashwant", "onCreate: try connection tarun folder path-"+pathh+" "+fileAllInformation.getAccessInformation());
                for (FileIdBothDirectoryInformation f : share.list("")) {
                    //Log.d("jashwant", "onCreate: try connection tarun f -"+share.list(""));
                    Log.d("jashwant", "onCreate: try connection tarun folder2 "+f.getFileName());
                    //Log.d("jashwant", "onCreate: try connection tarun folder2 "+f.getShortName());
                    //Log.d("jashwant", "onCreate: try connection tarun size- "+f.getAllocationSize());

                }
            }
        } catch (IOException e) {
            Log.d("jashwant", "onCreate: catch exception e-"+e);
            e.printStackTrace();
        }
    }
    // Helper method to extract file extension
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "No extension";
    }
}