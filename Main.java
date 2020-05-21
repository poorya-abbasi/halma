import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
public class Main {

    //Default Settings
    public static String players[];
    public static int currentPlayerIndex=0;
    public static int playerCount=2;
    public static int marbleCount=4;
    public static int dimen=8;
    //Colors
    public static Color color1;
    public static Color color2;
    //Playground Variables
    public static JButton playground[];
    public static boolean isSelected=false;
    public static int selectedIndex=0;
    public static JLabel playerTurnLabel;
    public static JButton endTurnButton;
    public static boolean didHop=false;
    public static boolean simpleMove=false;
    public static boolean didMove=false;
    public static JFrame gameFrame;
    //Setting Up Icons
    public static Icon marble1=new ImageIcon("marble1.png");
    public static Icon marble2=new ImageIcon("marble2.png");
    public static Icon marble3=new ImageIcon("marble3.png");
    public static Icon marble4=new ImageIcon("marble4.png");

    public static void main(String[] args)
    {    
        color1=Color.decode("#E8E6AE");
        color2=Color.decode("#60463B");
        // drawStartMenu();
        // String[] p={"Player 1","Player 2","Player 3","Player 4"};
        String[] p={"Player 1","Player 2"};
        startGame(p,8,4);
    }

    private static void drawStartMenu(){
        //Setting Up Window
        JFrame frame=new JFrame();
        frame.setSize(500,500);
        frame.setUndecorated(true);
        //Setting Up Welcome Label
        JLabel welcomeLable=new JLabel("    Welcome To Halma    ");
        welcomeLable.setHorizontalAlignment(JLabel.CENTER);
        welcomeLable.setForeground(Color.WHITE);
        //Setting Up Button Box
        Box box=Box.createVerticalBox();    
        //Setting Up playground
        JButton playButton=new JButton("Play!");
        playButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            drawPlayMenu(frame);
          }
        });
        JButton exitButton=new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(40,40));
        exitButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            exit();
          }
        });
        //Adding Components To The Box
        box.add(Box.createVerticalStrut(10));
        box.add(welcomeLable);
        box.add(Box.createVerticalStrut(10));
        box.add(playButton);
        box.add(Box.createVerticalStrut(10));
        box.add(exitButton);
        welcomeLable.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        //Adding Components To Frame
        frame.add(box);
        //Setting Window Visible
        frame.pack();
        processFrame(frame);
        frame.setVisible(true);
    }

    private static void drawPlayMenu(JFrame lastFrame){
        lastFrame.setVisible(false);
        JFrame frame=new JFrame();
        frame.setSize(500,500);
        frame.setUndecorated(true);
        Box box=Box.createVerticalBox();    
        players=new String[2];
        JTextField playerFields[]=addPlayerField(frame, box, playerCount);
        JTextField dimen=addTextField(frame, box, "Dimension ( Must Be Even )");
        JTextField marbleCount=addTextField(frame, box, "Marble Rows Count");
        JButton submitButton=new JButton("Start");
        submitButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            String dim="",mcount="";
            for(int i=0;i<playerCount;i++){
                players[i]=playerFields[i].getText();
            }
            dim=dimen.getText();
            mcount=marbleCount.getText();
            if(players.length>0 && dim!="" && mcount!=""){
                int c=Integer.valueOf(mcount);
                int d=Integer.valueOf(dim);
                if(d%2==0 && c>=1 && c<d){
                    frame.dispose();
                    startGame(players,d,c);
                }else{
                    JOptionPane.showMessageDialog(null, "The Values Are Incorrect");
                }
            }
          }
        });
        JButton addPlayerButton=new JButton("Add Player");
        JButton exitButton=new JButton("Exit");
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                exit();
            }
        });
        submitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        addPlayerButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        box.add(submitButton);
        box.add(Box.createVerticalStrut(10));     
        box.add(addPlayerButton);
        box.add(Box.createVerticalStrut(10));     
        box.add(exitButton);
        box.add(Box.createVerticalStrut(10));     
        frame.add(box);
        frame.pack();
        processFrame(frame);
        frame.setVisible(true);
    }

    private static JTextField[] addPlayerField(JFrame frame,Box box,int count){
        JTextField pfs[]=new JTextField[count];
        for(int i=0;i<count;i++){
            JTextField playerNameTxt=addTextField(frame, box, "Enter Player "+(i+1)+"'s' Name");
            pfs[i]=playerNameTxt;
        }
        return pfs;
    }

    private static JTextField addTextField(JFrame frame,Box box,String label){
        JLabel playerNameLabel=new JLabel(label);
        playerNameLabel.setForeground(Color.WHITE);
        JTextField playerNameTxt=new JTextField(16);
        playerNameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playerNameTxt.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        Box marginedBox=Box.createHorizontalBox();
        marginedBox.add(Box.createHorizontalStrut(10));
        marginedBox.add(playerNameTxt);
        marginedBox.add(Box.createHorizontalStrut(10));
        box.add(Box.createVerticalStrut(10));
        box.add(playerNameLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(marginedBox);
        box.add(Box.createVerticalStrut(10));    
        return playerNameTxt;
    }

    private static void startGame(String p[],int d,int count){
        players=p;
        JFrame frame=new JFrame();
        gameFrame=frame;
        frame.setSize(500,500);
        frame.setUndecorated(true);
        dimen=d;
        frame.setLayout(new GridLayout(dimen,dimen));
        playground=new JButton[dimen*dimen];
        for(int i=0;i<dimen;i++){
            for(int j=0;j<dimen;j++){
                int index=i*dimen+j;
                playground[index]=new JButton();
                playground[index].setOpaque(true);
                playground[index].setBorder(new LineBorder(Color.RED,3));
                playground[index].setBorderPainted(false);
                playground[index].addActionListener(new ActionListener()
                {
                public void actionPerformed(ActionEvent e){
                    simpleMove=false;
                    if(isSelected){
                        if(isMoveValid(selectedIndex,index)){
                            moveMarble(selectedIndex,index);
                        }else{
                            if(!didMove){
                                clearSelection();
                                didMove=false;
                            }
                        }
                    }else{
                        highlightSpace(index);
                    }
                }
                });   
                if(i%2==0){
                    if(j%2==0){
                        playground[index].setBackground(color1);
                        playground[index].setForeground(color1);
                    }else{
                        playground[index].setBackground(color2);
                        playground[index].setForeground(color2);
                    }
                }else{
                    if(j%2==1){
                        playground[index].setBackground(color1);
                        playground[index].setForeground(color1);
                    }else{
                        playground[index].setBackground(color2);
                        playground[index].setForeground(color2);
                    }
                }
                frame.add(playground[i*dimen+j]);
            }
        }
        marbleCount=count;
        drawMarbles(playground,marbleCount,dimen);
        processFrame(frame);
        showControlFrame(frame);
        resetVars();
        frame.setVisible(true);
    }

    private static void nextPlayer(){
        didHop=false;didMove=false;
        clearSelection();
        if(checkForWinner()>0){
            drawPlayMenu(gameFrame);
            return;
        }
        if(currentPlayerIndex<players.length-1){
            currentPlayerIndex++;
        }else{  
            currentPlayerIndex=0;
        }
        playerTurnLabel.setText(players[currentPlayerIndex]+"'s Turn");
    }

    private static int checkForWinner(){ 
        //Filling The Board
        int count=marbleCount;
        boolean check1=true,check2=true,check3=true,check4=true;
        for(int n=0;n<count;n++){
            for(int i=0,j=n;i<=n && j>=0;i++,j--){
                //Upper Left
                if(playground[i*dimen+j].getIcon()!=marble2){
                    check2=false;
                }
                //Lower Right
                if(playground[((dimen-i-1)*dimen+(dimen-j-1))].getIcon()!=marble1){
                    check1=false;
                }
                if(players.length>2){
                    //Lower Left
                    if(playground[(dimen-i)*dimen-(dimen-j)].getIcon()!=marble4){
                        check3=false;
                    }
                    //Upper Right
                    if(playground[(i*dimen+(dimen-j-1))].getIcon()!=marble3){
                        check4=false;
                    }
                }
            }
        }
        if(check1){
            JOptionPane.showMessageDialog(null, players[0]+" Won");
            return 1;
        }else if(check2){
            JOptionPane.showMessageDialog(null, players[1]+" Won");
            return 2;
        }
        // else if(check3){
        //     JOptionPane.showMessageDialog(null, players[2]+" Won");
        //     return 3;
        // }else if(check4){
        //     JOptionPane.showMessageDialog(null, players[3]+" Won");
        //     return 4;
        // }
        return 0;
    }

    private static void showControlFrame(JFrame gameFrame){
        JFrame frame=new JFrame();
        frame.setUndecorated(true);
        //Setting Up Welcome Label
        JLabel turnLabel=new JLabel(players[0]+"'s Turn");
        turnLabel.setHorizontalAlignment(JLabel.CENTER);
        turnLabel.setForeground(Color.WHITE);
        playerTurnLabel=turnLabel;
        //Setting Up Button Box
        Box box=Box.createVerticalBox();    
        //Setting Up playground
        endTurnButton=new JButton("End Turn");
        endTurnButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                endTurnButton.setEnabled(false);
                nextPlayer();
            }
        });
        JButton playButton=new JButton("Restart Game");
        playButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                gameFrame.dispose();
                frame.dispose();
                startGame(players, dimen, marbleCount);
            }
        });
        JButton exitButton=new JButton("Main Menu");
        exitButton.setPreferredSize(new Dimension(40,40));
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                gameFrame.dispose();
                frame.dispose();
                drawPlayMenu(frame);
            }
        });
        //Adding Components To The Box
        box.add(Box.createVerticalStrut(10));
        box.add(turnLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(endTurnButton);
        box.add(Box.createVerticalStrut(10));
        box.add(playButton);
        box.add(Box.createVerticalStrut(10));
        box.add(exitButton);
        turnLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        endTurnButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        //Adding Components To Frame
        frame.add(box);
        //Setting Window Visible
        frame.pack();
        processFrame(frame);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2+gameFrame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }

    private static void moveMarble(int from,int to){
        if(didHop && simpleMove){
            if(!didMove)
                clearSelection();
            return;
        }
        playground[to].setIcon(playground[from].getIcon());
        playground[from].setIcon(null);
        didMove=true;
        if(!isMarbleNearby(to) || simpleMove){
            nextPlayer();
            endTurnButton.setEnabled(false);
        }else{
            highlightSpace(to);
            endTurnButton.setEnabled(true);
        }
    }

    private static boolean isMoveValid(int from,int to){
        return from!=to && isSpaceEmpty(to) && isMoveInRange(from,to,true);
    }

    private static boolean isMarbleNearby(int index){
        int j=index/dimen;
        int i=index%dimen;
        if((i+2<dimen && !isSpaceEmpty(index+1)) || (i-2>=0 && !isSpaceEmpty(index-1))){
            System.out.println("Marble Found Linear");
            return true;
        }
        if((j+2<dimen && !isSpaceEmpty(index+dimen)) || (j-2>=0 && !isSpaceEmpty(index-dimen))){
            System.out.println("Marble Found Vertical");
            return true;
        }
        if(i+2<dimen && j+2<dimen  && !isSpaceEmpty(index+dimen+1)){
            System.out.println("Marble Found SE");
            return true;
        }
        if(i-2>=0 && j-2>=0 && !isSpaceEmpty(index-dimen-1)){
            System.out.println("Marble Found NW");
            return true;
        }
        if(i-2>=0 && j+2<dimen && !isSpaceEmpty(index-dimen-1)){
            System.out.println("Marble Found SW");
            return true;
        }
        if(i+2<dimen && j-2>=0 && !isSpaceEmpty(index-dimen+1)){
            System.out.println("Marble Found NE");
            return true;
        }
        return false;
    }

    private static boolean isMoveInRange(int from,int to,boolean simple){
        int iFrom=from/dimen;
        int jFrom=from%dimen;
        int iTo=to/dimen;
        int jTo=to%dimen;
        int iSum=iTo-iFrom;
        int jSum=jTo-jFrom;
        int distance=(int)Math.sqrt((iSum*iSum)+(jSum*jSum));
        if(distance<=1){
            if(simple){
                simpleMove=true;
            }
            return true;
        }
        if(iFrom==iTo){
            //Movement is Linear 
            if(jFrom+1<dimen && jFrom-jTo==-2){
                if(!isSpaceEmpty(from+1) && isMoveInRange(from+1,to,false)){
                    didHop=true;
                    return true;
                }
            }
            if(jFrom-1>=0 && jFrom - jTo==2){
                if(!isSpaceEmpty(from-1) && isMoveInRange(from-1,to,false)){
                    didHop=true;
                    return true;
                }
            }
        }else if(jFrom == jTo){
            //Movement is Vertical 
            if(iFrom<dimen && iFrom-iTo==-2){
                if(!isSpaceEmpty(from+dimen) && isMoveInRange(from+dimen,to,false)){
                    didHop=true;
                    return true;
                }
            }
            if(iFrom-1>=0 && iFrom - iTo==2){
                if(!isSpaceEmpty(from-dimen) && isMoveInRange(from-dimen,to,false)){
                    didHop=true;
                    return true;
                }
            }
        }else {
            if(jTo-jFrom==2 && iTo-iFrom==-2){
                //Diagonal North East
                if(!isSpaceEmpty(from-dimen+1) && isMoveInRange(from-dimen+1,to,false)){
                    didHop=true;
                    return true;
                }
            }else if(jTo-jFrom==2 && iTo-iFrom==2){
                //Diagonal South East
                if(!isSpaceEmpty(from+dimen+1) && isMoveInRange(from+dimen+1,to,false)){
                    didHop=true;
                    return true;
                }
            }else if(jTo-jFrom==-2 && iTo-iFrom==-2){
                //Diagonal North West
                if(!isSpaceEmpty(from-dimen-1) && isMoveInRange(from-dimen-1,to,false)){
                    didHop=true;
                    return true;
                }
            }else if(jTo-jFrom==-2 && iTo-iFrom==2){
                //Diagonal South West
                if(!isSpaceEmpty(from+dimen-1) && isMoveInRange(from+dimen-1,to,false)){
                    didHop=true;
                    return true;
                }
            }
        }
        // didHop=false;
        return false;
    }

    private static boolean isSpaceEmpty(int index){
        if(playground[index].getIcon()==null){
            return true;
        }else{
            return false;
        }
    }

    private static void highlightSpace(int index){   
        clearSelection();
        if(!playground[index].isBorderPainted() && !isSpaceEmpty(index)){
            if((currentPlayerIndex==0 && playground[index].getIcon()==marble1) || (currentPlayerIndex==1 && playground[index].getIcon()==marble2) || (currentPlayerIndex==2 && playground[index].getIcon()==marble3) || (currentPlayerIndex==3 && playground[index].getIcon()==marble4)){
                playground[index].setBorderPainted(true);
                isSelected=true;
                selectedIndex=index;
            }     
        }
    }

    private static void clearSelection(){
        for(int i=0;i<playground.length;i++){
            playground[i].setBorderPainted(false);
            isSelected=false;
        }
    }

    private static void drawMarbles(JButton[] playground,int count,int dimen){
        //Filling The Board
        for(int n=0;n<count;n++){
            for(int i=0,j=n;i<=n && j>=0;i++,j--){
                //Upper Left
                playground[i*dimen+j].setIcon(marble1);
                //Lower Right
                playground[((dimen-i-1)*dimen+(dimen-j-1))].setIcon(marble2);
                if(players.length>2){
                    //Lower Left
                    playground[(dimen-i)*dimen-(dimen-j)].setIcon(marble3);
                    //Upper Right
                    playground[(i*dimen+(dimen-j-1))].setIcon(marble4);
                }
            }
        }
    }

    private static void exit(){
        System.exit(0);
    }

    private static void processFrame(JFrame frame){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
    }

    private static void resetVars(){
        isSelected=false;
        selectedIndex=0;
        endTurnButton.setEnabled(false);
        didHop=false;
        simpleMove=false;
    }
 }