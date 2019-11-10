package threadtest;

public class SpringThread extends Thread{

	
	 private int parameter;

	    public SpringThread(int parameter){
	        this.parameter = parameter;
	    }
	    @Override
	    public void run() {
	        System.out.println(Thread.currentThread().getName() + ":执行了..." + parameter);
	        synchronized (Thread.currentThread()) {
	        	if(parameter==2){
	        		try {
	        			Thread.currentThread().wait(10000);
	        		} catch (InterruptedException e) {
	        			e.printStackTrace();
	        		}
	        	}else{
	        		for (int i = 0; i < 5; i++) {
						System.out.println( parameter+"线程  运行中");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
	        	}
			}
	        System.out.println(Thread.currentThread().getName() + " 结束..." + parameter);
	    }
	    
}
