import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;


public class PasswordPopup {

	private JFrame popup;
	private JTextField box_Scan;
	private static List<String> pws;
	private static boolean correct = false;

	/**
	 * Launch the application.
	 * @return 
	 */
	public static boolean main(String[] args) {
		pws = Arrays.asList(args);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PasswordPopup window = new PasswordPopup();
					window.popup.setVisible(true);
				} catch (Exception e) {
					if(e instanceof StopException)
					{
						//Do Nothing, just stopping the runnable;
					}
					else
					{
						e.printStackTrace();
					}
				}
			}
		});
		return correct;
	}

	/**
	 * Create the application.
	 * @throws StopException 
	 */
	public PasswordPopup() throws StopException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws StopException {
		popup = new JFrame();
		popup.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				box_Scan.requestFocus();
			}
		});
		popup.setResizable(false);
		popup.setTitle("Password");
		popup.setBounds(100, 100, 324, 151);
		popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popup.getContentPane().setLayout(null);
		
		JLabel label_Please = new JLabel("Please Scan an Administrative ID Badge");
		label_Please.setBounds(61, 30, 190, 14);
		popup.getContentPane().add(label_Please);
		
		box_Scan = new JTextField();
		box_Scan.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(pws.contains(box_Scan.getText()))
					{
						correct = true;
					}
					//throw new StopException();
				}
			}
		});
		box_Scan.setBounds(109, 55, 86, 20);
		popup.getContentPane().add(box_Scan);
		box_Scan.setColumns(10);
	}

}
