public class color {
    public int red;
    public int green;
    public int blue;
    public int alpha;

    public color(int red, int green, int blue, int alpha){
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = alpha;
    }

    public color(int red, int green, int blue){
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = 255;
    }

    public color(int color){
        this.alpha = (color>>24) & 0xff;
        this.red = (color>>16) & 0xff;
        this.green = (color>>8) & 0xff;
        this.blue = (color>>0) & 0xff;
    }

    public int toColor(){
        int color = 0;
        color = color | (alpha<<24);
        color = color | (red<<16);
        color = color | (green<<8);
        color = color | (blue<<0);
        return color;
    }
}
