import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;

public class Filter {
    private int[][] img;
    private double[][] filter;

    /**
     * 0 1 2
     * 0 [[1,2,3],
     * 1  [4,5,6],
     * 2  [7,8,9]]
     * filter[y][x]
     * filter[2][1]=8
     *
     * @param img
     * @param filter
     */
    public Filter(int[][] img, double[][] filter) {
        this.img = img;
        this.filter = filter;
    }

    public int[][] go() {
        int half = ((filter.length-1)/2);
        int[][] ret = new int[img.length-2*half][img[0].length-2*half];
        for (int x = 0; x < ret[0].length; x++) {
            for (int y = 0; y < ret.length; y++) {
                double[] result = new double[filter.length*filter[0].length];
                int index = 0;
                for (int dX = 0; dX < filter[0].length; dX++) {
                    for (int dY = 0; dY < filter.length; dY++) {
                        int relX = x+dX-half;
                        int relY = y+dY-half;
                        if(relX+half>-1&&relY+half>-1&&relX+half<ret[0].length&&relY+half<ret.length){
                            result[index] = img[relY+half][relX+half]*filter[dY][dX];
                            index++;
                        }else{
                            int i = 0;
                        }
                    }
                }
                ret[y][x] = (int)average(result);
            }
        }
        return ret;
    }

    private double average(double[] arr){
        double ans = 0;
        for(int i = 0; i<arr.length;i++){
            ans+=arr[i];
        }
        return ans/arr.length;
    }



/*
    public static void main(String[] args) throws Exception{

        File file = new File("img.jpg");
        BufferedImage img = ImageIO.read(file);
        double[][] blur =   {{1, 1, 1, 1, 1, 1, 1},
                             {1, 1, 1, 1, 1, 1, 1},
                             {1, 1, 1, 1, 1, 1, 1},
                             {1, 1, 1, 1, 1, 1, 1},
                             {1, 1, 1, 1, 1, 1, 1},
                             {1, 1, 1, 1, 1, 1, 1},
                             {1, 1, 1, 1, 1, 1, 1}};
        double[][] emboss = {{-2, -1, 0},
                             {-1, 1, 1},
                             {0, 1, 2}};
        double[][] galblur = {{1, 2, 1},
                              {2, 4, 2},
                              {1, 2, 1}};
        double[][] sharpen = {{0, -1, 0},
                              {-1, 5, -1},
                              {0, -1, 0}};
        double[][] VLine = {{1, 1, 1},
                              {0, 0, 0},
                              {-1, -1, -1}};
        double[][] HLine={{1, 0, -1},
                          {1, 0, -1},
                          {1, 0, -1}};
        double[][] outline={{-1, -1, -1},
                            {-1, 8, -1},
                            {-1, -1, -1}};

        Filter f = new Filter(img,blur);
        BufferedImage mid = f.go().toBufferedImage();
        Filter mf = new Filter(mid,blur);
        BufferedImage mid2 = f.go().toBufferedImage();
        Filter ff = new Filter(mid2, outline);
        BufferedImage out = ff.go().toBufferedImage();
        ImageIO.write(out,"jpg",new File("Output.jpg"));
        ImageIO.write(mid2,"jpg",new File("Mid.jpg"));
    }
    */

    public static int[][] bufferedImg2IntArr(BufferedImage bufferedImage){
        int[][] ret = new int[bufferedImage.getHeight()][bufferedImage.getWidth()];
        for(int x = 0; x<bufferedImage.getWidth();x++){
            for(int y = 0; y<bufferedImage.getHeight();y++){
                int[] rgb = colorToRgb(bufferedImage.getRGB(x,y));
                rgb[0] *= 0.299;
                rgb[1] *= 0.587;
                rgb[2] *= 0.114;
                ret[y][x] = rgb[0]+rgb[1]+rgb[2];
            }
        }
        return ret;
    }

    public static BufferedImage intArr2BufferedImg(int[][] i){
        BufferedImage ret = new BufferedImage(i[0].length,i.length,BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x<i[0].length;x++){
            for(int y = 0; y<i.length;y++){
                ret.setRGB(x,y,rgbToColor(i[y][x],i[y][x],i[y][x]));
            }
        }
        return ret;
    }
    public static BufferedImage intArr3BufferedImg(int[][][] img){
        BufferedImage ret = new BufferedImage(img[0].length,img.length,BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x<img[0].length;x++){
            for(int y = 0; y<img.length;y++){
                ret.setRGB(x,y,rgbToColor(img[y][x][0],img[y][x][1],img[y][x][2]));
            }
        }
        return ret;
    }
    private static int rgbToColor(int r, int g, int b){
        int color = 0;
        color = color | (r<<16);
        color = color | (g<<8);
        color = color | (b<<0);
        return color;
    }
    private static int[] colorToRgb(int color){
        int[] ret = new int[3];
        ret[0] = (color>>16) & 0xff;
        ret[1] = (color>>8) & 0xff;
        ret[2] = (color>>0) & 0xff;
        return ret;
    }
}
