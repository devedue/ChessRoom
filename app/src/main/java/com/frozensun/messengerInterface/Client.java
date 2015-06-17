package com.frozensun.messengerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Devedue on 21-05-2015.
 */
public class Client extends Thread
{
    private Thread mThread;
    private BufferedReader mIn;
    private Socket mSocket;
    private String mText = "", mRead = "", mName = "";
    private GUIClass mGUI;
    private Server mServer;
    private int mNumber;
    private boolean mInitiated = false, mCreate = false;

    public Client(String ip, int port, String name, Socket socket,
                  GUIClass gui, int clNumber) throws
            IOException
    {
        mNumber = clNumber;
        mName = name;
        mGUI = gui;
        if (socket == null)
        {
            mGUI.getText("<func>MESSAGE</func><name> </name><message>Connecting to " + ip + ":" + port + "</message>");
            mCreate = false;
            (new SocketThread(ip, port, this)).start();
        }
        else
        {
            mInitiated = true;
            mSocket = socket;
            mCreate = true;
            mIn = new BufferedReader(new InputStreamReader(
                    mSocket.getInputStream()));
        }
    }

    public void setGUI(GUIClass gui)
    {
        mGUI = gui;
        mGUI.setServer(mServer);
    }

    public void run()
    {
        while (mInitiated)
        {
            try
            {
                if (mInitiated)
                {
                    System.out.println("ClientThread Reading after initiated");
                    mRead = mIn.readLine();
                    if (mRead==null)
                        continue;
                    System.out.println("ClientThread Read" + mRead);
                    if (mCreate)
                    {
                        if (mRead!=null)
                        mServer.sendToAll(mRead, mNumber);
                    }
                    mGUI.getText(mRead);
                }
            } catch (SocketException e)
            {
                mInitiated = false;
            } catch (IOException e)
            {
            }
        }
    }

    public void start()
    {
        if (mThread == null)
        {
            mThread = new Thread(this);
            mThread.start();
        }
    }

    public String getInput()
    {
        return mText;
    }

    public Server getServer()
    {
        return mServer;
    }

    public void setSocket(Socket socket)
    {
        if (mSocket == null)
        {
            mSocket = socket;
            try
            {
                mIn = new BufferedReader(new InputStreamReader(
                        mSocket.getInputStream()));
                mGUI.getText("<func>TOAST</func><message>Connected to server</message>");
                mServer = new Server(0, mSocket, mName, this, mGUI);
                mServer.setGUI(mGUI);
                if (!mCreate)
                    mServer.sendDetails();
                mInitiated = true;
                start();
            } catch (IOException e)
            {
                System.out.println("ClientThread " + e);
            }
            System.out.println("Connected");
        }
    }

    public void setFailure()
    {
        mGUI.setConnectionFailure();
    }

    public void setServer(Server svr) throws IOException
    {
        mServer = svr;
        //TODO:mServer.updateDatabase(mIn.readLine());
        start();
    }
}

