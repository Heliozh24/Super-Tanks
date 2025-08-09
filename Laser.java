
class Laser
{
    private int posX;
    private int posY;
    private int width;
    private int height;
    private int delta_x;
    private int delta_y;
    private double angle;
    private int mouseX;
    private int mouseY;

    public Laser(int posX, int posY, int width, int height)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }
    
    public void setX(int x)
    {
        posX = x;
    }

    public void setY(int y)
    {
        posY = y;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public void setMousePos(int mx, int my)
    {
        mouseX = mx;
        mouseY = my;
    }
    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angl)
    {
        angle = angl;
    }
    
    public void setDx(int dx)
    {
        delta_x = dx;
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

    public void setWidth(int wth)
    {
        width = wth;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void relocateX( int dx )
    {
        posX += dx;
    }

    public void relocateY( int dy )
    {
        posY += dy;
    }

}