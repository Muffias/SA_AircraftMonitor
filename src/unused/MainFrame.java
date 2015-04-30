package unused;



import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;



public class MainFrame extends JFrame{
	
	public MainFrame(MessageTextArea idFrame,MessageTextArea posFrame,MessageTextArea veloFrame )
	{
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize(new Dimension((int)dim.getWidth(),(int)dim.getHeight()));
		this.setTitle("Aircraft");
		
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
		
		
		
		//msgPanel.setLayout(new GridLayout(3,0));
		JScrollPane idPane = new JScrollPane(idFrame);
		JScrollPane posPane = new JScrollPane(posFrame);
		JScrollPane veloPane = new JScrollPane(veloFrame);

		tabPane.add("ID",idPane);
		tabPane.add("Position",posPane);
		tabPane.add("Velocity",veloPane);
		
		this.add(tabPane);
		//this.add(msgPanel);
		//this.add(pane);
		
		this.setVisible(true);

	}


	

}
