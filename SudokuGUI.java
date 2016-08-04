import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.Font;

public class SudokuGUI extends JFrame implements ActionListener{
   
   private final int WINDOW_WIDTH=500;
   private final int WINDOW_HEIGHT=600;
   private final int NUM_BOXES=81;
   private final int TOTAL_GAME=3;
   private final int MAX_HINTS=3; 
   private int cols=0;
   private int rows=0;
   private SudokuGameModel dealGame;
   
   private TextArea[] boxes = new TextArea[NUM_BOXES];
   private TextArea gamesWonCount, messageArea;
   private JButton reset = new JButton();
   private JButton checkGame = new JButton();
   private JButton hint = new JButton();
   private String filler=" ";
   private JLabel banner;

   private int[] grayBoxes = {4,5,6,13,14,15,22,23,24,28,29,30,37,38,39,46,47,48,
   34,35,36,43,44,45,52,53,54,58,59,60,67,68,69,76,77,78};
   private String[] userAnswer = new String[81];
   private int[] userAnswerInt = new int[81];
   private int currentGame = 1; //starting game is 1
   private int gamesWon = 0; //initial # of games won
   private int hintCount = 0; //initial # of hints
   
   public SudokuGUI(){
      dealGame = new SudokuGameModel();
   
      setTitle("Sudoku Game");
      setResizable(false);
      setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      banner = new JLabel("*MAKE SURE YOUR NUMBERS DON'T HAVE SPACES IN FRONT OF OR BEHIND THEM.");
      setLayout(new BorderLayout(10,10));
      add(banner, BorderLayout.NORTH);
      add(new JLabel(filler), BorderLayout.SOUTH);
      add(new JLabel(filler), BorderLayout.WEST);
      add(new JLabel(filler), BorderLayout.EAST);
      
      Panel boxesPan = new Panel ();
      boxesPan.setLayout(new GridLayout(9,9,2,2));
      boxesPan.setBackground(Color.black);

      Font textFont = new Font("Courier",Font.BOLD,25);
      
      for(int i=0;i<NUM_BOXES;i++){
         boxes[i] = new TextArea("", 2,2,TextArea.SCROLLBARS_NONE);
         boxesPan.add(boxes[i]);
         boxes[i].setFont(textFont);     
      }//for  
      setBoard();

      for(int b=0;b<grayBoxes.length;b++){
         int grayIndex = grayBoxes[b]-1;
         boxes[grayIndex].setBackground(Color.LIGHT_GRAY);
      }//to make boxes differently colored
      
      add(boxesPan, BorderLayout.CENTER);
            
      Panel bottomPan = new Panel ();
      bottomPan.setLayout(new GridLayout(2,3,4,4));
      bottomPan.setBackground(new Color(128,128,128));
      
      JLabel gameLabel = new JLabel("Games Won", SwingConstants.RIGHT);
      gamesWonCount = new TextArea("0",1,1,TextArea.SCROLLBARS_NONE);
      gamesWonCount.setEditable(false);
      gamesWonCount.setBackground(Color.gray);
      JButton generate = new JButton("New Game");
      generate.setBackground(Color.white);
      JButton check = new JButton("Check");  
      check.setBackground(Color.white);
      JButton reset = new JButton("Clear");
      reset.setBackground(Color.white);
      JButton hint = new JButton("Hint");
      hint.setBackground(new Color(140,207,127));
      
      generate.addActionListener(this);
      check.addActionListener(this);
      reset.addActionListener(this);
      hint.addActionListener(this);
          
      bottomPan.add(gameLabel);
      bottomPan.add(gamesWonCount);
      bottomPan.add(hint);
      bottomPan.add(generate);
      bottomPan.add(check);
      bottomPan.add(reset);
      
      add(bottomPan, BorderLayout.SOUTH);      
      setVisible(true);
      
   }//public sudoku
   
   public void updateGamesWon(){
      String s = gamesWon+"";
      gamesWonCount.setText(s);
   }
   
   public void setBoard(){
      for(int i=0;i<NUM_BOXES;i++){
         String[] game = dealGame.getGame(currentGame);
         boxes[i].setText(game[i]);
            if(!game[i].equals("")){
               boxes[i].setEditable(false);
            }//if
            else{
               boxes[i].setEditable(true);
               boxes[i].setText("" + '\u0000');
            }  
      }//for 
   }//set board
      
   public void actionPerformed(ActionEvent click){
   
      String command = click.getActionCommand();      
      if(command.equals("New Game") && currentGame<TOTAL_GAME){
         currentGame++;
         setBoard(); 
         hintCount=0;
      }//if
      else if(command.equals("Check")){
         for(int i=0;i<NUM_BOXES;i++){
            userAnswer[i]=boxes[i].getText();
         }//for
         boolean win=dealGame.compareArray(userAnswer,dealGame.getSolution(currentGame));
         if(win==false){
            JOptionPane.showMessageDialog(null, "You lost!");
         } else{
            gamesWon++;
            updateGamesWon();
            String gameOver=dealGame.reportWinner();
            JOptionPane.showMessageDialog(null, gameOver);
         }
      }//if check
      else if(command.equals("Clear")){
         setBoard();
         hintCount=0;    
      }//clear board
      else if(command.equals("Hint")){
         if(hintCount<3){
            int h=(int)(Math.random()*81);
            if((boxes[h].getText()).equals("")){
               String hint=dealGame.getSolution(currentGame)[h];
               boxes[h].setText(hint);
               hintCount++;
            }
            System.out.println(boxes[h]);
         }
         else{
            JOptionPane.showMessageDialog(null, "Sorry! Your 3 hints are up! ");
         }//if 3 hints
      }//if hint
      
   }//action performed

   
}//class