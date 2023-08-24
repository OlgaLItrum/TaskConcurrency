package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class FileReader {

  private final Lock writeLock;
  private final File file;

  public FileReader(Lock writeLock, File file) {
    this.writeLock = writeLock;
    this.file = file;
  }

  public int[] read() {
    System.out.println("Reader start " + Thread.currentThread());
    StringBuilder builder = new StringBuilder();
    while (true) {
      if (writeLock.tryLock()) {
        try {
          System.out.println("Reading " + Thread.currentThread());
          try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file))) {
            if (bufferedReader.ready()) {
              String next;
              while ((next = bufferedReader.readLine()) != null) {
                builder.append(next);
              }
              return Arrays.stream(builder.toString().split(",")).mapToInt(Integer::parseInt).toArray();
            }
          }
        } catch (IOException e) {
          System.out.println("Read file exception: " + e.getMessage());
        } finally {
          writeLock.unlock();
          System.out.println("Reader end " + Thread.currentThread());
        }
      } else {
        System.out.println("Wait " + Thread.currentThread());
      }
    }
  }
}
