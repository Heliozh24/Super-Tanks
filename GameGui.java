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

class GameGui extends JFrame
{
    private JLayeredPane layeredPane;
    
    public JLayeredPane getLayeredPane()
    {
        return layeredPane;
    }

    public  void initLabel(JLabel label, int x, int y, int width, int height,Font font, Color color)    //initilaztion methods
    {
        label.setBounds(x,y,width,height);
        label.setFont(font);
        label.setForeground(color);
    }
    
    public  void initButton(JButton button, Dimension dimension, float allignment,Font font, Color foregroundColor, Color backgroundColor)
    {
        button.setPreferredSize(dimension);
        button.setMaximumSize(dimension);
        button.setAlignmentX(allignment);
        button.setFont(font);
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
    }
    public  void initPanel(JPanel panel, boolean isOpaque,LayoutManager layoutManager,int x, int y, int width, int height)
    {
        panel.setOpaque(isOpaque);
        panel.setLayout(layoutManager);
        panel.setBounds(x,y,width,height);
    }

    public GameGui()
    {
        super("Super Tanks");
        JLabel background = new JLabel(new ImageIcon("background.png"));
        JLabel gameTitle = new JLabel("Super Tanks");
        JLabel  copyrightLabel = new JLabel(" © 2025 Helio Zhuleku.");

        JButton playButton = new JButton("PLAY"); 
        JButton exitButton = new JButton("EXIT");

        layeredPane = new JLayeredPane();
        JPanel buttonPanel = new JPanel();

        background.setBounds(0, 0, 800, 600);

        initPanel(buttonPanel,false,new BoxLayout(buttonPanel, BoxLayout.Y_AXIS),-287, 150, 1400, 450);
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(100)); 
        buttonPanel.add(exitButton);

        Dimension buttonSize = new Dimension(280, 80);

        initButton(playButton,buttonSize,Component.CENTER_ALIGNMENT,new Font("Serif", Font.BOLD, 45),new Color(160, 160, 130),Color.yellow);
        initButton(exitButton,buttonSize,Component.CENTER_ALIGNMENT,new Font("Serif", Font.BOLD, 45),new Color(160, 160, 130),Color.yellow);

        initLabel(gameTitle,100,20,600,100, new Font("Serif", Font.BOLD, 80),Color.yellow);
        gameTitle.setHorizontalAlignment(SwingConstants.CENTER);
        initLabel(copyrightLabel,7,538,280,20,new Font("Serif", Font.BOLD, 25),(Color.white));

        layeredPane.setPreferredSize(new Dimension(300, 100));
        layeredPane.add(background, Integer.valueOf(0));
        layeredPane.add(gameTitle, Integer.valueOf(1)); 
        layeredPane.add(buttonPanel, Integer.valueOf(2));
        layeredPane.add(copyrightLabel, Integer.valueOf(3));

        setContentPane(layeredPane);

        Color buttonColor = new Color(218, 165, 32);

        playButton.addActionListener(e -> {LevelPanel levelPanel = new LevelPanel(GameGui.this); setContentPane(levelPanel);  revalidate();});

        playButton.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event)
            {
                ButtonModel model = (ButtonModel) event.getSource();
               
                if(model.isPressed())
                {
                    playButton.setBackground(buttonColor);
                }
                else if(model.isRollover())
                {
                    playButton.setBackground(buttonColor);
                    playButton.setForeground(Color.white);
                }
                else
                {
                    playButton.setBackground(Color.yellow);
                    playButton.setForeground(new Color(160, 160, 130));
                }
            }
        });

        exitButton.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent event)
            {
                ButtonModel model = (ButtonModel) event.getSource();
               
                if(model.isPressed())
                {
                    exitButton.setBackground(new Color(255,40,40));
                    System.exit(0);
                }
                else if(model.isRollover())
                {
                    exitButton.setBackground(new Color(255, 40, 40));
                    exitButton.setForeground(Color.white);
                }
                else
                {
                    exitButton.setBackground(Color.yellow);
                    exitButton.setForeground(new Color(160, 160, 130));
                }
            }
        });

    }
}