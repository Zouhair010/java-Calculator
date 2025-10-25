import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Main class for the Calculator, extending JFrame to create the application window.

public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_screen;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private JButton btnNewButton_4;
	private JButton btnNewButton_5;
	private JButton btnNewButton_6;
	private JButton btnNewButton_7;
	private JButton btnNewButton_02;
	private JButton btnNewButton_01;
	private JButton btnNewButton_03;
	private JButton btnNewButton_04;
	private JButton btnNewButton_05;
	private JButton btnNewButton_06;
	private JButton btnNewButton_07;
	private JButton btnNewButton_08;
	private JButton btnNewButton_09;
	private JButton btnNewButton_00;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator frame = new Calculator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Appends the clicked button's character to the calculator screen.
	 * @param choice The string from the button that was clicked.
	 */
	public void click(String choice) {
		String text = textField_screen.getText();
		textField_screen.setText(text+choice);
	}
	/**
	 * Checks if the given object represents a single digit.
	 * @param obj The object to check (can be String, Character, or Integer).
	 * @return true if the object is a digit, false otherwise.
	 */
	public static boolean isDigit(Object obj){
        if (obj instanceof String){
            try{
                Integer n =  Integer.valueOf(obj.toString());
                if(n.toString().equals(obj)){
                    return true;
                }
            }
            catch (NumberFormatException e){
                return false;
            }
        }
        else if (obj instanceof Character){
            try{
                Integer n = Integer.valueOf(obj.toString());
                Character c = n.toString().toCharArray()[0];
                if(c.equals(obj)){
                    return true;
                }
            }
            catch (NumberFormatException e){
                return false;
            }
        }
        else if (obj instanceof Integer){
            return true;
        }
        return false;
	}

	/**
	 * Checks if an ArrayList contains a specific item.
	 * @param items The ArrayList of objects to search through.
	 * @param item The object to find.
	 * @return true if the item is found, false otherwise.
	 */
	public static boolean contains(ArrayList<Object> items ,Object item){
        for (Object i : items){
            if (i.equals(item)){
                return true;
            }
        }
        return false;
    }

    /**
     * Parses the expression in the text field, calculates the result, and displays it.
     * It follows the order of operations by first calculating division and multiplication,
     * then addition and subtraction. This version works with Integers.
     * @param textField The text field containing the mathematical expression.
     */
    public static void result(JTextField textField){
        
        ArrayList<Object> items = new ArrayList<>(); // Holds numbers (Integer) and operators (Character)
        ArrayList<String> numbers = new ArrayList<>(); // Temporarily holds digits of a number
        
        // 1. Parse the input string into numbers and operators.
        for (char c : textField.getText().toCharArray()) {
            if(isDigit(c)){
                numbers.add(""+c);
            }
            else{
                items.add(Integer.valueOf(String.join("",numbers)));
                items.add(c);
                numbers = new ArrayList<String>();
            }       
        }
        items.add(Integer.valueOf(String.join("",numbers)));

        // 2. Perform multiplication and division operations (higher precedence).
        int i = 0;
        while (contains(items,'/')||contains(items, '*')){
            // Find the first '*' or '/'
            if (items.get(i).equals('/')){
                Integer n1 = (Integer)items.get(i-1);
                Integer n2 = (Integer)items.get(i+1);
                items.set(i-1, n1/n2);
                items.remove(i);
                items.remove(i);
                continue;
            }
            if (items.get(i).equals('*')){
                Integer n1 = (Integer)items.get(i-1);
                Integer n2 = (Integer)items.get(i+1);
                items.set(i-1, n1*n2);
                items.remove(i);
                items.remove(i);
                continue;
            }
            // If the end of the list is reached, reset the index to scan again.
            if (i>=items.size()){
                i = 0;
                continue;
            }
            i++; 
        }
        // 3. Perform addition and subtraction operations (lower precedence).
        int j = 0;
        while (contains(items,'+')||contains(items, '-')){
            // Find the first '+' or '-'
            if (items.get(j).equals('+')){
                Integer n1 = (Integer)items.get(j-1);
                Integer n2 = (Integer)items.get(j+1);
                items.set(j-1, n1+n2);
                items.remove(j);
                items.remove(j);
                continue;
            }
            if (items.get(j).equals('-')){
                Integer n1 = (Integer)items.get(j-1);
                Integer n2 = (Integer)items.get(j+1);
                items.set(j-1, n1-n2);
                items.remove(j);
                items.remove(j);
                continue;
            }
            // If the end of the list is reached, reset the index to scan again.
            if (j>=items.size()){
                j = 0;
                continue;
            }
            j++; 
        }
        // 4. Display the final result.
        textField.setText(""+items.get(0));
    }

    /**
     * Clears the calculator screen.
     * @param textField The text field to clear.
     */
    public void clear(JTextField textField) {
    	textField.setText("");
    }
    /**
     * Removes the last character from the calculator screen (backspace).
     * @param textField The text field to modify.
     */
    public void remove(JTextField textField) {
    	String[] elements = new String[textField.getText().length()];
    	int trucker = 0;
    	// This can be simplified using: textField.getText().substring(0, textField.getText().length() - 1)
    	for(char c : textField.getText().toCharArray()) {
    		elements[trucker] = ""+c;
    		trucker++;
    	}
    	textField.setText(String.join("", Arrays.copyOf(elements, elements.length-1)));
    }

	/**
	 * Create the frame.
	 */
	public Calculator() {
		// --- JFrame Setup ---
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // Using absolute positioning
		
		// --- Display Screen Panel ---
		JPanel panel = new JPanel();
		panel.setBounds(12, 0, 426, 44);
		contentPane.add(panel);
		panel.setLayout(null);
		
		// --- Text Field for Display ---
		textField_screen = new JTextField();
		textField_screen.setBounds(89, 0, 218, 44);
		textField_screen.setColumns(10);
		panel.add(textField_screen);
		
		// --- Number Pad Panel ---
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 61, 245, 202);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		// --- Number Buttons (0-9) ---
		// Each button's ActionListener calls the click() method with its text.
		btnNewButton_02 = new JButton("2");
		btnNewButton_02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_02.getText());
			}
		});
		btnNewButton_02.setBounds(71, 26, 48, 48);
		panel_1.add(btnNewButton_02);
		
		btnNewButton_01 = new JButton("1");
		btnNewButton_01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_01.getText());
			}
		});
		btnNewButton_01.setBounds(18, 26, 48, 48);
		panel_1.add(btnNewButton_01);
		
		btnNewButton_03 = new JButton("3");
		btnNewButton_03.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_03.getText());
			}
		});
		btnNewButton_03.setBounds(125, 26, 48, 48);
		panel_1.add(btnNewButton_03);
		
		btnNewButton_04 = new JButton("4");
		btnNewButton_04.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_04.getText());
			}
		});
		btnNewButton_04.setBounds(18, 80, 48, 48);
		panel_1.add(btnNewButton_04);
		
		btnNewButton_05 = new JButton("5");
		btnNewButton_05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_05.getText());
			}
		});
		btnNewButton_05.setBounds(71, 80, 48, 48);
		panel_1.add(btnNewButton_05);
		
		btnNewButton_06 = new JButton("6");
		btnNewButton_06.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_06.getText());
			}
		});
		btnNewButton_06.setBounds(125, 80, 48, 48);
		panel_1.add(btnNewButton_06);
		
		btnNewButton_07 = new JButton("7");
		btnNewButton_07.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_07.getText());
			}
		});
		btnNewButton_07.setBounds(18, 135, 48, 48);
		panel_1.add(btnNewButton_07);
		
		btnNewButton_08 = new JButton("8");
		btnNewButton_08.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_08.getText());
			}
		});
		btnNewButton_08.setBounds(71, 135, 48, 48);
		panel_1.add(btnNewButton_08);
		
		btnNewButton_09 = new JButton("9");
		btnNewButton_09.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_09.getText());
			}
		});
		btnNewButton_09.setBounds(125, 135, 48, 48);
		panel_1.add(btnNewButton_09);
		
		btnNewButton_00 = new JButton("0");
		btnNewButton_00.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_00.getText());
			}
		});
		btnNewButton_00.setBounds(185, 135, 48, 48);
		panel_1.add(btnNewButton_00);
		
		// --- Operator and Control Button Panel ---
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(270, 61, 168, 202);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		
		// --- Control Buttons (Backspace, Clear) ---
		JButton btnNewButton = new JButton("<-");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(textField_screen);
			}
		});
		btnNewButton.setBounds(60, 131, 48, 48);
		panel_2.add(btnNewButton);
		
		btnNewButton_1 = new JButton("C");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(textField_screen);
			}
		});
		btnNewButton_1.setBounds(7, 132, 48, 48);
		panel_2.add(btnNewButton_1);
		
		// --- Operator Buttons (*, /, %, +, -, =) ---
		btnNewButton_2 = new JButton("*");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_2.getText());
			}
		});
		btnNewButton_2.setBounds(7, 23, 48, 48);
		panel_2.add(btnNewButton_2);
		
		btnNewButton_3 = new JButton("/");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_3.getText());
			}
		});
		btnNewButton_3.setBounds(60, 23, 48, 48);
		panel_2.add(btnNewButton_3);
		
		btnNewButton_4 = new JButton("%");
		btnNewButton_4.addActionListener(new ActionListener() {
			// This button's action listener is empty.
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_4.setBounds(114, 23, 48, 48);
		panel_2.add(btnNewButton_4);
		
		btnNewButton_5 = new JButton("+");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_5.getText());
			}
		});
		btnNewButton_5.setBounds(60, 77, 48, 48);
		panel_2.add(btnNewButton_5);
		
		btnNewButton_6 = new JButton("-");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_6.getText());
			}
		});
		btnNewButton_6.setBounds(114, 77, 48, 48);
		panel_2.add(btnNewButton_6);
		
		btnNewButton_7 = new JButton("=");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result(textField_screen);
			}
		});
		btnNewButton_7.setBounds(7, 77, 48, 48);
		panel_2.add(btnNewButton_7);
		

	}
}
