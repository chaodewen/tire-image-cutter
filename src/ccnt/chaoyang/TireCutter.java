package ccnt.chaoyang;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Cc on 2017/4/21.
 */

public class TireCutter {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private static String binTarget = "/Users/Cc/Desktop/try2.jpg";
    public static void main(String[] args) {
        String src = "/Users/Cc/Desktop/try.jpg", target = "/Users/Cc/Desktop/try3.jpg";
        System.out.println(handle(src, target));
    }
    private static boolean handle(String src, String target) {
        Mat input = Imgcodecs.imread(src, Imgcodecs.IMREAD_GRAYSCALE), binaryImage = new Mat();

        Imgproc.threshold(input, binaryImage, 127, 255, Imgproc.THRESH_OTSU);

        System.out.println(binaryImage.channels());
        System.out.println(binaryImage.size());
        System.out.println(binaryImage.total());
        System.out.println(binaryImage.width());
        System.out.println(binaryImage.height());

        Imgcodecs.imwrite(binTarget, binaryImage);

//        for(int i = 0; i < 29; i ++) {
//            for (int j = 0; j < binaryImage.width(); j++)
//                System.out.print(binaryImage.get(i, j)[0] + " ");
//            System.out.println();
//        }

        int leftBegin = findNextBar(binaryImage, 0, true), leftEnd = findNextBar(binaryImage, leftBegin, false);
        int midBegin = findNextBar(binaryImage, leftEnd, true), midEnd = findNextBar(binaryImage, midBegin, false);
        int rightBegin = findNextBar(binaryImage, midEnd, true), rightEnd = findNextBar(binaryImage, rightBegin, false);

        if(leftBegin == 0 || midBegin == 0 || rightBegin == 0
                || leftEnd == 0 || midEnd == 0 || rightEnd == 0) {
            System.out.println(binaryImage + " : Region Selection Error!");
            return false;
        }

        Mat output = Imgcodecs.imread(src);
        for(int i = 0; i < input.height(); i ++)
            for(int j = 0; j < input.width(); j ++) {
                output.get(i, j)[0] = binaryImage.get(i, j)[0];
                output.get(i, j)[1] = binaryImage.get(i, j)[0];
                output.get(i, j)[2] = binaryImage.get(i, j)[0];
            }
        System.out.println(leftBegin + ", " + leftEnd);
        Imgproc.rectangle(output, new Point(leftBegin, 0), new Point(leftEnd, output.height() - 1)
                , new Scalar(0, 255, 0));
        System.out.println(midBegin + ", " + midEnd);
        Imgproc.rectangle(output, new Point(midBegin, 0), new Point(midEnd, output.height() - 1)
                , new Scalar(0, 255, 0));
        System.out.println(rightBegin + ", " + rightEnd);
        Imgproc.rectangle(output, new Point(rightBegin, 0), new Point(rightEnd, output.height() - 1)
                , new Scalar(0, 255, 0));

        Imgcodecs.imwrite(target, output);

        return true;
    }
    private static int findNextBar(Mat input, int curCol, boolean white) {
        if(white) {
            int totalCnt = 0, idx = -1;
            for (int j = curCol - 10 > 0 ? curCol : 10; j < input.width() - 10; j ++) {
                int cnt = 0;
                for (int i = 0; i < input.height(); i++)
                    if (input.get(i, j)[0] == 0)
                        cnt ++;
                if(cnt > 100) {
                    if(totalCnt == 0)
                        idx = j - 1;
                    totalCnt ++;
                }
                else
                    totalCnt = 0;
                if(totalCnt > 10)
                    return idx;
            }
        }
        else {
            int totalCnt = 0, idx = -1;
            for (int j = curCol - 10 > 0 ? curCol : 10; j < input.width() - 10; j ++) {
                int cnt = 0;
                for (int i = 0; i < input.height(); i++)
                    if (input.get(i, j)[0] == 255)
                        cnt++;
                if(cnt > 900) {
                    if(totalCnt == 0)
                        idx = j - 1;
                    totalCnt ++;
                }
                else
                    totalCnt = 0;
                if(totalCnt > 10)
                    return idx;
            }
        }

        return -1;
    }
}