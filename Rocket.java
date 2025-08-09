
class Rocket
{
   private int posX;
   private int posY;
   private int width;
   private int height;
   private int arcWidth;
   private int arcHeight;
   private int delta_x;
   private int delta_y;
   private double angle;
   private int mouseX;
   private int mouseY;
   private int distanceTravelled = 0;

    public Rocket(int posX, int posY, int width, int height, int arcWidth, int arcHeight)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public void setMouseX(int mx)
    {
        mouseX = mx;
    }

    public void setMouseY(int my)
    {
        mouseY = my;
    }

    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
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

    public void relocateX( int dx)
    {
        posX += dx;
        distanceTravelled += Math.abs(dx);
    }

    public void relocateY( int dy)
    {
        posY += dy;
        distanceTravelled += Math.abs(dy);
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

    public int getArcWidth()
    {
        return arcWidth;
    }

    public int getArcHeight()
    {
        return arcHeight;
    }
}