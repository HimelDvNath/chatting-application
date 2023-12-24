/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapplication;

import static chattingapplication.Server.boxLabel;
import static chattingapplication.Server.din;
import static chattingapplication.Server.dout;
import static chattingapplication.Server.frame;
import static chattingapplication.Server.vertical;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author himel
 */
public class Server implements ActionListener{
    static JTextField textField;
    static JPanel panel2, panel;
    static Box vertical = Box.createVerticalBox();
    static JFrame frame = new JFrame();
    static ObjectOutputStream dout;
    static ObjectInputStream din;
    public Server() {
        frame.setLayout(null);
        frame.setBounds(80, 200, 450, 600);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.WHITE);
        
        panel = new JPanel();
        panel.setBackground(new Color(7, 94, 84));
        panel.setBounds(0, 0, 450, 60);
        panel.setLayout(null);
        frame.add(panel);
        
        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("icons/cancel.png"));
        Image scale = image.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon scaleImage = new ImageIcon(scale);
        JLabel back = new JLabel(scaleImage);
        back.setBounds(410, 15, 30, 30);
        panel.add(back);
        
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });
        
        ImageIcon profileimage = new ImageIcon(ClassLoader.getSystemResource("icons/1.jpg"));
        scale = profileimage.getImage().getScaledInstance(55, 55, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(scale));
        profile.setBounds(15, 2, 55, 55);
        panel.add(profile);
        
        JLabel status = new JLabel("Active Now");
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Arial", Font.BOLD, 12));
        status.setBounds(80, 35,100, 20);
        panel.add(status);
        
        ImageIcon videoimage = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        scale = videoimage.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(scale));
        video.setBounds(370, 15, 30, 30);
        panel.add(video);
        
        ImageIcon audioimage = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        scale = audioimage.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel audio = new JLabel(new ImageIcon(scale));
        audio.setBounds(330, 15, 30, 30);
        panel.add(audio);
             
        JLabel name = new JLabel("Himel Devnath");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setBounds(80, 5,130, 30);
        panel.add(name);

        panel2 = new JPanel();
        panel2.setBounds(1, 61, 449, 485);
        frame.add(panel2);
        
        textField = new JTextField();
        textField.setBounds(1, 550, 310, 50);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER && !textField.getText().isEmpty()){
                    actionPerformed((ActionEvent) textField.getAction());
                }
            } 
        });
        frame.add(textField);

        ImageIcon sendimage = new ImageIcon(ClassLoader.getSystemResource("icons/send.png"));
        JButton button = new JButton(sendimage);
        button.setBounds(360, 550, 90, 50);
        button.setBackground(new Color(7, 94, 84));
        button.addActionListener(this);
        frame.add(button);
        
        ImageIcon linkimage = new ImageIcon(ClassLoader.getSystemResource("icons/link.png"));
        JButton linkbutton = new JButton(linkimage);
        linkbutton.setBounds(310, 550, 50, 50);
        frame.add(linkbutton);
        
        frame.validate();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Server();
        try{
            ServerSocket server = new ServerSocket(2000);
            while(true){
                Socket socket = server.accept();
                dout = new ObjectOutputStream(socket.getOutputStream());
                //din = new ObjectInputStream(socket.getInputStream());
                new serverThread(socket, frame, panel2);  
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        try{
            panel2.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());
            if(!textField.getText().isEmpty()){
                JPanel output = boxLabel(textField.getText(), true);
                right.add(output, BorderLayout.LINE_END);
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(15));

                panel2.add(vertical, BorderLayout.PAGE_END);
                dout.writeObject(textField.getText());
                dout.flush();
                textField.setText("");
            }
            frame.validate();          
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
    public static JPanel boxLabel(String output, boolean grn){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel outt = new JLabel(output);
        outt.setFont(new Font("Tahoma", Font.PLAIN, 16));
        if(grn){
            outt.setBackground(Color.GREEN);
        }
        else{
            outt.setBackground(Color.YELLOW);
        }
        outt.setOpaque(true);
        outt.setBorder(new EmptyBorder(15, 15, 15, 50));
        Calendar cal = Calendar.getInstance();
        
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(date.format(cal.getTime()));
        panel.add(outt);
        panel.add(time);
        
        return panel;
    }  
}
class serverThread implements Runnable{
    Socket socket;
    Thread t;
    JFrame frame;
    JPanel panel2;
    public serverThread(Socket socket, JFrame frame, JPanel panel) {
        this.socket = socket;
        this.frame = frame; 
        this.panel2 = panel;
        t = new Thread(this);
        t.start();
    }

