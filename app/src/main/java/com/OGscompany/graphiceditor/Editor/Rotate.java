package com.OGscompany.graphiceditor.Editor;

import android.graphics.Bitmap;
import android.view.View;

public class Rotate{
	private Bitmap rotatePrep(Bitmap bitin, Bitmap bitout, int angle) {
		if (angle < 0){
			angle += 360;
		}
        if (((angle/90)&1)==1){
            bitout = Bitmap.createBitmap(bitin.getHeight(),bitin.getWidth(),Bitmap.Config.ARGB_8888);
        }
        for(int i = 0;i<bitin.getWidth();i++){
            for(int j = 0;j<bitin.getHeight();j++){
                int iTemp = i;
				int jTemp = j;
                if(((angle/90)&1)==1){
                    iTemp = bitin.getHeight() - j;
                    jTemp = i;
                }
                if(((angle/90)&2)==2){
                    iTemp = bitin.getHeight() - j;
                    jTemp = bitin.getWidth() - i;
                }
				if(jTemp<0||jTemp>=bitout.getHeight()){
					continue;
				}
                if(iTemp<0||iTemp>=bitout.getWidth()){
					continue;
				}
                bitout.setPixel(iTemp, jTemp, bitin.getPixel(i, j));
            }
        }
		return bitout;
	}
    public Bitmap rotateOnAngle(Bitmap bitin, int angle) {
        Bitmap bitout = bitin.copy(Bitmap.Config.ARGB_8888, true);
        bitin = rotatePrep(bitin,bitout,angle);
        int width = bitout.getWidth();
        int height = bitout.getHeight();
        double temp = (double) (90-angle%90)*Math.PI/180.0;
        double cosA = Math.cos(temp);
        double sinA = Math.sin(temp);
        double wshc = width*sinA + height*cosA;
        double hswc = height*sinA + width*cosA;
        bitout = Bitmap.createBitmap((int) wshc + 2, (int) hswc + 2, Bitmap.Config.ARGB_8888);
        bitout = bitout.copy(Bitmap.Config.ARGB_8888, true);
        for (int inter=0;inter<=(int) wshc;inter++) {
            for (int inter2=0;inter2<=(int) hswc;inter2++) {
                int i = (int) (inter*sinA-inter2*cosA+width*cosA*cosA);
                int j = (int) (inter*cosA+inter2*sinA-width*sinA*cosA);
                if(i<0||i>= bitin.getWidth()){
					continue;
				}
                if(j<0||j>= bitin.getHeight()){
					continue;
				}
                bitout.setPixel(inter, inter2, bitin.getPixel(i, j));
            }
        }
        return bitout;
    }
}