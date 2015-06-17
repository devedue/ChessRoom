package com.frozensun.chess;

import android.util.Log;

/**
 * Created by Devedue on 29-05-2015.
 */
public class ChessClass extends Thread
{
    public static boolean getc(char a)
    {
        if (Character.isLowerCase(a))
            return true;
        else
            return false;
    }

    public static int[][] getBishop(int x, int y, char[][] board)
    {
        int moves[][] = new int[64][2];
        for (int i = 0; i < 64; i++)
        {
            moves[i][0] = -1;
            moves[i][1] = -1;
        }
        int m = 0;
        for (int s = -1; s <= 1; s += 2)
        {
            for (int s2 = -1; s2 <= 1; s2 += 2)
            {
                for (int i = x + s, j = y + s2; i != -1 && i != 8 && j != 8 && j != -1; i += s, j += s2)
                {
                    if (i < 0 || i > 7||j < 0 || j > 7)
                        break;
                    if (board[i][j] == ' ')
                    {
                        moves[m][0] = i;
                        moves[m][1] = j;
                        m++;
                    }
                    if (board[i][j] != ' ')
                    {
                        if (getc(board[i][j]) != getc(board[x][y]))
                        {
                            moves[m][0] = i;
                            moves[m][1] = j;
                            m++;
                        }
                        break;
                    }
                }
            }
        }
        return moves;
    }

    public static int[][] getRook(int x, int y, char[][] board)
    {
        int moves[][] = new int[64][2];
        for (int i = 0; i < 64; i++)
        {
            moves[i][0] = -1;
            moves[i][1] = -1;
        }
        int m = 0;
        for (int s = -1; s <= 1; s += 2)
        {
            for (int i = x + s, j = y; i != -1 && i != 8 && j != 8 && j != -1; i += s)
            {
                if (i < 0 || i > 7)
                    break;
                if (board[i][j] == ' ')
                {
                    moves[m][0] = i;
                    moves[m][1] = j;
                    m++;
                }
                if (board[i][j] != ' ')
                {
                    if (getc(board[i][j]) != getc(board[x][y]))
                    {
                        moves[m][0] = i;
                        moves[m][1] = j;
                        m++;
                    }
                    break;
                }
            }
            for (int i = x, j = y + s; i != -1 && i != 8 && j != 8 && j != -1; j += s)
            {
                if (j<0||j>7)
                    break;
                if (board[i][j] == ' ')
                {
                    moves[m][0] = i;
                    moves[m][1] = j;
                    m++;
                }
                if (board[i][j] != ' ')
                {
                    if (getc(board[i][j]) != getc(board[x][y]))
                    {
                        moves[m][0] = i;
                        moves[m][1] = j;
                        m++;
                    }
                    break;
                }
            }
        }

        return moves;
    }

    public static int[][] getQueen(int x, int y, char[][] board)
    {
        int moves[][] = new int[64][2];
        for (int i = 0; i < 64; i++)
        {
            moves[i][0] = -1;
            moves[i][1] = -1;
        }
        int m = 0;
        for (int s = -1; s <= 1; s += 2)
        {
            for (int s2 = -1; s2 <= 1; s2 += 2)
            {
                for (int i = x + s, j = y + s2; i != -1 && i != 8 && j != 8 && j != -1; i += s, j += s2)
                {
                    if (j<0||j>7||i<0||i>7)
                        break;
                    if (board[i][j] == ' ')
                    {
                        moves[m][0] = i;
                        moves[m][1] = j;
                        m++;
                    }
                    if (board[i][j] != ' ')
                    {
                        if (getc(board[i][j]) != getc(board[x][y]))
                        {
                            moves[m][0] = i;
                            moves[m][1] = j;
                            m++;
                        }
                        break;
                    }
                }
            }
        }
        for (int s = -1; s <= 1; s += 2)
        {
            for (int i = x + s, j = y; i != -1 && i != 8 && j != 8 && j != -1; i += s)
            {
                if (i<0||i>7)
                    break;
                if (board[i][j] == ' ')
                {
                    moves[m][0] = i;
                    moves[m][1] = j;
                    m++;
                }
                if (board[i][j] != ' ')
                {
                    if (getc(board[i][j]) != getc(board[x][y]))
                    {
                        moves[m][0] = i;
                        moves[m][1] = j;
                        m++;
                    }
                    break;
                }
            }
            for (int i = x, j = y + s; i != -1 && i != 8 && j != 8 && j != -1; j += s)
            {
                if (j<0||j>7)
                    break;
                if (board[i][j] == ' ')
                {
                    moves[m][0] = i;
                    moves[m][1] = j;
                    m++;
                }
                if (board[i][j] != ' ')
                {
                    if (getc(board[i][j]) != getc(board[x][y]))
                    {
                        moves[m][0] = i;
                        moves[m][1] = j;
                        m++;
                    }
                    break;
                }
            }
        }
        return moves;
    }

