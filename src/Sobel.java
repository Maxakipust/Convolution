import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Sobel {
    double[][] Xfilter;
    double[][] Yfilter;
    int[][] img;

    public Sobel(double[][] Xfilter, double[][] Yfilter, int[][] img){
        this.Xfilter = Xfilter;
        this.Yfilter = Yfilter;
        this.img = img;
    }

    public int[][] go()throws Exception{
        Filter filterX = new Filter(img,Xfilter);
        Filter filterY = new Filter(img,Yfilter);
        int[][] Ximg = filterX.go();
        int[][] Yimg = filterY.go();


        int[][] FinImg = new int[Ximg.length][Ximg[0].length];
        int min = 255;
        int max = 0;
        int xmin = 255;
        int xmax = 0;
        int ymin = 255;
        int ymax = 0;

        for(int x = 0; x<Ximg[0].length;x++){
            for(int y = 0; y<Ximg.length;y++){
                double mag = Math.pow(Math.pow(Ximg[y][x],2)+Math.pow(Yimg[y][x],2),0.5);
                FinImg[y][x] = (int)mag;
                if(mag>max){
                    max = (int)mag;
                }
                if(mag<min){
                    min = (int)mag;
                }

                if(Ximg[y][x]>xmax){
                    xmax = Ximg[y][x];
                }
                if(Ximg[y][x]<xmin){
                    xmin = Ximg[y][x];
                }

                if(Yimg[y][x]>ymax){
                    ymax = Yimg[y][x];
                }
                if(Yimg[y][x]<ymin){
                    ymin = Yimg[y][x];
                }
            }
        }

        for(int x = 0; x<Ximg[0].length;x++){
            for(int y = 0; y<Ximg.length;y++){
                FinImg[y][x] = map(FinImg[y][x],min,max,0,255);
                Ximg[y][x] = map(Ximg[y][x],xmin,xmax,0,255);
                Yimg[y][x] = map(Yimg[y][x],ymin,ymax,0,255);
            }
        }

        System.out.println("X min: "+xmin);
        System.out.println("X max: "+xmax);

        System.out.println("Y min: "+ymin);
        System.out.println("Y max: "+ymax);


        System.out.println("max: "+max);
        System.out.println("min: "+min);

        ImageIO.write(Filter.intArr2BufferedImg(Ximg),"jpg",new File("Ximg.jpg"));
        ImageIO.write(Filter.intArr2BufferedImg(Yimg),"jpg",new File("Yimg.jpg"));
        return FinImg;
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static void main(String[] args) throws Exception{
        File f = new File("img.jpg");
        int[][] img = Filter.bufferedImg2IntArr(ImageIO.read(f));
        double[][] VLine = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}};
        double[][] HLine={
                {1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}};
        double[][] blurarr = {
                {2, 2, 2},
                {2, 2, 2},
                {2, 2, 2}};

        Filter blur = new Filter(img,blurarr);
        System.out.println("running blur");
        img = blur.go();
        System.out.println("finished blur");
        ImageIO.write(Filter.intArr2BufferedImg(img),"jpg",new File("blurred.jpg"));
        Sobel sobel = new Sobel(VLine,HLine,img);
        System.out.println("running sobel");
        img = sobel.go();
        System.out.println("finished sobel");
        ImageIO.write(Filter.intArr2BufferedImg(img),"jpg",new File("sobel.jpg"));
    }
}
