package UI.local;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import UI.local.south.AddButton;
import UI.local.south.BranchButton;
import UI.local.south.CheckOutButton;
import UI.local.south.CommitButton;
import UI.local.south.InitButton;
import UI.local.south.MergeButton;

public class South extends JPanel {	
	JButton b1,b2,b3,b4,b5,b6;
	
	South() {		
		setLayout(new GridLayout(1,6,3,3));
		
		b1=new InitButton();
		b2=new AddButton();
		b3=new CommitButton();
		b4=new BranchButton();
		b5=new MergeButton();
		b6=new CheckOutButton();
		
		add(b1);
		add(b2);
		add(b3);
		add(b4);
		add(b5);
		add(b6);
		
		setBackground(Color.lightGray);
		
	}
}