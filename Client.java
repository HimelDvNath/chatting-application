
package chattingapplication;
import static chattingapplication.Client.din;
import com.github.sarxos.webcam.Webcam;
import com.sun.source.tree.ContinueTree;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author himel
 */
public class Client implements ActionListener{
    static JTextField textField;
    static JPanel panel2;
    static Box vertical = Box.createVerticalBox();
    static ObjectOutputStream dout;
    static ObjectInputStream din;
    static JFrame frame = new JFrame();
    static  Socket socket;
    public Client() {
        frame.setLayout(null);
        frame.setBounds(700, 200, 450, 600);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.WHITE);
        
        JPanel panel = new JPanel();
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
        
        ImageIcon profileimage = new ImageIcon(ClassLoader.getSystemResource("icons/2.jpg"));
        scale = profileimage.getImage().getScaledInstance(55, 55, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(scale));
        profile.setBounds(15, 2, 55, 55);
        panel.add(profile);
        
        ImageIcon videoimage = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        scale = videoimage.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(scale));
        video.setBounds(370, 15, 30, 30);
        video.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae) {
                videoCall();
            }
        });
        panel.add(video);
        
        ImageIcon audioimage = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        scale = audioimage.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel audio = new JLabel(new ImageIcon(scale));
        audio.setBounds(330, 15, 30, 30);
        panel.add(audio);

        JLabel name = new JLabel("Zuckerberg");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setBounds(80, 5,130, 30);
        panel.add(name);
        
        JLabel status = new JLabel("Active Now");
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Arial", Font.BOLD, 12));
        status.setBounds(80, 35,100, 20);
        panel.add(status);
        
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
        linkbutton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSender();
            }
            
        });
        
        frame.validate();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Client();
        try{
            socket = new Socket("127.0.0.1", 2000);
            dout = new ObjectOutputStream(socket.getOutputStream());
            din = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                panel2.setLayout(new BorderLayout());
                String message = din.readObject().toString();
                JPanel panel = boxLabel(message, false);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                panel2.add(vertical, BorderLayout.PAGE_END);
                frame.validate();
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
        
        JLabel out = new JLabel(output);
        out.setFont(new Font("Tahoma", Font.PLAIN, 16));
        if(grn){
            out.setBackground(Color.GREEN);
        }
        else{
            out.setBackground(Color.YELLOW);
        }
        
        out.setOpaque(true);
        out.setBorder(new EmptyBorder(15, 15, 15, 50));
        Calendar cal = Calendar.getInstance();
        
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(date.format(cal.getTime()));
        panel.add(out);
        panel.add(time);
        
        return panel;
    }
    public static void videoCall(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                JFrame videoFrame = new JFrame("Himel Devnath");
                videoFrame.setBounds(550, 200, 500, 500);
                videoFrame.setLayout(null);
                videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel videoLabel = new JLabel();
                videoLabel.setBounds(0, 0, 500, 500);
                videoLabel.setLayout(null);
                videoLabel.setVisible(true);

                videoFrame.add(videoLabel);
                videoFrame.setVisible(true);


                BufferedImage bm; 
                ImageIcon im;
                Webcam webcam = Webcam.getDefault();
                webcam.open();
                try {
                    Socket skt = new Socket("127.0.0.1", 2000);
                    ObjectOutputStream oos = new ObjectOutputStream(skt.getOutputStream());
                    try {
                        while(true){
                           bm = webcam.getImage();
                           im = new ImageIcon(bm);
                           oos.writeObject(im);
                           videoLabel.setIcon(new ImageIcon(im.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
                           videoFrame.validate(); 
                           oos.flush();
                           if(!videoFrame.isDisplayable()){
                               webcam.close();
                               skt.close();
                               break;
                           }
                          
                       }
                   } catch (IOException ex) {
                        ex.printStackTrace();
                   }
                    
                } catch (IOException ex) {
                     ex.printStackTrace();
                }
            }
        });
        t.start();
    }
    public void fileSender(){
        Thread fileThread = new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                try {
                    Socket fileSocket = new Socket("127.0.0.1", 2000);
                    InputStream inputStream = fileSocket.getInputStream();
                    OutputStream outputStream = fileSocket.getOutputStream();
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File fileToSend = fileChooser.getSelectedFile();

                        PrintWriter writer = new PrintWriter(outputStream, true);
                        writer.println(fileToSend.getName());

                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        FileInputStream fis = new FileInputStream(fileToSend);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        panel2.setLayout(new BorderLayout());
                        JPanel panel = buttonLabel(fileToSend.getName(), true, fileToSend);
                        JPanel right = new JPanel(new BorderLayout());
                        right.add(panel, BorderLayout.LINE_END);
                        vertical.add(right);
                        vertical.add(Box.createVerticalStrut(15));
                        panel2.add(vertical, BorderLayout.PAGE_END);
                        frame.add(panel2);
                        frame.validate();
                        
                        bis.close();
                        outputStream.close();
                        fileSocket.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }
        });
        fileThread.start();
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