    public static int[][] getKing(int x, int y, char[][] board)
    {
        int moves[][] = new int[64][2];
        for (int i = 0; i < 64; i++)
        {
            moves[i][0] = -1;
            moves[i][1] = -1;
        }
        int m = 0;
        if (x - 1 >= 0 && y - 1 >= 0)
            if (board[x - 1][y - 1] == ' ' || getc(board[x][y]) != getc(board[x - 1][y - 1]))
            {
                moves[m][0] = x - 1;
                moves[m][1] = y - 1;
                m++;
            }
        if (x - 1 >= 0)
            if (board[x - 1][y] == ' ' || getc(board[x][y]) != getc(board[x - 1][y]))
            {
                moves[m][0] = x - 1;
                moves[m][1] = y;
                m++;
            }
        if (x - 1 >= 0 && y + 1 <= 7)
            if (board[x - 1][y + 1] == ' ' || getc(board[x][y]) != getc(board[x - 1][y + 1]))
            {
                moves[m][0] = x - 1;
                moves[m][1] = y + 1;
                m++;
            }
        if (y - 1 >= 0)
            if (board[x][y - 1] == ' ' || getc(board[x][y]) != getc(board[x][y - 1]))
            {
                moves[m][0] = x;
                moves[m][1] = y - 1;
                m++;
            }
        if (y + 1 <= 7)
            if (board[x][y + 1] == ' ' || getc(board[x][y]) != getc(board[x][y + 1]))
            {
                moves[m][0] = x;
                moves[m][1] = y + 1;
                m++;
            }
        if (x + 1 <= 7 && y - 1 >= 0)
            if (board[x + 1][y - 1] == ' ' || getc(board[x][y]) != getc(board[x + 1][y - 1]))
            {
                moves[m][0] = x + 1;
                moves[m][1] = y - 1;
                m++;
            }
        if (x + 1 <= 7)
            if (board[x + 1][y] == ' ' || getc(board[x][y]) != getc(board[x + 1][y]))
            {
                moves[m][0] = x + 1;
                moves[m][1] = y;
                m++;
            }
        if (x + 1 <= 7 && y + 1 <= 7)
            if (board[x + 1][y + 1] == ' ' || getc(board[x][y]) != getc(board[x + 1][y + 1]))
            {
                moves[m][0] = x + 1;
                moves[m][1] = y + 1;
                m++;
            }
        return moves;
    }

    public static int[][] getKnight(int x, int y, char[][] board)
    {
        int moves[][] = new int[64][2];
        for (int i = 0; i < 64; i++)
        {
            moves[i][0] = -1;
            moves[i][1] = -1;
        }
        int m = 0;
        if (x - 1 >= 0 && y - 2 >= 0)
            if (board[x - 1][y - 2] == ' ' || getc(board[x][y]) != getc(board[x - 1][y - 2]))
            {
                moves[m][0] = x - 1;
                moves[m][1] = y - 2;
                m++;
            }
        if (x + 1 <= 7 && y - 2 >= 0)
            if (board[x + 1][y - 2] == ' ' || getc(board[x][y]) != getc(board[x + 1][y - 2]))
            {
                moves[m][0] = x + 1;
                moves[m][1] = y - 2;
                m++;
            }
        if (x - 2 >= 0 && y - 1 >= 0)
            if (board[x - 2][y - 1] == ' ' || getc(board[x][y]) != getc(board[x - 2][y - 1]))
            {
                moves[m][0] = x - 2;
                moves[m][1] = y - 1;
                m++;
            }
        if (x + 2 <= 7 && y - 1 >= 0)
            if (board[x + 2][y - 1] == ' ' || getc(board[x][y]) != getc(board[x + 2][y - 1]))
            {
                moves[m][0] = x + 2;
                moves[m][1] = y - 1;
                m++;
            }
        if (x - 1 >= 0 && y + 2 <= 7)
            if (board[x - 1][y + 2] == ' ' || getc(board[x][y]) != getc(board[x - 1][y + 2]))
            {
                moves[m][0] = x - 1;
                moves[m][1] = y + 2;
                m++;
            }
        if (x + 1 <= 7 && y + 2 <= 7)
            if (board[x + 1][y + 2] == ' ' || getc(board[x][y]) != getc(board[x + 1][y + 2]))
            {
                moves[m][0] = x + 1;
                moves[m][1] = y + 2;
                m++;
            }
        if (x - 2 >= 0 && y + 1 <= 7)
            if (board[x - 2][y + 1] == ' ' || getc(board[x][y]) != getc(board[x - 2][y + 1]))
            {
                moves[m][0] = x - 2;
                moves[m][1] = y + 1;
                m++;
            }
        if (x + 2 <= 7 && y + 1 <= 7)
            if (board[x + 2][y + 1] == ' ' || getc(board[x][y]) != getc(board[x + 2][y + 1]))
            {
                moves[m][0] = x + 2;
                moves[m][1] = y + 1;
                m++;
            }
        return moves;
    }

