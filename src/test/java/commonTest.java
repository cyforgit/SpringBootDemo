import org.junit.Test;

public class commonTest {


    @Test
    public void T1() {
        Resource resource = new Resource();
        Add_Thread add_thread = new Add_Thread(resource);
        sub_Thread sub_thread = new sub_Thread(resource);
        new Thread(add_thread, "加线程 A").start();
        new Thread(add_thread, "加线程 B").start();
        new Thread(sub_thread, "减线程 X").start();
        new Thread(sub_thread, "减线程 Y").start();
    }
}

class Add_Thread implements Runnable {
    private Resource resource;

    public Add_Thread(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 10; i++) {
                resource.add();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class sub_Thread implements Runnable {
    private Resource resource;

    public sub_Thread(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                resource.sub();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Resource {
    private int count = 0;
    private boolean flag = true;//false = sub true = add

    public synchronized void add() throws InterruptedException {
        //当前应该执行减法操作，线程进行等待
        if (!flag) {
            super.wait();
        }
        Thread.sleep(100);

        this.count++;    //当去掉this的时候，结果变了。 个人感觉，应该是加了锁的原因，如果不加锁，则count是公共变量，加入后当前count只能被一个线程所操作
        System.out.println("加法操作：" + Thread.currentThread().getName() + "," + (count));
        flag = false;
        super.notifyAll();
    }

    public synchronized void sub() throws InterruptedException {
        //当前应该执行加法操作，线程进行等待
        if (flag) {
            super.wait();
        }
        Thread.sleep(200);
        this.count--;    //当去掉this的时候，结果变了。 个人感觉，应该是加了锁的原因，如果不加锁，则count是公共变量，加入后当前count只能被一个线程所操作
        System.out.println("减法操作：" + Thread.currentThread().getName() + "," + (count));
        flag = true;
        super.notifyAll();
    }
}
