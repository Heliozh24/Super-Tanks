import java.awt.Color;
import java.awt.Graphics;

class Ball
{
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Color color;
    private int distanceTravelled = 0;
    private int delta_x;
    private int delta_y;
    private int distanceLimit;
    
    public Ball( int posX, int posY, int width, int height,Color color)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setDx(int dx)
    {
        delta_x = dx;
    }

    public void setDistanceLimit(int limit)
    {
        distanceLimit = limit;
    }

    public int getDistanceLimit()
    {
        return distanceLimit;
    }

    public void setDy(int dy)
    {
        delta_y = dy;
    }

    public int getDx()
    {
        return delta_x;
    }

    public int getDy()
    {
        return delta_y;
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

    public int getHeight()
    {
        return height;
    }

    public Color getColor()
    {
        return color;
    }

    public void relocateX( int dx )
    {
        posX += dx;
        distanceTravelled += Math.abs(dx);
    }

    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    public void relocateY( int dy )
    {
        posY += dy;
        distanceTravelled += Math.abs(dy);
    }

    public void draw(Graphics gfx)
    {
        gfx.fillOval(getPosX(),getPosY(),getWidth(),getHeight());
    }
}