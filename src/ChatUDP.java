import javax.swing.*;
import java.awt.*;

public class ChatUDP extends JFrame {

    private JTextArea taMain;
    private JTextField tfMsg;

    private final String FRM_TITLE = "MyChat";
    private final int FRM_LOC_X = 100;
    private final int FRM_LOC_Y = 100;
    private final int FRM_WIDTH = 400;
    private final int FRM_HEIGHT = 400;

    private final int port = 9876;
    private final String IP_BROADCAST = "192.168.0.255";




    private class thdReceiver extends Thread {
        @Override
        public void start(){
            super.start();
            System.out.println("it is from thread:");
        }
    }

    private void frameDraw (JFrame frame){
        tfMsg = new JTextField();
        taMain = new JTextArea(FRM_HEIGHT/19, 50);

        JScrollPane spMain = new JScrollPane(taMain);
        spMain.setLocation(0,0);
        taMain.setLineWrap(true);
        taMain.setEditable(false);

        JButton btnSend = new JButton();
        btnSend.setText("Send");
        btnSend.setToolTipText("Broadcast message");
        btnSend.addActionListener(e -> {
            taMain.append("button works");
        });

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(FRM_TITLE);
        frame.setLocation(FRM_LOC_X, FRM_LOC_Y);
        frame.setSize(FRM_WIDTH, FRM_HEIGHT);
        frame.setResizable(false);
        frame.getContentPane().add(BorderLayout.NORTH, spMain);
        frame.getContentPane().add(BorderLayout.CENTER, tfMsg);
        frame.getContentPane().add(BorderLayout.EAST, btnSend);
        frame.setVisible(true);




    }

    private void antistatic() {
        //UI there
        frameDraw (new ChatUDP());

        new thdReceiver().start();




    }

    public static void main(String[] args) {
        new ChatUDP().antistatic();
    }
}
