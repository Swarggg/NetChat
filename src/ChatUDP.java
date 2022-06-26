import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ChatUDP extends JFrame {
    private JTextArea taMain;
    private JTextField tfMsg;
    private final String FRM_TITLE = "MyChat";
    private final int FRM_LOC_X = 100;
    private final int FRM_LOC_Y = 100;
    private final int FRM_WIDTH = 400;
    private final int FRM_HEIGHT = 400;
    private final int PORT = 9876;
    private final String IP_BROADCAST = "192.168.0.255";

    private class thdReceiver extends Thread {
        @Override
        public void start(){
            super.start();
            //System.out.println("it is from thread:");
            try {
                customize();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        private void customize() throws Exception {
            DatagramSocket receiveSocket = new DatagramSocket(PORT);
            Pattern regex = Pattern.compile("[\u0020-\uFFFF]");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                receiveSocket.receive(receivePacket);

                //who send
                InetAddress IPAddressOfSender = receivePacket.getAddress();
                int portOfSender = receivePacket.getPort();

                String sentence = new String(receivePacket.getData());
                Matcher m = regex.matcher(sentence);
                //выводим полученное
                    //от кого пришло
                    taMain.append(IPAddressOfSender.toString()+":"+portOfSender+":");

                    //само сообщение
                    while (m.find()) {
                        taMain.append(sentence.substring(m.start(),m.end()));
                    }
                    //перевод строки после выведения сообщения
                    taMain.append("\n");

                    //чтобы последнее сообщение не уходило вниз и оставалось в поле видимости
                    taMain.setCaretPosition(taMain.getText().length());

                //taMain.append(sentence); // нефильтрованное сообщение - весь массив, включая пустые данные
            }
        }
    }

    private void btnSend_Handler() throws Exception {
        DatagramSocket sendSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(IP_BROADCAST);

        String sentence = tfMsg.getText();
        tfMsg.setText(""); //после записи в sentence обнуляем содержимое поля ввода (чтобы не флудить просто нажимая на кнопочку)
        byte[] sendData;
        sendData = sentence.getBytes(StandardCharsets.UTF_8);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);

        sendSocket.send(sendPacket);
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
            try {
                btnSend_Handler();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //taMain.append("button works");
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
