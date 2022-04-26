import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class Panel extends JPanel implements ActionListener {
	
	static final int BOARD_WIDTH = 600;
	static final int BOARD_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (BOARD_WIDTH*BOARD_HEIGHT)/UNIT_SIZE;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int delay = 70;
	int bodyParts = 1;
	int dotsEaten;
	int dotX;
	int dotY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	Panel(){
		random = new Random();
		this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newDot();
		running = true;
		delay = 70;
		bodyParts = 1;
		dotsEaten = 0;
		direction = 'R';
		timer = new Timer(delay,this);
		timer.start();
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running ) {
			for(int i=0;i<BOARD_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, BOARD_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, BOARD_WIDTH, i*UNIT_SIZE);	
			}
			g.setColor(Color.green);
			g.fillOval(dotX, dotY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0;i< bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.red);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.white);
			g.setFont(new Font("Sans Serrif",Font.BOLD, 35));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+ dotsEaten, (BOARD_WIDTH - metrics.stringWidth("Score: "+ dotsEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	public void newDot() {
		dotX = random.nextInt((int)(BOARD_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		dotY = random.nextInt((int)(BOARD_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkScore() {
		if((x[0] == dotX) && (y[0] == dotY)) {
			bodyParts++;
			dotsEaten++;
			if(delay >=10 ) {
				delay = delay - 10;
			}
			else {
				delay = 0;
			}
			
			newDot();
		}
	}
	
	public void checkCollision() {
		// checks if head collides to body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&&(y[0] == y[i])) {
				running = false;
			}
		}
		//checks if head collides left
		if(x[0] < 0) {
			running = false;
		}
		//check if head collides right
		if(x[0] == BOARD_WIDTH) {
			running = false;
		}
		// top border
		if(y[0] < 0) {
			running = false;
		}
		//bottom border
		if(y[0] == BOARD_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//Game Over Screen
		
		g.setColor(Color.red);
		g.setFont(new Font("Sans Serrif",Font.BOLD, 65));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over"))/2, BOARD_HEIGHT/2);
		
		// score on game over screen
		g.setColor(Color.white);
		g.setFont(new Font("Sans Serrif",Font.BOLD, 35));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+ dotsEaten, (BOARD_WIDTH - metrics2.stringWidth("Score: "+ dotsEaten))/2, g.getFont().getSize());
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkScore();
			checkCollision();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if(direction !='R') {
				direction = 'L';
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(direction !='L') {
				direction = 'R';
			}
			break;
		case KeyEvent.VK_UP:
			if(direction !='D') {
				direction = 'U';
			}
			break;
		case KeyEvent.VK_DOWN:
			if(direction !='U') {
				direction = 'D';
			}
			break;
		
		}
	}
		
	}

}
