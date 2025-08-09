import java.awt.Color;

class Barrel extends Block
{
    private final int damage = 40;
    public Barrel(int posX, int posY)
    {
        super(posX,posY,40,40,40,new Color(183, 65, 14));
    }

    public int getDamage()
    {
        return damage;
    }
}