import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Sobel {
    private double[][] Xfilter;
    private double[][] Yfilter;
    private int[][] img;

    /**
     * a Sobel operator that finds the edges of objects in images by applying filters
     * @param Xfilter the filter to use in the X direction
     * @param Yfilter the filter to use in the Y direction
     * @param img the image to apply the operator to
     */
    public Sobel(double[][] Xfilter, double[][] Yfilter, int[][] img){
        this.Xfilter = Xfilter;
        this.Yfilter = Yfilter;
        this.img = img;
    }

    /**
     * runs the operator on the image
     * @return an image with the outlines of the image
     * @throws Exception
     */
    public int[][] go()throws Exception{
        //create and run the filters on the image with both the x and y
        Filter filterX = new Filter(img,Xfilter);
        Filter filterY = new Filter(img,Yfilter);
        int[][] Ximg = filterX.go();
        int[][] Yimg = filterY.go();


        int[][] FinImg = new int[Ximg.length][Ximg[0].length];//create a var to store the final img


        for(int x = 0; x<Ximg[0].length;x++){//loop through x
            for(int y = 0; y<Ximg.length;y++){//loop through y
                FinImg[y][x] = (int)Math.pow(Math.pow(Ximg[y][x],2)+Math.pow(Yimg[y][x],2),0.5);//sqrt(x^2+y^2) gets the magnitude
            }//end y
        }//end x

        //save the filtered imgs cuz they are cool
        ImageIO.write(Filter.intArr2BufferedImg(Ximg),"jpg",new File("Ximg.jpg"));
        ImageIO.write(Filter.intArr2BufferedImg(Yimg),"jpg",new File("Yimg.jpg"));
        return FinImg;//return the final image
    }

    /**
     * main entry point of the program
     */
    public static void main(String[] args) throws Exception{
        //read the img file
        File f = new File("img.jpg");
        int[][] img = Filter.bufferedImg2IntArr(ImageIO.read(f));

        //some filters
        double[][] VLine = {//used for vertical lines
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}};
        double[][] HLine={//used of horizontal lines
                {1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}};
        double[][] blurarr = {//used for blurring
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}};
        double[][] identity = {//used to not change it
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}};

        Filter blur = new Filter(img,blurarr);//create the Filter object
        System.out.println("running blur");
        img = blur.go(10);//run it 10 times
        System.out.println("finished blur");
        ImageIO.write(Filter.intArr2BufferedImg(img),"jpg",new File("blurred.jpg"));//save the blurred file

        Sobel sobel = new Sobel(VLine,HLine,img);//create the Sobel object
        System.out.println("running sobel");
        img = sobel.go();//run it
        System.out.println("finished sobel");
        ImageIO.write(Filter.intArr2BufferedImg(img),"jpg",new File("sobel.jpg"));//save the final file
    }
}
