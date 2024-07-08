import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TicTacToe extends JComponent {
    public static final int FIELD_EMPTY = 0; //пустое поле
    public static final int FIELD_X = 10; //поле с крестиком
    public static final int FIELD_O = 200; //поле с ноликом
    int [][] field = new int[3][3];
    boolean isXturn;
    public  TicTacToe(){
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        field = new int[3][3];//выделяем память под массив при создании компанента
        initGame();
    }

    public void initGame(){
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                field[i][j] = FIELD_EMPTY;//очищение массива, заполняя его 0
            }
        }
        isXturn = true;
    }

//    обработка нажатия мыши и передача хода
    @Override
    protected void processMouseEvent(MouseEvent mouseEvent){
        super.processMouseEvent(mouseEvent);
        if (mouseEvent.getButton() == MouseEvent.BUTTON1){//проверим, что нажата клавиша
            int x = mouseEvent.getX(); //координата X клика
            int y = mouseEvent.getY(); //координат у клика
            //переводим координаты в индексы ячейки в массиве field
            int i = (int) ((float) x/getWidth() * 3);
            int j = (int) ((float) y/getHeight() * 3);
            //проверим, что выбранная ячейка пуста и туда можно сходить
            if (field[i][j] == FIELD_EMPTY){
                //проверка чей ход, если X - ставит крестик, если 0 - ставим нолик
                field[i][j] = isXturn ? FIELD_O : FIELD_X;
                isXturn = !isXturn; //меняем флаг хода
                repaint(); //прорисовка контента, это вызовет метод paintComponent()
                int res = checkState();
                if (res!=0){
                    if (res == FIELD_O * 3){
                        //победа 0
                        JOptionPane.showMessageDialog(this, "Нолики выйграли!", "Победа!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (res ==FIELD_X * 3) {
                        //поьеда X
                        JOptionPane.showMessageDialog(this, "Крестики выйграли!", "Победа!", JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog(this, "Ничья!", "Ничья!", JOptionPane.INFORMATION_MESSAGE);
                    }
                    //перезапускаем игру
                    initGame();
                    //переписываем поле
                    repaint();
                }
            }
        }
    }

//    отрисовка компанентов игры
    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        //очищаем холст
        graphics.clearRect(0,0,getWidth(),getHeight());
        //рисуем сетку из линий
        drawGrid(graphics);
        //рисуем текущие крестики и нолики
        drawXO(graphics);
    }

//    орисовка линий
    void drawGrid(Graphics graphics){
        int w = getWidth();//ширина игрового поля
        int h = getHeight();//высота игрового поля
        int dw = w/3;//делим ширину на 3 == ширина ячейки
        int dh = h/3;//делим высоту на 3 == высота ячейки
        graphics.setColor(Color.BLUE);//цвет линии
        for (int i = 1; i < 3; i++){
            graphics.drawLine(0, dh * i, w, dh * i);//горизонтальная линия
            graphics.drawLine(dw * i, 0, dw * i, h);//вертикальная линия
        }
    }

    //    отрисовка крестика
    void drawX(int i, int j, Graphics graphics){
        graphics.setColor(Color.BLACK);
        int dw = getWidth()/3;
        int dh = getHeight()/3;
        int x = i*dw;
        int y = j*dh;
        //линия от верхнего левого угла в правый нижний
        graphics.drawLine(x, y, x + dw, y + dh);
        //линия от левого нижнего угла до правого верхнего
        graphics.drawLine(x, y + dh, x+ dw, y);
    }

//    отрисовка нуля
    void  drawO(int i, int j, Graphics graphics){
        graphics.setColor(Color.BLACK);
        int dw = getWidth()/3;
        int dh = getHeight()/3;
        int x = i*dw;
        int y = j*dh;
//        магия с непонятным умножением и делением для того, чтобы нолик был чуть вытянут по вертикали и не касался боковых "стенок" ячейки
        graphics.drawOval(x + 5 * dw / 100, y, dw * 9 / 10, dh);
    }

//    метод проверки массива на соответствие с компанентом
    void  drawXO(Graphics graphics){
        //вложенные циклы
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                //если в данной ячейке крестик - рисуем его
                if (field[i][j] == FIELD_X){
                    drawX(i,j, graphics);
                    //тоже для нолика
                }else if (field[i][j] == FIELD_O){
                    drawO(i,j,graphics);
                }
            }
        }
    }

//    проверка состояния игры
    int checkState(){
        int diag = 0;
        int diag2 = 0;
        for (int i = 0; i < 3; i++){
            //сумма значений по диагонали от левого угла
            diag += field[i][i];
            //сумма значений по диагонали от правого угла
            diag2 += field[i][2-i];
        }
        //если по диагонали стоят одни крестили или одни нолики выходим из метода
        if(diag == FIELD_O * 3 || diag == FIELD_X * 3){return diag;}
        //то же самое для второй диагонали
        if(diag2 == FIELD_O * 3 || diag2 == FIELD_X * 3){return diag2;}
        int check_i, check_j;
        boolean hasEmpty = false;
        //будем бегать по всем рядам
        for (int i = 0; i < 3; i++){
            check_i = 0;
            check_j = 0;
            for (int j = 0; j < 3; j++){
                //суммируем знаки в текущем ряду
                if (field[i][j] == 0){
                    hasEmpty = true;
                }
                check_i += field[i][j];
                check_j += field[j][i];
            }
//            если выйгрыш крестика или нолика, то выводим
            if (check_i == FIELD_O*3 || check_i == FIELD_X*3){
                return check_i;
            }
            if (check_j == FIELD_O*3 || check_j == FIELD_X*3){
                return check_j;
            }
        }
        if (hasEmpty) return 0; else return -1;
    }
}