    @Override
    synchronized public void run() {
            
        try {
            din = new ObjectInputStream(socket.getInputStream());
            Object obj = din.readObject();
            if(obj instanceof ImageIcon){ 
                JFrame permissionFrame = new JFrame();
                permissionFrame.setBounds(300, 200, 500, 500);
                permissionFrame.setLayout(null);
               
                JLabel messageLabel = new JLabel("Zuckerberg Calling");
                messageLabel.setBounds(50, 100, 400, 50);
                messageLabel.setFont(new Font("Arial", Font.BOLD, 30));
                messageLabel.setForeground(Color.GREEN);
                permissionFrame.add(messageLabel);
                
                ImageIcon recimage = new ImageIcon(ClassLoader.getSystemResource("icons/received.png"));
                JButton yes = new JButton(recimage);
                yes.setBounds(30, 300, 120, 120);
                permissionFrame.add(yes);
                
                ImageIcon endimage = new ImageIcon(ClassLoader.getSystemResource("icons/end.png"));
                JButton no = new JButton(endimage);
                no.setBounds(320, 300, 120, 120);
                
                permissionFrame.add(no);
                permissionFrame.setVisible(true);
                
                Thread videoThread = new Thread(new Runnable() {
                    @Override
                  synchronized public void run() {
                       JFrame videoFrame = new JFrame("Zuckerberg");
                        videoFrame.setBounds(100, 200, 500, 500);
                        videoFrame.setLayout(null);
                        videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JLabel videoLabel = new JLabel();
                        videoLabel.setBounds(0, 0, 500, 500);
                        videoLabel.setLayout(null);
                        videoLabel.setVisible(true);

                        videoFrame.add(videoLabel);
                        videoFrame.setVisible(true);
                        try{
                            while (true) { 
                                try{
                                    videoLabel.setIcon(new ImageIcon(((ImageIcon)din.readObject()).getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
                                    
                                }catch(EOFException ex){
                                    videoFrame.setVisible(false);
                                    //break;
                                }
                                videoFrame.validate();    
                            }
                            
                        }catch(IOException ex){
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        
                    }
                });
                
                
                yes.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) { 
                        permissionFrame.setVisible(false);
                        videoThread.start();
                    }
                    
                });
                no.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) { 
                        permissionFrame.setVisible(false);
                    }
                    
                });
                
                  
 
            }else{
                while (true) {
                    String message = din.readObject().toString();
                    JPanel panel = boxLabel(message, false);
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    frame.validate();
                }
            }  
        } catch (IOException ex) {
            Thread fileThread = new Thread(new Runnable() {
                @Override
                synchronized public void run() {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String fileName = reader.readLine();

                        File receivedFile = new File(fileName);
                        Path receivedFilePath = receivedFile.toPath();
                        Files.copy(inputStream, receivedFilePath, StandardCopyOption.REPLACE_EXISTING);
                        
                        FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                        fileOutputStream.close();
                        panel2.setLayout(new BorderLayout());
                        JPanel panel = buttonLabel(receivedFile.getName(), false, receivedFile);
                        JPanel left = new JPanel(new BorderLayout());
                        left.add(panel, BorderLayout.LINE_START);
                        vertical.add(left);
                        vertical.add(Box.createVerticalStrut(15));
                        panel2.add(vertical, BorderLayout.PAGE_END);
                        frame.add(panel2);
                        frame.validate();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            fileThread.start();
                
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(serverThread.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public static JPanel buttonLabel(String output, boolean grn, File file){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JButton out = new JButton(output);
        out.setFont(new Font("Tahoma", Font.PLAIN, 16));
        if(grn){
            out.setBackground(Color.GRAY);
        }
        else{
            out.setBackground(Color.PINK);
        }
        
        out.setOpaque(true);
        out.setBorder(new EmptyBorder(15, 15, 15, 50));
        out.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(file);
                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        Files.copy(file.toPath(), selectedFile.toPath());
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
        Calendar cal = Calendar.getInstance();
        
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(date.format(cal.getTime()));
        panel.add(out);
        panel.add(time);
        
        return panel;
    }
}
