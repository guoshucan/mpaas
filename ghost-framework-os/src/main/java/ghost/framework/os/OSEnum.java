package ghost.framework.os;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:平台枚举。
 * @Date: 13:56 2018-07-08
 */
public enum OSEnum {
    Any("any"),

    Linux("Linux"),

    Mac_OS("Mac OS"),

    Mac_OS_X("Mac OS X"),

    Windows("Windows"),

    OS2("OS/2"),

    Solaris("Solaris"),

    SunOS("SunOS"),

    MPEiX("MPE/iX"),

    HP_UX("HP-UX"),

    AIX("AIX"),

    OS390("OS/390"),

    FreeBSD("FreeBSD"),

    Irix("Irix"),

    Digital_Unix("Digital Unix"),

    NetWare_411("NetWare"),

    OSF1("OSF1"),

    OpenVMS("OpenVMS"),

    Others("Others");

    private OSEnum(String os){
        this.os = os;
    }
    public String toString(){
        return os;
    }
    private String os;
}
