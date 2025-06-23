plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {

    implementation("org.seleniumhq.selenium:selenium-java:4.15.0")
    implementation("io.github.bonigarcia:webdrivermanager:6.1.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}