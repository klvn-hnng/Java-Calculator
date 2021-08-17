/* Name: Kelvin Hung
 * Date: 8/5/19
 * Instructor: Ziaullah Khan
 * Class Section: 2336.0U1
 * 
 * Create a functional programmer calculator.
 * 
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

public class Calculator {
    private Color mainBackground = new Color(102, 102, 102);
    private Color buttonBackground = new Color(51, 51, 51);
    private JPanel panel = new JPanel(new GridBagLayout(), true);
    
    private JLabel expressionLabel = new JLabel("", SwingConstants.RIGHT);
    private JLabel inputBox = new JLabel("0", SwingConstants.RIGHT);
    
    private JButton fakeMenu = new JButton("≡");
    private JLabel title = new JLabel("Programmer");
    
    private JLabel hexLabel = new JLabel("0");
    private JLabel decLabel = new JLabel("0");
    private JLabel binLabel = new JLabel("0");
    
    private JButton buttonHex = new JButton("HEX");
    private JButton buttonDec = new JButton("DEC");
    private JButton buttonBin = new JButton("BIN");

    
    private JButton buttonUpArrow = new JButton("↑");
    private JButton buttonMod = new JButton("Mod");
    private JButton buttonCE = new JButton("CE");
    private JButton buttonClear = new JButton("C");
    private JButton buttonDelete = new JButton("⌫");
    private JButton buttonDiv = new JButton("÷");
    
    private JButton buttonA = new JButton("A");
    private JButton buttonB = new JButton("B");
    private JButton button7 = new JButton("7");
    private JButton button8 = new JButton("8");
    private JButton button9 = new JButton("9");
    private JButton buttonMult = new JButton("✕");
    
    private JButton buttonC = new JButton("C");
    private JButton buttonD = new JButton("D");
    private JButton button4 = new JButton("4");
    private JButton button5 = new JButton("5");
    private JButton button6 = new JButton("6");
    private JButton buttonSub = new JButton("-");
    
    private JButton buttonE = new JButton("E");
    private JButton buttonF = new JButton("F");
    private JButton button1 = new JButton("1");
    private JButton button2 = new JButton("2");
    private JButton button3 = new JButton("3");
    private JButton buttonAdd = new JButton("+");
    
    private JButton buttonOpenP = new JButton("(");
    private JButton buttonCloseP = new JButton(")");
    private JButton buttonNeg = new JButton("±");
    private JButton button0 = new JButton("0");
    private JButton buttonPoint = new JButton(".");
    private JButton buttonEqual = new JButton("=");
    
    private JButton[] fullLayout = new JButton[]{fakeMenu, buttonHex, buttonDec, buttonBin, 
    											 buttonUpArrow, buttonMod, buttonCE, buttonClear, buttonDelete, buttonDiv, 
    											 buttonA, buttonB, button7, button8, button9, buttonMult, 
    											 buttonC, buttonD, button4, button5, button6, buttonSub, 
    											 buttonE, buttonF, button1, button2, button3, buttonAdd, 
    											 buttonOpenP, buttonCloseP, buttonNeg, button0, buttonPoint, buttonEqual};
    private JButton[] mainButtons = new JButton[]{buttonUpArrow, buttonMod, buttonCE, buttonClear, buttonDelete, buttonDiv, 
    											 buttonA, buttonB, button7, button8, button9, buttonMult, 
    											 buttonC, buttonD, button4, button5, button6, buttonSub, 
    											 buttonE, buttonF, button1, button2, button3, buttonAdd, 
    											 buttonOpenP, buttonCloseP, buttonNeg, button0, buttonPoint, buttonEqual};
    private JButton[] valueKeys = new JButton[]{button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, 
    											buttonA, buttonB, buttonC, buttonD, buttonE, buttonF};
    private JButton[] numKeys = new JButton[]{button0, button1, button2, button3, button4, button5, button6, button7, button8, button9};
    private JButton[] letterKeys = new JButton[]{buttonA, buttonB, buttonC, buttonD, buttonE, buttonF};
    private JButton[] operatorKeys = new JButton[]{buttonMod, buttonDiv, buttonMult, buttonSub, buttonAdd, buttonOpenP, buttonCloseP, buttonEqual};
    private JButton[] otherKeys = new JButton[]{buttonCE, buttonClear, buttonDelete, buttonNeg};
    private Base key = Base.DECIMAL;
    private StringBuilder currentExpression = new StringBuilder();
    private ArrayList<String> fullExpression = new ArrayList<String>();

    private Calculator() {
        setupUI();

        ActionListener valueKeysListener = event -> {
            JButton src = (JButton) event.getSource();

            if (src == button0) {
                if (!(currentExpression.length() == 1 && currentExpression.charAt(0) == '0')) {
                    currentExpression.append("0");
                }
            } 
            else {
                currentExpression.append(src.getText());
            }
            setLabels();
        };
        
        for (JButton valueKey : valueKeys) valueKey.addActionListener(valueKeysListener);

        ActionListener operatorKeysListener = event -> {
            Object src = event.getSource();
            int count = 0;
            
            if (currentExpression.length() < 0) {
                return;
            }
            
            try {
            	fullExpression.add(Long.toString(Long.parseLong(currentExpression.toString(), key.getValue())));
            } catch (Exception e) {
                e.printStackTrace();
                
                return;
            }
            
            currentExpression.setLength(0);
            if (src == buttonMod) fullExpression.add("%");
            else if (src == buttonDiv) fullExpression.add("/");
            else if (src == buttonMult) fullExpression.add("*");
            else if (src == buttonSub) fullExpression.add("-");
            else if (src == buttonAdd) fullExpression.add("+");
            else if (src == buttonOpenP) {
                fullExpression.add("(");
                count++;
            } 
            else if (src == buttonCloseP) {
                fullExpression.add(")");
                count--;
            } 
            else if (src == buttonEqual) {
                try {
                    currentExpression.append(Long.toString(Math.round(EvaluateExpression
                            .evaluateExpression(fullExpression)), key.getValue()));
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    fullExpression.clear();
                }
                fullExpression.clear();
            }
            setLabels();
        };
        
        for (JButton operatorKey : operatorKeys) operatorKey.addActionListener(operatorKeysListener);

        ActionListener baseChangeListener = event -> {
            Object src = event.getSource();
            
            buttonHex.setBorder(BorderFactory.createEmptyBorder());
            buttonDec.setBorder(BorderFactory.createEmptyBorder());
            buttonBin.setBorder(BorderFactory.createEmptyBorder());
            
            
            // Switch the look between bases.
            if (src == buttonHex) {
                currentExpression.setLength(0);
                fullExpression.clear();
                for (JButton valueKey : valueKeys) {
                    valueKey.setEnabled(true);
                    valueKey.setForeground(Color.white);
                }
                buttonHex.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                key = Base.HEX;
                }
            	else if (src == buttonDec) {
                currentExpression.setLength(0);
                //fullExpression.clear();
                for (JButton numKey : numKeys) {
                    numKey.setEnabled(true);
                }
                for (JButton letterKey : letterKeys) {
                    letterKey.setEnabled(false);
                }
                buttonDec.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                key = Base.DECIMAL;
            	} 
            	else if (src == buttonBin) {
                currentExpression.setLength(0);
                fullExpression.clear();
                
                for (JButton valueKey : valueKeys) {
                    valueKey.setEnabled(false);
                }
                button0.setEnabled(true);
                button1.setEnabled(true);
                buttonBin.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                key = Base.BINARY;
            }
            setLabels();
        };
        
        buttonHex.addActionListener(baseChangeListener);
        buttonDec.addActionListener(baseChangeListener);
        buttonBin.addActionListener(baseChangeListener);

        ActionListener buttonListener = event -> {
            Object src = event.getSource();
            
            if (src == buttonCE) currentExpression.setLength(0);
            else if (src == buttonClear) {
                currentExpression.setLength(0);
                fullExpression.clear();
            } else if (src == buttonDelete) {
                currentExpression.deleteCharAt(currentExpression.length() - 1);
            } else if (src == buttonNeg) {
                if (currentExpression.length() > 0 && currentExpression.charAt(0) == '-')
                    currentExpression.deleteCharAt(0);
                else currentExpression.insert(0, "-");
            }
            setLabels();
        };
        for (JButton button : otherKeys) {
            button.addActionListener(buttonListener);
        }
    }

    private static void layoutGUI() {
        // Creating the layout.
        JFrame frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.setContentPane(new Calculator().panel);

        // Setting up the size of the layout.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height * 2 / 3;
        int width = screenSize.width / 3;
        frame.setSize(new Dimension(width, height));

        // Displays the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Calculator::layoutGUI);
    }

    private void setLabels() {
        expressionLabel.setText(listToString(fullExpression));
        if (currentExpression.length() == 0) {
            decLabel.setText("0");
            hexLabel.setText("0");
            binLabel.setText("0");
            inputBox.setText("0");
            
            return;
        }
        
        decLabel.setText(Long.toString(Long.parseLong(currentExpression.toString(), key.getValue())));
        
        hexLabel.setText(splitStringAtN(
                Long.toHexString(Long.parseLong(currentExpression.toString(), key.getValue())), 2));
        
        binLabel.setText("<html>"
                + splitStringAtN(
                Long.toBinaryString(Long.parseLong(currentExpression.toString(), key.getValue())), 4)
                + "</html>");
        
        switch (key) {
        
            case DECIMAL:
            	inputBox.setText(decLabel.getText());
                break;
                
            case HEX:
            	inputBox.setText(hexLabel.getText());
                break;
                
            case BINARY:
            	inputBox.setText(binLabel.getText());
                break;
        }
    }

    private String splitStringAtN(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 0 && (s.length() - i) % n == 0)
                sb.append(" ");
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    private String listToString(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    private void setupUI() {
        panel.add(fakeMenu, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, 
        						GridBagConstraints.CENTER, 
        						GridBagConstraints.BOTH, 
        						new Insets(0, 0, 0, 0), 0, 0));
        panel.add(title, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, 
        					 GridBagConstraints.LINE_START, 
        					 GridBagConstraints.BOTH, 
        						new Insets(0, 0, 0, 0), 0, 0));
        panel.add(expressionLabel, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, 
        								   GridBagConstraints.LINE_END, 
        								   GridBagConstraints.BOTH, 
        						new Insets(0, 0, 0, 0), 0, 0));
        panel.add(inputBox, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.1, 0.3, 
        							   GridBagConstraints.LINE_END, 
        							   GridBagConstraints.BOTH, 
        						new Insets(0, 0, 0, 0), 0, 0));
        panel.add(buttonHex, new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, 
        					   	 GridBagConstraints.CENTER, 
        					   	 GridBagConstraints.BOTH, 
        					   new Insets(0, 0, 0, 0), 0, 0));
        panel.add(hexLabel, new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, 
        						GridBagConstraints.CENTER, 
        						GridBagConstraints.BOTH, 
        						new Insets(0, 0, 0, 0), 0, 0));
        panel.add(buttonDec, new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, 
        				   		 GridBagConstraints.CENTER, 
        				   		 GridBagConstraints.BOTH, 
        				   		 new Insets(0, 0, 0, 0), 0, 0));
        panel.add(decLabel, new GridBagConstraints(1, 4, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, 
        						GridBagConstraints.CENTER, 
        						GridBagConstraints.BOTH, 
        						new Insets(0, 0, 0, 0), 0, 0));
        panel.add(buttonBin, new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1, 
        				   	 	 GridBagConstraints.CENTER, 
        				   	 	 GridBagConstraints.BOTH, 
        				   	 new Insets(0, 0, 0, 0), 0, 0));
        panel.add(binLabel, new GridBagConstraints(1, 5, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, 
        						GridBagConstraints.CENTER, 
        						GridBagConstraints.BOTH, 
        					new Insets(0, 0, 0, 0), 0, 0));
        
        for (int i = 0; i < mainButtons.length; i++) {
            panel.add(mainButtons[i], new GridBagConstraints((i % 6), (i / 6) + 6, 1, 1, 0.1, 0.1, 
            						 	 GridBagConstraints.CENTER, 
            						 	 GridBagConstraints.BOTH, 
            						 	 new Insets(2, 2, 2, 2), 2, 2));
        }
        expressionLabel.setForeground(Color.white);
        panel.setBackground(mainBackground);
        
        for (JButton button : fullLayout) {
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setForeground(Color.white);
        }
        for (JButton padButton : mainButtons) {
            padButton.setBackground(buttonBackground);
            padButton.setOpaque(true);
        }
        for (JButton letterKey : letterKeys) {
            letterKey.setEnabled(false);
        }
        fakeMenu.setEnabled(false);
        fakeMenu.setBackground(mainBackground);
        
        title.setForeground(Color.white);
        
        inputBox.setBorder(new EmptyBorder(0, 0, 0, 10));
        inputBox.setForeground(Color.white);
        
        hexLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        hexLabel.setForeground(Color.white);
        
        decLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        decLabel.setForeground(Color.white);
        
        binLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        binLabel.setForeground(Color.white);
        
        buttonHex.setBackground(mainBackground);
        buttonDec.setBackground(mainBackground);
        buttonBin.setBackground(mainBackground);
        
        buttonDec.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        buttonUpArrow.setEnabled(false);
        buttonPoint.setEnabled(false);
    }

    private enum Base {
        DECIMAL(10), HEX(16), BINARY(2);

        private int value;

        Base(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }
}