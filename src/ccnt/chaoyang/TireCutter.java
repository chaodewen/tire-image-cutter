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

        Imgproc.threshold(input, binaryImage, 10, 255, Imgproc.THRESH_OTSU);

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

//        int start = findNextBar(binaryImage, 0, false);
        int leftBegin = findNextBar(binaryImage, 0, true), leftEnd = findNextBar(binaryImage, leftBegin, false);
        int midBegin = findNextBar(binaryImage, leftEnd, true), midEnd = findNextBar(binaryImage, midBegin, false);
        int rightBegin = findNextBar(binaryImage, midEnd, true), rightEnd = findNextBar(binaryImage, rightBegin, false);

        if(leftBegin == 0 || midBegin == 0 || rightBegin == 0
                || leftEnd == 0 || midEnd == 0 || rightEnd == 0) {
            System.out.println(binaryImage + " : Region Selection Error!");
            return false;
        }

        Mat output = Imgcodecs.imread(src);

        int white = 0, black = 0, cntw = 0, cntb = 0;
        for (int i = 0; i < input.height(); i ++) {
            int w = 0, b = 0;
            for (int j = 0; j < input.width(); j++) {
                if(binaryImage.get(i, j)[0] == 0) {
                    cntw ++;
                    w ++;
                }
                else if(binaryImage.get(i, j)[0] == 255) {
                    cntb ++;
                    b ++;
                }
                output.put(i, j, binaryImage.get(i, j)[0], binaryImage.get(i, j)[0], binaryImage.get(i, j)[0]);
            }
            if(w > 1030)
                white ++;
            if(b > 800)
                black ++;
        }

        System.out.println("white : " + white);
        System.out.println("black : " + black);

        System.out.println("cntw : " + cntw);
        System.out.println("cntb : " + cntb);

//        Imgproc.floodFill(binaryImage, new Mat(300, 1000, Imgproc.THRESH_OTSU), new Point(0, 0), new Scalar(0, 255, 0));
//        Imgcodecs.imwrite("/Users/Cc/Desktop/flood.jpg", binaryImage);

//        System.out.println("start : " + start);
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
            for (int j = curCol; j < input.width(); j ++) {
                int cnt = 0;
                for (int i = 0; i < input.height(); i ++)
                    if (input.get(i, j)[0] == 0)
                        cnt ++;
                if(cnt != input.height()) {
                    if(cnt > 300) {
                        if(totalCnt == 0)
                            idx = j - 1;
                        totalCnt ++;
                    }
                    else
                        totalCnt = 0;
                    if(totalCnt > 10) {
//                    for(int i = 0; i < input.height(); i ++)
//                        System.out.println(input.get(i, idx)[0]);
                        return idx;
                    }
                }
            }
        }
        else {
            int totalCnt = 0, idx = -1;
            for (int j = curCol; j < input.width(); j ++) {
                int cnt = 0;
                for (int i = 0; i < input.height(); i++)
                    if (input.get(i, j)[0] == 255)
                        cnt++;
                if(cnt > 800) {
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