    public static int[][] getPawn(int x, int y, char[][] board)
    {
        int moves[][] = new int[64][2];
        for (int i = 0; i < 64; i++)
        {
            moves[i][0] = -1;
            moves[i][1] = -1;
        }
        int m = 0;
        if (getc(board[x][y]))
        {
            if (y - 1 >= 0)
                if (board[x][y - 1] == ' ')
                {
                    moves[m][0] = x;
                    moves[m][1] = y - 1;
                    m++;
                }
            if (y - 2 >= 0)
                if (board[x][y - 2] == ' ')
                {
                    moves[m][0] = x;
                    moves[m][1] = y - 2;
                    m++;
                }
            if (x - 1 >= 0 && y - 1 >= 0)
                if (getc(board[x][y]) != getc(board[x - 1][y - 1])&&board[x - 1][y - 1] != ' ')
                {
                    moves[m][0] = x - 1;
                    moves[m][1] = y - 1;
                    m++;
                }
            if (x + 1 <= 7 && y - 1 >= 0)
                if (getc(board[x][y]) != getc(board[x + 1][y - 1])&&board[x + 1][y - 1] != ' ')
                {
                    moves[m][0] = x + 1;
                    moves[m][1] = y - 1;
                    m++;
                }
        }
        else
        {
            if (y + 1 <= 7)
                if (board[x][y + 1] == ' ')
                {
                    moves[m][0] = x;
                    moves[m][1] = y + 1;
                    m++;
                }
            if (y + 2 <= 7)
                if (board[x][y + 2] == ' ')
                {
                    moves[m][0] = x;
                    moves[m][1] = y + 2;
                    m++;
                }
            if (x - 1 >= 0 && y + 1 <= 7)
                if (getc(board[x][y]) != getc(board[x - 1][y + 1])&&board[x - 1][y + 1] != ' ')
                {
                    moves[m][0] = x - 1;
                    moves[m][1] = y + 1;
                    m++;
                }
            if (x + 1 <= 7 && y + 1 <= 7)
                if (getc(board[x][y]) != getc(board[x + 1][y + 1])&&board[x + 1][y + 1] != ' ')
                {
                    moves[m][0] = x + 1;
                    moves[m][1] = y + 1;
                    m++;
                }
        }
        return moves;
    }

    public static boolean legality(int x, int y, char[][] board, int dx, int dy, boolean turn)
    {
        int kx = -1, ky = -1;
        boolean ret = false;
        char prevs = board[x][y],prevd = board[dx][dy];
        int moves[][] = getMoves(x, y, board);
        if (moves == null)
            return false;
        for (int i=0;i<64;i++)
        {
            if (moves[i][0]==-1)
                break;
            Log.d("makemove listmovesof" + board[x][y], moves[i][0] + ", " + moves[i][1]);
        }
        for (int i = 0; i < 64; i++)
        {
            if (moves[i][0] == -1)
            {
                ret = false;
                break;
            }
            if (moves[i][0] == dx && moves[i][1] == dy)
            {
                ret = true;
                break;
            }
        }
        Log.d("makemove","inittest " + ret);
        if (ret)
        {
            board[dx][dy] = board[x][y];
            board[x][y] = ' ';
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (turn && board[i][j] == 'k')
                    {
                        kx = i;
                        ky = j;
                        break;
                    }
                    if (!turn && board[i][j] == 'K')
                    {
                        kx = i;
                        ky = j;
                        break;
                    }
                }
                if (kx != -1 && ky != -1)
                    break;
            }
            Log.d("makemove","king at " + kx + ", " + ky);
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (turn && Character.isLowerCase(board[i][j]))
                        continue;
                    else if (!turn && Character.isUpperCase(board[i][j]))
                        continue;
                    if (board[i][j] == ' ')
                        continue;
                    else
                    {
                        moves = getMoves(i, j, board);
                        for (int k = 0; k < 64; k++)
                        {
                            if (moves[k][0] == -1)
                                continue;
                            if (moves[k][0] == kx && moves[k][1] == ky)
                            {
                                board[dx][dy] = prevd;
                                board[x][y] = prevs;
                                ret = false;
                                break;
                            }
                        }
                    }
                    if (!ret)
                        break;
                }
                if (!ret)
                    break;
            }
        }
        Log.d("makemove","kingtest " + ret);
        if (!ret)
        {
            board[dx][dy] = prevd;
            board[x][y] = prevs;
        }
        return ret;
    }

    public static int[][] getMoves(int x, int y, char[][] board)
    {
        switch (board[x][y])
        {
            case 'P':
            case 'p':
                return getPawn(x, y, board);
            case 'R':
            case 'r':
                return getRook(x, y, board);
            case 'N':
            case 'n':
                return getKnight(x, y, board);
            case 'B':
            case 'b':
                return getBishop(x, y, board);
            case 'Q':
            case 'q':
                return getQueen(x, y, board);
            case 'K':
            case 'k':
                return getKing(x, y, board);
            default:
                return null;
        }
    }
}
