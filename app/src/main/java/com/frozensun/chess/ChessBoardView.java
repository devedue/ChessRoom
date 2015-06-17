package com.frozensun.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.frozensun.messenger.ChessFragment;
import com.frozensun.messenger.R;

public class ChessBoardView extends View
{
    Context mContext;

    ChessFragment mFragment;

    Drawable mChessPieces[] = new Drawable[12];
    Drawable mChessBoard = null;

    float mTouchX;
    float mTouchY;

    int mCanvasHeight;
    int mCanvasWidth;
    int mBlockSize = 20;

    int mActiveX = -1;
    int mActiveY = -1;

    char mBoard[][] = {
            {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'},
            {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'},
            {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'},
            {'K', 'P', ' ', ' ', ' ', ' ', 'p', 'k'},
            {'Q', 'P', ' ', ' ', ' ', ' ', 'p', 'q'},
            {'B', 'P', ' ', ' ', ' ', ' ', 'p', 'b'},
            {'N', 'P', ' ', ' ', ' ', ' ', 'p', 'n'},
            {'R', 'P', ' ', ' ', ' ', ' ', 'p', 'r'}};

    boolean mTurn = true;

    private void init(AttributeSet attrs, int defStyle)
    {
        mCanvasWidth = getWidth();
        mCanvasHeight = getHeight();
        mChessPieces[0] = mContext.getResources().getDrawable(R.drawable.bp);
        mChessPieces[1] = mContext.getResources().getDrawable(R.drawable.br);
        mChessPieces[2] = mContext.getResources().getDrawable(R.drawable.bn);
        mChessPieces[3] = mContext.getResources().getDrawable(R.drawable.bb);
        mChessPieces[4] = mContext.getResources().getDrawable(R.drawable.bq);
        mChessPieces[5] = mContext.getResources().getDrawable(R.drawable.bk);

        mChessPieces[6] = mContext.getResources().getDrawable(R.drawable.wp);
        mChessPieces[7] = mContext.getResources().getDrawable(R.drawable.wr);
        mChessPieces[8] = mContext.getResources().getDrawable(R.drawable.wn);
        mChessPieces[9] = mContext.getResources().getDrawable(R.drawable.wb);
        mChessPieces[10] = mContext.getResources().getDrawable(R.drawable.wq);
        mChessPieces[11] = mContext.getResources().getDrawable(R.drawable.wk);

        mChessBoard = mContext.getResources().getDrawable(R.drawable.chess_board);

        setupBoard();
    }

    public void setupBoard()
    {
    }

    public void drawActivePiece(Canvas canvas)
    {
        int im = 0;
        int x = mActiveX, y = mActiveY;
        switch (mBoard[x][y])
        {
            case 'P':
            case 'p':
                im = 0;
                break;
            case 'R':
            case 'r':
                im = 1;
                break;
            case 'N':
            case 'n':
                im = 2;
                break;
            case 'B':
            case 'b':
                im = 3;
                break;
            case 'Q':
            case 'q':
                im = 4;
                break;
            case 'K':
            case 'k':
                im = 5;
                break;
            default:
                im = -1;
        }
        if (Character.isLowerCase(mBoard[x][y]))
            im += 6;
        if (im >= 0)
        {
            mChessPieces[im].setBounds((int) mTouchX - (mBlockSize / 2) - 3, (int) mTouchY - (mBlockSize / 2) - 3, (int) mTouchX + (mBlockSize / 2) + 3, (int) mTouchY + +(mBlockSize / 2) + 3);
            mChessPieces[im].draw(canvas);
        }
    }

    public void drawBoard(Canvas canvas)
    {//p,r,n,b,q,k
        //60,55 - 1015, 920
        mBlockSize = (canvas.getWidth() / 9);
        mChessBoard.setBounds(0, 0, canvas.getWidth(), canvas.getWidth());
        mChessBoard.draw(canvas);
        int x, y;
        int im;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (mActiveX == i && mActiveY == j)
                    continue;
                x = calcC(i);
                y = calcC(j);
                switch (mBoard[i][j])
                {
                    case 'P':
                    case 'p':
                        im = 0;
                        break;
                    case 'R':
                    case 'r':
                        im = 1;
                        break;
                    case 'N':
                    case 'n':
                        im = 2;
                        break;
                    case 'B':
                    case 'b':
                        im = 3;
                        break;
                    case 'Q':
                    case 'q':
                        im = 4;
                        break;
                    case 'K':
                    case 'k':
                        im = 5;
                        break;
                    default:
                        im = -1;
                }
                if (im == -1)
                    continue;
                if (Character.isLowerCase(mBoard[i][j]))
                    im += 6;
                mChessPieces[im].setBounds(x, y, x + mBlockSize, y + mBlockSize);
                mChessPieces[im].draw(canvas);
            }
        }
    }

    public int calcC(int x)
    {
        return (x * mBlockSize) + (mBlockSize / 2);
    }

    public ChessBoardView(Context context)
    {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public ChessBoardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public ChessBoardView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }


    @Override
    protected void onMeasure(int h, int w)
    {
        mCanvasWidth = w;
        mCanvasHeight = h;
        setMeasuredDimension(h, w);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBoard(canvas);
        if (mActiveY >= 0 && mActiveX >= 0 && mActiveY <= 7 && mActiveX <= 7)
            drawActivePiece(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float x = e.getX(), y = e.getY();
        mTouchX = x;
        mTouchY = y;
        Log.d("touchEvent", "x = " + x + "y = " + y);
        if (mFragment.getColor()!=mTurn)
        {
            return false;
        }
        if (e.getAction() == MotionEvent.ACTION_DOWN)
        {
            mActiveX = getPos(x);
            mActiveY = getPos(y);
            if ((Character.isLowerCase(mBoard[mActiveX][mActiveY])&&!mFragment.getColor())||(Character.isUpperCase(mBoard[mActiveX][mActiveY])&&mFragment.getColor()))
            {
                mActiveX=-1;
                mActiveY=-1;
                return false;
            }
        }
        if (e.getAction() == MotionEvent.ACTION_UP)
        {
            makeMove(e.getX(), e.getY());
            mActiveY = -1;
            mActiveX = -1;
        }
        invalidate();
        postInvalidate();
        return true;
    }

    public int getPos(float x)
    {
        int X = ((int) (x - (mBlockSize / 2) - 1) / mBlockSize);
        return X;
    }

    public void makeMove(float x, float y)
    {
        int xx = getPos(x), yy = getPos(y);
        Log.d("makemove", mActiveX + ", " + mActiveY + " to " + xx + ", " + yy);
        if ((xx >= 0 && xx <= 7 && yy >= 0 && yy <= 7) && (mActiveY >= 0 && mActiveX >= 0 && mActiveY <= 7 && mActiveX <= 7))
        {
            if (ChessClass.legality(mActiveX, mActiveY, mBoard, xx, yy, mTurn))
            {
                mTurn=!mTurn;
                mFragment.enqueueMoves(mActiveX,mActiveY,xx,yy);
                mFragment.sendMove(mActiveX,mActiveY,xx,yy);
                for (int i = 0; i < 8; i++)
                {
                    String a = "";
                    for (int j = 0; j < 8; j++)
                    {
                        a = a + " " + mBoard[i][j];
                    }
                    Log.d("makemove CheckBoard", a);
                }
                Log.d("makemove", mActiveX + ", " + mActiveY + " to " + xx + ", " + yy);
            }
        }
    }

    public void setFragment(ChessFragment cf)
    {
        mFragment = cf;
    }

    public void forceMove(int x, int y, int dx, int dy)
    {
        Log.d("makemove",x + ", " + y + ", " + dx + ", " + dy);
        mBoard[dx][dy]=mBoard[x][y];
        mBoard[x][y]=' ';
        mTurn=!mTurn;
        postInvalidate();
    }

    public char[][] getBoard()
    {
        return mBoard;
    }

    public void setBoard(char board[][])
    {
        mBoard = board;
    }

    public char getPiece(int x, int y)
    {
        return mBoard[x][y];
    }

    public boolean getTurn()
    {
        return mTurn;
    }
}
