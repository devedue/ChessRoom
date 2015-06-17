package com.frozensun.messenger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.frozensun.messengerInterface.Client;
import com.frozensun.messengerInterface.GUIClass;
import com.frozensun.messengerInterface.Server;

import java.io.IOException;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ClientListDrawerFragment.ClientListDrawerCallbacks, GUIClass
{
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ClientListDrawerFragment mClientListDrawerFragment;
    private LinkedList<String> mListMessages = new LinkedList<>();

    private class LinkedArray extends LinkedList<LinkedList<Integer>> {}

    private LinkedArray mListMoves[] = new LinkedArray[32];

    private CharSequence mTitle;
    private ChatFragment mChatFragment;
    private ChessFragment mChessFragments[] = new ChessFragment[32];

    private String mClientList[][] = new String[32][2];
    private String mGameList[][] = new String[32][2];
    private Server mServer;
    private Client mClient;

    private int mActiveFrag = -1;

    String mMyIP = "192.168.2.13";
    String mIP = "192.168.2.13";
    int mPort = 56777;
    String mName = "Android";
    boolean mCreate = false;
    boolean mAllSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClientListDrawerFragment = (ClientListDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.client_list_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mClientListDrawerFragment.setUp(
                R.id.client_list_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        for (int i = 0; i < 32; i++)
        {
            mListMoves[i] = new LinkedArray();
        }

        try
        {
            if (mCreate)
            {
                mServer = new Server(mPort, null, mName, null, this);
                mServer.setGUI(this);
                mClientList[0][0] = mName;
                mClientList[0][1] = getIP();
                updateClientList(mClientList);
                getText("<func>MESSAGE</func><name>Status</name><message>Server Created at " + mClientList[0][1] + ":" + mPort + "</message>");
                Log.d("Server", "Created");
            }
            else
            {
                new Client(mIP, mPort, mName, null, this, 0);
            }
        } catch (IOException e)
        {
            Log.d("Server", "e: " + e);
        }
    }

    @Override
    public void setConnectionFailure()
    {
        final Context context = this;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, "Failed to connect to " + mIP + ":" + mPort, Toast.LENGTH_LONG).show();
            }
        });
        this.finish();
    }

    @Override
    public void setServerFailure()
    {
        final Context context = this;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, mPort + " already in use\nPlease Choose another port", Toast.LENGTH_LONG).show();
            }
        });
        this.finish();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        closeServers();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d("Clicked", "" + position);
        if (position != mActiveFrag)
        {
            switch (position)
            {
                case 0:
                    if (mAllSet)
                    {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        mChatFragment).commit();
                    }
                    else
                    {
                        mIP = getIntent().getStringExtra("IP");
                        mPort = getIntent().getIntExtra("Port", 56777);
                        mName = getIntent().getStringExtra("Name");
                        mCreate = getIntent().getBooleanExtra("Create", false);
                        mMyIP = getIntent().getStringExtra("myIP");
                        mAllSet = true;
                        mChatFragment = ChatFragment.newInstance(position + 1, mName);
                        mChatFragment.setRetainInstance(true);
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container,
                                        mChatFragment).commit();
                    }
                    break;
                default:
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.container,
                                    mChessFragments[position]).commit();
            }
        }
        mActiveFrag = position;
    }

    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() == 0)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Disconnection")
                    .setMessage("Are you sure you want to close and Disconnect?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


    public void onSectionAttached(int number)
    {
        switch (number)
        {
            case 1:
                mTitle = "Chat Box";
                break;
        }
    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClientListDrawerItemSelected(int position)
    {
        if (!getIP().equals(mClientListDrawerFragment.getIP(position)))
        {
            String ip = getIP();
            Log.d("ChatProcessing", "ChatProcessing " + ip);
            sendToAll("<func>CHESS</func><type>REQUEST</type><to>" + mClientListDrawerFragment.getIP(position) + "</to><from>" + ip + "</from><name>" + mName + "</name>");
        }
    }


    public void updateClientList(final String[][] c)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mClientListDrawerFragment.updateList(c);
            }
        });
    }

    public void closeServers()
    {
        try
        {
            mServer.sendToAll("<func>DELCLIENT</func><ip>" + mMyIP + "</ip>", -1);
            mServer.destroy();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendToAll(String string)
    {
        mServer.sendToAll(string, -1);
    }

    public void createChessGame(final String name, final String oname, boolean color, String white, String black)
    {
        mGameList[mNavigationDrawerFragment.getSize()][0] = "Chess";
        mGameList[mNavigationDrawerFragment.getSize()][1] = oname;
        mChessFragments[mNavigationDrawerFragment.getSize()] = ChessFragment.newInstance(mNavigationDrawerFragment.getSize(), color, name, oname, white, black);
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(getApplicationContext(), "Game Created\nSwipe right to access", Toast.LENGTH_LONG).show();
                mNavigationDrawerFragment.addGame("Chess", oname);
            }
        });
    }

    public void makeMove(String wip, String bip, final int x, final int y, final int dx, final int dy)
    {
        Log.d("makemove", "match" + wip + ", " + bip + " " + x + ", " + y + ", " + dx + ", " + dy + ", frags " + mChessFragments.length);
        for (int i = 0; i < mChessFragments.length; i++)
        {
            if (mChessFragments[i] != null)
            {
                if (mChessFragments[i].getmBlackIP().equals(bip) && mChessFragments[i].getmWhiteIP().equals(wip))
                {
                    Log.d("savemove", "match " + i);
                    if (mChessFragments[i].isVisible())
                        mChessFragments[i].makeMove(x, y, dx, dy);
                    enqueueMoves(x, y, dx, dy, i);
                    break;
                }
            }
        }
    }

    public void send(String string)
    {
        if (mServer != null)
            mServer.send(string);
    }

    @Override
    public void getText(String string)
    {
        Log.d("TextProcessing ", "MainActivity processed " + string);
        StringBuilder sb = new StringBuilder(string);
        if (sb.indexOf("<func>") >= 0 && sb.indexOf("</func>") >= 0)
        {
            if (sb.substring(sb.indexOf("<func>") + "<func>".length(), sb.indexOf("</func>")).contains("MESSAGE"))
            {
                if (mChatFragment.isVisible())
                    mChatFragment.getText(string);
                enqueueMessage(string);
            }
            else if (sb.substring(sb.indexOf("<func>") + "<func>".length(), sb.indexOf("</func>")).equals("NEWCLIENT"))
            {
                final String name = sb.substring(sb.indexOf("<name>") + "<name>".length(), sb.indexOf("</name>"));
                final String ip = sb.substring(sb.indexOf("<ip>") + "<ip>".length(), sb.indexOf("</ip>"));
                int si = mServer.getSize();
                Log.d("ChatProcessing ", "NEWCLIENT size = " + si + ", " + name + ", " + ip);
                mClientList[si][0] = name;
                mClientList[si][1] = ip;
                updateClientList(mClientList);
                mServer.sendToAll("<func>DATABASENEW</func>", -1);
                for (int i = 0; i <= si; i++)
                {
                    mServer.sendToAll("<func>DATABASE</func>"
                            + "<number>" + i + "</number>"
                            + "<name>" + mClientList[i][0] + "</name>"
                            + "<ip>" + mClientList[i][1] + "</ip>", -1);
                }
            }
            else if (sb.substring(sb.indexOf("<func>") + "<func>".length(), sb.indexOf("</func>")).equals("DATABASE"))
            {
                Log.d("ChatProcessing ", "DATABASE");
                final int number = Integer.parseInt(sb.substring(sb.indexOf("<number>") + "<number>".length(), sb.indexOf("</number>")));
                final String name = sb.substring(sb.indexOf("<name>") + "<name>".length(), sb.indexOf("</name>"));
                final String ip = sb.substring(sb.indexOf("<ip>") + "<ip>".length(), sb.indexOf("</ip>"));
                mClientList[number][0] = name;
                mClientList[number][1] = ip;
                updateClientList(mClientList);
            }
            else if (sb.substring(sb.indexOf("<func>") + "<func>".length(), sb.indexOf("</func>")).equals("DELCLIENT"))
            {
                final String ip = sb.substring(sb.indexOf("<ip>") + "<ip>".length(), sb.indexOf("</ip>"));
                if (mClientList[0][1].equals(ip))
                {
                    if (mMyIP.equals(mClientList[1][1]))
                    {
                        mCreate = true;
                        try
                        {
                            mServer = new Server(mPort, null, mName, null, this);
                        } catch (IOException e)
                        {

                        }
                        mServer.setGUI(this);
                        removeClient(ip);
                        mClientList[0][0] = mName;
                        mClientList[0][1] = getIP();
                        updateClientList(mClientList);
                        getText("<func>MESSAGE</func><name>Status</name><message>Server Created at " + mClientList[0][1] + ":" + mPort + "</message>");
                        Log.d("Server", "Created");
                    }
                    else
                    {
                        mIP = mClientList[1][1];
                        try
                        {
                            new Client(mIP, mPort, mName, null, this, 0);
                        } catch (IOException e)
                        {
                        }
                    }
                }
                else
                    removeClient(ip);
            }
            else if (sb.substring(sb.indexOf("<func>") + "<func>".length(), sb.indexOf("</func>")).equals("DATABASENEW"))
            {
                Log.d("ChatProcessing ", "DATABASENEW");
                resetDatabase();
            }
            else if (sb.substring(sb.indexOf("<func>") + "<func>".length(), sb.indexOf("</func>")).equals("CHESS"))
            {
                if (sb.substring(sb.indexOf("<type>") + "<type>".length(), sb.indexOf("</type>")).equals("REQUEST"))
                {
                    Log.d("ChatProcessing", "ChatProcessing REQUEST*" + sb.substring(sb.indexOf("<to>") + "<to>".length(), sb.indexOf("</to>")) + ", " + mMyIP);
                    if (sb.substring(sb.indexOf("<to>") + "<to>".length(), sb.indexOf("</to>")).equals(mMyIP))
                    {
                        final String name = sb.substring(sb.indexOf("<name>") + "<name>".length(), sb.indexOf("</name>"));
                        final String ip = sb.substring(sb.indexOf("<from>") + "<from>".length(), sb.indexOf("</from>"));
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                new AlertDialog.Builder(MainActivity.this).setTitle("Chess Request").setMessage(
                                        name + " challenges you to a game of chess. So you Accept?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                mServer.sendToAll("<func>CHESS</func><type>REPLY</type><to>" + ip + "</to><from>" + mName + "</from><fromip>" + mMyIP + "</fromip><answer>YES</answer>", -1);
                                                createChessGame(mName, name, false, ip, mMyIP);
                                            }
                                        })
                                        .setNegativeButton("No",
                                                new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(DialogInterface dialog,
                                                                        int which)
                                                    {
                                                        mServer.sendToAll("<func>CHESS</func><type>REPLY</type><to>" + ip + "</to><from>" + mName + "</from><fromip>" + mMyIP + "</fromip><answer>NO</answer>", -1);
                                                        dialog.cancel();
                                                    }
                                                }).show();
                            }
                        });

                    }
                    else if (mCreate)
                    {
                        sendToAll(sb.toString());
                    }
                }
                else if (sb.substring(sb.indexOf("<type>") + "<type>".length(), sb.indexOf("</type>")).equals("REPLY"))
                {
                    if (sb.substring(sb.indexOf("<to>") + "<to>".length(), sb.indexOf("</to>")).equals(getIP()))
                    {
                        String name = sb.substring(sb.indexOf("<from>") + "<from>".length(), sb.indexOf("</from>"));
                        String answer = sb.substring(sb.indexOf("<answer>") + "<answer>".length(), sb.indexOf("</answer>"));
                        String ip = sb.substring(sb.indexOf("<fromip>") + "<fromip>".length(), sb.indexOf("</fromip>"));
                        if (answer.equals("YES"))
                        {
                            createChessGame(mName, name, true, mMyIP, ip);
                        }
                    }
                    else if (mCreate)
                    {
                        sendToAll(sb.toString());
                    }
                }
                else if (sb.substring(sb.indexOf("<type>") + "<type>".length(), sb.indexOf("</type>")).equals("MOVE"))
                {
                    if (sb.substring(sb.indexOf("<to>") + "<to>".length(), sb.indexOf("</to>")).equals(getIP()))
                    {
                        String wip = sb.substring(sb.indexOf("<white>") + "<white>".length(), sb.indexOf("</white>"));
                        String bip = sb.substring(sb.indexOf("<black>") + "<black>".length(), sb.indexOf("</black>"));
                        int x = Integer.parseInt(sb.substring(sb.indexOf("<x>") + "<x>".length(), sb.indexOf("</x>")));
                        int dx = Integer.parseInt(sb.substring(sb.indexOf("<dx>") + "<dx>".length(), sb.indexOf("</dx>")));
                        int y = Integer.parseInt(sb.substring(sb.indexOf("<y>") + "<y>".length(), sb.indexOf("</y>")));
                        int dy = Integer.parseInt(sb.substring(sb.indexOf("<dy>") + "<dy>".length(), sb.indexOf("</dy>")));
                        makeMove(wip, bip, x, y, dx, dy);
                    }
                    else if (mCreate)
                    {
                        sendToAll(sb.toString());
                    }
                }
            }
        }
    }


    public void removeClient(final String ip)
    {
        onNavigationDrawerItemSelected(0);
        for (int i = 0; i < 32; i++)
        {
            if (mClientList[i][1].equals(ip))
            {
                mServer.removeClient(i);
                for (int j = i; j < 31; j++)
                {
                    mClientList[j] = mClientList[j + 1];
                }
                break;
            }
        }
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mClientListDrawerFragment.updateList(mClientList);
            }
        });
        for (int i = 0; i < 32; i++)
        {
            final int k = i;
            if (mChessFragments[i] != null)
            {
                if (mChessFragments[i].getmBlackIP().equals(ip) || mChessFragments[i].getmWhiteIP().equals(ip))
                {
                    for (int j = i; j < 31; j++)
                    {
                        mGameList[j] = mGameList[j + 1];
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mNavigationDrawerFragment.remove(k);
                        }
                    });
                    break;
                }
            }
        }
    }

    public void resetDatabase()
    {
        mClientList = new String[32][2];
    }

    @Override
    public void setClient(Client client)
    {
        mClient = client;
    }

    @Override
    public void setServer(Server server)
    {
        mServer = server;
    }

    public String getIP()
    {
        return mMyIP;
    }

    public String getPort()
    {
        return ("" + mPort);
    }

    public void enqueueMessage(String s)
    {
        mListMessages.add(s);
    }

    public void enqueueMoves(int x, int y, int dx, int dy, int num)
    {
        Log.d("savemove", "num " + num);
        LinkedList<Integer> a = new LinkedList<>();
        a.add(x);
        a.add(y);
        a.add(dx);
        a.add(dy);
        mListMoves[num].add(a);
    }

    public LinkedList<String> getMessages()
    {
        return mListMessages;
    }

    public LinkedList<LinkedList<Integer>> getMoves(int num)
    {
        Log.d("savemove", "match " + num);
        return mListMoves[num];
    }

}
