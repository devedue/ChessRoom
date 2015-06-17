package com.frozensun.messengerInterface;

/**
 * Created by Devedue on 23-05-2015.
 */
public interface GUIClass
{
    void getText(String string);
    void setClient(Client client);
    void setServer(Server server);
    void setConnectionFailure();
    void setServerFailure();
    String getIP();
}
