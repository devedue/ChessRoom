package com.frozensun.messengerInterface;

/**
 * Created by Devedue on 24-05-2015.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketThread extends Thread
{
    Client mClient;
    Socket mSocket;
    String mIp;
    int mPort;
    boolean mConnected=false;

    public SocketThread(String ip, int port, Client client)
    {
        mIp = ip;
        mPort = port;
        mClient = client;
    }

    public void run()
    {
        while (!mConnected)
        {
            if (!mConnected)
            {
                try
                {
                    mSocket = new Socket();
                    mSocket.connect(new InetSocketAddress(mIp, mPort), 10000);
                    setSocket();
                } catch (IOException e)
                {
                    setFailure();
                }
            }
        }
    }

    public void setSocket()
    {
        mClient.setSocket(mSocket);
        mConnected=true;
    }

    public void setFailure()
    {
        mClient.setFailure();
    }

}

