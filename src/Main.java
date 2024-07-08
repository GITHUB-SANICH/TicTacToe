import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Запускаем игру ... ");
        JFrame window = new JFrame("TicTacToe"); //главное окно
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //добавляем кнопку X, закрывающее окно
        window.setSize(400,400); //размер окна
        window.setLayout(new BorderLayout()); //менедже компановки
        window.setLocationRelativeTo(null); //чтобы окно было по-центру экрана
        window.setVisible(true); //включаем видимость окна

        TicTacToe game = new TicTacToe(); //создаем объект нашего класса
        window.add(game);
        System.out.println("конец ... ");
    }
}