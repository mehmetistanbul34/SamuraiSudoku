import src.Arayuz;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("serial")
class Samurai extends JPanel{

    private static final int SAMURAI_ARR_SIZE = 21;

    private static int[][] samuriArr = new int[SAMURAI_ARR_SIZE][SAMURAI_ARR_SIZE];

    private static String text2="", text="";

    public static void main(String args[]) throws IOException {
        // Create an instance.
        Samurai samurai = new Samurai();
        samuriArr = samurai.getSumariDokuArrFromFile();

        Scanner scanner = new Scanner(System.in);
        String secim = "İşlemler:\n"
                +"Çözülmemiş Sudokuyu Göster (1)\n"
                +"Sudokuyu Çöz ve Göster (2)\n"
                +"5 Thread Grafiği Göster (3)\n"
                +"10 Thread Grafiği Göster (4)\n"
                +"Çıkış için (-1)\n"
                +"Seçiminiz: ";
        int enter=0;
        System.out.print(secim);
        enter = scanner.nextInt();
        switch (enter){
            case 1:
                samurai.report(samuriArr);
                if (!text.equals("")) {
                    System.out.println("text : "+text);
                }
                else {
                    System.out.println("\n\ntext boş\n\n");
                }
                Arayuz arayuz = new Arayuz(text);
                arayuz.setVisible(true);
                break;
            case 2:
                samurai.solve(samuriArr);
                if (!text2.equals("")) {
                    System.out.println("text2 : "+text2);
                }
                else {
                    System.out.println("\n\ntext2 boş\n\n");
                }
                Arayuz arayuz2 = new Arayuz(text2);
                arayuz2.setVisible(true);
                break;
            case 3:
                List<Integer> scores = new ArrayList<>();
                Random random = new Random();
                int thread5 = 5;
                int maxScore = 20;
                for (int i = 1; i <= thread5 ; i++) {
                    if (enter==3) {
                        String name = "Thread"+String.valueOf(i);
                        Threads threads = new Threads(name);
                        maxScore = threads.threadIslem(i);
                        threads.start();
                    }
                    scores.add(random.nextInt(maxScore));
                }
                Grafiks grafiks = new Grafiks(scores);

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        grafiks.createAndShowGui(scores);
                    }
                });
            case 4:
                List<Integer> scores2 = new ArrayList<>();
                Random random2 = new Random();
                int thread10=5;
                int maxScore2 = 20;
                if(enter==4)
                    thread10 = 10;
                for (int i = 1; i <= thread10 ; i++) {
                    if (enter==4){
                        String name = "Thread"+String.valueOf(i);
                        Threads threads = new Threads(name);
                        maxScore2 = threads.threadIslem(i);
                        threads.start();
                    }
                    scores2.add(random2.nextInt(maxScore2));
                }
                if(enter==4) {
                    Grafiks grafiks2 = new Grafiks(scores2);

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            grafiks2.createAndShowGui(scores2);
                        }
                    });
                }
            case -1:
                break;
            default:
                System.out.println("Geçersiz seçim yaptınız. Lütfen tekrar deneyiniz!!");
                break;
        }


    }

    // Solve a puzzle.

    int[][]  getSumariDokuArrFromFile() throws IOException {
        FileReader f = new FileReader("harita.txt");
        BufferedReader in = new BufferedReader(f);
        String line;

        line = in.readLine();
        int i=0;
        while (line!=null){
            i++;
            text += line;
            //System.out.println(i+". satır: "+text);;
            line = in.readLine();
        }
        f.close();

        return getSamuriArrFromString(text);
    }

    //===================================================== initializeFromString
    public int[][] getSamuriArrFromString(final String boardStr) {
        int[][] samuriArr = new int[SAMURAI_ARR_SIZE][SAMURAI_ARR_SIZE];
        clear();  // Clear all values from the board.
        int row = 0;
        int col = 0;
        //... Loop over every character.
        for (int i = 0; i < boardStr.length(); i++) {
            char c = boardStr.charAt(i);
            if (c >= '1' && c <='9') {
                if (row > SAMURAI_ARR_SIZE || col > SAMURAI_ARR_SIZE) {
                    throw new IllegalArgumentException("src.SudokuModel: "
                            + " Attempt to initialize outside 1-9 "
                            + " at row " + (row+1) + " and col " + (col+1));
                }
                samuriArr[row][col] = Integer.valueOf(String.valueOf(c)).intValue();  // c-'0'; Translate digit to int.
                col++;
            } else if (c == '0') {
                col++;
            } else if (c == '/') {
                row++;
                col = 0;
            } else {
                throw new IllegalArgumentException("src.SudokuModel: Character '" + c
                        + "' not allowed in board specification");
            }
        }

        return samuriArr;
    }

    //===================================================================== clear
    public void clear() {
        for (int row = 0; row < SAMURAI_ARR_SIZE; row++) {
            for (int col = 0; col < SAMURAI_ARR_SIZE; col++) {
                samuriArr[row][col] = 0;
            }
        }
    }

    void solve(int[][] puzzle) {

        DancingLinks dl = new DancingLinks(puzzle);

        dl.solve(this);
    }

    void report(int[][] solution) {
        String txt = "";
        for (int r = 0; r < PUZZLE_SIDE; r++) {
            for (int c = 0; c < PUZZLE_SIDE; c++)
                if (solution[r][c] > 0) {
                    System.out.print(solution[r][c] + " ");
                    txt += solution[r][c];
                }else
                    System.out.print(". ");

            System.out.println();
            txt += "\n";
        }

        try {
            FileWriter fileWriter = new FileWriter("threadWorks.txt");
            fileWriter.write(txt);
            fileWriter.close();
        } catch (IOException e) {
            // Exception handling
        }

        System.out.println("-----------------------------------------");
    }



    static final int PUZZLE_SIDE = 21;
    static final int PUZZLE_SIZE = 441;
    static final int SUDOKU_SIDE = 9;
    static final int SUDOKU_SIZE = 81;
    static final int SQUARE_SIDE = 3;
    static final int COLUMN_SIZE = 1692;


    static final int[][] SAMURAI_SQUARE =
            {{0}, {0}, {0}, {}, {1}, {1}, {1},
                    {0}, {0}, {0}, {}, {1}, {1}, {1},
                    {0}, {0}, {0, 2}, {2}, {1, 2}, {1}, {1},
                    {}, {}, {2}, {2}, {2}, {}, {},
                    {3}, {3}, {2, 3}, {2}, {2, 4}, {4}, {4},
                    {3}, {3}, {3}, {}, {4}, {4}, {4},
                    {3}, {3}, {3}, {}, {4}, {4}, {4}};

    static final int[][] SUDOKU_ROW =
            {{0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {-1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8}};

    static final int[][] SUDOKU_COLUMN =
            {{0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {-1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8}};



    class DancingLinks {
        Samurai samurai;
        boolean stop;
        int[] stats;
        int index;
        Column h;
        Node[] o;


        DancingLinks(int[][] p) {
            // Column row head.

            h = new Column(null, 0);
            Column[] m = new Column[COLUMN_SIZE];

            // Create the row of columns.

            for (int i = 0; i < COLUMN_SIZE; i++)
                m[i] = new Column(h, 0);

            // List of rows that are part of the solution.

            Node[] l = new Node[PUZZLE_SIZE];
            int i = 0;

            for (int r = 0; r < PUZZLE_SIDE; r++)
                for (int c = 0; c < PUZZLE_SIDE; c++)
                    for (int d = 0; d < SUDOKU_SIDE; d++) {

                        int k = 1 + (r * PUZZLE_SIDE * SUDOKU_SIDE) +
                                (c * SUDOKU_SIDE) + d;

                        int s = (c / 3) + ((r / 3) * 7);

                        if (SAMURAI_SQUARE[s].length > 0) {

                            Node n = new Node(m[(r * PUZZLE_SIDE) + c], k);

                            for (int j = 0; j < SAMURAI_SQUARE[s].length; j++) {

                                int pz = SAMURAI_SQUARE[s][j];

                                int pr = SUDOKU_ROW[pz][r];
                                int pc = SUDOKU_COLUMN[pz][c];

                                n.add(new Node(m[PUZZLE_SIZE +
                                        (pz * SUDOKU_SIZE) +
                                        (pr * SUDOKU_SIDE) + d], k));

                                n.add(new Node(m[PUZZLE_SIZE + 405 +
                                        (pz * SUDOKU_SIZE) +
                                        (pc * SUDOKU_SIDE) + d], k));
                            }

                            n.add(new Node(m[PUZZLE_SIZE + 405 + 405 +
                                    (s * SUDOKU_SIDE) + d], k));

                            if (p[c][r] == (d + 1))
                                l[i++] = n;
                        }
                    }

            for (Column c = (Column) h.r; c != h; c = (Column) c.r)
                if (c.s == 0)
                    c.cover();

            o = new Node[PUZZLE_SIZE];

            for (int j = 0; j < i; j++) {
                l[j].remove();
                o[index++] = l[j];
            }

            stats = new int[PUZZLE_SIZE];
        }

        void report(int[] o) {

            int a[][] = new int[PUZZLE_SIDE][PUZZLE_SIDE];

            for (int i = 0; i < o.length; i++) {
                int v = o[i];

                int d = v % SUDOKU_SIDE;
                int c = (v / SUDOKU_SIDE) % PUZZLE_SIDE;
                int r = (v / (PUZZLE_SIDE * SUDOKU_SIDE)) % PUZZLE_SIDE;

                a[c][r] = d + 1;
            }

            // Report the result.
            for (int i = 0; i <21 ; i++) {
                for (int j = 0; j < 21; j++) {
                    text2 += a[i][j];
                }
                text2 +='/';
            }
            samurai.report(a);

            // Create an array for the stats

            int s[][] = new int[PUZZLE_SIDE][PUZZLE_SIDE];

            for (int i = 0; i < o.length; i++)
                s[i / PUZZLE_SIDE][i % PUZZLE_SIDE] = stats[i];
        }

        void solve(Samurai s) {
            samurai = s;
            search(index);
        }

        void search(int k) {
            // If a result has already been found, return.

            if (stop)
                return;

            // If there are no more columns, report the result.

            if (h.r == h) {
                int[] a = new int[k];

                // Extract the row numbers.

                for (int i = 0; i < k; i++)
                    a[i] = o[i].n - 1;

                // Report the result and set the stop flag.

                report(a);
                stop = true;
            }

            // Else find the shortest column and cover it.

            else {
                Column c = null;
                int s = Integer.MAX_VALUE;

                // Increment stats;

                stats[k]++;

                // Find the shortest column.

                for (Column j = (Column) h.r; j != h; j = (Column) j.r)
                    if (s > j.s) {
                        c = j;
                        s = j.s;
                    }

                // Cover it.

                c.cover();

                // For each row in the column...

                for (Node r = c.d; r != c; r = r.d) {
                    // Skip this if a result has been found.

                    if (stop)
                        break;

                    // Save the row in the output array.

                    o[k] = r;

                    // For each node in this row, cover it's column.

                    for (Node j = r.r; j != r; j = j.r)
                        j.c.cover();

                    // Recurse with k + 1.

                    search(k + 1);

                    // For each node in this row, uncover it's column.

                    for (Node j = r.l; j != r; j = j.l)
                        j.c.uncover();
                }

                // Uncover the column.

                c.uncover();
            }
        }
    }

    class Node {
        Node l;
        Node r;
        Node u;
        Node d;
        Column c;
        int n;

        // Create a self referencing node.

        Node(Column c, int n) {
            this.l = this;
            this.r = this;

            this.u = this;
            this.d = this;

            // Column and row number.

            this.c = c;
            this.n = n;

            // If the column isn't null, add this node to it.

            if (c != null)
                c.add(this);
        }

        // Remove a row of nodes.

        void remove() {
            Node n = this;

            // Cover this node's column and move on to the next right.

            do {
                n.c.cover();
                n = n.r;
            }

            // While we haven't got back to this node.

            while (n != this);
        }

        // Add a node to the left of this node.

        void add(Node n) {
            n.l = this.l;
            n.r = this;

            this.l.r = n;
            this.l = n;
        }
    }

    class Column extends Node {
        int s;

        Column(Column c, int n) {
            super(null, n);

            if (c != null)
                c.add(this);
        }

        void cover() {
            // Cover this column.

            r.l = l;
            l.r = r;

            for (Node i = d; i != this; i = i.d)

                for (Node j = i.r; j != i; j = j.r) {
                    // Cover this row.

                    j.u.d = j.d;
                    j.d.u = j.u;

                    // Adjust the column size.

                    j.c.s--;
                }
        }

        void uncover() {

            for (Node i = u; i != this; i = i.u)

                for (Node j = i.l; j != i; j = j.l) {
                    // Uncover this row.

                    j.u.d = j;
                    j.d.u = j;

                    j.c.s++;
                }

            // Uncover this column.

            r.l = this;
            l.r = this;
        }

        void add(Column c) {
            c.l = this.l;
            c.r = this;

            this.l.r = c;
            this.l = c;
        }

        void add(Node n) {
            n.u = this.u;
            n.d = this;

            this.u.d = n;
            this.u = n;

            s++;
        }
    }
}
