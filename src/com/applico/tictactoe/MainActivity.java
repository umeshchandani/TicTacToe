package com.applico.tictactoe;

import java.util.ArrayList;




import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity  implements OnClickListener {

	static final int GRIDSIZE = 3;
	public ImageButton[][] _tttGrid= new ImageButton[GRIDSIZE][GRIDSIZE]; 
	int [][] playGrid = new int[GRIDSIZE][GRIDSIZE];
	public static final int _COMP_TURN =0;
	public static final int _PLAYER_TURN = 1;
	public static final int _BLANK = 0;
	public static final int _X_MOVE = 1;
	public static final int _O_MOVE = 2;
	public static int totalSteps = 0;
	public static int turn = _PLAYER_TURN;
	public RelativeLayout  rl0,rl1;
	public ImageView win;
	public TextView display, click_start, message_text;
	public Dialog game_loop;
	public Dialog choice;
	public Dialog play_game;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_tttGrid[0][0]= (ImageButton) this.findViewById(R.id.button01);
		_tttGrid[0][1]= (ImageButton) this.findViewById(R.id.button02);
		_tttGrid[0][2]= (ImageButton) this.findViewById(R.id.button03);
		_tttGrid[1][0]= (ImageButton) this.findViewById(R.id.button04);
		_tttGrid[1][1]= (ImageButton) this.findViewById(R.id.button05);
		_tttGrid[1][2]= (ImageButton) this.findViewById(R.id.button06);
		_tttGrid[2][0]= (ImageButton) this.findViewById(R.id.button07);
		_tttGrid[2][1]= (ImageButton) this.findViewById(R.id.button08);
		_tttGrid[2][2]= (ImageButton) this.findViewById(R.id.button09);
		click_start = (TextView) findViewById(R.id.click_to_start);
		rl0 =(RelativeLayout)findViewById(R.id.rl0);
		//rl0.setBackgroundResource(R.drawable.mainbackground);
		win = (ImageView) findViewById(R.id.imageViewWin);
		
		display = (TextView) findViewById(R.id.display_field);
		
		//Initialize Game loop Dialog
		game_loop = onCreateDialog(R.string.restart);
		
		
		//Initialize Game turn Dialog
		choice = onCreateDialog(R.string.choice);
	
		
		// Play Game
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Play Game!!!")
		       .setPositiveButton("Start", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //Ask turn choice
		        	   choice.show();
		        	   message_text = (TextView)choice.findViewById(android.R.id.message);
		       		   message_text.setGravity(Gravity.CENTER);
		           }
		       });
		play_game = builder.create();
		play_game.show();
		message_text = (TextView)play_game.findViewById(android.R.id.message);
		message_text.setGravity(Gravity.CENTER);
		
		for(int i =0; i<GRIDSIZE; i++)
   		{
   			for(int j= 0; j< GRIDSIZE; j++)
   			{
   				_tttGrid[i][j].setOnClickListener(this);
   				playGrid[i][j] = _BLANK;
   				totalSteps = 0;
   			}
   		}
   		if( turn == _COMP_TURN)
   		{
   			click_start.setOnClickListener(this);
   			click_start.setVisibility(View.VISIBLE);

   		}

		
	}





	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//click_start.setVisibility(View.INVISIBLE); 
		click_start.setText("");
		ImageButton clickButton = (ImageButton)arg0;
		for( int i =0; i<GRIDSIZE; i++ )
		{
			for( int j = 0; j < GRIDSIZE; j++ )
			{
				if( clickButton.equals( _tttGrid[ i ][ j ] ))
				{
					if( playGrid[i][j] == _BLANK)
					{
						if(turn == _PLAYER_TURN)
						{	
							//Increment stepCount;
							totalSteps++;
							clickButton.setBackgroundResource(R.drawable.star);

							clickButton.setEnabled(false);
							playGrid[i][j] = _X_MOVE;
						}
						turn = _PLAYER_TURN;
						//check Win
						if(winCheck(_X_MOVE, i, j))
						{
							//Player Wins
							gameResult(R.string.player_win);

						}
						else
						{
							//in case player plays last chance
							if(totalSteps == ( GRIDSIZE * GRIDSIZE ))
							{
								gameResult(R.string.no_result);
							}
							else
							{
								//Increment stepCount;
								totalSteps++;
								if( aiMove())
								{
									//Computer Wins and Check double
									gameResult(R.string.robot_win);
								}
								//Tie Case //in case robot plays last chance
								if(totalSteps == ( GRIDSIZE * GRIDSIZE ))
								{
									//tieGame();
									gameResult(R.string.no_result);
								}
								click_start.setText("Your Turn");
							}
						
						}
					}
					else//Tie case
					{
						gameResult(R.string.no_result);
					}
				}

			}

		}
	}

	//Restart Game
	public void restartGame()
	{
		turn = _COMP_TURN;
		click_start.setText(R.string.click);
		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	public void gameResult(int robotWin){
		
		click_start.setText("");
		
		if(robotWin == (R.string.no_result))
		{
			display.setText(R.string.no_result);
			win.setBackgroundResource(R.drawable.tie);
			//Toast.makeText(getApplicationContext(), "It's a Tie! ", Toast.LENGTH_SHORT).show();
		}
		
		if( robotWin == (R.string.robot_win))
		{
			display.setText(R.string.robot_win);
			win.setBackgroundResource(R.drawable.robot);
			//Toast.makeText(getApplicationContext(), "Yeah! I win! ", Toast.LENGTH_SHORT).show();
		}
		
		if( robotWin == (R.string.player_win))
		{
			display.setText(R.string.player_win);
			win.setBackgroundResource(R.drawable.win);
			//Toast.makeText(getApplicationContext(), "Yeah! You win! ", Toast.LENGTH_SHORT).show();	
		}
		

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	game_loop.show();
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(game_loop.getWindow().getAttributes());
				lp.x= lp.width/2;
				lp.y= 600;
				game_loop.getWindow().setAttributes(lp);
				TextView message_text = (TextView)game_loop.findViewById(android.R.id.message);
		 		message_text.setGravity(Gravity.CENTER);
		    }
		}, 2000);
		
	
	}

	//Check for player Win and Robot Win!
	public boolean winCheck(int player, int row, int col) {
		int v = 0, h = 0,   diag_One = 0, diag_Two = 0;
		int winValue = 3*player;
		for (int i = 0; i < GRIDSIZE; i++) {
			if (playGrid[i][col] == player) {
				v += player;// vertical
			}
			if(v==winValue)
			{
				for(int j =0 ;j < GRIDSIZE; j++)
					_tttGrid[j][col].setBackgroundResource(R.drawable.thunder);
				return true;
			}
			if (playGrid[row][i] == player) {
				h += player;// horizontal 
			}
			if(h==winValue)
			{
				for(int j =0 ;j < GRIDSIZE; j++)
					_tttGrid[row][j].setBackgroundResource(R.drawable.thunder);
				return true;
			}
		}



		if (row == col) {
			for (int i = 0; i < GRIDSIZE; i++) {
				if (playGrid[i][i] == player) {
					diag_One += player;// backward slash like slant
				}
			}
			if((diag_One)==winValue)
			{
				for(int j =0 ;j < GRIDSIZE; j++)
					_tttGrid[j][j].setBackgroundResource(R.drawable.thunder);
				return true;
			}
		}
		if ((row + col+ 2)% (GRIDSIZE +1) == 0) {
			for (int i = 0; i < GRIDSIZE; i++) {
				if (playGrid[2 - i][i] == player) {
					diag_Two += player;// forward slant
				}
			}
			if((diag_Two)==winValue)
			{
				for(int j =0 ;j < GRIDSIZE; j++)
					_tttGrid[2-j][j].setBackgroundResource(R.drawable.thunder);
				return true;
			}
		}

		return false;
	}

	// Computer moves
	public boolean aiMove()
	{

		//Check if it can win
		if(checkBlank(_O_MOVE))
		{
			return true;
		}

		//If Computer Cant win then block Player
		if( checkBlank(_X_MOVE))
		{
			return false;
		}

		// Random Movement
		ArrayList<Integer> blankList = new ArrayList<Integer>();
		for(int i =0; i< GRIDSIZE ; i++)
		{
			for(int j= 0; j< GRIDSIZE ; j++ )
			{
				if(playGrid[i][j] == _BLANK)
				{
					blankList.add(i*10 + j);
				}
			}
		}

		int curChoice = (int)(Math.random() *blankList.size());

		playGrid[blankList.get(curChoice)/10][blankList.get(curChoice)%10] = _O_MOVE;
		//_tttGrid[blankList.get(curChoice)/10][blankList.get(curChoice)%10].setText("O");
		_tttGrid[blankList.get(curChoice)/10][blankList.get(curChoice)%10].setBackgroundResource(R.drawable.heart);
		_tttGrid[blankList.get(curChoice)/10][blankList.get(curChoice)%10].setEnabled(false);
		//Toast.makeText(getApplicationContext(), "Randomn Play", Toast.LENGTH_SHORT).show();
		return false;



	}

	public boolean checkBlank (int playerId)
	{
		//check columns

		int blankI = 0;
		int blankJ = 0;
		int blankCount = 0;
		int _OCount = 0;
		boolean draw = false;
		for(int i =0; i< GRIDSIZE ; i++)
		{
			//check rows
			blankCount = 0;
			blankI = 0;
			blankJ = 0;
			_OCount = 0;
			for(int j= 0; j< GRIDSIZE ; j++ )
			{
				if(playGrid[i][j] == _BLANK)
				{
					blankCount ++;
					blankI = i;
					blankJ = j;

				}

				if(playGrid[i][j] == playerId)
				{
					_OCount++;
				}
			}

			if(_OCount == 2 && blankCount == 1)
			{
				playGrid[ blankI ][ blankJ ] = _O_MOVE;
				//_tttGrid[blankI][blankJ].setText("O");
				_tttGrid[blankI][blankJ].setBackgroundResource(R.drawable.heart);
				_tttGrid[blankI][blankJ].setEnabled(false);
				if(playerId == _O_MOVE)
				{
					draw = winCheck(_O_MOVE, blankI, blankJ);
				}
				return true;
			}

		}




		for(int j= 0; j< GRIDSIZE ; j++ )
		{
			//check rows
			blankCount = 0;
			blankI = 0;
			blankJ = 0;
			_OCount = 0;
			for(int i =0; i< GRIDSIZE ; i++)
			{
				if(playGrid[i][j] == _BLANK)
				{
					blankCount ++;
					blankI = i;
					blankJ = j;

				}

				if(playGrid[i][j] == playerId)
				{
					_OCount++;
				}
			}

			if(_OCount == 2 && blankCount == 1)
			{
				playGrid[ blankI ][ blankJ ] = _O_MOVE;
				//_tttGrid[blankI][blankJ].setText("O");
				_tttGrid[blankI][blankJ].setBackgroundResource(R.drawable.heart);
				_tttGrid[blankI][blankJ].setEnabled(false);
				if(playerId == _O_MOVE)
				{
					draw = winCheck(_O_MOVE, blankI, blankJ);
				}
				return true;
			}


		}



		//Check diagonal from top left to bottom right

		blankCount = 0;
		blankI = 0;
		blankJ = 0;
		_OCount = 0;
		for(int i = 0; i< GRIDSIZE ; i++ ){
			if(playGrid[i][i] == _BLANK)
			{
				blankCount ++;
				blankI = i;
				blankJ = i;

			}
			if(playGrid[i][i] == playerId)
			{
				_OCount++;
			}
		}

		if(_OCount == 2 && blankCount == 1)
		{
			playGrid[ blankI ][ blankJ ] = _O_MOVE;
			_tttGrid[blankI][blankJ].setBackgroundResource(R.drawable.heart);
			//_tttGrid[blankI][blankJ].setText("O");
			_tttGrid[blankI][blankJ].setEnabled(false);
			if(playerId == _O_MOVE)
			{
				draw = winCheck(_O_MOVE, blankI, blankJ);
			}
			return true;
		}

		//Check diagonal from top right to bottom left


		blankCount = 0;
		blankI = 0;
		blankJ = 0;
		_OCount = 0;

		for(int i = 0; i< GRIDSIZE ; i++ ){

			if(playGrid[i][GRIDSIZE-1-i] == _BLANK)
			{
				blankCount ++;
				blankI = i;
				blankJ = GRIDSIZE-1-i;


			}
			if(playGrid[i][GRIDSIZE-1-i] == playerId)
			{
				_OCount++;
			}
		}

		if(_OCount == 2 && blankCount == 1)
		{
			playGrid[ blankI ][ blankJ ] = _O_MOVE;
			//_tttGrid[blankI][blankJ].setText("O");
			_tttGrid[blankI][blankJ].setBackgroundResource(R.drawable.heart);
			_tttGrid[blankI][blankJ].setEnabled(false);
			if(playerId == _O_MOVE)
			{
				draw = winCheck(_O_MOVE, blankI, blankJ);
			}
			return true;
		}



		return false;



	}


	public Dialog onCreateDialog(int option) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		if(option == R.string.restart)
		{
			builder.setMessage(R.string.try_again)
			
			.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			})
			.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					restartGame();
				}
			});
		}


		if(option == (R.string.choice))
		{
			builder.setMessage("Take First Turn ? ")
			.setCancelable(false)
			.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					turn = 0;
				}
			})
			.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					turn = 1;
				}
			});
		}

		// Create the AlertDialog object and return it
		return builder.create();
	}


}
