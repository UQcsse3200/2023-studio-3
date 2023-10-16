package com.csse3200.game.screens;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.MainGameScreen;
//TODO make the loading animation fit in with all aspects
//public class LoadingScreen {
// Object monitor = new Object();
//GdxGame game;
//TurretSelectionScreen turretSelectionScreen;
//public void Loading(GdxGame game, TurretSelectionScreen turretSelectionScreen) {
// Create a shared monitor object
//  this.game=game;
//this.turretSelectionScreen = turretSelectionScreen;
// First thread
//Thread thread1 = new Thread(new Runnable() {
// @Override
//  public void run() {
//    synchronized (monitor) {
//      try {
//        turretSelectionScreen.toggleLoadingCircle(true);
//      monitor.wait(); // Wait for a signal from thread 2
//} catch (InterruptedException e) {
//             e.printStackTrace();
//}

//       game.setScreen(GdxGame.ScreenType.MAIN_GAME);
//    }
//}
//});

// Second thread
//Thread thread2 = new Thread(new Runnable() {
//  @Override
//public void run() {
//System.out.println("Thread 2 is running.");
//  try {
// Simulate some work
//  Thread.sleep(2000);
//} catch (InterruptedException e) {
//  e.printStackTrace();
//}
//synchronized (monitor) {
//  MainGameScreen.loadAssets();
//monitor.notify(); // Notify thread 1 that it can proceed
//     }
//  }
//});

// Start both threads
//thread1.start();
//thread2.start();//
// }
//}