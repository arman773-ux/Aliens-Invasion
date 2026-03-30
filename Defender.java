import java.awt.*; 
import javax.swing.*;
public class Defender extends Sprite {
	
	private ImageIcon DefenderImage;
	
	public Defender() 
	{ 
		x = 0; 
		y = 200; 
		width = 20; 
		height = 10; 
		DefenderImage = new ImageIcon("defender.png");
	}
	public void draw(JPanel panel) 
	{ 
		Graphics paper = panel.getGraphics(); 
		DefenderImage.paintIcon(this, paper, x, y);
	}

	public void move(int mouseX) 
	{ 
		if(mouseX < 200 && mouseX > 0)
		{	
			x = mouseX;
		}
	}
	
	public void move(int mouseX, int mouseY)
	{
		if(mouseX < 200 && mouseX > 0)
		{	
			x = mouseX;
		}
		if(mouseY < 200 && mouseY > 50)
		{	
			y = mouseY;
		}
	}
	
}
