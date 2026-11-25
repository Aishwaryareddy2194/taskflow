package com.taskflow.web;

import com.taskflow.core.model.Task;
import com.taskflow.core.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.taskflow")
@EntityScan(basePackageClasses = Task.class)
@EnableJpaRepositories(basePackageClasses = TaskRepository.class)
public class TaskFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskFlowApplication.class, args);
    }

    @Bean
    CommandLineRunner loadSampleTasks(TaskRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Task("Explore the TaskFlow dashboard",
                        "Review the dashboard and understand the task workflow."));
                repository.save(new Task("Configure the CI pipeline",
                        "Build, test and package the application with Jenkins."));
                repository.save(new Task("Deploy to AWS EC2",
                        "Deploy the packaged application to an EC2 instance."));
            }
        };
    }
}
