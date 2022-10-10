
public class Settings {
	public static int mainpagewidth=800;
	public static int mainpageheight=500;
	
	public static int blockx=13;
	public static int blocky=17;
	public static int blockxmax=30;
	public static int blockxmin=3;
	public static int blockymax=30;
	public static int blockymin=3;
	public static int blockwidth=20;
	public static int blockheight=20;
	public static int screenwidth=blockx*blockwidth;
	public static int screenheight=blocky*blockheight;
	public static int facewidth=screenwidth+200;
	public static int faceheight=screenheight+100;
	
	public static int defaultlength=3;
	public static int snakewidth=10;
	public static int snakeheight=10;
	public static int move_fps=1000;
	public static int move_fps_max=1000;
	
	public static int applewidth=10;
	public static int appleheight=10;
	public static int boomPerScore=3;
	public static int secondPerBoom=6;
	public static float secondPerScore=(float) 1.5;

	public static int secondPerAcc=1000;
	public static float accRate=(float) 2.0;
	
	public String[] username;
	
	public static void update() {
		screenwidth=blockx*blockwidth;
		screenheight=blocky*blockheight;
		facewidth=screenwidth+200;
		faceheight=screenheight+100;
	}
	
}
