import java.awt.*; 
import javax.swing.*; 
import java.awt.geom.*; 
import java.awt.event.*;

public class Pong extends JPanel implements KeyListener, ActionListener, MouseListener{ 
    
    private static final int WINDOW_WIDTH = 1010, WINDOW_HEIGHT = 735;
    
    private Rectangle2D.Double leftPaddle, rightPaddle; 
    private final int PADDLE_WIDTH = 20;
    private final int PADDLE_HEIGHT = 200;
    private final Color LEFT_PADDLE_COLOR = Color.RED;
    private final Color RIGHT_PADDLE_COLOR = Color.BLUE;
    private int paddleVel = 10;

    private Ellipse2D.Double ball; 
    private final int BALL_RADIUS = 20;
    private final Color BALL_COLOR = Color.GREEN;
    private int ballVelX = 3, ballVelY = 3;
    
    private final Color BACKGROUND = Color.BLACK;    
    private Line2D.Double sideSeparator;
    private Ellipse2D.Double playAgain, quit; 
    
    private String player1Name, player2Name; 
    private int player1Score = 0, player2Score = 0; 
    private final int WINNING_SCORE = 5;
    private boolean isGameOver;
    
    private Timer timer = new Timer(5,this);   
    
    public Pong()
    { 
        setBackground(Color.BLACK); 
        addKeyListener(this); 
        setFocusable(true); 
        addMouseListener(this); 
        
        introMsgs();
        initComponents();
        isGameOver = false;
          
        timer.start(); 
    }

    public void introMsgs()
    {
        JOptionPane.showMessageDialog(null, "First to 5 points wins!"); 

        do{ 
            player1Name = JOptionPane.showInputDialog("Player 1, Enter Your Name:"); 
        }while(player1Name == null || player1Name.equals("") ); 

        do{ 
            player2Name = JOptionPane.showInputDialog("Player 2, Enter Your Name:"); 
        }while(player2Name == null || player2Name.equals("") ); 
    }

    public void initComponents()
    {
        leftPaddle = new Rectangle2D.Double(50, 250, PADDLE_WIDTH, PADDLE_HEIGHT); 
        rightPaddle = new Rectangle2D.Double(950, 250, PADDLE_WIDTH, PADDLE_HEIGHT); 
        
        ball = new Ellipse2D.Double(500,350, BALL_RADIUS, BALL_RADIUS); 
        sideSeparator = new Line2D.Double(WINDOW_WIDTH/2, 0, WINDOW_WIDTH/2, WINDOW_HEIGHT);
        
        playAgain = new Ellipse2D.Double(150,400,300,200); 
        quit = new Ellipse2D.Double(600,400,300,200); 
    }

    public void paintComponent(Graphics g)
    { 
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g; 
          
        g2.setColor(Color.WHITE); 
        g2.setStroke(new BasicStroke(3)); 
        g2.draw(sideSeparator); 
          
        g2.setFont(new Font("Serif", Font.PLAIN, 78)); 
        g2.drawString("" + player1Score, 300, 330); 
        g2.drawString("" + player2Score, 700, 330); 
          
        g2.setColor(LEFT_PADDLE_COLOR); 
        g2.fill(leftPaddle); 
          
        g2.setColor(RIGHT_PADDLE_COLOR); 
        g2.fill(rightPaddle); 
          
        g2.setColor(BALL_COLOR); 
        g2.fill(ball); 
          
        if(player1Score == WINNING_SCORE)
        { 
            timer.stop(); 
            isGameOver = true;
            
            g2.setColor(BACKGROUND);
            g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT); 
              
            g2.setColor(LEFT_PADDLE_COLOR); 
            g2.drawString(player1Name + " wins!!!!!" ,250,300); 
            g2.fill(playAgain); 
            g2.fill(quit); 
              
            g2.setColor(BACKGROUND); 
            g2.setFont(new Font("serif", Font.PLAIN, 34)); 
            g2.drawString("PLAY AGAIN",200,500); 
            g2.drawString("QUIT", 700, 500); 
          
        } 
        else if(player2Score == WINNING_SCORE)
        { 
            timer.stop(); 
            isGameOver = true;

            g2.setColor(BACKGROUND); 
            g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT); 
              
            g2.setColor(RIGHT_PADDLE_COLOR); 
            g2.drawString(player2Name + " wins!!!!!" ,250,300); 
            g2.fill(playAgain); 
            g2.fill(quit); 
              
