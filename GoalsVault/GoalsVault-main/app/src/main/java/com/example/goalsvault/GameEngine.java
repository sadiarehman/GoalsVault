package com.example.goalsvault;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goalsvault.R;

public class GameEngine extends AppCompatActivity {

    // GameView instance "gameView" is the view of our game
    // It holds all the game logics and features
    // It responds to screen touches as well
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView in onCreate method and set it as the view
        gameView = new GameView(this);
        setContentView(gameView);

    }

    // Public getter method to retrieve width of the device screen
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    // Public setter method to retrieve height of the device screen
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    // Here is our implementation of GameView
    // It is an inner class
    // Notice we implement runnable so we have a thread and can override the run method
    class GameView extends SurfaceView implements Runnable {

        // This is our thread
        Thread gameThread = null;

        // Declare and initialize fields for the initSoundPool() method
        // MAX_STREAMS sets the maximum number of streams that can be played at a time
        // soundBackground stores the audio that has to be played
        // soundPoolLoaded set to true when sound has been loaded from the specified path
        // SoundPool is a collection of sound samples that can be loaded into memory
        // from a resource inside the APK or from a file in the system
        private static final int MAX_STREAMS=100;
        private int soundBackground;
        private boolean soundPoolLoaded;
        private SoundPool soundPool;

        // screenX stores screen width
        // screenY stores screen height
        int screenX = getScreenWidth();
        int screenY = getScreenHeight();


        // Counter for background change
        private int bgCount = 0;

        // We need a SurfaceHolder when we use Paint and Canvas in a thread
        // We will see it in action in the draw method
        SurfaceHolder ourHolder;

        // A boolean which we will set and reset
        // Determines whether the game is running or not
        volatile boolean playing;

        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;

        // This variable tracks the game frame rate
        long fps;

        // This is used to help calculate the fps
        private long timeThisFrame;

        // Declare objects of type Bitmap
        // They include:
        // 1) Backgrounds of all levels
        // 2) Bob's moving positions (forward and reverse)
        // 3) Four buttons (forward, back, jump, crouch)
        // 4) Game obstacles
        Bitmap bitmapBob, bitmapBobBack, bitmapBg, bitmapBg2, bitmapBg3, bitmapBgEnd, bitmapBg4, bitmapBg5, bitmapWin;
        Bitmap bitmapSpike2, bitmapSpike, bitmapSpikeInvert, bitmapBrick, bitmapFire, bitmapFireInvert, bitmapRed, bitmapball;
        Bitmap bitmapFwd, bitmapBack, bitmapDown, bitmapUp, bitmapFwdPressed, bitmapBackPressed, bitmapDownPressed, bitmapUpPressed;

        // Bob starts off not moving
        // all movement indicator variables set to false
        boolean isMovingForward = false;
        boolean isMovingBackward = false;
        boolean isJumping = false;
        boolean isCrouching = false;

        // Bob walks at 500 pixels per second
        float walkSpeedPerSecond = 500;

        // Bob's starting positions
        // bobXPosition determines Bob's displacement along x-axis
        // bobYPosition determines Bob's displacement along y-axis
        double bobXPosition = screenX / (float) 8.5;
        float bobYPosition = screenY / (float) 1.55;

        // Declare and initialize variables to set button positions
        // Position along y-axis stays the same so use one variable for y displacement
        float fwdXPosition = screenX / (float) 5.5;
        float backXPosition = screenX / (float) 35;
        float downXPosition = screenX / (float) 1.7;
        float upXPosition = screenX / (float) 1.16;
        float btnYPosition = screenY / (float) 1.36;

        // Define width and height of button using btnWidth and btnHeight respectively
        private final int btnWidth = 235;
        private final int  btnHeight = 235;

        // Adjust the spikes and ball positions
        float spike1XPosition = screenX / (float) 1.65;
        float spike1YPosition = screenY / (float) 2.75;
        float spike2XPosition = screenX / (float) 1.4;
        float spike2YPosition = screenY / (float) 2.7;
        float spike3XPosition = screenX / (float) 1.2;
        float spike3YPosition = screenY / (float) 2.75;
        float spike4XPosition = screenX / (float) 5.3;
        float spike4YPosition = screenY / (float) 1.5;
        float movingSpikeX = screenX / (float) 3;
        float ballXPosition = screenX + 200 ;
        float ballYPosition = screenY / (float) 9.5 ;

        //background four obstacles position
        float red1XPosition = screenX / (float) 11.5;
        float red2XPosition = screenX / (float) 8;
        float red3XPosition = screenX / (float) 3.9;
        float red4XPosition = screenX / (float) 2.95;
        float red5XPosition = screenX / (float) 3.35;
        float red1YPosition = screenY / (float) 1.3;
        float red2YPosition = screenY / (float) 1.3;
        float red3YPosition = screenY / (float) 1.3;
        float fireXPosition = -1500;
        float fireYPosition = screenY / (float) 8;
        float brickXPosition = screenX / (float) 1.45;
        float brickYPosition = screenY /(float) 1.15 + 100;

        // Fire for last level
        float fireBg4XPosition = screenX + 200 ;
        float fire1Bg4YPosition = screenY/(float) 1.5 ;
        float fire2Bg4YPosition = screenY / (float) 3.5 ;

        // Specify heights of spikes
        private float spike1Height = 120;
        private float spike2Height = 395;
        private float spike3Height = 120;

        // level 4 obstacle height adjustment
        private float red1Height = 1500;
        private float red2Height = 1500;
        private float red3Height = 1500;


        //Spike moving speed
        float spikeSpeedPerSecond = 440;


        //For sprite sheet animation

        // These next two values can be anything you like
        // As long as the ratio doesn't distort the sprite too much
        private final int  frameWidth = 100;
        private int frameHeight = 100;

        // Number of frames on the sprite sheet
        private int frameCount = 5;

        // Start at the first frame
        private int currentFrame = 0;

        // Time when we last changed frames
        private long lastFrameChangeTime = 0;

        // Each frame duration
        private int frameLengthInMilliseconds = 100;

        // A rectangle to define an area of the sprite sheet that represents 1 frame
        // Parameters: left, up, right, bottom (Sprite sheet rectangle dimensions)
        private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);

        // Rectangles that define an area of the screen on which to draw
        // Parameters: left, up, right, bottom (Screen rectangle dimensions)
        RectF whereToDraw = new RectF((int)bobXPosition, bobYPosition, (int)bobXPosition + frameWidth, bobYPosition + frameHeight);
        RectF whereToDrawFwd = new RectF(fwdXPosition, btnYPosition, fwdXPosition + btnWidth, btnYPosition + btnHeight);
        RectF whereToDrawBack = new RectF(backXPosition, btnYPosition, backXPosition + btnWidth, btnYPosition + btnHeight);
        RectF whereToDrawDown = new RectF(downXPosition, btnYPosition, downXPosition + btnWidth, btnYPosition + btnHeight);
        RectF whereToDrawUp = new RectF(upXPosition, btnYPosition, upXPosition + btnWidth, btnYPosition + btnHeight);

        // Variable initialY for jump height
        private float initialY = screenY / (float) 1.55;

        // Public getter method to retrieve initialY
        public float getInitialY(){
            return this.initialY;
        }

        // Public setter method to set initialY
        public void setInitialY (float y){
            this.initialY = y;
        }


        // When the we initialize (call new()) on gameView
        // This special constructor method runs
        public GameView(Context context) {
            // The next line of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Load bitmaps from their .png file
            // They include bitmaps of Bob, level backgrounds, buttons and obstacles
            // R class used to access the .png files
            bitmapBg = BitmapFactory.decodeResource(this.getResources(), R.drawable.first);
            bitmapBg2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.second);
            bitmapBg3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.third);
            bitmapBg4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.fourth);
            bitmapBg5 = BitmapFactory.decodeResource(this.getResources(), R.drawable.fifth);
            bitmapWin = BitmapFactory.decodeResource(this.getResources(), R.drawable.win);
            bitmapBgEnd = BitmapFactory.decodeResource(this.getResources(), R.drawable.end);
            bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob);
            bitmapBobBack = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob_back);
            bitmapBrick = BitmapFactory.decodeResource(this.getResources(), R.drawable.brick);
            bitmapSpike = BitmapFactory.decodeResource(this.getResources(), R.drawable.spike);
            bitmapSpike2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.spike);
            bitmapSpikeInvert = BitmapFactory.decodeResource(this.getResources(), R.drawable.spike_invert);
            bitmapFire = BitmapFactory.decodeResource(this.getResources(), R.drawable.fire);
            bitmapFireInvert = BitmapFactory.decodeResource(this.getResources(), R.drawable.fire_invert);
            bitmapball = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle);
            bitmapRed = BitmapFactory.decodeResource(this.getResources(), R.drawable.red);
            bitmapFwd = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_fwd);
            bitmapBack = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_back);
            bitmapDown = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_down);
            bitmapUp = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_up);
            bitmapFwdPressed = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_fwd_pressed);
            bitmapBackPressed = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_back_pressed);
            bitmapUpPressed = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_up_pressed);
            bitmapDownPressed = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_down_pressed);


            // Scale the bitmap to the correct size
            // We need to do this because Android automatically scales bitmaps based on screen density
            // Parameters: bitmap, bitmap width, bitmap height and boolean filter
            bitmapBg = Bitmap.createScaledBitmap(bitmapBg, screenX, screenY,false);
            bitmapBg2 = Bitmap.createScaledBitmap(bitmapBg2, screenX, screenY, false);
            bitmapBg3 = Bitmap.createScaledBitmap(bitmapBg3, screenX, screenY, false);
            bitmapBg4 = Bitmap.createScaledBitmap(bitmapBg4, screenX, screenY, false);
            bitmapBg5 = Bitmap.createScaledBitmap(bitmapBg5, screenX, screenY, false);
            bitmapWin = Bitmap.createScaledBitmap(bitmapWin, screenX, screenY, false);
            bitmapBgEnd = Bitmap.createScaledBitmap(bitmapBgEnd, screenX, screenY, false);
            bitmapBob = Bitmap.createScaledBitmap(bitmapBob, frameWidth * frameCount, frameHeight, false);
            bitmapBobBack = Bitmap.createScaledBitmap(bitmapBobBack, frameWidth * frameCount, frameHeight, false);
            bitmapFwd = Bitmap.createScaledBitmap(bitmapFwd, btnWidth, btnHeight, false);
            bitmapBack = Bitmap.createScaledBitmap(bitmapBack, btnWidth, btnHeight, false);
            bitmapDown = Bitmap.createScaledBitmap(bitmapDown, btnWidth, btnHeight, false);
            bitmapUp = Bitmap.createScaledBitmap(bitmapUp, btnWidth, btnHeight, false);
            bitmapFwdPressed = Bitmap.createScaledBitmap(bitmapFwdPressed, btnWidth, btnHeight, false);
            bitmapBackPressed = Bitmap.createScaledBitmap(bitmapBackPressed, btnWidth, btnHeight, false);
            bitmapUpPressed = Bitmap.createScaledBitmap(bitmapUpPressed, btnWidth, btnHeight, false);
            bitmapDownPressed = Bitmap.createScaledBitmap(bitmapDownPressed, btnWidth, btnHeight, false);

            // Set our boolean to true - game on!
            playing = true;

            if (playing) {
                // call initSoundPool() to play the game sound
                this.initSoundPool();
            }
        }

        // initSoundPool() method to play game sound
        private void initSoundPool()  {
            // With Android API >= 21.
            if (Build.VERSION.SDK_INT >= 21 ) {

                AudioAttributes audioAttrib = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();

                SoundPool.Builder builder= new SoundPool.Builder();
                builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
                this.soundPool = builder.build();
            }
            // With Android API < 21
            else {
                // SoundPool(int maxStreams, int streamType, int srcQuality)
                this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
            }

            // When SoundPool load complete.
            this.soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                soundPoolLoaded = true;

                // Playing background sound.
                playSoundBackground();
            });

            // Load the sound background.mp3 into SoundPool
            this.soundBackground= this.soundPool.load(this.getContext(), R.raw.game_sound,1);
        }

        // Executes when sound has been loaded from the specified path
        public void playSoundBackground()  {
            if(this.soundPoolLoaded) {
                float leftVolumn = 0.8f;
                float rightVolumn =  0.8f;

                // Play sound background.mp3
                int streamId = this.soundPool.play(this.soundBackground,leftVolumn, rightVolumn, 1, -1, 1f);
            }
        }

        @Override
        public void run() {
            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                update();

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }
            }//end while loop
        }// end method run()


        // reverse variables created to control obstacles' to and fro movement
        boolean reverse = false, reverseBall = false, reverseBrick = false;

        // updateSpike() method to set spike positions relative to Bob's position
        public void updateSpike() {

            // switch-case statements used to perform certain function depending upon the current level
            switch (bgCount) {
                case 0:
                    // Level 1 spike adjustments
                    // To check of Bob has collided with any of the spikes in level 1
                    // Call checkCollision method
                    checkCollision(spike1YPosition, spike1Height, bobYPosition, bobXPosition, 75, spike1XPosition, 80);
                    checkCollision(spike2YPosition, spike2Height, bobYPosition, bobXPosition, 50, spike2XPosition, 80);
                    checkCollision(spike3YPosition, spike3Height, bobYPosition, bobXPosition, 70, spike3XPosition, 80);

                    // Adjust spike positions according to Bob's position
                    if (bobXPosition > screenX/2.5) {
                        if (spike1Height < 395) {
                            spike1Height += 1.9;
                        }
                        if (spike2Height > 120) {
                            spike2Height -= 0.65 ;
                        }
                        if (spike3Height < 395) {
                            spike3Height += 0.815;
                        }
                    }
                    break;
                case 1:
                    // Level 2 spike adjustments
                    checkCollision(spike4YPosition, 80, bobYPosition, bobXPosition, 75, spike4XPosition, 85);
                    checkCollision(spike4YPosition, 80, bobYPosition, bobXPosition, 50, movingSpikeX, 85);
                    if ((bobXPosition) >= (spike4XPosition + 400)) {
                        movingSpikeX = movingSpikeX + (spikeSpeedPerSecond / fps);
                    }
                    break;

                case 2:
                    // Level 3 ball adjustments
                    checkCollision(ballYPosition, 300, bobYPosition, bobXPosition, 75, ballXPosition, 300);
                    if (ballXPosition >= screenX) {
                        reverseBall = false;
                    } else if (ballXPosition < -100) {
                        reverseBall = true;
                    }

                    // Depending upon ball's movement direction, increment or decrement ballXPosition
                    // If it's moving forward i.e. reverseBall==true, increment it
                    // If it's moving backward i.e. reverseBall==false, decrement it
                    if (!reverseBall) {
                        ballXPosition = ballXPosition - (float)(500 / fps);
                    } else {
                        ballXPosition = ballXPosition + (float)(500 / fps);
                    }

                    // Move the ball up for a moment to allow Bob to dodge it
                    if (((int)ballXPosition <= (int)(screenX / 1.95 )) && ((int)ballXPosition >= (int)screenX/2.4 )) {
                        ballYPosition -= 100;
                    } else {
                        ballYPosition = screenY / (float) 9.5 ;
                    }
                    break;


                case 3:
                    // Level 4 fire adjustments

                    checkCollision(fireYPosition, 250 , bobYPosition, bobXPosition, 70, fireXPosition, 310);
                    checkCollision(red1YPosition , red1Height , bobYPosition, bobXPosition, 70, red1XPosition , 60);
                    checkCollision(red2YPosition , red2Height , bobYPosition, bobXPosition, 70, red2XPosition , 60);
                    checkCollision(red1YPosition , red1Height , bobYPosition, bobXPosition, 70, red3XPosition , 60);
                    checkCollision(red2YPosition , red2Height , bobYPosition, bobXPosition, 70, red4XPosition , 60);
                    checkCollision(red3YPosition , red3Height , bobYPosition, bobXPosition, 70, red5XPosition , 60);

                    // To check if fire should be reversed or not
                    if (fireXPosition <= -100) {
                        reverse = false;
                    } else if (fireXPosition > screenX) {
                        reverse = true;
                    }

                    //update fire X position
                    if (!reverse) {
                        fireXPosition = fireXPosition + (float)(600 / fps);
                    } else {
                        fireXPosition = fireXPosition - (float)(600 / fps);
                    }

                    //Update red spikes Y position
                    if (red1YPosition >= 100) {
                        red1YPosition = red1YPosition - (float)(600 / fps);
                        red2YPosition = red2YPosition - (float)(500 / fps);
                        red3YPosition = red3YPosition - (float)(400 / fps);
                    } else if (red1YPosition < 100){
                        red1YPosition = screenY;
                        red2YPosition = screenY;
                        red3YPosition = screenY;
                    }
                    break;

                case 4:

                    checkCollision(fire2Bg4YPosition , 85 , bobYPosition, bobXPosition, 60, fireBg4XPosition , 60);
                    checkCollision(fire2Bg4YPosition , 85 , bobYPosition, bobXPosition, 60, fireBg4XPosition , 60);

                    // Level 5 fire and brick adjustments
                    if ((fireBg4XPosition) >= screenX/(float) 1.43 ) {
                        fireBg4XPosition = fireBg4XPosition - (float)(185 / fps);
                    } else {
                        fireBg4XPosition = screenX + 200 ;
                    }

                    // To check if brick should be reversed or not
                    if (brickYPosition > 0 ) {
                        reverseBrick = false;
                    } else if (brickYPosition <= 0 ) {
                        reverseBrick = true;
                    }

                    // Update brick position
                    if (!reverseBrick) {
                        brickYPosition -= 1 ;
                    } else {
                        brickYPosition = screenY  ;
                    }

                    // Move the brick up as soon a sBob lands on it
                    // Increment bgCount to display last background i.e. win screen
                    if (((int)bobXPosition > (int)brickXPosition) && ((int)bobXPosition < ((int)brickXPosition + 250)) && (((int)(bobYPosition) + 100) == (int)brickYPosition)) {
                        bobYPosition -= 1 ;
                        if (( bobYPosition )<= 0 ){
                            bgCount ++;
                        }
                    }
                    break;

            }
        }//end method updateSpike()

        // check collision method to check for bob's interaction with the obstacles
        public void checkCollision(float spike1YPosition, float spike1Height, float bobYPosition, double bobXPosition, int frameWidth, float spike1XPosition , int spikeWidth) {
            switch(bgCount) {
                // Collision check maintained via the use of conditional statements
                // If bob's collides with the obstacle in any level, the bgCount would be updated to 6
                // bgCount 6 displays the GAME OVER screen

                case 0:
                    if (((int) (spike1YPosition + spike1Height) == (int) bobYPosition) && (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    if (((int) (spike1YPosition + spike1Height) > (int) bobYPosition) && (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    break;
                case 1:
                    if (((int)spike1YPosition  <= ((int) bobYPosition + 70)) && (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    break;

                case 2:
                    if ((((int) spike1YPosition + spike1Height ) >= (int) bobYPosition )&& (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    break;
                case 3:
                    if ((spikeWidth == 310 )&& ((int) (spike1YPosition + spike1Height) >= (int) bobYPosition) && (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    if ((spikeWidth == 60) && ((int)spike1YPosition  <= ((int) bobYPosition + 70)) && (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    if ((int) bobYPosition >= screenY / (float) 1.3) {
                        bgCount = 6;
                    }
                    break;

                case 4:
                    if (bobYPosition == screenY ) {
                        bgCount = 6;
                    }
                    if (((((int)spike1YPosition + spike1Height ) >= (int) bobYPosition)) && (spike1YPosition <= ((int)bobYPosition + 50 )) && (((int) bobXPosition + frameWidth) >= (int) (spike1XPosition)) && ((int) bobXPosition <= ((int) (spike1XPosition) + spikeWidth))) {
                        bgCount = 6;
                    }
                    break;

            }
        }//end collision check


        // Handles Bob's x and y positions as you proceed through a level
        public void positionSet() {
            switch (bgCount) {
                case 2:
                    // Bob positions for level 3
                    if (bobXPosition > screenX / (float) 1.8 && bobXPosition < screenX / (float) 1.6) {
                        bobYPosition = screenY / (float) 2.65;
                        if (isCrouching) {bobYPosition += 30;}
                    }
                    if (bobXPosition < screenX/(float)1.8 || bobXPosition > screenX / (float) 1.6) {
                        bobYPosition = screenY /(float) 3;
                    }
                    break;
                case 3:
                    // Bob positions for level 4
                    if (bobYPosition == screenY / (float) 2.85) {
                        if ((bobXPosition > screenX / (float) 14 && bobXPosition < screenX / (float) 6.4) ||
                                (bobXPosition > screenX / (float) 4.2 && bobXPosition < screenX / (float) 2.8) ||
                                (bobXPosition > screenX / (float) 2.25 && bobXPosition < screenX / (float) 1.8) ||
                                (bobXPosition > screenX / (float) 1.52 && bobXPosition < screenX / (float) 1.3) ||
                                (bobXPosition > screenX / (float) 1.16 && bobXPosition < screenX / (float) 1.1)) {
                            bobYPosition = screenY / (float) 1.2;
                        }
                    }
                    break;
            }
        } //end method positionSet()

        // Everything that needs to be updated goes in here
        public void update() {

            // Call updateSpike() method to update obstacle positions and movements
            updateSpike();

            // If bob is moving (the player is touching the screen)
            // then move him to the right based on his target speed and the current fps.
            if (isMovingForward) {
                bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);

                if (bobXPosition > getWidth() && bgCount != 4) {
                    //Increment bgCount if bob has traversed initial background completely
                    bgCount++;
                    setBobYPosition();
                    bobXPosition = 0;
                } else if (bobXPosition >= (getWidth()- 300) && (bgCount==4)) {
                    bobXPosition = getWidth()- 300;

                }

                // If bob is moving backward (the player is touching the screen at reverse button location)
                // then move him to the left based on his target speed and the current fps
            } else if (isMovingBackward) {
                bobXPosition = bobXPosition - (walkSpeedPerSecond / fps);

                // If bob tries to go beyond the screen from the left
                // Reset bob position to start of screen
                if (bgCount == 0 && bobXPosition < screenX / (float) 8.5) {
                    bobXPosition = screenX / (float) 8.5;
                } else if (bobXPosition < 0) {
                    bobXPosition = 0;
                }

                // If jump button pressed
            } else if (isJumping) {
                // decrement bobYPosition and increment bobXPosition for a stable jump
                bobYPosition = bobYPosition - (walkSpeedPerSecond / (fps + 5));
                bobXPosition = bobXPosition + 10;

                // Thread.sleep() used to add delay in the jump function
                // If bob reaches the jump height, then increment his y value after some delay
                if (bobYPosition < (getInitialY() - screenY / 6)) {
                    try {
                        //set time in mili
                        Thread.sleep(100);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // final landing x position greater than starting x position
                    bobXPosition = bobXPosition + 10;
                    bobYPosition = getInitialY();
                }

                //To move onto next level
                if (bobXPosition > getWidth() && bgCount != 4) {
                    //increment bgCount if bob has traversed initial background completely
                    bgCount++;
                    bobXPosition = 0;
                } else if (bobXPosition >= (getWidth()- 100) && (bgCount==4)) {
                    //In level 5 don't allow Bob to exceed screen width
                    bobXPosition = getWidth()- 100;
                }

                // If crouch button pressed
            } else if (isCrouching) {
                // Decrease frameHeight to create crouching effect
                // Increment bob y position so he doesn't seem to float on the screen
                if (frameHeight > 60) {
                    frameHeight -= 10;
                    bobYPosition += 10;
                }
            }
            // Call position set to set Bob's x and y values
            positionSet();
        }//end method Update()

        // Retrieves the current frame of the sprite sheet
        public void getCurrentFrame(){

            long time  = System.currentTimeMillis();
            if(isMovingForward) {// Only animate if bob is moving forward
                if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                    lastFrameChangeTime = time;
                    currentFrame++;
                    if (currentFrame >= frameCount) {
                        currentFrame = 0;
                    }
                }
            }

            else if(isMovingBackward) {// Only animate if bob is moving backward
                if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                    lastFrameChangeTime = time;
                    currentFrame++;
                    if (currentFrame >= frameCount) {
                        currentFrame = 0;
                    }
                }
            }

            else if (isJumping) {// Only animate if bob is jumping
                if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
                    lastFrameChangeTime = time;
                    currentFrame++;
                    if (currentFrame >= frameCount) {
                        currentFrame = 0;
                    }
                }
            }

            //update the left and right values of the source of
            //the next frame on the sprite sheet
            frameToDraw.left = currentFrame * frameWidth;
            frameToDraw.right = frameToDraw.left + frameWidth;
        }

        // Draw the newly updated scene
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                //Draw background
                drawBackground();

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  249, 129, 0));

                // Make the text a bit bigger
                paint.setTextSize(45);

                //Draw buttons
                whereToDrawFwd.set(fwdXPosition,btnYPosition, fwdXPosition+btnWidth, btnYPosition+btnHeight);
                whereToDrawBack.set(backXPosition,btnYPosition, backXPosition+btnWidth, btnYPosition+btnHeight);
                whereToDrawDown.set(downXPosition,btnYPosition, downXPosition+btnWidth, btnYPosition+btnHeight);
                whereToDrawUp.set(upXPosition,btnYPosition, upXPosition+btnWidth, btnYPosition+btnHeight);
                drawButtons();

                // Draw bob in accordance with bobXPosition and bobYPosition
                whereToDraw.set((int) bobXPosition, (int) bobYPosition, (int) bobXPosition + frameWidth, (int) bobYPosition + frameHeight);
                getCurrentFrame();
                canvas.drawBitmap(bitmapBob, frameToDraw, whereToDraw, paint);

                // Drawing on the canvas when bob is moving forward
                if (isMovingForward) {
                    // Draw background by calling drawBackground() method
                    drawBackground();
                    canvas.drawBitmap(bitmapBob, frameToDraw, whereToDraw, paint);
                    // Draw buttons by calling drawButtons() method
                    drawButtons();
                    if (bgCount < 5) {
                        canvas.drawBitmap(bitmapFwdPressed, null, whereToDrawFwd, paint);
                    }
                }

                // Drawing on the canvas when bob is moving backward
                else if(isMovingBackward) {
                    //Draw background
                    drawBackground();
                    canvas.drawBitmap(bitmapBobBack, frameToDraw, whereToDraw, paint);
                    drawButtons();
                    if (bgCount < 5) {
                        canvas.drawBitmap(bitmapBackPressed, null, whereToDrawBack, paint);
                    }
                }

                // Drawing on the canvas when bob is jumping
                else if (isJumping) {
                    //Draw background
                    drawBackground();
                    canvas.drawBitmap(bitmapBob, frameToDraw, whereToDraw, paint);
                    drawButtons();
                    if (bgCount < 5) {
                        canvas.drawBitmap(bitmapUpPressed, null, whereToDrawUp, paint);
                    }
                }

                // Drawing on the canvas when bob is crouching
                else if (isCrouching) {
                    //Draw background
                    drawBackground();
                    canvas.drawBitmap(bitmapBob, frameToDraw, whereToDraw, paint);
                    drawButtons();
                    if (bgCount < 5) {
                        canvas.drawBitmap(bitmapDownPressed, null, whereToDrawDown, paint);
                    }
                }


                // Draw obstacles on the screen in accordance with each level
                switch (bgCount) {
                    case 0:

                        // Three spikes drawn for level 1
                        bitmapSpike = Bitmap.createScaledBitmap(bitmapSpike, 100, (int) spike1Height, false);
                        canvas.drawBitmap(bitmapSpike, spike1XPosition, spike1YPosition, paint);

                        bitmapSpike = Bitmap.createScaledBitmap(bitmapSpike, 100, (int) spike2Height, false);
                        canvas.drawBitmap(bitmapSpike, spike2XPosition, spike1YPosition, paint);

                        bitmapSpike2 = Bitmap.createScaledBitmap(bitmapSpike2, 100, (int) spike3Height, false);
                        canvas.drawBitmap(bitmapSpike2, spike3XPosition, spike3YPosition, paint);
                        break;

                    case 1:
                        // Two spikes of level 2
                        // One moving and one static
                        if (bobXPosition > (spike4XPosition - 200)) {
                            bitmapSpikeInvert = Bitmap.createScaledBitmap(bitmapSpikeInvert, 90, 80, false);
                            canvas.drawBitmap(bitmapSpikeInvert, spike4XPosition, spike4YPosition, paint);
                        }
                        if (bobXPosition >= (movingSpikeX - 200)) {
                            bitmapSpikeInvert = Bitmap.createScaledBitmap(bitmapSpikeInvert, 90, 80, false);
                            canvas.drawBitmap(bitmapSpikeInvert, movingSpikeX, spike4YPosition , paint);
                        }
                        break;

                    case 2:
                        // Rolling ball of level 3
                        bitmapball = Bitmap.createScaledBitmap(bitmapball, 350, 350, false);
                        canvas.drawBitmap(bitmapball, ballXPosition, ballYPosition, paint);
                        break;

                    case 3:
                        // To draw fire in level 4
                        if (!reverse ) {
                            bitmapFire = Bitmap.createScaledBitmap(bitmapFire, 250, 250, false);
                            canvas.drawBitmap(bitmapFire, fireXPosition, fireYPosition , paint);
                        } else {
                            bitmapFireInvert = Bitmap.createScaledBitmap(bitmapFireInvert, 250, 250, false);
                            canvas.drawBitmap(bitmapFireInvert, fireXPosition, fireYPosition , paint);
                        }//end else to draw reverse fire

                        // To draw red spikes
                        bitmapRed = Bitmap.createScaledBitmap(bitmapRed, 80, (int) red1Height, false);
                        canvas.drawBitmap(bitmapRed, red1XPosition, red1YPosition, paint);
                        bitmapRed = Bitmap.createScaledBitmap(bitmapRed, 90, (int) red2Height, false);
                        canvas.drawBitmap(bitmapRed, red2XPosition, red2YPosition, paint);

                        bitmapRed = Bitmap.createScaledBitmap(bitmapRed, 80, (int) red1Height, false);
                        canvas.drawBitmap(bitmapRed, red3XPosition, red1YPosition, paint);
                        bitmapRed = Bitmap.createScaledBitmap(bitmapRed, 80, (int) red2Height, false);
                        canvas.drawBitmap(bitmapRed, red4XPosition, red2YPosition, paint);
                        bitmapRed = Bitmap.createScaledBitmap(bitmapRed, 80, (int) red3Height, false);
                        canvas.drawBitmap(bitmapRed, red5XPosition, red3YPosition, paint);
                        drawButtons();
                        break;

                    case 4:
                        // To draw fire in level 5
                        bitmapFireInvert = Bitmap.createScaledBitmap(bitmapFireInvert, 100, 100, false);
                        canvas.drawBitmap(bitmapFireInvert, fireBg4XPosition, fire1Bg4YPosition , paint);

                        bitmapFireInvert = Bitmap.createScaledBitmap(bitmapFireInvert, 100, 100, false);
                        canvas.drawBitmap(bitmapFireInvert, fireBg4XPosition, fire2Bg4YPosition, paint);

                        // To draw brick in level 5
                        bitmapBrick = Bitmap.createScaledBitmap(bitmapBrick, 370, 100, false);
                        canvas.drawBitmap(bitmapBrick, brickXPosition, brickYPosition, paint);
                        drawButtons();
                        break;


                }

                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }


        // Set bobPosition in level 3,4,5
        // SAME bobPosition in level 1 and 2
        public void setBobYPosition(){
            switch(bgCount){
                case 2:
                    bobYPosition = screenY /(float) 3 ;
                    setInitialY(screenY /(float) 3);
                    break;
                case 3:
                    bobYPosition = screenY /(float) 2.85 ;
                    setInitialY(screenY /(float) 2.85);
                    break;
                case 4:
                    bobYPosition = screenY /(float) 1.15 ;
                    setInitialY(screenY /(float) 1.15);
                    break;
            }
        }//end method setBobYPosition()

        // To draw background in accordance with the current level
        public void drawBackground(){

            // Compare bgCount to 0,1,2,3,4 and 5 to check for the current level
            // drawBitmap() method of canvas used to draw the respective background bitmaps
            // Parameters: bitmap, x axis starting point, y axis starting point, Paint class instance
            // The Paint class holds the style and color information about how to draw geometries, text and bitmaps
            switch(bgCount) {
                case 0:
                    canvas.drawBitmap(bitmapBg, 0, 0, paint);
                    break;
                case 1:

                    canvas.drawBitmap(bitmapBg2, 0, 0, paint);
                    break;
                case 2:

                    canvas.drawBitmap(bitmapBg3, 0, 0, paint);
                    break;
                case 3:
                    canvas.drawBitmap(bitmapBg4, 0, 0, paint);
                    break;
                case 4:
                    canvas.drawBitmap(bitmapBg5, 0, 0, paint);
                    break;
                case 5:
                    bobYPosition = screenY;  //to make bob disappear from game won screen
                    canvas.drawBitmap(bitmapWin, 0, 0, paint);
                    playing = false;
                    break;
                case 6:
                    bobYPosition = screenY; //to make bob disappear from game over screen
                    canvas.drawBitmap(bitmapBgEnd, 0, 0, paint);
                    playing = false;
                    break;
            }

        }//end drawBackground() method

        // To draw buttons on screen
        // drawBitmap() method of canvas used to draw the respective button bitmaps
        public void drawButtons(){
            if (bgCount < 5 ) {
                canvas.drawBitmap(bitmapBack, null, whereToDrawBack, paint);
                canvas.drawBitmap(bitmapFwd, null, whereToDrawFwd, paint);
                canvas.drawBitmap(bitmapDown, null, whereToDrawDown, paint);
                canvas.drawBitmap(bitmapUp, null, whereToDrawUp, paint);
            }
        }//end drawButtons() method

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }


        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            // int x: to get screen touch x position
            int x = (int) motionEvent.getX();
            // int y: to get screen touch y position
            int y = (int) motionEvent.getY();

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    // To see if forward button has been pressed by user
                    // If true, then forward functionality executed
                    if (whereToDrawFwd.contains(x, y)) {
                        isMovingForward = true;
                        isMovingBackward = false;

                    } else if (whereToDrawBack.contains(x, y)) {
                        // To see if reverse button has been pressed by user
                        // If true, then back functionality executed
                        isMovingBackward = true;
                        isMovingForward = false;

                    } else if (whereToDrawUp.contains(x, y)) {
                        // To see if jump button has been pressed by user
                        // If true, then jump functionality executed
                        isJumping = true;
                        isMovingForward = false;
                        isMovingBackward = false;

                    } else if (whereToDrawDown.contains(x, y)) {
                        // To see if crouch button has been pressed by user
                        // If true, then crouch functionality executed
                        isCrouching = true;
                        isMovingForward = false;
                        isMovingBackward = false;
                        isJumping = false;
                    }
                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    // Set all movement variables to false so Bob does not move
                    isMovingForward = false;
                    isMovingBackward = false;
                    isJumping = false;
                    isCrouching = false;

                    if (bgCount == 4 && bobXPosition > screenX/1.5) {
                        if (frameHeight < 100  ) {
                            bobYPosition = brickYPosition - 110;
                            frameHeight = 100; // reset frameHeight as it is changed in crouch function
                        }
                       /* if (!isJumping ){
                            isJumping = false;
                            bobYPosition = brickYPosition - 110;
                        }*/

                    } else{
                        isJumping = false;
                        frameHeight = 100; // reset frameHeight as it is changed in crouch function
                        bobYPosition = getInitialY();
                    }


                    break;
            }
            return true;

        }//end method onTouchEvent()


    } // This is the end of our GameView inner class

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        gameView.pause();
    }

}//end of class SimpleGameEngine

