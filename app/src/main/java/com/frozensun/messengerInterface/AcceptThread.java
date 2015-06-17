package com.frozensun.messengerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * Created by Devedue on 23-05-2015.
 */
public class AcceptThread extends Thread
{
    ServerSocket mServerSocket;
    Server mServer;
    Socket mSocket;

    AcceptThread(ServerSocket server, Server sclass)
    {
        mServerSocket = server;
        mServer = sclass;
    }

    public void init()
    {

    }

    public void run()
    {
        while (true)
        {
            try
            {
                mSocket = mServerSocket.accept();
                mServer.setSocket(mSocket);
            } catch (IOException e)
            {
            } catch (InterruptedException e)
            {
            } catch (ExecutionException e)
            {
            }
        }
    }


}
