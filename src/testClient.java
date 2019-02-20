public class testClient {
  public static void main(String args[]){
   MockClient client = new MockClient("localhost", 8888);
   client.downloadFile("zip.zip");
  }
}
