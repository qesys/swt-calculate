package ex.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Stack;

public class ex1 {
    public static String string = null;//代表结果


    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell();
        shell.setText("计算器");
        shell.setBounds(400, 450, 650, 400);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 16;
        shell.setLayout(gridLayout);

        GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
        gridData1.horizontalSpan = 10;
        gridData1.widthHint = 400;
        gridData1.heightHint = 30;
        Text text1 = new Text(shell, SWT.READ_ONLY | SWT.RIGHT);
        text1.setText("");
        text1.setFont(new Font(display, "宋体", 22, SWT.NONE));
        text1.setBackground(new Color(255, 0, 0));
        text1.setLayoutData(gridData1);

        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        gridData2.horizontalSpan = 3;
        gridData2.widthHint = 100;
        gridData2.heightHint = 30;
        Text text2 = new Text(shell, SWT.READ_ONLY | SWT.RIGHT);
        text2.setText("");
        text2.setFont(new Font(display, "宋体", 22, SWT.NONE));
        text2.setBackground(new Color(0, 255, 0));
        text2.setLayoutData(gridData2);

        GridData gridData3 = new GridData(GridData.FILL_HORIZONTAL);
        gridData3.horizontalSpan = 3;
        gridData3.widthHint = 100;
        gridData3.heightHint = 30;
        Text text3 = new Text(shell, SWT.READ_ONLY | SWT.RIGHT);
        text3.setText("");
        text3.setFont(new Font(display, "宋体", 22, SWT.NONE));
        text3.setBackground(new Color(255, 255, 0));
        text3.setLayoutData(gridData3);

        String[] a = {" ", "( ", " )", "+", "7", "8", "9", "-", "4", "5", "6", "*", "1", "2", "3", "/", "C", "0", ".", "="};
        GridData gridData4 = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        gridData4.horizontalSpan = 4;
        for (String s : a) {
            Button button = new Button(shell, SWT.PUSH);
            button.setText(s);
            button.setFont(new Font(display, "宋体", 22, SWT.NONE));
            button.setLayoutData(gridData4);
            button.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent selectionEvent) {
                    if (!button.getText().equals("=")) {
                        text1.setText(text1.getText() + button.getText());
                    }
                    string = text1.getText();
                    if (button.getText().equals("=")) {
                        int out = calcuqq(tostackqq(string));
                        text3.setText(String.valueOf(out));
                    }
                    if (button.getText().equals("C")) {
                        text1.setText("");
                        text2.setText("");
                        text3.setText("");
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent selectionEvent) {

                }
            });
        }

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public static Stack<String> tostackqq(String string) {
        Stack<String> stack1 = new Stack<>();
        Stack<Character> stack2 = new Stack<>();
        int index = 0;
        while (index < string.length()) {
            char c = string.charAt(index);
            if (isDigital(c)) {
                int p = index;
                while (p < string.length() && isDigital(string.charAt(p))) {
                    p++;
                }
                stack1.push(string.substring(index, p));
                index = p;
                continue;
            } else if (c == '(') {
                stack2.push(c);
            } else if (c == ')') {
                while ('(' != stack2.peek()) {
                    stack1.push(String.valueOf(stack2.pop()));
                }
                stack2.pop();
            } else if (isOperator(c)) {
                // 如果符号栈为空，就直接压入栈
                if (stack2.isEmpty()) {
                    stack2.push(c);
                    // 如果符号栈的栈顶是左括号，则压入栈中
                } else if ('(' == stack2.peek()) {
                    stack2.push(c);
                    // 如果当前元素的优先级比符号栈的栈顶元素优先级高，则压入栈中
                } else if (priority(c) > priority(stack2.peek())) {
                    stack2.push(c);
                } else if (priority(c) <= priority(stack2.peek())) {
                    stack1.push(String.valueOf(stack2.pop()));
                    stack2.push(c);
                }
            }
            index++;
        }
        // 遍历完后，将栈中元素全部弹出到队列中
        while (!stack2.isEmpty()) {
            stack1.push(String.valueOf(stack2.pop()));
        }
        return stack1;
    }

    public static int calcuqq(Stack<String> stack1) {
        Stack<Integer> stack2 = new Stack<>();
        Stack<String> stack3 = new Stack<>();
        while (!stack1.empty()) {
            stack3.push(stack1.pop());
        }
        while (!stack3.empty()) {
            if (!stack3.peek().equals("+") && !stack3.peek().equals("-")
                    && !stack3.peek().equals("*") && !stack3.peek().equals("/")) {
                stack2.push(Integer.valueOf(stack3.pop()));
            } else {
                String c2 = stack3.pop();
                int a = stack2.pop();
                int b = stack2.pop();
                switch (c2) {
                    case "+":
                        stack2.push(a + b);
                        break;
                    case "-":
                        stack2.push(b - a);
                        break;
                    case "*":
                        stack2.push(a * b);
                        break;
                    case "/":
                        stack2.push(b / a);
                        break;
                }
            }
        }
        return stack2.peek();
    }

    //判断是符号
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    //判断是数字
    private static boolean isDigital(char c) {
        return c >= '0' && c <= '9';
    }

    //运算符的优先级
    private static int priority(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                throw new RuntimeException("Illegal operator:" + c);
        }
    }
}