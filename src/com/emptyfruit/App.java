package com.emptyfruit;

import java.math.BigInteger;
import java.util.*;
/**
 * Класс умеет находить медиану 2-х равных по длинне массивов, а также определять первое вхождение заданной цифровой
 * последовательности А в бесконечной последовательности цифр S типа (1234567891011...).
 */
public class App {

    public static void main(String[] args) {

        // запускаем интерфейс программы
        AppInterface face = new AppInterface();
        face.setVisible(true);
        face.pack();

    }

    /**
     * метод рассчета медианы для 2-х объединенных массивов одинаковой длинны
     */
    public static double median(int[] arrayA, int[] arrayB) throws Exception {

        // длины исходных массивов (для читаемости)
        int lengthA = arrayA.length; int lengthB = arrayB.length;

        // проверяем длины массивов (на соответствие условию)
        // если длина массивов одинакова, то выполняем
        if (lengthA == lengthB) {
            // объединяем массивы в новый массив
            int[] arrayCombined = new int[lengthA + lengthB];
            System.arraycopy(arrayA, 0, arrayCombined, 0, lengthA);
            System.arraycopy(arrayB, 0, arrayCombined, lengthA, lengthB);
            // сортируем массив
            Arrays.sort(arrayCombined);
            // вычисляем медиану (т.к. исходные массивы имею одинкому длину, общий массив имеет четное кол-во элементов)
            // берем 2 центральных члена массива
            double m1 = arrayCombined[arrayCombined.length / 2 - 1];
            double m2 = arrayCombined[arrayCombined.length / 2];
            // вычисляем медиану
            double median = (m1 + m2) / 2;
            return median;
        }
        // если длины массивов разные, выдаем ошибку:
        else {
            throw new Exception("Массивы должны быть одникового размера");
        }
    }

    /**
     * метод поиска первого вхождения
      */
    public  static BigInteger findFirstEntry(String str) throws Exception {
        // переменная для проверки не найдет ли результат
        boolean found = false;
        // получаем из строки данных массив А, представляющий собой цифровую последовательность А
        ArrayList<Integer> arrayA = toIntegerList(str);
        // создаем массив S, представляющий собой выбоку по цифрам, такого же размера как массив A
        ArrayList<Integer> arrayS = new ArrayList<>();
        // прибавляем одну цифру к массиву S пока их не станет столько же сколько в А:
        // создаем переменную, которая будет хранить следующее число для "приклеивания" к ряду S.
        // (теоретически цифровая последовательность может быть очень большой, поэтому BigInteger)
        BigInteger nextNumber = BigInteger.ONE;
        // создаем банк в котором это число будет храниться в виде цифр для пополнения массива S
        ArrayList<Integer> bank = new ArrayList<>();
        // переменная для проверки хватает ли цифр в стартовом массиве S
        boolean enough = false;
        // если входные данные указаны, то ищем:
        if (!(arrayA.isEmpty())) {
            // заполняем исходный ряд S пока не он не сравняется с А по размеру
            while (!enough) {
                // пополняем банк
                if (bank.isEmpty()) {
                    updateBank(bank, nextNumber);
                    // обновляем следующее число
                    nextNumber = nextNumber.add(BigInteger.ONE);
                }
                // прибавляем число к S по отдельным цифрам
                arrayS.add(bank.get(bank.size() - 1));
                // убираем из банка использованную цифру
                bank.remove((bank.size() - 1));
                // проверяем хватает ли цифр в ряду S
                enough = arrayS.size() == arrayA.size();
            }
            // переменная обозначает номер первого вхождения искомой последовательности
            // поскольку это счетчик, он может достигать очень большого числа => BigInteger.
            BigInteger entry = BigInteger.ONE;
            // сравниваем S и А
            found = arrayA.equals(arrayS);

            //движемся по виртуальному ряду S выборкой размера ряда А пока не совпадем с рядом А.
            while (!found) {
                // добавляем следующее число в виде цифр к S
                // если банк пуст - пополняем
                if (bank.isEmpty()) {
                    updateBank(bank, nextNumber);
                    // обновляем следующее число
                    nextNumber = nextNumber.add(BigInteger.ONE);
                }
                // убираем одну цифру слева (пройденную)
                arrayS.remove(0);
                // добавляем цифру из банка (последнюю, так как она является первой цифрой числа).
                arrayS.add(bank.get(bank.size() - 1));
                // удаляем из банка использованную цифру
                bank.remove(bank.size() - 1);
                // сравниваем S и A
                found = arrayS.equals(arrayA);
                // повышяем счетчик
                entry = entry.add(BigInteger.ONE);
                if (entry.mod(new BigInteger("10000000")).equals(BigInteger.ZERO)) {
                    System.out.println("reached " + entry + " entries");
                }
            }
            return entry;
        // если входные данные не указаны, выдаем ошибку
        } else {
            throw new Exception("Последовательность не указана");
        }
    }

    /**
     * метод добавляет число в массив по отдельным цифрам
      */
    public static void updateBank(ArrayList<Integer> bank, BigInteger number) {
        // превращаем число в массив цифр (способ выбран для скорости)
        String s = number.toString(); // (например 123456)
        // добавляем в массив задом наперед
        for (int i=s.length()-1; i>=0; i--)
        {
            bank.add(s.charAt(i)-'0');
        } // (получаем 654321). Нужно для того, чтобы выбор следующей цифры из банка проходил быстрее
    }

    /**
     * метод певращает строку цифр в массив (список) цифр
      */
    public static ArrayList<Integer> toIntegerList(String str) throws Exception {
        // переводим строку в массив символов
        char[] sArray = str.toCharArray();
        // создаем список
        ArrayList<Integer> list = new ArrayList<>();
        // добавляем цифры

        for (int i=0; i<str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                list.add(str.charAt(i)-'0');
            } else {
                throw new Exception("В строке могут быть только цифры");
            }
        }
        return list;

    }

}


