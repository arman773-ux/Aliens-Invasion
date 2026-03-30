import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import java.io.*;

class Game extends JFrame implements MouseListener, MouseMotionListener, ActionListener
{
	private int bombCount = 5, laserCount = 3;
	private JPanel panel;
	private JButton startButton;
	private Timer timer;
	private Graphics paper; 
	private Defender defender;
	private Laser lasers[] = new Laser[laserCount];
	private Alien alien;
	private Bomb bombs[] = new Bomb[bombCount];
	
	private void createGUI()
	{
		Container window = getContentPane();
		window.setLayout(null); //layout is set to null because we will use setBounds 
		panel = new JPanel();
		panel.setBounds(25, 0, 225, 220);
		window.add(panel);
		
		startButton = new JButton("start"); 
		startButton.setBounds(100, 220, 100, 25);
		window.add(startButton); 
		startButton.addActionListener(this); //add action listener to startButton

		ActionListener taskPerformer = new ActionListener() 
		{
			public void actionPerformed(ActionEvent evt) 
			{
				timer_Tick();
			}
		};
		timer = new Timer(100,taskPerformer); //run timer_Tick every 0.1 s
	}	
	private void newGame() 
	{ 
		defender = new Defender(); 
		alien = new Alien(); 
		timer.start(); 
	} 
	
	private int getRandomX()
	{
		Random rand = new Random(); 
		return rand.nextInt(200);
	}
	
	private void timer_Tick() 
	{ 
	/*tasks to be performed every 100 seconds by timer*/
		for(int i = 0; i < bombCount; i++)
		{
			if (bombs[i] == null) 
			{ 
				bombs[i] = new Bomb(getRandomX(), alien.getY()); //drop bombs from random positions together 
			}
		}
		moveAll(); 
		drawAll(); 
		checkHits(); 
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == startButton)
		{
			addMouseListener(this); //add mouse listener for mouseClicked 
			addMouseMotionListener(this); //add mouse motion listener for mouseMoved
			newGame();
		}
	}

	public void mouseClicked(MouseEvent event) 
	{ 
		int initialX = defender.getX();
		int initialY = defender.getY(); 
		for(int i = 0; i < laserCount; i++)
		{
			if(lasers[i] == null) //find first empty laser index
			{
				lasers[i] = new Laser(initialX, initialY); //fire the laser
				break;
			}
		}			
	}
	//remaining methods of MouseListener
	public void mouseExited(MouseEvent e) 
	{    
    }
	public void mouseEntered(MouseEvent e) 
	{
   	}
	public void mousePressed(MouseEvent e) 
	{
   	}
	public void mouseReleased(MouseEvent e) 
	{
   	}
	
	public void mouseMoved(MouseEvent event) 
	{ 
		defender.move(event.getX(), event.getY()); 
	}
	//remaining methods of MouseMotionListener
	public void mouseDragged(MouseEvent e)
	{	
	}
	
	private void moveAll() 
	{ 
		alien.move(); 
		for(int i = 0; i < bombCount; i++)
		{
			if (bombs[i] != null) 
			{ 
				bombs[i].move();
			}
		}
		for(int i = 0; i < laserCount; i++)
		{
			if(lasers[i] != null) 
			{
				lasers[i].move();
			}
		}
	}
	
	private void drawAll() 
	{ 
		Graphics paper = panel.getGraphics(); 
		paper.setColor(Color.white);
		paper.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		paper.setColor(Color.black); 
		defender.draw(panel); 
		alien.draw(panel); 
		for(int i = 0; i < laserCount; i++)
		{
			if(lasers[i] != null) 
			{
				lasers[i].draw(panel);
			}
		} 
		for(int i = 0; i < bombCount; i++)
		{
			if (bombs[i] != null) 
			{ 
				bombs[i].draw(panel);
			}
		}
	}

	private void checkHits() 
	{
		for(int i = 0; i < laserCount; i++)
		{
			if (collides(lasers[i], alien))
			{
				endGame("user");
				break;
			}
		}
		for(int i = 0; i < bombCount; i++)
		{
			if (collides(bombs[i], defender)) 
			{ 
				endGame("alien"); 
				break;
			} 
		}
		for(int i = 0; i < bombCount; i++)
		{
			if (bombs[i] != null) 
			{ 
				if (bombs[i].getY() > panel.getHeight()) 
				{ 
					bombs[i] = null; 
				} 
			}
		}
		for(int i = 0; i < laserCount; i++)
		{
			if(lasers[i] != null) 
			{
				if(lasers[i].getY() < 0)
				{
					lasers[i] = null;
				}
			}
		}
	}
	
	private boolean collides(Sprite one, Sprite two) 
	{ 
		if (one == null || two == null) 
		{ 
			return false; 
		} 
		if ( one.getX() > two.getX() && one.getY() < (two.getY() + two.getHeight()) && (one.getX() + one.getWidth()) < (two.getX() + two.getWidth()) && (one.getY() + one.getWidth()) > (two.getY())) 
		{ 
			return true;
		} 
		else 
		{ 
			return false; 
		} 
	}

	private void endGame(String winner) 
	{ 
		for(int i = 0; i < laserCount; i++)
		{
			lasers[i] = null;
		}
		for(int i = 0; i < bombCount; i++)
		{
			bombs[i] = null;
		}
		timer.stop(); 
		removeMouseListener(this);
		removeMouseMotionListener(this);
		JOptionPane.showMessageDialog(this, "game over - " + winner + " wins"); //this will center the message on current GUI frame
		
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter("results.txt", true)); 
			out.write(winner); //write winner's name to file
			out.newLine();
			out.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String args[])
	{
		Game frame = new Game();
		frame.setSize(300,300); //set screen size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setVisible(true); 
		frame.createGUI();
	}
}
