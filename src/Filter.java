import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;

public class Filter {
    private int[][] img;
    private double[][] filter;

    /**
     * creates a filter object that has the capability to run that filter on the image
     * @param img the grayscale image to run the filter on in the form of a 2d array
     * @param filter the filter to use on the image in the form of a 2d array
     */
    public Filter(int[][] img, double[][] filter) throws Exception{
        //check the filter to make sure it works
        if(filter.length!=filter[0].length){
            throw new Exception("filter needs to be square. this might be changed in the future");
        }
        if(filter.length%2==0||filter[0].length%2==0){
            throw new Exception("Filter need to have odd dimensions");
        }
        //store the variables
        this.img = img;
        this.filter = filter;

    }

    /**
     * applies the filter to the image. crops out segments of the image where it would
     * apply the filter to a pixel that dosen't exist. If you run this function it will
     * remember the output and set it as the input for the naxt time you call this function
     * @return the filtered image.
     */
    public int[][] go() {
        int half = ((filter.length-1)/2);//a variable to store how much of the filter is to the side of the center pixel
        int[][] ret = new int[img.length-2*half][img[0].length-2*half];//a variable that will be returned, stores the image that the filter has bee applied to
        for (int x = 0; x < ret[0].length; x++) {//loop through the x positions
            for (int y = 0; y < ret.length; y++) {//loop through the y positions
                double[] result = new double[filter.length*filter[0].length];//an array to store the values of the individual pixels that the filer has been applied to
                int index = 0;//the index in the result array that we have filled
                for (int dX = -half; dX <= half; dX++) {
                    for (int dY = -half; dY <= half; dY++) {//loop through the filter starting at -half and going to half
                        int relX = x+dX;
                        int relY = y+dY;//calculate the position on the image that we are looking at
                        result[index] = img[relY+half][relX+half]*filter[dY+half][dX+half];//apply the filter to the image by multiplying the value of the pixel by the value of the filter
                        index++;//add to the index because we added an element to result
                    }//end dY
                }//end dX
                ret[y][x] = (int)average(result);//store the average of the single application of the filter into the image
            }//end y
        }//end x
        img = ret;
        return ret;//return the image
    }

    /**
     * calculates the average of an array of doubles
     * @param arr the array to average
     * @return the average of the array
     */
    private double average(double[] arr){
        double ans = 0;
        for(int i = 0; i<arr.length;i++){
            ans+=arr[i];
        }
        return ans/arr.length;
    }

    /**
     * applies the filter to the image. crops out segments of the image where it would
     * apply the filter to a pixel that dosen't exist.
     * @param times the number of times to apply the filter to the image
     * @return the filtered image.
     */
    public int[][] go(int times){
        for(int i = 0; i<times;i++){
            img = go();
        }
        return img;
    }


    //Static functions

    /**
     * converts a bufferedImg to a 2D array, averaging the colors to make it grey scale making it brighter to us
     * @param bufferedImage the image to convert
     * @return a 2D array of the image
     */
    public static int[][] bufferedImg2IntArr(BufferedImage bufferedImage){
        int[][] ret = new int[bufferedImage.getHeight()][bufferedImage.getWidth()];//a 2D array to store the answer
        for(int x = 0; x<bufferedImage.getWidth();x++){//loop through x
            for(int y = 0; y<bufferedImage.getHeight();y++){//loop through y
                Color c = new Color(bufferedImage.getRGB(x,y));//get the color from the buffered image
                ret[y][x] = (int)(c.getRed()*0.3+c.getGreen()*0.59+c.getBlue()*0.11);//saves a brightened version of the image
            }//end y
        }//end x
        return ret;//return the image
    }

    /**
     * converts a 2D array int a buffered image
     * @param i the 2D array to convert to the buffered image
     * @return the buffered image based on the 2D array
     */
    public static BufferedImage intArr2BufferedImg(int[][] i){
        BufferedImage ret = new BufferedImage(i[0].length,i.length,BufferedImage.TYPE_INT_RGB); //a buffered image to store the image in
        //finds the min and max of the array
        int min = i[0][0];
        int max = i[0][0];//sets them both to the first index of the array
        for(int x = 0; x<i[0].length;x++){//loop through x
            for(int y = 0; y<i.length;y++){//loop through y
                if(i[y][x]>max){//check max
                    max = i[y][x];
                }
                if(i[y][x]<min){//check min
                    min = i[y][x];
                }
            }//end y
        }//end x

        //map each value of the image to between 0 and 255 because images
        for(int x = 0; x<i[0].length;x++){//loop through x
            for(int y = 0; y<i.length;y++){//loop through y
                i[y][x] = map(i[y][x],min,max,0,255);//do the maping
                ret.setRGB(x,y,new Color(i[y][x],i[y][x],i[y][x]).getRGB());//save the value to the buffered image
            }//end y
        }//end x

        return ret;//return the buffered image
    }

    /**
     * a standared map function without error catching
     * @param x the value
     * @param in_min the input min
     * @param in_max the input max
     * @param out_min the output min
     * @param out_max the output max
     * @return a number between out_min and out_max that corresponds to x between in_min and in_max
     */
    private static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