            g2.setColor(BACKGROUND); 
            g2.setFont(new Font("serif", Font.PLAIN, 34) ); 
            g2.drawString("PLAY AGAIN",200,500); 
            g2.drawString("QUIT", 700, 500); 
        } 
        
    } 

    public void restartGame()
    {
        player1Score = 0; 
        player2Score = 0; 
        isGameOver = false; 
        timer.restart();
    }

    public int ranPosition()
    {
        return 10 + (int)(Math.random()*WINDOW_HEIGHT-10); 
    } 

    public void actionPerformed(ActionEvent event)
    { 
        if(ball.x < 0 || ball.x > WINDOW_WIDTH-BALL_RADIUS) 
            ballVelX = -ballVelX; 
        if(ball.y < 0 ||  ball.y > WINDOW_HEIGHT-(BALL_RADIUS*2))
            ballVelY = -ballVelY; 
          
        ball.y += ballVelY; 
        ball.x += ballVelX; 
        
        if(ball.intersects(leftPaddle)  ) 
            ballVelX = -ballVelX; 
        else if(ball.intersects(rightPaddle) )
            ballVelX = -ballVelX;

        if(ball.x < 0 || ball.x > WINDOW_WIDTH-PADDLE_WIDTH)
        {
            if(ball.x < 0)
                player2Score++;
            else
                player1Score++;

            ball.x = WINDOW_WIDTH/2;
            ball.y = ranPosition(); 
            ballVelX = -ballVelX;
        }
        repaint(); 
    } 

    private boolean upKeyPressed = false;
    private boolean downKeyPressed = false;
    private boolean wKeyPressed = false;
    private boolean sKeyPressed = false;

    public void keyPressed(KeyEvent event)
    { 
        if(event.getKeyCode() == KeyEvent.VK_UP)
        { 
            upKeyPressed = true;

            if(rightPaddle.y > 0)
                rightPaddle.y -= paddleVel;

            if(wKeyPressed && leftPaddle.y > 0)
                leftPaddle.y -= paddleVel;
            else if(sKeyPressed && leftPaddle.y < WINDOW_HEIGHT-PADDLE_HEIGHT-35)
                leftPaddle.y += paddleVel;
        } 
        else if(event.getKeyCode() == KeyEvent.VK_DOWN)
        { 
            downKeyPressed = true;

            if(rightPaddle.y < WINDOW_HEIGHT-PADDLE_HEIGHT-35)
                rightPaddle.y += paddleVel;

            if(wKeyPressed && leftPaddle.y > 0)
                leftPaddle.y -= paddleVel;
            else if(sKeyPressed && leftPaddle.y < WINDOW_HEIGHT-PADDLE_HEIGHT-35)
                leftPaddle.y += paddleVel;
        } 

        if(event.getKeyCode() == KeyEvent.VK_W)
        { 
            wKeyPressed = true;

            if(leftPaddle.y > 0)
                leftPaddle.y -= paddleVel; 

            if(upKeyPressed && rightPaddle.y > 0)
                rightPaddle.y -= paddleVel;
            else if(downKeyPressed && rightPaddle.y < WINDOW_HEIGHT-PADDLE_HEIGHT-35)
                rightPaddle.y += paddleVel;
        } 
        else if(event.getKeyCode() == KeyEvent.VK_S)
        { 
            sKeyPressed = true;

            if(leftPaddle.y < WINDOW_HEIGHT-PADDLE_HEIGHT-35 )
                leftPaddle.y += paddleVel; 

            if(upKeyPressed && rightPaddle.y > 0)
                rightPaddle.y -= paddleVel;
            else if(downKeyPressed && rightPaddle.y < WINDOW_HEIGHT-PADDLE_HEIGHT-35)
                rightPaddle.y += paddleVel;
        } 
    }

    public void keyReleased(KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.VK_UP)
            upKeyPressed = false;
        else if(event.getKeyCode() == KeyEvent.VK_DOWN)
            downKeyPressed = false;
        
        if(event.getKeyCode() == KeyEvent.VK_W)
            wKeyPressed = false;
        else if(event.getKeyCode() == KeyEvent.VK_S)
            sKeyPressed = false;
    }    
    
    public void mouseClicked(MouseEvent event)
    { 
        if(isGameOver && playAgain.contains(event.getPoint() ) )
            restartGame();
        else if(isGameOver && quit.contains(event.getPoint() )) 
            System.exit(0); 
    }

    public void mouseEntered(MouseEvent event){} 
    public void mouseExited(MouseEvent event){} 
    public void mousePressed(MouseEvent event){} 
    public void mouseReleased(MouseEvent event){} 
    public void keyTyped(KeyEvent event){} 
        
    public static void main(String[] args)
    { 
        JFrame win = new JFrame("Pong"); 
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        win.add(new Pong() ); 
        win.setVisible(true); 
        win.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); 
        win.setResizable(false);
        win.setLocationRelativeTo(null); 
    } 
} 