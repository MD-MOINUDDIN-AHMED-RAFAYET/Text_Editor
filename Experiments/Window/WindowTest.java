package Window;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
public class WindowTest{
    public static void main(String[]args)throws IOException,InterruptedException{
        final JFrame textEditor=new JFrame("Page");
        JLabel text=new JLabel("Hello World");
        final ImageIcon icon=new ImageIcon("G:\\ALL PROGRAM FILES\\JAVA_Intellij\\Projects\\Text_Editor\\Reference_Pictures\\Icons\\ChatGPT Image Sep 16, 2026, 06_43_42 PM.png");
        textEditor.setSize(1000,800);
        textEditor.setIconImage(icon.getImage());
        textEditor.setLocationRelativeTo(null);
        textEditor.setLayout(new GridBagLayout());
        text.setFont(new Font("Consolas", Font.PLAIN, 16));
        text.setForeground(Color.WHITE);
        textEditor.add(text);
        textEditor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textEditor.setResizable(true);
        textEditor.getContentPane().setBackground(Color.BLACK);
        textEditor.setVisible(true);
    }
}