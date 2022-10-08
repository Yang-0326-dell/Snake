import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Background extends Canvas {
	final static int default_mode = 1;

	Settings settings = new Settings();
	private int[][] num = new int[Settings.blockheight][Settings.blockwidth];
	public BufferedImage bi ;
	static public BufferedImage block = new BufferedImage(Settings.blockwidth, Settings.blockheight,
			BufferedImage.TYPE_INT_RGB);
	private boolean initialized = false;

	BufferedImage obj;
	int objx, objy, objwidth, objheight;

	Background(int mode) {
		generateimg(mode);
	}

	private void generateimg(int mode) {
		bi= new BufferedImage(Settings.screenwidth, Settings.screenheight,
				BufferedImage.TYPE_INT_RGB);
		switch (mode) {
		case default_mode:
			for (int y = 0; y < Settings.blockheight; y++) {
				for (int x = 0; x < Settings.blockwidth; x++) {
					double sq = abs((x + y - (Settings.blockwidth + Settings.blockheight + 0.0) / 2) / 2)
							+ abs((x - y - (Settings.blockwidth - Settings.blockheight + 0.0) / 2) / 2);
					int colormix = (int) Math.floor(sq * 128 / Settings.blockwidth);
					num[y][x] = colormix << 16 | colormix << 8 | (int) (3.5 * colormix);
				}
			}
			break;

		default:
			break;
		}
		for (int y = 0; y < Settings.blocky; y++) {
			for (int x = 0; x < Settings.blockx; x++) {
				for (int j = 0; j < Settings.blockheight; j++) {
					for (int i = 0; i < Settings.blockwidth; i++) {
						bi.setRGB(x * Settings.blockwidth + i, y * Settings.blockheight + j, (int) num[i][j]);
					}
				}
			}
		}	
		for (int j = 0; j < Settings.blockheight; j++) {
			for (int i = 0; i < Settings.blockwidth; i++) {
				block.setRGB( i,  j, (int) num[i][j]);
			}
		}
	}

	public void add_object(BufferedImage obj, int x, int y, int width, int height) {
		this.obj = obj;
		this.objx = x;
		this.objy = y;
		this.objwidth = width;
		this.objheight = height;
	}

	public int ddddd=0;
	//@Override
	public void paintSelf(Snake snake) {
		Graphics g=super.getGraphics();
		if (ddddd<2) {
			g.drawImage(bi, 0, 0,Settings.screenwidth,Settings.screenheight, this);
			initialized= true;
				ddddd++;
				//if(ddddd==2) {	
					for(int i=0;i<snake.length;i++) {
							if(i==snake.start_serial) {
									g.drawImage(Snake.head[snake.snake_body[i].direction],snake.snake_body[i].px*Settings.blockwidth,snake.snake_body[i].py*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
								}
							else {
								g.drawImage(Snake.tail[snake.snake_body[i].direction],snake.snake_body[i].px*Settings.blockwidth,snake.snake_body[i].py*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
							}
						}
				//}
				this.paintApple(Engine.apple);
				this.paintBoom(Engine.boom);
				this.layOutScore();
				}
		
		else {
			for(int i=0;i<snake.length;i++) {
				if(i==snake.start_serial) {
						g.drawImage(Snake.head[snake.snake_body[i].direction],snake.snake_body[i].px*Settings.blockwidth,snake.snake_body[i].py*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
						
				}
				else if(i==snake.second_serial) {
					g.drawImage(block,snake.snake_body[i].px*Settings.blockwidth,snake.snake_body[i].py*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
					g.drawImage(Snake.tail[snake.snake_body[i].direction],snake.snake_body[i].px*Settings.blockwidth,snake.snake_body[i].py*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
				}
				else {
					//g.drawImage(Snake.tail[snake.snake_body[i].direction],snake.snake_body[i].px*Settings.blockwidth,snake.snake_body[i].py*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
				}
				}
			if(snake.rubbish.erase) {
				g.drawImage(block, snake.rubbish.px*Settings.blockwidth, snake.rubbish.py*Settings.blockheight, Settings.blockwidth,Settings.blockheight,this);
			}
		//复杂性的擦除暂时不解决
		//System.out.println("1");
		}
	}
	
	public void layOutScore() {
		Graphics g=super.getGraphics();
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		FontMetrics fontMetrics = g.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth("score: "+Engine.score);
		int stringHeight = fontMetrics.getAscent();
		g.setColor(Color.getColor("", 200<<16|200<<8|255));
		g.fillRect(Settings.screenwidth/2-stringWidth/2, (int)(Settings.screenheight*1.1-stringHeight), stringWidth, stringHeight);
		g.setColor(Color.black);
		g.drawString("score: "+Engine.score,Settings.screenwidth/2-stringWidth/2 , (int)(Settings.screenheight*1.1));
	}
	
	public void displayLeftSecond() {
		Graphics g=super.getGraphics();
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		FontMetrics fontMetrics = g.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth("Game Over");
		int stringHeight = fontMetrics.getAscent();
		if(Engine.boomLeftSecond!=-1) {
		g.setColor(Color.black);
		g.fillRect(Settings.screenwidth, (int)(Settings.screenheight/2-stringHeight), stringWidth, stringHeight);
		g.setColor(Color.red);
		g.drawString(Engine.boomLeftSecond+" LEFT",Settings.screenwidth , (int)(Settings.screenheight/2));
	}
		else {
			g.setColor(Color.white);
			g.fillRect(Settings.screenwidth, (int)(Settings.screenheight/2-stringHeight), stringWidth, stringHeight);
		}
	}

	public void paintApple(Apple apple) {
		Graphics g=super.getGraphics();
		//若希望同时多个苹果，此处需更改
		g.drawImage(Apple.apple,apple.position[0]*Settings.blockwidth,apple.position[1]*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
	}
	
	public void paintBoom(Boom boom) {
		Graphics g=super.getGraphics();
		//若希望同时多个红点，此处需更改
		g.drawImage(Boom.boom,boom.position[0]*Settings.blockwidth,boom.position[1]*Settings.blockheight,Settings.blockwidth,Settings.blockheight,this);
	}
	
	public void drawGameOver() {
		Graphics g=super.getGraphics();
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		FontMetrics fontMetrics = g.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth("Game Over");
		int stringHeight = fontMetrics.getAscent();
		g.setColor(Color.getColor("",100<<16|100<<8|150));
		g.fillRect(Settings.screenwidth/2-stringWidth/2, Settings.screenheight/2-stringHeight, stringWidth, stringHeight);
		g.setColor(Color.red);
		g.drawString("Game Over",Settings.screenwidth/2-stringWidth/2, (int)(Settings.screenheight/2));
	}

	private double abs(double i) {
		// TODO Auto-generated method stub
		return i >= 0 ? i : -i;
	}
}
