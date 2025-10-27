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
import javax.swing.SwingConstants;
import java.awt.Color;

/**
 * A simple calculator application with a graphical user interface built using Java Swing.
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
     * Casts a Double to a Long if it has no fractional part, otherwise returns the Double.
     * @param number The Double to cast.
     * @return A Long if the number is a whole number, otherwise the original Double.
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
     * Checks if an object represents a single digit.
     * @param obj The object to check (can be String, Character, or Integer).
     * @return true if the object is a digit, false otherwise.
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
     * @param items The ArrayList to search through.
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
     * Calculates the exponent of a number (a^b) for positive integer exponents.
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
     * Performs a specific arithmetic operation (+, -, *, /, ^) on a list of numbers and operators.
     * The list is modified in-place.
     * @param items The list of numbers (Doubles) and operators (Characters).
     * @param operator The character of the operation to perform.
     * @return The modified list after performing all occurrences of the specified operation.
     */
	private static ArrayList<Object> operation(ArrayList<Object> items , char operator){
		int i = 0;
        while (contains(items,operator) && i<items.size()){
            if (items.get(i).equals(operator)){
                Double number1 = (Double)items.get(i-1);
                Double number2 = (Double)items.get(i+1);
                switch(operator) {
                case '^':
                	items.set(i-1, exponent(number1,number2));
                	break;
                case '*':
                	items.set(i-1, number1*number2);
                	break;
                case '/':
                	items.set(i-1, number1/number2);
                	break;
                case '+':
                	items.set(i-1, number1+number2);
                	break;
                case '-':
                	items.set(i-1, number1-number2);
                	break;
                }
                items.remove(i);
                items.remove(i);
                i = 0; // Reset index to re-scan from the beginning
                continue; // Continue to the next iteration of the loop
            }
            i++;
        }
        return items;
	}

	/**
     * Calculates the result of an expression stored in a list, respecting the order of operations.
     * @param items The list of numbers and operators.
     * @return The final result of the calculation as a Double.
     */
	private static Double calculation(ArrayList<Object> items){
		operation(items,'^');
		operation(items,'*');operation(items,'/');
		operation(items,'+');operation(items,'-');
        return (Double)items.get(0);
    }

	/**
     * Splits a mathematical expression string into a list of numbers (Doubles) and operators (Characters).
     * @param string The input expression string.
     * @return An ArrayList of Objects, containing Doubles for numbers and Characters for operators.
     */
	private static ArrayList<Object> split(String string){
        ArrayList<Object> items = new ArrayList<Object>();
        ArrayList<String> number = new ArrayList<String>();
        
        for (char c : string.toCharArray()) {
			// If the character is a digit or a decimal point, append it to the current number
            if(isDigit(c) || c=='.'){
            	number.add(""+c);
            }
            else{
                if (number.size()>0){
                    items.add(Double.parseDouble(String.join("",number)));
                }
                items.add(c);
                number = new ArrayList<String>();
            }       
        }
        if (number.size()>0){
            items.add(Double.parseDouble(String.join("",number)));
        }
        return items;
    }

	/**
     * Finds and evaluates expressions within parentheses in the list.
     * @param items The list of numbers and operators, which may include parentheses.
     */
	private static void calculationInParentheses(ArrayList<Object> items){
        while (contains(items,'(')&&contains(items,')')){
            ArrayList<Object> inParentheses = new ArrayList<Object>(items.subList(items.indexOf('(')+1,items.indexOf(')')));
            int size = inParentheses.size();
            int start = items.indexOf('(');
            for (int j=0 ; j<=size; j++){
                items.remove(start+1);
            }
            items.set(start, calculation(inParentheses));
        }
    }

	/**
     * Main calculation function. It takes the text from the screen, processes it, and displays the result or an error.
     * @param textField The text field containing the expression to calculate.
     */
	private static void calculate(JTextField textField){
        try {
        	ArrayList<Object> items = split(textField.getText());
            calculationInParentheses(items);
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
     * Appends the given string (choice) to the text field.
     * @param choice The string to append, usually from a button press.
     */
	private void click(String choice) {
		String text = textField_screen.getText();
		textField_screen.setText(text+choice);
	}

	/**
     * Clears the text field.
     * @param textField The text field to clear.
     */
	private void clear(JTextField textField) {
    	textField.setText("");
    }
	/**
     * Removes the last character from the text field.
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
     * Calculates the square root of the number in the text field using the Babylonian method.
     * @param textField The text field containing the number.
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
     * Toggles between inserting an opening and a closing parenthesis.
     * @return A "(" if no parenthesis is open, or a ")" if one is.
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
	 * Create the frame.
	 */
	public Calculator() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 12, 407, 54);
		contentPane.add(panel);
		panel.setLayout(null);
		
		textField_screen = new JTextField();
		textField_screen.setForeground(new Color(0, 51, 204));
		textField_screen.setEditable(false);
		textField_screen.setFont(new Font("Dialog", Font.BOLD, 20));
		textField_screen.setBounds(38, 12, 328, 30);
		textField_screen.setColumns(10);
		panel.add(textField_screen);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(22, 71, 408, 242);
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
		btnNewButton_00.setBounds(12, 188, 48, 48);
		panel_1.add(btnNewButton_00);
		
		btnNewButton_point = new JButton(".");
		btnNewButton_point.setFont(new Font("Dialog", Font.BOLD, 18));
		btnNewButton_point.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_point.getText());
			}
		});
		btnNewButton_point.setBounds(239, 134, 48, 48);
		panel_1.add(btnNewButton_point);		
		
		JButton btnNewButton_delete = new JButton("D");
		btnNewButton_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(textField_screen);
			}
		});
		btnNewButton_delete.setBounds(186, 133, 48, 48);
		panel_1.add(btnNewButton_delete);
		
		btnNewButton_clear = new JButton("clear");
		btnNewButton_clear.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear(textField_screen);
			}
		});
		btnNewButton_clear.setBounds(186, 188, 100, 48);
		panel_1.add(btnNewButton_clear);
		
		btnNewButton_multiply = new JButton("*");
		btnNewButton_multiply.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton_multiply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_multiply.getText());
			}
		});
		btnNewButton_multiply.setBounds(239, 79, 48, 48);
		panel_1.add(btnNewButton_multiply);
		
		btnNewButton_divide = new JButton("/");
		btnNewButton_divide.setFont(new Font("Dialog", Font.BOLD, 18));
		btnNewButton_divide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_divide.getText());
			}
		});
		btnNewButton_divide.setBounds(186, 79, 48, 48);
		panel_1.add(btnNewButton_divide);
		
		btnNewButton_add = new JButton("+");
		btnNewButton_add.setFont(new Font("Dialog", Font.BOLD, 16));
		btnNewButton_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_add.getText());
			}
		});
		btnNewButton_add.setBounds(293, 79, 48, 48);
		panel_1.add(btnNewButton_add);
		
		btnNewButton_subtract = new JButton("-");
		btnNewButton_subtract.setFont(new Font("Dialog", Font.BOLD, 22));
		btnNewButton_subtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(btnNewButton_subtract.getText());
			}
		});
		btnNewButton_subtract.setBounds(347, 79, 48, 48);
		panel_1.add(btnNewButton_subtract);
		
		btnNewButton_equals = new JButton("=");
		btnNewButton_equals.setFont(new Font("Dialog", Font.BOLD, 16));
		btnNewButton_equals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate(textField_screen);
			}
		});
		btnNewButton_equals.setBounds(293, 134, 102, 102);
		panel_1.add(btnNewButton_equals);
		
		JButton btnNewButton_paranthes = new JButton("( )");
		btnNewButton_paranthes.setFont(new Font("Dialog", Font.BOLD, 14));
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
		btnNewButton_exp.setFont(new Font("Dialog", Font.BOLD, 16));
		btnNewButton_exp.setBounds(347, 25, 48, 48);
		panel_1.add(btnNewButton_exp);
		
		JButton btnNewButton_sqr = new JButton("sqrt");
		btnNewButton_sqr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				squirRoot(textField_screen);
			}
		});
		btnNewButton_sqr.setFont(new Font("Dialog", Font.BOLD, 13));
		btnNewButton_sqr.setBounds(239, 25, 102, 48);
		panel_1.add(btnNewButton_sqr);
		
		JButton btnNewButton_py = new JButton("py");
		btnNewButton_py.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(""+(double)22/7);
			}
		});
		btnNewButton_py.setFont(new Font("Dialog", Font.BOLD, 11));
		btnNewButton_py.setBounds(119, 188, 48, 48);
		panel_1.add(btnNewButton_py);
		
		JButton btnNewButton_eulir = new JButton("e");
		btnNewButton_eulir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				click(""+(double)19/7);
			}
		});
		btnNewButton_eulir.setFont(new Font("Dialog", Font.BOLD, 13));
		btnNewButton_eulir.setBounds(65, 188, 48, 48);
		panel_1.add(btnNewButton_eulir);
		

	}
}
