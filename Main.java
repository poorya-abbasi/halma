import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public static ArrayList<Record> records=new ArrayList<Record>();
    public static int moves[]={0,0,0,0};
    public static JButton playground[];
    public static boolean isSelected=false;
    public static int selectedIndex=0;
    public static JLabel playerTurnLabel;
    public static JButton endTurnButton;
    public static boolean didHop=false;
    public static boolean simpleMove=false;
    public static boolean didMove=false;
    public static JFrame gameFrame;
    public static JFrame controlFrame;
    public static JButton indicatorIcon;
    
    //Setting Up Icons
    public static Icon marble1=new ImageIcon("marble1.png");
    public static Icon marble2=new ImageIcon("marble2.png");
    public static Icon marble3=new ImageIcon("marble3.png");
    public static Icon marble4=new ImageIcon("marble4.png");

    public static void main(final String[] args)
    {    
        color1=Color.decode("#E8E6AE");
        color2=Color.decode("#60463B");
        moves[0]=80;
        loadData();
        sortRecords();
        drawStartMenu();
        // String[] p={"Player 1","Player 2","Player 3","Player 4"};
        // final String[] p={"Player 1","Player 2"};
        // startGame(p,8,4);
    }

    private static void drawStartMenu(){
        //Setting Up Window
        final JFrame frame=new JFrame();
        frame.setSize(500,500);
        frame.setUndecorated(true);
        //Setting Up Welcome Label
        final JLabel welcomeLable=new JLabel("    Welcome To Halma    ");
        welcomeLable.setHorizontalAlignment(JLabel.CENTER);
        welcomeLable.setForeground(Color.WHITE);
        //Setting Up Button Box
        final Box box=Box.createVerticalBox();    
        //Setting Up playground
        final JButton playButton=new JButton("Play!");
        playButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(final ActionEvent e)
          {
            drawPlayMenu(frame,playerCount);
          }
        });
        final JButton recordsButton=new JButton("Leaderboard");
        recordsButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(final ActionEvent e)
          {
              frame.dispose();
              showRecordsWindow();
          }
        });
        final JButton exitButton=new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(40,40));
        exitButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(final ActionEvent e)
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
        box.add(recordsButton);
        box.add(Box.createVerticalStrut(10));
        box.add(exitButton);
        welcomeLable.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        recordsButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        //Adding Components To Frame
        frame.add(box);
        //Setting Window Visible
        frame.pack();
        processFrame(frame);
        frame.setVisible(true);
    }

    private static void drawPlayMenu(final JFrame lastFrame,int count){
        lastFrame.setVisible(false);
        final JFrame frame=new JFrame();
        frame.setSize(500,500);
        frame.setUndecorated(true);
        final Box box=Box.createVerticalBox();    
        players=new String[count];
        final JTextField playerFields[]=addPlayerField(frame, box, count);
        final JTextField dimen=addTextField(frame, box, "Dimension ( Must Be Even )");
        final JTextField marbleCount=addTextField(frame, box, "Marble Rows Count");
        final JButton submitButton=new JButton("Start");
        submitButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(final ActionEvent e)
          {
            String dim="",mcount="";
            for(int i=0;i<count;i++){
                players[i]=playerFields[i].getText();
            }
            dim=dimen.getText();
            mcount=marbleCount.getText();
            if(players.length>0 && dim!="" && mcount!=""){
                final int c=Integer.valueOf(mcount);
                final int d=Integer.valueOf(dim);
                if(d%2==0 && c>=1 && c<d){
                    frame.dispose();
                    startGame(players,d,c);
                }else{
                    JOptionPane.showMessageDialog(null, "The Values Are Incorrect");
                }
            }
          }
        });
        final JButton addPlayerButton;
        if(count==2){
           addPlayerButton =new JButton("4 Players Mode");
            addPlayerButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(final ActionEvent e)
                {
                    playerCount=4;
                    frame.dispose();
                    // gameFrame.dispose();
                    drawPlayMenu(lastFrame, 4);
                }
            });
        }else{
            addPlayerButton =new JButton("2 Players Mode");
            addPlayerButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(final ActionEvent e)
                {
                    playerCount=2;
                    frame.dispose();
                    // gameFrame.dispose();
                    drawPlayMenu(lastFrame, 2);
                }
            });
        }
        final JButton exitButton=new JButton("Back");
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                frame.dispose();
                drawStartMenu();
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

    private static void showRecordsWindow(){
        //Showing The Records
        String data[]=new String[records.size()];
        for(int i=0;i<records.size();i++){
            data[i]=(i+1)+"- "+records.get(i).name+" : "+records.get(i).count;
        }
        JFrame frame=new JFrame();
        frame.setUndecorated(true);
        JList<String> list=new JList<String>(data);
        Box box=Box.createVerticalBox();
        box.add(Box.createVerticalStrut(10));     
        

        final Box marginedBox=Box.createHorizontalBox();
        marginedBox.add(Box.createHorizontalStrut(10)); 
        marginedBox.add(list);  
        marginedBox.add(Box.createHorizontalStrut(10)); 
        box.add(marginedBox);
        box.add(Box.createVerticalStrut(10));
        final JButton exitButton=new JButton("Back");
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                frame.dispose();
                drawStartMenu();
            }
        });
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(exitButton);
        frame.add(box);
        frame.pack();
        processFrame(frame);
        frame.getContentPane().setBackground(Color.ORANGE);
        list.setBackground(Color.ORANGE);
        frame.setVisible(true);
    }

    private static JTextField[] addPlayerField(final JFrame frame,final Box box,final int count){
        final JTextField pfs[]=new JTextField[count];
        for(int i=0;i<count;i++){
            final JTextField playerNameTxt=addTextField(frame, box, "Enter Player "+(i+1)+"'s' Name");
            pfs[i]=playerNameTxt;
        }
        return pfs;
    }

    private static JTextField addTextField(final JFrame frame,final Box box,final String label){
        final JLabel playerNameLabel=new JLabel(label);
        playerNameLabel.setForeground(Color.WHITE);
        final JTextField playerNameTxt=new JTextField(16);
        playerNameLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playerNameTxt.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        final Box marginedBox=Box.createHorizontalBox();
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

    private static void startGame(final String p[],final int d,final int count){
        players=p;
        final JFrame frame=new JFrame();
        gameFrame=frame;
        frame.setSize(500,500);
        frame.setUndecorated(true);
        dimen=d;
        frame.setLayout(new GridLayout(dimen,dimen));
        playground=new JButton[dimen*dimen];
        for(int i=0;i<dimen;i++){
            for(int j=0;j<dimen;j++){
                final int index=i*dimen+j;
                playground[index]=new JButton();
                playground[index].setOpaque(true);
                playground[index].setBorder(new LineBorder(Color.RED,3));
                playground[index].setBorderPainted(false);
                playground[index].addActionListener(new ActionListener()
                {
                public void actionPerformed(final ActionEvent e){
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
        indicatorIcon.setIcon(marble1);
        resetVars();
        frame.setVisible(true);
    }

    private static void nextPlayer(){
        didHop=false;didMove=false;
        clearSelection();
        moves[currentPlayerIndex]++;
        final int winner=checkForWinner();
        if(winner>0){
            saveRecord(winner);
            return;
        }
        if(currentPlayerIndex<players.length-1){
            currentPlayerIndex++;
        }else{  
            currentPlayerIndex=0;
        }
        playerTurnLabel.setText(players[currentPlayerIndex]+"'s Turn");
        switch(currentPlayerIndex){
            case 0:indicatorIcon.setIcon(marble1);
            break;
            case 1:indicatorIcon.setIcon(marble2);
            break;
            case 2:indicatorIcon.setIcon(marble3);
            break;
            case 3:indicatorIcon.setIcon(marble4);
            break;
        }
    }

    private static void saveRecord(final int winner){
        final Record record=new Record();
        record.name=players[winner-1];
        record.count=moves[winner-1];
        records.add(record);
        sortRecords();
        saveData();

    }

    private static int checkForWinner(){ 
        //Filling The Board
        final int count=marbleCount;
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
                        check4=false;
                    }
                    //Upper Right
                    if(playground[(i*dimen+(dimen-j-1))].getIcon()!=marble3){
                        check3=false;
                    }
                }else{
                    check3=false;check4=false;
                }
            }
        }
        int winner=-1;
        if(check1){
            winner=1;
        }else if(check2){
            winner=2;
        }
        else if(check3){
            winner=3;
        }else if(check4){
            winner=4;
        }
        if(check1 || check2 || check3 || check4){
            JOptionPane.showMessageDialog(null, players[winner-1]+" Won");
            gameFrame.dispose();
            controlFrame.dispose();
            drawPlayMenu(gameFrame,playerCount);
            return winner;
        }
        return 0;
    }

    private static void showControlFrame(final JFrame gameFrame){
        final JFrame frame=new JFrame();
        controlFrame=frame;
        frame.setUndecorated(true);
        //Setting Up Welcome Label
        final JLabel turnLabel=new JLabel(players[0]+"'s Turn");
        turnLabel.setHorizontalAlignment(JLabel.CENTER);
        turnLabel.setForeground(Color.WHITE);
        playerTurnLabel=turnLabel;
        //Setting Up Button Box
        final Box box=Box.createVerticalBox();    
        //Setting Up playground
        endTurnButton=new JButton("End Turn");
        endTurnButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                endTurnButton.setEnabled(false);
                nextPlayer();
            }
        });
        final JButton playButton=new JButton("Restart Game");
        playButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                gameFrame.dispose();
                frame.dispose();
                startGame(players, dimen, marbleCount);
            }
        });
        final JButton exitButton=new JButton("Main Menu");
        exitButton.setPreferredSize(new Dimension(40,40));
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                gameFrame.dispose();
                frame.dispose();
                drawPlayMenu(frame,playerCount);
            }
        });
        //Adding Components To The Box
        indicatorIcon=new JButton();
        indicatorIcon.setBackground(Color.GRAY);
        indicatorIcon.setForeground(Color.GRAY);
        box.add(Box.createVerticalStrut(10));
        box.add(turnLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(indicatorIcon);
        box.add(Box.createVerticalStrut(10));
        box.add(endTurnButton);
        box.add(Box.createVerticalStrut(10));
        box.add(playButton);
        box.add(Box.createVerticalStrut(10));
        box.add(exitButton);
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalStrut(10));

        turnLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        endTurnButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        indicatorIcon.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        //Adding Components To Frame
        frame.add(box);
        //Setting Window Visible
        frame.pack();
        processFrame(frame);
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2+gameFrame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }

    private static void moveMarble(final int from,final int to){
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

    private static boolean isMoveValid(final int from,final int to){
        return from!=to && isSpaceEmpty(to) && isMoveInRange(from,to,true);
    }

    private static boolean isMarbleNearby(final int index){
        final int j=index/dimen;
        final int i=index%dimen;
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

    private static boolean isMoveInRange(final int from,final int to,final boolean simple){
        final int iFrom=from/dimen;
        final int jFrom=from%dimen;
        final int iTo=to/dimen;
        final int jTo=to%dimen;
        final int iSum=iTo-iFrom;
        final int jSum=jTo-jFrom;
        final int distance=(int)Math.sqrt((iSum*iSum)+(jSum*jSum));
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

    private static boolean isSpaceEmpty(final int index){
        if(playground[index].getIcon()==null){
            return true;
        }else{
            return false;
        }
    }

    private static void highlightSpace(final int index){   
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

    private static void drawMarbles(final JButton[] playground,final int count,final int dimen){
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

    private static void processFrame(final JFrame frame){
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
    }

    private static void resetVars(){
        currentPlayerIndex=0;
        isSelected=false;
        selectedIndex=0;
        endTurnButton.setEnabled(false);
        didHop=false;
        simpleMove=false;
    }

    @SuppressWarnings("unchecked")
    private static void loadData(){
        try{
            final FileInputStream fis = new FileInputStream("records.json");
            final ObjectInputStream ois = new ObjectInputStream(fis);
            records = (ArrayList<Record>)( ois.readObject());
            ois.close();
            fis.close();
        } catch (final Exception ex) {
            return;
        } 
    }

    private static void saveData(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream("records.json");
            out = new ObjectOutputStream(fos);
            out.writeObject(records);
            out.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sortRecords(){
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record z1, Record z2) {
                if (z1.count < z2.count)
                    return 1;
                if (z1.count > z2.count)
                    return -1;
                return 0;
            }
        });
    }
 }