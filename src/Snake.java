import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.*;

public class Snake {
	static final int default_mode = 1;
	static final byte[][] directionList = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

	static final byte crash = 1;
	static final byte eatFood = 2;
	static final byte eatBoom = 3;

	public int length = Settings.defaultlength;
	static BufferedImage tail_s = new BufferedImage(Settings.blockwidth, Settings.blockheight,
			BufferedImage.TYPE_INT_RGB);
	static BufferedImage head_s = new BufferedImage(Settings.blockwidth, Settings.blockheight,
			BufferedImage.TYPE_INT_RGB);
	static BufferedImage[] tail = new BufferedImage[4];
	static BufferedImage[] head = new BufferedImage[4];
	Part[] snake_body = new Part[length];
	public Part rubbish = new Part();
	public int start_serial = 0;
	public int second_serial = 1;
	public int end_serial = length - 1;
	public int end_serial_old;
	public byte direction = Part.left;
	private boolean addLength = false;

	public void initialize(int mode) {
		setbodyshape(mode);
		Random r=new Random();
		int seed=r.nextInt(0,Settings.blocky);

		for (int i = 0; i < length; i++) {
			snake_body[i] = new Part();
			snake_body[i].serial = i;
			snake_body[i].px = i;
			snake_body[i].py = seed;
			snake_body[i].direction = Part.left;
		}
		snake_body[0].hort = Part.head;
		
		snake_body[2].end = true;

	}

	private void setbodyshape(int mode) {
		for (int y = 0; y < Settings.blockheight; y++) {
			for (int x = 0; x < Settings.blockwidth; x++) {
				double sq = Math.abs((x + y - (Settings.blockwidth + Settings.blockheight + 0.0) / 2) / 2)
						+ Math.abs((x - y - (Settings.blockwidth - Settings.blockheight + 0.0) / 2) / 2);
				int colormix = (int) Math.floor(sq * 128 / Settings.blockwidth);
				tail_s.setRGB(x, y, colormix << 16 | colormix << 8 | (int) (3.5 * colormix));
				head_s.setRGB(x, y, colormix << 16 | colormix << 8 | (int) (3.5 * colormix));
			}
		}
		int k1 = Settings.blockwidth / Settings.snakewidth;
		int k2 = Settings.blockheight / Settings.snakeheight;
		int[][] buffer = new int[Settings.snakeheight][Settings.snakewidth];
		int[][] buffer1 = buffer;
		switch (mode) {
		case default_mode:
			int c0 = 50 << 16 | 50 << 8 | 150;
			int c1 = 200 << 16 | 200 << 8 | 0;
			int c2 = 100 << 16 | 100 << 8 | 50;
			int c3 = 255 << 16 | 255 << 8 | 255;
			int c4 = 0;
			int[][] buffertmp = { { c0, c0, c1, c1, c1, c1, c1, c1, c0, c0 },
					{ c0, c1, c1, c1, c1, c1, c1, c1, c1, c0 }, { c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 },
					{ c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 }, { c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 },
					{ c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 }, { c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 },
					{ c1, c1, c1, c2, c2, c2, c2, c1, c1, c1 }, { c0, c1, c1, c1, c1, c1, c1, c1, c1, c0 },
					{ c0, c0, c1, c1, c1, c1, c1, c1, c0, c0 }, };
			int[][] buffertmp1 = { { c0, c0, c4, c4, c1, c1, c4, c4, c0, c0 },
					{ c0, c4, c3, c3, c4, c4, c3, c3, c4, c0 }, { c1, c4, c3, c3, c4, c4, c3, c3, c4, c0 },
					{ c1, c1, c4, c4, c2, c2, c4, c4, c1, c1 }, { c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 },
					{ c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 }, { c1, c1, c1, c1, c2, c2, c1, c1, c1, c1 },
					{ c1, c1, c1, c2, c2, c2, c2, c1, c1, c1 }, { c0, c1, c1, c1, c1, c1, c1, c1, c1, c0 },
					{ c0, c0, c1, c1, c1, c1, c1, c1, c0, c0 }, };
			buffer = buffertmp;
			buffer1 = buffertmp1;
			break;
		default:
			break;
		}
		for (int i = 0; i < 4; i++) {
			head[i] = new BufferedImage(Settings.blockwidth, Settings.blockheight, BufferedImage.TYPE_INT_RGB);
			tail[i] = new BufferedImage(Settings.blockwidth, Settings.blockheight, BufferedImage.TYPE_INT_RGB);
		}
		for (int y = 0; y < Settings.snakeheight; y++) {
			for (int x = 0; x < Settings.snakewidth; x++) {
				if (buffer[y][x] != -1) {
					for (int i = 0; i < 2; i++) {
						for (int j = 0; j < 2; j++) {
							tail[Part.up].setRGB(x * k1 + i, y * k2 + j, buffer[y][x]);
							head[Part.up].setRGB(x * k1 + i, y * k2 + j, buffer1[y][x]);
							tail[Part.down].setRGB(x * k1 + i, y * k2 + j, buffer[Settings.snakeheight - 1 - y][x]);
							head[Part.down].setRGB(x * k1 + i, y * k2 + j, buffer1[Settings.snakeheight - 1 - y][x]);
							tail[Part.left].setRGB(x * k1 + i, y * k2 + j, buffer[x][y]);
							head[Part.left].setRGB(x * k1 + i, y * k2 + j, buffer1[x][y]);
							tail[Part.right].setRGB(x * k1 + i, y * k2 + j,
									buffer[Settings.snakewidth - 1 - x][Settings.snakeheight - 1 - y]);
							head[Part.right].setRGB(x * k1 + i, y * k2 + j,
									buffer1[Settings.snakewidth - 1 - x][Settings.snakeheight - 1 - y]);
						}
					}
				}
			}
		}
	}

