package bnew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CheckersGUI extends JFrame {

	private CheckersGame game;
	private BoardLayeredPane boardPanel;

	ImageIcon redChecker = new ImageIcon("start3.gif");
	ImageIcon blueChecker = new ImageIcon("start4.gif");

	private int currentPlayer = 0;

	public CheckersGUI() {// Constructer
		game = new CheckersGame();
		Gamegui();
	}

	// set up the game board and side menus
	private void Gamegui() {// method

		JPanel mainPanel = new JPanel();// main panel

		boardPanel = new BoardLayeredPane();// LayeredPanal

		// set layout for panels//adding border layout to main panel
		mainPanel.setLayout(new BorderLayout());
		// add boardPanel layered to the main panel
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		GridLayout layout = new GridLayout();
		setLayout(layout);
		add(mainPanel);
//		JButton b1 =new JButton();
//		add(b1);
	}

	// A JLabel that remembers its position on the checkers board
	public class PositionedLabel extends JLabel {
		private static final long serialVersionUID = 8259625158276346535L;
		private int position;

		PositionedLabel(Icon image, int horizontalAlignment, int argPosition) {
			super(image, horizontalAlignment);
			position = argPosition;
		}

		public int getPos() {
			return position;
		}

		public void setPos(int argPos) {
			position = argPos;
		}
	}

	// A layered panel for the checkers board
	public class BoardLayeredPane extends JLayeredPane {

		private static final long serialVersionUID = -7159931441732047527L;
		private GridLayout gridlayout = new GridLayout(9,9);// Checker Boards
		private JPanel squarePanel = new JPanel(gridlayout);
		private JPanel[] squares = new JPanel[104];
		private final Color darkSquare = new Color(71, 71, 71);
		private final Color brightSquare = new Color(204, 204, 225);

		public BoardLayeredPane() { // Constructer

			squarePanel.setSize(new Dimension(500, 500));// making square of
															// board
			squarePanel.setBackground(Color.black);
			for (int i = 0; i < 64; i++)
			{
				squares[i] = new JPanel(new GridBagLayout());

				squares[i].setBackground(darkSquare);

				if (!CheckersGame.VALID_SQUARE[i]) {
					squares[i].setBackground(brightSquare);
				} else if (game.getOwnerAt(i) == 0 && game.getTypeAt(i) == 0) {
					squares[i].add(new PositionedLabel(blueChecker,
							SwingConstants.CENTER, i));
				} else if (game.getOwnerAt(i) == 1 && game.getTypeAt(i) == 0) {
					squares[i].add(new PositionedLabel(redChecker,
							SwingConstants.CENTER, i));
				}

				squarePanel.add(squares[i]);
			}
			add(squarePanel, JLayeredPane.DEFAULT_LAYER);

			// add mouse listeners for drag and drop
			BoardMouseAdapter myMouseAdapter = new BoardMouseAdapter();
			addMouseListener(myMouseAdapter);
			addMouseMotionListener(myMouseAdapter);
		}

		/*
		 * Repaint the board based on the status of the game. If boolean
		 * "borders" is set to true, borders around squares will be removed.
		 */
		public void refreshBoardWithBorders(boolean borders) {
			for (int i = 0; i < 64; i++) {
				squares[i].removeAll();
				if (borders)
					squares[i].setBorder(BorderFactory.createEmptyBorder());

				if (!CheckersGame.VALID_SQUARE[i]) {
					squares[i].setBackground(brightSquare);
				}

				else if (game.getOwnerAt(i) == CheckersPiece.Umer
						&& game.getTypeAt(i) == CheckersPiece.CHECKER) {
					squares[i].add(new PositionedLabel(blueChecker,
							SwingConstants.CENTER, i));
				}

				else if (game.getOwnerAt(i) == CheckersPiece.Khalid
						&& game.getTypeAt(i) == CheckersPiece.CHECKER) {
					squares[i].add(new PositionedLabel(redChecker,
							SwingConstants.CENTER, i));
				}

				squarePanel.add(squares[i]);
			}
			validate();
			repaint();
		}

		// A mouse adapter for the board that enables Drag and Drop
		private class BoardMouseAdapter extends MouseAdapter 
		{

			private PositionedLabel draggedLabel;
			private JPanel clickedPanel;
			private int labelMiddle;
			private int originalLabelPos = -1;

			@Override
			public void mousePressed(MouseEvent e) {

				clickedPanel = (JPanel) squarePanel
						.getComponentAt(e.getPoint());

				// if the clicked panel has no labels
				if (!(clickedPanel instanceof JPanel)
						|| clickedPanel.getComponentCount() == 0) {
					return;
				}

				// retrieve item from clicked panel, check if it's a label
				Component clickedComp = clickedPanel.getComponent(0);

				if (!(clickedComp instanceof PositionedLabel)) {
					return;
				}

				// retrieve the label
				draggedLabel = (PositionedLabel) clickedComp;
				originalLabelPos = draggedLabel.getPos();

				// check owner of the picked up piece/label
				if (game.getOwnerAt(originalLabelPos) != currentPlayer) {
					return;
				}

				// remove the label from the board
				clickedPanel.remove(draggedLabel);

				// position the label on the mouse cursor
				labelMiddle = draggedLabel.getWidth() / 2;
				int x = e.getPoint().x - labelMiddle;
				int y = e.getPoint().y - labelMiddle;
				draggedLabel.setLocation(x, y);

				// add the label to the drag layer
				add(draggedLabel, JLayeredPane.DRAG_LAYER);

				validate();
				repaint();
			}

			// update the position of the label as mouse cursor moves
			@Override
			public void mouseDragged(MouseEvent e) {
				if (draggedLabel == null
						|| game.getOwnerAt(originalLabelPos) != currentPlayer) {
					return;
				}

				int x = e.getPoint().x - labelMiddle;
				int y = e.getPoint().y - labelMiddle;
				draggedLabel.setLocation(x, y);
				repaint();
			}

			// act when the label is dropped
			@Override
			public void mouseReleased(MouseEvent e) {

				// if no label, or incorrect owner of the piece, quit
				if (draggedLabel == null
						|| game.getOwnerAt(originalLabelPos) != currentPlayer) {
					return;
				}

				// remove dragLabel from the drag layer
				remove(draggedLabel);
				draggedLabel = null;

				// locate where the label was dropped
				JPanel droppedPanel = (JPanel) squarePanel.getComponentAt(e.getPoint());

				// find the position of the dropped item, return -1 if off the
				// game board
				int droppedPos = -1;
				for (int i = 0; i < squares.length; i++) 
				{
					if (squares[i] == droppedPanel) {
						droppedPos = i;
						break;
					}
				}

				// if the move was invalid put label in original position
				if (droppedPos == -1
						|| !game.movePiece(originalLabelPos, droppedPos,
								currentPlayer)) 
				{
					boardPanel.refreshBoardWithBorders(false);
					return;
				}

				// store the previous game state for the "Undo" option

				boardPanel.refreshBoardWithBorders(true);
				currentPlayer = (++currentPlayer) % 2;
			}
		}
	}

}