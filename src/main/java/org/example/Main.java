package org.example;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

  public static void main(String[] args) {
    File file = new File("ArrayFile.txt");
    Lock writeLock = new ReentrantLock();

    CompletableFuture<int[]> futureReadFile = CompletableFuture.supplyAsync(() ->
      new FileReader(writeLock, file).read());

    AsyncArrayCalculation calculation = new AsyncArrayCalculation(futureReadFile);
    try {
      List<FutureValue> futureResult = calculation.getCombinedFutures().get();
      for (FutureValue value : futureResult) {
        System.out.println(value);
      }
    } catch (InterruptedException | ExecutionException e) {
      System.out.println(e.getMessage());
    }
  }
}