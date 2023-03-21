package selenium;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.awt.Toolkit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
	public static void main(String[] args) throws IOException {
		
    	

    	String str = Files.readString(Paths.get("f:\\merged.txt"));
    	str = str.replaceAll(System.getProperty("line.separator"), "\r");
    	int s_len = str.length();
    	System.out.println("문자열길이 : " + s_len);
    	
    	// 문자열을 배열로 전환
    	char[] arr = str.toCharArray();    	
    	int a_len = arr.length;
    	System.out.println("배열길이 : " + a_len);
    	
    	int countT=0;
    	int limit_len = 4999; //제한 범위
    	int be_point = 0; //배열 자른 이전 범위
    	int cutPoint = 4999; // 잘라야되는 다음 배열 끝 범위
    	int map_num=0;
    	
    	//잘라낸 문자열을 저장하기 위한 해시맵 선언
    	HashMap<Integer, String> myHashMap = new HashMap<Integer, String>();
    	
    	// 제한 글자보다 적은 양의 txt 파일일 경우
    	if(arr.length <limit_len) {
    		//배열 내용 한번에 꺼내기
    		String strtoken = new String(arr);
    		myHashMap.put(map_num, strtoken);
    	}
    	
    	// 제한 글자보다 많은 양의 txt 파일일 경우
    	else {
    		do {
    			while(true) {
    				// 잘라야하는 범위값의 내용물이 줄 바꿈인 경우
    				if(arr[cutPoint] == '\r') {
    					//배열 내용 나눠서 꺼내기
    					char[] arr1 = Arrays.copyOfRange(arr, be_point, cutPoint);
    					String strtoken = new String(arr1);
    					myHashMap.put(map_num, strtoken);
    					map_num++;
    					
    					be_point = cutPoint;
    					cutPoint = cutPoint + limit_len;
        				break;
        			}
    				// 줄바꿈이 아니면 문장의 끝이 아님으로 거슬러 올라가서 문장의 끝을 찾음
        			else{
        				cutPoint--;
        			}
    			}
    		} while(cutPoint < arr.length);  //최종 잘라야하는 범위가 배열 길이를 초과하면 일단 종료 
    		
    		//범위안에 남은 마지막 배열 내용 꺼내기
        	char[] arr1 = Arrays.copyOfRange(arr, be_point, arr.length);
        	String strtoken = new String(arr1);
    		myHashMap.put(map_num, strtoken);
    	}
    	
    	
    	System.out.println(myHashMap.size()+"\n");
    

		
		System.out.println(System.getProperty("user.dir"));

		Path path = Paths.get(System.getProperty("user.dir"), "src/selenium/resources/chromedriver.exe");

		System.setProperty("webdriver.chrome.driver", path.toString());

		
		//크롬 옵션들
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-default-apps");
		//options.addArguments("headless");
		options.addArguments("disable-gpu");
		options.addArguments("disable-infobars");
		options.addArguments("--disable-extensions");
		options.addArguments("--incognito");
		

		ChromeDriver driver = new ChromeDriver(options);
		//WebElement webElement1;
		WebElement webElement2;
		String sendText = null;
		String revText="";
		WebDriverWait wait=new WebDriverWait(driver, 20);
		List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		
		
		// 첫번째 탭
		driver.switchTo().window(tabs.get(0));
		//파파고 페이지 로드
		driver.get("https://papago.naver.com/");
		
		
	
		
		for( Integer key : myHashMap.keySet() ){
			sendText=myHashMap.get(key);
			sendText = sendText.replaceAll("\r", System.getProperty("line.separator"));
			
			
			StringSelection data = new StringSelection(sendText);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
			driver.findElement(By.id("txtSource")).click();
			Actions actions = new Actions(driver);
			actions.keyDown(Keys.LEFT_CONTROL).sendKeys("v").perform();
			
			
			/* webElement1 = driver.findElement(By.id("txtSource"));
			webElement1.sendKeys(sendText); */
			
			

			wait.until(ExpectedConditions.elementToBeClickable(By.className("text_edit_area___3o8uj")));
			
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			webElement2 = driver.findElement(By.id("txtTarget"));
			//System.out.println(webElement2.getText());
			revText = revText + webElement2.getText();
			
			System.out.println(countT++);
			//driver.findElement(By.id("txtSource")).clear();
			
			driver.findElement(By.cssSelector("#sourceEditArea > button")).click();
			
			
		} 
			 

		
		try {
			//드라이버가 null이 아니라면
			if(driver != null) {
				//드라이버 연결 종료
				driver.close(); //드라이버 연결 해제
				
				//프로세스 종료
				driver.quit();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		

        
		String fileName ="example.txt";
        String FullfileName = "f:\\example\\" + fileName;
        
        try {
   		 
            // 1. 파일 객체 생성
    	    //가변성 없을 때 하던 흔적
            //File file = new File("f:\\example\\writeFile.txt");
    	    
    		File file = new File(FullfileName);
 
            // 2. 파일 존재여부 체크 및 생성
            if (!file.exists()) {
                file.createNewFile();
            }
 
            // 3. Buffer를 사용해서 File에 write할 수 있는 BufferedWriter 생성
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            
           
            // 4. 파일에 쓰기
            
            writer.write(revText);
            // 5. BufferedWriter close
            writer.close();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
