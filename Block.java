import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;

class Block
{
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected int health;
    protected Color color;
    protected Color ogColor;
    protected int ogHealth;
    protected Timer damageTimer;
    protected int damageTime = 200;
    protected Rectangle healthBar;
    protected Color barColor = Color.orange;
    protected Rectangle backgroundBar;

    public Block(int posX, int posY, int width, int height,int health,Color color)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.color = color;
        this.ogColor = color;
        this.health = health;
        this.ogHealth = health;
        backgroundBar = new Rectangle(posX,posY-15,60,8);
        healthBar = new Rectangle(posX,posY-15,60,8);

        damageTimer = new Timer(damageTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                setColor(ogColor);
            }
        });
        damageTimer.setRepeats(false);

    }

    public Timer getDamageTimer()
    {
        return damageTimer;
    }


    public int getOgHealth()
    {
        return ogHealth;
    }

    public Rectangle getHealthBar()
    {
        return healthBar;
    }

    public Color getBarColor()
    {
        return barColor;
    }

    public Rectangle getBackgroundBar()
    {
        return backgroundBar;
    }

    public void decrHealthBarWidth(double width)
    {
        healthBar.width -= width;
    }

    public int getPosX()
    {
        return posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public int getWidth()
    {
        return width;
    }

    public Color getOgColor()
    {
        return ogColor;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHealth(int hp)
    {
        health = hp;
    }

    public int getHealth()
    {
        return health;
    }

    public Color getColor()
    {
        return color;
    }

    public double getCenterX()
    {
        return (new Rectangle(posX,posY,width,height).getCenterX());
    }

    public double getCenterY()
    {
        return (new Rectangle(posX,posY,width,height).getCenterY());
    }

    public void setColor(Color clr)
    {
        color = clr;
    }
}

