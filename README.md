DeepSeek Social Integration
Java application that helps user be constantly online in social network "Vk"  
  
🔹 Gathering data from browser, getting it from Vk  
🔹 Sending and processing data with DeepSeek  
🔹 Returning the result to the social network  

📌 Contents  
-Functions  
-Techonlogies  
-Setup  
-Ideas  
-License  

🔹 Functions  
 Automatically gathering data from Chrome browser  
 Sending the data to DeepSeek and getting an answer  
 Integration with Vk - sending appropriate messages 

⚙️ Technologies  
Java 17+ (programming language)  
Selenium (browser parser)  
OkHttp (HTTP-requests to DeepSeek)  
Gradle (build system)  

🚀 Setup  

Clone repo:  
bash  
git clone https://github.com/ADefaultDev/DummyVkBot  
cd deepseek-social-integration  

Make sure to have following dependencies:  
JDK 17+  
Gradle  
ChromeDriver  

How to start:  
bash  
mvn clean install     
mvn exec:java -Dexec.mainClass="com.yourpackage.Main"    
 
📅 Ideas  
Integration with different social networks  
Extension for Chrome/Firefox.  
GUI  

📜 License  
This project is using MIT license.  
