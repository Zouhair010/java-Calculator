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
 * The main class for the Calculator application, extending JFrame to create the GUI.
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
	
	// State variable to track whether a parenthesis has been opened and not yet closed.
	private boolean parenthesesOpen = false;

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
     * Casts a Double to a Long if it represents a whole number, otherwise returns the Double.
     * This is used for cleaner display of results (e.g., "5" instead of "5.0").
     * @param number The number to cast.
     * @return A Long if it's a whole number, otherwise the original Double.
     */
    private static Object cast(Double number) {
    	long longNumber = number.longValue();
        double doubleNumber = (double)longNumber;
        if (doubleNumber==number){
            return longNumber;
        }
        return number;
    }

	/**
	 * Checks if an object is a single digit.
	 * This method is quite complex for its purpose and handles String, Character, and Integer types.
	 * @param obj The object to check.
	 * @return true if the object represents a single digit, false otherwise.
	 */
	private static boolean isDigit(Object obj){
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
	private static boolean contains(ArrayList<Object> items ,Object item){
        for (Object i : items){
            if (i.equals(item)){
                return true;
            }
        }
        return false;
    }

	/**
	 * Calculates the power of a number (a^b) using a simple loop.
	 * Note: This implementation is intended for positive integer exponents.
	 * @param a The base.
	 * @param b The exponent.
	 * @return The result of a raised to the power of b.
	 */
	private static Double exponent(Double a, Double b) {
		Double times = b;
		Double exp = (double) 1;
		while (times>0) {
			exp*=a;
			times--;
		}
		return exp;
	}

	/**
	 * Performs calculations on a list of numbers and operators, respecting operator precedence.
	 * The order of operations is: 1st pass for power (^), 2nd for multiplication/division (*, /),
	 * and 3rd for addition/subtraction (+, -).
	 * This method modifies the list in-place by replacing operations with their results.
	 * @param items An ArrayList containing a mix of Double and Character (operator) objects.
	 * @return The final result of the calculation as a Double.
	 */
	private static Double calculation(ArrayList<Object> items){
		int i = 0; // Pass 1: Handle exponents (^)
        while (contains(items,'^')||i<items.size()){
            if (items.get(i).equals('^')){
                Double number1 = (Double)items.get(i-1);
                Double number2 = (Double)items.get(i+1);
                items.set(i-1, exponent(number1,number2));
                items.remove(i);
                items.remove(i);
                continue;
            }
            i++; 
        }
        int j = 0; // Pass 2: Handle multiplication and division (*, /)
        while (contains(items,'/')||contains(items, '*')||j<items.size()){
            if (items.get(j).equals('/')){
                Double number1 = (Double)items.get(j-1);
                Double number2 = (Double)items.get(j+1);
                items.set(j-1, number1/number2);
                items.remove(j);
                items.remove(j);
                continue;
            }
            if (items.get(j).equals('*')){
                Double number1 = (Double)items.get(j-1);
                Double number2 = (Double)items.get(j+1);
                items.set(j-1, number1*number2);
                items.remove(j);
                items.remove(j);
                continue;
            }
            j++; 
        }
        int k = 0; // Pass 3: Handle addition and subtraction (+, -)
        while (contains(items,'+')||contains(items, '-')||k<items.size()){
            if (items.get(k).equals('+')){
                Double number1 = (Double)items.get(k-1);
                Double number2 = (Double)items.get(k+1);
                items.set(k-1, number1+number2);
                items.remove(k);
                items.remove(k);
                continue;
            }
            if (items.get(k).equals('-')){
                Double number1 = (Double)items.get(k-1);
                Double number2 = (Double)items.get(k+1);
                items.set(k-1, number1-number2);
                items.remove(k);
                items.remove(k);
                continue;
            }
            k++; 
        }
        return (Double)items.get(0);
    }

	/**
	 * Splits an expression string into a list of numbers (Doubles) and operators (Characters).
	 * For example, "12.5+3" becomes [12.5, '+', 3.0].
	 * @param string The mathematical expression as a String.
	 * @return An ArrayList of objects representing the parsed expression.
	 */
	private static ArrayList<Object> split(String string){
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
	 * It iteratively finds the first pair of parentheses, calculates the expression inside,
	 * and replaces the parentheses and their content with the result.
	 * Note: This does not handle nested parentheses correctly, e.g., (3*(4+5)).
	 * @param items The list of parsed expression tokens (numbers and operators).
	 * @return The list with all top-level parentheses-enclosed expressions resolved.
	 */
	private static ArrayList<Object> calculationInParentheses(ArrayList<Object> items){
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
	 * tokenizes it, solves parentheses, performs the final calculation, and displays
	 * the formatted result or an error message.
	 * @param textField The text field containing the expression to calculate.
	 */
	private static void calculate(JTextField textField){
        try {
        	ArrayList<Object> items = split(textField.getText());
            items = calculationInParentheses(items);
            calculation(items);
            textField.setText(""+cast((Double) items.get(0)));
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
	private void click(String choice) {
		String text = textField_screen.getText();
		textField_screen.setText(text+choice);
	}

    /**
     * Clears the calculator's display.
     * @param textField The text field to clear.
     */
	private void clear(JTextField textField) {
    	textField.setText("");
    }

    /**
     * Removes the last character from the calculator's display (backspace functionality).
     * This implementation is inefficient but functional.
     * @param textField The text field to modify.
     */
	private void remove(JTextField textField) {
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
     * Calculates the square root of the number in the display using the Babylonian method.
     * The result replaces the current content of the text field.
     * @param textField The text field containing the number to find the square root of.
     */
	private static void squirRoot(JTextField textField) {
    	try {
    		String text = textField.getText();
        	Double initial = (Double.valueOf(text)+1)/10;
        	Double sqrtVal;
        	while(true) {
        		sqrtVal = (initial+(Double.valueOf(text)/initial))/2;
        		if (initial.equals(sqrtVal)) {
        			break;
        		}
        		initial = sqrtVal;
        	}
        	textField.setText(sqrtVal.toString());
    	}
    	catch(Exception e) {
        	System.out.println(e.getMessage());
        	textField.setText("ERROR!");
        	return;
        }
	}

	/**
     * Toggles between inserting an opening or closing parenthesis.
     * It uses the `parenthesesOpen` state variable to decide which character to return.
     * @return "(" if no parenthesis is open, ")" if one is already open.
     */
	private String parantheses() {
    	if(!parenthesesOpen) {
    		parenthesesOpen = true;
    		return "(";
    	}
    	else {
    		parenthesesOpen = false;
    		return ")";
    	}
    }

	/**
	 * Constructor for the Calculator. Initializes the GUI components and sets up the frame.
	 */
	public Calculator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 0, 352, 44);
		contentPane.add(panel);
		panel.setLayout(null);
		
		textField_screen = new JTextField();
		textField_screen.setEditable(false);
		textField_screen.setFont(new Font("Dialog", Font.BOLD, 14));
		textField_screen.setBounds(66, -1, 218, 44);
		textField_screen.setColumns(10);
		panel.add(textField_screen);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(22, 49, 353, 264);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		btnNewButton_02 = new JButton("2");
		btnNewButton_02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_02.getText());
			}
		});
		btnNewButton_02.setBounds(65, 25, 48, 48);
		panel_1.add(btnNewButton_02);
		
		btnNewButton_01 = new JButton("1");
		btnNewButton_01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_01.getText());
			}
		});
		btnNewButton_01.setBounds(12, 25, 48, 48);
		panel_1.add(btnNewButton_01);
		
		btnNewButton_03 = new JButton("3");
		btnNewButton_03.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_03.getText());
			}
		});
		btnNewButton_03.setBounds(119, 25, 48, 48);
		panel_1.add(btnNewButton_03);
		
		btnNewButton_04 = new JButton("4");
		btnNewButton_04.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_04.getText());
			}
		});
		btnNewButton_04.setBounds(12, 79, 48, 48);
		panel_1.add(btnNewButton_04);
		
		btnNewButton_05 = new JButton("5");
		btnNewButton_05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_05.getText());
			}
		});
		btnNewButton_05.setBounds(65, 79, 48, 48);
		panel_1.add(btnNewButton_05);
		
		btnNewButton_06 = new JButton("6");
		btnNewButton_06.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_06.getText());
			}
		});
		btnNewButton_06.setBounds(119, 79, 48, 48);
		panel_1.add(btnNewButton_06);
		
		btnNewButton_07 = new JButton("7");
		btnNewButton_07.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_07.getText());
			}
		});
		btnNewButton_07.setBounds(12, 134, 48, 48);
		panel_1.add(btnNewButton_07);
		
		btnNewButton_08 = new JButton("8");
		btnNewButton_08.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_08.getText());
			}
		});
		btnNewButton_08.setBounds(65, 134, 48, 48);
		panel_1.add(btnNewButton_08);
		
		btnNewButton_09 = new JButton("9");
		btnNewButton_09.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_09.getText());
			}
		});
		btnNewButton_09.setBounds(119, 134, 48, 48);
		panel_1.add(btnNewButton_09);
		
		btnNewButton_00 = new JButton("0");
		btnNewButton_00.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_00.getText());
			}
		});
		btnNewButton_00.setBounds(65, 188, 48, 48);
		panel_1.add(btnNewButton_00);
		
		btnNewButton_point = new JButton(".");
		btnNewButton_point.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_point.getText());
			}
		});
		btnNewButton_point.setBounds(119, 188, 48, 48);
		panel_1.add(btnNewButton_point);		
		
		JButton btnNewButton_delete = new JButton("D");
		btnNewButton_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(textField_screen);
			}
		});
		btnNewButton_delete.setBounds(293, 133, 48, 48);
		panel_1.add(btnNewButton_delete);
		
		btnNewButton_clear = new JButton("C");
		btnNewButton_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(textField_screen);
			}
		});
		btnNewButton_clear.setBounds(239, 188, 48, 48);
		panel_1.add(btnNewButton_clear);
		
		btnNewButton_multiply = new JButton("*");
		btnNewButton_multiply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_multiply.getText());
			}
		});
		btnNewButton_multiply.setBounds(186, 79, 48, 48);
		panel_1.add(btnNewButton_multiply);
		
		btnNewButton_divide = new JButton("/");
		btnNewButton_divide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_divide.getText());
			}
		});
		btnNewButton_divide.setBounds(239, 79, 48, 48);
		panel_1.add(btnNewButton_divide);
		
		btnNewButton_add = new JButton("+");
		btnNewButton_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_add.getText());
			}
		});
		btnNewButton_add.setBounds(239, 133, 48, 48);
		panel_1.add(btnNewButton_add);
		
		btnNewButton_subtract = new JButton("-");
		btnNewButton_subtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_subtract.getText());
			}
		});
		btnNewButton_subtract.setBounds(293, 79, 48, 48);
		panel_1.add(btnNewButton_subtract);
		
		btnNewButton_equals = new JButton("=");
		btnNewButton_equals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate(textField_screen);
			}
		});
		btnNewButton_equals.setBounds(186, 133, 48, 48);
		panel_1.add(btnNewButton_equals);
		
		JButton btnNewButton_paranthes = new JButton("(  )");
		btnNewButton_paranthes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(parantheses());
			}
		});
		btnNewButton_paranthes.setBounds(186, 25, 48, 48);
		panel_1.add(btnNewButton_paranthes);
		
		JButton btnNewButton_exp = new JButton("^");
		btnNewButton_exp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_exp.getText());
			}
		});
		btnNewButton_exp.setFont(new Font("Dialog", Font.BOLD, 12));
		btnNewButton_exp.setBounds(293, 25, 48, 48);
		panel_1.add(btnNewButton_exp);
		
		JButton btnNewButton_sqr = new JButton("sqr");
		btnNewButton_sqr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				squirRoot(textField_screen);
			}
		});
		btnNewButton_sqr.setFont(new Font("Dialog", Font.BOLD, 9));
		btnNewButton_sqr.setBounds(239, 25, 48, 48);
		panel_1.add(btnNewButton_sqr);
		

	}
}
