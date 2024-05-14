import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    public static Thread thread;
    public static int texts = 10_000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AtomicInteger maxA = new AtomicInteger(0);
        AtomicInteger maxB = new AtomicInteger(0);
        AtomicInteger maxC = new AtomicInteger(0);

        thread = (new Thread(() ->
        {
            for (int i = 0; i < texts; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        thread.start();

        ConcurrentLinkedQueue<Thread> threadList = new ConcurrentLinkedQueue<>();

        threadList.add(new MyThread('a', maxA, queue1));
        threadList.add(new MyThread('b', maxB, queue2));
        threadList.add(new MyThread('c', maxC, queue3));

        for (Thread thread: threadList) {
            thread.start();
        }

        for (Thread thread: threadList) {
            thread.join();
        }

        System.out.println("Максимальное количество букв 'а' в одном тексте: " + maxA.get());
        System.out.println("Максимальное количество букв 'b' в одном тексте: " + maxB.get());
        System.out.println("Максимальное количество букв 'c' в одном тексте: " + maxC.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }

        return text.toString();
    }
}
