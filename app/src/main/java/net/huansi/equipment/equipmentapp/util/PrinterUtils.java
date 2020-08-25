package net.huansi.equipment.equipmentapp.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

public class PrinterUtils {
    /**
     * 设置二维码大小
     */
    public static final byte[] setCodeSize = new byte[8];
    /**
     * 设置纠错正等级
     */
    public static final byte[] setCodeLevel = new byte[] {0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x30};
    /**
     * 加载二维码
     */
    public static byte[] setCode = new byte[8];
    /**
     * 打印二维码
     */
    public static final byte[] printCode = new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30};
    /**
     * 设置加载二维码指令
     * @param code
     */
    public static void doSetCode(String code) {
        setCode[0] = 0x1D;
        setCode[1] = 0x28;
        setCode[2] = 0x6B;
        setCode[3] = (byte) (code.length()+3);
        setCode[4] = 0x00;
        setCode[5] = 0x31;
        setCode[6] = 0x50;
        setCode[7] = 0x30;
    }











    /**
     * 设置二维码大小
     * @param size
     */
    public static void doSetQrSize(int size) {
        setCodeSize[0] = 0x1D;
        setCodeSize[1] = 0x28;
        setCodeSize[2] = 0x6B;
        setCodeSize[3] = 0x03;
        setCodeSize[4] = 0x00;
        setCodeSize[5] = 0x31;
        setCodeSize[6] = 0x43;
        setCodeSize[7] = (byte)size;
    }

    /**
     * byte转Byte
     *
     * @param srcArray
     * @param cpyArray
     */
    public static void CopyArray(byte[] srcArray, byte[] cpyArray) {
        for (int index = 0; index < cpyArray.length; index++) {
            cpyArray[index] = srcArray[index];
        }
    }
}
