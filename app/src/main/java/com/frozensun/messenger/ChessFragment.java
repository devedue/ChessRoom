package com.frozensun.messenger;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.frozensun.chess.ChessBoardView;
import com.frozensun.messengerInterface.Client;
import com.frozensun.messengerInterface.Server;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChessFragment extends Fragment implements View.OnTouchListener
{
    private static final String ARG_NUMBER = "fragNum";
    private static final String ARG_COLOR = "color";
    private static final String ARG_OPNAME = "opponentName";
    private static final String ARG_NAME = "personName";
    private static final String ARG_BIP = "blackIP";
    private static final String ARG_WIP = "whiteIP";

    private int mFragNum;
    private String mName = "android";
    private String mOpName = "android";
    private String mWhiteIP = "";
    private String mBlackIP = "";

    private TextView mTurnView;

    private LayoutInflater mInflater;
    private ViewGroup mViewGroup;

    private Context mContext;

    private boolean mColor = false;

    private ChessBoardView mChessBoardView;

    public static ChessFragment newInstance(int pos, boolean color, String name, String oname, String wip, String bip)
    {
        ChessFragment fragment = new ChessFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, pos);
        args.putBoolean(ARG_COLOR, color);
        args.putString(ARG_NAME, name);
        args.putString(ARG_OPNAME, oname);
        args.putString(ARG_BIP, bip);
        args.putString(ARG_WIP, wip);
        fragment.setArguments(args);
        Log.d("Start", "ChessFragment");
        return fragment;
    }

    public ChessFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("FragmentCycle", "ChessFragment onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        Log.d("FragmentCycle", "ChessFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        setRetainInstance(true);
        Log.d("FragmentCycle", "ChessFragment onCreateView");
        mViewGroup = container;
        mInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_chess, container, false);
        mTurnView = (TextView) v.findViewById(R.id.whose_turn_text);
        ChessBoardView cbv = (ChessBoardView) v.findViewById(R.id.chess_board);
        cbv.setFocusable(true);
        cbv.setFragment(this);
        mChessBoardView = cbv;
        Log.d("ChessFragment", "Inflated View");
        if (getArguments() != null)
        {
            mFragNum = getArguments().getInt(ARG_NUMBER);
            mColor = getArguments().getBoolean(ARG_COLOR);
            mName = getArguments().getString(ARG_NAME);
            mOpName = getArguments().getString(ARG_OPNAME);
            mBlackIP = getArguments().getString(ARG_BIP);
            mWhiteIP = getArguments().getString(ARG_WIP);
        }
        if (mColor)
            mTurnView.setText("Your Turn");
        else
            mTurnView.setText("Waiting for Opponent");

        LinkedList<LinkedList<Integer>> moves = ((MainActivity) getActivity()).getMoves(mFragNum);
        for (int i = 0; i < moves.size(); i++)
        {
            makeMove(moves.get(i).get(0), moves.get(i).get(1), moves.get(i).get(2), moves.get(i).get(3));
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity)
    {
        Log.d("FragmentCycle", "ChessFragment onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onDetach()
    {
        Log.d("FragmentCycle", "ChessFragment onDetach");
        super.onDetach();
    }

    public void makeMove(final int x, final int y, final int dx, final int dy)
    {
        Log.d("makemove", x + ", " + y + ", " + dx + ", " + dy);
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mChessBoardView.forceMove(x, y, dx, dy);
                if (mColor == mChessBoardView.getTurn())
                    mTurnView.setText("Your Turn");
                else
                    mTurnView.setText("Waiting for opponent");
            }
        });
    }

    public void enqueueMoves(final int x, final int y, final int dx, final int dy)
    {
        ((MainActivity)getActivity()).enqueueMoves(x,y,dx,dy,mFragNum);
    }

    public void sendMove(int x, int y, int dx, int dy)
    {
        String move = "<func>CHESS</func><type>MOVE</type><to>" + (mColor ? mBlackIP : mWhiteIP) + "</to><white>" + mWhiteIP + "</white><black>" + mBlackIP + "</black><x>" + x + "</x><y>" + y + "</y><dx>" + dx + "</dx><dy>" + dy + "</dy>";
        ((MainActivity) getActivity()).send(move);
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (mColor == mChessBoardView.getTurn())
                    mTurnView.setText("Your Turn");
                else
                    mTurnView.setText("Waiting for opponent");
            }
        });
    }


    public void setChessBoardView(ChessBoardView v)
    {
        mChessBoardView = v;
        if (mColor == mChessBoardView.getTurn())
            mTurnView.setText("Your Turn");
        else
            mTurnView.setText("Waiting for opponent");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        Log.d("", "" + event.getX() + " , " + event.getY());
        return false;
    }

    public String getmBlackIP()
    {
        return mBlackIP;
    }

    public String getmWhiteIP()
    {
        return mWhiteIP;
    }

    public boolean getColor()
    {
        return mColor;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
