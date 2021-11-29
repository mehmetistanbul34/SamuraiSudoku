package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Arayuz extends JFrame {
    private SudokuModel _sudokuLogic;//new src.SudokuModel(INITIAL_BOARD);
    private SudokuBoardDisplay _sudokuBoard;//new src.SudokuBoardDisplay(_sudokuLogic);

    private JTextField _rowTF = new JTextField(2);
    private JTextField _colTF = new JTextField(2);
    private JTextField _valTF = new JTextField(2);

    public Arayuz(String text) throws HeadlessException, IOException {
        _sudokuLogic = new SudokuModel(text);
        _sudokuLogic.solve();
        _sudokuBoard = new SudokuBoardDisplay(_sudokuLogic);

        JButton moveBtn = new JButton("Ekle");
        JButton solveBtn = new JButton("Solve");

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        // 3... Add the components to the content panel.
        content.add(controlPanel, BorderLayout.NORTH);
        content.add(_sudokuBoard, BorderLayout.CENTER);

        // 4... Set this window's attributes, and pack it.
        setContentPane(content);
        setTitle("Sudoku 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);               // Don't let user resize it.
        pack();
        setLocationRelativeTo(null);
    }
}
