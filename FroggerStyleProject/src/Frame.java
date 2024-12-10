import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	
	final int GAME = 0;
	final int DIE = 1;
	final int WIN = 2;
	final int RESTART = 3;
	final int GAMEWON = 4;
	int score = 0;
	int currentState = RESTART;
	int wins = 0;
	//for any debugging code we add
	public static boolean debugging = true;
	
	//Timer related variables
	int waveTimer = 5; //each wave of enemies is 20s
	long ellapseTime = 0;
	Font timeFont = new Font("Courier", Font.BOLD, 70);
	int level = 0;
	
	
	Font myFont = new Font("Courier", Font.BOLD, 40);
	SimpleAudioPlayer backgroundMusic = new SimpleAudioPlayer("The Simpsons.wav", true);
	
	Background bkg = new Background(0,0);
	Donut donut1 = new Donut();
	Donut donut2 = new Donut(300, 120);
	PoliceCar pc = new PoliceCar(100, 200);
	Skateboard skate = new Skateboard();
	Couch couch = new Couch();
	Bart bart = new Bart(250, 440);
	WinGiph homer = new WinGiph(0,0);
	//a row of DonutScrolling objects!
	DonutScrolling[] row1 = new DonutScrolling[10];
	CouchScrolling[] row4 = new CouchScrolling[10];
	CouchScrolling2[] row5 = new CouchScrolling2[10];
	DonutScrolling2[] row6 = new DonutScrolling2[10]; 
	ArrayList<DonutScrolling2> row6List = new ArrayList<DonutScrolling2>(); 
	ArrayList<LifeImage> lives = new ArrayList<LifeImage>();
	//frame width/heightwd
	int width = 600;
	int height = 600;	
	
	SkateboardScrolling[] row3 = new SkateboardScrolling[10];	
	PoliceCarScrolling[] row2 = new PoliceCarScrolling[10];
	//frame width/height
	

	
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		if(currentState == DIE || currentState == WIN || currentState == GAME || currentState == RESTART || currentState == GAMEWON) {
			//paint the other objects on the screen
			
			bkg.paint(g);
			bart.paint(g);
			
			//paint the donuts at the top (goal points)
			donut2.paint(g);
			
		}	
		//paint scoreboard
		g.drawString("Score: " + score, 10, 10);
		//have the row1 objects paint on the screen!
		//for each obj in row1
		for(DonutScrolling obj : row1) {
			obj.paint(g);
		}
		for(SkateboardScrolling obj : row3) {
			obj.paint(g);
		}
		for(PoliceCarScrolling obj : row2) {
			obj.paint(g);
		}
		for(CouchScrolling obj : row4) {
			obj.paint(g);
		}
		for(CouchScrolling2 obj : row5) {
			obj.paint(g);
		}
		for(DonutScrolling2 obj : row6List) { //for every DonutScrolling2 object in row6List
			obj.paint(g);
		}
		bart.paint(g);
		
		//Collision Detection
		boolean riding = false;
		for(CouchScrolling obj: row4) {
			if(obj.collided(bart)) {
				bart.setVx(obj.getVx()-1.75);
				riding = true;
				break;
			}
		}
		for(CouchScrolling2 obj: row5) {
			if(obj.collided(bart)) {
				bart.setVx(obj.getVx()+2.95);
				riding = true;
				break;
			}
		}
		
		for(DonutScrolling2 obj: row6List) {
			if(obj.collided(bart)) {
				bart.setVx(obj.getVx()+2.95);
				riding = true;
				break;
			}
		}
		
		
		//main character stops moving if not on a rideable object
		//but also lets limit it in the Y
		if(!riding && bart.getY() < 280 && bart.getY() > 120) {
			riding = false;
			bart.setVx(0);
			bart.x = 250;
			bart.y = 440;
			if(score == 5) {
				currentState = GAMEWON;
			}
			if(lives.size()>0) {
				currentState = DIE;
				lives.remove(lives.size()-1);
			}
			if(lives.size()==0) {
				currentState = GAME;
				for(int i = 0; i<6; i++) {
					this.lives.add(new LifeImage(i*40+10, 500));
				}
			}
			score--;
			JOptionPane.showMessageDialog(null,  "YOU FELL AND DIED! Press ENTER to play again. Score: " + score);
			//if ! riding and the duck is in the water area
			//reset back to starting
		}
		else if(!riding) {
			bart.setVx(0);
		}
		
		//drawing the life counter images
		for(LifeImage obj : lives) {
			//draw the LifeImage objects
			obj.paint(g);
		}
		
		if(((bart.getY()<280 && bart.getY()>120) && bart.getX()<-20) || ((bart.getY()<280 && bart.getY()>120) && bart.getX()>600)) {
			bart.setVx(0);
			bart.x=250;
			bart.y=440;
			if(score == 5) {
				currentState = GAMEWON;
			}
			if(lives.size()>0) {
				currentState = DIE;
				lives.remove(lives.size()-1);
			}
			if(lives.size()==0) {
				currentState = GAME;
				for(int i = 0; i<6; i++) {
					this.lives.add(new LifeImage(i*40+10, 500));
				}
			}
			score--;
			JOptionPane.showMessageDialog(null,  "YOU FELL AND DIED! Press ENTER to play again. Score: " + score);
		}
		
		//Collision detection
		for(SkateboardScrolling obj : row3) {
			//invoke the collided method for your
			//class - pass the main character
			//as your argument
			if(obj.collided(bart)) {
				System.out.println("ouch");
				bart.x = 250;
				bart.y= 440;
				if(score == 5) {
					currentState = GAMEWON;
				}
				if(lives.size()>0) {
					currentState = DIE;
					lives.remove(lives.size()-1);
				}
				if(lives.size()==0) {
					currentState = GAME;
					for(int i = 0; i<6; i++) {
						this.lives.add(new LifeImage(i*40+10, 500));
					}
				}
				score--;
				JOptionPane.showMessageDialog(null,  "YOU LOST! Press ENTER to play again. Score: " + score);
			}
		}

		for(DonutScrolling obj : row1) {
			//invoke the collided method for your
			//class - pass the main character
			//as your argument
			if(obj.collided(bart)) {
				System.out.println("ouch");
				bart.x = 250;
				bart.y= 440;
				
				if(score == 5) {
					currentState = GAMEWON;
				}
				if(lives.size()>0) {
					currentState = DIE;
					lives.remove(lives.size()-1);
				}
				
				if(lives.size()==0) {
					currentState = GAME;
					for(int i = 0; i<6; i++) {
						this.lives.add(new LifeImage(i*40+10, 500));
					}
				}
				score--;
				JOptionPane.showMessageDialog(null,  "YOU LOST! Press ENTER to play again. Score: " + score);
			}
		}
	
		for(PoliceCarScrolling obj : row2) {
			//invoke the collided method for your
			//class - pass the main character
			//as your argument
			if(obj.collided(bart)) {
				System.out.println("ouch");
				bart.x = 250;
				bart.y= 440;
				
				if(score == 5) {
					currentState = GAMEWON;
				}
				
				if(lives.size()>0) {
					currentState = DIE;
					lives.remove(lives.size()-1);
				}
				if(lives.size()==0) {
					currentState = GAME;
					for(int i = 0; i<6; i++) {
						this.lives.add(new LifeImage(i*40+10, 500));
					}
				}
				score--;
				JOptionPane.showMessageDialog(null,  "YOU LOST! Press ENTER to play again. Score: " + score);
			}
		}
		
		if(bart.y == 10) {
			bart.vx = 0;
		}
		
		
		
		if(donut2.collided(bart)) {
			bart.x = 250;
			bart.y = 440;
			if(score == 5) {
				currentState = GAMEWON;
			}
			else {
				currentState = WIN;
			}
			score++;
			JOptionPane.showMessageDialog(null,  "YOU WON! Score: " + score + " Press ENTER to play again.");
		}
		if(currentState == GAMEWON) {
			score = 0;
			while(lives.size()>0) {
				lives.remove(lives.size()-1);
			}
			for(int i = 0; i<6; i++) {
				this.lives.add(new LifeImage(i*40+10, 500));
			}
			drawGameWonState(g);
		}
		
		if(currentState == GAME) {
			score = 0;
			drawGameState(g);
		}
		
		if(currentState == WIN){
			drawWinState(g);
		}
		if(currentState == DIE){
			
			drawDeathState(g);
		}
	}
	void drawGameWonState(Graphics g) {
		g.drawImage(getImage("/imgs/"+"winner.gif"), 0, 0, 600, 600, null);
	}
	
	void drawGameState(Graphics g) {
		g.drawImage(getImage("/imgs/"+"gameover.gif"), 0, 0, 600, 600, null);
	}
	void drawWinState(Graphics g) {
		g.drawImage(getImage("/imgs/"+"giphy.gif"), 0, 0, 600, 600, null);
	}
	void drawDeathState(Graphics g) {
		g.drawImage(getImage("/imgs/"+"falling.gif"), 0, 0, 600, 600, null);
	}
	
	private Image getImage(String path) {
		// TODO Auto-generated method stub
		Image tempImage = null;
		try {
			URL imageURL = WinGiph.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

	public static void main(String[] arg) {
		Frame f = new Frame();
		
	}
	
	public Frame() {
		JFrame f = new JFrame("Duck Hunt");
		f.setSize(new Dimension(width, height));
		f.setBackground(Color.white);
		f.add(this);
		f.setResizable(false);
 		f.addMouseListener(this);
		f.addKeyListener(this);
	
	//	backgroundMusic.play();

		/*
		 * Setup any 1D array here! - create the objects that go in them ;)
		 */
		//traverse the array
		
		//practice row for ArrayList
		for(int i = 0; i<10; i++) {
			this.row6List.add(new DonutScrolling2(i*150, 240));
		}
		for(int i = 0; i<row1.length; i++) {
			row1[i] = new DonutScrolling(i*150, 320);
		}
		for(int i = 0; i<row3.length; i++) {
			row3[i] = new SkateboardScrolling(i*300, 400);
		}
		for(int i = 0; i<row2.length; i++) {
			row2[i] = new PoliceCarScrolling(i*300, 360);
		}
		for(int i = 0; i<row4.length; i++) {
			row4[i] = new CouchScrolling(i*300, 200);
		}
		for(int i = 0; i<row5.length; i++) {
			row5[i] = new CouchScrolling2(i*300, 160);
		}
		for(int i = 0; i<row1.length; i++) {
			row6[i] = new DonutScrolling2(i*150, 240);
		}
		
		//START WITH 6 ATTEMPTS
		for(int i = 0; i<6; i++) {
			this.lives.add(new LifeImage(i*40+10, 500));
		}
		
		
		//the cursor image must be outside of the src folder
		//you will need to import a couple of classes to make it fully 
		//functional! use eclipse quick-fixes
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("torch.png").getImage(),
				new Point(0,0),"custom cursor"));	
		
		
		Timer t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		
	
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode() == 38) {
			//move main character up
			bart.move(0);
		}
		else if(arg0.getKeyCode() == 40) {
			//move main character down
			bart.move(1);
		}
		else if(arg0.getKeyCode() == 39) {
			//move main character right
			bart.move(3);
		}
		else if(arg0.getKeyCode() == 37) {
			//move main character left
			bart.move(2);
		}
		else if(arg0.getKeyCode() == 10) {
			currentState = RESTART;
			}
		
		if(bart.x<0) {
			bart.x = 10;
		}
		if(bart.x + bart.width > 600) {
			bart.x = 550;
		}
		if(bart.y + bart.height > 600) {
			bart.y = 560;
		}
		if(bart.y < 0) {
			bart.y = 0;
		}
		
		
		
	}
	
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
