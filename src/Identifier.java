import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Identifier {

    /*Размеры карты при выходном размере 636x1166 пикселей. Можно было бы попытаться разработать формулу, но так как все
    входящие изображения одинаковой размерности проверить ее довольно сложно*/
    final static private int cardSizeX = 63;
    final static private int cardSizeY = 87;
    //размеры области нахождения цифры
    final static private int numberSizeX = 15;
    final static private int numberSizeY = 22;
    //размеры области нахождения масти
    final static private int suitSizeX = 31;
    final static private int suitSizeY = 31;
    //Координаты цифры на вырезанной карту
    final static private int numberCoordX = 10;
    final static private int numberCoordY = 6;
    //Координаты масти на вырезанной карте
    final static private int suitCoordX = 26;
    final static private int suitCoordY = 50;
    //Координаты карт на исходном рисунке размерностью 636x1166 пикселей
    final static private int cardCoordY = 586;
    final static private int[] cardCoordX = {143, 215, 286, 358, 430};
    //Координаты для проверки на затемненность карты
    final static private int shadowCheckX = 58;
    final static private int shadowCheckY = 5;

    private String folderPath;
    private List<File> filesInFolder;
    private static Logic logic;

    //конструктор. Принимает из командной строки путь к папке с изображениями и сохраняет его
    Identifier(String foldrePath) {
        this.folderPath = foldrePath;
        System.out.println("Catalog: " + foldrePath);
    }

    //старт работы программы
    public void start() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            filesInFolder = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .sorted(Comparator.comparing(File::getName))
                    .collect(Collectors.toList());
        }

        logic = new Logic();
        //для каждого файла в каталоге
        filesInFolder.forEach(Identifier::cardIdentifier);
    }

    //Фнкция, для обработки каждой карты
    private static void cardIdentifier(File f) {
        StringBuilder res = new StringBuilder();
        res.append(f.getName());
        res.append(" - ");
        try {
            BufferedImage img = ImageIO.read(f);
            for (int i = 0; i < cardCoordX.length; i++) {
                BufferedImage card = img.getSubimage(cardCoordX[i], cardCoordY, cardSizeX, cardSizeY);
                Color test = new Color(card.getRGB(1, 1));
                if (test.getBlue() <= 50 && test.getRed() <= 50 && test.getGreen() <= 50)
                    break;
                BufferedImage imgNumber = card.getSubimage(numberCoordX, numberCoordY, numberSizeX, numberSizeY);
                ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                ColorConvertOp op = new ColorConvertOp(cs, null);
                BufferedImage imgNumberWB = op.filter(imgNumber, null);
                Color check = new Color(card.getRGB(shadowCheckX, shadowCheckY));
                res.append(logic.numberResult(logic.numberDefinder(imgNumberWB, logic.shadowCheck(check))));
                BufferedImage imgSuit = card.getSubimage(suitCoordX, suitCoordY, suitSizeX, suitSizeY);
                res.append(logic.suitDefinder(imgSuit));
            }
            System.out.println(res.toString());
        } catch (IOException ex) {
            System.out.println("File " + f.getName() + " can not be open");
        }
    }
}