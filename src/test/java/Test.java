public class Test {
	public static void main(String[] args) {
		RuntimeException runtimeException = new RuntimeException("");
		new RuntimeException(runtimeException);
	}
}
