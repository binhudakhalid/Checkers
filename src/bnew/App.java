package bnew;


import javax.swing.JFrame;

public class App {
	public static void main(String[] are) {
		CheckersGUI frame = new CheckersGUI();
	    frame.setTitle("Hellow Checker Master");
	    frame.setSize(500, 700);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    frame.setResizable(true);
	}
}
