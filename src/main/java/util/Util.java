package util;


public class Util {
    private static volatile Util INSTANCE;

    public static Util getInstance() {
        if (null == INSTANCE) {
            synchronized (Util.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Util();
                }
            }
        }
        return INSTANCE;
    }
}

