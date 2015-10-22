package com.emptyfruit;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Класс представляет интерфейс программы
 */
public class AppInterface extends JFrame {

    private JButton buttonM;
    private JButton buttonE;
    private JLabel choiceLabel;
    private JLabel taskLabel;
    private JPanel medianPanel;
    private JPanel entryPanel;
    private boolean median = false;
    private boolean entry = false;


    public AppInterface() {
        super("Emptyfruit for HeadHunter");

        // задаем layout основного окна
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS)
        );
        // задаем кроссплатформенный внещний вид
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }  catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // задаем изначальные рамеры окна программы
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(350, 100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // кнопки выбора режимов программы
        buttonM = new JButton("Рассчет медианы");
        buttonM.addActionListener(new ButtonMActionListener());
        buttonM.setFocusPainted(false);
        buttonE = new JButton("Первое вхождение");
        buttonE.addActionListener(new ButtonEActionListener());
        buttonE.setFocusPainted(false);
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(buttonE);
        buttonsPanel.add(buttonM);
        // легенда к кнопкам выбора функции
        choiceLabel = new JLabel("Выберите задание");
        JPanel choiceLabelPanel = new JPanel();
        choiceLabelPanel.add(choiceLabel);
        // инструкция к вводу данных
        taskLabel = new JLabel();
        JPanel taskLabelPanel = new JPanel();
        taskLabelPanel.add(taskLabel);
        // заполняем интерфейс
        add(choiceLabelPanel);
        add(buttonsPanel);
        add(taskLabelPanel);

        // задаем изначальные положение окна программы
        setLocationRelativeTo(null);

    }

    class ButtonMActionListener implements ActionListener{
        private JTextArea array1Text;
        private JTextArea array2Text;
        private JButton loadArray1Button;
        private JButton loadArray2Button;
        private JPanel insertPanel;
        private JPanel resultPanel;
        private JLabel resultLabel;
        private JButton executeButton;
        @Override
        public void actionPerformed(ActionEvent e) {

            // очищаем интерфейс
            if (!(entryPanel==null)) {
                remove(entryPanel);
            }
            // подкрашиваем кнопку, соответствующую функции программы
            buttonM.setEnabled(false);
            buttonM.setBackground(Color.orange);
            buttonM.setBorderPainted(false);
            buttonE.setBackground(null);
            buttonE.setEnabled(true);
            // меняем инструкцию
            taskLabel.setText("<html><center>" +
                                "Введите числа через запятую без пробелов <br> или загрузите из .txt файлов"
                                 + "</center></html>");
            // создаем основные элементы верстки
            insertPanel = new JPanel();
            medianPanel = new JPanel(new BorderLayout());
            // атрибуты для верстки
            Dimension textAreaDim = new Dimension(150,50);
            Border solidBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

            // форма для ввода массива 1
            array1Text = new JTextArea();
            array1Text.setBorder(solidBorder);
            array1Text.setLineWrap(true);
            JScrollPane array1Scroll = new JScrollPane(array1Text);
            array1Scroll.setPreferredSize(textAreaDim);
            JPanel array1TextPanel = new JPanel();
            array1TextPanel.add(array1Scroll);
            // форма для ввода массива 2
            array2Text = new JTextArea();
            array2Text.setBorder(solidBorder);
            array2Text.setLineWrap(true);
            JScrollPane array2Scroll = new JScrollPane(array2Text);
            array2Scroll.setPreferredSize(textAreaDim);
            JPanel array2TextPanel = new JPanel();
            array2TextPanel.add(array2Scroll);

            // кнопка загрузки из файла массива 1
            loadArray1Button = new JButton("Загрузить..");
            loadArray1Button.addActionListener(new loadArray1ButtonActionListener());
            JPanel loadArray1ButtonPanel = new JPanel();
            loadArray1ButtonPanel.add(loadArray1Button);
            loadArray1ButtonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            // кнопка загрузки из файла массива 2
            loadArray2Button = new JButton("Загрузить..");
            loadArray2Button.addActionListener(new loadArray2ButtonActionListener());
            JPanel loadArray2ButtonPanel = new  JPanel();
            loadArray2ButtonPanel.add(loadArray2Button);

            // настраиваем и заполняем панель ввода данных
            insertPanel.setLayout(new GridLayout(0, 2, 0, 1));
            insertPanel.setPreferredSize(new Dimension(0 , 150));
            //insertPanel.setSize(10,5);
            insertPanel.add(loadArray1ButtonPanel);
            insertPanel.add(array1TextPanel);
            insertPanel.add(loadArray2ButtonPanel);
            insertPanel.add(array2TextPanel);
            // панель результата
            resultPanel = new JPanel();
            // кнопка для запуска
            executeButton = new JButton("Вычислить");
            executeButton.addActionListener(new executeButtonActionListener());
            executeButton.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel executeButtonPanel = new JPanel();
            executeButtonPanel.add(executeButton);
            //  результат
            resultLabel = new JLabel();
            // настраиваем и заполняем панель результата
            resultPanel.setLayout(new GridLayout(1,0,1,1));
            resultPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            resultPanel.add(executeButtonPanel);
            resultPanel.add(resultLabel);
            // настраиваем и заполняем общую панель расчета медианы
            // medianPanel.setSize(300,300);
            medianPanel.add(insertPanel, BorderLayout.NORTH);
            medianPanel.add(resultPanel, BorderLayout.SOUTH);
            add(medianPanel);

            repaint();
            pack();

        }

        class executeButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    String [] arr1 = array1Text.getText().split(",");
                    String [] arr2 = array2Text.getText().split(",");

                    int[] array1 = new int[arr1.length];
                    for (int i=0; i<arr1.length; i++) {
                        array1[i] = Integer.parseInt(arr1[i]);
                    }

                    int[] array2 = new int[arr2.length];
                    for (int i=0; i<arr2.length; i++) {
                        array2[i] = Integer.parseInt(arr2[i]);
                    }

                    double result = App.median(array1, array2);
                    resultLabel.setText("Медиана: " + String.valueOf(result));

                } catch (NumberFormatException ex) {
                    resultLabel.setText("Ошибка ввода данных");
                } catch (Exception ex) {
                    resultLabel.setText("<html>"+ex.getMessage()+"</html>");
                }

            }
        }
        class loadArray1ButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String digits = "";
                // открываем файл
                JFileChooser fc = new JFileChooser();
                int ret = fc.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        FileReader reader = new FileReader(file);
                        BufferedReader in = new BufferedReader(reader);
                        digits = in.readLine();
                    } catch (Exception ex) {};

                    // заменяем окно ввода с клавиатуры на полученное значение
                    array1Text.setText(digits);
                    //label.setText(file.getName());
                }
            }
        }
        class loadArray2ButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numbers= "";
                // открываем файл
                JFileChooser fc = new JFileChooser();
                int ret = fc.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        FileReader reader = new FileReader(file);
                        BufferedReader in = new BufferedReader(reader);
                        numbers = in.readLine();
                    } catch (Exception ex) {};

                    // заменяем окно ввода с клавиатуры на полученное значение
                    array2Text.setText(numbers);
                    //label.setText(file.getName());
                }
            }
        }

    }

    class ButtonEActionListener implements ActionListener{
        private JPanel innerPanel;
        private JButton loadButton;
        private JTextArea textArea;
        private JButton executeButton;
        private JTextArea resultTextArea;
        @Override
        public void actionPerformed(ActionEvent e){
            // переключаем и перекрашиваем кнопки
            buttonE.setEnabled(false);
            buttonE.setBackground(Color.orange);
            buttonE.setBorderPainted(false);
            buttonM.setEnabled(true);
            buttonM.setBackground(null);
            // убираем панель расчета медианы
            if (!(medianPanel==null)) {
                remove(medianPanel);
            }
            // обновляем инструкцию
            taskLabel.setText("<html><center>" +
                    "Введите последовтельность цифр <br> " +
                            "или загрузите файл .txt c одной строкой" +
                    "</center></html>");
            // обновляем интерфейс
            revalidate();
            repaint();

            // панель расчета первого вхождения
            entryPanel = new JPanel();
            innerPanel = new JPanel();
            innerPanel.setLayout(new GridLayout(2,2));
            // ввод данных с клавиатуры
            textArea = new JTextArea(1, 12);
            textArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JPanel scrollPanePanel = new JPanel();
            scrollPanePanel.add(scrollPane);
            // ввод данных с кнопки
            loadButton = new JButton("Загрузить..");
            loadButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
            loadButton.addActionListener(new loadButtonActionListener());
            JPanel loadButtonPanel = new JPanel();
            loadButtonPanel.add(loadButton);
            // кнопка выполнения вычислений
            executeButton = new JButton("Вычислить");
            executeButton.addActionListener(new executeButtonActionListener());
            JPanel executeButtonPanel = new JPanel();
            executeButtonPanel.add(executeButton);
            // результат
            resultTextArea = new JTextArea("Результат:");
            resultTextArea.setWrapStyleWord(true);
            resultTextArea.setLineWrap(true);
            resultTextArea.setBackground(null);
            Font font = new Font("Verdana", Font.BOLD, 12);
            resultTextArea.setFont(font);

            // настраиваем и заполняем основную панель
            innerPanel.add(loadButtonPanel);
            innerPanel.add(scrollPanePanel);
            innerPanel.add(executeButtonPanel);
            innerPanel.add(resultTextArea);
            entryPanel.add(innerPanel);
            add(entryPanel);

            repaint();
            pack();
        }

        class loadButtonActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String digits = "";
                // открываем файл
                JFileChooser fc = new JFileChooser();
                int ret = fc.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    // считываем первую строку из файла
                    try {
                        FileReader reader = new FileReader(file);
                        BufferedReader in = new BufferedReader(reader);
                        digits = in.readLine();
                    } catch (Exception ex) {};

                // заменяем окно ввода с клавиатуры на полученное значение
                textArea.setText(digits);
                }
            }
        }
        class executeButtonActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                // создаем новый поток (для того чтобы вывести на экран сообщение о выполнении расчетов
                Thread t = new Thread((new Runnable() {
                    @Override
                    public void run() {
                        String result;
                        String digits = textArea.getText();
                        // сообщаем о вычислении
                        resultTextArea.setText("Идет вычисление..");
                        try {
                            // вызывем основный метод
                            BigInteger value = App.findFirstEntry(digits);
                            // обрабатываем результат для вывода (разбиваем по разрядам)
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(' ');
                            DecimalFormat df = new DecimalFormat();
                            df.setDecimalFormatSymbols(symbols);
                            df.setGroupingSize(3);
                            df.setMaximumFractionDigits(2);
                            result = df.format(value);
                            // выводим результат (если оч. длинный, то на новой строке)
                            if (value.compareTo(new BigInteger("1000000")) == -1) {
                                resultTextArea.setText("Результат: " + result);
                            } else {
                                resultTextArea.setText("Реультат: " + "\n"+result);
                            }
                        // в случае ошибки выводим сообщение о ней
                        } catch (Exception ex) {
                            resultTextArea.setText(ex.getMessage());
                        }

                    }
                }));
                // запускаем поток
                t.start();


            }
        }

    }
}
