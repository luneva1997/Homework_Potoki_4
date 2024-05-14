import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    private final char letter;
    private final AtomicInteger max;
    private final ArrayBlockingQueue<String> queue;

    public MyThread(char letter, AtomicInteger max, ArrayBlockingQueue<String> queue){
        this.letter = letter;
        this.max = max;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            resolveTask(letter, max, queue);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resolveTask (char letter, AtomicInteger max, ArrayBlockingQueue<String> queue) throws InterruptedException {
        char[] textMassive = queue.take().toCharArray();
        int sum = 0;
        for (char c : textMassive) {
            if (c == letter) {
                sum += 1;
            }
        }
        if (sum > max.get()) {
            max.set(sum);
        }
    }
}
