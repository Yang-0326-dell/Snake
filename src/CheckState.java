import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class CheckState implements KeyListener {
	public void initialize() {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		byte dir = -1;
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			dir = Part.down;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			dir = Part.up;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dir = Part.right;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dir = Part.left;
		} else if (e.getKeyCode() == KeyEvent.VK_ALT) {
			Engine.repaintbg();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Engine.forceCease();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Engine.forceContinue();
		}
		else if(e.getKeyCode()==KeyEvent.VK_CONTROL) {
			Engine.beginAcc();
		}
		Engine.changeDirection(dir);

		// System.out.println("ONCE");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_CONTROL) {
			Engine.endAcc();
		}
	}

}

class CheckWindow implements WindowListener, WindowStateListener {
	public void windowActivated(WindowEvent e) {
		// 将 Window 设置为活动 Window 时调用。
		// Engine.repaintbg();
		Engine.continueGame();
	}

	public void windowClosed(WindowEvent e) {
		// 因对窗口调用 dispose 而将其关闭时调用。
	}

	public void windowClosing(WindowEvent e) {
		// 用户试图从窗口的系统菜单中关闭窗口时调用。
	}

	public void windowDeactivated(WindowEvent e) {
		// 当 Window 不再是活动 Window 时调用。
		Engine.cease();
	}

	public void windowDeiconified(WindowEvent e) {
		// 窗口从最小化状态变为正常状态时调用。
		// Engine.repaintbg();
		Engine.continueGame();
	}

	public void windowIconified(WindowEvent e) {
		// 窗口从正常状态变为最小化状态时调用。
		Engine.cease();
	}

	public void windowOpened(WindowEvent e) {
		// 窗口首次变为可见时调用。
		Engine.repaintbg();
	}

	public void windowStateChanged(WindowEvent e) {
		Engine.repaintbg();
//窗口状态改变时调用。 
	}
}

class CheckChoiceAction implements ItemListener {
	Choice c;
	CheckChoiceAction(Choice d) {
		c = d;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		System.out.println("打印选择项：" + c.getSelectedIndex());
		Settings.move_fps = (int) (Settings.move_fps_max / (1+c.getSelectedIndex()));
	}

}

class CheckTextInput implements DocumentListener {
	JTextField jt;
	JLabel err;
	int widthOrHeight = 0;
	JButton jb;
	static final int width = 1;
	static final int height = 2;

	CheckTextInput(JTextField j, int x, JLabel e,JButton jbb) {
		jt = j;
		widthOrHeight = x;
		err = e;
		jb=jbb;
	}

	// @Override
	public void textValueChanged() {
		// TODO Auto-generated method stub
		int w,h;
		if (widthOrHeight == width) {
			if (jt.getText().length() > 0 && jt.getText().length() < 3) {
				try {
					w = Integer.parseInt(jt.getText());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Engine.winitialized = false;
					jb.setVisible(false);
					return;
				}
				if (String.valueOf(w).length() == jt.getText().length()) {
					if (w > Settings.blockxmax || w < Settings.blockxmin) {
						err.setText("Unvalid input, width should be bigger than " + Settings.blockxmin
								+ " and smaller than " + Settings.blockxmax + " .");
						Engine.winitialized = false;
						jb.setVisible(false);
					} else {
						err.setText("");
						Engine.winitialized = true;
						Settings.blockx=w;
						if(Engine.hinitialized)
						jb.setVisible(true);
					}
				} else {
					Engine.winitialized = false;
					jb.setVisible(false);
					err.setText("Unvalid input, only number is available.");
					// System.out.println("Unvalid input, only number is available.");
				}
			}
			else {
				Engine.winitialized = false;
				jb.setVisible(false);
				err.setText("Unvalid input, too long or void.");
			}
		} 
		else if (widthOrHeight == height) {
			if (jt.getText().length() > 0 && jt.getText().length() < 3) {
				try {
					h = Integer.parseInt(jt.getText());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Engine.hinitialized = false;
					jb.setVisible(false);
					return;
				}
				if (String.valueOf(h).length() == jt.getText().length()) {
					if (h > Settings.blockymax || h < Settings.blockymin) {
						err.setText("Unvalid input, height should be bigger than " + Settings.blockymin
								+ " and smaller than " + Settings.blockymax + " .");
						jb.setVisible(false);
						Engine.hinitialized = false;
					} 
					else {
						err.setText("");
						Engine.hinitialized = true;
						Settings.blocky=h;
						if(Engine.hinitialized)
						jb.setVisible(true);
					}
				} 
				else {
					Engine.hinitialized = false;
					jb.setVisible(false);
					err.setText("Unvalid input, only number is available.");
				}
			}
			else {
				Engine.hinitialized = false;
				jb.setVisible(false);
				err.setText("Unvalid input, too long or void.");
			}
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		textValueChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		textValueChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
	}

}

class CheckButtonAction implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
