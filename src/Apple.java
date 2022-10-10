import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Apple extends Canvas{
	public static BufferedImage apple = new BufferedImage(Settings.blockwidth, Settings.blockheight,
			BufferedImage.TYPE_INT_RGB);
	//若同时多个苹果，则需更改
	public boolean eaten = false;
	public int[] position = new int[2];

	public void initialize() {
		int c2 = 0 << 16 | 255 << 8 | 0;
		int c1 = 0 << 16 | 0 << 8 | 0;
		int c3 = 255 << 16 | 0 << 8 | 0;
		int c0 = -1;
		// int[][] num=new int[Settings.blockwidth][Settings.blockheight];
		int[][] num = { { c0, c0, c0, c0, c0, c0, c1, c0, c0, c0 }, { c0, c0, c0, c0, c0, c1, c2, c2, c0, c0 },
				{ c0, c0, c3, c3, c1, c3, c2, c2, c2, c0 }, { c0, c3, c3, c3, c1, c3, c2, c2, c3, c0 },
				{ c3, c3, c1, c1, c1, c3, c1, c3, c3, c3 }, { c3, c3, c3, c1, c1, c1, c3, c3, c3, c3 },
				{ c3, c3, c3, c3, c3, c3, c3, c3, c3, c3 }, { c3, c3, c3, c3, c3, c3, c3, c3, c3, c3 },
				{ c0, c3, c3, c3, c3, c3, c3, c3, c3, c0 }, { c0, c0, c3, c3, c3, c3, c3, c3, c0, c0 } };

		for (int y = 0; y < Settings.blockheight; y++) {
			for (int x = 0; x < Settings.blockwidth; x++) {
				double sq = Math.abs((x + y - (Settings.blockwidth + Settings.blockheight + 0.0) / 2) / 2)
						+ Math.abs((x - y - (Settings.blockwidth - Settings.blockheight + 0.0) / 2) / 2);
				int colormix = (int) Math.floor(sq * 128 / Settings.blockwidth);
				apple.setRGB(x, y, colormix << 16 | colormix << 8 | (int) (3.5 * colormix));
			}
		}
		int k1 = Settings.blockwidth / Settings.applewidth;
		int k2 = Settings.blockheight / Settings.appleheight;
		//BufferedImage b1 = new BufferedImage(Settings.appleheight, Settings.applewidth, BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < Settings.appleheight; j++) {
			for (int i = 0; i < Settings.applewidth; i++) {
				if (num[j][i] != -1) {
					apple.setRGB(i * k1, j * k2, num[j][i]);
					apple.setRGB(i * k1 + 1, j * k2, num[j][i]);
					apple.setRGB(i * k1, j * k2 + 1, num[j][i]);
					apple.setRGB(i * k1 + 1, j * k2 + 1, num[j][i]);
				}
			}
		}
	}

	public void generateApple() {
		eaten = false;
		Random r = new Random();
		if(Settings.blockx * Settings.blocky - Engine.snake.length-1==0) {
			Engine.gameOver();
			return;
		}
		int seed = r.nextInt(0, Settings.blockx * Settings.blocky - Engine.snake.length-1);
		int it = 0;
		boolean[] map = new boolean[Settings.blockx * Settings.blocky];
		for (int l = 0; l < Engine.snake.length; l++) {
			map[Engine.snake.snake_body[l].px + Settings.blockx * Engine.snake.snake_body[l].py] = true;
		}
		for (int j = 0; j < Settings.blocky; j++) {
			for (int i = 0; i < Settings.blockx; i++) {
				if (map[j * Settings.blockx + i]) {
					continue;
				} else {
					if (it == seed) {
						position[0] = i; 
						position[1] = j;
					}
				}
				it++;
			}
		}
	}
}

class Boom extends Apple{
	public static BufferedImage boom = new BufferedImage(Settings.blockwidth, Settings.blockheight,
			BufferedImage.TYPE_INT_RGB);
	public static long millisecond=0;
	public static boolean appear=false;
	
	//@Override 
	public void initializeBoom() {
		position[1]=-1;
		position[0]=-1;
		int c2 = 0 << 16 | 255 << 8 | 0;
		int c1 = 0 << 16 | 0 << 8 | 0;
		int c3 = 255 << 16 | 215 << 8 | 0;
		int c0 = -1;
		// int[][] num=new int[Settings.blockwidth][Settings.blockheight];
		int[][] num = { { c0, c0, c0, c0, c0, c0, c1, c0, c0, c0 }, { c0, c0, c0, c0, c0, c1, c2, c2, c0, c0 },
				{ c0, c0, c3, c3, c1, c3, c2, c2, c2, c0 }, { c0, c3, c3, c3, c1, c3, c2, c2, c3, c0 },
				{ c3, c3, c1, c1, c1, c3, c1, c3, c3, c3 }, { c3, c3, c3, c1, c1, c1, c3, c3, c3, c3 },
				{ c3, c3, c3, c3, c3, c3, c3, c3, c3, c3 }, { c3, c3, c3, c3, c3, c3, c3, c3, c3, c3 },
				{ c0, c3, c3, c3, c3, c3, c3, c3, c3, c0 }, { c0, c0, c3, c3, c3, c3, c3, c3, c0, c0 } };

		for (int y = 0; y < Settings.blockheight; y++) {
			for (int x = 0; x < Settings.blockwidth; x++) {
				double sq = Math.abs((x + y - (Settings.blockwidth + Settings.blockheight + 0.0) / 2) / 2)
						+ Math.abs((x - y - (Settings.blockwidth - Settings.blockheight + 0.0) / 2) / 2);
				int colormix = (int) Math.floor(sq * 128 / Settings.blockwidth);
				boom.setRGB(x, y, colormix << 16 | colormix << 8 | (int) (3.5 * colormix));
			}
		}
		int k1 = Settings.blockwidth / Settings.applewidth;
		int k2 = Settings.blockheight / Settings.appleheight;
		for (int j = 0; j < Settings.appleheight; j++) {
			for (int i = 0; i < Settings.applewidth; i++) {
				if (num[j][i] != -1) {
					boom.setRGB(i * k1, j * k2, num[j][i]);
					boom.setRGB(i * k1 + 1, j * k2, num[j][i]);
					boom.setRGB(i * k1, j * k2 + 1, num[j][i]);
					boom.setRGB(i * k1 + 1, j * k2 + 1, num[j][i]);
				}
			}
		}
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
}