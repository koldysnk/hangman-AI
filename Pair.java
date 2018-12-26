public class Pair
{
    private String key;
    private int value;

    public Pair(String aKey, int aValue)
    {
        key   = aKey;
        value = aValue;
    }

    public String key()   { return key; }
    public int value() { return value; }
    public void incValue() { value++; }
}