	public boolean[] updateState() {
		int[] position = { snake_body[start_serial].px + directionList[direction][0],
				snake_body[start_serial].py + directionList[direction][1] };
		if (position[0] == -1) {
			position[0] = Settings.blockx - 1;
		} else if (position[0] == Settings.blockx) {
			position[0]= 0;
		} else if (position[1] == -1) {
			position[1] = Settings.blocky - 1;
		} else if (position[1] == Settings.blocky) {
			position[1] = 0;
		}
		boolean[] result = new boolean[3];
		if (checkFoodEaten(position)) {
			result[0] = true;
		}
	 if(checkBoomEaten(position)) {
		 result[1]=true;
	 }
		if (checkCrash(position)) {
			result[2] = true;
		} else {
			move();
		}
		return result;
	}

	boolean checkFoodEaten(int[] position) {// 如果想同时出现多个苹果，则以下代码需更改
		if (position[0] == Engine.apple.position[0] && position[1] == Engine.apple.position[1]) {
			addLength = true;
			return true;
		}
		return false;

	}
	boolean checkBoomEaten(int[] position) {// 如果想同时出现多个苹果，则以下代码需更改
		if (position[0] == Engine.boom.position[0] && position[1] == Engine.boom.position[1]) {
			return true;
		}
		return false;

	}

	boolean checkCrash(int[] position) {
		for (int i = 0; i < length; i++) {
			if (position[0] == snake_body[i].px && position[1] == snake_body[i].py) {
				return true;
			}
		}
		return false;
	}

	boolean move() {
		 //Adjust snake's head
		if (addLength) {
			rubbish.erase = false;
			for(int i=0;i<length;i++) {
				snake_body[i].serial+=1;
			}
			 Part[] tmp=new Part[length+1];
			 System.arraycopy(snake_body, 0, tmp, 0, length);
			 tmp[start_serial].hort=Part.tail;
			 tmp[length]=new Part();
			 snake_body=tmp;
			second_serial=start_serial;
			start_serial=length;
			length+=1;
			addLength=false;
		}
		else {
		rubbish.px = snake_body[end_serial].px;
		rubbish.py = snake_body[end_serial].py;
		rubbish.erase = true;
		second_serial=start_serial;
		start_serial = end_serial;
		for (int i = 0; i < length; i++) {
			if (i != start_serial) {
				snake_body[i].serial+=1;
				if(snake_body[i].serial==length-1) {
					end_serial=i;
				}
			}
		}
		}
		 snake_body[start_serial].px=snake_body[second_serial].px+directionList[direction][0];
		 snake_body[start_serial].py=snake_body[second_serial].py+directionList[direction][1];
		 snake_body[start_serial].direction=direction;
		 snake_body[start_serial].hort=Part.head;
		 snake_body[start_serial].serial=0;
		if (snake_body[start_serial].px == -1) {
			snake_body[start_serial].px = Settings.blockx - 1;
		} else if (snake_body[start_serial].px == Settings.blockx) {
			snake_body[start_serial].px = 0;
		} else if (snake_body[start_serial].py == -1) {
			snake_body[start_serial].py = Settings.blocky - 1;
		} else if (snake_body[start_serial].py == Settings.blocky) {
			snake_body[start_serial].py = 0;
		}
		
		Engine.directionChange = false;// Ensure that during the gap direction should only be changed once
		return false;
	}

}

class Part {

	static final boolean head = true;
	static final boolean tail = false;
	static final byte up = 0;
	static final byte down = 1;
	static final byte left = 2;
	static final byte right = 3;

	public byte direction = 0;
	public boolean hort = head;
	public boolean end = false;
	public int px = -1;
	public int py = -1;
	public int serial;
	public boolean erase = false;
}