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
import java.awt.Font;

/**
 * A simple Swing-based calculator application that supports basic arithmetic operations, decimals, and parentheses.
 */
public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_screen;
	private JButton btnNewButton_clear;
	private JButton btnNewButton_multiply;
	private JButton btnNewButton_divide;
	private JButton btnNewButton_add;
	private JButton btnNewButton_subtract;
	private JButton btnNewButton_equals;
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
	private JButton btnNewButton_point;

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
	 * Checks if an object is a single digit.
	 * This method is quite complex for its purpose and handles String, Character, and Integer types.
	 * @param obj The object to check.
	 * @return true if the object represents a single digit, false otherwise.
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
	 * @param items The ArrayList to search in.
	 * @param item The item to search for.
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
	 * Performs calculations on a list of numbers and operators, respecting operator precedence (* and / before + and -).
	 * This method modifies the list in-place.
	 * Note: This method does not handle parentheses.
	 * @param items An ArrayList containing a mix of Double and Character (operator) objects.
	 * @return The final result of the calculation as a Double.
	 */
	public static Double calculation(ArrayList<Object> items){
        int i = 0;
        while (contains(items,'/')||contains(items, '*')||i<items.size()){
            if (items.get(i).equals('/')){
                Double number1 = (Double)items.get(i-1);
                Double number2 = (Double)items.get(i+1);
                items.set(i-1, number1/number2);
                items.remove(i);
                items.remove(i);
                continue;
            }
            if (items.get(i).equals('*')){
                Double number1 = (Double)items.get(i-1);
                Double number2 = (Double)items.get(i+1);
                items.set(i-1, number1*number2);
                items.remove(i);
                items.remove(i);
                continue;
            }
            i++; 
        }
        int j = 0;
        while (contains(items,'+')||contains(items, '-')||j<items.size()){
            if (items.get(j).equals('+')){
                Double number1 = (Double)items.get(j-1);
                Double number2 = (Double)items.get(j+1);
                items.set(j-1, number1+number2);
                items.remove(j);
                items.remove(j);
                continue;
            }
            if (items.get(j).equals('-')){
                Double number1 = (Double)items.get(j-1);
                Double number2 = (Double)items.get(j+1);
                items.set(j-1, number1-number2);
                items.remove(j);
                items.remove(j);
                continue;
            }
            j++; 
        }
        return (Double)items.get(0);
    }

	/**
	 * Splits an expression string into a list of numbers (Doubles) and operators (Characters).
	 * For example, "12.5+3" becomes [12.5, '+', 3.0].
	 * @param string The mathematical expression as a String.
	 * @return An ArrayList of objects representing the parsed expression.
	 */
	public static ArrayList<Object> split(String string){
        ArrayList<Object> items = new ArrayList<Object>();
        ArrayList<String> numbers = new ArrayList<String>();
        
        for (char c : string.toCharArray()) {
            if(isDigit(c) || c=='.'){
                numbers.add(""+c);
            }
            else{
                if (numbers.size()>0){
                    items.add(Double.parseDouble(String.join("",numbers)));
                }
                items.add(c);
                numbers = new ArrayList<String>();
            }       
        }
        if (numbers.size()>0){
            items.add(Double.parseDouble(String.join("",numbers)));
        }
        return items;
    }

	/**
	 * Finds and calculates expressions within parentheses.
	 * It iteratively finds the innermost parentheses, calculates the expression inside, and replaces the parentheses and their content with the result.
	 * @param items The list of parsed expression tokens (numbers and operators).
	 * @return The list with all parentheses-enclosed expressions resolved.
	 */
	public static ArrayList<Object> calculationInParentheses(ArrayList<Object> items){
        while (contains(items,'(')&&contains(items,')')){
            ArrayList<Object> inParentheses = new ArrayList<Object>(items.subList(items.indexOf('(')+1,items.indexOf(')')));
            int size = inParentheses.size();
            Double result = calculation(inParentheses);
            int start = items.indexOf('(');
            for (int j=0 ; j<=size; j++){
                items.remove(start+1);
            }
            items.set(start, result);
        }
        return items;
    }

	/**
	 * The main calculation trigger method. It takes the expression from the text field,
	 * tokenizes it, solves parentheses, performs the final calculation, and displays the result or an error message.
	 * @param textField The text field containing the expression to calculate.
	 */
	public static void calculate(JTextField textField){
        try {
        	ArrayList<Object> items = split(textField.getText());
            items = calculationInParentheses(items);
            calculation(items);
            textField.setText(""+items.get(0));
        }
        catch(Exception e) {
        	System.out.println(e.getMessage());
        	textField.setText("ERROR!");
        	return;
        }
    }

	/**
	 * Appends a given string (from a button press) to the calculator's display.
	 * @param choice The string to append (e.g., a digit or an operator).
	 */
	public void click(String choice) {
		String text = textField_screen.getText();
		textField_screen.setText(text+choice);
	}

    /**
     * Clears the calculator's display.
     * @param textField The text field to clear.
     */
    public void clear(JTextField textField) {
    	textField.setText("");
    }
    /**
     * Removes the last character from the calculator's display (backspace functionality).
     * @param textField The text field to modify.
     */
    public void remove(JTextField textField) {
    	if(textField.getText().length()==0) {
    		return;
    	}
    	String[] elements = new String[textField.getText().length()];
    	int trucker = 0;
    	for(char c : textField.getText().toCharArray()) {
    		elements[trucker] = ""+c;
    		trucker++;
    	}
    	textField.setText(String.join("", Arrays.copyOf(elements, elements.length-1)));
    }

	/**
	 * Constructor for the Calculator. Initializes the GUI components and sets up the frame.
	 */
	public Calculator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 521, 314);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 0, 426, 44);
		contentPane.add(panel);
		panel.setLayout(null);
		
		textField_screen = new JTextField();
		textField_screen.setFont(new Font("Dialog", Font.BOLD, 14));
		textField_screen.setBounds(0, 0, 245, 44);
		textField_screen.setColumns(10);
		panel.add(textField_screen);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 61, 245, 202);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
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
		
		btnNewButton_point = new JButton(".");
		btnNewButton_point.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_point.getText());
			}
		});
		btnNewButton_point.setBounds(185, 80, 48, 48);
		panel_1.add(btnNewButton_point);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(269, 61, 240, 202);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		
		JButton btnNewButton_delete = new JButton("D");
		btnNewButton_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(textField_screen);
			}
		});
		btnNewButton_delete.setBounds(167, 131, 48, 48);
		panel_2.add(btnNewButton_delete);
		
		btnNewButton_clear = new JButton("C");
		btnNewButton_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(textField_screen);
			}
		});
		btnNewButton_clear.setBounds(113, 131, 48, 48);
		panel_2.add(btnNewButton_clear);
		
		btnNewButton_multiply = new JButton("*");
		btnNewButton_multiply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_multiply.getText());
			}
		});
		btnNewButton_multiply.setBounds(60, 22, 48, 48);
		panel_2.add(btnNewButton_multiply);
		
		btnNewButton_divide = new JButton("/");
		btnNewButton_divide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_divide.getText());
			}
		});
		btnNewButton_divide.setBounds(113, 22, 48, 48);
		panel_2.add(btnNewButton_divide);
		
		btnNewButton_add = new JButton("+");
		btnNewButton_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_add.getText());
			}
		});
		btnNewButton_add.setBounds(113, 76, 48, 48);
		panel_2.add(btnNewButton_add);
		
		btnNewButton_subtract = new JButton("-");
		btnNewButton_subtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_subtract.getText());
			}
		});
		btnNewButton_subtract.setBounds(167, 22, 48, 48);
		panel_2.add(btnNewButton_subtract);
		
		btnNewButton_equals = new JButton("=");
		btnNewButton_equals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate(textField_screen);
			}
		});
		btnNewButton_equals.setBounds(60, 76, 48, 48);
		panel_2.add(btnNewButton_equals);
		
		JButton btnNewButton_paranthes = new JButton("(  )");
		btnNewButton_paranthes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click("()");
			}
		});
		btnNewButton_paranthes.setBounds(167, 76, 48, 48);
		panel_2.add(btnNewButton_paranthes);
		

	}
}
