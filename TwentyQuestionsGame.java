package stackutils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import datastructures.BinaryTreeNode;
import datastructures.DefaultBinaryTree;
import datastructures.DefaultBinaryTreeNode;

public class TwentyQuestionsGame extends JPanel implements ActionListener {
	private JButton yes;
	private JButton no;
	private JLabel instructions;
	private JLabel output;
	private String instructionsText;
	private String initialMessage;
	private DerbyFileReader reader;
	private DefaultBinaryTree<String> tree;
	private BinaryTreeNode<String> currentNode;
	java.lang.String[] args;

	public TwentyQuestionsGame(java.lang.String[] args) {
		super(new BorderLayout());
		this.args = args;
		instructionsText = "<html>Think of your favorite roller derby team.<br> Answer the yes or no questions and"
				+ " see if the computer can guess your choice! <br> <br> <br></html>";
		reader = new DerbyFileReader(args);
		tree = reader.getTree();
		instructions = new JLabel(instructionsText);
		this.add(instructions, BorderLayout.NORTH);
		initialMessage = "Ready to play?";
		output = new JLabel(initialMessage);
		output.setHorizontalAlignment(JLabel.CENTER);
		output.setVerticalAlignment(JLabel.NORTH);
		this.add(output, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		yes = new JButton("Yes");
		yes.addActionListener(this);
		no = new JButton("No");
		no.addActionListener(this);
		buttonPanel.add(yes);
		buttonPanel.add(no);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (output.getText().equals(initialMessage)) {
			if (e.getSource() == yes) {
				currentNode = tree.getRoot();
				output.setText(currentNode.getData());
			}
		} else {
			if (e.getSource() == yes) {
				if (!currentNode.isLeaf()) {
					currentNode = currentNode.getLeftChild();
					if (currentNode.isLeaf()) {
						output.setText("is " + currentNode.getData()
								+ " correct?");
					} else {
						output.setText(currentNode.getData());
					}

				} else {
					newGame();
				}
			} else {
				if (!currentNode.isLeaf()) {
					currentNode = currentNode.getRightChild();
					if (currentNode.isLeaf()) {
						output.setText("is " + currentNode.getData()
								+ " correct?");
					} else {
						output.setText(currentNode.getData());
					}
				} else {
					String[] inputs = openDialog();
					if (inputs[0] == null | inputs[1] == null
							| inputs[2] == null) {
						output.setText("New answer input failed. Hit yes to start a new game. Hit no to try input again");
					} else {
						addAnswer(inputs[0], inputs[1], inputs[2], currentNode);
						newGame();
					}

				}

			}

		}
	}

	private String[] openDialog() {
		String[] returns = new String[3];
		returns[0] = JOptionPane.showInputDialog("What were you thinking of?");
		returns[1] = JOptionPane
				.showInputDialog("Please give me a yes/no question that would have determined your thing:");
		String[] choices = { "Yes", "No" };
		returns[2] = (String) JOptionPane.showInputDialog(null,
				"Choose now...", "The Choice of a Lifetime",
				JOptionPane.QUESTION_MESSAGE, null, // Use
				// default
				// icon
				choices, // Array of choices
				choices[0]); // Initial choice
		return returns;

	}

	private void newGame() {
		currentNode = null;
		output.setText(initialMessage);
	}

	private void addAnswer(String thing, String question, String answer,
			BinaryTreeNode<String> currentNode) {
		String orignal = currentNode.getData();
		DefaultBinaryTreeNode<String> newAnswer = new DefaultBinaryTreeNode<String>();
		newAnswer.setData(thing);
		DefaultBinaryTreeNode<String> originalAnswer = new DefaultBinaryTreeNode<String>();
		originalAnswer.setData(orignal);
		currentNode.setData(question);
		if (answer.equals("Yes")) {
			currentNode.setLeftChild(newAnswer);
			currentNode.setRightChild(originalAnswer);
		} else {
			currentNode.setRightChild(newAnswer);
			currentNode.setLeftChild(originalAnswer);
		}

	}

}
