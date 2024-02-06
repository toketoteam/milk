의존성

(1) (Spring Boot내부)

	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'

	//validation의존성 추가

	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	//머스태치 => setting -> pluglns -> 검색창 : mustache 검색 후 수염모양 다운로드 받아야함
	implementation('org.springframework.boot:spring-boot-starter-mustache')

	implementation 'org.springframework.boot:spring-boot-starter-security'
	//ㄴ11/29/15:20
	implementation 'commons-io:commons-io:2.6'
	//ㄴ12/04/13:50
//	implementation 'org.springframework.boot:spring-boot-starter-security' /* security */
//	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
	// Spring Data JPA
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

	implementation 'javax.servlet:javax.servlet-api:4.0.1'

(2) JDK17-Gradle JVM: java "17.0.9" 필요
