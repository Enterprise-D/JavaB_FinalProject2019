import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BeginFrame extends JFrame implements MouseListener {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new BeginFrame();
    }

    public BeginFrame() {
        setVisible(true);
        setLayout(null);
        setBounds(0, 0, 650, 450);
        setTitle("Stock Analysis");

        setResizable(false);
        showBg();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);

    }

    private void showBg() {
// TODO Auto-generated method stub
        ImageIcon image = new ImageIcon("pics/cover2.jpg");
        image.setImage(image.getImage().getScaledInstance(650, 450, Image.SCALE_DEFAULT));
        JLabel jl = new JLabel(image);
        jl.setBounds(0, 0, 650, 450);
        this.getLayeredPane().add(jl, new Integer(Integer.MAX_VALUE));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (x >= 150 && x <= 480 && y >= 170 && y <= 300) {
            dispose();
            new MainExecute();
        } else if (x >= 500 && x < 600 && y >= 310 && y <= 400) {
            dispose();
            new about();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

