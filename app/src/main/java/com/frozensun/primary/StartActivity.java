package com.frozensun.primary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.frozensun.messenger.GetAddress;
import com.frozensun.messenger.R;

import java.util.concurrent.ExecutionException;

public class StartActivity extends Activity implements View.OnClickListener
{
    private EditText mIPBox, mNameBox, mPortBox;
    private Button mCreateButton, mJoinButton;
    private String mMyIP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_layout);
        try
        {
            mMyIP = (new GetAddress()).execute().get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        mIPBox = (EditText) findViewById(R.id.ip_text_box);
        mNameBox = (EditText) findViewById(R.id.name_text_box);
        mPortBox = (EditText) findViewById(R.id.port_text_box);
        mCreateButton = (Button) findViewById(R.id.create_server_button);
        mJoinButton = (Button) findViewById(R.id.join_server_button);
        mCreateButton.setOnClickListener(this);
        mJoinButton.setOnClickListener(this);
    }

    public boolean checkIP(String ipField)
    {
        boolean ch = true;
        Log.d("startactivity","matching" + ipField);
        if (!ipField.matches("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"))
        {
            Log.d("startactivity","does not match" + ipField);
            ch = false;
        }
        return ch;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if (mPortBox.getText().toString().equals(""))
        {
            Toast.makeText(this, "Invalid Port", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean validity = true;
        Intent i = new Intent(this, com.frozensun.messenger.MainActivity.class);
        i.putExtra("Name", mNameBox.getText().toString());
        i.putExtra("IP", mIPBox.getText().toString());
        i.putExtra("Port", Integer.parseInt(mPortBox.getText().toString()));
        i.putExtra("myIP", mMyIP);
        if (v == mCreateButton)
            i.putExtra("Create", true);
        else
        {
            i.putExtra("Create", false);
            if (!checkIP(mIPBox.getText().toString()))
            {
                Toast.makeText(this, "Invalid IP", Toast.LENGTH_SHORT).show();
                validity = false;
            }
        }
        if (Integer.parseInt(mPortBox.getText().toString()) <= 0 || Integer.parseInt(mPortBox.getText().toString()) >= 65536)
        {
            Toast.makeText(this, "Invalid Port", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        if (validity)
            startActivity(i);
    }
}
