import java.awt.*;
import java.awt.image.BufferedImage;

public class Logic {

    /* Хранит проверку выбранных координат у типичных цифр с колоды карт. По идее можно заполнять этот массив
    при помощи функции numberDefinder по набору вырезанных рисунков из папки "/путь_к_проекту/numbers", но для экономии
    времени и памяти проще хранить их сразу. Да, лучше всего было бы обучать виртуальную машину при помощи KNearest
    или любого другого подобного алгоритма (готовых алгоритмов и библотек много), но так как задача была
    "придумать свой алгоритм", я решил так. Благо уложился и в время, и точность, и кол-во строк.
     Да, можно было бы использовать 0 и 1, но это бы меня немного отвлекало. Изменить не проблема.*/
    final static private String[] numbersBool = new String[]{"ttftftfftftftff", "tttfffftfffttft", "fftfttffttftfft",
            "ttttfftttffttft", "ftttffttttfttft", "tttfftfffftfftf", "ttttftttttfttft", "ttttfttftftttft",
            "tftttfttfttfttt", "fftfftfftffttft", "ftttfftfftfttff", "tfftftttftfttft", "ftfftftftttttff"};
    //Для вывода в консоль массив возможных размерностей карт
    final static private String[] numbersOut = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    //3 координаты для определения масти
    final static private int x1 = 15;
    final static private int y1 = 2;
    final static private int x2 = 10;
    final static private int y2 = 2;
    final static private int x3 = 19;
    final static private int y3 = 2;
    //Граница для определения темно-серого
    final static private int grayTrashHold = 50;
    //Граница для определеения белого
    final static private int whiteTrashHold = 200;
    //Вывод мастей
    private static final String SPADES = "s";
    private static final String HEARTS = "h";
    private static final String CLUBS = "c";
    private static final String DIAMONDS = "d";

    //На текущем этапе он не нужен даже для инициализации
    Logic() {
    }

    //Определяет масть на карте
    public static String suitDefinder(BufferedImage img) {
        String res = new String();
        int x = img.getWidth() / 2;
        int y = img.getHeight() / 2;
        Color c = new Color(img.getRGB(x, y));
        if (c.getBlue() < grayTrashHold && c.getRed() < grayTrashHold && c.getGreen() < grayTrashHold) {
            Color c1 = new Color(img.getRGB(x1, y1));
            Color c2 = new Color(img.getRGB(x2, y2));
            Color c3 = new Color(img.getRGB(x3, y3));
            res = c1.equals(c2) && c1.equals(c3) ? CLUBS : SPADES;
        } else {
            Color c1 = new Color(img.getRGB(x1, y1));
            Color c2 = new Color(img.getRGB(x2, y2));
            Color c3 = new Color(img.getRGB(x3, y3));
            res = c1.equals(c2) && c1.equals(c3) ? HEARTS : DIAMONDS;
        }
        return res;
    }

    //Определяет основные пиксели на цифре (фон или цифра)
    public static String numberDefinder(BufferedImage img, boolean isShadow) {
        StringBuilder s = new StringBuilder();
        int w = img.getHeight();
        int h = img.getHeight();
        int x = 0;
        int y = 0;
        int backgroundTrashHold;
        backgroundTrashHold = isShadow ? 90 : 190;
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 3; k++) {
                x = 1 + k * 5;
                y = 1 + i * 4;
                Color c = new Color(img.getRGB(x, y));
                if (c.getRed() > backgroundTrashHold || c.getGreen() > backgroundTrashHold || c.getBlue() > backgroundTrashHold)
                    s.append("f");
                else
                    s.append("t");
            }
        }
        return s.toString();
    }


    //Проверяет на затемненность
    public static boolean shadowCheck(Color c) {
        if (c.getRed() > whiteTrashHold && c.getBlue() > whiteTrashHold && c.getGreen() > whiteTrashHold)
            return false;
        else return true;
    }

    //На основе получившейся строки с определенной полезностю пикселей на карте определяет достоинство карты
    public static String numberResult(String s) {
        int resDistance = 6;
        String res = new String();
        for (int i = 0; i < 13; i++) {
            int distance = 0;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) != numbersBool[i].charAt(j))
                    distance++;
            }
            if (distance <= resDistance) {
                resDistance = distance;
                res = numbersOut[i];
            }
        }
        return res;
    }
}