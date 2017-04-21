package ccnt.chaoyang;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by Cc on 2017/4/20.
 */

// -Djava.library.path=/Users/Cc/IdeaProjects/tire-image-cutter/lib/opencv-3.2.0/lib;D:/Users/Cc/IdeaProjects/tire-image-cutter/lib/opencv-3.2.0/bin

public class Main {
    public static void main(String[] args) {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        Mat image = imread("/Users/Cc/Desktop/test.png", 1);
        Mat grayImage;
    }
}