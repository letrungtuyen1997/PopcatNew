package com.ss.gameLogic.objects;

import com.ss.gameLogic.config.Config.Direction;
import java.util.ArrayList;


class Tuple<X, Y> {
    public X obj;
    public Y delta;

    Tuple(X x, Y y){
        obj = x;
        delta = y;
    }
}

class D {
    public int dr;
    public int dc;

    D(int dr, int dc){
        this.dr = dr;
        this.dc = dc;
    }
}

public class Utils {

    public static <T> void square(T[][] input, ArrayList<T> output, T main, int row, int col){
        int[][] area = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {1, -1}, {-1, 1}};
        output.add(main);

        for (int i = 0; i < area.length; i++)
            if (Utils.isInBound(input, row + area[i][0], col + area[i][1]))
                    output.add(input[row + area[i][0]][col + area[i][1]]);

    }

    public static <T> void vertical(T[][] input, ArrayList<T> output, T main, int row, int col){
        output.add(main);

        for (int i = 0; i < input.length; i++)
            if (Utils.isInBound(input, i, col))
                output.add(input[i][col]);

    }

    public static <T> boolean isInBound(T[][] input, int row, int col) {
        if (row < 0 || row >= input.length || col < 0 || col >= input[0].length)
            return false;
        if (input[row][col] == null)
            return false;
        return true;
    }

    public static <T extends com.ss.interfaces.Comparable> void area(Pop[][] input, ArrayList<Pop> output, Pop main, int row, int col){
        if (row < 0 || row >= input.length || col < 0 || col >= input[0].length)
            return;

        if (input[row][col] == null)
            return;

        for (Pop t : output) {
            int[] rc = t.getRC();
            if (row == rc[0] && col == rc[1])
                return;
        }

        if (main.compare(input[row][col])) {
            output.add(input[row][col]);

            area(input, output, main, row - 1, col);
            area(input, output, main, row + 1, col);
            area(input, output, main, row, col - 1);
            area(input, output, main, row, col + 1);
        }
    }

    public static <T> int nullSliceV(T[][] input, ArrayList<Tuple<T, D>> output, int anchor, int col) {
        int sp = (anchor == 0) ? input.length - 1 : 0;
        int ep = (anchor == 0) ? 0 : input.length - 1;
        int d = (sp < ep) ? 1 : -1;

        for (int i = sp; i != ep + d; i += d) {
            if (input[i][col] != null) {
                int dr = 0;
                for (int j = i + d; j != ep + d; j += d)
                    if (input[j][col] == null)
                        dr += d;
                if (dr != 0)
                    output.add(new Tuple<T, D>(input[i][col], new D(dr, 0)));
            }
        }

        return output.size();
    }

    public static <T> int nullSliceH(T[][] input, ArrayList<Tuple<T, D>> output, int anchor, int row) {
        int sp = (anchor == 0) ? input[0].length - 1 : 0;
        int ep = (anchor == 0) ? 0 : input[0].length - 1;
        int d = (sp < ep) ? 1 : -1;

        for (int i = sp; i != ep + d; i += d) {
            if (input[row][i] != null) {
                int dc = 0;
                for (int j = i + d; j != ep + d; j += d)
                    if (input[row][j] == null)
                        dc += d;
                if (dc != 0)
                    output.add(new Tuple<T, D>(input[row][i], new D(0, dc)));
            }
        }

        return output.size();
    }

    public static <T> int nullSlice(T[][] input, ArrayList<Tuple<T, D>> output, int anchor, int range, Direction direction) {
        if (direction == Direction.HORIZON)
            return nullSliceH(input, output, anchor, range);
        else
            return nullSliceV(input, output, anchor, range);
    }

    public static <T> int remain(T[][] input, ArrayList<T> output) {
        int r = 0;
        for (T[] row : input)
            for (T t : row)
                if (t != null) {
                    r++;
                    output.add(t);
                }
        return r;
    }

    public static <T extends com.ss.interfaces.Comparable> boolean haveAdjacent(Pop[][] input, int row, int col, Pop main){
        if (row + 1 < input.length && input[row + 1][col] != null)
            if (main.compare(input[row + 1][col])) return true;

        if (row - 1 >= 0 && input[row - 1][col] != null)
            if (main.compare(input[row - 1][col])) return true;

        if (col + 1 < input[0].length && input[row][col + 1] != null)
            if (main.compare(input[row][col + 1])) return true;

        if (col - 1 >= 0 && input[row][col - 1] != null)
            if (main.compare(input[row][col - 1])) return true;
        return false;
    }

    public static int shuffle(int range, int sp, int limit) {
        int id = (int)(Math.floor(Math.random()*(range)));
        id = ( (id + sp) > limit - 1 ) ? (id + sp)%limit : id + sp;
        return id;
    }
}
