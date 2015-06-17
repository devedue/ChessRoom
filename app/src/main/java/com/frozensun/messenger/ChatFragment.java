package com.frozensun.messenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.frozensun.messengerInterface.Client;
import com.frozensun.messengerInterface.GUIClass;
import com.frozensun.messengerInterface.Server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ChatFragment extends Fragment implements View.OnClickListener
{
    private static final String ARG_NUMBER = "fragNum";
    private static final String ARG_CREATE = "createServer";
    private static final String ARG_PORT = "connectPort";
    private static final String ARG_IP = "connectIP";
    private static final String ARG_NAME = "personName";

    private static int mFragNum = 0;
    private String mName = "android";

    private LinearLayout mMessagesView;
    private ViewGroup mViewGroup;
    private Button mSendButton;
    private EditText mMessageBox;
    private ScrollView mScrollView;

    private LayoutInflater mInflater;

    public static ChatFragment newInstance(int pos, String name)
    {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, pos);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        Log.d("Start", "ChatFragment");
        return fragment;
    }

    public ChatFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mViewGroup = container;
        mInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        mSendButton = (Button) v.findViewById(R.id.send_button);
        mSendButton.setOnClickListener(this);
        mMessageBox = (EditText) v.findViewById(R.id.input_message);
        mMessagesView = (LinearLayout) v.findViewById(R.id.messages_frame);
        mScrollView = (ScrollView) v.findViewById(R.id.scroll_messages_frame);
        Log.d("ChatFragment", "Inflated View");
        if (getArguments() != null)
        {
            mFragNum = getArguments().getInt(ARG_NUMBER);
            mName = getArguments().getString(ARG_NAME);
        }
        LinkedList<String> messages = ((MainActivity)getActivity()).getMessages();
        for (int i=0;i<messages.size();i++)
        {
            Log.d("getting messages",""+messages.get(i) + ", " + messages.size());
            getText(messages.get(i));
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public void getText(final String string)
    {
        Log.d("TextProcessing ", "ChatFragment MESSAGE" + string);
        StringBuilder sb = new StringBuilder(string.replace("\t","\n"));
        final String name = sb.substring(sb.indexOf("<name>") + "<name>".length(), sb.indexOf("</name>"));
        final String message = sb.substring(sb.indexOf("<message>") + "<message>".length(), sb.indexOf("</message>"));
        if (message.length() > 0)
        {
            //33,150,243
            //45,118,183
            if (isAdded())
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mName.equals(name))
                    {
                        View m = mInflater.inflate(R.layout.message_view_item_self, mViewGroup, false);
                        TextView tv1 = (TextView) m.findViewById(R.id.text_name_s);
                        tv1.setText(name);
                        TextView tv2 = (TextView) m.findViewById(R.id.text_message_s);
                        tv2.setText(message);
                        mMessagesView.addView(m);
                    }
                    else
                    {
                        View m = mInflater.inflate(R.layout.message_view_item, mViewGroup, false);
                        TextView tv1 = (TextView) m.findViewById(R.id.text_name);
                        tv1.setText(name);
                        TextView tv2 = (TextView) m.findViewById(R.id.text_message);
                        tv2.setText(message);
                        mMessagesView.addView(m);
                    }
                }
            });
        }
        if (isAdded())
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Log.d("onClick", "" + v.getTag());
        if (v == mSendButton)
        {
            String s = "<func>MESSAGE</func><name>" + mName + "</name><message>" + mMessageBox.getText() + "</message>";
            s = s.replace("\n","\t");
            ((MainActivity) getActivity()).send(s);
            ((MainActivity) getActivity()).enqueueMessage(s);
            getText(s);
            mMessageBox.setText("");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(Uri uri);
    }

}
