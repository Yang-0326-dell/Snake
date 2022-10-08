import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.EventHandler;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

public class Engine {
	public static boolean winitialized=false;
	public static boolean hinitialized=false;
	public static boolean initialized=false;

	public static JFrame f = new JFrame();
	public static JFrame f1 = new JFrame();
	public static JPanel p =new JPanel();

	static Background bg = new Background(1);
	static boolean repaint = false;
	static Snake snake;
	static int score = 0;
	static boolean gameCease = false;
	static boolean gameCeaseForced = false;
	static boolean directionChange = false;
	static int gap = Settings.move_fps;
	static Apple apple = new Apple();
	static Boom boom=new Boom();
	static int boomLeftSecond=-1;
	static boolean gameOver=false;
	static boolean 不给红点=true;

	public static void createUI() {
		while(!initialized) {
			try {
				Thread.currentThread();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		f1.setVisible(false);
		Settings.update();
		
		snake=new Snake();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(Settings.facewidth, Settings.faceheight+70);
		f.addKeyListener(new CheckState());
		f.addWindowStateListener(new CheckWindow());
		f.addWindowListener(new CheckWindow());
		f.setLayout(null);
		f.setVisible(true);

		JPanel j1=new JPanel();
		j1.setLocation(0,0);
		j1.setSize(Settings.facewidth,Settings.faceheight);
		j1.setLayout(null);
		bg = new Background(1);
		bg.setLocation(0,0);
		bg.setSize(Settings.facewidth,Settings.faceheight);
		j1.add(bg);
		
		JPanel j2=new JPanel();
		j2.setLocation(0,Settings.faceheight);
		j2.setSize(Settings.facewidth,20);
		j2.setLayout(null);
		JLabel properties = new JLabel(Settings.blockx+"*"+Settings.blocky+" Speed="+Settings.move_fps_max/Settings.move_fps);
		Font font=new Font("黑体",Font.PLAIN,10);
		properties.setSize(Settings.facewidth, 20);
		properties.setLocation(0,0);
		j2.add(properties);
		
		snake.initialize(Snake.default_mode);
//		f.add(bg);
//		f.add(properties);
		f.add(j1);
		f.add(j2);
		apple.initialize();
		boom.initializeBoom();
		apple.generateApple();
		bg.paintApple(apple);
		// f.add(scoreOutput);
		f.setFocusable(true);
		f.addKeyListener(new CheckState());
		f.setVisible(true);

		int i = 0;
		while (true) {
			try {
				Thread.currentThread();
				Thread.sleep(gap);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!gameOver&&!gameCease && !gameCeaseForced) {
				//Check whether apple is eaten
				 if(apple.eaten) {//此句块需置于检测状态模块的前面
					 不给红点=false;
				apple.generateApple();
				bg.layOutScore();
				}
				bg.paintApple(apple);
				//Decide whether red spot is eaten
				if(boomLeftSecond==0) {
					boom.appear=false;
					boomLeftSecond=-1;
					bg.displayLeftSecond();
					不给红点=true;
				}
				if(score%6==0&&score>0&&boom.appear==false&&!不给红点) {
					boom.appear=true;
					boom.millisecond=System.currentTimeMillis();
					boom.generateApple();
				}
				if(boom.appear) {
					boomLeftSecond=Settings.secondPerBoom-(int) ((System.currentTimeMillis()-boom.millisecond)/1000);
					bg.displayLeftSecond();
					bg.paintBoom(boom);
				}
				
				boolean[] snakeState = snake.updateState();
				if (snakeState[0]) {
					apple.eaten = true;
					score++;
					System.out.println("Eaaaaa");
				}
				if(snakeState[1]&&!不给红点) {
					boom.eaten = true;
					boom.appear=false;
					score+=boomLeftSecond*3;
					boomLeftSecond=-1;
					bg.displayLeftSecond();
					bg.layOutScore();
					不给红点=true;
				}
				if (!snakeState[2]) {
					bg.paintSelf(snake);
				} else {
					gameOver();
				}
				f.setVisible(true);
				// System.out.println(i);
				i += 10;
			}
		}
	}
	
	public static void mainpage() {
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setSize(Settings.mainpagewidth, Settings.mainpageheight);
		f1.setLayout(null);
		
		JPanel jp1=new JPanel();
		JPanel jp2=new JPanel();
		JPanel jp3=new JPanel();
		
		Font font=new Font("宋体",Font.PLAIN,20);
		//输入难度
		jp1.setLocation(10,30);
		jp1.setSize(300,50);
		jp1.setLayout(null);
		JLabel jl1=new JLabel("难度:");
		jl1.setFont(font);
		jl1.setLocation(0,0);
		jl1.setSize(100,20);
		Choice c1 = new Choice();
		c1.setLocation(100,0);
		c1.setSize(40,20);
		c1.add("1");
		c1.add("2");
		c1.add("3");
		c1.add("4");
		c1.add("5");
		c1.add("6");
		c1.add("7");
		c1.add("8");
		c1.add("9");
		c1.add("10");
		c1.add("11");
		c1.add("12");
		c1.add("13");
		c1.add("14");
		c1.add("15");
		c1.add("16");
		c1.add("17");
		c1.add("18");
		c1.add("19");
		c1.add("20");
		jp1.add(jl1);
		jp1.add(c1);
		c1.addItemListener(new CheckChoiceAction(c1));
		
		//输入大小
		jp2.setLocation(10,100);
		jp2.setSize(300,50);
		jp2.setLayout(null);
		JLabel jl2=new JLabel("地图大小:");
		jl2.setFont(font);
		jl2.setLocation(0,0);
		jl2.setSize(100,20);
		JLabel jl3=new JLabel("宽=");
		jl3.setLocation(100,0);
		jl3.setSize(50,20);
		JLabel jl4=new JLabel("长=");
		jl4.setLocation(200,0);
		jl4.setSize(50,20);
		JLabel jl5=new JLabel(" ");
		jl5.setLocation(0,20);
		jl5.setSize(800,20);
		JTextField jt1=new JTextField();
		JTextField jt2=new JTextField();
		jt1.setLocation(130,0);
		jt1.setSize(50,20);
		jt2.setLocation(230,0);
		jt2.setSize(50,20);
		jp2.add(jl2);
		jp2.add(jl3);
		jp2.add(jl4);
		jp2.add(jl5);
		jp2.add(jt1);
		jp2.add(jt2);
		Document dt1 = jt1.getDocument();
		Document dt2 = jt2.getDocument();
		
		//显示确认键
		jp3.setLocation(200,200);
		jp3.setSize(200,50);
		jp3.setLayout(null);
		JButton jb=new JButton("Confirm");
		jb.setSize(100,20);
		jb.setLocation(0,0);
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				initialized=true;
			}
			
		});
		jp3.add(jb);
		jb.setVisible(false);
		dt1.addDocumentListener(new CheckTextInput(jt1, CheckTextInput.width, jl5,jb));
		dt2.addDocumentListener(new CheckTextInput(jt2, CheckTextInput.height, jl5,jb));
		
		f1.add(jp1);
		f1.add(jp2);
		f1.add(jp3);
		jp1.setVisible(true);
		jp2.setVisible(true);
		jl5.setVisible(true);
		f1.setVisible(true);
		//jl.setVisible(true);
	}

	public static void repaintbg() {
		bg.ddddd = 0;
		bg.paintSelf(snake);
	}

	public static void changeDirection(byte dir) {
		if (dir != -1) {
			if (!directionChange) {
				if (!(((dir + 1) * (snake.direction + 1)) == 2) && !(((dir + 1) * (snake.direction + 1)) == 12)) {
					snake.direction = dir;
				}
				directionChange = true;
			}
		}
	}

	public static void cease() {
		gap = 0;
		gameCease = true;
	}

	public static void continueGame() {
		gap = Settings.move_fps;
		gameCease = false;
		repaintbg();
	}

	public static void forceCease() {
		gap = 0;
		gameCeaseForced = true;
	}

	public static void forceContinue() {
		gap = Settings.move_fps;
		gameCeaseForced = false;
		repaintbg();
	}

	public static void gameOver() {
		gameOver=true;
		bg.drawGameOver();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainpage();
		createUI();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//createUI();
			}
		});
		
	}
}
