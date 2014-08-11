import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;

public class MainWindow {

	protected static final String[] StatusOKStrings = {"All Systems Are Operational", "No Problems Here!", "Nothing to see, keep on moving.", "How about that weather?", "Allan please add StatusOK Strings", "Don't call me if you're seeing this message", "Wait, shouldn't this be blank?", "Jeremy! Help me in the Equipment Room!"};
	private JFrame form_InventoryExpress;
	private JTextField checkout_Box_Auth;
	private JTextField checkout_Box_Lend;
	private JTextField checkout_Box_Items;
	public DatabaseHandler it;
	private JLabel statusLabel;
	
	ActionListener timerProc = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			statusLabel.setForeground(Color.black);
			statusLabel.setText(StatusOKStrings[new Random().nextInt(StatusOKStrings.length)]);
			statusErrorReset.stop();
		}
	};
	private Timer statusErrorReset = new Timer(15000, timerProc);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.form_InventoryExpress.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void infoBox(String infoMessage, String location)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + location, JOptionPane.INFORMATION_MESSAGE);
    }

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		form_InventoryExpress = new JFrame();
		form_InventoryExpress.setTitle("InventoryExpress");
		form_InventoryExpress.setBounds(100, 100, 405, 558);
		form_InventoryExpress.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		form_InventoryExpress.getContentPane().setLayout(null);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		statusPanel.setBounds(0, 498, 389, 20);
		form_InventoryExpress.getContentPane().add(statusPanel);
		statusPanel.setLayout(null);
		
		statusLabel = new JLabel("All Systems Are Operational");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusLabel.setBounds(5, 4, 312, 14);
		statusPanel.add(statusLabel);
		
		JLabel versionLabel = new JLabel("Version 0.1");
		versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		versionLabel.setBounds(305, 4, 79, 14);
		statusPanel.add(versionLabel);
		
		JTabbedPane tabbedPane_Overlord = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_Overlord.setBounds(0, 0, 389, 498);
		form_InventoryExpress.getContentPane().add(tabbedPane_Overlord);
		
		JPanel tab_Checkout = new JPanel();
		tab_Checkout.setBackground(new Color(250, 215, 215));
		tabbedPane_Overlord.addTab("Checkout", null, tab_Checkout, null);
		tabbedPane_Overlord.setEnabledAt(0, true);
		tab_Checkout.setLayout(null);
		
		checkout_Box_Auth = new JTextField();
		checkout_Box_Auth.addKeyListener(new KeyAdapter() {
			private boolean keyIgnore = false;
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() > 64 && arg0.getKeyCode() < 91)
				{
					keyIgnore = true;
					arg0.consume();
					try { tossUnimportantError("No alphas allowed in UserID slot!"); } catch (IOException k) { infoBox(k.getMessage(), "Call Zach About This!"); }
				}
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String authorizee = it.checkUserCode(checkout_Box_Auth.getName(), false);
					if(!(authorizee == null))
					{
						checkout_Box_Auth.setText(authorizee);
						checkout_Box_Auth.setEnabled(false);
						checkout_Box_Lend.setEnabled(true);
						checkout_Box_Lend.requestFocus();
					}
					else
					{
						statusLabel.setForeground(Color.red);
						try { tossUnimportantError("User's ID Number not found OR invalid number entered"); } catch (IOException k) { infoBox(k.getMessage(), "Call Zach About This!"); }
						checkout_Box_Auth.setText("");
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if(keyIgnore)
				{
					e.consume();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(keyIgnore)
				{
					e.consume();
				}
			}
		});
		checkout_Box_Auth.setBounds(83, 11, 105, 20);
		tab_Checkout.add(checkout_Box_Auth);
		checkout_Box_Auth.setColumns(10);
		
		JLabel checkout_label_Auth = new JLabel("Authorization:");
		checkout_label_Auth.setBounds(10, 14, 80, 14);
		tab_Checkout.add(checkout_label_Auth);
		
		JLabel checkout_label_Lendee = new JLabel("Lendee:");
		checkout_label_Lendee.setBounds(198, 14, 46, 14);
		tab_Checkout.add(checkout_label_Lendee);
		
		checkout_Box_Lend = new JTextField();
		checkout_Box_Lend.addKeyListener(new KeyAdapter() {
			private boolean keyIgnore = false;
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() > 64 && e.getKeyCode() < 91)
				{
					keyIgnore = true;
					e.consume();
					try { tossUnimportantError("No alphas allowed in UserID slot!"); } catch (IOException k) { infoBox(k.getMessage(), "Call Zach About This!"); }
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String lendee = it.checkUserCode(checkout_Box_Lend.getName(), false);
					if(!(lendee == null))
					{
						checkout_Box_Lend.setText(lendee);
						checkout_Box_Lend.setEnabled(false);
						checkout_Box_Items.setEnabled(true);
						checkout_Box_Items.requestFocus();
					}
					else
					{
						try { tossUnimportantError("User's ID Number not found OR invalid number entered"); } catch (IOException k) { infoBox(k.getMessage(), "Call Zach About This!"); }
						checkout_Box_Lend.setText("");
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				if(keyIgnore)
				{
					e.consume();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(keyIgnore)
				{
					e.consume();
				}
			}
		});
		checkout_Box_Lend.setEnabled(false);
		checkout_Box_Lend.setBounds(242, 11, 112, 20);
		tab_Checkout.add(checkout_Box_Lend);
		checkout_Box_Lend.setColumns(10);
		
		JList<String> checkout_List_Items = new JList<String>();
		checkout_List_Items.setBorder(new LineBorder(new Color(0, 0, 0)));
		checkout_List_Items.setBounds(10, 42, 234, 411);
		tab_Checkout.add(checkout_List_Items);
		
		checkout_Box_Items = new JTextField();
		checkout_Box_Items.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		checkout_Box_Items.setEnabled(false);
		checkout_Box_Items.setBounds(254, 433, 120, 20);
		tab_Checkout.add(checkout_Box_Items);
		checkout_Box_Items.setColumns(10);
		
		JPanel tab_Returns = new JPanel();
		tabbedPane_Overlord.addTab("Returns", null, tab_Returns, null);
		
		JPanel tab_Admin = new JPanel();
		tab_Admin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				/*ArrayList<String> listArgs = new ArrayList<String>();
				try
				{
					Statement st = mainConnection.createStatement();
					ResultSet rs = st.executeQuery("SELECT id FROM people WHERE permissions > 2");
					for(;rs.next();)
					{
						listArgs.add(Integer.toString(rs.getInt(0)));
					}
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage());
				}
				String[] args = null; 
				listArgs.toArray(args);
				//boolean password = PasswordPopup.main(args);*/
			}
		});
		tabbedPane_Overlord.addTab("Administration", null, tab_Admin, null);
		
		JPanel tab_Stats = new JPanel();
		tabbedPane_Overlord.addTab("Statistics", null, tab_Stats, null);
		
		it = new DatabaseHandler(this);
	}
	
	File logFile = new File(System.getProperty("user.dir") + "/log.log");
	public void tossUnimportantError(String errorText) throws IOException
	{
		if(!logFile.exists()) {
			logFile.createNewFile();
		}
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFile.getAbsoluteFile(), true)))) {
		    out.println(errorText + " -- " + todaysDate());
		}catch (IOException e) {
		    infoBox(e.getMessage(), "Call Zach!");
		}
		statusLabel.setForeground(Color.red);
		statusLabel.setText(errorText + " -- " + todaysDate());
		if(statusErrorReset.isRunning()) {
			statusErrorReset.restart();
		}
		else {
			statusErrorReset.start();
		}
		Toolkit.getDefaultToolkit().beep();
	}
	public void tossNotification(String notification) throws IOException
	{
		statusLabel.setForeground(Color.getHSBColor(0.32f, 0.67f, 0.5f));
		statusLabel.setText(notification + " -- " + todaysDate());
		if(statusErrorReset.isRunning()) {
			statusErrorReset.restart();
		}
		else {
			statusErrorReset.start();
		}
	}

	protected String todaysDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss aa");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
}
