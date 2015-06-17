
package com.frozensun.messengerInterface;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * Created by Devedue on 21-05-2015.
 */
public class Server
{
    private ServerSocket mServer;
    private Client mClient[];
    private int mPort, mSocketIter = 0;
    private String mDatabase[] = new String[32];
    private String mName = "";
    private PrintWriter mOut[];
    private Socket mSocket[] = new Socket[32];
    private GUIClass mGUI = null;
    boolean mInitiated[] = new boolean[32], mCreate;

    public Server(int port, Socket socket, String name, Client client, GUIClass gui) throws IOException
    {
        System.out.println("Server setting");
        mPort = port;
        mName = name;
        mOut = new PrintWriter[32];
        mGUI = gui;
        if (socket == null)
        {
            try
            {
                mServer = new ServerSocket(port);
            } catch (IOException e)
            {
                mGUI.setServerFailure();
            }
            mClient = new Client[32];
            AcceptThread acct = new AcceptThread(mServer, this);
            acct.start();
            mCreate = true;
        }
        else
        {
            mClient = new Client[32];
            mClient[0] = client;
            mCreate = false;
            mSocket[0] = socket;
            mOut[mSocketIter] = new PrintWriter(socket.getOutputStream(), true);
            mOut[mSocketIter].println("<func>NAME</func><message>" + name + "</message>");
            mOut[mSocketIter].println("<func>MESSAGE</func><name>" + name + "</name><message>Connected at " + mGUI.getIP() + "</message>");
            mSocketIter++;
        }
    }

    public void send(String send)
    {
        System.out.println("ServerSend received stage 1 ___" + send);
        if (!(send.trim().equalsIgnoreCase("")) || (send.trim().equalsIgnoreCase("\n")))
        {
            for (int i = 0; i < mSocketIter; i++)
            {
                System.out.println("ServerSend received to " + i + "___" + send);
                mOut[i].println(send);
            }
        }
        else
            mGUI.getText("<func>TOAST</func><message>None Connected</message>");
    }

    public void destroy() throws IOException
    {
        if (mSocket != null)
        {
            for (int i = 0; i < 32; i++)
            {
                if (mSocket[i] != null && !mSocket[i].isClosed())
                    mSocket[i].close();
            }
        }
        if (mServer != null)
            mServer.close();
    }

    public void setSocket(Socket socket) throws InterruptedException, IOException, ExecutionException
    {
        mClient[mSocketIter] = new Client(mGUI.getIP(), mPort, mName, socket, mGUI, mSocketIter);
        mClient[mSocketIter].setServer(this);
        mGUI.setClient(mClient[mSocketIter]);
        mSocket[mSocketIter] = socket;
        mOut[mSocketIter] = new PrintWriter(socket.getOutputStream(), true);
        mInitiated[mSocketIter] = true;
        if (mSocketIter > 30)
        {
            mOut[mSocketIter].println("<func>MESSAGE</func><name></name><message>Server Full</message>");
        }
        else
        {
            mSocketIter++;
        }
    }

    public void setGUI(GUIClass gui)
    {
        mGUI = gui;
        mGUI.setServer(this);
        System.out.println("sendText Successful");
    }

    public void sendDetails()
    {
        mOut[0].println("<func>NEWCLIENT</func><name>" + mName + "</name><ip>" + mGUI.getIP() + "</ip>");
    }

    public void sendToAll(String read, int number)
    {
        if (read.indexOf("<func>NEWCLIENT</func>") != 0)
        {
            for (int j = 0; j < mSocketIter; j++)
            {
                if (j == number)
                    continue;
                mOut[j].println(read);
            }
        }
    }

    public int getNumber(String string)
    {
        int ret = 0;
        for (int i = 0; i < 32; i++)
        {
            if (string.equals(mDatabase[i]))
                ret = i;
        }
        return ret;
    }

    public int getSize()
    {
        return mSocketIter;
    }

    public void removeClient(int n)
    {
        mSocketIter--;
        for (int i = n; i < 31; i++)
        {
            mClient[i]=mClient[i+1];
        }
    }
}