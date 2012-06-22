package com.tyj.onepiece;

import android.app.Activity;
import android.util.AttributeSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OnePieceGame extends Activity {
	/** Called when the activity is first created. */
	private ProgressBar pb1;
	private ProgressBar pb2;
	private CtrlView cv1;
	private CtrlView cv2;
	public static final int START_ID = Menu.FIRST;
	public static final int REARRARY_ID = Menu.FIRST + 1;
	public static final int END_ID = REARRARY_ID + 1;
	private int dormant = 1000;
	private boolean isCancel=true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViews();
		mRedrawHandler.sleep(dormant);

	}

	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(isCancel){
				run();
			}else{}						
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// 移除信息队列中最顶部的信息（从顶部取出信息）
			sendMessageDelayed(obtainMessage(0), delayMillis);// 获得顶部信息并延时发送
		}
	};

	public void run() {
		if (cv1.much != 0 && cv2.much != 0) {
			pb1.setProgress(pb1.getMax() - cv1.much);
			pb2.setProgress(pb2.getMax() - cv2.much);
			mRedrawHandler.sleep(dormant);
		} else {
			cv1.setEnabled(false);
			cv2.setEnabled(false);
			dialogForFinish().show();
		}
	}

	private void findViews() {
		pb1 = (ProgressBar) findViewById(R.id.pb1);
		cv1 = (CtrlView) findViewById(R.id.cv1);
		pb1.setMax(cv1.much);
		pb1.setProgress(0);
		
		pb2 = (ProgressBar) findViewById(R.id.pb2);
		cv2 = (CtrlView) findViewById(R.id.cv2);
		pb2.setMax(cv2.much);
		pb2.setProgress(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, START_ID, 0, R.string.newgame);
		menu.add(0, REARRARY_ID, 0, R.string.rearrage);
		menu.add(0, END_ID, 0, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case START_ID:
			newPlay();
			break;
		case REARRARY_ID:
			cv1.rearrange();
			cv1.PROCESS_VALUE = cv1.PROCESS_VALUE - 5;
			
			cv2.rearrange();
			cv2.PROCESS_VALUE = cv1.PROCESS_VALUE - 5;
			break;
		case END_ID:
			isCancel=false;
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		isCancel=false;
		pb1 = null;
		cv1 = null;
		pb2 = null;
		cv2 = null;
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		isCancel=false;
		super.onDestroy();
	}
	
	@Override
	protected void onStart(){
		isCancel=false;
		newPlay();
		isCancel=true;
		super.onStart();
		
	}
//	@Override
//	protected void onRestart(){
//		cv.reset();
//		super.onRestart();
//	}
//	

	public void newPlay() {
		cv1.reset();
		pb1.setProgress(cv1.GAMETIME);
		cv1.PROCESS_VALUE = cv1.GAMETIME;
		mRedrawHandler.sleep(dormant);
		cv1.setEnabled(true);
		
		cv2.reset();
		pb2.setProgress(cv2.GAMETIME);
		cv2.PROCESS_VALUE = cv2.GAMETIME;
		mRedrawHandler.sleep(dormant);
		cv2.setEnabled(true);
	}
	
	public AlertDialog dialogForFinish() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.finishInfo)
				.setPositiveButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();
							}
						}).setNeutralButton(R.string.exit,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								System.exit(0);//直接退出游戏
							}
						});
		return builder.create();
	}

	public AlertDialog dialogForSucceed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.succeedInfo)
				.setPositiveButton(R.string.next,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dormant = dormant - 300;
								newPlay();
							}
						}).setNeutralButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();
							}
						});
		return builder.create();
	}

	public AlertDialog dialogForFail() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.failInfo)
				.setPositiveButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();
							}
						}).setNegativeButton(R.string.exit,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								isCancel=false;
								finish();
							}
						});
		return builder.create();
	}

}