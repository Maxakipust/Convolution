import java.awt.image.BufferedImage;

public class Image {
    private color[][] pixels;
    public Image(int w, int h, color c){
        pixels = new color[h][w];
        for(int y = 0; y<h;y++){
            for(int x = 0; x<w; x++){
                pixels[y][x] = c;
            }
        }
    }
    public Image(BufferedImage img){
        pixels = new color[img.getHeight()][img.getWidth()];
        for(int y = 0; y<img.getHeight();y++){
            for(int x = 0; x<img.getWidth(); x++){
                pixels[y][x] = new color(img.getRGB(x,y));
            }
        }
    }

    public color getPixel(int x, int y){
        return pixels[y][x];
    }
    public void setPixel(int x, int y, color c){
        pixels[y][x] = c;
    }
    public int getWidth(){
        return pixels[0].length;
    }
    public int getHeight(){
        return pixels.length;
    }

    public BufferedImage toBufferedImage(){
        BufferedImage ret = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x<getWidth();x++){
            for(int y = 0; y<getHeight();y++){
                ret.setRGB(x,y,getPixel(x,y).toColor());
            }
        }
        return ret;
    }
}
