package ru.mail.polis.homework.simple;


/**
 * Возможно вам понадобится класс Math с его методами. Например, чтобы вычислить квадратный корень, достаточно написать
 * Math.sqrt(1.44)
 * Чтобы увидеть все методы класса Math, достаточно написать Math. и среда вам сама покажет возможные методы.
 * Для просмотра подробной документации по выбранному методу нажмите Ctrl + q
 */
public class IntegerAdvancedTask {

    private static final double EPS = 1e-10;
    private static final byte HEX_BASE = 16;

    /**
     * Сумма первых n-членов геометрической прогрессии с первым элементом a и множителем r
     * a + aq + aq^2 + ... + aq^(n-1)
     * <p>
     * Пример: (1, 2, 3) -> 7
     */
    public static long progression(int a, double q, int n) {
        if (Math.abs(q - 1) < EPS) {
            return (long) a * n;
        }
        // Формула суммы n первых членов геометрической прогрессии.
        return (long) (a * (Math.pow(q, n) - 1) / (q - 1));
    }

    /**
     * Гусеница ползает по столу квадратами по часовой стрелке. За день она двигается следующим образом:
     * сначала наверх на up, потом направо на right. Ночью она двигается вниз на down и налево на left.
     * Сколько суток понадобится гусенице, чтобы доползти до поля с травой?
     * Считаем, что на каждой клетке с координатами >= grassX или >= grassY находится трава
     * Если она этого никогда не сможет сделать, Верните число Integer.MAX_VALUE;
     * Пример: (10, 3, 5, 5, 20, 11) -> 2
     */
    public static int snake(int up, int right, int down, int left, int grassX, int grassY) {
        if (up >= grassY || right >= grassX) {
            return 1;
        }

        int dx = right - left;
        int dy = up - down;

        if (dx <= 0 && dy <= 0) {
            return Integer.MAX_VALUE;
        }

        // Если достигнем одной из этих точек - на следующий день попадем на поле.
        int criticalX = grassX - right;
        int criticalY = grassY - up;
        int resultX = (criticalX % dx == 0 ? criticalX / dx : criticalX / dx + 1) + 1;
        int resultY = (criticalY % dy == 0 ? criticalY / dy : criticalY / dy + 1) + 1;

        if (dx > 0 && dy > 0) {
            return Math.min(resultX, resultY);
        }
        if (dx > 0) {
            return resultX;
        }
        return resultY;
    }

    /**
     * Дано число n в 10-ном формате и номер разряда order.
     * Выведите цифру стоящую на нужном разряде для числа n в 16-ом формате
     * Нельзя пользоваться String-ами
     * Пример: (454355, 2) -> D
     */
    public static char kDecimal(int n, int order) {
        int number = n;
        number /= (int) Math.pow(16, order - 1);
        int mod = number % 16;

        if (mod >= 10) {
            return (char) (mod + 'A' - 10);
        }
        return (char) (mod + '0');
    }

    /**
     * Дано число в 10-ном формате.
     * Нужно вывести номер минимальной цифры для числа в 16-ном формате. Счет начинается справа налево,
     * выводим номер первой минимальной цифры (если их несколько)
     * Нельзя пользоваться String-ами
     * (6726455) -> 2
     */
    public static byte minNumber(long a) {
        long number = a;
        byte min = HEX_BASE;
        byte minIndex = 0;
        byte count = 0;
        byte current;

        while (number > 0 && min > 0) {
            count++;
            current = (byte) (number % HEX_BASE);
            if (current < min) {
                min = current;
                minIndex = count;
            }
            number /= HEX_BASE;
        }

        return minIndex;
    }

}
