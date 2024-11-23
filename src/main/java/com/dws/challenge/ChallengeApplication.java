package com.dws.challenge;

import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.repository.AccountsRepositoryInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

}
