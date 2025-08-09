import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.FlowLayout;
import java.awt.Component;

class LevelPanel extends JLayeredPane
{
    private GameGui frame;
    public void initLabel(JLabel label, int x, int y, int width, int height,Font font, Color color)    //initilaztion methods
    {
        label.setBounds(x,y,width,height);
        label.setFont(font);
        label.setForeground(color);
    }
    
    public void initButton(JButton button, Dimension dimension, float allignment,Font font, Color foregroundColor, Color backgroundColor)
    {
        button.setPreferredSize(dimension);
        button.setMaximumSize(dimension);
        button.setAlignmentX(allignment);
        button.setFont(font);
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
    }

    public void initPanel(JPanel panel, boolean isOpaque,LayoutManager layoutManager,int x, int y, int width, int height)
    {
        panel.setOpaque(isOpaque);
        panel.setLayout(layoutManager);
        panel.setBounds(x,y,width,height);
    }

    public void setUpButton(Color color, JButton button, int levelIndex)
    {
        button.addActionListener(e -> {GameLogicPanel gamePanel = new GameLogicPanel(levelIndex,frame); frame.setContentPane(gamePanel); frame.setSize(new Dimension(800,800));  frame.revalidate(); gamePanel.requestFocusInWindow();});

        button.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event)
            {
                ButtonModel model = (ButtonModel) event.getSource();
               
                if(model.isPressed())
                {
                    button.setBackground(color);
                }
                else if(model.isRollover())
                {
                    button.setBackground(color);
                    button.setForeground(Color.white);
                }
                else
                {
                    button.setBackground(Color.yellow);
                    button.setForeground(new Color(160, 160, 130));
                }
            }
        });
    }

    public LevelPanel(GameGui frame)
    {
        this.frame = frame;
        JLabel background = new JLabel(new ImageIcon("background.png"));
        JLabel levelTitle = new JLabel("Select Level");

        JPanel firstLevelPanel = new JPanel();
        JPanel secondLevelPanel = new JPanel();

        background.setBounds(0,0,800,600);

        initPanel(firstLevelPanel,false,new BoxLayout(firstLevelPanel, BoxLayout.X_AXIS),10, 0, 1100, 450);
        initPanel(secondLevelPanel,false,new BoxLayout(secondLevelPanel, BoxLayout.X_AXIS),10, 200, 1100, 450);

        Dimension buttonDimension = new Dimension(100,100);

        for(int i = 1; i <= 5; i++)
        {
            JButton button = new JButton(String.valueOf(i));
            setUpButton(new Color(218, 165, 32),button,i);
            initButton(button,buttonDimension,45,new Font("Serif", Font.BOLD, 45),new Color(160, 160, 130),Color.yellow);
            firstLevelPanel.add(Box.createHorizontalStrut(45)); 
            firstLevelPanel.add(button);
        }

        for(int i = 6; i <= 10; i++)
        {
            JButton button = new JButton(String.valueOf(i));
            setUpButton(new Color(218, 165, 32),button,i);
            initButton(button,buttonDimension,45,new Font("Serif", Font.BOLD, 45),new Color(160, 160, 130),Color.yellow);
            secondLevelPanel.add(Box.createHorizontalStrut(45)); 
            secondLevelPanel.add(button);
        }

        initLabel(levelTitle,100,20,600,100, new Font("Serif", Font.BOLD, 80),Color.yellow);
        levelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        setPreferredSize(new Dimension(300, 100));

        add(background, Integer.valueOf(0));
        add(levelTitle, Integer.valueOf(1));
        add(firstLevelPanel, Integer.valueOf(2));
        add(secondLevelPanel, Integer.valueOf(3));

    }
}