

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class about extends JFrame implements MouseListener {
 
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
        new about();
    }
    public about()
    {
        setVisible(true);
        setLayout(null);
        setBounds(0,0,650,450);
        setTitle("Stock Analysis");

        setResizable(false);
        showBg(); 

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addMouseListener(this);
    }
    private void showBg() {
// TODO Auto-generated method stub
        ImageIcon image = new ImageIcon("pics/about1.jpg");
        image.setImage(image.getImage().getScaledInstance(650, 450, Image.SCALE_DEFAULT));
        JLabel jl = new JLabel(image);
        jl.setBounds(0,0,650,450);
        this.getLayeredPane().add(jl, Integer.valueOf(Integer.MAX_VALUE));
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        if(x>=0 && x<=650 && y>=0&& y<=450)
        {
            dispose();
            new BeginFrame();
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
