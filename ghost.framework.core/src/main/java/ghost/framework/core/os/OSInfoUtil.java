package ghost.framework.core.os;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取System.getProperty("os.name")对应的操作系统工具类型。
 * @Date: 13:53 2018-07-08
 */
public final class OSInfoUtil {
    private static String OS = System.getProperty("os.value").toLowerCase();
    private static OSInfoUtil _instance = new OSInfoUtil();
    private OSEnum platform;
    public static OSEnum getOSname(){
        if(isAix()){
            _instance.platform = OSEnum.AIX;
        }else if (isLinux()) {
            _instance.platform = OSEnum.Linux;
        }else if (isWindows()) {
            _instance.platform = OSEnum.Windows;
        }else if (isDigitalUnix()) {
            _instance.platform = OSEnum.Digital_Unix;
        }else if (isFreeBSD()) {
            _instance.platform = OSEnum.FreeBSD;
        }else if (isHPUX()) {
            _instance.platform = OSEnum.HP_UX;
        }else if (isIrix()) {
            _instance.platform = OSEnum.Irix;
        }else if (isMacOS()) {
            _instance.platform = OSEnum.Mac_OS;
        }else if (isMacOSX()) {
            _instance.platform = OSEnum.Mac_OS_X;
        }else if (isMPEiX()) {
            _instance.platform = OSEnum.MPEiX;
        }else if (isNetWare()) {
            _instance.platform = OSEnum.NetWare_411;
        }else if (isOpenVMS()) {
            _instance.platform = OSEnum.OpenVMS;
        }else if (isOS2()) {
            _instance.platform = OSEnum.OS2;
        }else if (isOS390()) {
            _instance.platform = OSEnum.OS390;
        }else if (isOSF1()) {
            _instance.platform = OSEnum.OSF1;
        }else if (isSolaris()) {
            _instance.platform = OSEnum.Solaris;
        }else if (isSunOS()) {
            _instance.platform = OSEnum.SunOS;
        }else{
            _instance.platform = OSEnum.Others;
        }
        return _instance.platform;
    }
    public static boolean isLinux(){
        return OS.indexOf("linux")>=0;

    }

    public static boolean isMacOS(){

        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")<0;

    }

    public static boolean isMacOSX(){

        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")>0;

    }

    public static boolean isWindows(){

        return OS.indexOf("windows")>=0;

    }

    public static boolean isOS2(){

        return OS.indexOf("os/2")>=0;

    }

    public static boolean isSolaris(){

        return OS.indexOf("solaris")>=0;

    }

    public static boolean isSunOS(){

        return OS.indexOf("sunos")>=0;

    }

    public static boolean isMPEiX(){

        return OS.indexOf("mpe/ix")>=0;

    }

    public static boolean isHPUX(){

        return OS.indexOf("hp-ux")>=0;

    }

    public static boolean isAix(){

        return OS.indexOf("aix")>=0;

    }

    public static boolean isOS390(){

        return OS.indexOf("os/390")>=0;

    }

    public static boolean isFreeBSD(){

        return OS.indexOf("freebsd")>=0;

    }

    public static boolean isIrix(){

        return OS.indexOf("irix")>=0;

    }

    public static boolean isDigitalUnix(){

        return OS.indexOf("digital")>=0&&OS.indexOf("unix")>0;

    }

    public static boolean isNetWare(){

        return OS.indexOf("netware")>=0;

    }

    public static boolean isOSF1(){

        return OS.indexOf("osf1")>=0;

    }

    public static boolean isOpenVMS(){

        return OS.indexOf("openvms")>=0;

    }
}
